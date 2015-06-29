package com.crazytech.miniwl.bible;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.transform.TransformerException;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import wei.mark.standout.views.StandOutSpinner;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;

import com.crazytech.io.IOUtil;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;
import com.crazytech.theocratic.bible.BibleMapping;
import com.crazytech.xslt.XSLT;

public class BibleStandout extends StandOutWindow{
	private String locale;
	private Map<String, String> codeToBook;
	private WebView webV;
	private StandOutSpinner spinnerBook;
	private StandOutSpinner spinnerChap;
	//private StandOutSpinner spinnerVerse;
	private BibleMapping biMap;
	private View view;
	private int id;
	@Override
	public String getAppName() {
		// TODO Auto-generated method stub
		return getString(R.string.float_bible);
	}

	@Override
	public int getAppIcon() {
		// TODO Auto-generated method stub
		return R.drawable.ic_bible;
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_bible_pop, frame, true);
		this.view = view;
		this.id = id;
		initView();
	}
	
	private void initView() {
		setTitle(id, getTitle(id));
		locale = getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0,2);
		webV = (WebView)view.findViewById(R.id.webv_bible_pop);
		webV.setBackgroundColor(Color.TRANSPARENT);
		webV.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		webV.getSettings().setSupportZoom(true);
		webV.getSettings().setBuiltInZoomControls(true);
		webV.getSettings().setLoadWithOverviewMode(true);
		webV.getSettings().setUseWideViewPort(false);
		
		
		Button btnLeft = (Button)view.findViewById(R.id.button_bible_left_pop);
		btnLeft.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_left));
		btnLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flingChapter(true);
			}
		});
		
		Button btnRight = (Button)view.findViewById(R.id.button_bible_right_pop);
		btnRight.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_right));
		btnRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flingChapter(false);
			}
		});	
		
		String[] bookArray = getResources().getStringArray(R.array.bi_bk_array);
		codeToBook = new HashMap<String, String>();
		int i = 1;
		for (String book : bookArray) {
			codeToBook.put("bk_"+i++, book);
		}
		
		biMap = new BibleMapping();
		
		spinnerBook = (StandOutSpinner)view.findViewById(R.id.bi_book_spinner_pop);
		initSpinnerBook(spinnerBook);
		
		spinnerChap = (StandOutSpinner)view.findViewById(R.id.bi_chap_spinner_pop);
		initSpinnerChap(spinnerChap, spinnerBook.getSelectedItem().toString(), biMap);
		
		/*
		spinnerVerse = (StandOutSpinner)view.findViewById(R.id.bi_verse_spinner_pop);
		initSpinnerVerse(spinnerVerse, spinnerBook.getSelectedItem().toString(), 
				spinnerChap.getSelectedItem().toString(), biMap);
		*/
		String book = spinnerBook.getSelectedItem().toString();
		new LoadXSLT(webV).execute(book,"1");
		
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
		return getString(R.string.float_bible);
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
		return getString(R.string.float_bible)+" "+getString(R.string.hidden);
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
	
	
	private void initSpinnerBook(final StandOutSpinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bi_bk_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setPrompt(getResources().getString(R.string.bible));
		spinner.setAdapter(adapter);
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String book = spinner.getAdapter().getItem(which).toString();
				spinner.setSelection(which);
				initSpinnerChap(spinnerChap, book, new BibleMapping());
				String chap = spinnerChap.getSelectedItem().toString();
				new LoadXSLT(webV).execute(book, chap);
			}
		});
	}
	
	private void initSpinnerChap(final StandOutSpinner spinner, String selectedBook, BibleMapping biMap) {
		ArrayAdapter<CharSequence> adapterChap = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterChap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final BibleMapping bibleMap = biMap;
		for (String key : codeToBook.keySet()) {
			if (codeToBook.get(key).equals(selectedBook)) {
				for (int i = 1; i <= bibleMap.getBibleMap().get(key).keySet().size(); i++) {
					adapterChap.add(i+"");
				}
				break;
			}
			
		}
		spinner.setPrompt(getResources().getString(R.string.chapter));
		spinner.setAdapter(adapterChap);
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String book = spinnerBook.getSelectedItem().toString();
				String chap = spinner.getAdapter().getItem(which).toString();
				spinner.setSelection(which);
				new LoadXSLT(webV).execute(book,chap);
				//initSpinnerVerse(spinnerVerse, book, chap, new BibleMapping());
			}
		});
		
	}
	/*
	private void initSpinnerVerse(final StandOutSpinner spinner, String selectedBook,
			String selectedChap, BibleMapping biMap) {
		
		ArrayAdapter<CharSequence> adapterVerse = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapterVerse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final BibleMapping bibleMap = biMap;
		String bookKey = "";
		for (String key : codeToBook.keySet()) {
			if (codeToBook.get(key).equals(selectedBook)) {
				bookKey = key;
				break;
			}
			
		}
		for (int i = 1; i <= bibleMap.getBibleMap().get(bookKey).get(Integer.parseInt(selectedChap)); i++) {
			adapterVerse.add(Integer.toString(i));
		}
		spinner.setPrompt(getResources().getString(R.string.verse));
		spinner.setAdapter(adapterVerse);
		final String book = selectedBook;
		final String chap = selectedChap;
		spinner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				spinner.setSelection(which);
				loadUrl(book, chap, spinner.getAdapter().getItem(which).toString(), bibleMap);
			}
		});
	}
	*/
	
	
	private void flingChapter(boolean isLeft){
		String book = spinnerBook.getSelectedItem().toString();
		String bookKey = "";
		
		for (String key: codeToBook.keySet()) {
			if (codeToBook.get(key).equals(book)) {
				bookKey = key;
				break;
			}
		}
		int chap = Integer.parseInt(spinnerChap.getSelectedItem().toString());
		if (!isLeft
				&& chap + 1 < biMap.getBibleMap().get(bookKey).keySet().size())
			chap = chap + 1;
		else if (chap - 1 >= 1) 
			chap = chap - 1;
		for (int pos = 0; pos < spinnerChap.getCount(); pos++) {
			if (spinnerChap.getItemAtPosition(pos).toString().equals(chap+"")) {
				spinnerChap.setSelection(pos);
				break;
			}
		}
		new LoadXSLT(webV).execute(book,chap+"");
	}
	
	private class LoadXSLT extends AsyncTask<String, String, String>{
		private WebView webV;
		private String html;
		public LoadXSLT(WebView webV) {
			super();
			this.webV = webV;
		}
		
		@Override
		protected void onPreExecute() {
			webV.loadDataWithBaseURL("file:///android_asset/", MainActivity.htmlLoading(getString(R.string.loading),MainActivity.STYLEDAY), "text/html", "UTF-8", null);
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String book = params[0];
			String chap = params[1];
			loadUrl(book, chap, "");
			// TODO Auto-generated method stub
			return null;
		}
		
		private void loadUrl(String selectedBook, String chapter, String verse){
			String bookCode = "";
			for (String key : codeToBook.keySet()) {
				if (codeToBook.get(key).equals(selectedBook)) {
					bookCode = key;
					break;
				}
				
			}
			
			try {
				bookCode=bookCode.split("_")[1];
				AssetManager assetMan = getBaseContext().getAssets();
				InputStream isXml = assetMan.open("bi/"+locale+"/"+locale+"_bi_"+bookCode+".xml");
				InputStream isXsl = assetMan.open("bi.xsl");
				String biXml = IOUtil.readFileWithIS(isXml, "UTF-8");
				String biXsl = IOUtil.readFileWithIS(isXsl, "UTF-8");
				isXml.close();
				isXsl.close();
				Map<String, String> params = new HashMap<String, String>();
				params.put("chap", chapter);
				params.put("verse", verse);
				html = XSLT.transform(biXsl, biXml,params,"UTF-8");
			} catch (TransformerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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

}
