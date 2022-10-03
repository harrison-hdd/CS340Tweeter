package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.Observer;

public abstract class TaskHandler extends Handler {
    protected Observer observer;

    protected TaskHandler(Observer observer){
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccess(msg);
        } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            observer.handleFailure("Failed to " + getTaskName() + ": " + message);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            observer.handleFailure("Failed to " + getTaskName() + " because of exception: " + ex.getMessage());
        }
    }

    protected abstract String getTaskName();
    protected abstract void handleSuccess(Message message);
}
