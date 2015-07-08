package it.ialweb.poi.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	
	private static final String ENABLE_NOTIFICATION = "ENABLE_NOTIFICATION";

	public static void enableNotification(Context context, boolean isEnable)
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putBoolean(ENABLE_NOTIFICATION, isEnable).apply();
	}
	
	public static boolean isNotificationEnabled(Context context)
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean isChecked = sharedPreferences.getBoolean(ENABLE_NOTIFICATION, true);
		return isChecked;
	}
}
