package com.crazytech.miniwl.pubs.booklets;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.books.PubsBooksFragment;

public class PubsBookletsFragment extends PubsBooksFragment{
	public PubsBookletsFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return "pubs_booklets";
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "booklets";
	}
	
	@Override
	public String getLongPublicationType() {
		// TODO Auto-generated method stub
		return getString(R.string.pubs_booklets);
	}
}
