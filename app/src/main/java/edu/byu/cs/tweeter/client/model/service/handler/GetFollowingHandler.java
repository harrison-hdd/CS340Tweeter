package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingHandler extends PagedServiceHandler<User>{
    public GetFollowingHandler(PagedServiceObserver<User> pagedServiceObserver){
        super(pagedServiceObserver);
    }


    @Override
    protected String getTaskName() {
        return "get following";
    }
}
