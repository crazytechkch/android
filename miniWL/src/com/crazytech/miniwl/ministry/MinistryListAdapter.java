package com.crazytech.miniwl.ministry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MinistryListAdapter extends BaseAdapter {
	private List<Ministry> ministries;
	private Context context;
	private TextView textDate,textType,textName,textAddress,textDuration;
	private TextView textBook,textMag,textBroc,textTract;
	private ImageButton btnEdit, btnDelete;
	
	
	public MinistryListAdapter(List<Ministry> ministries, Context context) {
		super();
		this.ministries = ministries;
		this.context = context;
	}

	@Override
	public int getCount() {
		return ministries.size();
	}

	@Override
	public Object getItem(int position) {
		return ministries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ministries.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listitem_ministry, null);
        }
		Ministry ministry = (Ministry)getItem(position);
		textDate = (TextView)convertView.findViewById(R.id.text_date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			textDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(df.parse(ministry.getDate())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		}
		textType = (TextView)convertView.findViewById(R.id.text_type);
		switch (ministry.getType()) {
		case 1:
			textType.setText(context.getString(R.string.field));
			break;
		case 2:
			textType.setText(context.getString(R.string.rv));
			break;
		case 3:
			textType.setText(context.getString(R.string.bible_study));
			break;
		case 4:
			textType.setText(context.getString(R.string.informal_witnessing));
			break;
		default:
			break;
		}
		textName = (TextView)convertView.findViewById(R.id.text_name);
		textName.setText(ministry.getName());
		textAddress = (TextView)convertView.findViewById(R.id.text_address);
		textAddress.setText(ministry.getStreet());
		textDuration = (TextView)convertView.findViewById(R.id.text_duration);
		Long duration = ministry.getDuration();
		Long hour = duration/3600000;
		Long minute = (duration%3600000)/60000;
		textDuration.setText(String.format(context.getString(R.string.hours_mins_short), hour, minute));
		textBook = (TextView)convertView.findViewById(R.id.text_books);
		textBook.setText(ministry.getBooks()+"");
		textMag = (TextView)convertView.findViewById(R.id.text_mags);
		textMag.setText(ministry.getMags()+"");
		textBroc = (TextView)convertView.findViewById(R.id.text_brochures);
		textBroc.setText(ministry.getBrocs()+"");
		textTract = (TextView)convertView.findViewById(R.id.text_tract);
		textTract.setText(ministry.getTracts()+"");
		return convertView;
	}

}
