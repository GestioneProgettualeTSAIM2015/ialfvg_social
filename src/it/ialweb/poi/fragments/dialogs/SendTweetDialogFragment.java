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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

@SuppressLint("InflateParams") public class SendTweetDialogFragment extends DialogFragment {

	public final static String TAG = "TweetDialogFragment";
	
	public static SendTweetDialogFragment newInstance() {
		SendTweetDialogFragment loginDialogFragment = new SendTweetDialogFragment();
		return loginDialogFragment;
	}

	public interface ISendTweetDialogFragment {
		void onSendTweet(String text);
	}

	private ISendTweetDialogFragment mListener;

	private EditText mEtTweet;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.fragment_dialog_tweet, null);

		mEtTweet = (EditText) view.findViewById(R.id.etTweet);

		builder.setView(view).setPositiveButton(getString(R.string.confirm_tweet), null);
		builder.setView(view).setNegativeButton(getString(R.string.cancel_tweet), null);

		Dialog dialog = builder.create();
		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button positiveButton = ((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE);
				positiveButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mListener != null) {
							mListener.onSendTweet(mEtTweet.getText().toString());
							dismiss();
						}
					}
				});
				Button negativeButton = ((AlertDialog) dialog).getButton(Dialog.BUTTON_NEGATIVE);
				negativeButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
			}
		});

		return dialog;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mListener == null && activity instanceof ISendTweetDialogFragment)
			mListener = (ISendTweetDialogFragment) activity;
	}

}
