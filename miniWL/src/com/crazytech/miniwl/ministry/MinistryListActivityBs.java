package com.crazytech.miniwl.ministry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;

public class MinistryListActivityBs extends MinistryListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setType(3);
	}
	
	@Override
	public void initList(Calendar cal) {
		String date = new SimpleDateFormat("yyyy-MM").format(cal.getTime());
		MinistryListAdapter adapter = new MinistryListAdapter(MinistryFragment.getTypeBasedMinistries(this, date, 3, 500), this);
		getList().setAdapter(adapter);
	}

}
