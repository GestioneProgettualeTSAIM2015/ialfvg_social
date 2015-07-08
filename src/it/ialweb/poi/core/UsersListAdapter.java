package it.ialweb.poi.core;

import it.ialweb.poi.R;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
		super.getItemView(object, v, parent);
		UserViewHolder holder;
		
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
		
		holder.userIdTextView.setText(object.getString("objectId"));
		holder.ownerTextView.setText(object.getString("username"));
		holder.follow.setActivated(false);
		
		ParseFile imageFile = object.getParseFile("image");
		if (imageFile != null) {
			holder.icon.setParseFile(imageFile);
			holder.icon.loadInBackground();
		} else {
			holder.icon.setBackgroundResource(R.drawable.tweet_default);
		}
		
		return v;
	}
	
	static class UserViewHolder {
		ParseImageView icon;
		TextView userIdTextView;
		TextView ownerTextView;
		SwitchCompat follow;
	}

}
