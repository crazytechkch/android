package com.crazytech.miniwl.pubs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.crazytech.json.JSONParser;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.MyAsyncTask;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.miniwl.connect.Connect;
import com.crazytech.miniwl.pubs.booklets.PubsBookletsFragment;
import com.crazytech.miniwl.pubs.books.PubsBooksFragment;
import com.crazytech.miniwl.pubs.brochures.PubsBrochuresFragment;
import com.crazytech.miniwl.pubs.g.PubsAwakeFragment;
import com.crazytech.miniwl.pubs.okm.PubsOkmFragment;
import com.crazytech.miniwl.pubs.wt.PubsWtAdapter;
import com.crazytech.miniwl.pubs.wt.PubsWtFragment;
import com.crazytech.miniwl.pubs.wt.PubsWtObj;
import com.crazytech.miniwl.pubs.yearbooks.PubsYearbooksFragment;

public class RefreshPubs extends MyAsyncTask{
	private Context _context;
	private Activity _activity;
	private boolean failed;
	private String table;
	private String locale;
	public static final String REFRESH_ALL = "refreshall";
	public static final String REFRESH_ONE = "refreshone";
	
	public RefreshPubs(Activity activity, Context context, TaskCallbacks taskCallback) {
		super(taskCallback);
		_context = context;
		_activity = activity;
		locale = _context.getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0,2);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd.setTitle(_context.getString(R.string.publications));
		pd.setIcon(_context.getResources().getDrawable(R.drawable.ic_pubs));
		pd.setMessage(String.format(_context.getString(R.string.refreshing),""));
		pd.setCancelable(false);
		pd.setIndeterminate(true);
		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		table = params[0];
		
