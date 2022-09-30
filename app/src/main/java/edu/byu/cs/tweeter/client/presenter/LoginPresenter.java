package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter extends Presenter{

    private static final String LOG_TAG = "LoginPresenter";
    private final LoginView view;


    public interface LoginView extends View{
        void displayErrorMessage(String message);
        String getUsername();
        String getPassword();
        void loginSuccessful();

    }

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public void initiateLogin(){
        String alias = view.getUsername();
        String password = view.getPassword();

        try {
            validateLogin(alias, password);
            view.displayErrorMessage(null);
            view.displayInfoMessage("Logging in...");

            new UserService().login(alias, password, new LoginObserver());
        }catch (Exception e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void validateLogin(String alias, String password) {
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    private class LoginObserver implements UserService.LoginObserver{
        @Override
        public void handleSuccess() {
            view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.loginSuccessful();
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to login: " + message);
        }
    }
}
