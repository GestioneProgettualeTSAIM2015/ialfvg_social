package it.ialweb.poi.adapters;

import it.ialweb.poi.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ListAdapter extends ParseQueryAdapter<ParseObject> {
	
	private static final String TYPE_FOLLOWER = "TYPE_FOLLOWER";
	private static final String TYPE_FOLLOWING = "TYPE_FOLLOWING";
	private static final String TYPE_FAVORITE = "TYPE_FAVORITE";

	public ListAdapter(Context context, final boolean isMyProfile, final String dialogType) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
//				String type;
//				switch (dialogType) {
//					case TYPE_FOLLOWER:
//						type = "Followers";
//						break;
//					case TYPE_FOLLOWING:
//						type = "Following";
//						break;
//					case TYPE_FAVORITE:
//						type = "Followers";
//						break;
//				}
				ParseUser user = ParseUser.getCurrentUser();
				ParseQuery query = new ParseQuery("Tweets");
				query.orderByAscending("updatedAt");
//				if (user != null && isMyProfile)
//					query.whereEqualTo("OwnerId", user.getObjectId());
				return query;
			}
		});
	}

	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.tweet_row, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		ParseImageView icon = (ParseImageView) v.findViewById(R.id.tweetIcon);
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			icon.setParseFile(imageFile);
			icon.loadInBackground();
		} else {
			icon.setBackgroundResource(R.drawable.tweet_default);
		}

		// Add the author of message
		TextView ownerTextView = (TextView) v.findViewById(R.id.tweetOwner);
		ownerTextView.setText(object.getString("ownerId"));

		// Add the message
		TextView messageView = (TextView) v.findViewById(R.id.tweetText);
		messageView.setText(object.getString("message"));
		return v;
	}

}

