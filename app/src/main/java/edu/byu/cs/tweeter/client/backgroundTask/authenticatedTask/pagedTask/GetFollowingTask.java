package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.PagedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {


    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
    }


    @Override
    protected Pair<List<User>, Boolean> getData() {
        return getFakeData().getPageOfUsers(lastItem, limit, targetUser);
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

}
