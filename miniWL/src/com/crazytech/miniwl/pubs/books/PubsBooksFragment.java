package com.crazytech.miniwl.pubs.books;

import java.util.ArrayList;
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
import android.widget.GridView;

import com.crazytech.android.network.Network;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.miniwl.pubs.PDFUtil;
import com.crazytech.miniwl.pubs.PubsImgAdapter;
import com.crazytech.miniwl.pubs.PubsMagObj;
import com.crazytech.miniwl.pubs.RefreshPubs;

public class PubsBooksFragment extends Fragment{
	private View rootV;
	protected String locale, table = "pubs_books", type = "books";
	protected Network network;
	private PDFUtil pdf;
	protected TaskCallbacks taskCallback;
	
	public PubsBooksFragment() {
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
		rootV = inflater.inflate(R.layout.fragment_pubs_books, container, false);
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0,2);
		network = new Network(getActivity());
		pdf = new PDFUtil(getActivity());
		if (localPubList(getActivity()).size()==0) {
			invokeRefresh(RefreshPubs.REFRESH_ONE);
		} else {
			initPubsBooks();
		}
		return rootV;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		taskCallback  = (TaskCallbacks)activity;
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		taskCallback = null;
	}
	
	protected void initPubsBooks(){
		GridView gv = (GridView)rootV.findViewById(R.id.gv_pubs_books);
		PubsImgAdapter adapter = new PubsImgAdapter(getActivity(), getActivity(), localPubList(getActivity()));
		gv.setAdapter(adapter);
		initGvListeners(gv);
	}
	
	protected void invokeRefresh(String refreshMethod) {
		GridView gv = (GridView)rootV.findViewById(R.id.gv_pubs_books);
		if (network.isNetworkAvailable()) new RefreshPubs(getActivity(), getActivity(),taskCallback).execute(getTable(),refreshMethod);
		else {
			network.showNetworkDialog();
			PubsImgAdapter adapter = new PubsImgAdapter(getActivity(), getActivity(), localPubList(getActivity()));
			gv.setAdapter(adapter);
		}
		initGvListeners(gv);
	}
	
	protected void initGvListeners(GridView gv) {
		gv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				pdf.openPDF(localPubList(getActivity()).get(position));
				
			}
		});
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setTitle(localPubList(getActivity()).get(position).getDesc());
				dialog.setIcon(getResources().getDrawable(R.drawable.ic_pubs));
				final CharSequence[] items = getResources().getStringArray(R.array.pdf_dialog);
				dialog.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (items[which].equals(getResources().getString(R.string.open_pdf))) {
							pdf.openPDF(localPubList(getActivity()).get(position));
						}
						if (items[which].equals(getResources().getString(R.string.delete))) {
							pdf.deletePDF(localPubList(getActivity()).get(position));
						}
					}
				});
				dialog.create().show();
				return true;
			}
		});
		
	}
	
	public List<PubsMagObj> localPubList(Context context){
		locale = context.getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		List<PubsMagObj> pubLst = new ArrayList<PubsMagObj>();
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		Cursor cursor = db.query(getTable(), 
				new String[]{"filename","description","locale","url","issue_date"}, 
				"locale=?", new String[]{locale}, null, null, "issue_date desc,description");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PubsMagObj pub = new PubsMagObj(cursor, getType());
			cursor.moveToNext();
			pubLst.add(pub);
		}
		cursor.close();
		pm.close();
		return pubLst;
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
	
	public String getLongPublicationType() {
		return getString(R.string.pubs_books);
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

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public View getRootV() {
		return rootV;
	}
	
}
