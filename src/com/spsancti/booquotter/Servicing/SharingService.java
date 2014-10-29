package com.spsancti.booquotter.Servicing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SharingService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(getBaseContext(), "Hello from service", Toast.LENGTH_LONG).show();
		return 0;		
	}
	
	@Override
	public void onDestroy(){
		Toast.makeText(getBaseContext(), "Goodbye from service", Toast.LENGTH_LONG).show();
	}

}
