package com.crazytech.miniwl;

import android.content.Context;

public class DeleteAllDialog extends DeleteDialog {

	public DeleteAllDialog(Context context, String title, int iconResId) {
		super(context, title, iconResId);
		getTv().setText(context.getString(R.string.confirm_delete_all));
	}

}
