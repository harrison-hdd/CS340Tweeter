package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.PostStatusTask;
import edu.byu.cs.tweeter.client.cache.Cache;
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

    private class GetFeedHandler extends Handler {
        private final PagedServiceObserver<Status> pagedServiceObserver;

        public GetFeedHandler(PagedServiceObserver<Status> pagedServiceObserver){
            super(Looper.getMainLooper());
            this.pagedServiceObserver = pagedServiceObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);

                pagedServiceObserver.handleSuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                pagedServiceObserver.handleFailure("Failed to get feed: " + message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                pagedServiceObserver.handleFailure("Failed to get feed because of exception: " + ex.getMessage());
            }
        }
    }

    private class GetStoryHandler extends Handler {

        private final PagedServiceObserver<Status> pageServiceObserver;

        public GetStoryHandler(PagedServiceObserver<Status> pagedServiceObserver){
            this.pageServiceObserver = pagedServiceObserver;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);

                pageServiceObserver.handleSuccess(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                pageServiceObserver.handleFailure("Failed to get story: " + message);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                pageServiceObserver.handleFailure("Failed to get story because of exception: " + ex.getMessage());
            }
        }
    }

}
