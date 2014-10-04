package com.spsancti.booquotter;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.spsancti.booquotter.R;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TwitterPoster tp = new TwitterPoster(this);

		/*tp.login();
		tp.postTweet("My tweet from application!");
		*/
		ParseAnalytics.trackAppOpened(getIntent());
	}
}
