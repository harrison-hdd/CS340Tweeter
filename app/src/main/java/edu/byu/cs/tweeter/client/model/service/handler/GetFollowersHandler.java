package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.PagedServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersHandler extends PagedServiceHandler<User>{

    public GetFollowersHandler(PagedServiceObserver<User> pagedServiceObserver){
        super(pagedServiceObserver);
    }


    @Override
    protected String getTaskName() {
        return "get followers";
    }
}
