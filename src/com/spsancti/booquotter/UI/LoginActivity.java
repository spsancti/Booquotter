package com.spsancti.booquotter.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseFacebookUtils;
import com.spsancti.booquotter.Booquotter;
import com.spsancti.booquotter.R;

public class LoginActivity extends Activity{


	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Booquotter.tp.setActivity(this);
		Booquotter.fp.setActivity(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);	  
	}
	
	
	@Override
	protected void onDestroy(){
		Booquotter.tp.setActivity(null);
		Booquotter.fp.setActivity(null);
	}
	
	
	public void loginTwitter(View v){
		Booquotter.tp.login();
	}
	
	public void loginFacebook(View v){
		Booquotter.fp.login();
	}
	
	public void logout(View v){
		Booquotter.tp.logout();
		Booquotter.fp.logout();
	}


	
}
