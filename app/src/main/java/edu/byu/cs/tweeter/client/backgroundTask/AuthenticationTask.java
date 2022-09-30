package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

public abstract class AuthenticationTask extends BackgroundTask{
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private String username;
    private String password;

    protected AuthenticationTask(Handler messageHandler, String username, String password){
        super(messageHandler);
        this.username = username;
        this.password = password;
    }



}
