package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetsListAdapter;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.parse.ParseUser;

public class TweetsFragment extends Fragment {
	
	public static TweetsFragment newInstance() {
		TweetsFragment myProfileFragment = new TweetsFragment();
		return myProfileFragment;
	}

	private ListView mList;
	private ProgressBar mProgressBar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		mList = (ListView) view.findViewById(R.id.list);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		
		initList();
				
		return view;
	}
	
	private void initList() {
		TweetsListAdapter adapter = new TweetsListAdapter(getActivity(), false);
		mList.setAdapter(adapter);
		adapter.loadObjects();
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
	}
}
