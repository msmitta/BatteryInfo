package com.cowy.batteryinfo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Services extends Service {

    public String TAG = Services.class.getSimpleName();

    public Services(Context context) {
        Log.i(TAG, "Service được khởi tạo");

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service được hủy");
        super.onDestroy();
    }


}
