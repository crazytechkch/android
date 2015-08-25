package com.crazytech.miniwl;

import android.app.Activity;
import android.content.SharedPreferences;

import com.crazytech.android.login.LoginActivity;
import com.crazytech.miniwl.connect.Connect;

public class MyLoginActivity extends LoginActivity {

	@Override
	public void onAunthenticatePostExecute(Integer result, Long id) {
		if (result==1) setSharedPref(id,getUsername(),getUserpass());
		super.onAunthenticatePostExecute(result, id);
	}
	
	private void setSharedPref(Long id, String... params) {
		SharedPreferences sharedPref = this.getSharedPreferences(MainActivity.class.getSimpleName(), Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong("id", id);
		editor.putString("username", params[0]);
		editor.putString("password", params[1]);
		editor.commit();
	}
	
	@Override
	public String getAuthenticateUrl() {
		// TODO Auto-generated method stub
		return Connect.PHP.AUTHENTICATE;
	}
}
