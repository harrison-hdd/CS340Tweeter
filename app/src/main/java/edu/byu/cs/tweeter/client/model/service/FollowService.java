package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.UnfollowTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface IsFollowerObserver{
        void handleIsFollowerSuccess(boolean isFollower);
        void handleIsFollowerFailure(String message);
    }

    public interface FollowingAndFollowerCountObserver{
        void handleFollowingCountSuccess(int numFollowing);
        void handleFollowingCountFailure(String message);
        void handleFollowerCountSuccess(int numFollowers);
        void handleFollowerCountFailure(String message);
    }

    public interface FollowObserver{
        void handleFollowSuccess();
        void handleFollowFailure(String message);
    }

    public interface UnfollowObserver{
        void handleUnfollowSuccess();
        void handleUnfollowFailure(String message);
    }



    public void getFollowing(User user, User lastFollowee, int pageSize, PagedServiceObserver<User> getFollowingObserver){
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(getFollowingObserver));

        BackgroundTaskUtils.runTask(getFollowingTask);

    }

    public void getFollowers(User user, User lastFollower, int pageSize, PagedServiceObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowersObserver));

        BackgroundTaskUtils.runTask(getFollowersTask);
    }


    public void isFollower(User selectedUser, IsFollowerObserver isFollowerObserver){
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(isFollowerObserver));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public void followingAndFollowerCount(User selectedUser, FollowingAndFollowerCountObserver followingAndFollowerCountObserver){


        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(followingAndFollowerCountObserver));


        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(followingAndFollowerCountObserver));

        BackgroundTaskUtils.runTask(followersCountTask);
        BackgroundTaskUtils.runTask(followingCountTask);
    }

    public void follow(User selectedUser, FollowObserver followObserver){
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(followObserver));
        BackgroundTaskUtils.runTask(followTask);
    }

    public void unfollow(User selectedUser, UnfollowObserver unfollowObserver){
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(unfollowObserver));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    private class IsFollowerHandler extends Handler {//service
        private final IsFollowerObserver isFollowerObserver;

        public IsFollowerHandler(IsFollowerObserver isFollowerObserver){
            super(Looper.getMainLooper());
            this.isFollowerObserver = isFollowerObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
                isFollowerObserver.handleIsFollowerSuccess(isFollower);

            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                isFollowerObserver.handleIsFollowerFailure("Failed to determine following relationship: " + message);
            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                isFollowerObserver.handleIsFollowerFailure("Failed to determine following relationship because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetFollowersCountHandler extends Handler {//service
        private final FollowingAndFollowerCountObserver followingAndFollowerCountObserver;

        public GetFollowersCountHandler(FollowingAndFollowerCountObserver observer){
            super(Looper.getMainLooper());
            this.followingAndFollowerCountObserver = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                followingAndFollowerCountObserver.handleFollowerCountSuccess(count);
            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                followingAndFollowerCountObserver.handleFollowerCountFailure("Failed to get followers count: " + message);
            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                followingAndFollowerCountObserver.handleFollowerCountFailure(
                        "Failed to get followers count because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetFollowingCountHandler extends Handler {//service
        private final FollowingAndFollowerCountObserver followingAndFollowerCountObserver;

        public GetFollowingCountHandler(FollowingAndFollowerCountObserver observer){
            super(Looper.getMainLooper());
            this.followingAndFollowerCountObserver = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                followingAndFollowerCountObserver.handleFollowingCountSuccess(count);
            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                followingAndFollowerCountObserver.handleFollowingCountFailure("Failed to get following count: " + message);
            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                followingAndFollowerCountObserver.handleFollowingCountFailure("Failed to get following count due to exception: " + ex.getMessage());
            }
        }
    }

    private class FollowHandler extends Handler {
        private final FollowObserver followObserver;

        public FollowHandler(FollowObserver followObserver){
            super(Looper.getMainLooper());
            this.followObserver = followObserver;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                followObserver.handleFollowSuccess();
            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                followObserver.handleFollowFailure("Failed to follow: " + message);
            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                followObserver.handleFollowFailure("Failed to follow because of exception: " + ex.getMessage());
            }

        }
    }

    private class UnfollowHandler extends Handler {//service
        private final UnfollowObserver unfollowObserver;

        public UnfollowHandler(UnfollowObserver unfollowObserver){
            super(Looper.getMainLooper());
            this.unfollowObserver = unfollowObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                unfollowObserver.handleUnfollowSuccess();
            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                unfollowObserver.handleUnfollowFailure("Failed to unfollow: " + message);
            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                unfollowObserver.handleUnfollowFailure("Failed to unfollow because of exception: " + ex.getMessage());
            }

        }
    }
}
