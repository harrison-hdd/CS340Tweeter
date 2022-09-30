package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String LOG_TAG = "AuthenticatedTask";
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose story is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Maximum number of statuses to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;

    protected PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    @Override
    public void run() {
        try {
            Pair<List<T>, Boolean> pageOfItems = getData();

            List<T> items = pageOfItems.getFirst();
            boolean hasMorePages = pageOfItems.getSecond();

            sendSuccessMessage(items, hasMorePages);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract Pair<List<T>, Boolean> getData();

    private void sendSuccessMessage(List<T> statuses, boolean hasMorePages) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) statuses);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
