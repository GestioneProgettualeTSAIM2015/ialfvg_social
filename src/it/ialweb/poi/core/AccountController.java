package it.ialweb.poi.core;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AccountController {
	
	private final static String TAG = "AccountController";
	
	public static boolean isLoggedIn() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		if (currentUser != null) return currentUser.isAuthenticated();
		else return false;
	}
	
	public static void register(String username, String password, String email, SignUpCallback callback) {
		Log.i(TAG, "Register: " + username + ":" + password + ":" + email);
		
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);

		user.signUpInBackground(callback);
		user.pinInBackground();
		user.saveEventually();
	}
	
	public static void logIn(String username, String password, LogInCallback callback) {
		Log.i(TAG, "Log in: " + username + ":" + password);
		ParseUser.logInInBackground(username, password, callback);
	}
	
	public static void anonymousLogIn(LogInCallback callback) {
		Log.i(TAG, "Anonymous log in");
		ParseAnonymousUtils.logIn(callback);
	}
	
	public static void logOut() {
		Log.i(TAG, "Log out");
		ParseUser.logOut();		
	}
}
