package com.crazytech.android.dialog;

import java.util.Calendar;

import com.crazytech.commons.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

public class DateTimeDialog extends Dialog {
	private Context context;
	private Button btnOk, btnCancel,btnDate,btnTime;
	private CalendarView calv;
	private TimePicker timp;
	private Calendar calendar;
	
	public DateTimeDialog(Context context, Calendar calendar) {
		super(context);
		this.context = context;
		this.calendar = calendar;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(context.getString(R.string.date_time));
		setContentView(R.layout.layout_date_time);
		calv = (CalendarView)findViewById(R.id.cal);
		timp = (TimePicker)findViewById(R.id.time);
		setDateTime();
		btnDate = (Button)findViewById(R.id.btn_date);
		btnDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnDate.setBackground(context.getResources().getDrawable(R.drawable.color_sel_tr));
				btnTime.setBackground(context.getResources().getDrawable(R.drawable.color_sel_gray));
				calv.setVisibility(View.VISIBLE);
				timp.setVisibility(View.GONE);
			}
		});
		btnTime = (Button)findViewById(R.id.btn_time);
		btnTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnDate.setBackground(context.getResources().getDrawable(R.drawable.color_sel_gray));
				btnTime.setBackground(context.getResources().getDrawable(R.drawable.color_sel_tr));
				calv.setVisibility(View.GONE);
				timp.setVisibility(View.VISIBLE);
			}
		});
		btnOk = (Button)findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public Button getPositiveButton() {
		return btnOk;
	}
	
	public Calendar getCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(calv.getDate());
		cal.set(Calendar.HOUR_OF_DAY, timp.getCurrentHour());
		cal.set(Calendar.MINUTE, timp.getCurrentMinute());
		return cal;
	}
	
	private void setDateTime() {
		calv.setDate(calendar.getTimeInMillis());
		timp.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timp.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}
}
