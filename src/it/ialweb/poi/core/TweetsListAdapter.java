package it.ialweb.poi.core;

import it.ialweb.poi.R;
import it.ialweb.poi.core.UsersListAdapter.UserViewHolder;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class TweetsListAdapter extends ParseQueryAdapter<ParseObject> {

	public TweetsListAdapter(Context context, final boolean isMyProfile) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseUser user = ParseUser.getCurrentUser();
				ParseQuery query = new ParseQuery("Tweets");
				query.orderByDescending("updatedAt");
				if (user != null && isMyProfile)
					query.whereEqualTo("createdBy", user);
				return query;
			}
		});
	}

	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		super.getItemView(object, v, parent);
		
		TweetViewHolder holder;
		
		if (v == null) {
			v = View.inflate(getContext(), R.layout.tweet_row, null);
			holder = new TweetViewHolder();
			holder.icon = (ParseImageView) v.findViewById(R.id.tweetIcon);
			holder.ownerTextView = (TextView) v.findViewById(R.id.tweetOwner);
			holder.messageView = (TextView) v.findViewById(R.id.tweetText);
			v.setTag(holder);
		} else {
			holder = (TweetViewHolder) v.getTag();
		}

		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			holder.icon.setParseFile(imageFile);
			holder.icon.loadInBackground();
		} else {
			holder.icon.setBackgroundResource(R.drawable.tweet_default);
		}

		ParseUser creator = object.getParseUser("createdBy");
		holder.ownerTextView.setText(creator.getUsername());

		holder.messageView.setText(object.getString("message"));
		return v;
	}
	
	static class TweetViewHolder {
		ParseImageView icon;
		TextView ownerTextView;
		TextView messageView;
	}
}
