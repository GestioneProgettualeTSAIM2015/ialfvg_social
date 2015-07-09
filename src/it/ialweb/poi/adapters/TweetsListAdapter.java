package it.ialweb.poi.adapters;

import it.ialweb.poi.R;
import it.ialweb.poi.core.TweetUtils;
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

public class TweetsListAdapter extends ParseQueryAdapter<ParseObject> {


	public TweetsListAdapter(Context context, final String dialogType) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ParseQuery create() {
				switch (dialogType) {
					case TweetUtils.TYPE_ALL_TWEETS:
						return TweetUtils.getAllTweetsQuery();
					case TweetUtils.TYPE_MINE_TWEETS:
						return TweetUtils.getMineTweetsQuery();
					case TweetUtils.TYPE_FAVORITE_TWEETS:
						return TweetUtils.getFavoritesQuery();
					default:
						return null;
				}
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
			holder.idTextView = (TextView) v.findViewById(R.id.tweetId);
			holder.icon = (ParseImageView) v.findViewById(R.id.tweetIcon);
			holder.ownerTextView = (TextView) v.findViewById(R.id.tweetOwner);
			holder.messageView = (TextView) v.findViewById(R.id.tweetText);
			v.setTag(holder);
		} else {
			holder = (TweetViewHolder) v.getTag();
		}

		holder.idTextView.setText(object.getObjectId());
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			holder.icon.setParseFile(imageFile);
			holder.icon.loadInBackground();
		} else {
			holder.icon.setBackgroundResource(R.drawable.tweet_default);
		}

		ParseUser creator = null;
		try {
			creator = object.getParseUser("createdBy");
			holder.ownerTextView.setText(creator.getUsername());
		} catch (Exception ex) {
			ex.printStackTrace();
			android.util.Log.d("aaa", ex.getMessage());
		}

		holder.messageView.setText(object.getString("message"));
		return v;
	}
	
	static class TweetViewHolder {
		ParseImageView icon;
		TextView ownerTextView;
		TextView idTextView;
		TextView messageView;
	}
}
