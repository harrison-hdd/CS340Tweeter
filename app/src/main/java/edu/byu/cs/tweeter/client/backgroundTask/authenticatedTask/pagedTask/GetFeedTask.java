package edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.authenticatedTask.pagedTask.PagedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetFeedTask";


    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }


    @Override
    protected Pair<List<Status>, Boolean> getData() {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastItem, limit);
        return pageOfStatus;
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

}
