package com.crazytech.miniwl;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Object, String> {
	private TaskCallbacks taskCallback;
	public ProgressDialog pd;
	
	public MyAsyncTask(TaskCallbacks taskCallback) {
		super();
		this.taskCallback = taskCallback;
	}

	@Override
	protected void onPreExecute() {
		if (taskCallback!=null){
			taskCallback.onPreExecute();
			pd = taskCallback.getProgressDialog();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (taskCallback!=null)taskCallback.onPostExecute();
		super.onPostExecute(result);
	}
	
	@Override
	protected void onCancelled() {
		if (taskCallback!=null)taskCallback.onCancelled();
		super.onCancelled();
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		if (taskCallback!=null)taskCallback.onProgressUpdate(values);
		super.onProgressUpdate(values);
	}
}
