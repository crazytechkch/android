package com.crazytech.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Handler;
import android.widget.TextView;

public class Stopwatch {
private long startTime = 0;
private boolean running = false;
private boolean paused;
private boolean firstRun = true;
private long currentTime = 0;
private Handler handler;
public static final int START_TIMER = 0;
public static final int STOP_TIMER = 1;
public static final int UPDATE_TIMER = 2;
public static final int PAUSE_TIMER = 3;
public static final int RESUME_TIMER = 4;


public Stopwatch(final TextView tv, final int interval){
	handler =  new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_TIMER:
				start(); //start timer
				handler.sendEmptyMessage(UPDATE_TIMER);
				break;
			case STOP_TIMER:
				handler.removeMessages(UPDATE_TIMER); // no more updates.
	            stop();//stop timer
	            tv.setText(getElapsedTime());
				break;
			case UPDATE_TIMER:
				tv.setText(getElapsedTime());
				handler.sendEmptyMessageDelayed(UPDATE_TIMER,interval); //text view is updated every second, 
				break;
			case PAUSE_TIMER:
				pause();
				handler.removeMessages(UPDATE_TIMER); 
				tv.setText(getCurrentTime());
				break;
			case RESUME_TIMER:
				resume();
				handler.sendEmptyMessage(UPDATE_TIMER); 
				tv.setText(getElapsedTime());
				break;
			default:
				break;
			}
		};
	};
}


public void start() {
    this.startTime = System.currentTimeMillis();
    this.running = true;
    this.firstRun = false;
}


public void stop() {
    this.running = false;
    this.firstRun = true;
}

public void pause() {
    this.running = false;
    this.paused = true; 
    currentTime = System.currentTimeMillis() - startTime;
}
public void resume() {
    this.running = true;
    this.paused = false;
    this.startTime = System.currentTimeMillis() - currentTime;
}


//elaspsed time in milliseconds
public long getElapsedTimeMili() {
    long elapsed = 0;
    if (running) {
         elapsed =((System.currentTimeMillis() - startTime)/100) % 1000 ;
    }
    return elapsed;
}

public String getElapsedTime() {
	return String.format("%02d:%02d:%02d", getElapsedTimeHour(),getElapsedTimeMin(),getElapsedTimeSecs());
}

public String getCurrentTime() {
	Long currTimeSec = (currentTime/1000)%60;
	Long currTimeMin = (currentTime/1000/60)%60;
	Long currTimeHour = (currentTime/1000/60/60);
	return String.format("%02d:%02d:%02d", currTimeHour,currTimeMin,currTimeSec);
}


//elaspsed time in seconds
public long getElapsedTimeSecs() {
    long elapsed = 0;
    if (running) {
        elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
    }
    return elapsed;
}

  //elaspsed time in minutes
public long getElapsedTimeMin() {
    long elapsed = 0;
    if (running) {
        elapsed = (((System.currentTimeMillis() - startTime) / 1000) / 60 ) % 60;
    }
    return elapsed;
}

  //elaspsed time in hours
public long getElapsedTimeHour() {
    long elapsed = 0;
    if (running) {
        elapsed = ((((System.currentTimeMillis() - startTime) / 1000) / 60 ) / 60);
    }
    return elapsed;
}


public Handler getHandler() {
	return handler;
}


public boolean isPaused() {
	return paused;
}


public boolean isFirstRun() {
	return firstRun;
}

	
}