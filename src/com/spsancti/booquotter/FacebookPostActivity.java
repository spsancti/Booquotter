package com.spsancti.booquotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FacebookPostActivity extends Activity {
	private static String TAG = "FacebookPostActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.facebook_post_activity);
	    
	    FacebookPoster fp = new FacebookPoster(this);
	    Intent intent 	  = getIntent();
	    String action 	  = intent.getAction();
	    String type 	  = intent.getType();

	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	        if ("text/plain".equals(type)) {
	        	fp.post(intent.getStringExtra(Intent.EXTRA_TEXT));
	        }
	    }
	}

}
