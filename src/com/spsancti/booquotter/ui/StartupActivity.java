package com.spsancti.booquotter.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle; 
import android.widget.Toast;

import com.spsancti.booquotter.R;
import com.spsancti.booquotter.servicing.HeadService;

public class StartupActivity extends Activity {
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pm = getPackageManager();
		launchFBReader();
		startService(new Intent(this, HeadService.class));
		//finish();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		launchFBReader();		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "StartupActivity destroyed", Toast.LENGTH_SHORT).show();
		stopService(new Intent(this, HeadService.class));
	}
	
	protected void launchFBReader() {
        Intent launchIntent = pm.getLaunchIntentForPackage(getResources().getString(R.string.fbreader_package));
        if (launchIntent == null) {
            try {
            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.geometerplus.zlibrary.ui.android")));            
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=org.geometerplus.zlibrary.ui.android")));
            }
        } else {
            startActivity(launchIntent);
        }
    }
}
