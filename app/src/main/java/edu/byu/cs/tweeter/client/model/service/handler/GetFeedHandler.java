package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedHandler extends PagedServiceHandler<Status> {
    public GetFeedHandler(PagedServiceObserver<Status> pagedServiceObserver){
        super(pagedServiceObserver);
    }

    @Override
    protected String getTaskName() {
        return "get feed";
    }
}
