package edu.byu.cs.tweeter.client.presenter.pagedPresenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {


    public StoryPresenter(View<Status> view){
        super(view);
    }

    @Override
    protected void callService(User user) {
        new StatusService().getStory(user, lastItem, PAGE_SIZE, new GetItemsObserver());
    }


}
