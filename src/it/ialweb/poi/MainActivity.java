package it.ialweb.poi;

import com.parse.ParseObject;
import com.parse.ParseUser;

import it.ialweb.poi.fragments.MyProfileFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendTweetDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendTweetDialogFragment.ISendTweetDialogFragment;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements ISendTweetDialogFragment {

	private TabLayout tabLayout;
	private ViewPager viewPager;

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
				ParseUser user = ParseUser.getCurrentUser();
				if (user != null && user.isAuthenticated()) {
					SendTweetDialogFragment dialog = SendTweetDialogFragment.newInstance();
					dialog.show(getSupportFragmentManager(), SendTweetDialogFragment.TAG);
				} else {
					LoginDialogFragment dialog = LoginDialogFragment.newInstance();
					dialog.show(getSupportFragmentManager(), LoginDialogFragment.TAG);
				}

			}
		});
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
					return new ViewHolder(view) {
					};
				}
			});
			return recyclerView;
		}
	}

	@Override
	public void onSendTweet(String text) {
		ParseObject tweet = new ParseObject("Tweets");
		tweet.put("message", text);
		//			tweet.put("ownerId", user.getObjectId());
		tweet.saveInBackground();
		Snackbar.make(findViewById(R.id.coordinator), "sent: " + text, Snackbar.LENGTH_LONG).show();
	}
}