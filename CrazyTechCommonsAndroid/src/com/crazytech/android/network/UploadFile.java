package com.crazytech.android.network;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;

import com.crazytech.commons.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UploadFile extends AsyncTask<String, String, String> {
	private ProgressDialog pd;
	private Context context;
	private Integer iconRes, serverResponseCode = 0;
	private String title,message;
	private Boolean success;
	
	/*
	 * @param title, message, success, failed
	 */
	public UploadFile(Context context, Integer iconRes, String... params) {
		super();
		this.context = context;
		this.iconRes = iconRes;
		this.title = params[0];
		this.message = params[1];
	}

	@Override
	protected void onPreExecute() {
		pd =new ProgressDialog(context);
		pd.setIcon(context.getResources().getDrawable(iconRes));
		pd.setTitle(title);
		pd.setMessage(message);
		pd.setCancelable(false);
		pd.show();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		uploadFile(params[0], params[1], params[2]);
		return null;
	}
	
	public void uploadFile(String sourceFileUri, String uploadServerUri, String id) {
		HttpURLConnection conn = null;
		DataOutputStream outStream = null;
		Integer bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		File sourceFile = new File(sourceFileUri);
		try {
			if (!sourceFile.isFile()) throw new FileNotFoundException();
			FileInputStream inStream = new FileInputStream(sourceFile);
			URL uploadUrl = new URL(uploadServerUri);
			
			conn = (HttpURLConnection)uploadUrl.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
	        conn.setRequestProperty("uploaded_file", sourceFileUri); 
	        conn.setRequestProperty("id",id);
	        outStream = new DataOutputStream(conn.getOutputStream());
	        outStream.writeBytes("--*****\r\n");
	        outStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + sourceFileUri + "\"" + "\r\n");
	        outStream.writeBytes("\r\n");
	        
	        bytesAvailable = inStream.available();
	        
	        bufferSize = Math.min(bytesAvailable, 1024*1024);
	        buffer = new byte[bufferSize];
	        bytesRead = inStream.read(buffer,0,bufferSize);
	        
	        while (bytesRead > 0) {
				outStream.write(buffer, 0, bufferSize);
				bytesAvailable = inStream.available();
				bufferSize = Math.min(bytesAvailable, 1024*1024);
				bytesRead = inStream.read(buffer, 0, bufferSize);
			}
	        
	        outStream.writeBytes("\r\n");
	        outStream.writeBytes("--*****--\r\n");
	        
	        serverResponseCode = conn.getResponseCode();
	        String serverRespMsg = conn.getResponseMessage(); 
	        
	        Log.i("UploadFile", serverResponseCode+":"+serverRespMsg);
	        
	        inStream.close();
	        outStream.flush();
	        outStream.close();
	        if (serverResponseCode!=200) 
	        	throw new Exception("File Upload Failed \n"+serverResponseCode+"\n"+serverRespMsg);
	        success=true;
		} catch (MalformedURLException e) {
			logExceptionAndCancel(e);
		} catch (IOException e) {
			logExceptionAndCancel(e);
		} catch (Exception e) {
			logExceptionAndCancel(e);
		}
		
	}
	
	protected void logExceptionAndCancel(Exception e){
		Log.e("UploadFile", e.getMessage());
		cancel(true);
	}
	@Override
	protected void onPostExecute(String result) {
		try {
			pd.dismiss();
		} catch (Exception e) {
			
		} finally {
			if (success) Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected void onCancelled() {
		try {
			pd.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_LONG).show();
		}
		super.onCancelled();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public ProgressDialog getPd() {
		return pd;
	}

	public void setPd(ProgressDialog pd) {
		this.pd = pd;
	}

}
