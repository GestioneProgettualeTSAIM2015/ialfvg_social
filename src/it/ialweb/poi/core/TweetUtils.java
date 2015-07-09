package it.ialweb.poi.core;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class TweetUtils {

	public static boolean sendTweet(String message) {
		
		if (!AccountController.isLoggedIn()) return false;
		
		ParseObject tweet = new ParseObject("Tweets");
		tweet.put("message", message);
		tweet.put("createdBy", ParseUser.getCurrentUser());
		tweet.saveInBackground();
		
		return true;
	}
	
	public static boolean sendRetweet(String message) {
		
		if (!AccountController.isLoggedIn()) return false;
		
		ParseObject tweet = new ParseObject("Tweets");
		tweet.put("message", message);
		tweet.put("createdBy", ParseUser.getCurrentUser());
		tweet.saveInBackground();
		
		return true;
	}
	
	public static boolean follow(String followedUserId) {
		
		if (!AccountController.isLoggedIn()) return false;
		
		ParseObject follow = new ParseObject("Followers");
		follow.put("followedUserId", followedUserId);
		follow.put("userId", ParseUser.getCurrentUser().getObjectId());
		follow.saveInBackground();
		
		return true;
	}
}
