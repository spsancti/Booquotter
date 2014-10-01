package com.spsancti.booquotter;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.spsancti.booquotter.R;
import com.parse.ParseException;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseUser user = new ParseUser();
		user.setUsername("spsancti");
		user.setPassword("tplp12e");
		user.setEmail("email@example.com");
		  
		// other fields can be set just like with ParseObject
		user.put("phone", "0503502525");
		  
		user.signUpInBackground(new SignUpCallback() {

		@Override
		public void done(ParseException e) {
			 if (e == null) {
			      // Hooray! Let them use the app now.
			    } else {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    }
		}

		});
		ParseAnalytics.trackAppOpened(getIntent());
	}
}
