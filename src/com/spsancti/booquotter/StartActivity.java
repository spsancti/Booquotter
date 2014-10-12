package com.spsancti.booquotter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.spsancti.booquotter.R;

public class StartActivity extends Activity{

	private TwitterPoster tp;
	private FacebookPoster fp;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tp = new TwitterPoster(this);
		fp = new FacebookPoster(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);	  
	}
	
	public void loginTwitter(View v){
		tp.login();
	}
	
	public void loginFacebook(View v){
		fp.login();
	}
	
	public void logout(View v){
		tp.logout();
		fp.logout();
	}

	
}
