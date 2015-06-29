package com.crazytech.miniwl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import wei.mark.standout.StandOutWindow;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.crazytech.android.login.Authenticate;
import com.crazytech.android.login.LoginTask;
import com.crazytech.android.network.DownloadFile;
import com.crazytech.android.network.UploadFile;
import com.crazytech.miniwl.adapter.NavDrawerListAdapter;
import com.crazytech.miniwl.bible.BibleFragment;
import com.crazytech.miniwl.bible.BibleStandout;
import com.crazytech.miniwl.connect.Connect;
import com.crazytech.miniwl.daytext.DailyTextFragment;
import com.crazytech.miniwl.daytext.DailyTextStandout;
import com.crazytech.miniwl.ministry.MinistryFragment;
import com.crazytech.miniwl.model.NavDrawerChildItem;
import com.crazytech.miniwl.model.NavDrawerGroupItem;
import com.crazytech.miniwl.wol.WolFragment;
import com.crazytech.util.LocaleUtil;

public class MainActivity extends FragmentActivity implements TaskCallbacks,LoginTask{
	private Bundle bundle;
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ProgressDialog pd;
	private Fragment currFragment;
	private LoginTask loginTask;
	private TaskCallbacks taskCallback;
	private int uploadDownload = 0;
	
	public static android.support.v4.app.FragmentManager fragMan;
	
	public static final String LOG_TAG = "MiniBible";
	public static final String PACKAGE_NAME = "com.crazytech.miniwl";
	public static final String SD_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/"+PACKAGE_NAME+"/";
	public static final String LOC_STORAGE_PATH = Environment.getDataDirectory()+"/data/"+PACKAGE_NAME+"/";
	public static final String OBB_FILE_PATH = Environment.getExternalStorageDirectory()+"/Android/obb/"+PACKAGE_NAME+"/main.1."+PACKAGE_NAME+".obb";
	public static final String OBB_PATH = Environment.getExternalStorageDirectory()+"/Android/obb/"+PACKAGE_NAME;
	public static final String DB_ERROR = "db error";
	public static final String STYLEDAY = "body{background:transparent; color:#000000}";
	public static final String STYLENIGHT = "body{background:#000000; color:#ffffff}";
	
	public static final Integer MINYEAR = 2013;
	private String locale;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;


	private ArrayList<NavDrawerGroupItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loginTask = (LoginTask)this;
		taskCallback = (TaskCallbacks)this;
		locale = getBaseContext().getResources().getConfiguration().locale.toString();
        if (locale!=null)locale = locale.substring(0,2);
       /* File file = new File(OBB_FILE_PATH);
        if (!file.exists()) {
        	new InstallOBB(MainActivity.this, locale,taskCallback).execute();
        	isInstalling =true;
		} 
		*/
        fragMan = getSupportFragmentManager();
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		// getString(R.string.daily_text) = getResources().getStringArray(R.array.pubs_array);

		

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerGroupItem>();

