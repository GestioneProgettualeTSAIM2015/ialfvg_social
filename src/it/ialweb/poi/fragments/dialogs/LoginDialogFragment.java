package it.ialweb.poi.fragments.dialogs;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

@SuppressLint("InflateParams") public class LoginDialogFragment extends DialogFragment {

	public final static String TAG = "LoginDialogFragment";

	private final static String IS_REGISTER_TAG = "isregistertag";
	private final static String IS_LOADING_TAG = "isloadingtag";
	
	public static LoginDialogFragment newInstance() {
		LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
		return loginDialogFragment;
	}

	public interface ILoginDialogFragment {
		void onLoggedIn();
	}

	private ILoginDialogFragment mListener;

	private boolean isRegister, isLoading;
	private LinearLayout mFormLayout;
	private ProgressBar mPbLoading;
	private SwitchCompat mSwForm;
	private EditText mEtUsername, mEtPassword, mEtEmail;

	private Button mPositiveButton;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.fragment_dialog_login, null);

		mFormLayout = (LinearLayout) view.findViewById(R.id.formLayout);
		mPbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
		mSwForm = (SwitchCompat) view.findViewById(R.id.swForm);
		mEtUsername = (EditText) view.findViewById(R.id.etUsername);
		mEtPassword = (EditText) view.findViewById(R.id.etPassword);
		mEtEmail = (EditText) view.findViewById(R.id.etEmail);

		if (savedInstanceState != null) {
			isLoading = savedInstanceState.getBoolean(IS_LOADING_TAG);
			isRegister = savedInstanceState.getBoolean(IS_REGISTER_TAG);
		}

		mSwForm.setChecked(isRegister);
		mSwForm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isRegister = !isRegister;
				invalidateForm();
			}
		});

		builder.setView(view).setPositiveButton(getString(R.string.confirm), null);

		Dialog dialog = builder.create();
		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				mPositiveButton = ((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE);
				mPositiveButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						onPositiveButtonClick();
					}
				});
			}
		});

		invalidateForm();
		if (isLoading) toggleLoading(true);

		return dialog;
	}
	
	private void invalidateForm() {
		mEtEmail.setVisibility(isRegister ? View.VISIBLE : View.GONE);
		mSwForm.setText(isRegister ? getString(R.string.registration) : getString(R.string.login));
	}
	
	private void toggleLoading(boolean loading) {
		isLoading = loading;
		Dialog dialog = getDialog();
		if (dialog != null) dialog.setCancelable(!loading);
		mPositiveButton.setEnabled(!loading);
		mFormLayout.setVisibility(loading ? View.GONE : View.VISIBLE);
		mPbLoading.setVisibility(loading? View.VISIBLE : View.GONE);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		outState.putBoolean(IS_LOADING_TAG, isLoading);
		outState.putBoolean(IS_REGISTER_TAG, isRegister);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mListener == null && activity instanceof ILoginDialogFragment)
			mListener = (ILoginDialogFragment) activity;
	}

	@Override
	public void setTargetFragment(Fragment fragment, int requestCode) {
		super.setTargetFragment(fragment, requestCode);

		if (fragment instanceof ILoginDialogFragment)
			mListener = (ILoginDialogFragment) fragment;
	}

	private void onPositiveButtonClick() {
		String username = mEtUsername.getText().toString();
		if (username.length() == 0) {
			mEtUsername.setError("Missing");
			return;
		}

		String password = mEtPassword.getText().toString();
		if (password.length() == 0) {
			mEtPassword.setError("Missing");
			return;
		}

		String email = null;
		if (isRegister) {
			email = mEtEmail.getText().toString();
			if (email.length() == 0) {
				mEtEmail.setError("Missing");
				return;
			}
		}

		// form ok

		toggleLoading(true);

		if (isRegister)
			AccountController.register(username, password, email, new SignUpCallback() {
				@Override
				public void done(ParseException pe) {
					if (pe != null) {
						String message = "Login failed: " + pe.getMessage();
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
						Log.i(TAG, message);
						toggleLoading(false);
					}
					else {
						if (mListener != null) mListener.onLoggedIn();
						dismiss();
					}
				}
			});
		else
			AccountController.logIn(username, password, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException pe) {
					if (pe != null) {
						String message = "Login failed: " + pe.getMessage();
						Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
						Log.i(TAG, message);
						toggleLoading(false);
					}
					else {
						if (mListener != null) mListener.onLoggedIn();
						dismiss();
					}
				}
			});
	}
}
