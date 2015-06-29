package com.crazytech.android.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.crazytech.android.network.Network;
import com.crazytech.commons.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class PDF {
	private Context context;
	private Network network;
	
	

	public PDF(Context context) {
		super();
		this.context = context;
		network = new Network(context);
	}

	public Boolean openPDF(String fileDir) throws FileNotFoundException, ActivityNotFoundException{
		if (!fileDir.endsWith(".pdf")) fileDir+=".pdf";
		File file = new File(fileDir);
		if (file.exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
			context.startActivity(intent);
			return true;
		}  else {
			throw new FileNotFoundException(fileDir+" not found.");
		}
	}
	
	public Boolean deletePDF(String filePath, String fileName)throws FileNotFoundException{
		if (!fileName.endsWith(".pdf")) fileName+=".pdf";
		String path = filePath+"/"+fileName;
		File file = new File(path);
		if (file.exists()) {
			file.delete();
			if (!file.exists())return true;
		} else {
			throw new FileNotFoundException(fileName+" not found.");
		}
		return false;
	}
}
