package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.Observer;

public interface PagedServiceObserver<T> extends Observer {
    void handleSuccess(List<T> items, boolean hasMorePages);
}
