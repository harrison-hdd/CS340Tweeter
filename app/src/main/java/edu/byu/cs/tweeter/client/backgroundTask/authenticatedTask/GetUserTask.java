package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";

    public static final String USER_KEY = "user";



    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;


    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;

    }

    @Override
    public void run() {
        try {
            User user = getUser();

            sendSuccessMessage(user);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private User getUser() {
        User user = getFakeData().findUserByAlias(alias);
        return user;
    }

    private void sendSuccessMessage(User user) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, user);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
