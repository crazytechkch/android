package com.crazytech.android.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.crazytech.commons.R;

import android.app.Notification;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class DownloadFile extends UploadFile {

	public DownloadFile(Context context, Integer iconRes, String... params) {
		super(context,iconRes, params);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		downloadFile(params[0], params[1]);
		return null;
	}
	
	private void downloadFile(String filePath, String fileUrl) {
		StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
		//double GB_Available = (avail_sd_space / 1073741824);
		double MB_Available = (avail_sd_space / 1048576);
		//System.out.println("Available MB : " + MB_Available);
		Log.d("MB",""+MB_Available);
		try {
			File root =new File(filePath);
			if(!root.exists() && root.isDirectory()) {
				root.mkdirs();
			}
			Log.d("CURRENT PATH",root.getPath());
			URL url = new URL(fileUrl);
			URLConnection conn = url.openConnection();
			conn.connect();
			int fileSize  = conn.getContentLength();
			Log.d("FILESIZE",""+fileSize);
			if(MB_Available <= (fileSize/1048576) )throw new Exception("Not Enough Space");
			
			FileOutputStream fout = new FileOutputStream(new File(filePath));
			
			InputStream in = conn.getInputStream();
			
			
			byte[] buffer = new byte[1024];
			//int total = 0;
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				//total += count;
				fout.write(buffer, 0, count);
			}
			fout.close();
			setSuccess(true);
		} catch (MalformedURLException e) {
			logExceptionAndCancel(e);
		} catch (IOException e) {
			logExceptionAndCancel(e);
		} catch (Exception e) {
			logExceptionAndCancel(e);
		}
	}

}
