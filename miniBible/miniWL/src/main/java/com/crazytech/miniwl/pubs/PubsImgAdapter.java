package com.crazytech.miniwl.pubs;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.crazytech.android.image.Image;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;

public class PubsImgAdapter extends BaseAdapter {
	
	protected Context context;
	protected Activity activity;
	protected List<PubsMagObj> magLst;
	
	
	

	public PubsImgAdapter(Activity activity, Context context, List<PubsMagObj> magLst) {
		super();
		this.context = context;
		this.activity = activity;
		this.magLst = magLst;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return magLst.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View pubView;
		ViewHolder holder;
		PubsMagObj mag = magLst.get(position);
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	pubView = new RelativeLayout(context);
        	String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater)context.getSystemService(inflater);
			li.inflate(R.layout.listitem_pub_image, (ViewGroup)pubView, true);
			holder = new ViewHolder();
			holder.image = (ImageView)pubView.findViewById(R.id.pubs_lvitem_imv);
			holder.downloadOverlay = (LinearLayout)pubView.findViewById(R.id.pubs_lvitem_downovr);
			pubView.setTag(holder);
        } else {
        	pubView = convertView;
        	holder = (ViewHolder)pubView.getTag();
        }
        String fileDir = MainActivity.SD_STORAGE_PATH+"pubs/"+mag.getType()+"/"+mag.getFilename();
		//ImageView pubImage = (ImageView)pubView.findViewById(R.id.pubs_lvitem_imv);
		Image image = new Image();
		if(new File(fileDir).exists())image.decodeImageViewBitmap(holder.image, fileDir, getDmWidth(), getDmWidth());
		holder.image.setLayoutParams(new RelativeLayout.LayoutParams(getDmWidth(), getDmWidth()));
        holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.image.setPadding(0, 0, 0, 0);
        if(!(new File(fileDir+".pdf").exists())){
        	holder.downloadOverlay.setVisibility(View.VISIBLE);
        	holder.downloadOverlay.setLayoutParams(new RelativeLayout.LayoutParams(getDmWidth(),getDmWidth()));
         }
        return pubView;
	}
	/*
	private String getImgDir(String path,String filename){
		String[] filenames = new File(path).list();
		for (int i = 0; i < filenames.length; i++) {
			String currName = filenames[i];
			if (currName.contains(filename)&&!currName.endsWith(".pdf")) {
				filename = currName;
				break;
			}
		}
		return path+filename;
	}
	*/
	
	public Integer getDmWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels/3;
	}
	
	private static class ViewHolder{
		ImageView image;
		View downloadOverlay;
	}
}
