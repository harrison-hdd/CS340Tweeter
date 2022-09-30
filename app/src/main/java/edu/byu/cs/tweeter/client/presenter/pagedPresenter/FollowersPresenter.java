package edu.byu.cs.tweeter.client.presenter.pagedPresenter;


import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(View<User> view){
        super(view);
    }

    @Override
    protected void callService(User user) {
        new FollowService().getFollowers(user, lastItem, PAGE_SIZE, new GetItemsObserver());
    }

}
