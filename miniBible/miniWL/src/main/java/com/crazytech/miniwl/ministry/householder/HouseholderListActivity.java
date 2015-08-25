package com.crazytech.miniwl.ministry.householder;

import java.util.Calendar;

import com.crazytech.android.dialog.ConfirmDialog;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.ministry.Ministry;
import com.crazytech.miniwl.ministry.MinistryFragment;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class HouseholderListActivity extends ListActivity {
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		boolean isBs = getIntent().getBooleanExtra(Householder.Fields.BS, false);
		getListView().setAdapter(new HouseholderAdapter(context, MinistryFragment.getHouseholders(this, isBs)));
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, HouseholderEditActivity.class);
				intent.putExtras(((Householder)parent.getAdapter().getItem(position)).getExtras(context,true));
				startActivity(intent);
			}
		});
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				Runnable okRun = new Runnable() {
					
					@Override
					public void run() {
						try {
							Householder hse = (Householder)parent.getAdapter().getItem(position);
							HouseholderDb hDb = new HouseholderDb(context, hse);
							hDb.delete();
							Toast.makeText(context, getString(R.string.deleted), Toast.LENGTH_LONG).show();
							finish();
						} catch (Exception e) {
							Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_LONG).show();
							Log.e(MainActivity.LOG_TAG, e.getMessage());
						}
					}
				};
				ConfirmDialog confirm = new ConfirmDialog(context, getString(R.string.delete), 
						String.format(getString(R.string.confirm_delete), "?"), 
						okRun, null);
				return true;
			}
		});
	}
}
