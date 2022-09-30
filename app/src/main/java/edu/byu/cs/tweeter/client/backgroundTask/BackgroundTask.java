package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class BackgroundTask implements Runnable {
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    protected Handler messageHandler;

    protected BackgroundTask(Handler messageHandler){
        this.messageHandler = messageHandler;
    }

    @Override
    public abstract void run();



    protected void sendFailedMessage(String message) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, message);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

}
