package it.ialweb.poi.activities;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetUtils;
import it.ialweb.poi.fragments.MyProfileFragment;
import it.ialweb.poi.fragments.TweetsFragment;
import it.ialweb.poi.fragments.UsersFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendRetweetDialogFragment.ISendRetweetDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendTweetDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendTweetDialogFragment.ISendTweetDialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements ISendTweetDialogFragment, ISendRetweetDialogFragment, ILoginDialogFragment {

	private final static String SENDING_TWEET_TAG = "sendingtweettag";
	
	private TabLayout tabLayout;
	private ViewPager viewPager;
	
	private boolean sendingTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		viewPager = (ViewPager) findViewById(R.id.pager);

		FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			private int[] titles = new int[] { R.string.Timeline, R.string.Users, R.string.MyProfile };

			@Override
			public int getCount() {
				return titles.length;
			}

			@Override
			public Fragment getItem(int position) {
				switch (position) {
				case 0:
					return TweetsFragment.newInstance();
				case 1:
					return UsersFragment.newInstance();
				case 2:
					return MyProfileFragment.newInstance();
				}
				return null;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return getResources().getString(titles[position]);
			}
		};
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(adapter);

		tabLayout.setupWithViewPager(viewPager);

		findViewById(R.id.fabBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AccountController.isLoggedIn()) {
					SendTweetDialogFragment tweetDialog = SendTweetDialogFragment.newInstance();
					tweetDialog.show(getSupportFragmentManager(), SendTweetDialogFragment.TAG);
				} else {
					sendingTweet = true;
					LoginDialogFragment dialog = LoginDialogFragment.newInstance();
					dialog.show(getSupportFragmentManager(), LoginDialogFragment.TAG);
				}

			}
		});
		
		if (savedInstanceState != null)
			sendingTweet = savedInstanceState.getBoolean(SENDING_TWEET_TAG);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            openSettingsActivity();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openSettingsActivity() {
		Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onSendTweet(String message) {
		TweetUtils.sendTweet(message);
		Snackbar.make(findViewById(R.id.coordinator), "sent: " + message, Snackbar.LENGTH_LONG).show();
	}
	
	@Override
	public void onSendRetweet(String message) {
		TweetUtils.sendRetweet(message);
		Snackbar.make(findViewById(R.id.coordinator), "sent: " + message, Snackbar.LENGTH_LONG).show();
	}
	
	@Override
	public void onLoggedIn() {
		if (sendingTweet) {
			sendingTweet = false;
			SendTweetDialogFragment tweetDialog = SendTweetDialogFragment.newInstance();
			tweetDialog.show(getSupportFragmentManager(), SendTweetDialogFragment.TAG);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState,
			PersistableBundle outPersistentState) {
		
		outState.putBoolean(SENDING_TWEET_TAG, sendingTweet);
		
		super.onSaveInstanceState(outState, outPersistentState);
	}
}