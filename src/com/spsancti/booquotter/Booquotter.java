package com.spsancti.booquotter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class Booquotter extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Add your initialization code here
    Parse.initialize(this, "01PRrsZ0tuLhR8pyPLyNDuWQUDxhSd2Mi7YO5sHO", "fLPOE4sZWmrKe2HpSwyewqCsmKX7MFOauzgALnAF");


    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
      
    // If you would like all objects to be private by default, remove this line.
    defaultACL.setPublicReadAccess(true);
    
    ParseACL.setDefaultACL(defaultACL, true);//dd
  }
}
