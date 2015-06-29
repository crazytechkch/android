package com.crazytech.miniwl;

import android.app.Dialog;
import android.content.Context;
import android.webkit.WebView;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context, String title) {
		super(context);
		setContentView(R.layout.dialog_loading);
		String loading = context.getString(R.string.loading).toUpperCase();
		setTitle(title);
		WebView wv = (WebView)findViewById(R.id.webview_loading);
		String html = MainActivity.htmlLoading(loading, MainActivity.STYLEDAY);
		wv.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
		// TODO Auto-generated constructor stub
	}

}
