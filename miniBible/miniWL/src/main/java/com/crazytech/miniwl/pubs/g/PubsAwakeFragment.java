package com.crazytech.miniwl.pubs.g;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crazytech.android.network.Network;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.miniwl.pubs.PDFUtil;
import com.crazytech.miniwl.pubs.PubsImgWithDescAdapter;
import com.crazytech.miniwl.pubs.PubsMagObj;
import com.crazytech.miniwl.pubs.RefreshPubs;
import com.crazytech.miniwl.pubs.wt.PubsWtObj;

public class PubsAwakeFragment extends Fragment {
	private View rootV;
	private String locale;
	private Network network;
	private Spinner spinnerYear;
	private TextView tvYear;
	private PDFUtil pdf;
	private TaskCallbacks taskCallback;
	private String table = "pubs_g";
	
	public PubsAwakeFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.fragment_pubs_awake, container, false);
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0,2);
		network = new Network(getActivity());
		pdf = new PDFUtil(getActivity());
		initSpinners();
		Integer year = Integer.valueOf(spinnerYear.getSelectedItem().toString());
		tvYear = (TextView)rootV.findViewById(R.id.pubsawake_textview_year);
		tvYear.setText(year+"");
		if (localMagList(getActivity()).size()==0) {
			invokeRefresh(RefreshPubs.REFRESH_ONE);
		} else {
			initListPubsAwake(getTable());
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
	
	protected void initListPubsAwake(String table){
		GridView gv = (GridView)rootV.findViewById(R.id.pubsawake_gridview);
		final List<PubsMagObj> magLst = localMagList(getActivity());
		PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(getActivity(), getActivity(), magLst);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				pdf.openPDF(magLst.get(position));
			}
		});
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				final Context context = getActivity();
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle(magLst.get(position).getDesc());
				dialog.setIcon(context.getResources().getDrawable(R.drawable.ic_pubs));
				final CharSequence[] items = context.getResources().getStringArray(R.array.pdf_dialog);
				dialog.setItems(items, new DialogInterface.OnClickListener() {
				
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (items[which].equals(context.getResources().getString(R.string.open_pdf))) {
							pdf.openPDF(magLst.get(position));
						}
						if (items[which].equals(context.getResources().getString(R.string.delete))) {
							pdf.deletePDF(magLst.get(position));
						}
					}
				});
				dialog.create().show();
				return true;
			}
		});
	}
	
	private void invokeRefresh(String refreshMethod){
		if (network.isNetworkAvailable()) new RefreshPubs(getActivity(), getActivity(),taskCallback).execute(getTable(),refreshMethod);
		else {
			network.showNetworkDialog();
			initListPubsAwake(getTable());
		}
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
				// TODO Auto-generated method stub
				String yearStr = parent.getItemAtPosition(pos).toString();
				initListPubsAwake(getTable());
				tvYear.setText(yearStr);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
    
   public List<PubsMagObj> localMagList(Context context){
		locale = context.getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		Log.d("PubsAwakeActivity:Locale", locale);
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		PubsWtObj wt = new PubsWtObj();
		String issueDate = spinnerYear!=null?spinnerYear.getSelectedItem().toString():(new Date().getYear()+1900)+"";
		wt.setIssueDate(issueDate);
		List<PubsMagObj> magLst = new ArrayList<PubsMagObj>();
		Cursor magCursor = db.query(getTable(), new String[]{"filename","description","locale","issue_date","url"}, 
				"issue_date like ? and locale=?", new String[]{issueDate+"%",locale}, null, null, "filename desc");
		magCursor.moveToFirst();
		while (!magCursor.isAfterLast()) {
			PubsMagObj mag = new PubsMagObj(magCursor,getPubType());
			magCursor.moveToNext();
			magLst.add(mag);
		}
		magCursor.close();
		wt.setMagLst(magLst);
		pm.close();
		return magLst;
	}
   
   public String getLongPublicationType() {
	   return getString(R.string.pubs_awake);
   }
    
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
   	inflater.inflate(R.menu.menu_pubs, menu);
   	menu.findItem(R.id.refresh).setTitle(String.format(getString(R.string.refresh), getLongPublicationType()));
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
   
   	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
   		Configuration config = getActivity().getBaseContext().getResources().getConfiguration();
   		config.locale = new Locale(locale);
   		Log.d("WolActivity:locale", config.locale.toString());
   		getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
   		newConfig = config;
    	super.onConfigurationChanged(newConfig);
    }

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String getPubType(){
		return "g";
	}
    
}
