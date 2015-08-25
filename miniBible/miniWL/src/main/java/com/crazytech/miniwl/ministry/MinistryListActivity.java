package com.crazytech.miniwl.ministry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.crazytech.miniwl.R;

import co.crazytech.android.activity.DatedContainerActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MinistryListActivity extends DatedContainerActivity {
	private ListView list;
	private Bundle extras;
	private Button btnAdd, btnBack;
	private Context context;
	private int type = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		extras = getIntent().getExtras();
		LinearLayout container = (LinearLayout)findViewById(R.id.lay_content);
		View view = getLayoutInflater().inflate(R.layout.layout_ministry_list, container);
		list = (ListView)view.findViewById(R.id.list_ministries);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Ministry minis = (Ministry)parent.getAdapter().getItem(position);
				Intent editIntent = new Intent(context, MinistryEditActivity.class);
				editIntent.putExtras(minis.getExtras(true));
				startActivity(editIntent);
			}
		});
		btnAdd = (Button)view.findViewById(R.id.btn_ok);
		btnAdd.setText(getString(R.string.add));
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Ministry minis = new Ministry();
				minis.setType(getType());
				Intent editIntent = new Intent(context, MinistryEditActivity.class);
				editIntent.putExtras(minis.getExtras(false));
				startActivity(editIntent);
			}
		});
		btnBack = (Button)view.findViewById(R.id.btn_cancel);
		btnBack.setText(getString(R.string.btn_back));
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishAndInitMinistry();
			}
		});
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(extras.getLong("cal"));
		initList(cal);
		init(cal);
	}
	
	public void initList(Calendar cal){
		MinistryListAdapter adapter = new MinistryListAdapter(MinistryFragment.getMinistries(this, new SimpleDateFormat("yyyy-MM").format(cal.getTime()), 500), this);
		list.setAdapter(adapter);
	}
	
	@Override
	public void onNewDateConfirmed() {
		initList(getCal());
		super.onNewDateConfirmed();
	}
	
	@Override
	public void onDateLongClick() {
		super.onDateLongClick();
		initList(getCal());
	}
	
	@Override
	public void onPrevClick() {
		super.onPrevClick();
		initList(getCal());
	}
	
	@Override
	public void onNextClick() {
		super.onNextClick();
		initList(getCal());
	}
	
	private void finishAndInitMinistry() {
		Intent resIntent = new Intent();
		resIntent.putExtra(MinistryFragment.INIT_VIEW, true);
		setResult(Activity.RESULT_OK, resIntent);
		finish();
	}

	public ListView getList() {
		return list;
	}

	public void setList(ListView list) {
		this.list = list;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
