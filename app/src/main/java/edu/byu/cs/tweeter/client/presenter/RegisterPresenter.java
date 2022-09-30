package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;


public class RegisterPresenter extends Presenter{
    private static final String LOG_TAG = "RegisterPresenter";
    private final RegisterView view;

    public interface RegisterView extends Presenter.View{
        String getFirstName();
        String getLastName();
        String getUsername();
        String getPassword();
        String getImageBase64();

        void displayErrorMessage(String message);

        void registerSuccess();
    }

    public RegisterPresenter(RegisterView view){
        this.view = view;
    }

    public void initiateRegistration(){
        String firstName = view.getFirstName();
        String lastname = view.getLastName();
        String username = view.getUsername();
        String password = view.getPassword();
        String imageBase64 = view.getImageBase64();

        try {
            validateRegistration(firstName, lastname, username, password, imageBase64);
            view.displayErrorMessage(null);

            view.displayInfoMessage("Registering...");

            new UserService().register(firstName, lastname, username, password, imageBase64, new RegisterObserver());

        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String username, String password, String imageBase64) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (username.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (username.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (username.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageBase64 == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    private class RegisterObserver implements UserService.RegisterObserver {
        @Override
        public void handleSuccess() {
            view.displayInfoMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.registerSuccess();
        }

        @Override
        public void handleFailure(String message) {
            view.displayInfoMessage("Failed to register" + message);
        }
    }
}
