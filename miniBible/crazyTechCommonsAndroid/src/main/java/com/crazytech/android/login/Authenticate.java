package com.crazytech.android.login;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.crazytech.commons.R;
import com.crazytech.json.JSONParser;
import com.crazytech.util.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Authenticate extends AsyncTask<String, Integer, String> {
	private ProgressDialog pd;
	private Context context;
	private boolean success;
	private LoginTask loginTask;
	private Long id;
	
	public Authenticate(Context context, LoginTask loginTask) {
		super();
		this.context = context;
		this.loginTask = loginTask;
	}

	@Override
	protected void onPreExecute() {
		if (loginTask!=null) {
			loginTask.onPreExecute();
			pd = loginTask.getProgressDialog();
		}
		else pd = new ProgressDialog(context);
		pd.setTitle(context.getString(R.string.authenticate));
		pd.setMessage(context.getString(R.string.please_wait));
		pd.setCancelable(true);
		pd.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
		pd.show();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			String user = params[0];
			String pass = params[1];
			List<NameValuePair> nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("user", user));
			nvp.add(new BasicNameValuePair("pass", pass));
			String url = params[2];
			JSONObject json = new JSONParser().makeHttpRequest(url, "GET", nvp);
			int success = json.getInt("success");
			if (success==1) {
				id = json.getLong("id");
				this.success = true;
			}
			else new Exception("Login Failed");
		} catch (JSONException e) {
			Log.e(Authenticate.class.getSimpleName(), e.getMessage());
			cancel(true);
		} catch (Exception e) {
			cancel(true);
		} 
		return null;
	}
	
	@Override
	protected void onCancelled() {
		if (loginTask!=null) loginTask.onCancelled();
		success = false;
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (loginTask!=null) loginTask.onAunthenticatePostExecute(success?1:0, id);
		super.onPostExecute(result);
	}

}
