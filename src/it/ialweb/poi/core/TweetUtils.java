package it.ialweb.poi.core;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class TweetUtils {
	
	private final static String TAG = "TweetUtils";
	
	public static final String TYPE_FOLLOWER = "TYPE_FOLLOWER";
	public static final String TYPE_FOLLOWING = "TYPE_FOLLOWING";
	public static final String TYPE_FAVORITE_TWEETS = "TYPE_FAVORITE";
	public static final String TYPE_ALL_TWEETS = "TYPE_ALL";
	public static final String TYPE_MINE_TWEETS = "TYPE_MINE";
	public static final String TYPE_ALL_USERS = "ALL_USER";
	
	public interface ITweetsUtils {
		void onResponseResult(boolean result);
	}
	

	public static boolean sendTweet(String message) {
		
		if (!AccountController.isLoggedIn()) return false;
		
		Log.d(TAG, "sendRetweet: " + message);
		ParseObject tweet = new ParseObject("Tweets");
		tweet.put("message", message);
		tweet.put("createdBy", ParseUser.getCurrentUser());
		//tweet.saveInBackground();
		tweet.pinInBackground();
		tweet.saveEventually();
		return true;
	}
	
	public static boolean sendRetweet(String message) {
		
		if (!AccountController.isLoggedIn()) return false;
		
		Log.d(TAG, "sendRetweet: " + message);
		ParseObject tweet = new ParseObject("Tweets");
		tweet.put("message", message);
		tweet.put("createdBy", ParseUser.getCurrentUser());
		tweet.saveInBackground();
		
		return true;
	}
	
	public static ParseUser getUserById(String userId) {
		ParseUser user;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		try {
			user = query.get(userId);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return user;
	}
	public static void addFavorite(String tweetId) {
		if (!AccountController.isLoggedIn()) return;
		
		Log.d(TAG, "addFavorite: " + tweetId);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweets");
		query.whereEqualTo("objectId", tweetId);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (list.size() > 0) {
					ParseUser currentUser = ParseUser.getCurrentUser();
					ParseRelation<ParseObject> tweetRelation = currentUser.getRelation("favorites");
					tweetRelation.add(list.get(0));
					currentUser.saveInBackground();
				}
			}
		});
	}
	
	
	public static boolean follow(ParseObject user, final ITweetsUtils listener) {
		if (!AccountController.isLoggedIn()) return false;
		
		Log.d(TAG, "follow: " + user.getString("username"));
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> followRelation = currentUser.getRelation("follows");
		followRelation.add(user);
		currentUser.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				Log.d("TWEET UTILS", "followed");
				if (listener != null) listener.onResponseResult(e == null);
				if (e != null) Log.e("TWEET UTILS", e.getMessage());
			}
		});

		return true;
	}
	
	public static boolean unfollow(ParseObject user, final ITweetsUtils listener) {
		if (!AccountController.isLoggedIn()) return false;
		
		Log.d(TAG, "unfollow: " + user.getString("username"));
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> followRelation = currentUser.getRelation("follows");
		followRelation.remove(user);
		currentUser.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				Log.d("TWEET UTILS", "unfollowed");
				if (listener != null) listener.onResponseResult(e == null);
				if (e != null) Log.e("TWEET UTILS", e.getMessage());
			}
		});

		return true;
	}
	
	public static boolean removeFavorite(ParseObject tweet) {
		if (!AccountController.isLoggedIn()) return false;
		
		Log.d(TAG, "removeFavorite: " + tweet.getString("message"));
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> tweetRelation = currentUser.getRelation("favorites");
		tweetRelation.remove(tweet);
		currentUser.saveInBackground();
		return true;
	}
	
	public static ParseQuery<ParseObject> getFollowingQuery(Context context) {
		if (!AccountController.isLoggedIn()) return null;
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> followRelation = currentUser.getRelation("follows");
		ParseQuery<ParseObject> query =  followRelation.getQuery();
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}
	
	public static ParseQuery<ParseObject> getFollowersQuery(Context context) {
		if (!AccountController.isLoggedIn()) return null;
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> followRelation = currentUser.getRelation("followedBy");
		ParseQuery<ParseObject> query =  followRelation.getQuery();
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}

	public static ParseQuery<ParseObject> getFavoritesQuery(Context context) {
		if (!AccountController.isLoggedIn()) return null;
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseRelation<ParseObject> favoriteRelation = currentUser.getRelation("favorites");
		ParseQuery<ParseObject> query = favoriteRelation.getQuery();
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}

	public static ParseQuery<ParseObject> getAllTweetsQuery(Context context) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweets");
		query.orderByDescending("updatedAt");
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}

	public static ParseQuery<ParseObject> getMineTweetsQuery(Context context) {
		if (!AccountController.isLoggedIn()) return null;
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweets");
		query.orderByDescending("updatedAt");
		query.whereEqualTo("createdBy", currentUser);
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}

	public static ParseQuery<ParseUser> getAllUserQuery(Context context) {
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		return isNetworkAvailable(context) ? query : query.fromLocalDatastore();
	}
	
	private static boolean isNetworkAvailable(Context context) {
		final Context c = context;
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null; 
	}
}
