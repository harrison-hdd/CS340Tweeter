package edu.byu.cs.tweeter.client.presenter.pagedPresenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.PagedServiceObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.Presenter;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    protected View<T> view;

    protected static final int PAGE_SIZE = 10;

    protected boolean isLoading;
    protected boolean hasMorePages;

    protected T lastItem;

    public boolean isLoading(){
        return isLoading;
    }



    protected PagedPresenter(View<T> view){
        this.view = view;
        isLoading = false;
        hasMorePages = true;
    }

    public interface View<ItemType> extends Presenter.View {
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


    protected class GetItemsObserver implements PagedServiceObserver<T> {
        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages){
            isLoading = false;
            view.removeLoadingFooter();

            PagedPresenter.this.hasMorePages = hasMorePages;
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayInfoMessage(message);
        }
    }

}
