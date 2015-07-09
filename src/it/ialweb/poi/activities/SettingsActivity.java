package it.ialweb.poi.activities;

import it.ialweb.poi.R;
import it.ialweb.poi.core.App;
import it.ialweb.poi.core.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends AppCompatActivity
{
	SwitchCompat mSwitchNotification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		
		mSwitchNotification = (SwitchCompat) findViewById(R.id.switchEnableNotification);
		mSwitchNotification.setChecked(Settings.isNotificationEnabled(getApplicationContext()));
		mSwitchNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				App app = (App) getApplication();
				
				if (isChecked) {
					if (!app.enableNotification())
						mSwitchNotification.setChecked(false);
					else mSwitchNotification.setChecked(isChecked);
				} else {
					if (!app.disableNotification())
						mSwitchNotification.setChecked(true);
					else mSwitchNotification.setChecked(isChecked);
				}
			}
		});
	}
}
