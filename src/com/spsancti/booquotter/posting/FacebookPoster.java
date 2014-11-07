package com.spsancti.booquotter.posting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.spsancti.booquotter.R;

public class FacebookPoster extends SocialPoster{
	private static String TAG 			  = "FacebookPoster";
	public  static String FACEBOOK_APP_ID = "317236605127445";
	
	private List<String> permissions = new ArrayList<String>();

	/*
	 * To make it work you shall pass @Activity, not the @Context
	 */
	public FacebookPoster(Context c){
		context = c;	
		permissions.add(ParseFacebookUtils.Permissions.Extended.PUBLISH_ACTIONS);
	}
	
	
	private boolean needPostAfterLogin;
	private LogInCallback pLICB = new LogInCallback() {		
		@Override
			public void done(ParseUser user, ParseException err) {
			Log.d("DEBUG", "Done FacebookPoster.LoginCallBack");
				  if (user == null) {
				      	Toast.makeText(context, R.string.facebook_login_cancelled,   Toast.LENGTH_SHORT).show();
				      	doFinish();
				      	return;
				    } else if (user.isNew()) {
				    	Toast.makeText(context, R.string.facebook_signin_successful, Toast.LENGTH_SHORT).show();
				    } else {
				    	Toast.makeText(context, R.string.facebook_login_successful,  Toast.LENGTH_SHORT).show();
				   }
				  	ParseFacebookUtils.getSession().requestNewPublishPermissions(new NewPermissionsRequest((Activity) context, permissions));
					ParseFacebookUtils.saveLatestSessionData(ParseUser.getCurrentUser());
					
					if(needPostAfterLogin){
						post(lastPost);
					}
					else doFinish();
			  }
	};
	
	@Override
	public void login() throws ActivityNotFoundException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");		
		if(ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){
			Toast.makeText(context, R.string.facebook_already_logged_in,   Toast.LENGTH_SHORT).show();
			return;
		}		
		needPostAfterLogin = false;
		ParseFacebookUtils.logIn((Activity) context, pLICB);
	}

	/*
	 * Call this whenever you want 
	 */	
	@Override
	public void logout() throws ActivityNotFoundException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");
	
		com.facebook.Session fbs = com.facebook.Session.getActiveSession();
		  if(fbs == null) {
		     fbs = new com.facebook.Session(context);
		     com.facebook.Session.setActiveSession(fbs);
		  }
		  fbs.closeAndClearTokenInformation();
		  
		if(ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())){
			try {
				ParseFacebookUtils.unlink(ParseUser.getCurrentUser());
				Toast.makeText(context, R.string.facebook_logged_out, Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				e.printStackTrace();
				Toast.makeText(context, R.string.facebook_not_logged_out, Toast.LENGTH_LONG).show();
			}
		}		
	}

	/*
	 * If you're not logged in, it will log you in and than post
	 */
	//In my opinion, there is some shit with re-logging in... needs review
	@Override
	public void post(String text) throws ActivityNotFoundException, NullPointerException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");	
		if(text    == null) throw new NullPointerException("Unfortunately, @text is null");
		lastPost = text;
		
		Bundle params = new Bundle();		

		Request.Callback callback = new Request.Callback() {
			@Override
	        public void onCompleted(Response response) {
				FacebookRequestError error = response.getError();
				if(error != null){
					Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, R.string.facebook_post_successful, Toast.LENGTH_LONG).show();
				}
				doFinish();
			}};		

		Log.d(TAG, "ENTERD POST");		
		
		//if we're not logged in, log in first and than try to post in callback!
		if(!isLoggedIn()){//it cannot log in here! need review
			Log.d(TAG, "NOT LOGGED IN");
			needPostAfterLogin = true;
			ParseFacebookUtils.logIn((Activity) context, pLICB);
		}
		else {
			//there are funky &nbsp in some books instead of simple spaces (WTF, what for..?). Replace them.
			text = text.replaceAll("([\\t\\r\\f\\xA0])", " ");
			params.putString("message", text);
			new Request(Session.getActiveSession(), "/me/feed", params, HttpMethod.POST, callback).executeAsync();
		}
	}

	@Override
	public boolean isLoggedIn() {
		return ParseFacebookUtils.isLinked(ParseUser.getCurrentUser());
	}
}
