package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements StatusDialogFragment.Observer, MainPresenter.MainView {

    private static final String LOG_TAG = "MainActivity";

    public static final String CURRENT_USER_KEY = "CurrentUser";

    private Toast infoToast;

    private Toast postingToast;
    private User selectedUser;
    private TextView followeeCount;
    private TextView followerCount;
    private Button followButton;

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if (selectedUser == null) {
            throw new RuntimeException("User not passed to activity");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), selectedUser);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
                statusDialogFragment.show(getSupportFragmentManager(), "post-status-dialog");
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(selectedUser.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(selectedUser.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        Picasso.get().load(selectedUser.getImageUrl()).into(userImageView);

        followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, "..."));

        followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, "..."));

        followButton = findViewById(R.id.followButton);

        mainPresenter = new MainPresenter(this);


        mainPresenter.onNewUserSelected();


        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onFollowButtonClicked();
            }
        });
    }



    @Override
    public User getSelectedUser(){
        return selectedUser;
    }

    @Override
    public void setFollowButton(boolean isVisible, boolean isFollowing){
        if(!isVisible){
            followButton.setVisibility(View.GONE);
            return;
        }

        followButton.setVisibility(View.VISIBLE);

        if(isFollowing){
            followButton.setText(R.string.following);

            followButton.setBackgroundColor(getResources().getColor(R.color.white));

            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        }else{
            followButton.setText(R.string.follow);

            followButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            followButton.setTextColor(getResources().getColor(R.color.lightGray));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            mainPresenter.initiateLogout();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void logoutSuccess() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onStatusPosted(String post) {
        mainPresenter.postStatus(post);
    }

    @Override
    public void displayInfoMessage(String message) {
        if(infoToast != null){
            infoToast.cancel();
        }
        infoToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        infoToast.show();
    }

    @Override
    public void setFollowerCount(int numFollowers) {
        followerCount.setText(getString(R.string.followerCount, String.valueOf(numFollowers)));
    }

    @Override
    public void setFollowingCount(int numFollowing){
        followeeCount.setText(getString(R.string.followeeCount, String.valueOf(numFollowing)));
    }

    @Override
    public void setFollowButtonEnabled(boolean isEnabled) {
        followButton.setEnabled(isEnabled);
    }

    @Override
    public String getStringByResId(int resId) {
        return getString(resId);
    }

    @Override
    public String getFollowButtonText(){
        return followButton.getText().toString();
    }
}
