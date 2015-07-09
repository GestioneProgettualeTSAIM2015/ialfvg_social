package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.adapters.TweetsListAdapter;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetUtils;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendRetweetDialogFragment;
import it.ialweb.poi.fragments.dialogs.SendTweetDialogFragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class TweetsFragment extends Fragment implements ILoginDialogFragment {
	
	private final static String SENDING_TWEET_TAG = "sendingtweettag";
	
	public static TweetsFragment newInstance() {
		TweetsFragment myProfileFragment = new TweetsFragment();
		return myProfileFragment;
	}

	private ListView mList;
	@SuppressWarnings("unused")
	private ProgressBar mProgressBar;
	private boolean sendingTweet;
	
	private String tweetOwner;
	private String tweetText;
	private Bundle mBundle;
	private String tweetId;
	private SwipeRefreshLayout swipeLayout;
	private TweetsListAdapter adapter; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
	    swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
	    swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
	    swipeLayout.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				adapter.loadObjects();
			}
		});
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		
		mList = (ListView) view.findViewById(R.id.list);
		initList();
		mList.setOnItemClickListener(new OnItemClickListener()
		{



			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
						
				tweetOwner = ((TextView) view.findViewById(R.id.tweetOwner)).getText().toString();
				tweetText = ((TextView) view.findViewById(R.id.tweetText)).getText().toString();
				tweetId = ((TextView) view.findViewById(R.id.tweetId)).getText().toString();
				mBundle = new Bundle();
				mBundle.putString(SendRetweetDialogFragment.TWEET_OWNER, tweetOwner);
				mBundle.putString(SendRetweetDialogFragment.TWEET_TEXT, tweetText);
				mBundle.putString(SendRetweetDialogFragment.TWEET_ID, tweetId);
				
				if (AccountController.isLoggedIn()) {
					SendRetweetDialogFragment retweetDialog = SendRetweetDialogFragment.newInstance(mBundle);
					retweetDialog.setTargetFragment(TweetsFragment.this, 0);
					retweetDialog.show(getChildFragmentManager(), SendTweetDialogFragment.TAG);
				} else {
					sendingTweet = true;
					LoginDialogFragment dialog = LoginDialogFragment.newInstance();
					dialog.setTargetFragment(TweetsFragment.this, 0);
					dialog.show(getChildFragmentManager(), LoginDialogFragment.TAG);
				}
			}
		});
		
		mList.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
			    int topRowVerticalPosition = (mList == null || mList.getChildCount() == 0) ? 0 : mList.getChildAt(0).getTop();
			    swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);  
			}
		});
		
		if (savedInstanceState != null)
			sendingTweet = savedInstanceState.getBoolean(SENDING_TWEET_TAG);
				
		return view;
	}
	
	private void initList() {
		adapter = new TweetsListAdapter(getActivity(), TweetUtils.TYPE_ALL_TWEETS);
		
		adapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {

			@Override
			public void onLoaded(List<ParseObject> arg0, Exception arg1) {
				 swipeLayout.setRefreshing(false);
			}

			@Override
			public void onLoading() {
				
			}
		});
		
		mList.setAdapter(adapter);
		adapter.loadObjects();
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {	
		outState.putBoolean(SENDING_TWEET_TAG, sendingTweet);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onLoggedIn() {
		if (sendingTweet) {
			sendingTweet = false;
			SendRetweetDialogFragment retweetDialog = SendRetweetDialogFragment.newInstance(mBundle);
			retweetDialog.setTargetFragment(TweetsFragment.this, 0);
			retweetDialog.show(getChildFragmentManager(), SendTweetDialogFragment.TAG);
		}
	}
	
}
