package com.crazytech.miniwl.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazytech.miniwl.R;
import com.crazytech.util.Stopwatch;

public class NavDrawerChildItem extends NavDrawerItem{
	private Context context;
	private View view;
	private TextView txtTitle;
	public NavDrawerChildItem(String title, int icon, Fragment fragment, boolean isCounterVisible,
			String count) {
		super(title, icon,fragment, isCounterVisible, count);
		// TODO Auto-generated constructor stub
	}

	public NavDrawerChildItem(String title, int icon, Fragment fragment, Context context) {
		super(title, icon, fragment);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public NavDrawerChildItem(String title, int icon, Context context) {
		super(title, icon);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	
	public View getView(int index, int groupIndex) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.drawer_list_item, null);
		ImageView imgIcon = (ImageView) view.findViewById(R.id.icon);
		txtTitle = (TextView) view.findViewById(R.id.title);
		txtTitle.setText(getTitle());
		TextView txtCount = (TextView) view.findViewById(R.id.counter);
		imgIcon.setImageResource(getIcon());  
		//if(getCounterVisibility())txtCount.setText(getCount());
		//else txtCount.setVisibility(View.GONE);
		if(groupIndex==5&&index==0){
			txtTitle.setText("00:00:00");
			LinearLayout linlay = (LinearLayout)view.findViewById(R.id.timer_controls);
			linlay.setVisibility(View.VISIBLE);
			final ImageButton buttonStart = (ImageButton)view.findViewById(R.id.timer_start);
			final Stopwatch stopwatch = new Stopwatch(txtTitle,1000);
			buttonStart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ImageButton btn = (ImageButton)v;
					if(stopwatch.isFirstRun()){
						stopwatch.getHandler().sendEmptyMessage(Stopwatch.START_TIMER);
						btn.setImageDrawable(context.getResources().getDrawable(R.drawable.playback_pause));
						
					} else{
						if(stopwatch.isPaused()){
							stopwatch.getHandler().sendEmptyMessage(Stopwatch.RESUME_TIMER);
							btn.setImageDrawable(context.getResources().getDrawable(R.drawable.playback_pause));
						}
						else {
							stopwatch.getHandler().sendEmptyMessage(Stopwatch.PAUSE_TIMER);
							btn.setImageDrawable(context.getResources().getDrawable(R.drawable.playback_play));
						}
					}
				}
			});
			ImageButton buttonStop = (ImageButton)view.findViewById(R.id.timer_stop);
			buttonStop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					stopwatch.getHandler().sendEmptyMessage(Stopwatch.STOP_TIMER);
					buttonStart.setImageDrawable(context.getResources().getDrawable(R.drawable.playback_play));
				}
			});
		}
		return view;
	}
}
