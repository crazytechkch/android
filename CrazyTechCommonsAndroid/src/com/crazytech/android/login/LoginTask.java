package com.crazytech.android.login;

import android.app.ProgressDialog;

public interface LoginTask {
	public void onPreExecute();
	public void onAunthenticatePostExecute(Integer result, Long id);
	public void onProgressUpdate();
	public void onCancelled();
	public ProgressDialog getProgressDialog() ;
}
