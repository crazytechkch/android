package com.crazytech.miniwl.pubs;

import java.io.FileNotFoundException;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.crazytech.android.network.Network;
import com.crazytech.android.pdf.PDF;
import com.crazytech.android.services.DownloadService;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;
import com.crazytech.util.Util;

public class PDFUtil {
	private Context context;
	private Network network;
	private PDF pdf;

	public PDFUtil(Context context) {
		super();
		this.context = context;
		network = new Network(context);
		pdf = new PDF(context);
	}

	public void openPDF(PubsMagObj mag){
		String filePath = MainActivity.SD_STORAGE_PATH+"pubs/"+mag.getType();
		try {
			pdf.openPDF(filePath+"/"+mag.getFilename());
		} catch (FileNotFoundException e) {
			if (network.isNetworkAvailable()&&!mag.getUrl().equals("")) {
				String url = mag.getUrl()!=null?mag.getUrl():"";
				String fileDir = MainActivity.SD_STORAGE_PATH+"pubs/"+mag.getType();;
				Intent downloadIntent = new Intent(context, DownloadService.class);
				downloadIntent.putExtra("downloadUrl", url);
				downloadIntent.putExtra("filePath", fileDir+"/"+mag.getFilename()+".pdf");
				downloadIntent.putExtra("id", Util.randomNumbers(7));
				if (mag.getDesc()!=null||!mag.getDesc().equals(""))downloadIntent.putExtra("filename", mag.getDesc());
				else downloadIntent.putExtra("filename", mag.getFilename()+".pdf");
				ServiceConnection servConn = new ServiceConnection() {
					
					@Override
					public void onServiceDisconnected(ComponentName name) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onServiceConnected(ComponentName name, IBinder service) {
						// TODO Auto-generated method stub
						
					}
				};
				
				context.startService(downloadIntent);
				//new DownloadFile(context,mag).execute(url,mag.getFilename()+".pdf",fileDir);
			} else Toast.makeText(context, mag.getDesc()+" "+context.getResources().getString(R.string.not_found), Toast.LENGTH_LONG).show();
		} catch (ActivityNotFoundException e) {
			Log.e("miniWL PDF", e.getMessage());
			Toast.makeText(context, context.getResources().getString(R.string.no_pdf_viewer), Toast.LENGTH_LONG).show();
		}
	}
	
	public void deletePDF(PubsMagObj mag){
		String filePath = MainActivity.SD_STORAGE_PATH+"pubs/"+mag.getType();
		try {
			pdf.deletePDF(filePath, mag.getFilename());
			Toast.makeText(context, mag.getDesc()+" "+context.getResources().getString(R.string.deleted), Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(context, mag.getDesc()+" "+context.getResources().getString(R.string.not_found), Toast.LENGTH_LONG).show();
		} 
	}
}
