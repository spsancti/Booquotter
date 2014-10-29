package com.spsancti.booquotter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.spsancti.booquotter.Posting.FacebookPoster;
import com.spsancti.booquotter.Posting.TwitterPoster;

public class Booquotter extends Application {
	public static TwitterPoster tp;
	public static FacebookPoster fp;
	
  @Override
  public void onCreate() {
    super.onCreate();

    Parse.initialize(this, "01PRrsZ0tuLhR8pyPLyNDuWQUDxhSd2Mi7YO5sHO", "fLPOE4sZWmrKe2HpSwyewqCsmKX7MFOauzgALnAF");    
	ParseTwitterUtils.initialize(TwitterPoster.TWITTER_API_KEY, TwitterPoster.TWITTER_API_SECRET);
	ParseFacebookUtils.initialize(FacebookPoster.FACEBOOK_APP_ID);
	
    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();     

    // If you would like all objects to be private by default, remove this line.
    defaultACL.setPublicReadAccess(true);    
    ParseACL.setDefaultACL(defaultACL, true);
    
    tp = new TwitterPoster();
    fp = new FacebookPoster();
  }
}
