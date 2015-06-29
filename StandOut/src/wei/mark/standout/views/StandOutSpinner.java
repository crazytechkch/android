package wei.mark.standout.views;

import wei.mark.standout.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class StandOutSpinner extends Spinner {
	private OnItemClickListener itemClickListener;
	private android.content.DialogInterface.OnClickListener onClickListener;
	private String title = "";
	
	public StandOutSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public StandOutSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public StandOutSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean performClick() {
		// TODO Auto-generated method stub
		Dialog dialog = getDialog();
		Window window = dialog.getWindow();
		window.getAttributes().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		dialog.show();
		return true;
	}
	
	private AlertDialog getDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		CharSequence[] items = new CharSequence[getAdapter().getCount()];
		for (int i = 0; i < getAdapter().getCount(); i++) {
			items[i] = getAdapter().getItem(i).toString();
		}
		builder.setItems(items, this.onClickListener);
		builder.setTitle(getPrompt());
		
		
		
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
	public void setOnClickListener(DialogInterface.OnClickListener onClickListener){
		this.onClickListener = onClickListener; 
	}
	
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener l) {
		// TODO Auto-generated method stub
		this.itemClickListener = l;
	}
	
	@Override
	public void setAdapter(SpinnerAdapter adapter) {
		// TODO Auto-generated method stub
		super.setAdapter(adapter);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
