package com.crazytech.miniwl.pubs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazytech.miniwl.R;

public class PubsImgWithDescAdapter extends PubsImgAdapter {
	public PubsImgWithDescAdapter(Activity activity, Context context, List<PubsMagObj> magLst) {
		super(activity, context, magLst);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View pubView = super.getView(position, convertView, parent);
		LinearLayout linlay = (LinearLayout)pubView.findViewById(R.id.pubs_lvitem_titleovr);
		linlay.setVisibility(View.VISIBLE);
		LayoutParams params = linlay.getLayoutParams();
		params.width = getDmWidth();
		linlay.setLayoutParams(params);
		PubsMagObj mag = magLst.get(position);
		TextView pubText = (TextView)pubView.findViewById(R.id.pubs_lvitem_title);
        String issueDate = mag.getIssueDate();
		pubText.setText(issueDate);
        
		return pubView;
	}
}
