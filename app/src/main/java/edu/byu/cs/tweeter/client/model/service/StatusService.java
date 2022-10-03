package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public interface PostStatusObserver{
        void handlePostSuccess();
        void handlePostFailure(String message);
    }

    public void getFeed(User user, Status lastStatus, int pageSize, PagedServiceObserver<Status> pagedServiceObserver){

        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetFeedHandler(pagedServiceObserver));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void getStory(User user, Status lastStatus, int pageSize, PagedServiceObserver<Status> pagedServiceObserver){
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetStoryHandler(pagedServiceObserver));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void postStatus(Status newStatus, PostStatusObserver postStatusObserver){
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(postStatusObserver));
        BackgroundTaskUtils.runTask(statusTask);
    }

    private class PostStatusHandler extends Handler {//service
        private final PostStatusObserver postStatusObserver;

        public PostStatusHandler(PostStatusObserver postStatusObserver){
            super(Looper.getMainLooper());
            this.postStatusObserver = postStatusObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) {
                postStatusObserver.handlePostSuccess();
            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
                postStatusObserver.handlePostFailure("Failed to post status: " + message);
            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
                postStatusObserver.handlePostFailure("Failed to post status because of exception: " + ex.getMessage());
            }
        }
    }
}
