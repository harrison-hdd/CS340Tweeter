package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;

public abstract class PagedServiceHandler<ItemType> extends TaskHandler {
    protected final PagedServiceObserver<ItemType> pagedServiceObserver;

    protected PagedServiceHandler(PagedServiceObserver<ItemType> pagedServiceObserver){
        super(pagedServiceObserver);
        this.pagedServiceObserver = pagedServiceObserver;
    }
//
//    @Override
//    public void handleMessage(@NonNull Message msg) {
//
//        boolean success = msg.getData().getBoolean(PagedTask.SUCCESS_KEY);
//        if (success) {
//            List<ItemType> items = (List<ItemType>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
//            boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
//
//            pagedServiceObserver.handleSuccess(items, hasMorePages);
//        } else if (msg.getData().containsKey(PagedTask.MESSAGE_KEY)) {
//            String message = msg.getData().getString(PagedTask.MESSAGE_KEY);
//            handleFailure(message);
//        } else if (msg.getData().containsKey(PagedTask.EXCEPTION_KEY)) {
//            Exception ex = (Exception) msg.getData().getSerializable(PagedTask.EXCEPTION_KEY);
//            handleException(ex);
//        }
//    }


    @Override
    protected void handleSuccess(Message msg) {
        List<ItemType> items = (List<ItemType>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);

        pagedServiceObserver.handleSuccess(items, hasMorePages);
    }

//    protected abstract void handleFailure(String message);
//    protected abstract void handleException(Exception e);

}
