package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticationTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticationTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    public interface GetUserObserver{
        void handleGetUserSuccess(User user);
        void handleGetUserFailure(String message);
    }

    public interface LoginObserver{
        void handleSuccess();
        void handleFailure(String message);
    }
    public interface RegisterObserver{
        void handleSuccess();
        void handleFailure(String message);
    }

    public interface LogoutObserver{
        void handleLogoutSuccess();
        void handleLogoutFailure(String message);
    }

    public UserService(){}

    public void login(String username, String password, LoginObserver loginObserver){
        //is void because we're doing async programming, so we can return anything right away
        //run LoginTask in background
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(loginObserver));

        BackgroundTaskUtils.runTask(loginTask);
    }

    public void register(String firstName, String lastName, String username, String password, String image, RegisterObserver registerObserver){
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password, image, new RegisterHandler(registerObserver));
        BackgroundTaskUtils.runTask(registerTask);
    }

    public void logout(LogoutObserver logoutObserver){
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(logoutObserver));
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public void getUser(String username, GetUserObserver getUserObserver){
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new GetUserHandler(getUserObserver));
        BackgroundTaskUtils.runTask(getUserTask);
    }

    private class GetUserHandler extends Handler {//service
        private final GetUserObserver getUserObserver;

        public GetUserHandler(GetUserObserver getUserObserver){
            super(Looper.getMainLooper());
            this.getUserObserver = getUserObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                getUserObserver.handleGetUserSuccess(user);

            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                getUserObserver.handleGetUserFailure("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                getUserObserver.handleGetUserFailure("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

    private class LoginHandler extends Handler {
        private final LoginObserver loginObserver;

        public LoginHandler(LoginObserver loginObserver){
            super(Looper.getMainLooper());
            this.loginObserver = loginObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

                Cache.getInstance().setCurrUser(loggedInUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                loginObserver.handleSuccess();

            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                loginObserver.handleFailure(msg.getData().getString(LoginTask.MESSAGE_KEY));
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception e = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                loginObserver.handleFailure("Failed to login because of exception: " + e.getMessage());
            }
        }
    }

    private class RegisterHandler extends Handler {
        private final RegisterObserver registerObserver;

        public RegisterHandler(RegisterObserver registerObserver){
            super(Looper.getMainLooper());
            this.registerObserver = registerObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

                Cache.getInstance().setCurrUser(registeredUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                registerObserver.handleSuccess();
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                registerObserver.handleFailure(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                registerObserver.handleFailure("Exception: " + ex.getMessage());
            }
        }
    }

    private class LogoutHandler extends Handler {
        private final LogoutObserver logoutObserver;

        public LogoutHandler(LogoutObserver logoutObserver){
            super(Looper.getMainLooper());
            this.logoutObserver = logoutObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                Cache.getInstance().clearCache();
                logoutObserver.handleLogoutSuccess();
            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                logoutObserver.handleLogoutFailure("Failed to logout: " + message);
            } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                logoutObserver.handleLogoutFailure("Failed to logout because of exception: " + ex.getMessage());
            }
        }
    }
}
