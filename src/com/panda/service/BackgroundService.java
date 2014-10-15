package com.panda.service;

import com.panda.view.InvisibleView;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

public class BackgroundService extends Service {

	InvisibleView mView;

	public BackgroundService() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Toast.makeText(getBaseContext(), "onCreate",
		// Toast.LENGTH_SHORT).show();

		mView = new InvisibleView(this);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(mView, params);

	}

	// use flag START_STICKY to keep the service running
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, START_STICKY);
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "onServiceDestroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();

		if (mView != null) {
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.removeView(mView);
			mView = null;
		}

		Intent localIntent = new Intent();
		localIntent.setClass(this, BackgroundService.class);
		this.startService(localIntent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
