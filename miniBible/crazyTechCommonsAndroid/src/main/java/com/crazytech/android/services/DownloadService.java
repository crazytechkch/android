package com.crazytech.android.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.crazytech.commons.R;
import com.crazytech.util.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;

public class DownloadService extends Service {
	SharedPreferences preferences;
	
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private NotificationManager mNM;
	public static boolean serviceState=false;
	private Long startTime;
	private Long lastByte;
	private Boolean downloadFailed = false;
	private Notification notif;
	private PendingIntent contentIntent;
	
	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			showNotification(bundle.getInt("id"),bundle.getString("filename"),"Downloading",Notification.FLAG_NO_CLEAR);
			downloadFile(bundle.getString("filename"), bundle.getString("downloadUrl"),bundle.getString("filePath"), bundle.getInt("id"));
			
			
//			stopSelf(msg.arg1);
		}
	}
	
	public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
		 ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (serviceClass.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
	}
	
	@Override
	public void onCreate() {
		serviceState=true;
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
//		showNotification("","Downloading",true);
		HandlerThread thread = new HandlerThread("ServiceStartArguments",1);
		thread.start();
		
		// Get the HandlerThread's Looper and use it for our Handler 
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStartOrBind(intent);
		// If we get killed, after returning from here, restart
		return START_STICKY;
	}
	
	private void onStartOrBind(Intent intent) {
		startTime = System.currentTimeMillis();
		lastByte = 0L;
		Log.d("SERVICE-ONCOMMAND","onStartCommand");  
		Message msg = mServiceHandler.obtainMessage();
		msg.setData(intent.getExtras());
		mServiceHandler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		
		Log.d("SERVICE-DESTROY","DESTORY");
		serviceState=false;
//		if (downloadFailed) {
//			showNotification(intent.getStringExtra("filename"), getString(R.string.download_failed),false);
//		} else {
//			showNotification(intent.getStringExtra("filename"), getString(R.string.downloaded),false);
//		}
		//Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		notif = new Notification(R.drawable.download, getResources().getString(R.string.download_started),
				System.currentTimeMillis());
		contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0,
				new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
		// Set the icon, scrolling text and timestamp
		
		// Set the info for the views that show in the notification panel.
		notif.setLatestEventInfo(this, intent.getStringExtra("filename"), "Download Started", contentIntent);
		startForeground(intent.getIntExtra("id", 0), notif);
		onStartOrBind(intent);
		return null;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		int id = bundle.getInt("id");
		String filename =bundle.getString("filename");
		if (downloadFailed) {
			showNotification(id,filename, getString(R.string.download_failed),Notification.FLAG_AUTO_CANCEL);
		} else {
			showNotification(id,filename, getString(R.string.downloaded),Notification.FLAG_AUTO_CANCEL);
		}
		stopForeground(false);
		return super.onUnbind(intent);
	}
	
	
	
	
	
	
	private void showNotification(int id, String message,String title, int flag) {
		
		notif = new Notification(R.drawable.download, getResources().getString(R.string.download_started),
				System.currentTimeMillis());
		notif.flags = flag;
		
		//The PendingIntent to launch our activity if the user selects this notification
		contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0,
				new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		// Set the icon, scrolling text and timestamp
		
		// Set the info for the views that show in the notification panel.
		notif.setLatestEventInfo(this, title, message, contentIntent);
		// Send the notification.
		// We use a layout id because it is a unique number.  We use it later to cancel.
		mNM.notify(id, notif);
	}
	
	public void downloadFile(String filename, String fileUrl, String filePath, int id) {
		
		StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
		//double GB_Available = (avail_sd_space / 1073741824);
		double MB_Available = (avail_sd_space / 1048576);
		//System.out.println("Available MB : " + MB_Available);
		Log.d("MB",""+MB_Available);
		File root =new File(filePath);
		try {
			if(!root.exists() && root.isDirectory()) {
				root.mkdir();
			}
			Log.d("CURRENT PATH",root.getPath());
			URL url = new URL(fileUrl);
			URLConnection conn = url.openConnection();
			conn.connect();
			int fileSize  = conn.getContentLength();
			Log.d("FILESIZE",""+fileSize);
			if(MB_Available <= (fileSize/1048576) ){
				this.showNotification(id, getResources().getString(R.string.no_memory),getResources().getString(R.string.error),Notification.FLAG_AUTO_CANCEL);
				downloadFailed = true;
				return;
			} 
			
			FileOutputStream fout = new FileOutputStream(root);
			
			InputStream in = conn.getInputStream();
			
			
			byte[] buffer = new byte[1024];
			int total = 0;
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				total += count;
				fout.write(buffer, 0, count);
				publishProgress(id, total, fileSize, filename);
			}
			showNotification(id, getString(R.string.downloaded), filename, Notification.FLAG_AUTO_CANCEL);
			fout.close();
			stopSelf();
			
		} catch (IOException e) {
			Log.d("Downloader", e.getMessage());
			downloadFailed = true;
			root.delete();
		}
	}
	
	private void publishProgress(int id, Integer downSize, Integer totalSize, String title) {
		Long currTime = System.currentTimeMillis();
		Long timeDiff = currTime - startTime;
		if (timeDiff >= 2000) {
			downSize = downSize/1024; totalSize = totalSize/1024;
			timeDiff = timeDiff/1000;
			Long perc = Long.parseLong(downSize*100/totalSize+"");
			String downSpeed = ((downSize - lastByte)/timeDiff)+"";
			showNotification(id, String.valueOf(perc)+"%  "+downSize+"/"+totalSize+"KB  "+downSpeed+"KB/s", title, Notification.FLAG_NO_CLEAR);
			lastByte = Long.parseLong(downSize+"");
			startTime = System.currentTimeMillis();
		}
	}
}
