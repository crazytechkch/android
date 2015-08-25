package com.crazytech.android.network;

import com.crazytech.commons.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Network {
	private Context context;
	
	public Network(Context context) {
		super();
		this.context = context;
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	public void showNetworkDialog(){
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.layout_dialog_network);
		dialog.setTitle(R.string.net_not_avail);
		Button btn3g = (Button)dialog.findViewById(R.id.dialog_network_button_3g);
		btn3g.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		Button btnWifi = (Button)dialog.findViewById(R.id.dialog_network_button_wifi);
		btnWifi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
		Button btnCancel = (Button)dialog.findViewById(R.id.dialog_network_button_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();	
			}
		});
		dialog.show();
	}
}
