package com.cowy.batteryinfo.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;


import com.cowy.batteryinfo.CPU.CPUmonitor;
import com.cowy.batteryinfo.ServicesThread.MonitorBatteryStateService;

import static android.content.Context.ACTIVITY_SERVICE;

public class BatteryChangedReceiver extends BroadcastReceiver {

	private MonitorBatteryStateService service = null;

    public String getCommand(String command){
        CPUmonitor exe = new CPUmonitor();
        String result = exe.Executer(command);
        String[] text = result.split("[n]");
        return text[0];
    }

	public BatteryChangedReceiver(final MonitorBatteryStateService service) {
		this.service = service;
	}

	@Override
	public void onReceive(final Context context, final Intent intent) {
        BatteryManager manager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
		ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);


        //final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        final int GPUtemp = Integer.valueOf(getCommand("cat sys/class/thermal/thermal_zone12/temp"));
        final int CPUtemp = Integer.valueOf(getCommand("cat sys/class/thermal/thermal_zone7/temp"));
/*		final int GPUtemp = 0;
		final int CPUtemp = 0;*/
		//final int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		final int powerAmpe = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
		final int BATTtemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);

		Double avail = new Double(memInfo.availMem / 0x100000L);
		Double total = new Double(memInfo.totalMem  / 0x100000L);
		int availmemory = avail.intValue();
		int totalmemory = total.intValue();




		this.service.insertPowerValue(powerAmpe, CPUtemp, BATTtemp, GPUtemp, memInfo.lowMemory, availmemory, totalmemory);

	}


}
