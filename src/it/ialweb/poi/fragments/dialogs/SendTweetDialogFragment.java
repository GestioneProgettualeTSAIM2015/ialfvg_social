package it.ialweb.poi.fragments.dialogs;

import it.ialweb.poi.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

@SuppressLint("InflateParams") public class SendTweetDialogFragment extends DialogFragment {

	public final static String TAG = "TweetDialogFragment";
	
	public static SendTweetDialogFragment newInstance() {
		SendTweetDialogFragment loginDialogFragment = new SendTweetDialogFragment();
		return loginDialogFragment;
	}

	public interface ISendTweetDialogFragment {
		void onSendTweet(String message);
	}

	private ISendTweetDialogFragment mListener;

	private EditText mEtTweet;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.fragment_dialog_tweet, null);

		mEtTweet = (EditText) view.findViewById(R.id.etTweet);

		builder
			.setView(view)
			.setPositiveButton(getString(R.string.confirm_tweet), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if (mListener != null) {
						String tweet = mEtTweet.getText().toString();
						if (tweet.length() == 0) mEtTweet.setError("Missing");
						else mListener.onSendTweet(tweet);
					}
				}
			});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mListener == null && activity instanceof ISendTweetDialogFragment)
			mListener = (ISendTweetDialogFragment) activity;
	}
}
