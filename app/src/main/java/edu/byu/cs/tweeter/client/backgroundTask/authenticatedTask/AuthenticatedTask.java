package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Tasks that need an authToken
 */
public abstract class AuthenticatedTask extends BackgroundTask {

    protected AuthToken authToken;


    protected AuthenticatedTask(Handler messageHandler, AuthToken authToken){
        super(messageHandler);
        this.authToken = authToken;
    }
}
