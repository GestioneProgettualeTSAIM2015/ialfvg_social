package it.ialweb.poi.core;

import it.ialweb.poi.R;
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

public class UsersListAdapter extends ParseQueryAdapter<ParseObject> {

	public UsersListAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
			public ParseQuery create() {
				ParseQuery<ParseUser> query = ParseUser.getQuery();

				return query;
			}
		});
	}

	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.user_row, null);
		}

		super.getItemView(object, v, parent);

		// Add and download the image
		ParseImageView icon = (ParseImageView) v.findViewById(R.id.userIcon);
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			icon.setParseFile(imageFile);
			icon.loadInBackground();
		} else {
			icon.setBackgroundResource(R.drawable.tweet_default);
		}

		// Add the name of user
		TextView ownerTextView = (TextView) v.findViewById(R.id.userName);
		ownerTextView.setText(object.getString("username"));
		
		// Add follow switch
		SwitchCompat follow = (SwitchCompat) v.findViewById(R.id.swFollow);
		//TODO check if im following this user)
		follow.setActivated(false);

		return v;
	}

}
