package com.spsancti.booquotter.Posting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.spsancti.booquotter.R;

public class TwitterPoster extends SocialPoster{
	private static String TAG = "TwitterPoster";	
	public 	static String TWITTER_API_KEY 	 = "KgrlLNXIvWyzRpzo3sJbDRkgs";
	public 	static String TWITTER_API_SECRET = "OmmcA3RCyyryJHgUAs5bDtKO2cnvIZ3MkKLVh60FvVjKOI34Yz";
	
	private HttpClient   		client;
	private HttpPost	  		tweet;
	private List<NameValuePair> json;
	
	/*
	 * To make it work you shall pass @Activity, not the @Context
	 */
	public TwitterPoster(Context c){
		context = c;		
		tweet 	= new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
	}
	public TwitterPoster(){
		context = null;
		tweet 	= new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
	}
	
	/*
	 * Calls built in activity in parse to open login dialog in twitter
	 */
	@Override
	public void login() throws ActivityNotFoundException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");
		if(ParseTwitterUtils.isLinked(ParseUser.getCurrentUser())){
			Toast.makeText(context, R.string.twitter_already_logged_in,   Toast.LENGTH_SHORT).show();
			return;
		}
		ParseTwitterUtils.logIn(context, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user == null) {
			      	Toast.makeText(context, R.string.twitter_login_cancelled,   Toast.LENGTH_SHORT).show();	
			    } else if (user.isNew()) {
			    	Toast.makeText(context, R.string.twitter_signin_successful, Toast.LENGTH_SHORT).show();
			    } else {
			    	Toast.makeText(context, R.string.twitter_login_successful,  Toast.LENGTH_SHORT).show();
			    }
			  }
			});		
	}
	
	/*
	 * Call this whenever you want 
	 */	
	@Override
	public void logout() throws ActivityNotFoundException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");
		if(ParseTwitterUtils.isLinked(ParseUser.getCurrentUser())){
			try {
				ParseTwitterUtils.unlink(ParseUser.getCurrentUser());
				Toast.makeText(context, R.string.twitter_logged_out, Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				e.printStackTrace();
				Toast.makeText(context, R.string.twitter_not_logged_out, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/*
	 * If you're not logged in, it will log you in and than post
	 */
	@Override
	public void post(String text) throws ActivityNotFoundException{
		if(context == null)	throw new ActivityNotFoundException("It seems, you've forgotten to call setActivity(), dude.");
		if(text.length() > 140) {
			Toast.makeText(context, "Text length exceeds 140 symbols", Toast.LENGTH_SHORT).show();
			return;
		}
		//there are funky &nbsp in some books instead of simple spaces (WTF, what for..?). Replace them.
		text = text.replaceAll("([\\t\\r\\f\\xA0])", " ");
	
		
		//if we're not logged in, log in first and than try to post in callback!
		if(!isLoggedIn()){
			final String fText = text;
			ParseTwitterUtils.logIn(context, new LogInCallback() {
				  @Override
				  public void done(ParseUser user, ParseException err) {
				    if (user == null) {
				      	Toast.makeText(context, R.string.twitter_login_cancelled,   Toast.LENGTH_SHORT).show();
				      	return;
				    } else if (user.isNew()) {
				    	Toast.makeText(context, R.string.twitter_signin_successful, Toast.LENGTH_SHORT).show();
				    } else {
				    	Toast.makeText(context, R.string.twitter_login_successful,  Toast.LENGTH_SHORT).show();
				    }
				   new makeTweetTask().execute(fText); 
				  }
				});		
		}
		else new makeTweetTask().execute(text);		
	}
	
	private class makeTweetTask extends AsyncTask<String, Void, Integer>{

	    public String convertStreamToString(InputStream is)
	    {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            } 
	        }
	        catch (IOException e) {e.printStackTrace();}
	        finally{
	            try{
	                is.close();
	            }
	            catch (IOException e){e.printStackTrace();}
	        }
	        return sb.toString();
	    }
		
		@Override
		protected Integer doInBackground(String... params) {
			String text = new String(params[0]);
			json 		= new ArrayList<NameValuePair>();
			client  	= new DefaultHttpClient();

			 try {
				json.add(new BasicNameValuePair("status", text));
				tweet.setHeader("Content-type", "application/x-www-form-urlencoded");
			    tweet.setEntity(new UrlEncodedFormEntity(json, HTTP.UTF_8));
			    
				ParseTwitterUtils.getTwitter().signRequest(tweet);
				
				HttpResponse httpresponse = client.execute(tweet);
				HttpEntity entity = httpresponse.getEntity();
				
				Log.d(TAG, "HM " + convertStreamToString(entity.getContent()));

				return httpresponse.getStatusLine().getStatusCode();			    
			} catch (Exception e) {	e.printStackTrace();}
			
			 return null;
		}
		
//TODO: Add more response codes here
		protected void onPostExecute(Integer result){
			try {
				switch(result){
					case 200:{
				    	Toast.makeText(context, R.string.twitter_tweet_successful,  Toast.LENGTH_LONG).show();
					}break;
					
					case 401:{
				    	Toast.makeText(context, R.string.twitter_tweet_unauthorized,  Toast.LENGTH_LONG).show();
						Log.d(TAG, "Server returned 401:Unauthorized");
					}break;
					
					case 220:{
				    	Toast.makeText(context, R.string.twitter_tweet_login_failed,  Toast.LENGTH_LONG).show();
				    	logout();
				    	login();
					}break;
				}
			} catch(Exception e) { e.printStackTrace();}
		}
	}

	@Override
	public boolean isLoggedIn() {
		return ParseTwitterUtils.isLinked(ParseUser.getCurrentUser());
	}
}
