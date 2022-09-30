package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    protected PagedView<T> view;

    protected static final int PAGE_SIZE = 10;

    protected boolean isLoading;
    protected boolean hasMorePages;

    protected T lastItem;

    public boolean isLoading(){
        return isLoading;
    }

    public boolean hasMorePages(){
        return hasMorePages;
    }

    protected PagedPresenter(PagedView<T> view){
        this.view = view;
        isLoading = false;
        hasMorePages = true;
    }

    public interface PagedView<ItemType> extends Presenter.View{
        void goToNewUserPage(User user);
        void addLoadingFooter();
        void removeLoadingFooter();
        void addItems(List<ItemType> items);
        int getVisibleItemCount();
        int getTotalItemCount();
        int getFirstVisibleItemPosition();
    }

    public void onUserSelected(String username){
        new UserService().getUser(username, new GetUserObserver());
        view.displayInfoMessage("Getting user's profile...");
    }

    public void onScroll(User user){
        int visibleItemCount = view.getVisibleItemCount();
        int totalItemCount = view.getTotalItemCount();
        int firstVisibleItemPosition = view.getFirstVisibleItemPosition();

        if (!isLoading && hasMorePages) {
            if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0) {
                // Run this code later on the UI thread
                loadMoreItems(user);
            }
        }
    }

    public void loadMoreItems(User user){
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();

            callService(user);
        }
    }

    protected abstract void callService(User user);

    private class GetUserObserver implements UserService.GetUserObserver{

        @Override
        public void handleGetUserSuccess(User user) {
            view.goToNewUserPage(user);
        }

        @Override
        public void handleGetUserFailure(String message) {
            view.displayInfoMessage(message);
        }
    }

}
