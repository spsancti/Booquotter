package com.spsancti.booquotter.servicing;

import java.util.List;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("RtlHardcoded")
public class HeadService extends Service implements ConnectionListener, ApiListener{
	String TAG = "HeadService";
	
	static final String EVENT_POST_TWITTER = "event.type.post.twitter"; 
	static final String EVENT_POST_FACEBOOK = "event.type.post.facebook";
	
	private WindowManager windowManager;
	private ApiClientImplementation api;
	private View Head;
	
	@Override //From Service
	public IBinder onBind(Intent intent) {
		return null;
	}	
	@Override //From Service
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		showFloatingWindow(R.layout.activity_menu);
		api = new ApiClientImplementation(this, this);
	}
	@Override //From Service
	public void onDestroy() {
	  super.onDestroy();
	  myStopSelf(false);
	}

	public void myStopSelf(boolean needStop){
		hideFloatngWindow();
		if(needStop){
			if(api != null) api.disconnect();	
			stopSelf();			
			Log.i(TAG, "I'm leaving you, Lord!");
		}
	}
	
	protected void showFloatingWindow(int resourceId) {
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
		Log.d(TAG, "Head added to window");
	}	
	protected void hideFloatngWindow(){
		if(Head != null){
			windowManager.removeView(Head);
			Head = null;
		}
		Log.d(TAG, "Head removed from window");
	}
	
	protected String constructTitleShare(){
		try {
			return  getBaseContext().getString(R.string.post_now_reading_start) 
					+ api.getBookTitle() 
					+ getBaseContext().getString(R.string.post_now_reading_end);			
		}
		catch (ApiException e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.fbreader_api_exception, Toast.LENGTH_LONG).show();
			stopSelf();
			return null;
		}		
	}
	protected String constructQuote(String text, List<String> authors){
		if(!authors.isEmpty()){
			if(!text.endsWith("."))	text += ".";
			text += " - " + authors.get(0).replaceAll("\\((.*?)\\)|[ (]|)", "");
		}
		return text;
	}
	
	public void postToFB(String text){
		try {			
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
	public void postToTwitter(String text){
		try {
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
	
	public void onClick(View v){
		switch(v.getId()){
			case R.id.pbTweet:{
				postToTwitter(constructTitleShare());break;
			}
			case R.id.pbFB:{
				postToFB(constructTitleShare()); 	 break;
			}
			case R.id.pbExit:{
				myStopSelf(true); 	 				 break;
			}
			default:{
				Toast.makeText(this, "Got view:" + String.valueOf(v.getId()) + "\nFinishing...", Toast.LENGTH_LONG).show();
				myStopSelf(true);
			}
		}
	}	
			
	@Override //From ConnectionListener
	public void onConnected() {
		Toast.makeText(this, "Connected to FBReader", Toast.LENGTH_SHORT).show();
		api.addListener(this);		
		try {	
			api.addSelectionHandler("@"+EVENT_POST_TWITTER,  false, getPackageName()+":drawable/twitter", 0);
			api.addSelectionHandler("@"+EVENT_POST_FACEBOOK, false, getPackageName()+":drawable/facebook", 1);
		} catch (ApiException e) {
			e.printStackTrace();
			myStopSelf(true);
		}
	}
	@Override //From ConnectionListener
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected from FBReader", Toast.LENGTH_SHORT).show();
		myStopSelf(true);	
	}

	@Override //From ApiListener
	public void onEvent(String event) {
		Toast.makeText(this, "Got event: " + event, Toast.LENGTH_SHORT).show();
		try{	
			if(event.equalsIgnoreCase(EVENT_READ_MODE_OPENED)){
		         showFloatingWindow(R.layout.activity_menu);
			}
			else if(event.equalsIgnoreCase(EVENT_READ_MODE_CLOSED)){
		         hideFloatngWindow();	         
			}
			else if(event.equalsIgnoreCase(EVENT_POST_TWITTER)){
				postToTwitter(constructQuote(api.getSelectedText(), api.getBookAuthors()));
			}
			else if(event.equalsIgnoreCase(EVENT_POST_FACEBOOK)){
				postToFB(constructQuote(api.getSelectedText(), api.getBookAuthors()));
			}
		} catch(ApiException aex){aex.printStackTrace();}
	}
}

