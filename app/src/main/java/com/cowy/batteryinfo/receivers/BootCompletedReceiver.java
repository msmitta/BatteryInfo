package com.cowy.batteryinfo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cowy.batteryinfo.ServicesThread.MonitorBatteryStateService;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// start the monitoring service
		context.startService(new Intent(context, MonitorBatteryStateService.class));
	}

}
