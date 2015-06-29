package com.crazytech.miniwl.pubs.brochures;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.books.PubsBooksFragment;

public class PubsBrochuresFragment extends PubsBooksFragment{
	public PubsBrochuresFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return "pubs_brochures";
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "brochures";
	}
	
	@Override
	public String getLongPublicationType() {
		// TODO Auto-generated method stub
		return getString(R.string.pubs_brochures);
	}
}
