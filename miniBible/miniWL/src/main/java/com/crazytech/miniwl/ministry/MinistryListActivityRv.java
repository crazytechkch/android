package com.crazytech.miniwl.ministry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;

public class MinistryListActivityRv extends MinistryListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setType(2);
	}
	
	@Override
	public void initList(Calendar cal) {
		String date = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
		MinistryListAdapter adapter = new MinistryListAdapter(MinistryFragment.getTypeBasedMinistries(this, date, 2, 500), this);
		getList().setAdapter(adapter);
	}

}
