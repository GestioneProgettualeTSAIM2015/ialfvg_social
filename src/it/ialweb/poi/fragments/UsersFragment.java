package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetsListAdapter;
import it.ialweb.poi.core.UsersListAdapter;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
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
		
		initList();
				
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
