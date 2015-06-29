package com.crazytech.miniwl.bible;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.crazytech.io.IOUtil;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.MyAsyncTask;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.TaskCallbacks;
import com.crazytech.theocratic.bible.BibleMapping;
import com.crazytech.util.LocaleUtil;
import com.crazytech.xslt.XSLT;

public class BibleFragment extends Fragment {
	private View rootV;
	private Map<String, String> _codeToBook;
//	private Map<String, Integer> _bookToChap;
	private Map<String, Map<Integer, Integer>> _bibleMap;
	private WebView webV1,webV2;
	private Spinner spinnerBook,spinnerChap,spinnerVerse;
	private FrameLayout fl2;
	private ArrayAdapter<CharSequence> adapterChap,adapterVerse;
	private ImageButton btnDualview;
	private Button btnGo;
	private String locale,locale2,html;
	private boolean checkingBookmark, isNightMode;
	
	private Menu menu;
	private TaskCallbacks taskCallback;
	
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.fragment_bible, container, false);
		initActivity();
		return rootV;
	}
	
	private void initActivity() {
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		locale2=locale;
		
		adapterChap = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item);
		adapterChap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		adapterVerse = new ArrayAdapter<CharSequence>(BibleActivity.this, android.R.layout.simple_spinner_item);
//		adapterVerse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		BibleMapping biMap = new BibleMapping();
		_bibleMap = biMap.getBibleMap();
		initCodeToBook();
		
//		spinnerVerse = (Spinner)rootV.findViewById(R.id.bi_verse_spinner);
//		spinnerVerse.setAdapter(adapterVerse); 
		spinnerChap = (Spinner)rootV.findViewById(R.id.bi_chap_spinner);
		spinnerChap.setAdapter(adapterChap);
		initSpinnerBook();
		spinnerChap.setOnItemSelectedListener(spinnerChapListener());
		//spinnerChap.setOnItemSelectedListener(initChapSelectedListener());
		fl2 = (FrameLayout)rootV.findViewById(R.id.bible_frameLayout_locale2);
		webV1 = (WebView)rootV.findViewById(R.id.webv_bible);
		webV1.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		webV1.getSettings().setSupportZoom(true);
		webV1.getSettings().setBuiltInZoomControls(true);
		//webV1.getSettings().setUseWideViewPort(true);
		webV1.getSettings().setLoadWithOverviewMode(true);
		
		webV2 = (WebView)rootV.findViewById(R.id.webv_bible2);
		webV2.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		webV2.getSettings().setSupportZoom(true);
		webV2.getSettings().setBuiltInZoomControls(true);
		webV2.getSettings().setLoadWithOverviewMode(true);
