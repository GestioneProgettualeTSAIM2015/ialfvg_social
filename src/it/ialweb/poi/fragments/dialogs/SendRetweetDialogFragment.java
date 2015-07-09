package it.ialweb.poi.fragments.dialogs;

import it.ialweb.poi.R;
import it.ialweb.poi.core.TweetUtils;
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
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") public class SendRetweetDialogFragment extends DialogFragment {

	public final static String TAG = "TweetDialogFragment";

	public static final String TWEET_OWNER = "TWEET_OWNER";
	public static final String TWEET_TEXT = "TWEET_TEXT";
	public static final String TWEET_ID = "TWEET_ID";
	
	public static SendRetweetDialogFragment newInstance(Bundle b) {
		SendRetweetDialogFragment retweetDialogFragment = new SendRetweetDialogFragment();
		retweetDialogFragment.setArguments(b);
		return retweetDialogFragment;
	}

	public interface ISendRetweetDialogFragment {
		void onSendRetweet(String message);
	}

	private ISendRetweetDialogFragment mListener;
	
	private TextView mReTweetText;
	private TextView mReTweetOwner;
	
	private String mText;
	private String mOwner;

	private String mId;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View view = inflater.inflate(R.layout.fragment_dialog_retweet, null);
		
		mReTweetOwner = (TextView) view.findViewById(R.id.reTweetOwner);
		mReTweetText = (TextView) view.findViewById(R.id.reTweetText);
		
		Bundle bundle = getArguments();
		mOwner = bundle.getString(TWEET_OWNER);
		mText = bundle.getString(TWEET_TEXT);
		mId = bundle.getString(TWEET_ID);
		if (mOwner != null) mReTweetOwner.setText("@" + mOwner);
		if (mText != null) mReTweetText.setText(mText);
		
		
		builder
			.setView(view)
			.setPositiveButton(R.string.confirm_retweet, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mListener != null) {
						String tweet = String.format("%s @%s", mText, mOwner);
						mListener.onSendRetweet(tweet);
					}
				}
			}).setNeutralButton(R.string.favorite, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mId != null) 
						TweetUtils.addFavorite(mId);
					Toast.makeText(getActivity(), mId != null ? "Added to favorite" : "Error", Toast.LENGTH_SHORT).show();
					
				}
			});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (mListener == null && activity instanceof ISendRetweetDialogFragment)
			mListener = (ISendRetweetDialogFragment) activity;
	}
}
