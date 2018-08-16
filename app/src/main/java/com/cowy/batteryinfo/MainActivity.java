package com.cowy.batteryinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.cowy.batteryinfo.ServicesThread.MonitorBatteryStateService;
import com.cowy.batteryinfo.SettingsCompat.SettingsCompat;
import android.support.v7.app.AppCompatActivity;
import com.cowy.batteryinfo.FPS.FPSService;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext;
    public int GetmemoryUsed(){
        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        int usedmemory=(int)totalMemory;

    	/*		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		double availableMegs = mi.availMem / 0x100000L;
		Double d = new Double(availableMegs);
		int usedmemory = d.intValue();

//Percentage can be calculated for API 16+
		double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;*/
        return usedmemory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Mem", String.valueOf(GetmemoryUsed()));

        Switch switchButton = (Switch) findViewById(R.id.switch1);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    startService(new Intent(MainActivity.this, FPSService.class).putExtra(FPSService.FPS_COMMAND, FPSService.FPS_COMMAND_OPEN));
                } else {
                    startService(new Intent(MainActivity.this, FPSService.class).putExtra(FPSService.FPS_COMMAND, FPSService.FPS_COMMAND_CLOSE));

                }
            }
        });

       if (!ApplicationCore.isServiceRunning(this, MonitorBatteryStateService.class.getName())) {
            //Log.v(TAG, "Monitoring service is not running, starting it...");
            this.getApplicationContext().startService(new Intent(this.getApplicationContext(), MonitorBatteryStateService.class));
        }



        /*Intent receiver = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        float voltage = receiver.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        Log.d("Battery.java","Voltage "+String.valueOf(voltage/1000));

        BatteryManager manager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        int value = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.d("Battery.java","%Pin "+String.valueOf(value));

        value = receiver.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        Log.d("Battery.java","Nhiệt độ Pin "+String.valueOf(value));

        float valueFloat = -manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)/1000;
        Log.d("Battery.java","Số ampe " +String.valueOf(valueFloat));
        ////âm đang dùng pin, dương đang sạc vào

        Log.d("Battery.java","CPU " + getCommand("cat sys/class/thermal/thermal_zone7/temp"));*/


    }

    @Override
    public void onClick(View v) {
        //setContentView(R.layout.activity_main);
        switch (v.getId()) {
            case R.id.button2: {
                SettingsCompat.manageDrawOverlays(this);
                break;
            }
            case R.id.button1: {
                Intent myIntent = new Intent(MainActivity.this, Services.class);
                this.startService(myIntent);
                break;
            }

        }
    }

}