//		webV2.getSettings().setUseWideViewPort(false);
		
		
		ImageButton btnLeft = (ImageButton)rootV.findViewById(R.id.button_bible_left);
		btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flingChapter(true);
			}
		});
		
		ImageButton btnRight = (ImageButton)rootV.findViewById(R.id.button_bible_right);
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flingChapter(false);
			}
		});
		initButtonChangeLang((ImageButton)rootV.findViewById(R.id.bible_button_bibleLocale1),1);
		initButtonChangeLang((ImageButton)rootV.findViewById(R.id.bible_button_bibleLocale2),2);
		initBtnDualview();
		btnGo = (Button)rootV.findViewById(R.id.bible_btn_go);
		btnGo.setOnClickListener(goClickListener());
		btnGo.performClick();
	}
	
	
	private void initCodeToBook(){
		_codeToBook = new HashMap<String, String>();
		String[] bookArray = getResources().getStringArray(R.array.bi_bk_array);
		int i = 1;
		for (String book : bookArray) {
			_codeToBook.put("bk_"+i++, book);
		}
	}

	private void initSpinnerBook(){
		spinnerBook = (Spinner)rootV.findViewById(R.id.bi_book_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.bi_bk_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBook.setAdapter(adapter);
		spinnerBook.setOnItemSelectedListener(spinnerBookListener());
		spinnerBook.setSelection(0);
	}
	
	private OnItemSelectedListener spinnerBookListener() {
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				if (!checkingBookmark) {
					ArrayAdapter<CharSequence> adap = (ArrayAdapter<CharSequence>) spinnerChap.getAdapter();
					adap.clear();
					String selectedBook = parent.getItemAtPosition(pos).toString();
					String bookKey = getBookKey(selectedBook);
					for (int i = 1; i <= _bibleMap.get(bookKey).keySet().size(); i++) {
						adap.add(Integer.toString(i));
					}
					if(spinnerChap.getSelectedItemPosition()==0)btnGo.callOnClick();
					spinnerChap.setSelection(0);
					//loadUrl(bookKey, spinnerChap.getSelectedItem()!=null?spinnerChap.getSelectedItem().toString():"1", "", locale);
				}
					
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	private OnItemSelectedListener spinnerChapListener(){
		return new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				btnGo.callOnClick();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
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
	
	private void initBtnDualview() {
		btnDualview = (ImageButton)rootV.findViewById(R.id.bible_button_bilang);
		btnDualview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDualView();
			}
		});
	}
	
	private String getBookKey(String book) {
		String bookKey = "";
		for (String key : _codeToBook.keySet()) {
			if (_codeToBook.get(key).equals(book)) {
				bookKey = key;
				break;
			}
		}
		return bookKey;
	}
	
	
	
	/*private OnItemSelectedListener initChapSelectedListener(){
		OnItemSelectedListener chapListener = new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,
					long id) {
				if (!checkingBookmark) {
					Spinner spinnerBook = (Spinner)rootV.findViewById(R.id.bi_book_spinner);
					String selectedBook = spinnerBook.getSelectedItem().toString();
					String bookKey = getBookKey(selectedBook);
					loadUrl(bookKey, parent.getItemAtPosition(pos).toString(),"1",
							locale2);
					
					String selectedText = selectedBook+" "+parent.getItemAtPosition(pos);
					setTextviewBibleTop(selectedText);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		return chapListener;
	}*/
	
	private OnClickListener goClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initText(webV1, locale);
				if(isWebV2Visible())initText(webV2, locale2);
			}
		};
	}
	
	private void initText(WebView webV, String locale) {
		String bookKey = getBookKey(spinnerBook.getSelectedItem().toString());
		String chapter = spinnerChap.getSelectedItem()!=null?spinnerChap.getSelectedItem().toString():"1";
		new LoadBible(taskCallback,webV).execute(bookKey,chapter,"",locale);
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
					locale = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
					initText(webV1, locale);
					break;
				case 2:
					locale2 = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
					initText(webV2, locale2);
					break;
				default:
					break;
				}
			}
		});
		builder.create().show();
    }
    
    private boolean isWebV2Visible() {
    	return fl2.getVisibility()==View.VISIBLE;
    }
    
    private void showDualView() {
    	if (!isWebV2Visible()) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		builder.setTitle(R.string.language);
    		final CharSequence[] items = getResources().getStringArray(R.array.applang);
    		builder.setIcon(getResources().getDrawable(R.drawable.globe));
    		builder.setItems(items, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				locale2 = new LocaleUtil(getActivity()).getLocaleMap().get(items[which]);
    				initText(webV2, locale2);
    			}
    		});
    		builder.create().show();
			fl2.setVisibility(View.VISIBLE);
			btnDualview.setImageDrawable(getResources().getDrawable(R.drawable.langsingle));
		} else {
			fl2.setVisibility(View.GONE);
			btnDualview.setImageDrawable(getResources().getDrawable(R.drawable.langdual));
		}
    }
    
    private void changeDisplayMode(MenuItem item) {
    	isNightMode = !isNightMode;
    	initText(webV1,locale);
		if (isWebV2Visible()) initText(webV2, locale2);
		LinearLayout linlay = (LinearLayout)rootV.findViewById(R.id.linlay_bible);
		if (isNightMode) {
			item.setTitle(getString(R.string.day_mode));
			linlay.setBackgroundColor(getResources().getColor(android.R.color.black));
		}
		else {
			item.setTitle(getString(R.string.night_mode));
			linlay.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	initActivity();
    	if (isWebV2Visible()) fl2.setVisibility(View.VISIBLE);
    	else fl2.setVisibility(View.GONE);
    }
    
    
    private void checkBookmark() {
    	checkingBookmark = true;
    	SharedPreferences _sharedPref = getActivity().getSharedPreferences(BibleFragment.class.getSimpleName(), Activity.MODE_PRIVATE);
		String favBibleBook = _sharedPref.getString("favBibleBook",null);
		String favBibleChap = _sharedPref.getString("favBibleChap",null);
		if (favBibleBook!=null&&favBibleChap!=null) {
			
			ArrayAdapter<CharSequence> bookAdapter = (ArrayAdapter<CharSequence>)spinnerBook.getAdapter();
			spinnerBook.setSelection(bookAdapter.getPosition(_codeToBook.get(favBibleBook)));
			
			ArrayAdapter<CharSequence> chapAdapter = (ArrayAdapter<CharSequence>)spinnerChap.getAdapter();
			spinnerChap.setSelection(chapAdapter.getPosition(favBibleChap));
			//loadUrl(getBookKey(spinnerBook.getSelectedItem().toString()), spinnerChap.getSelectedItem().toString(), "", locale);
			String bookKey = getBookKey(spinnerBook.getSelectedItem().toString());
			String selectedChapter = spinnerChap.getSelectedItem().toString();
			new LoadBible(taskCallback,webV1).execute(bookKey,selectedChapter,"",locale);
			if (isWebV2Visible()) new LoadBible(taskCallback,webV2).execute(bookKey,selectedChapter,"",locale2);
		}
		checkingBookmark = false;
    }
    /*
    private void setBookmark() {
    	SharedPreferences _sharedPref = getActivity().getSharedPreferences(BibleFragment.class.getSimpleName(), Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = _sharedPref.edit();
		editor.putString("favBibleBook", getBookKey(spinnerBook.getSelectedItem().toString()));
		editor.putString("favBibleChap", spinnerChap.getSelectedItem().toString());
		editor.commit();
		Toast.makeText(getActivity(), BibleFragment.this.getString(R.string.bookmark_added)+" "+spinnerBook.getSelectedItem().toString()+" "+spinnerChap.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
    }
    */
    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
    
    private void flingChapter(boolean isLeft){
		String book = spinnerBook.getSelectedItem().toString();
		String bookKey = "";
		for (String key: _codeToBook.keySet()) {
			if (_codeToBook.get(key).equals(book)) {
				bookKey = key;
				break;
			}
		}
		int chap = Integer.parseInt(spinnerChap.getSelectedItem().toString());
		if (!isLeft
				&& chap + 1 < _bibleMap.get(bookKey).keySet().size())
			chap = chap + 1;
		else if (chap - 1 >= 1) 
			chap = chap - 1;
		for (int pos = 0; pos < spinnerChap.getCount(); pos++) {
			if (spinnerChap.getItemAtPosition(pos).toString().equals(chap+"")) {
				spinnerChap.setSelection(pos);
				break;
			}
		}
		//loadUrl(bookKey, chap+"","", locale2);
		new LoadBible(taskCallback,webV1).execute(bookKey,chap+"","",locale);
		if (isWebV2Visible()) new LoadBible(taskCallback,webV2).execute(bookKey,chap+"","",locale2);
	}

	public GestureDetector getgD() {
		GestureDetector.SimpleOnGestureListener gd = new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if (Math.abs(e2.getY()-e1.getY())<50&&Math.abs(velocityX)>200) {
					if (velocityX>0) flingChapter(true);
					else flingChapter(false);
					
				}
				return true;
			}
		};
		return new GestureDetector(getActivity(), gd);
	}
	
	private String getDisplayStyle() {
		return isNightMode?MainActivity.STYLENIGHT:MainActivity.STYLEDAY;
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
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	inflater.inflate(R.menu.menu_daytext, menu);
    	menu.findItem(R.id.changedate).setVisible(false);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.displaymode:
			changeDisplayMode(item);
			break;
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
    
    private class CustomWebViewClient extends WebViewClient {
    	 boolean runPending = false;

    	    // Other code here
    	@Override
    	public void onScaleChanged(final WebView view, float oldScale, float newScale) {
    		if (runPending) return;
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.evaluateJavascript("recalcWidth();", null);
                    runPending = false;
                }
            }, 100);
    	}
    }
	
    private class LoadBible extends MyAsyncTask {
    	private WebView webV;
    	private String html;
    	
    	public LoadBible(TaskCallbacks taskCallback, WebView webV) {
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
			loadUrl(params[0], params[1], params[2], params[3]);
			return super.doInBackground(params);
		}
		
		private void loadUrl(String bookCode, String chapter, 
				String verse, String locale){
			try {
				bookCode=bookCode.split("_")[1];
				AssetManager assetMan = getActivity().getAssets();
				InputStream isXml = assetMan.open("bi/"+locale+"/"+locale+"_bi_"+bookCode+".xml");
				InputStream isXsl = assetMan.open("bi.xsl");
				String biXml = IOUtil.readFileWithIS(isXml, "UTF-8");
				String biXsl = IOUtil.readFileWithIS(isXsl, "UTF-8");
				isXml.close();
				isXsl.close();
				Map<String, String> params = new HashMap<String, String>();
				params.put("chap", chapter);
				params.put("verse", verse);
				params.put("style", getDisplayStyle());
				html = XSLT.transform(biXsl, biXml,params,"UTF-8");

			} catch (TransformerException | IOException e) {
				// TODO Auto-generated catch block
				Log.e("BiXML", e.getMessage());
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
