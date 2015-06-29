package com.crazytech.miniwl.pubs.okm;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.g.PubsAwakeFragment;

public class PubsOkmFragment extends PubsAwakeFragment {
	private String table = "pubs_okm";
	
	public PubsOkmFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return table;
	}
	
	@Override
	public String getPubType() {
		// TODO Auto-generated method stub
		return "okm";
	}
	
	@Override
	public String getLongPublicationType() {
		// TODO Auto-generated method stub
		return getString(R.string.pubs_okm);
	}
	
}