		if(params[1].equals(REFRESH_ALL)){
			String refresh = _context.getString(R.string.refreshing);
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_watchtower)));
			refreshDbTable("pubs_wt");
			loadImgres("pubs_wt");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_awake)));
			refreshDbTable("pubs_g");
			loadImgres("pubs_g");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_okm)));
			refreshDbTable("pubs_okm");
			loadImgres("pubs_okm");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_books)));
			refreshDbTable("pubs_books");
			loadImgres("pubs_books");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_booklets)));
			refreshDbTable("pubs_booklets");
			loadImgres("pubs_booklets");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_brochures)));
			refreshDbTable("pubs_brochures");
			loadImgres("pubs_brochures");
			publishProgress(String.format(refresh,_context.getString(R.string.pubs_yearbooks)));
			refreshDbTable("pubs_yearbooks");
			loadImgres("pubs_yearbooks");
			
		} else if(params[1].equals(REFRESH_ONE)){
			Map<String, String> map = new HashMap<String, String>();
			map.put("pubs_wt", _context.getString(R.string.pubs_watchtower));
			map.put("pubs_g", _context.getString(R.string.pubs_awake));
			map.put("pubs_okm", _context.getString(R.string.pubs_okm));
			map.put("pubs_books", _context.getString(R.string.pubs_books));
			map.put("pubs_booklets", _context.getString(R.string.pubs_booklets));
			map.put("pubs_brochures", _context.getString(R.string.pubs_brochures));
			map.put("pubs_yearbooks", _context.getString(R.string.pubs_yearbooks));
			String refresh = _context.getString(R.string.refreshing);
			publishProgress(String.format(refresh,map.get(table)));
			refreshDbTable(table);
			loadImgres(table);
		}
		return null;
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		initPubs();
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(String result) {
		initPubs();
		String refresh = String.format(getString(R.string.refresh), "");
		String finished = String.format(getString(R.string.finished),refresh);
		if(!failed){
			Toast.makeText(_context, finished, Toast.LENGTH_LONG).show();
		}else{
			finished = String.format(getString(R.string.finished_err),refresh);
			String errorText = "";
			Toast.makeText(_context, String.format(finished, errorText), Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}
	
	private String getString(int resId){
		return _context.getString(resId);
	}
	
	private void initPubs() {
		if (table.equals("pubs_wt")) initPubsMags(R.id.listview_pubs_wt, new PubsWtFragment().localMagList(_context));
		if (table.equals("pubs_g")) initPubsMagsAwake(new PubsAwakeFragment().localMagList(_context));
		if (table.equals("pubs_okm")) initPubsMagsAwake(new PubsOkmFragment().localMagList(_context));
		if (table.equals("pubs_books")) initPubsBooks(new PubsBooksFragment().localPubList(_context));
		if (table.equals("pubs_booklets")) initPubsBooks(new PubsBookletsFragment().localPubList(_context));
		if (table.equals("pubs_brochures")) initPubsBooks(new PubsBrochuresFragment().localPubList(_context));
		if (table.equals("pubs_yearbooks")) initPubsYearbooks(new PubsYearbooksFragment().localPubList(_context));
		
	}
	
	
	private void initPubsMags(int listviewRes, List<PubsWtObj> magObjs) {
		PubsWtAdapter adapter = new PubsWtAdapter(_activity, _context, R.layout.listitem_pub_wt, 
				magObjs);
		ListView lv = (ListView)_activity.findViewById(listviewRes);
		lv.setAdapter(adapter);
	}
	
	private void initPubsMagsAwake(List<PubsMagObj> obj) {
		PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(_activity, _context, obj);
		GridView gv = (GridView)_activity.findViewById(R.id.pubsawake_gridview);
		gv.setAdapter(adapter);
	}
	
	private void initPubsBooks(List<PubsMagObj> obj) {
		PubsImgAdapter adapter = new PubsImgAdapter(_activity, _context, obj);
		GridView gv = (GridView)_activity.findViewById(R.id.gv_pubs_books);
		gv.setAdapter(adapter);
	}
	
	private void initPubsYearbooks(List<PubsMagObj> obj){
		PubsImgWithDescAdapter adapter = new PubsImgWithDescAdapter(_activity, _context, obj);
		GridView gv = (GridView)_activity.findViewById(R.id.gv_pubs_books);
		gv.setAdapter(adapter);
	}
	
	private void refreshDbTable(String table){
    	PersistanceManager pm = new PersistanceManager(_context);
    	pm.open();
    	JSONParser jParser = new JSONParser();
    	List<NameValuePair> nvp = new ArrayList<NameValuePair>();
    	nvp.add(new BasicNameValuePair("table", table));
    	nvp.add(new BasicNameValuePair("locale", locale));
    	JSONObject json;
		try {
			json = jParser.makeHttpRequest(getPhpUrl(table, locale), "GET", nvp);
			int success = json.getInt("success");
			
			if (success == 1) {
				JSONArray pubs = json.getJSONArray("pubs");
				SQLiteDatabase db = pm.getDb();
//    		pm.recreate(table);
				db.execSQL("delete from "+table+" where locale = ?", new String[]{locale});
				for (int i = 0; i < pubs.length(); i++) {
					JSONObject c = pubs.getJSONObject(i);
					if (c.getString("locale").equals(locale)) {
						ContentValues values = new ContentValues();
//    			values.put("id", c.getInt("id"));
						values.put("filename", c.getString("filename"));
						values.put("description", c.getString("description"));
						values.put("locale", c.getString("locale"));
						values.put("issue_date", c.getString("issue_date"));
						/*if (table.equals("pubs_wt")) {
							values.put("dt_start", c.getString("dt_start"));
							values.put("dt_start", c.getString("dt_end"));
						}*/
						values.put("url", c.getString("url"));
						values.put("imgurl", c.getString("imgurl"));
						db.insert(table, "", values);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			failed = true;
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			failed = true;
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			failed = true;
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			failed = true;
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			failed = true;
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		}
    	
    	pm.close();
    }
	
	private void loadImgres(String table){
    	PersistanceManager pm = new PersistanceManager(_context);
    	pm.open();
    	SQLiteDatabase db = pm.getDb();
    	
    	Cursor cursor = db.rawQuery("select filename,description,imgurl from "+table+" where locale=?",new String[]{locale});
    	cursor.moveToFirst();
    	while (!cursor.isAfterLast()) {
    		String imgName = cursor.getString(0)/*+".jpg"*/;
    		String desc = cursor.getString(1);
    		String imgurl = cursor.getString(2);
    		String localDir = MainActivity.SD_STORAGE_PATH+getImgresPath(table)+imgName;
			File file = new File(localDir);
			if (!file.exists()) {
				try{
					file.mkdirs();
					file.delete();
					if(imgurl==null||imgurl.equals("null")||imgurl.equals(""))imgurl = Connect.URL_HOST+getImgresPath(table)+imgName+".jpg";
					imgurl = imgurl.replace("http://www.", "http://assets.");
					//String netDir = Connect.URL_HOST+getImgresPath(table)+imgName+".jpg";
					Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgurl).getContent());
					file = new File(MainActivity.SD_STORAGE_PATH+getImgresPath(table));
					File imgfile = new File(file, imgName);
					FileOutputStream out = new FileOutputStream(imgfile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
				} catch (IOException e) {
					failed = true;
				}
				
			}
			cursor.moveToNext();
		}
    	cursor.close();
    	pm.close();
    }

	private String getPhpUrl(String table, String locale) {
		/*if (table.equals("pubs_wt")||table.equals("pubs_g")||table.equals("pubs_okm")) return Connect.PHP.GET_PUBS_WT;
		if (table.equals("pubs_books")||table.equals("pubs_booklets")||
			table.equals("pubs_brochures")||table.equals("pubs_yearbooks")) 
			return Connect.PHP.GET_PUBS_BOOKS;
			*/
		return Connect.PHP.GET_PUBS_WT+"?table="+table+"&locale="+locale;
	}
	
	private String getImgresPath (String table) {
		if (table.equals("pubs_wt")) return "pubs/wt/";
		if (table.equals("pubs_g")) return "pubs/g/";
		if (table.equals("pubs_okm")) return "pubs/okm/";
		if (table.equals("pubs_books")) return "pubs/books/";
		if (table.equals("pubs_booklets")) return "pubs/booklets/";
		if (table.equals("pubs_brochures")) return "pubs/brochures/";
		if (table.equals("pubs_yearbooks")) return "pubs/yearbooks/";
		return "";
	}
	
	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		pd.setMessage(values[0]+"");
		super.onProgressUpdate(values);
	}
}
