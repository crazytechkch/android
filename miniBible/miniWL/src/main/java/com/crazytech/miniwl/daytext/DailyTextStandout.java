package com.crazytech.miniwl.daytext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.TransformerException;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crazytech.io.IOUtil;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.bible.BibleStandout;
import com.crazytech.xslt.XSLT;

public class DailyTextStandout extends StandOutWindow {
	private String locale1;
	private View view;
	private int id;
	private WebView webV;
	private Button btnToday;
	private ImageButton btnNext,btnPrev,btnDual,btnLocale;
	private Date newDate,nextDate,prevDate;
	private Integer year,month,day;
	
	@Override
	public String getAppName() {
		// TODO Auto-generated method stub
		return getString(R.string.float_daytext);
	}

	@Override
	public int getAppIcon() {
		// TODO Auto-generated method stub
		return R.drawable.ic_daytext;
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.fragment_daytext, frame, true);
		this.view = view;
		this.id = id;
		initView();
	}
	
	private void initView(){
		locale1 = getResources().getConfiguration().locale.toString();
		if (locale1!=null) locale1=locale1.substring(0,2);
		LinearLayout linlay = (LinearLayout)view.findViewById(R.id.linlay_daytext);
		linlay.setBackground(getResources().getDrawable(R.color.white_tr));
		LinearLayout linlayBtn = (LinearLayout)view.findViewById(R.id.linlay_daytext_buttons);
		linlayBtn.setBackground(getResources().getDrawable(R.color.white_tr));
		btnDual = (ImageButton)view.findViewById(R.id.btn_showdual);
		btnDual.setVisibility(View.GONE);
		btnLocale = (ImageButton)view.findViewById(R.id.btn_daytextLocale1);
		btnLocale.setVisibility(View.GONE);
		webV = (WebView)view.findViewById(R.id.webV_daytext_1);
		webV.setBackgroundColor(getResources().getColor(R.color.white_tr));
		webV.getSettings().setSupportZoom(true);
		webV.getSettings().setBuiltInZoomControls(true);
		webV.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		//webV.getSettings().setLoadWithOverviewMode(true);
		webV.getSettings().setUseWideViewPort(false);
		btnToday = (Button)view.findViewById(R.id.btn_daytext_today);
		btnToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initText(new Date(),webV,locale1);
				setNewDate(new Date());
				year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
				month = Integer.parseInt((String)DateFormat.format("MM", newDate));
				day = Integer.parseInt((String)DateFormat.format("dd", newDate));
			}
		});
		btnToday.setTextColor(getResources().getColor(R.color.black));
		btnPrev = (ImageButton)view.findViewById(R.id.btn_daytext_prev);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previousText();
			}
		});
		btnNext = (ImageButton)view.findViewById(R.id.btn_daytext_next);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextText();
			}
		});
		initText(new Date(), webV, locale1);
		setNewDate(new Date());
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
	}
	
	private void initText(Date date, WebView webV, String locale){
		String inYearLong = (String)DateFormat.format("yyyy", date);
		String inMonth = (String)DateFormat.format("MM", date);
		String inDay = (String)DateFormat.format("dd", date);
		String dateStr = inDay+"-"+inMonth+"-"+inYearLong;
		new LoadXSLT(webV).execute(locale,dateStr,inYearLong);
		
	}
	
	private void nextText(){
		nextDate = new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()+1);
		initText(nextDate,webV,locale1);
		setNewDate(nextDate);
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
		
	}
	
	private void previousText(){
		prevDate = new Date(newDate.getYear(), newDate.getMonth(), newDate.getDate()-1);
		initText(prevDate,webV,locale1);
		setNewDate(prevDate);
		year = Integer.parseInt((String)DateFormat.format("yyyy", newDate));
		month = Integer.parseInt((String)DateFormat.format("MM", newDate));
		day = Integer.parseInt((String)DateFormat.format("dd", newDate));
	}
	

	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		// TODO Auto-generated method stub
		return new StandOutLayoutParams(id, 400, 400, 
				StandOutLayoutParams.AUTO_POSITION, 
				StandOutLayoutParams.AUTO_POSITION,
				100, 100);
	}
	
	@Override
	public int getFlags(int id) {
		return StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE
				| StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_WINDOW_HIDE_ENABLE
				| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}
	
	@Override
	public String getPersistentNotificationTitle(int id) {
		// TODO Auto-generated method stub
		return getString(R.string.float_daytext);
	}
	
	@Override
	public String getPersistentNotificationMessage(int id) {
		return getString(R.string.tap_to_close);
	}
	
	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getCloseIntent(this, BibleStandout.class, id);
	}
	
	@Override
	public String getHiddenNotificationTitle(int id) {
		// TODO Auto-generated method stub
		return getString(R.string.float_daytext)+" "+getString(R.string.hidden);
	}
	
	@Override
	public String getHiddenNotificationMessage(int id) {
		// TODO Auto-generated method stub
		return getString(R.string.tap_to_restore);
	}
	
	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}
	
	
	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		// TODO Auto-generated method stub
		List<DropDownListItem> items = new ArrayList<StandOutWindow.DropDownListItem>();
		items.add(localeDropDownListItem(new Locale("ms")));
		items.add(localeDropDownListItem(Locale.GERMAN));
		items.add(localeDropDownListItem(Locale.ENGLISH));
		items.add(localeDropDownListItem(new Locale("es")));
		items.add(localeDropDownListItem(Locale.FRENCH));
		items.add(localeDropDownListItem(Locale.CHINESE));
		items.add(localeDropDownListItem(Locale.JAPANESE));
		items.add(localeDropDownListItem(Locale.KOREAN));
		return items;
	}
	
	private DropDownListItem localeDropDownListItem(final Locale locale) {
		String localeDesc = locale.toString();
		localeDesc = localeDesc.substring(0, 2);
		if (localeDesc.equals("ms")) localeDesc = getString(R.string.locale_ms);
		if (localeDesc.equals("de")) localeDesc = getString(R.string.locale_de);
		if (localeDesc.equals("en")) localeDesc = getString(R.string.locale_en);
		if (localeDesc.equals("es")) localeDesc = getString(R.string.locale_es);
		if (localeDesc.equals("fr")) localeDesc = getString(R.string.locale_fr);
		if (localeDesc.equals("zh")) localeDesc = getString(R.string.locale_zh);
		if (localeDesc.equals("ja")) localeDesc = getString(R.string.locale_ja);
		if (localeDesc.equals("ko")) localeDesc = getString(R.string.locale_ko);
		DropDownListItem item = new DropDownListItem(R.drawable.globe, localeDesc, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Configuration config = getResources().getConfiguration();
				config.locale = new Locale(locale.toString());
				getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
				initView();
			}
		});
		return item;
	}
	
	private class LoadXSLT extends AsyncTask<String, String, String> {
		private WebView webV;
		private String html;
		
		public LoadXSLT(WebView webV) {
			super();
			this.webV = webV;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			webV.loadDataWithBaseURL("file:///android_asset/", MainActivity.htmlLoading(getString(R.string.loading),MainActivity.STYLEDAY), "text/html", "UTF-8", null);
		}
		
		@Override
		protected String doInBackground(String... params) {
			String locale = params[0];
			String date  = params[1];
			String year = params[2];
			try {
				/*ZipResourceFile obbFile = APKExpansionSupport.getAPKExpansionZipFile(getActivity(), 1, 1);
				String xslStr = IOUtil.readRawResource(getActivity(), R.raw.es);
				Log.d("DTCurrYear", year);
				*/
				AssetManager assetMan = getBaseContext().getAssets();
				InputStream isXml = assetMan.open("es/"+year+"/"+"es_"+locale+".xml");
				InputStream isXsl = assetMan.open("es.xsl");
				String xml = IOUtil.readFileWithIS(isXml, "UTF-8");
				String xsl = IOUtil.readFileWithIS(isXsl, "UTF-8");
				isXml.close();
				isXsl.close();
				Map<String, String> args = new HashMap<String, String>();
				args.put("date", date);
				args.put("style", MainActivity.STYLEDAY);
				html = XSLT.transform(xsl, xml,args,"UTF-8");
			} catch (TransformerException | IOException e) {
				// TODO Auto-generated catch block
				Log.e("XSLT error", e.getMessage());
				cancel(true);
			}
			return null;
		}
		
		@Override
		protected void onCancelled() {
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

	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}
}
