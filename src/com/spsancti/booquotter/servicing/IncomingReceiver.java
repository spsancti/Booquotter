package com.spsancti.booquotter.servicing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class IncomingReceiver extends BroadcastReceiver {
	private String TAG = "IncomingReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.d(TAG, intent.getAction());
//		Bundle bundle = intent.getExtras();
//		if(bundle == null) return;
//		for (String key : bundle.keySet()) {
//		    Object value = bundle.get(key);
//		    Log.d(TAG, String.format("%s %s (%s)", key,  
//		        value.toString(), value.getClass().getName()));
//		}
//		Log.d(TAG, "===================================");
	}

}
