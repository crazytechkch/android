package com.crazytech.miniwl.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.booklets.PubsBookletsFragment;
import com.crazytech.miniwl.pubs.books.PubsBooksFragment;
import com.crazytech.miniwl.pubs.brochures.PubsBrochuresFragment;
import com.crazytech.miniwl.pubs.g.PubsAwakeFragment;
import com.crazytech.miniwl.pubs.okm.PubsOkmFragment;
import com.crazytech.miniwl.pubs.wt.PubsWtFragment;
import com.crazytech.miniwl.pubs.yearbooks.PubsYearbooksFragment;

public class NavDrawerGroupItem extends NavDrawerItem{
	private List<NavDrawerChildItem> childs;
	private String title;
	private Context context;
	
	public NavDrawerGroupItem() {
		super();
		initChilds();
	}

	public NavDrawerGroupItem(Context context, String title, int icon, Fragment fragment, boolean isCounterVisible,
			String count) {
		super(title, icon, fragment, isCounterVisible, count);
		this.title=title;
		
	}

	public NavDrawerGroupItem(Context context, String title, int icon, Fragment fragment) {
		super(title, icon, fragment);
		this.title=title;
		this.context = context;
		initChilds();
	}
	
	public NavDrawerGroupItem(Context context, String title, int icon) {
		super(title, icon);
		this.title = title;
		this.context = context;
		initChilds();
	}

	private void initChilds() {
		childs = new ArrayList<NavDrawerChildItem>();
		String pubs = context.getResources().getString(R.string.publications);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View defaultView = inflater.inflate(R.layout.drawer_list_item, null);
		
		if (title.equals(pubs)) {
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_watchtower), R.drawable.ic_pubs, new PubsWtFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_awake), R.drawable.ic_pubs, new PubsAwakeFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_okm), R.drawable.ic_pubs, new PubsOkmFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_books), R.drawable.ic_pubs, new PubsBooksFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_brochures), R.drawable.ic_pubs, new PubsBrochuresFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_booklets), R.drawable.ic_pubs, new PubsBookletsFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.pubs_yearbooks), R.drawable.ic_pubs, new PubsYearbooksFragment(),context));
		}
		/*
		String ministry = context.getResources().getString(R.string.my_ministry);
		if (title.equals(ministry)) {
			childs.add(new NavDrawerChildItem(context.getString(R.string.timer), R.drawable.clock, context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.add_householder), R.drawable.ic_people_white, new AddPeopleFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.householder), R.drawable.ic_people_white, new PeopleListFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.add_field_record), R.drawable.ic_list_add, new FieldRecordFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.field_records), R.drawable.ic_list, new FieldRecordListFragment(),context));
			childs.add(new NavDrawerChildItem(context.getString(R.string.map), R.drawable.ic_map, new MiniWLMapFragment(),context));
		}
		*/
	}

	public List<NavDrawerChildItem> getChilds() {
		return childs;
	}

	public void setChilds(List<NavDrawerChildItem> childs) {
		this.childs = childs;
	}
	
}
