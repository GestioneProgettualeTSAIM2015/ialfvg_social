package it.ialweb.poi.core;

import it.ialweb.poi.R;
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize Crash Reporting.
		ParseCrashReporting.enable(this);

		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);

		// Add your initialization code here
		Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
	}
}
