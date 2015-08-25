package com.crazytech.miniwl.pubs.wt;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazytech.miniwl.R;
import com.crazytech.miniwl.pubs.PDFUtil;
import com.crazytech.miniwl.pubs.PubsImgWithDescAdapter;

public class PubsWtAdapter extends ArrayAdapter<PubsWtObj> {
	int resource;
	String response;
	Context context;
	Activity activity;
	List<PubsWtObj> objects;
	private PDFUtil pdf;
	
	public PubsWtAdapter(Activity activity, Context context, int resource,
			List<PubsWtObj> objects) {
		super(context, resource, objects);
		this.context = context;
		this.activity = activity;
		this.resource = resource;
		this.objects = objects;
		pdf = new PDFUtil(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout pubView;
		final PubsWtObj wt = getItem(position);
		
		if (convertView==null) {
			pubView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(resource, pubView, true);
		} else pubView = (LinearLayout)convertView;
		
		TextView pubText = (TextView)pubView.findViewById(R.id.tv_list_item_pub_wt);
		pubText.setText(wt.getIssueDate());
		
		
		GridView gv = (GridView)pubView.findViewById(R.id.gv_pubs_wt_thumbs);
		PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(activity,context, wt.getMagLst());
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				pdf.openPDF(wt.getMagLst().get(position));
			}
		});
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle(wt.getMagLst().get(position).getDesc());
				dialog.setIcon(context.getResources().getDrawable(R.drawable.ic_pubs));
				final CharSequence[] items = context.getResources().getStringArray(R.array.pdf_dialog);
				dialog.setItems(items, new DialogInterface.OnClickListener() {
				
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (items[which].equals(context.getResources().getString(R.string.open_pdf))) {
							pdf.openPDF(wt.getMagLst().get(position));
						}
						if (items[which].equals(context.getResources().getString(R.string.delete))) {
							pdf.deletePDF(wt.getMagLst().get(position));
						}
					}
				});
				dialog.create().show();
				return true;
			}
		});
		
		
		return pubView;
		
	}
	
	
	

}
