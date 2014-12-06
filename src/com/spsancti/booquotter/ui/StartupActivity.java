package com.spsancti.booquotter.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle; 
import android.view.View;
import android.widget.Toast;

import com.spsancti.booquotter.R;
import com.spsancti.booquotter.servicing.HeadService;

public class StartupActivity extends Activity {
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_startup);
		pm = getPackageManager();		
	}
	
	@Override
	protected void onPause(){
		super.onPause();		
	}
	@Override
	protected void onResume(){
		super.onResume();		
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "I'm done! Goodbye!", Toast.LENGTH_SHORT).show();
	}
	public void onClick(View v){
		switch(v.getId()){
		case R.id.pbStartReading:{
			startReading();
			finish();
		}break;
		
		default:
			Toast.makeText(this, "Unsupported ID: "+v.getId(), Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void startReading(){
		launchFBReader();
		startService(new Intent(this, HeadService.class));
	}
	
	protected void launchFBReader() {
        Intent launchIntent = pm.getLaunchIntentForPackage(getResources().getString(R.string.fbreader_package));
        if (launchIntent == null) {
            try {
            	startActivity(new Intent(Intent.ACTION_VIEW,
            			Uri.parse("market://details?id=org.geometerplus.zlibrary.ui.android")));            
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                		Uri.parse("http://play.google.com/store/apps/details?id=org.geometerplus.zlibrary.ui.android")));
            }
        } else {
            startActivity(launchIntent);
        }
    }
}
