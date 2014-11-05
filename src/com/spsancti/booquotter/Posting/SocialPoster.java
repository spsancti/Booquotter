package com.spsancti.booquotter.posting;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;

public abstract class SocialPoster {	
	protected Context context;
	
	protected String lastPost;
	
	public abstract void login() 	throws ActivityNotFoundException;
	public abstract void logout() 	throws ActivityNotFoundException;
	public abstract boolean isLoggedIn() 	throws ActivityNotFoundException;
	public abstract void post(String text) 	throws ActivityNotFoundException, NullPointerException;

	public void setActivity(Context c){
		context = c;
	}
	
	public void doFinish() {
		if(context instanceof Activity){
			((Activity)context).finish();
			setActivity(null);
		}		
	}
}
