package com.crazytech.miniwl;

import android.app.ProgressDialog;


public interface TaskCallbacks {
	public void onPreExecute();
	public void onProgressUpdate(Object... params);
	public void onCancelled();
	public void onPostExecute();
	public ProgressDialog getProgressDialog();
	public void setProgressDialog(ProgressDialog pd);
}
