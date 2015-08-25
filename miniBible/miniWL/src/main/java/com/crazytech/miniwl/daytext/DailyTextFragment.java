package com.crazytech.miniwl.daytext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crazytech.io.IOUtil;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.MyAsyncTask;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.util.LocaleUtil;
import com.crazytech.xslt.XSLT;

public class DailyTextFragment extends Fragment {
	private String locale1,locale2;
	private String html;
	
	private View rootV;
	private WebView webV1,webV2;
	
	private ImageButton btnShowDual;
	
	private Date newDate,nextDate,prevDate;
	private Integer year,month,day;
	
	private boolean isNightMode;
	
	private TaskCallbacks taskCallback;
	
	public DailyTextFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		locale1 = getResources().getConfiguration().locale.toString();
		if (locale1!=null) locale1=locale1.substring(0,2);
		locale2=locale1;
        rootV = inflater.inflate(R.layout.fragment_daytext, container, false);
        webV1 = (WebView)rootV.findViewById(R.id.webV_daytext_1);
		webV1.getSettings().setSupportZoom(true);
		webV1.getSettings().setBuiltInZoomControls(true);
		webV1.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		//webV1.getSettings().setLoadWithOverviewMode(true);
		webV1.getSettings().setUseWideViewPort(false);
		//webV1.getSettings().setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		webV2 = (WebView)rootV.findViewById(R.id.webV_daytext_2);
		webV2.getSettings().setSupportZoom(true);
		webV2.getSettings().setBuiltInZoomControls(true);
		webV2.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		//webV2.getSettings().setLoadWithOverviewMode(true);
		webV2.getSettings().setUseWideViewPort(false);
		//webV2.getSettings().setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		initText(new Date(),webV1,locale1);
		setNewDate(new Date());
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
		initButtonNext();
		initButtonPrev();
		initButtonToday();
		initButtonShowDual();
		initButtonChangeLang((ImageButton)rootV.findViewById(R.id.btn_daytextLocale1),1);
		initButtonChangeLang((ImageButton)rootV.findViewById(R.id.btn_daytextLocale2),2);
        return rootV;
    }
	
	private void initText(Date date, WebView webV, String locale){
		String inYearLong = (String)DateFormat.format("yyyy", date);
		String inMonth = (String)DateFormat.format("MM", date);
		String inDay = (String)DateFormat.format("dd", date);
		String dateStr = inDay+"-"+inMonth+"-"+inYearLong;
		new LoadXSLT(taskCallback,webV).execute(locale,dateStr,inYearLong,getDisplayStyle());
		
	}
	
	private void initButtonPrev(){
		ImageButton button = (ImageButton)rootV.findViewById(R.id.btn_daytext_prev);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				previousText();
			}
		});
	}
	
	private void initButtonToday(){
		Button button = (Button)rootV.findViewById(R.id.btn_daytext_today);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initText(new Date(),webV1,locale1);
				if (getWebV2Layout().getVisibility()==View.VISIBLE) initText(new Date(), webV2, locale2);
				setNewDate(new Date());
				year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
				month = Integer.parseInt((String)DateFormat.format("MM", newDate));
				day = Integer.parseInt((String)DateFormat.format("dd", newDate));
			}
		});
	}
	
	private void initButtonNext(){
		ImageButton button = (ImageButton)rootV.findViewById(R.id.btn_daytext_next);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextText();
			}
		});
	}
	
	private void initButtonShowDual() {
		btnShowDual = (ImageButton)rootV.findViewById(R.id.btn_showdual);
		btnShowDual.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDualView();
			}
		});
	}
	
	private void initButtonChangeLang(ImageButton btn, final Integer loc1or2) {
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeLang(loc1or2);
			}
		});
	}
	
	private void nextText(){
		nextDate = new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()+1);
		initText(nextDate,webV1,locale1);
		if (isWebView2Visible()) initText(nextDate, webV2, locale2);
		setNewDate(nextDate);
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
		
	}
	
	private void previousText(){
		prevDate = new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()-1);
		initText(prevDate,webV1,locale1);
		if (isWebView2Visible()) initText(prevDate, webV2, locale2);
		setNewDate(prevDate);
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
	}
	
	private void changeLang(final Integer locale1Or2) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.language);
		builder.setIcon(getResources().getDrawable(R.drawable.globe));
		final CharSequence[] items = getResources().getStringArray(R.array.applang);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (locale1Or2) {
				case 1:
					locale1 = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
					initText(newDate, webV1, locale1);
					break;
				case 2:
					locale2 = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
					initText(newDate, webV2, locale2);
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
    }
	
	private void showDualView(){
    	FrameLayout fl2 = getWebV2Layout();
    	if (!isWebView2Visible()) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		builder.setTitle(R.string.language);
    		builder.setIcon(getResources().getDrawable(R.drawable.globe));
    		final CharSequence[] items = getResources().getStringArray(R.array.applang);
    		builder.setItems(items, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				locale2 = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
    				initText(newDate,webV2,locale2);
    			}
    		});
    		builder.create().show();
    		fl2.setVisibility(View.VISIBLE);
    		btnShowDual.setImageResource(R.drawable.langsingle);
    	}
    	else {
    		fl2.setVisibility(View.GONE);
    		btnShowDual.setImageResource(R.drawable.langdual);
    	}
    }
	
	private FrameLayout getWebV2Layout(){
		FrameLayout fl2 = (FrameLayout)rootV.findViewById(R.id.daytext_frameLyt_locale2);
		return fl2;
	}
	
	public Date getNewDate() {
		return newDate;
	}

	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
    	
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
	
	private boolean isWebView2Visible(){
		return getWebV2Layout().getVisibility()==View.VISIBLE;
	}
	
	
	private void changeDisplayMode(MenuItem item) {
		isNightMode = !isNightMode;
		initText(newDate,webV1,locale1);
		if (isWebView2Visible()) initText(newDate, webV2, locale2);
		LinearLayout linlay = (LinearLayout)rootV.findViewById(R.id.linlay_daytext);
		Button btnToday = (Button)rootV.findViewById(R.id.btn_daytext_today);
		if (isNightMode) {
			item.setTitle(getString(R.string.day_mode));
			linlay.setBackgroundColor(getResources().getColor(android.R.color.black));
			btnToday.setTextColor(getResources().getColor(android.R.color.white));
		}
		else {
			item.setTitle(getString(R.string.night_mode));
			linlay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			btnToday.setTextColor(getResources().getColor(android.R.color.black));
		}
	}
	
	private String getDisplayStyle() {
		if(isNightMode) return MainActivity.STYLENIGHT;
		return MainActivity.STYLEDAY;
	}
	
	private void changeDate() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.dialog_changedate);
		dialog.setTitle(R.string.date);
		LinearLayout timePanel = (LinearLayout)dialog.findViewById(R.id.date_time_panel);
		timePanel.setVisibility(View.GONE);
		final CalendarView cv = (CalendarView)dialog.findViewById(R.id.date_calV);
		cv.setDate(newDate.getTime());
		Button btnConfirm = (Button)dialog.findViewById(R.id.date_btn_done);
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newDate = new Date(cv.getDate());
				initText(newDate, webV1, locale1);
				if (isWebView2Visible()) initText(newDate, webV2, locale2);
				dialog.dismiss();
			}
		});
		Button btnCancel = (Button)dialog.findViewById(R.id.date_btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_calendar);
		dialog.show();
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.menu_daytext, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.changedate:
			changeDate();
			break;
    	case R.id.displaymode:
			changeDisplayMode(item);
			break;
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
	
	private class LoadXSLT extends MyAsyncTask {
		private WebView webV;
		
		public LoadXSLT(TaskCallbacks taskCallback, WebView webV) {
			super(taskCallback);
			this.webV = webV;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			webV.loadDataWithBaseURL("file:///android_asset/", MainActivity.htmlLoading(getString(R.string.loading),getDisplayStyle()), "text/html", "UTF-8", null);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String locale = params[0];
			String date  = params[1];
			String year = params[2];
			String style = params[3];
			try {
				/*ZipResourceFile obbFile = APKExpansionSupport.getAPKExpansionZipFile(getActivity(), 1, 1);
				String xslStr = IOUtil.readRawResource(getActivity(), R.raw.es);
				Log.d("DTCurrYear", year);
				*/
				AssetManager assetMan = getActivity().getAssets();
				InputStream isXml = assetMan.open("es/"+year+"/"+"es_"+locale+".xml");
				InputStream isXsl = assetMan.open("es.xsl");
				String xml = IOUtil.readFileWithIS(isXml, "UTF-8");
				String xsl = IOUtil.readFileWithIS(isXsl, "UTF-8");
				isXml.close();
				isXsl.close();
				Map<String, String> args = new HashMap<String, String>();
				args.put("date", date);
				args.put("style", style);
				html = XSLT.transform(xsl, xml,args,"UTF-8");
			} catch (TransformerException | IOException e) {
				// TODO Auto-generated catch block
				Log.e("XSLT error", e.getMessage());
				html = e.getMessage();
				cancel(true);
			}
			return super.doInBackground(params);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			webV.loadDataWithBaseURL(null,getString(R.string.lookup_failed), "text/html","UTF-8",null);
			super.onCancelled();
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			webV.loadDataWithBaseURL(null,html, "text/html","UTF-8",null);
			super.onPostExecute(result);
		}
	}
}
