package com.cowy.batteryinfo.FPS;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.cowy.batteryinfo.FPS.debugOverlay.DebugOverlayController;

public class FPSService extends Service {
  public static final String TAG = "FPSService";
  public static final String FPS_COMMAND = "FPS_COMMAND";
  public static final String FPS_COMMAND_OPEN = "FPS_COMMAND_OPEN";
  public static final String FPS_COMMAND_CLOSE = "FPS_COMMAND_CLOSE";
  private DebugOverlayController mDebugOverlayController;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    String command = intent.getStringExtra(FPS_COMMAND);
    if (command != null) {
      if (command.equals(FPS_COMMAND_OPEN)) {
        openFPS();
      } else if (command.equals(FPS_COMMAND_CLOSE)) {
        closeFPS();
      }
    }

    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onDestroy() {
    stopService(new Intent(this, FPSService.class));
    super.onDestroy();
  }

  public void openFPS() {
    if (mDebugOverlayController == null) {
      mDebugOverlayController = new DebugOverlayController(this);
    }
    mDebugOverlayController.setFpsDebugViewVisible();
  }

  public void closeFPS() {
    if (mDebugOverlayController != null) {
      mDebugOverlayController.stopFps();
    }
  }
}
