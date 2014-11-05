package com.spsancti.booquotter.ui;

import com.parse.ParseFacebookUtils;
import com.spsancti.booquotter.Booquotter;
import com.spsancti.booquotter.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class DummyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_dummy);
		Intent 		i =   getIntent();
		Bundle bundle = i.getExtras();
		
		if(bundle.containsKey("Twitter")){
			Booquotter.tp.setActivity(this);
			Booquotter.tp.post(bundle.getString("Twitter", null));
		}
		else if(bundle.containsKey("Facebook")){
			Booquotter.fp.setActivity(this);
			Booquotter.fp.post(bundle.getString("Facebook", null));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);	  
	}
}
