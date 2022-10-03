package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryHandler extends PagedServiceHandler<Status>{

    public GetStoryHandler(PagedServiceObserver<Status> pagedServiceObserver){
        super(pagedServiceObserver);
    }

    @Override
    protected String getTaskName() {
        return "get story";
    }

}
