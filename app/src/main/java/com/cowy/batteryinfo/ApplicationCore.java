package com.cowy.batteryinfo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

public class ApplicationCore extends Application {

	private static final String TAG = "ApplicationCore";

	public static boolean isServiceRunning(final Context ctx, final String serviceName) {
		Log.v(ApplicationCore.TAG, "Checking if the monitoring service is running or not...");
		boolean serviceRunning = false;
		final ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		final List<ActivityManager.RunningServiceInfo> runningServicesInfo = am.getRunningServices(50);
		for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServicesInfo) {
			if (runningServiceInfo.service.getClassName().equals(serviceName) && runningServiceInfo.started) {
				serviceRunning = true;
			}
		}
		return serviceRunning;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
