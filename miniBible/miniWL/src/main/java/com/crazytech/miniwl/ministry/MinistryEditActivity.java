package com.crazytech.miniwl.ministry;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.crazytech.android.dialog.DateTimeDialog;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.map.MiniWLMapFragment;
import com.crazytech.miniwl.ministry.householder.Householder;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MinistryEditActivity extends FragmentActivity implements OnMapReadyCallback {
	private Context context;
	private TextView textStartDate, textEndDate;
	private Button btnOk, btnCancel;
	private EditText editBooks,editMags,editBrocs,editTracts, editStreet, editRemarks, editHours, editMinutes;
	private MiniWLMapFragment miniWlMap;
	private Spinner spinType,spinPerson;
	private Bundle extras;
	private Ministry ministry;
	private Calendar calStart,calEnd;
	private int persPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_ministry);
		context = this;
		extras = getIntent().getExtras();
		ministry = initMinistry();
		initView();
		initMap();
		
	}
	
	private void initMap() {
		miniWlMap = (MiniWLMapFragment)getSupportFragmentManager().findFragmentById(R.id.frag_minis_map);
		miniWlMap.setGoneView(findViewById(R.id.scroll));
	}
	
	private void initView(){
		calStart = ministry.getCalStart();
		calEnd = ministry.getCalEnd();
		final SimpleDateFormat df = new SimpleDateFormat("EEE dd-MM-yyyy hh:mm a");
		LinearLayout layStartDate = (LinearLayout)findViewById(R.id.lay_start_date);
		layStartDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DateTimeDialog dateDialog = new DateTimeDialog(context,calStart);
				dateDialog.show();
				dateDialog.getPositiveButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Long timeDiff = calStart.getTimeInMillis() - dateDialog.getCalendar().getTimeInMillis();
						if(timeDiff!=0L){
							calStart = dateDialog.getCalendar();
							textStartDate.setText(df.format(calStart.getTime()));
							Long duration = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
							editHours.setText((duration/3600000)+"");
							editMinutes.setText((duration%3600000)/60000+"");
							ministry.setCalStart(calStart);
							ministry.setDuration(duration);
						}
						dateDialog.dismiss();
					}
				});
			}
		});
		LinearLayout layEndDate = (LinearLayout)findViewById(R.id.lay_end_date);
		layEndDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DateTimeDialog dateDialog = new DateTimeDialog(context,calEnd);
				dateDialog.show();
				dateDialog.getPositiveButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Long timeDiff = calEnd.getTimeInMillis() - dateDialog.getCalendar().getTimeInMillis();
						if(timeDiff!=0L){
							calEnd = dateDialog.getCalendar();
							textEndDate.setText(df.format(calEnd.getTime()));
							Long duration = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
							editHours.setText((duration/3600000)+"");
							editMinutes.setText((duration%3600000)/60000+"");
							ministry.setCalEnd(calEnd);
							ministry.setDuration(duration);
						}
						dateDialog.dismiss();
					}
				});
			}
		});
		textStartDate = (TextView)findViewById(R.id.text_start_date);
		textStartDate.setText(df.format(calStart.getTime()));
		textEndDate = (TextView)findViewById(R.id.text_end_date);
		textEndDate.setText(df.format(calEnd.getTime()));
		editBooks = (EditText)findViewById(R.id.edit_books);
		editBooks.setText(ministry.getBooks()+"");
		editBooks.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setBooks(Integer.valueOf(s.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editMags = (EditText)findViewById(R.id.edit_mags);
		editMags.setText(ministry.getMags()+"");
		editMags.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setMags(Integer.valueOf(s.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editBrocs = (EditText)findViewById(R.id.edit_brocs);
		editBrocs.setText(ministry.getBrocs()+"");
		editBrocs.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setBrocs(Integer.valueOf(s.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTracts = (EditText)findViewById(R.id.edit_tracts);
		editTracts.setText(ministry.getTracts()+"");
		editTracts.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setTracts(Integer.valueOf(s.toString()));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		editRemarks = (EditText)findViewById(R.id.edit_remarks);
		editRemarks.setText(ministry.getRemarks());
		editRemarks.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setRemarks(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editStreet = (EditText)findViewById(R.id.edit_street);
		editStreet.setText(ministry.getStreet());
		editStreet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ministry.setStreet(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		editHours = (EditText)findViewById(R.id.edit_hour);
		editMinutes = (EditText)findViewById(R.id.edit_minute);
		final Long duration = ministry.getDuration();
		final Long hour = duration/3600000;
		final Long minute = (duration%3600000)/60000;
		editHours.setText(String.format("%d", hour));
		editHours.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Long newHour = Long.valueOf(s+"")*3600000;
				Long diff = newHour - hour;
				Long newDuration = duration + diff;
				ministry.setDuration(newDuration);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editMinutes.setText(String.format("%d", minute));
		editMinutes.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Long newMinute = Long.valueOf(s+"")*60000;
				Long diff = newMinute - minute;
				Long newDuration = duration + diff;
				ministry.setDuration(newDuration);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ArrayAdapter<CharSequence> adapterPers = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterPers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final List<Householder> householders = getHouseholders(false);
		Log.d(MainActivity.LOG_TAG, "Names : "+householders.size());
		for (int i = 0; i < householders.size(); i++) {
			adapterPers.add(householders.get(i).getName());
		}
		final ArrayAdapter<CharSequence> adapterPersBs = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterPersBs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final List<Householder> householdersBs = getHouseholders(true);
		for (int i = 0; i < householdersBs.size(); i++) {
			adapterPersBs.add(householdersBs.get(i).getName());
		}
		spinPerson = (Spinner)findViewById(R.id.spin_person);
		spinPerson.setAdapter(adapterPers);
		spinPerson.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Householder hse = new Householder();
				GoogleMap gMap = miniWlMap.getMap();
				gMap.clear();
				switch (ministry.getType()) {
				case 2:
					hse = householders.get(position);
					break;
				case 3:
					hse = householdersBs.get(position);
					break;
				default:
					break;
				}
				ministry.setHouseholderId(hse.getId());
				if(editStreet.getText().length()<=0||persPosition!=position){
					editStreet.setText(hse.getStreet());
					persPosition = position;
				}
				if (hse.getMapLat()!=null&&hse.getMapLong()!=null) {
					LatLng latLng = new LatLng(hse.getMapLat().doubleValue(), hse.getMapLong().doubleValue());
					gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
					gMap.addMarker(new MarkerOptions().position(latLng).title(hse.getName()));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				ministry.setHouseholderId(-1L);
				editStreet.setText("");
			}
		});
		
		final String strField = getString(R.string.field);
		final String strInformal = getString(R.string.informal_witnessing);
		final String strRv = getString(R.string.return_visit);
		final String strBs = getString(R.string.bible_study);
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(strField);
		adapter.add(strInformal);
		adapter.add(strRv);
		adapter.add(strBs);
		
		spinType = (Spinner)findViewById(R.id.spin_type);
		spinType.setAdapter(adapter);
		spinType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String type = adapter.getItem(position).toString();
				if (type.equals(strField)) ministry.setType(1);
				if (type.equals(strInformal)) ministry.setType(4);
				if (type.equals(strRv)) {
					ministry.setType(2);
					spinPerson.setAdapter(adapterPers);
					ArrayAdapter<CharSequence> adap = (ArrayAdapter<CharSequence>)spinPerson.getAdapter();
					spinPerson.setSelection(adap.getPosition(ministry.getName()));
				}
				if (type.equals(strBs)) {
					ministry.setType(3);
					spinPerson.setAdapter(adapterPersBs);
					ArrayAdapter<CharSequence> adap = (ArrayAdapter<CharSequence>)spinPerson.getAdapter();
					spinPerson.setSelection(adap.getPosition(ministry.getName()));
				}
				if (type.equals(strRv)||type.equals(strBs)) {
					spinPerson.setVisibility(View.VISIBLE);
				} else spinPerson.setVisibility(View.GONE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		int type = ministry.getType();
		if(type==1)spinType.setSelection(0);
		if(type==2)spinType.setSelection(2);
		if(type==3)spinType.setSelection(3);
		if(type==4)spinType.setSelection(1);
		
		btnOk = (Button) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(!extras.getBoolean(Ministry.IS_EDIT))new MinistryDb(context, ministry).insert();
					else new MinistryDb(context, ministry).update();
					finishAndInitMinistry();
					Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_LONG).show();
				} catch (SQLException e) {
					Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_LONG).show();
					Log.e(MainActivity.LOG_TAG, e.getMessage());
				}
			}
		});
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setText(getString(R.string.btn_back));
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private Ministry initMinistry() {
		Ministry m = new Ministry();
		m.setId(extras.getLong(Ministry.ID));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(extras.getLong(Ministry.CAL_START));
		m.setCalStart(cal);
		cal = Calendar.getInstance();
		cal.setTimeInMillis(extras.getLong(Ministry.CAL_END));
		m.setCalEnd(cal);
		m.setDate(extras.getString(Ministry.DATE));
		m.setDuration(extras.getLong(Ministry.DURATION));
		m.setHouseholderId(extras.getLong(Ministry.PERS_ID));
		m.setName(extras.getString(Ministry.NAME));
		m.setType(extras.getInt(Ministry.TYPE));
		m.setBooks(extras.getInt(Ministry.BOOKS));
		m.setMags(extras.getInt(Ministry.MAGS));
		m.setBrocs(extras.getInt(Ministry.BROCS));
		m.setTracts(extras.getInt(Ministry.TRACTS));
		m.setStreet(extras.getString(Ministry.STREET));
		m.setRemarks(extras.getString(Ministry.REMARKS));
		return m;
	}
	
	private void finishAndInitMinistry() {
		Intent resIntent = new Intent();
		resIntent.putExtra(MinistryFragment.INIT_VIEW, true);
		setResult(Activity.RESULT_OK, resIntent);
		super.finish();
	}
	
	private List<Householder> getHouseholders(boolean isBs){
		List<Householder> housholders = new ArrayList<Householder>();
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		try {
			String sql = "";
			if(isBs) sql = "select id,name,street,map_lat,map_long from householders where bible_student = 1 order by name";
			else sql = "select id,name,street,map_lat,map_long from householders order by name";
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Householder householder = new Householder();
				householder.setId(cursor.getLong(0));
				householder.setName(cursor.getString(1));
				householder.setStreet(cursor.getString(2));
				householder.setMapLat(BigDecimal.valueOf(cursor.getDouble(3)));
				householder.setMapLong(BigDecimal.valueOf(cursor.getDouble(4)));
				housholders.add(householder);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		}  finally {
			pm.close();
		}
		return housholders;
	}

	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		
	}
}
