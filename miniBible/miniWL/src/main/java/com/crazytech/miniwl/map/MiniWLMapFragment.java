package com.crazytech.miniwl.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.crazytech.miniwl.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MiniWLMapFragment extends Fragment {
	private View rootV,goneView;
	private GoogleMap map;
	private ImageButton btnMaximize, btnMapType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.layout_map, container,false);
		initMap();
		declareViews();
		return rootV;
	}
	
	private void initMap() {
		map = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
		if(map!=null)map.setMyLocationEnabled(true);
		
	}
	
	private void declareViews() {
		btnMapType = (ImageButton)rootV.findViewById(R.id.btn_maptype);
		btnMapType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(map.getMapType()==GoogleMap.MAP_TYPE_NORMAL)map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				else map.setMapType(GoogleMap.MAP_TYPE_NORMAL);;
			}
		});
		btnMaximize = (ImageButton)rootV.findViewById(R.id.btn_mapview);
		btnMaximize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getGoneView()!=null) {
					switch (getGoneView().getVisibility()) {
					case View.VISIBLE:
						getGoneView().setVisibility(View.GONE);
						btnMaximize.setImageResource(R.drawable.minimise);
						break;
						
					default:
						getGoneView().setVisibility(View.VISIBLE);
						btnMaximize.setImageResource(R.drawable.maximise);
						break;
					}
					
				}
			}
		});
	}

	public View getGoneView() {
		return goneView;
	}

	public void setGoneView(View goneView) {
		this.goneView = goneView;
	}

	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}
}
