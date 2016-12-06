package com.fisher.hoverer.launcher;

import android.content.Intent;
import android.os.IBinder;

import com.fisher.hoverer.pop.FloatView;
import com.fisher.utils.app.BaseService;

/**
 * Service to show pop window.
 */
public class ServicePopWindow extends BaseService {

	private FloatView floatView;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flush();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		floatView.fnDestroyFloatView();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void init() {
		floatView = new FloatView(this)
				.init();
	}

	private void flush() {
//		floatView.flushImage();
	}

}
