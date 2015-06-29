package co.crazytech.android.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.crazytech.commons.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatedContainerActivity extends Activity {
	private Calendar cal;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dated_container);
		context = this;
		init(Calendar.getInstance());
	}
	
	public void init(Calendar cal) {
		this.cal = cal;
		LinearLayout layout = (LinearLayout)findViewById(R.id.layout_date);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DatePickerDialog dpd = new DatePickerDialog(context, null, 
						getCal().get(Calendar.YEAR), getCal().get(Calendar.MONTH), getCal().get(Calendar.DAY_OF_MONTH));
				final Calendar currCal = getCal();
				dpd.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DatePicker date = dpd.getDatePicker();
						currCal.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
						init(currCal);
						onNewDateConfirmed();
					}
				});
				dpd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dpd.dismiss();
					}
				});
				dpd.show();
			}
		});
		layout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				onDateLongClick();
				return true;
			}
		});
		TextView textDate = (TextView)findViewById(R.id.textv_ms_date);
		textDate.setText(new SimpleDateFormat("yyyy MM").format(cal.getTime()));
		Button btnPrev = (Button)findViewById(R.id.btn_prevMonth);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onPrevClick();
			}
		});
		Button btnNext = (Button)findViewById(R.id.btn_nextMonth);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onNextClick();
			}
		});
	}
	
	public void onNewDateConfirmed() {
		
	}
	
	public void onDateLongClick(){
		cal = Calendar.getInstance();
		init(cal);
	}
	
	public void onPrevClick(){
		cal.add(Calendar.MONTH, -1);
		init(cal);
	}
	
	public void onNextClick(){
		cal.add(Calendar.MONTH, 1);
		init(cal);
	}

	public Calendar getCal() {
		return cal;
	}

	public void setCal(Calendar cal) {
		this.cal = cal;
	}
}
