package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    public static final String COUNT_KEY = "count";


    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;


    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    public void run() {
        try {

            sendSuccessMessage(20);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private void sendSuccessMessage(int count) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putInt(COUNT_KEY, count);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

}
