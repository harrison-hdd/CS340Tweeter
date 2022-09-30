package edu.byu.cs.tweeter.client.backgroundTask;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundTaskUtils {
    static public void runTask(BackgroundTask task){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
