package edu.byu.cs.tweeter.client.presenter.pagedPresenter;



import edu.byu.cs.tweeter.client.model.service.FollowService;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {


    public FollowingPresenter(View<User> view){
        super(view);
    }

    @Override
    protected void callService(User user) {
        new FollowService().getFollowing(user, lastItem, PAGE_SIZE, new GetItemsObserver());
    }


}
