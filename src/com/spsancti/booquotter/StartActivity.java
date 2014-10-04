package com.spsancti.booquotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.spsancti.booquotter.R;

public class StartActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		FacebookPoster tp = new FacebookPoster(this);
		
		//tp.logout();
		tp.login();
		tp.post("My post from application!");
		
		ParseAnalytics.trackAppOpened(getIntent());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
}
