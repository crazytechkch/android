package com.crazytech.miniwl.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.model.NavDrawerChildItem;
import com.crazytech.miniwl.model.NavDrawerGroupItem;

public class NavDrawerListAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private ArrayList<NavDrawerGroupItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerGroupItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return navDrawerItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(groupPosition).getChilds().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_group_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
         
        imgIcon.setImageResource(navDrawerItems.get(groupPosition).getIcon());  
        txtTitle.setText(navDrawerItems.get(groupPosition).getTitle());
        
        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(groupPosition).getCounterVisibility()){
        	txtCount.setText(navDrawerItems.get(groupPosition).getCount());
        }else{
        	// hide the counter view
        	txtCount.setVisibility(View.GONE);
        }
        
        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/*if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
         
        imgIcon.setImageResource(child.get(childPosition).getIcon());  
        txtTitle.setText(child.get(childPosition).getTitle());
        
        // displaying count
        // check whether it set visible or not
        if(child.get(childPosition).getCounterVisibility()){
        	txtCount.setText(child.get(childPosition).getCount());
        }else{
        	// hide the counter view
        	txtCount.setVisibility(View.GONE);
        }
        */
		List<NavDrawerChildItem> child = navDrawerItems.get(groupPosition).getChilds();
        convertView = child.get(childPosition).getView(childPosition,groupPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
