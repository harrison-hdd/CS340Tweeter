package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {
    private static final String LOG_TAG = "IsFollowerTask";

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;



    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    public void run() {
        try {

            sendSuccessMessage(new Random().nextInt() > 0);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private void sendSuccessMessage(boolean isFollower) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

}
