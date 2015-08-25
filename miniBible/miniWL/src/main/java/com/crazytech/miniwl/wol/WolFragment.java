package com.crazytech.miniwl.wol;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crazytech.android.network.Network;
import com.crazytech.miniwl.LoadingDialog;
import com.crazytech.miniwl.R;

public class WolFragment extends Fragment {
	private View rootV;
	private WebView webV;
	Bundle bundle;
	private String locale;
	private Network network;
	private LoadingDialog loadDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.fragment_wol, container,false);
		bundle = savedInstanceState;
		network = new Network(getActivity());
		initActivity();
		return rootV;
	}
	
	private void initActivity() {
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		
		webV = (WebView)rootV.findViewById(R.id.webv_wol);
		webV.getSettings().setSupportZoom(true);
		webV.getSettings().setBuiltInZoomControls(true);
		loadDialog = new LoadingDialog(getActivity(), getString(R.string.wol));
		webV.setWebViewClient(new WebViewClient(){
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
//				pD.show();
				//webV.loadDataWithBaseURL("file:///android_asset/", MainActivity.htmlLoading(getString(R.string.loading),MainActivity.STYLEDAY), "text/html", "UTF-8", url);
				loadDialog.show();
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
//				if (pD.isShowing()) {
//					pD.dismiss();
//				}
				loadDialog.dismiss();
				super.onPageFinished(view, url);
			}
			
		});
		if (network.isNetworkAvailable()) {
			loadPage();
		} else {
			new Network(getActivity()).showNetworkDialog();
		}
	}
	
	private void loadPage(){
/*    	pD = new ProgressDialog(WolActivity.this);
    	pD.setTitle(getString(R.string.wol));
    	pD.setIcon(getResources().getDrawable(R.drawable.tab_wol));
    	pD.setMessage(getResources().getString(R.string.load_wol));
    	pD.setIndeterminate(false);
    	pD.setCancelable(true);
    	pD.show();*/
    	webV.loadUrl("http://m.wol.jw.org/"+locale);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
}
