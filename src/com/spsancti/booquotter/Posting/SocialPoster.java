package com.spsancti.booquotter.posting;

import android.content.ActivityNotFoundException;
import android.content.Context;

public abstract class SocialPoster {	
	protected Context context;
	protected boolean nullifyActivityOnPost;
	
	
	public abstract void login() 	throws ActivityNotFoundException;
	public abstract void logout() 	throws ActivityNotFoundException;
	public abstract boolean isLoggedIn() 	throws ActivityNotFoundException;
	public abstract void post(String text) 	throws ActivityNotFoundException, NullPointerException;
	public void requestNullifyActivityOnPost(boolean flag) 
		{nullifyActivityOnPost = flag;}
	public void setActivity(Context c){
		context = c;
	}
}
