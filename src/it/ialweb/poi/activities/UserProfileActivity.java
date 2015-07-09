package it.ialweb.poi.activities;

import java.util.List;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetUtils;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class UserProfileActivity extends AppCompatActivity implements ILoginDialogFragment {
	
	public final static String USERNAME_TAG = "usernametag";
	public final static String EMAIL_TAG = "emailtag";
	public final static String IS_FOLLOWED_TAG = "isfollowedtag";
	public final static String USER_ID_TAG = "useridtag";
	
	private final static String FOLLOW_ACTION_TAG = "followingtag";
	
	private boolean isActionFollow;
	private boolean isFollowed;
	private String mUserId;
	
	private BootstrapButton mBtnToggleFollow;
	private TextView mUsername;
	private TextView mEmail;
	
	ParseUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		
		mBtnToggleFollow = (BootstrapButton) findViewById(R.id.btnFollow);
		mUsername = (TextView) findViewById(R.id.tvUsername);
		mEmail = (TextView) findViewById(R.id.tvEmail);
		
		mBtnToggleFollow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!AccountController.isLoggedIn()) {
					isActionFollow = true;
					showLoginDialog();
				}
				else follow();
			}
		});
		
		Intent intent = getIntent();
		if (intent != null) {
			mUserId = intent.getStringExtra(USER_ID_TAG);
			isFollowed = intent.getBooleanExtra(IS_FOLLOWED_TAG, false);
			
			user = TweetUtils.getUserById(mUserId);
			if (user != null) {
				mUsername.setText(user.getUsername());
				mEmail.setText(user.getEmail());
			}
		}
		
		if (savedInstanceState != null) {
			isFollowed = savedInstanceState.getBoolean(IS_FOLLOWED_TAG);
			isActionFollow = savedInstanceState.getBoolean(FOLLOW_ACTION_TAG);
		}
		
		updateBtnToggleFollow();
	}
	
	private void updateBtnToggleFollow() {
		mBtnToggleFollow.setText(isFollowed ? getString(R.string.unfollow) : getString(R.string.follow));
		mBtnToggleFollow.setBootstrapType(isFollowed ? "success" : "info");
	}
	
	private void showLoginDialog() {
		LoginDialogFragment dialog = LoginDialogFragment.newInstance();
		dialog.show(getSupportFragmentManager(), LoginDialogFragment.TAG);
	}
	
	@Override
	public void onLoggedIn() {
		if (isActionFollow) {
			isActionFollow = false;
			follow();
		}
	}
	
	private void follow() {

		isFollowed = !isFollowed;

		if (isFollowed) {
			TweetUtils.follow(user, new TweetUtils.ITweetsUtils() {
				
				@Override
				public void onResponseResult(boolean done) {
					if (!done) {
						isFollowed = !isFollowed;
						updateBtnToggleFollow();
						Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
		} else {
			TweetUtils.unfollow(user, new TweetUtils.ITweetsUtils() {
				
				@Override
				public void onResponseResult(boolean done) {
					if (!done) {
						isFollowed = !isFollowed;
						updateBtnToggleFollow();
						Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		if (isFollowed) Toast.makeText(this, "Follow!", Toast.LENGTH_SHORT).show();
		updateBtnToggleFollow();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(IS_FOLLOWED_TAG, isFollowed);
		outState.putBoolean(FOLLOW_ACTION_TAG, isActionFollow);
		super.onSaveInstanceState(outState);
	}
}
