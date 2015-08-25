package com.crazytech.miniwl.ministry.householder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazytech.android.image.Image;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;

public class HouseholderAdapter extends BaseAdapter {
	private Context context;
	private List<Householder> pplList;
	private TextView tvAddress;
	
	public HouseholderAdapter(Context context, List<Householder> pplList) {
		super();
		this.context = context;
		this.pplList = pplList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pplList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pplList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return pplList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		Householder hse = pplList.get(position);
		if (convertView==null) {
			view = new LinearLayout(context);
			 LayoutInflater mInflater = (LayoutInflater)
	                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         convertView = mInflater.inflate(R.layout.listitem_people, (ViewGroup)view,true);
	         holder = new ViewHolder();
	         holder.image = (ImageView)convertView.findViewById(R.id.image_profpic);
	         holder.textName = (TextView)convertView.findViewById(R.id.text_name);
	         holder.textAddress = (TextView)convertView.findViewById(R.id.text_address);
	         setTvAddress(holder.textAddress);
	         holder.textNextVisit = (TextView)convertView.findViewById(R.id.text_next_visit);
	         view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder)view.getTag();
		}
		File imageFile = new File(MainActivity.SD_STORAGE_PATH+"/householders/profile/"+hse.getId()+".jpg");
		if(imageFile.exists()){
			Image image = new Image();
			image.decodeImageViewBitmap(holder.image, imageFile.getAbsolutePath(), 200, 200);
		}
		else if(hse.getGender().equals("f"))holder.image.setImageResource(R.drawable.ic_woman);
		else holder.image.setImageResource(R.drawable.ic_man);
		holder.textName.setText(hse.getName());
		holder.textAddress.setText(hse.getStreet());
		String nextVisit = context.getString(R.string.next_visit_neg);
		if (hse.getNextVisit()!=-1){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(hse.getNextVisit());
			nextVisit = new SimpleDateFormat("EEE dd-MM-yyyy").format(cal.getTime());
		}
		holder.textNextVisit.setText(nextVisit);
		return convertView;
	}
	
	private static class ViewHolder{
		ImageView image;
		TextView textName, textAddress, textNextVisit;
	}

	public TextView getTvAddress() {
		return tvAddress;
	}

	public void setTvAddress(TextView tvAddress) {
		this.tvAddress = tvAddress;
	}

}
