package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.activities.UserProfileActivity;
import it.ialweb.poi.adapters.UsersListAdapter;
import it.ialweb.poi.core.TweetUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		mList = (ListView) view.findViewById(R.id.list);
		
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
		
		initList();
				
		return view;
	}
	
	private void initList() {
		UsersListAdapter adapter = new UsersListAdapter(getActivity(), TweetUtils.TYPE_ALL_USERS);
		mList.setAdapter(adapter);
		adapter.loadObjects();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
	}
}
