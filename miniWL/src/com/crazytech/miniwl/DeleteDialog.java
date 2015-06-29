package com.crazytech.miniwl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DeleteDialog extends Dialog {
	private Button btnCancel, btnConfirm;
	private TextView tv;
	private int iconResId;
	public DeleteDialog(Context context, String title, int iconResId) {
		super(context);
		this.iconResId = iconResId;
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.dialog_delete);
		setTitle(title);
		tv = (TextView)findViewById(R.id.deld_tv);
		btnCancel = (Button)findViewById(R.id.deld_btn_cancel);
		btnConfirm = (Button)findViewById(R.id.deld_btn_confirm);
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	public Button getBtnCancel() {
		return btnCancel;
	}
	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}
	public Button getBtnConfirm() {
		return btnConfirm;
	}
	public void setBtnConfirm(Button btnConfirm) {
		this.btnConfirm = btnConfirm;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, iconResId);
	}
	public TextView getTv() {
		return tv;
	}
	public void setTv(TextView tv) {
		this.tv = tv;
	}

}
