package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

public interface PagedServiceObserver<T> {
    void handleSuccess(List<T> items, boolean hasMorePages);
    void handleFailure(String message);
}
