package com.crazytech.miniwl.ministry.householder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.crazytech.android.dialog.DateTimeDialog;
import com.crazytech.android.image.Image;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.map.MiniWLMapFragment;
import com.crazytech.miniwl.ministry.Ministry;
import com.crazytech.miniwl.ministry.MinistryFragment;
import com.crazytech.miniwl.ministry.MinistryListAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class HouseholderEditActivity extends FragmentActivity {
	private Context context;
	private Householder householder;
	private ImageView imageProf;
	private View layNextVisit; 
	private TextView textNextVisit;
	private Spinner spinAge, spinGender;
	private EditText editName,editLang,editPhone1,editPhone2,
		editEmail,editAddress,editRemarks;
	private CheckBox checkBs;
	private MiniWLMapFragment miniWlMap;
	private Calendar calNextVisit;
	private SimpleDateFormat df = new SimpleDateFormat("EEE dd-MM-yyyy hh:mm a");
	private ImageButton btnMapType,btnMapView;
	private Uri profPicUri;
	private File imgFile;
	public static final int REQ_TAKE_PHOTO = 0;
	public static final int REQ_PICK_PHOTO = 1;
	private boolean fromCamera;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_edit_householder);
		context = this;
		householder = getHouseholderFromExtras();
		declareViews();
		declareMinistries();
		declareMap();
		setViews(householder);
	}
	
	private void declareViews() {
		imageProf = (ImageView)findViewById(R.id.image_profpic);
		imgFile = new File(MainActivity.SD_STORAGE_PATH+"/householders/profile/"+householder.getId()+".jpg");
		if(imgFile.exists()){
			new Image().decodeImageViewBitmap(imageProf, imgFile.getAbsolutePath(), 
					300, 300);
		}
		imageProf.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		
		editName = (EditText)findViewById(R.id.edit_name);
		editName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setName(s.toString());
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
		editLang = (EditText)findViewById(R.id.edit_language);
		editLang.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setLanguage(s.toString());
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
		editPhone1 = (EditText)findViewById(R.id.edit_phone1);
		editPhone1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setPhoneMain(s.toString());
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
		editPhone2 = (EditText)findViewById(R.id.edit_phone2);
		editPhone2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setPhoneHome(s.toString());
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
		editEmail = (EditText)findViewById(R.id.edit_email);
		editEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setEmail(s.toString());
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
		editAddress = (EditText)findViewById(R.id.edit_address);
		editAddress.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setStreet(s.toString());
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
		editRemarks.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				householder.setRemarks(s.toString());
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
		calNextVisit = Calendar.getInstance();
		layNextVisit = findViewById(R.id.lay_next_visit);
		layNextVisit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DateTimeDialog dialog = new DateTimeDialog(context, calNextVisit);
				dialog.show();
				dialog.getPositiveButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						calNextVisit = dialog.getCalendar();
						textNextVisit.setText(df.format(calNextVisit.getTime()));
						householder.setNextVisit(calNextVisit.getTimeInMillis());
						dialog.dismiss();
					}
				});
			}
		});
		textNextVisit = (TextView)findViewById(R.id.text_next_visit);
		spinAge = (Spinner)findViewById(R.id.spin_age);
		ArrayAdapter<CharSequence> adapterAge = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i <=100; i+=5) {
			adapterAge.add(i+"+");
		}
		spinAge.setAdapter(adapterAge);
		spinAge.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String age = spinAge.getAdapter().getItem(position).toString();
				householder.setAge(age);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		spinGender = (Spinner)findViewById(R.id.spin_gender);
		ArrayAdapter<CharSequence> adapterGender = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterGender.add(getString(R.string.male));
		adapterGender.add(getString(R.string.female));
		spinGender.setAdapter(adapterGender);
		
		spinGender.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String gender = parent.getAdapter().getItem(position).toString();
				if(gender.equals(getString(R.string.female))){
					if(!imgFile.exists())imageProf.setImageResource(R.drawable.ic_woman);
					householder.setGender("f");
				}
				else {
					if(!imgFile.exists())imageProf.setImageResource(R.drawable.ic_man);
					householder.setGender("m");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		checkBs = (CheckBox)findViewById(R.id.check_bs);
		checkBs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)householder.setBiStudent(1);
				else householder.setBiStudent(0);
			}
		});
		View btnsConfirmation = findViewById(R.id.inc_btn_confirmation);
		Button btnOk = (Button)btnsConfirmation.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AsyncTask<String, Integer, String>() {
					private ProgressDialog pd = new ProgressDialog(context);
					private boolean failed;
					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub
						pd.setTitle(getString(R.string.householder));
						pd.setMessage(getString(R.string.saving_content));
						pd.setCancelable(false);
						pd.setIndeterminate(true);
						pd.show();
						super.onPreExecute();
					}

					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						boolean isEdit = getIntent().getExtras().getBoolean(Ministry.IS_EDIT);
						HouseholderDb db = new HouseholderDb(context, householder);
						try {
							if (isEdit) db.update();
							else db.insert();
							setProgress(50);
							if (fromCamera) {
								String tempImgDir = MainActivity.SD_STORAGE_PATH+"/householders/profile/temp.jpg";
								String imgDir = MainActivity.SD_STORAGE_PATH+"/householders/profile/";
								new Image().saveTempImage(tempImgDir, imgDir, householder.getId()+".jpg", 300, CompressFormat.JPEG, 50);
							} else if (profPicUri!=null){
								new Image().saveImage(context, profPicUri, MainActivity.SD_STORAGE_PATH+"/householders/profile/", 
										householder.getId()+".jpg", 300, CompressFormat.JPEG, 50);
							}
						} catch (SQLException | IOException e) {
							Log.e(MainActivity.LOG_TAG, e.getMessage());
							failed = true;
						} 
						return null;
					}
					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						if(failed)Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_LONG).show();
						else Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show();
						pd.dismiss();
						super.onPostExecute(result);
						finishAndInitMinistry();
					}
					@Override
					protected void onProgressUpdate(Integer[] values) {
						if(values[0]>=50)pd.setMessage(getString(R.string.saving_image));
						super.onProgressUpdate(values);
					};
				}.execute("");
			}
		});
		Button btnCancel = (Button)btnsConfirmation.findViewById(R.id.btn_cancel);
		btnCancel.setText(getString(R.string.btn_back));
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ImageButton btnMinis = (ImageButton)findViewById(R.id.btn_minis);
		btnMinis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View minis = findViewById(R.id.lay_ministries);
				minis.setVisibility(View.VISIBLE);
				View map = findViewById(R.id.lay_map);
				map.setVisibility(View.GONE);
			}
		});
		ImageButton btnMap = (ImageButton)findViewById(R.id.btn_map);
		btnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View minis = findViewById(R.id.lay_ministries);
				minis.setVisibility(View.GONE);
				View map = findViewById(R.id.lay_map);
				map.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void declareMap(){
		miniWlMap = (MiniWLMapFragment)getSupportFragmentManager().findFragmentById(R.id.frag_map);
		miniWlMap.setGoneView(findViewById(R.id.scroll));
		final GoogleMap gMap = miniWlMap.getMap();
		
		gMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				gMap.clear();
				gMap.addMarker(new MarkerOptions().draggable(true).position(point).title(householder.getName()));
				householder.setMapLat(BigDecimal.valueOf(point.latitude));
				householder.setMapLong(BigDecimal.valueOf(point.longitude));
			}
		});
		gMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker marker) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng latLng = marker.getPosition();
				householder.setMapLat(BigDecimal.valueOf(latLng.latitude));
				householder.setMapLong(BigDecimal.valueOf(latLng.longitude));
			}
			
			@Override
			public void onMarkerDrag(Marker marker) {
				
			}
		});
	}
	
	private void declareMinistries(){
		View minisView = findViewById(R.id.lay_ministries);
		ListView minisList = (ListView)minisView.findViewById(R.id.list_ministries);
		MinistryListAdapter adapter = new MinistryListAdapter(MinistryFragment.getHouseholdersMinistries(context, householder.getId()), context);
		minisList.setAdapter(adapter);
		View btnsBottom = minisView.findViewById(R.id.lay_buttons_bottom);
		btnsBottom.setVisibility(View.GONE);
	}

	private void setViews(Householder hse){
		if(!hse.getGender().equals("m"))imageProf.setImageResource(R.drawable.ic_woman);
		editName.setText(hse.getName());
		editLang.setText(hse.getLanguage());
		editPhone1.setText(hse.getPhoneMain());
		editPhone2.setText(hse.getPhoneHome());
		editEmail.setText(hse.getEmail());
		editAddress.setText(hse.getStreet());
		editRemarks.setText(hse.getRemarks());
		if(hse.getNextVisit()!=0L){
			calNextVisit.setTimeInMillis(hse.getNextVisit());
			textNextVisit.setText(df.format(calNextVisit.getTime()));
		}
		checkBs.setChecked(hse.getBiStudent()==1);
		for (int i = 0; i < spinAge.getAdapter().getCount(); i++) {
			String age = spinAge.getAdapter().getItem(i).toString();
			if (hse.getAge().equals(age)) {
				spinAge.setSelection(i);
				break;
			}
		}
		if(hse.getGender().equals("f"))spinGender.setSelection(1);
		else spinGender.setSelection(0);
		if(!hse.getMapLat().equals(BigDecimal.valueOf(0.0))&&
				!hse.getMapLong().equals(BigDecimal.valueOf(0.0))){
			setMap(hse);
		}
	}
	
	private void setMap(Householder hse){
		GoogleMap gMap = miniWlMap.getMap();
		gMap.clear();
		LatLng latLng = new LatLng(hse.getMapLat().doubleValue(), hse.getMapLong().doubleValue());
		gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
		gMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(hse.getName()));
		
	}
	
	private Householder getHouseholderFromExtras() {
		Bundle extras = getIntent().getExtras();
		return new Householder(extras.getLong(Householder.Fields.ID), 
				extras.getLong(Householder.Fields.MINIS_ID), 
				extras.getString(Householder.Fields.NAME), 
				extras.getString(Householder.Fields.AGE), 
				extras.getString(Householder.Fields.GENDER), 
				extras.getString(Householder.Fields.LANG),
				extras.getLong(Householder.Fields.NEXT_VISIT),
				extras.getString(Householder.Fields.REMARKS),
				extras.getString(Householder.Fields.PHONE1), 
				extras.getString(Householder.Fields.PHONE2), 
				extras.getString(Householder.Fields.EMAIL),
				extras.getString(Householder.Fields.ADDRESS), 
				extras.getBoolean(Householder.Fields.BS)?1:0, 
				BigDecimal.valueOf(extras.getDouble(Householder.Fields.LAT)), 
				BigDecimal.valueOf(extras.getDouble(Householder.Fields.LONG)));
	}
	
	private void selectImage() {
		final CharSequence[] items = { getString(R.string.camera), getString(R.string.select_gallery),
		getString(R.string.cancel) };
		 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(getString(R.string.select_picture));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals(getString(R.string.camera))) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File tempImgDir = new File(MainActivity.SD_STORAGE_PATH+"/householders/profile/");
					if (!tempImgDir.exists()) tempImgDir.mkdirs();
					File tempImgFile = new File(MainActivity.SD_STORAGE_PATH+"/householders/profile/", "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImgFile));
					startActivityForResult(intent, REQ_TAKE_PHOTO);
				} else if (
					items[item].equals(getString(R.string.select_gallery))) {
					Intent intent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(intent,REQ_PICK_PHOTO);
				} else if (items[item].equals(getString(R.string.cancel))) {
					dialog.dismiss();
			}
		}
		});
		builder.show();
	}
	
	private void finishAndInitMinistry() {
		Intent resIntent = new Intent();
		resIntent.putExtra(MinistryFragment.INIT_VIEW, true);
		setResult(Activity.RESULT_OK, resIntent);
		super.finish();
	}
	
	@Override
	protected void onActivityResult(int req, int res, final Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(req, res, data);
		switch (req) {
		case REQ_TAKE_PHOTO:
			final File tempImage = new File(MainActivity.SD_STORAGE_PATH+"/householders/profile/temp.jpg");
			if(res==RESULT_OK&&tempImage.exists()){
				fromCamera = true;
				try {
					new AsyncTask<String, String, String>() {
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
						}

						@Override
						protected String doInBackground(String... params) {
							profPicUri = Uri.parse(tempImage.toString());
							return null;
						}
						protected void onPostExecute(String result) {
							imageProf.setImageURI(profPicUri);
						};
					}.execute("");
					
				} catch (Exception e) {
					Log.e(MainActivity.LOG_TAG, e.getMessage());
					Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_LONG).show();
				}
			}
			break;
		case REQ_PICK_PHOTO:
			if(res==RESULT_OK&&data!=null){
				fromCamera = false;
				try {
					new AsyncTask<String, String, String>() {
						@Override
						protected void onPreExecute() {
							super.onPreExecute();
						}

						@Override
						protected String doInBackground(String... params) {
							profPicUri = data.getData();
							return null;
						}
						protected void onPostExecute(String result) {
							imageProf.setImageURI(profPicUri);
							super.onPostExecute(result);
						};
						
					}.execute("");
					
				} catch (Exception e) {
					Log.e(MainActivity.LOG_TAG, e.getMessage());
					Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}
}
