package com.crazytech.miniwl.pubs.wt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.crazytech.android.network.Network;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.miniwl.pubs.PubsMagObj;
import com.crazytech.miniwl.pubs.RefreshPubs;

public class PubsWtFragment extends Fragment {
	private View rootV;
	private String locale;
	private Network network;
	private Spinner spinnerYear;
	private TaskCallbacks taskCallback;
	
	public PubsWtFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.fragment_pubs_wt, container, false);
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0,2);
		initSpinners();
		network = new Network(getActivity());
		if (localMagList(getActivity()).size()==0) {
			invokeRefresh(RefreshPubs.REFRESH_ONE);
		} else {
			initListPubsWt();
			List<PubsWtObj> wtList = localMagList(getActivity());
			//initCurrentIssues(wtList,(LinearLayout)rootV.findViewById(R.id.wt_current_panel1),2);
			//initCurrentIssues(wtList,(LinearLayout)rootV.findViewById(R.id.wt_current_panel2),1);
		}
		return rootV;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		taskCallback = (TaskCallbacks)activity;
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		taskCallback = null;
	}
	
	private void initListPubsWt(){
		ListView lv = (ListView)rootV.findViewById(R.id.listview_pubs_wt);
		PubsWtAdapter adapter = new PubsWtAdapter(getActivity(), getActivity(), R.layout.listitem_pub_wt, localMagList(getActivity()));
		lv.setAdapter(adapter);
	}
	
	private void invokeRefresh(String refreshMethod){
		ListView lv = (ListView)rootV.findViewById(R.id.listview_pubs_wt);
		if (network.isNetworkAvailable()) new RefreshPubs(getActivity(), getActivity(),taskCallback).execute("pubs_wt",refreshMethod);
		else {
			network.showNetworkDialog();
			PubsWtAdapter adapter = new PubsWtAdapter(getActivity(), getActivity(), R.layout.listitem_pub_wt, localMagList(getActivity()));
			lv.setAdapter(adapter);
		}
	}
	
	public Integer getDmWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels/3;
	}
	
	public List<PubsWtObj> localMagList(Context context){
		locale = context.getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		List<PubsWtObj> wtLst = new ArrayList<PubsWtObj>();
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		String year = spinnerYear!=null?spinnerYear.getSelectedItem().toString():(new Date().getYear()+1900)+"";
		Cursor cursor = db.rawQuery("select distinct issue_date from pubs_wt where locale = ? and issue_date like ? order by issue_date desc", 
				new String[]{locale,year+"%"});
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PubsWtObj wt = new PubsWtObj();
			String issueDate = cursor.getString(cursor.getColumnIndex("issue_date"));
			wt.setIssueDate(issueDate);
			List<PubsMagObj> magLst = new ArrayList<PubsMagObj>();
			Cursor magCursor = db.query("pubs_wt", new String[]{"filename","description","locale","url","issue_date","dt_start","dt_end"}, 
					"issue_date=? and locale=?", new String[]{issueDate,locale}, null, null, "filename");
			magCursor.moveToFirst();
			while (!magCursor.isAfterLast()) {
				PubsMagObj mag = new PubsMagObj(magCursor, "wt");
				magCursor.moveToNext();
				magLst.add(mag);
			}
			magCursor.close();
			wt.setMagLst(magLst);
			wtLst.add(wt);
			cursor.moveToNext();
			
		}
		cursor.close();
		pm.close();
		if (year.equals((new Date().getYear()+1900)+"")) {
			PubsWtObj currWtObj = new PubsWtObj();
			Date date = new Date();
			currWtObj.setIssueDate(context.getString(R.string.issue_of_the_month));
			List<PubsMagObj> currMagLst = new ArrayList<PubsMagObj>();
			PubsMagObj currMag1 = initCurrentIssue(wtLst, 3,context);
			currMagLst.add(currMag1);
			PubsMagObj currMag2 = initCurrentIssue(wtLst, 2,context);
			currMagLst.add(currMag2);
			currWtObj.setMagLst(currMagLst);
			wtLst.add(0, currWtObj);
		}
		return wtLst;
	}
	
	private PubsMagObj initCurrentIssue(List<PubsWtObj> wtList, Integer monthCount, Context context) {
		PubsMagObj mag = new PubsMagObj();
		Date date = new Date();
		date.setMonth(date.getMonth()-monthCount);
		String dateStr = new SimpleDateFormat("yyyy_MM").format(date);
		String filename = "w_"+MainActivity.getWtLocaleMap().get(locale)+"_"+new SimpleDateFormat("yyyyMM").format(date)+"15";
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase  db = pm.getDb();
		Cursor cursor = db.rawQuery("select distinct * from pubs_wt where filename = ?", new String[]{filename});
		/*for (int i = 0; i < wtList.size(); i++) {
			PubsWtObj wtObj = wtList.get(i);
			if (wtObj.getLongIssueDate().startsWith(dateStr)) {
				mag = wtObj.getMagLst().get(0);
				break;
			}
		}*/
		cursor.moveToFirst();
		if(cursor.getCount()>0)mag = new PubsMagObj(cursor, "wt");
		else {
			mag.setFilename(filename);
			mag.setIssueDate(new SimpleDateFormat("yyyy_MM_yyyy MM").format(date));
		}
		cursor.close();
		pm.close();
		return mag;
	}
	
	private void initSpinners(){
		spinnerYear = (Spinner)rootV.findViewById(R.id.spinner_year);
		ArrayAdapter<CharSequence> adpYear = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item);
		adpYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Integer year = MainActivity.getMaxYear(getActivity());
		for (int i = year; i >= MainActivity.MINYEAR; i--) {
			adpYear.add(i+"");
		}
		spinnerYear.setAdapter(adpYear);
		if (year>Calendar.getInstance().get(Calendar.YEAR)&&adpYear.getCount()>=2) {
			spinnerYear.setSelection(1);
		}
		spinnerYear.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				initListPubsWt();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
    	config.locale = new Locale(locale);
    	Log.d("WolActivity:locale", config.locale.toString());
    	getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.menu_pubs, menu);
    	menu.findItem(R.id.refresh).setTitle(String.format(getString(R.string.refresh), getString(R.string.pubs_watchtower)));
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.refresh:
			invokeRefresh(RefreshPubs.REFRESH_ONE);
			break;
		case R.id.refresh_all:
			invokeRefresh(RefreshPubs.REFRESH_ALL);
			break;
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
}
