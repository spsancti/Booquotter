package com.spsancti.booquotter.UI;

import com.spsancti.booquotter.R;
import com.spsancti.booquotter.Servicing.BgService;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class StartupActivity extends Activity {
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pm = getPackageManager();
		launchFBReader();
		startService(new Intent(this, BgService.class));
		
		finish();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		//stopService(new Intent(this, BgService.class));
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Toast.makeText(this, "StartupActivity destroyed", Toast.LENGTH_SHORT).show();
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
