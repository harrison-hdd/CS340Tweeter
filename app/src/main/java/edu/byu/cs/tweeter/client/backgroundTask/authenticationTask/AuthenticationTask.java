package edu.byu.cs.tweeter.client.backgroundTask.authenticationTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticationTask extends BackgroundTask {
    private final String LOG_TAG;

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    protected String username;
    protected String password;

    protected AuthenticationTask(Handler messageHandler, String username, String password, String LOG_TAG){
        super(messageHandler);
        this.username = username;
        this.password = password;
        this.LOG_TAG = LOG_TAG;
    }

    @Override
    public void run(){
        try {
            Pair<User, AuthToken> result = doTask();

            User loggedInUser = result.getFirst();
            AuthToken authToken = result.getSecond();

            sendSuccessMessage(loggedInUser, authToken);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected void sendSuccessMessage(User loggedInUser, AuthToken authToken) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, loggedInUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected abstract Pair<User, AuthToken> doTask();

}
