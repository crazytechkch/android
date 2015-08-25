package com.crazytech.util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.crazytech.commons.R;

public class LocaleUtil {
	private Map<String, String> localeMap;
	
	public LocaleUtil(Activity activity) {
		super();
		localeMap = new HashMap<String, String>();
		localeMap.put(activity.getString(R.string.locale_fr), "fr");
    	localeMap.put(activity.getString(R.string.locale_es), "es");
    	localeMap.put(activity.getString(R.string.locale_de), "de");
    	localeMap.put(activity.getString(R.string.locale_ms), "ms");
    	localeMap.put(activity.getString(R.string.locale_en), "en");
    	localeMap.put(activity.getString(R.string.locale_ja), "ja");
    	localeMap.put(activity.getString(R.string.locale_zh), "zh");
    	localeMap.put(activity.getString(R.string.locale_ko), "ko");
		// TODO Auto-generated constructor stub
	}

	public Map<String, String> getLocaleMap() {
		return localeMap;
	}

	public void setLocaleMap(Map<String, String> localeMap) {
		this.localeMap = localeMap;
	}
	
	
}
