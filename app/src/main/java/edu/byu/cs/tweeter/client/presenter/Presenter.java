package edu.byu.cs.tweeter.client.presenter;

public abstract class Presenter {
    public interface View{
        void displayInfoMessage(String message);
    }
}
