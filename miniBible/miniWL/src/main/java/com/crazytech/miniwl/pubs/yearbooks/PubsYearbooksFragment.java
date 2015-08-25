package com.crazytech.miniwl.pubs.yearbooks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.GridView;

import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.PubsImgWithDescAdapter;
import com.crazytech.miniwl.pubs.PubsMagObj;
import com.crazytech.miniwl.pubs.RefreshPubs;
import com.crazytech.miniwl.pubs.books.PubsBooksFragment;

public class PubsYearbooksFragment extends PubsBooksFragment{

	public PubsYearbooksFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return "pubs_yearbooks";
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "yearbooks";
	}
	
	@Override
	public String getLongPublicationType() {
		// TODO Auto-generated method stub
		return getString(R.string.pubs_yearbooks);
	}
	
	@Override
	protected void initPubsBooks() {
		GridView gv = (GridView)getRootV().findViewById(R.id.gv_pubs_books);
		PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(getActivity(), getActivity(), localPubList(getActivity()));
		gv.setAdapter(adapter);
		initGvListeners(gv);
	}
	
	@Override
	protected void invokeRefresh(String refreshMethod) {
		GridView gv = (GridView)getRootV().findViewById(R.id.gv_pubs_books);
		if (network.isNetworkAvailable()) new RefreshPubs(getActivity(), getActivity(),taskCallback).execute(getTable(),refreshMethod);
		else {
			network.showNetworkDialog();
			PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(getActivity(), getActivity(), localPubList(getActivity()));
			gv.setAdapter(adapter);
		}
		initGvListeners(gv);
	}
}
