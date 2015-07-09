package it.ialweb.poi.adapters;

import it.ialweb.poi.R;
import it.ialweb.poi.core.AccountController;
import it.ialweb.poi.core.TweetUtils;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class UsersListAdapter extends ParseQueryAdapter<ParseObject> {
	
	private Context mContext;

	public UsersListAdapter(final Context context, final String dialogType) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				switch (dialogType) {
					case TweetUtils.TYPE_FOLLOWER:
						return TweetUtils.getFollowersQuery(context);
					case TweetUtils.TYPE_FOLLOWING:
						return TweetUtils.getFollowingQuery(context);
					case TweetUtils.TYPE_ALL_USERS:
						return TweetUtils.getAllUserQuery(context);
					default:
						return null;
				}
			}
		});
		
		mContext = context;
	}

	@Override
	public View getItemView(final ParseObject object, View v, ViewGroup parent) {
		super.getItemView(object, v, parent);
		final UserViewHolder holder;
		
		if (v == null) {
			v = View.inflate(getContext(), R.layout.user_row, null);
			holder = new UserViewHolder();
			holder.icon = (ParseImageView) v.findViewById(R.id.userIcon);
			holder.userIdTextView = (TextView) v.findViewById(R.id.userID);
			holder.ownerTextView = (TextView) v.findViewById(R.id.userName);
			holder.follow = (SwitchCompat) v.findViewById(R.id.swFollow);
			v.setTag(holder);
			
		} else {
			holder = (UserViewHolder) v.getTag();
		}
		object.pinInBackground();
		holder.userIdTextView.setText(object.getObjectId());
		holder.ownerTextView.setText(object.getString("username"));
		
		if (AccountController.isLoggedIn()) {
			final ParseUser me = ParseUser.getCurrentUser();
			final ParseRelation<ParseObject> followRelation = me.getRelation("follows");
			ParseQuery<ParseObject> query = followRelation.getQuery();
			query.whereEqualTo("objectId", object.getObjectId());
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if (list == null) return;
					
					if (list.size() > 0) {
						holder.follow.setChecked(true);
					} else {
						holder.follow.setChecked(false);
					}
				}
			});
			
			
			holder.follow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (holder.userIdTextView.getText().toString().equals(ParseUser.getCurrentUser().getObjectId())) {
						holder.follow.setChecked(!holder.follow.isChecked());
						Toast.makeText(mContext, mContext.getString(R.string.fyourselferr), Toast.LENGTH_SHORT).show();
						return;
					}
					
					if (holder.follow.isChecked()) {
						TweetUtils.follow(object, new TweetUtils.ITweetsUtils() {
							
							@Override
							public void onResponseResult(boolean done) {
								holder.follow.setChecked(done);
								if (!done) {
									Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
								}
							}
						}); 
					} else {
						TweetUtils.unfollow(object, new TweetUtils.ITweetsUtils() {
							
							@Override
							public void onResponseResult(boolean done) {
								holder.follow.setChecked(!done);
								if (!done) {
									Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
								}
							}
						}); 
					}
					me.saveInBackground();
				}
			});
			
		} else {
			holder.follow.setVisibility(View.INVISIBLE);
		}
		
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			holder.icon.setParseFile(imageFile);
			holder.icon.loadInBackground();
		} else {
			holder.icon.setBackgroundResource(R.drawable.tweet_default);
		}
		
		return v;
	}
	
	public static class UserViewHolder {
		public ParseImageView icon;
		public TextView userIdTextView;
		public TextView ownerTextView;
		public SwitchCompat follow;
	}
}
