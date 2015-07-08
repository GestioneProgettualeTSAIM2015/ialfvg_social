package it.ialweb.poi.activities;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetUtils;
import it.ialweb.poi.fragments.MyProfileFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ISendTweetDialogFragment, ILoginDialogFragment {

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
				case 2:
					return MyProfileFragment.newInstance();

				default:
					return new PlaceHolder();
				}
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return getResources().getString(titles[position]);
			}
		};
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

	public static class PlaceHolder extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			RecyclerView recyclerView = new RecyclerView(getActivity());
			recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
			recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
				@Override
				public int getItemCount() {
					return 30;
				}

				@Override
				public void onBindViewHolder(ViewHolder holder, int position) {
					((TextView) holder.itemView).setText("Item " + position);
				}

				@Override
				public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
					LayoutInflater layoutInflater = getActivity().getLayoutInflater();
					View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
					view.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent myIntent = new Intent(getActivity(), UserProfileActivity.class);
							myIntent.putExtra(UserProfileActivity.USER_ID_TAG, "111");
							myIntent.putExtra(UserProfileActivity.USERNAME_TAG, "Gianni");
							myIntent.putExtra(UserProfileActivity.EMAIL_TAG, "mail@a.com");
							getActivity().startActivity(myIntent);
						}
					});
					
					return new ViewHolder(view) {};
				}
			});
			return recyclerView;
		}
	}

	@Override
	public void onSendTweet(String message) {
		TweetUtils.sendTweet(message);
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