package com.spsancti.booquotter.servicing;

import org.geometerplus.android.fbreader.api.ApiClientImplementation;
import org.geometerplus.android.fbreader.api.ApiClientImplementation.ConnectionListener;
import org.geometerplus.android.fbreader.api.ApiException;
import org.geometerplus.android.fbreader.api.ApiListener;

import com.spsancti.booquotter.Booquotter;
import com.spsancti.booquotter.R;
import com.spsancti.booquotter.ui.DummyActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("RtlHardcoded")
public class HeadService extends Service implements ConnectionListener, ApiListener{
	String TAG = "HeadService";

	private WindowManager windowManager;
	ApiClientImplementation api;
	private View Head;
	
	
	@Override //From Service
	public IBinder onBind(Intent intent) {
	  return null;
	}

	protected void makeFloatingWindow(int resourceId) {
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Head =  View.inflate(this, resourceId, null);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
												100, //spike, this can disable a built-in brightness slider which is 120dp width from left
										    	WindowManager.LayoutParams.WRAP_CONTENT,
										    	WindowManager.LayoutParams.TYPE_PHONE,
										    	WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
										    	PixelFormat.TRANSLUCENT);											    
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0; params.y = 0;		    
		windowManager.addView(Head, params);
	}	
	
	@Override //From Service
	public void onCreate() {
	  super.onCreate();
	  
	  api = new ApiClientImplementation(this, this);
	  api.connect();
	  
	  makeFloatingWindow(R.layout.activity_menu); 	  
	}
	


	public String getTextForTitleShare(){
		try {
			String text = "Я сейчас читаю \"" + api.getBookTitle() + "\".";
			return text;
		}
		catch (ApiException e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.fbreader_api_exception, Toast.LENGTH_LONG).show();
			stopSelf();
			return null;
		}		
	}
	
	public void postCurrentBookToFB(View v){
		try {
			String text = getTextForTitleShare();
			if(!Booquotter.fp.isLoggedIn()){
				Intent i = new Intent(this, DummyActivity.class);
				i.putExtra("Facebook", text);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(i);
			}
			else {
				Booquotter.fp.setActivity(this);
				Booquotter.fp.post(text);	
			}
		}
		catch (ActivityNotFoundException e) {e.printStackTrace();}
		catch (NullPointerException e) 		{e.printStackTrace();}
	}
	public void postCurrentBookToTwitter(View v){
		try {
			String text = getTextForTitleShare();
			if(!Booquotter.tp.isLoggedIn()){
				Intent i = new Intent(this, DummyActivity.class);
				i.putExtra("Twitter", text);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplication().startActivity(i);
			}
			else {
				Booquotter.tp.setActivity(this);
				Booquotter.tp.post(text);	
			}
			
		}
		catch (ActivityNotFoundException e) {e.printStackTrace();}
		catch (NullPointerException e) 		{e.printStackTrace();}
	}
	
	@Override //From Service
	public void onDestroy() {
	  super.onDestroy();
	  if(Head != null) windowManager.removeView(Head);
	  if(api  != null) api.disconnect();
	}

	
	@Override //From ConnectionListener
	public void onConnected() {
		Toast.makeText(this, "Connected to FBReader", Toast.LENGTH_SHORT).show();
		api.addListener(this);
		try {
			api.addSelectionHandler("shareClick", true, R.drawable.abc_ab_stacked_solid_light_holo, 0);
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

	@Override //From ApiListener
	public void onEvent(String event) {
		Toast.makeText(this, "Got event:" +event, Toast.LENGTH_SHORT).show();
		
	}
}