		// adding nav drawer items to array
		// Home
		// List<String> items = getNavItemsTitle();
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.daily_text), R.drawable.ic_daytext, new DailyTextFragment()));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.float_daytext), R.drawable.ic_daytext));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.bible), R.drawable.ic_bible, new BibleFragment()));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.float_bible), R.drawable.ic_bible));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.publications), R.drawable.ic_pubs));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.wol), R.drawable.ic_wol, new WolFragment()));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.my_ministry), R.drawable.ic_briefcase, new MinistryFragment()));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.about_crazytech), R.drawable.ic_about_light));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.language), R.drawable.globe));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.backup), R.drawable.ic_upload));
		navDrawerItems.add(new NavDrawerGroupItem(MainActivity.this, getString(R.string.restore), R.drawable.ic_download));
		// Recycle the typed array

		mDrawerList.setOnGroupClickListener(navItemGroupClickListerner());
		mDrawerList.setOnChildClickListener(navItemChildClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		

		// enabling action bar app icon and behaving it as toggle button
		if (android.os.Build.VERSION.SDK_INT>=11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				if (android.os.Build.VERSION.SDK_INT>=11){
					getActionBar().setTitle(mTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}
			}

			public void onDrawerOpened(View drawerView) {
				if (android.os.Build.VERSION.SDK_INT>=11){
					getActionBar().setTitle(mDrawerTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0,getString(R.string.daily_text), new DailyTextFragment());
		}
		bundle = savedInstanceState;
		mDrawerLayout.openDrawer(mDrawerList);
	}

	/**
	 * Slide menu item click listener
	 * */
	private OnGroupClickListener navItemGroupClickListerner() {
		return new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				NavDrawerGroupItem group = navDrawerItems.get(groupPosition);
				String title = group.getTitle();
				currFragment = group.getFragment();
				if (currFragment!=null) displayView(0, title, currFragment);
				else if (title.equals(getString(R.string.about_crazytech)))showAbout(MainActivity.this);
				else if (title.equals(getString(R.string.language)))changeLang();
				else if (title.equals(getString(R.string.float_bible)))showPopoutBible();
				else if (title.equals(getString(R.string.float_daytext)))showPopoutDaytext();
				else if (title.equals(getString(R.string.backup)))backupRestoreData(0);
				else if (title.equals(getString(R.string.restore)))backupRestoreData(1);
				return false;
			}
		};
	}
	
	private OnChildClickListener navItemChildClickListener() {
		return new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				NavDrawerChildItem child = navDrawerItems.get(groupPosition).getChilds().get(childPosition);
				String title = child.getTitle();
				Fragment frag = child.getFragment();
				displayView(0, title, frag);
				return false;
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position, String title, Fragment fragment) {
		// update the main content by replacing fragments
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction tran = fragmentManager.beginTransaction();
			tran.replace(R.id.frame_container, fragment);
			tran.commit();
			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(position, true);
			// mDrawerList.setSelection(position);
			if (android.os.Build.VERSION.SDK_INT>=11) setTitle(title);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	
	private void backupRestoreData(int mode) {
		SharedPreferences sharedPref = this.getSharedPreferences(MainActivity.class.getSimpleName(), Activity.MODE_PRIVATE);
		Long id = sharedPref.getLong("id",0L);
		String username = sharedPref.getString("username", null);
		String password = sharedPref.getString("password", null);
		uploadDownload = mode;
		if (id!=0L&&username!=null&&password!=null) {
			new Authenticate(this, loginTask).execute(username,password,Connect.PHP.AUTHENTICATE);
		} else {
			Intent intent = new Intent(this, MyLoginActivity.class);
			startActivity(intent);
		}
	}
	
	public void showAbout(Context context){
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.dialog_about);
		dialog.setTitle(getString(R.string.about_crazytech)+" "+getString(R.string.app_name));
		TextView tv = (TextView) dialog.findViewById(R.id.dialog_text);
		tv.setText(R.string.about_text);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		TextView tvVer = (TextView) dialog.findViewById(R.id.dialog_tv_version);
		try {
			String localVer = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			tvVer.setText(context.getString(R.string.version)+" : "+localVer);
		} catch (NameNotFoundException e) {
			Log.e("Package Name Error", e.getMessage());
		}
		Button btn = (Button)dialog.findViewById(R.id.dialog_button_dismiss);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_about);
		dialog.show();
		
	}
	
	private void changeLang() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.language);
		builder.setIcon(getResources().getDrawable(R.drawable.globe));
		final CharSequence[] items = getResources().getStringArray(R.array.applang);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String lang = items[which].toString();
				String locale = new LocaleUtil(MainActivity.this).getLocaleMap().get(lang);
				Configuration config = MainActivity.this.getBaseContext().getResources().getConfiguration();
				config.locale = new Locale(locale);
				MainActivity.this.getBaseContext().getResources().updateConfiguration(config, MainActivity.this.getBaseContext().getResources().getDisplayMetrics());
				MainActivity.this.getApplicationContext().getResources().updateConfiguration(config, MainActivity.this.getBaseContext().getResources().getDisplayMetrics());
				MainActivity.this.getResources().updateConfiguration(config, MainActivity.this.getResources().getDisplayMetrics());
				refresh();
			}
		});
		builder.create().show();
    }
	
	public void showPopoutBible() {
		StandOutWindow.closeAll(this, BibleStandout.class);
		StandOutWindow.show(this, BibleStandout.class, StandOutWindow.DEFAULT_ID+1);
	}
	
	public void showPopoutDaytext() {
		StandOutWindow.closeAll(this, DailyTextStandout.class);
		StandOutWindow.show(this, DailyTextStandout.class, StandOutWindow.DEFAULT_ID+1);
	}
	
	private void refresh() {
        this.finish();
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
	
	public static String htmlLoading(String loadText, String style) {
		return "<html><head><style>"+style+"</style></head><body><center><div style='height:100vh;display:table-cell;vertical-align:middle;text-align:center'><img src='loading.gif' width='150px'/><br/>"
				+"<span style='font-size:12pt;text-transform: uppercase;color:#7c8183'>"+loadText+"</span></div></center></body></html>";
	}
	
	public static Map<String, String> getWtLocaleMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("en", "E");
		map.put("zh", "CHS");
		map.put("ja", "J");
		map.put("ko", "KO");
		map.put("fr", "F");
		map.put("de", "X");
		map.put("ms", "ML");
		map.put("es", "S");
		return map;
	}
	
	public static Integer getMaxYear(Context context) {
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		Cursor cursor = db.rawQuery("select issue_date from pubs_wt order by issue_date desc limit 1", null);
		cursor.moveToFirst();
		if (cursor.getCount()>0) {
			String issuedate = cursor.getString(cursor.getColumnIndex("issue_date"));
			cursor.close();pm.close();
			String year = issuedate.substring(0,4);
			return Integer.valueOf(year);
		}
		return new Date().getYear()+1900;
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	
	/*
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
			Runnable okRun = new Runnable() {
				@Override
				public void run() {
					MainActivity.super.onBackPressed();
				}
			};
			String appName = getString(R.string.app_name);
			new ConfirmDialog(MainActivity.this, MainActivity.this, 
					appName, getString(R.string.exit_app_confirm, appName), okRun, null);
		} else {
			mDrawerLayout.openDrawer(GravityCompat.START);
		}
		
	}*/

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
		currFragment.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void finish() {
		loginTask = null;
		taskCallback = null;
		super.finish();
	}

	@Override
	public void onPreExecute() {
		pd = new ProgressDialog(this);
	}

	@Override
	public void onProgressUpdate(Object... params) {
		try {
			pd.setProgress(Integer.valueOf(params[0]+""));
			
		} catch (NumberFormatException e) {
			Log.w(this.getClass().getSimpleName(), e.getMessage());
		}
	}

	@Override
	public void onCancelled() {
		try {
			pd.dismiss();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onPostExecute() {
		try {
			pd.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public ProgressDialog getProgressDialog() {
		return pd;
	}

	@Override
	public void setProgressDialog(ProgressDialog pd) {
		this.pd = pd;
	}

	@Override
	public void onAunthenticatePostExecute(Integer result, Long id) {
		try {
			pd.dismiss();
			if (result==1) {
				String title,msg;
				switch (uploadDownload) {
				case 0:
					title = getString(R.string.backup);
					msg = getString(R.string.performing_backup);
					UploadFile upload = new UploadFile(this, R.drawable.ic_upload_dark, title, msg);
					upload.execute(LOC_STORAGE_PATH+"/databases/miniWLDB",Connect.PHP.UPLOAD, id+"");
					break;
				case 1:
					title = getString(R.string.restore);
					msg = getString(R.string.restoring);
					DownloadFile download = new DownloadFile(this, R.drawable.ic_download_dark, title, msg);
					download.execute(LOC_STORAGE_PATH+"/databases/miniWLDB", Connect.URL.DBDIR+"_"+id);
					break;
				}
			}  else {
				Intent intent = new Intent(this, MyLoginActivity.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onProgressUpdate() {
		// TODO Auto-generated method stub
		
	}
	
}
