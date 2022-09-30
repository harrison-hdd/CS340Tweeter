package edu.byu.cs.tweeter.client.presenter.pagedPresenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    public FeedPresenter(View<Status> view){
        super(view);
    }

    @Override
    protected void callService(User user){
        new StatusService().getFeed(user, lastItem, PAGE_SIZE, new GetItemsObserver());
    }
}
