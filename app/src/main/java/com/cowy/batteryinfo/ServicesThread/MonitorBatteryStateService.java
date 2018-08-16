package com.cowy.batteryinfo.ServicesThread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.cowy.batteryinfo.R;
import com.cowy.batteryinfo.receivers.BatteryChangedReceiver;

public class MonitorBatteryStateService extends Service {

	private static final int MY_NOTIFICATION_ID = 1;
	private static final String TAG = "BatteryStateService";
	private final Messenger serviceMessenger = new Messenger(new IncomingHandler());
	private Notification myNotification = null;
	private NotificationManager notificationManager = null;

	public void insertPowerValue(final int powerAmpe, final int CPUtemp, final int BATTtemp,  final int GPUtemp, final boolean lowmem, final int avail, final int total) {

				this.showNewPercentageNotification(powerAmpe, CPUtemp, BATTtemp, GPUtemp, lowmem, avail, total);

	}
	@Override
	public IBinder onBind(final Intent intent) {
		return this.serviceMessenger.getBinder();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startid) {

		//Log.v(MonitorBatteryStateService.TAG, "Starting service for collecting battery statistics...");

		this.notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		BatteryChangedReceiver batteryChangedReceiver = new BatteryChangedReceiver(this);
		this.registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		//Log.v(MonitorBatteryStateService.TAG, "Service successfully started");
		return Service.START_STICKY;
	}

	private void showNewPercentageNotification(final int powerAmpe, final int CPUtemp, final int BATTtemp, final int GPUtemp, final boolean lowmem, final int avail, final int total) {

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify);
		contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
		contentView.setTextViewText(R.id.cpu, String.valueOf(CPUtemp));
		contentView.setTextViewText(R.id.gpu, String.valueOf(GPUtemp));
		contentView.setTextViewText(R.id.bat, String.valueOf(Float.valueOf(BATTtemp) / 10));
		if (powerAmpe >= 0) {
            contentView.setTextViewText(R.id.ampe, String.valueOf(String.format("%.1f", Float.valueOf(-powerAmpe) / 1000)));
        }
        else
        {
            contentView.setTextViewText(R.id.ampe, "+" + String.valueOf(String.format("%.1f", Float.valueOf(-powerAmpe) / 1000)));
        }

		if (lowmem){
			contentView.setTextColor(R.id.mem, Color.rgb(255, 0, 0));
			contentView.setTextColor(R.id.memPercent, Color.rgb(255, 0, 0));
		}

		contentView.setTextViewText(R.id.mem, String.valueOf(String.format("%.2f", Float.valueOf(total - avail)/1024))+"/"+String.valueOf(String.format("%.2f", Float.valueOf(total)/1024)));
		contentView.setTextViewText(R.id.memPercent, String.valueOf(String.format("%.0f", ((Float.valueOf(total) - Float.valueOf(avail))/Float.valueOf(total))*100))+"% ");



		if (CPUtemp > 70) {
			contentView.setTextColor(R.id.cpu, Color.rgb(255, 0, 0));
		}
		if (CPUtemp <= 70 && CPUtemp >= 60) {
			contentView.setTextColor(R.id.cpu, Color.rgb(255, 128, 0));
		}
		if (CPUtemp < 60) {
			contentView.setTextColor(R.id.cpu, Color.rgb(0, 255, 0));
		}

		if (GPUtemp > 70) {
			contentView.setTextColor(R.id.gpu, Color.rgb(255, 0, 0));
		}
		if (GPUtemp <= 70 && GPUtemp >= 55) {
			contentView.setTextColor(R.id.gpu, Color.rgb(255, 128, 0));
		}
		if (GPUtemp < 55) {
			contentView.setTextColor(R.id.gpu, Color.rgb(0, 255, 0));
		}

		if (BATTtemp > 390) {
			contentView.setTextColor(R.id.bat, Color.rgb(255, 0, 0));
		}
		if (BATTtemp <= 390 && BATTtemp >= 370) {
			contentView.setTextColor(R.id.bat, Color.rgb(255, 128, 0));
		}
		if (BATTtemp < 370) {
			contentView.setTextColor(R.id.bat, Color.rgb(0, 255, 0));
		}



		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext());
		notificationBuilder.setContent(contentView);
		notificationBuilder.setSmallIcon(R.mipmap.ic_stat_00_pct_charged);
		notificationBuilder.setOngoing(true);
		//notificationBuilder.setContentIntent(PendingIntent.getActivity(this.getApplicationContext(), 0, new Intent(this.getApplicationContext(), MainActivity.class), 0));
		notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);




		// get the created notification and show it
		this.myNotification = notificationBuilder.build();
		this.notificationManager.notify(MonitorBatteryStateService.MY_NOTIFICATION_ID, this.myNotification);
	}

	private class IncomingHandler extends Handler {

		@Override
		public void handleMessage(final Message msg) {
/*			switch (msg.what) {
				case MonitorBatteryStateService.MSG_UPDATE_WIDGETS:
					Log.d(MonitorBatteryStateService.TAG, "Updating widgets...");
					MonitorBatteryStateService.this.showNewPercentageNotification(0,0,0,0);
					break;
				default:
					super.handleMessage(msg);
			}*/
		}
	}
}
