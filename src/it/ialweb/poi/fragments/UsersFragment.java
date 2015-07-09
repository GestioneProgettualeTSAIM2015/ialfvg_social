package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.activities.UserProfileActivity;
import it.ialweb.poi.adapters.UsersListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UsersFragment extends Fragment{
	
	public static UsersFragment newInstance() {
		UsersFragment myProfileFragment = new UsersFragment();
		return myProfileFragment;
	}
	
	private ListView mList;
	private SwipeRefreshLayout swipeLayout;
	
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
			    new Handler().postDelayed(new Runnable() {
			        @Override public void run() {
			        	android.util.Log.d("REFRESH", "refresh UsersFragment");
			            swipeLayout.setRefreshing(false);
			        }
			    }, 5000);
			}
		});
		
		mList = (ListView) view.findViewById(R.id.list);
		initList();
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String userId = ((TextView) view.findViewById(R.id.userID)).getText().toString();
				if (userId != null) {
					Intent myIntent = new Intent(getActivity(), UserProfileActivity.class);
					myIntent.putExtra(UserProfileActivity.USER_ID_TAG, userId);
					getActivity().startActivity(myIntent);
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
				
		return view;
	}
	
	private void initList() {
		UsersListAdapter adapter = new UsersListAdapter(getActivity());
		mList.setAdapter(adapter);
		adapter.loadObjects();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
	}
}
