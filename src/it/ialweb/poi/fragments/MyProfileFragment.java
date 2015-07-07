package it.ialweb.poi.fragments;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment;
import it.ialweb.poi.fragments.dialogs.LoginDialogFragment.ILoginDialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.parse.ParseUser;

public class MyProfileFragment extends Fragment implements ILoginDialogFragment {
	
	public static MyProfileFragment newInstance() {
		MyProfileFragment myProfileFragment = new MyProfileFragment();
		return myProfileFragment;
	}

	private BootstrapButton mToggleLogin;
	private TextView mUsername;
	private TextView mEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
		
		mToggleLogin = (BootstrapButton) view.findViewById(R.id.btnToggle);
		mUsername = (TextView) view.findViewById(R.id.tvUsername);
		mEmail = (TextView) view.findViewById(R.id.tvEmail);
		
		if(!AccountController.INSTANCE.isLoggedIn()) onLoggedOut();
		else showLoginView();
		
		mToggleLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AccountController.INSTANCE.isLoggedIn()) {
					AccountController.INSTANCE.logOut();
					onLoggedOut();
					Toast.makeText(getActivity(), getString(R.string.byebye), Toast.LENGTH_SHORT).show();
				} else showLoginDialog();
			}
		});
		
		return view;
	}
	
	private void onLoggedOut() {
		mUsername.setVisibility(View.GONE);
		mEmail.setVisibility(View.GONE);
		mToggleLogin.setText(getString(R.string.login));
		mToggleLogin.setBootstrapType("info");
	}
	
	private void showLoginDialog() {
		LoginDialogFragment dialog = LoginDialogFragment.newInstance();
		dialog.setTargetFragment(this, 0);
		dialog.show(getActivity().getSupportFragmentManager(),	LoginDialogFragment.TAG);
	}
	
	@Override
	public void onLoggedIn() {
		ParseUser user = AccountController.INSTANCE.getUser();
		if (user != null) Toast.makeText(getActivity(),
				getString(R.string.welcomeback) + " " + user.getUsername(), Toast.LENGTH_SHORT).show();
			
		showLoginView();
	}
	
	private void showLoginView() {
		ParseUser user = AccountController.INSTANCE.getUser();
		
		if(user != null) {
			mUsername.setText(user.getUsername());
			mEmail.setText(user.getEmail());
			mUsername.setVisibility(View.VISIBLE);
			mEmail.setVisibility(View.VISIBLE);
		}	
		
		mToggleLogin.setText(getString(R.string.logout));
		mToggleLogin.setBootstrapType("warning");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
	}
}
