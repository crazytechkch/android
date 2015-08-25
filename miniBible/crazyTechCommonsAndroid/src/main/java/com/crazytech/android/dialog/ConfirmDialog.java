package com.crazytech.android.dialog;

import com.crazytech.commons.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmDialog extends AlertDialog {
	private Context context;
	
	public ConfirmDialog(final Context context, String title, String message,
			final Runnable okRun, final Runnable cancelRun) {
		super(context);
		this.context = context;
		setTitle(title);
		setMessage(message);
		setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				okRun.run();
				dismiss();
			}
		});
		setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(cancelRun!=null)cancelRun.run();
				dismiss();
			}
		});
		show();
	}

}
