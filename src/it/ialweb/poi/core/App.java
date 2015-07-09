package it.ialweb.poi.core;

import it.ialweb.poi.R;
import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class App extends Application {
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
		
		//ParsePush.unsubscribeInBackground("");

		ParsePush.subscribeInBackground("", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("App",
							"successfully subscribed to the broadcast channel.");
				} else {
					Log.e("App", "failed to subscribe for push", e);
				}
			}
		});
	}
}
