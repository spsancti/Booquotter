package com.spsancti.booquotter.servicing;

import org.geometerplus.android.fbreader.api.ApiClientImplementation;
import org.geometerplus.android.fbreader.api.ApiClientImplementation.ConnectionListener;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BgService extends Service implements ConnectionListener{
	String TAG = "BGService";
	private ApiClientImplementation api;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(getBaseContext(), "Hello from service", Toast.LENGTH_LONG).show();
	    api = new ApiClientImplementation(this, this);
		return startId;
	}
	
	@Override
	public void onDestroy(){
		Toast.makeText(getBaseContext(), "Goodbye from service", Toast.LENGTH_LONG).show();
	}

	
	@Override
	public void onConnected() {
		
		
	}

}
