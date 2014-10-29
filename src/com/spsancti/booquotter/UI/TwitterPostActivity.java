package com.spsancti.booquotter.UI;

import com.spsancti.booquotter.Booquotter;
import com.spsancti.booquotter.R;

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
    	Booquotter.tp.setActivity(this);

	    Intent intent 	 = getIntent();
	    String action 	 = intent.getAction();
	    String type 	 = intent.getType();

	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	        if ("text/plain".equals(type)) {
	        	Booquotter.tp.post(intent.getStringExtra(Intent.EXTRA_TEXT));
	        }
	    }
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Booquotter.tp.setActivity(null);
	}
}
