package com.spsancti.booquotter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ParseException;

public class FacebookPoster implements SocialPoster{
	private static String TAG 			  = "FacebookPoster";
	public  static String FACEBOOK_APP_ID = "317236605127445";
	
	private Context context;
	private List<String> permissions;
	
	/*
	 * To make it work you shall pass @Activity, not the @Context
	 */
	public FacebookPoster(Context c){
		context = c;
		permissions = new ArrayList<String>();
	}
	
	/*
	 * Calls built in activity in facebook to open login dialog
	 * You must call it prior to public void @post()
	 */
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

	/*
	 * Call this whenever you want 
	 */	
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

	/*
	 * Call this after login
	 */
	@Override
	public void post(String text) {		
		Bundle params = new Bundle();
		params.putString("message", text);
	
		new Request(Session.getActiveSession(), "/me/feed", params, HttpMethod.POST, new Request.Callback() {
			@Override
	        public void onCompleted(Response response) {
				FacebookRequestError error = response.getError();
				if(error != null){
					Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, R.string.facebook_post_successful, Toast.LENGTH_LONG).show();
				}
			}
		   }
		).executeAsync();
	}
}
