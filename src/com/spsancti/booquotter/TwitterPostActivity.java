package com.spsancti.booquotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TwitterPostActivity extends Activity {
	@SuppressWarnings("unused")
	private static String TAG = "TwitterPostActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.twitter_post_activity);

	    TwitterPoster tp = new TwitterPoster(this);
	    Intent intent 	 = getIntent();
	    String action 	 = intent.getAction();
	    String type 	 = intent.getType();

	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	        if ("text/plain".equals(type)) {
	        	tp.post(intent.getStringExtra(Intent.EXTRA_TEXT));
	        }
	    }
	}
}
