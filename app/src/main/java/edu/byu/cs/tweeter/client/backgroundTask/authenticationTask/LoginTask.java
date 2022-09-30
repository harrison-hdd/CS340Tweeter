package edu.byu.cs.tweeter.client.backgroundTask.authenticationTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.backgroundTask.authenticationTask.AuthenticationTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticationTask {

    private static final String LOG_TAG = "LoginTask";




    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password, LOG_TAG);
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

    @Override
    protected Pair<User, AuthToken> doTask() {
        User loggedInUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }
}
