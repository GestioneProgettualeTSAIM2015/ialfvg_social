package it.ialweb.poi.core;

import it.ialweb.poi.R;
import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class App extends Application {
	
	private static final String TAG = "App";

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize Crash Reporting.
		ParseCrashReporting.enable(this);

		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);

		// Add your initialization code here
		Parse.initialize(this, getString(R.string.parse_app_id),
				getString(R.string.parse_client_key));

		if (Settings.isNotificationEnabled(getApplicationContext()))
			enableNotification();
	}
	
	public boolean enableNotification() {
		
		if (!AccountController.isLoggedIn())
			return false;
		
		ParsePush.subscribeInBackground(getFeedChannel(), new SaveCallback() {
			
			@Override
			public void done(ParseException ex) {
				if (ex != null)
					Log.w(TAG, "sub: " + ex.getMessage());
			}
		});
		return true;
	}
	
	public boolean disableNotification() {
		
		if (!AccountController.isLoggedIn())
			return false;
		
		ParsePush.unsubscribeInBackground(getFeedChannel(), new SaveCallback() {
			@Override
			public void done(ParseException ex) {
				if (ex != null)
					Log.w(TAG, "unsub: " + ex.getMessage());
			}
		});
		return true;
	}
	
	public String getFeedChannel() {
		return "BT_" + ParseUser.getCurrentUser().getObjectId() + "_feed";
	}
}
