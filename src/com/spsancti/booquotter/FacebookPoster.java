package com.spsancti.booquotter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Session.NewPermissionsRequest;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ParseException;

public class FacebookPoster implements SocialPoster{
	private static String TAG = "FacebookPoster";
	
	public static String FACEBOOK_APP_ID = "317236605127445";
	
	private Context context;
	private List<String> permissions;
	public FacebookPoster(Context c){
		context = c;
		permissions = new ArrayList<String>();
	}
	
	@Override
	public void login() {
		permissions.add(ParseFacebookUtils.Permissions.Extended.PUBLISH_ACTIONS);

		final NewPermissionsRequest newPermissionsRequest = new NewPermissionsRequest((Activity) context, permissions);

		ParseFacebookUtils.logIn((Activity) context, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
				  if (user == null) {
				      	Toast.makeText(context, R.string.facebook_login_cancelled,   Toast.LENGTH_SHORT).show();
				      	return;
				    } else if (user.isNew()) {
				    	Toast.makeText(context, R.string.facebook_signin_successful, Toast.LENGTH_SHORT).show();
				    } else {
				    	Toast.makeText(context, R.string.facebook_login_successful,  Toast.LENGTH_SHORT).show();
				   }
				  	ParseFacebookUtils.getSession().requestNewPublishPermissions(newPermissionsRequest);
					ParseFacebookUtils.saveLatestSessionData(ParseUser.getCurrentUser());
			  }			  
			});
	}

	@Override
	public void logout() {
		ParseUser.logOut();
		com.facebook.Session fbs = com.facebook.Session.getActiveSession();
		  if (fbs == null) {
		    fbs = new com.facebook.Session(context);
		    com.facebook.Session.setActiveSession(fbs);
		  }
		  fbs.closeAndClearTokenInformation();
		if(ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){
			try {
				ParseFacebookUtils.unlink(ParseUser.getCurrentUser());
			} catch (ParseException e) {e.printStackTrace();}
		}		
	}

	@Override
	public void post(String text) {

	}

}
