package com.crazytech.miniwl.ministry.householder;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class HouseholderAdapterSimple extends HouseholderAdapter {

	public HouseholderAdapterSimple(Context context, List<Householder> pplList) {
		super(context, pplList);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		getTvAddress().setVisibility(View.GONE);
		return view;
	}

}
