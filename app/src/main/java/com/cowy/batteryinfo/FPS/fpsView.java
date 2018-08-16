package com.cowy.batteryinfo.FPS;

/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.cowy.batteryinfo.R;

import java.util.Locale;

/**
 * View that automatically monitors and displays the current app frame rate. Also logs the current
 * FPS to logcat while active.
 *
 * NB: Requires API 16 for use of FpsDebugFrameCallback.
 */
@TargetApi(16)
public class fpsView extends FrameLayout {

    private static final int UPDATE_INTERVAL_MS = 500;

    private final TextView mTextView;
    private final FpsDebugFrameCallback mFrameCallback;
    private final FPSMonitorRunnable mFPSMonitorRunnable;
    private final WindowManager mWindowManager;

    public fpsView(Context reactContext) {
        super(reactContext);
        inflate(reactContext, R.layout.fps_view, this);
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mTextView = (TextView) findViewById(R.id.fps_text);
        mFrameCallback = new FpsDebugFrameCallback(ChoreographerCompat.getInstance(), reactContext);
        mFPSMonitorRunnable = new FPSMonitorRunnable();
        setCurrentFPS(0, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFrameCallback.reset();
        mFrameCallback.start();
        mFPSMonitorRunnable.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFrameCallback.stop();
        mFPSMonitorRunnable.stop();
    }

    private void setCurrentFPS(double currentFPS, double currentJSFPS) {


        String fpsString = String.format(Locale.getDefault(),"FPS %.1f",currentFPS);
        mTextView.setText(fpsString);
    }

    /**
     * Timer that runs every UPDATE_INTERVAL_MS ms and updates the currently displayed FPS.
     */
    private class FPSMonitorRunnable implements Runnable {

        private boolean mShouldStop = false;

        @Override
        public void run() {
            if (mShouldStop) {
                return;
            }
            setCurrentFPS(mFrameCallback.getFPS(), mFrameCallback.getJSFPS());
            mFrameCallback.reset();

            postDelayed(this, UPDATE_INTERVAL_MS);
        }

        public void start() {
            mShouldStop = false;
            post(this);
        }

        public void stop() {
            mShouldStop = true;
        }
    }
}
