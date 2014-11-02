package com.spsancti.booquotter.servicing;

import com.spsancti.booquotter.R;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class HeadService extends Service{
	String TAG = "BGService";

	private WindowManager windowManager;
	private View Head;

	@Override 
	public IBinder onBind(Intent intent) {
	  // Not used
	  return null;
	}

	@Override 
	public void onCreate() {
	  super.onCreate();
	  Log.d(TAG, "Created");
	  windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

	  Head =  View.inflate(this, R.layout.activity_menu, null);

	  WindowManager.LayoutParams params = new WindowManager.LayoutParams(
									    	  95, //spike?
									    	  WindowManager.LayoutParams.WRAP_CONTENT,
									    	  WindowManager.LayoutParams.TYPE_PHONE,
									    	  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
									    	  PixelFormat.TRANSLUCENT);
										    
	  params.gravity = Gravity.TOP | Gravity.LEFT;
	  params.x = 0;params.y = 0;
	    
	  windowManager.addView(Head, params);
	}

	@Override
	public void onDestroy() {
	  super.onDestroy();
	  if (Head != null) windowManager.removeView(Head);
	  Log.d(TAG, "Destroyed");
	}
}

