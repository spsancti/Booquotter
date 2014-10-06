package com.spsancti.booquotter;

public interface SocialPoster {
	public void login();
	public void logout();
	//public boolean isLoggedIn();
	public void post(String text);
}
