package com.crazytech.miniwl.ministry;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazytech.android.dialog.ConfirmDialog;
import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.ministry.householder.Householder;
import com.crazytech.miniwl.ministry.householder.HouseholderAdapter;
import com.crazytech.miniwl.ministry.householder.HouseholderAdapterSimple;
import com.crazytech.miniwl.ministry.householder.HouseholderDb;
import com.crazytech.miniwl.ministry.householder.HouseholderEditActivity;
import com.crazytech.miniwl.ministry.householder.HouseholderListActivity;

public class MinistryFragment extends Fragment {
	private View rootV;
	private TextView textSumTime;
	private TextView textGoalHours,textGoalBs,textGoalRv,textGoalPubs;
	private TextView textPubs,textPubsBook,textPubsMag,textPubsBroc,textPubsTract;
	private TextView textBs,textBsTime,textBsRv,textBsPubs;
	private TextView textRv,textRvBs,textRvTime,textRvPubs,textRvPend;
	private TextView textWorkBook,textWorkMag,textWorkBroc,textWorkTract;
	private NumberPicker numpBooks,numpMags,numpBrocs,numpTracts;
	private ScrollView scrollContent ;
	private static final int FOCUS_BOOK = 0;
	private static final int FOCUS_MAG = 1;
	private static final int FOCUS_BROC = 2;
	private static final int FOCUS_TRACT = 3;
	private LinearLayout layStart, layWork;
	private Chronometer chronoWork;
	private Long chronoBase = 0L;
	private int chronoState = 1;
	private static final int CHRONO_PAUSED = 0;
	private static final int CHRONO_TICKING = 1;
	private String locale;
	private Ministry monthlyRecord;
	private Calendar cal,calStart;
	private static final String  HEADER_COLOR = "#4a6da7";
	private static final String  BODY_COLOR = "#f28330";
	private static final String PASS_COLOR = "#008000";
	
	private MinistryGoal goal;
	private static final String GOAL_HOURS = "goal_hours";
	private static final String GOAL_BS = "goal_bs";
	private static final String GOAL_RV = "goal_rv";
	private static final String GOAL_BOOKS = "goal_books";
	private static final String GOAL_MAGS = "goal_mags";
	private static final String GOAL_BROCS = "goal_brocs";
	private static final String GOAL_TRACTS = "goal_tracts";
	private static final int CONTEXT_RECENT_EDIT = 0;
	private static final int CONTEXT_RECENT_DELETE = 1;
	//private int bottomRecent,bottomGoal,bottomBs,bottomRv,bottomPubs;
	
	private ListView listRecentMinistries,listHouseholderBs,listHouseholderRv;
	
	private Ministry ministry;
	
	private static final int MINISTRY_EDIT = 0;
	public static final String INIT_VIEW = "init";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootV = inflater.inflate(R.layout.fragment_ministry, container, false);
		initView(Calendar.getInstance());
		return rootV;
	}
	
	private void initView(Calendar cal){
		scrollContent = (ScrollView)rootV.findViewById(R.id.scroll_content);
		locale = getActivity().getBaseContext().getResources().getConfiguration().locale.toString();
		if (locale!=null) locale = locale.substring(0, 2);
		monthlyRecord = getMonthlyRecord(new SimpleDateFormat("yyyy-MM").format(cal.getTime()));
		this.cal =cal;
		initDate(cal);
		initSummary();
		initRecentActivities(cal);
		initPlacements();
		initBs();
		initRv();
		initGoal();
		layStart = (LinearLayout)rootV.findViewById(R.id.layout_work_start);
		initWorking();
		layStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				calStart = Calendar.getInstance();
				layStart.setVisibility(View.GONE);
				layWork.setVisibility(View.VISIBLE);
				chronoBase = 0L;
				chronoWork.setBase(SystemClock.elapsedRealtime());
				chronoWork.start();
			}
		});
	}
	
	private void initDate(final Calendar cal) {
		LinearLayout layout = (LinearLayout)rootV.findViewById(R.id.layout_date);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeDate(cal);
			}
		});
		layout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				initView(Calendar.getInstance());
				return true;
			}
		});
		TextView textDate = (TextView)rootV.findViewById(R.id.textv_ms_date);
		textDate.setText(new SimpleDateFormat("yyyy MMMM", new Locale(locale)).format(cal.getTime()));
		final Calendar currCal = cal;
		Button btnPrev = (Button)rootV.findViewById(R.id.btn_prevMonth);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currCal.add(Calendar.MONTH, -1);
				initView(currCal);
			}
		});
		Button btnNext = (Button)rootV.findViewById(R.id.btn_nextMonth);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currCal.add(Calendar.MONTH, 1);
				initView(currCal);
			}
		});
	}
	
	private void initSummary() {
		textSumTime = (TextView)rootV.findViewById(R.id.textv_ms_sum_time);
		Long duration = monthlyRecord.getDuration();
		Long hour = duration/3600000;
		Long minute = (duration%3600000)/60000;
		String time = String.format(getString(R.string.hours_mins), hour, minute);
		setSummary(time);
	}
	
	private void setSummary(String time){
		textSumTime.setText(spannedStrRes(R.string.ms_sum_time, HEADER_COLOR, time));
		View view = rootV.findViewById(R.id.ipt_summary);
		TextView textvBookCount = (TextView)view.findViewById(R.id.text_books);
		TextView textvMagCount = (TextView)view.findViewById(R.id.text_mags);
		TextView textvBrocCount = (TextView)view.findViewById(R.id.text_brochures);
		TextView textvTractCount = (TextView)view.findViewById(R.id.text_tract);
		TextView textvRvCount = (TextView)view.findViewById(R.id.textV_visits);
		TextView textvBsCount = (TextView)view.findViewById(R.id.textV_studies);
		textvBookCount.setText(spanned(HEADER_COLOR, monthlyRecord.getBooks()+""));
		textvMagCount.setText(spanned(HEADER_COLOR, monthlyRecord.getMags()+""));
		textvBrocCount.setText(spanned(HEADER_COLOR, monthlyRecord.getBrocs()+""));
		textvTractCount.setText(spanned(HEADER_COLOR, monthlyRecord.getTracts()+""));
		textvRvCount.setText(spanned(HEADER_COLOR, monthlyRecord.getRv()+""));
		textvBsCount.setText(spanned(HEADER_COLOR, monthlyRecord.getBsDistinct()+""));
	}
	
	private void initRecentActivities(final Calendar cal) {
		TextView textHeader = (TextView)rootV.findViewById(R.id.text_header_recent);
		View layBody = rootV.findViewById(R.id.lay_body_recent);
		textHeader.setOnClickListener(getBodyVisibilityListener(layBody));
		View listContainer = rootV.findViewById(R.id.inc_list_recent);
		listRecentMinistries = (ListView)listContainer.findViewById(R.id.list);
		MinistryListAdapter adapter = new MinistryListAdapter(getMinistries(getActivity(),new SimpleDateFormat("yyyy-MM").format(cal.getTime()), 5), getActivity());
		listRecentMinistries.setAdapter(adapter);
		LayoutParams layParams = listRecentMinistries.getLayoutParams();
		listRecentMinistries.setOnTouchListener(new ListView.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
	        }
	    });
		if(listRecentMinistries.getAdapter().getCount()>2) layParams.height = 400;
		else layParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
		listRecentMinistries.setLayoutParams(layParams);
		listRecentMinistries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Ministry currMinistry = (Ministry)parent.getAdapter().getItem(position);
				startEditActivity(currMinistry,true);
			}
		});
		registerForContextMenu(listRecentMinistries);
		
		View view = rootV.findViewById(R.id.inc_more_add_recent);
		Button btnAdd = (Button)view.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startEditActivity(new Ministry(),false);
			}
		});
		Button btnMore = (Button)view.findViewById(R.id.btn_more);
		btnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MinistryListActivity.class);
				intent.putExtra("cal", cal.getTimeInMillis());
				startActivityForResult(intent,MINISTRY_EDIT);
			}
		});
		
		ImageButton btnUp = (ImageButton)listContainer.findViewById(R.id.btn_up);
		btnUp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				listRecentMinistries.smoothScrollBy(-50, 500);
				return false;
			}
		});
		btnUp.setOnLongClickListener(openMinistryList(btnMore));
		ImageButton btnDown = (ImageButton)listContainer.findViewById(R.id.btn_down);
		btnDown.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				listRecentMinistries.smoothScrollBy(50, 500);
				return false;
			}
		});
		btnDown.setOnLongClickListener(openMinistryList(btnMore));
		Button listBtnAdd = (Button)listContainer.findViewById(R.id.btn_add);
		listBtnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startEditActivity(new Ministry(),false);
			}
		});
		
		
	}
	
	private void initWorking() {
		ministry = new Ministry();
		layWork = (LinearLayout)rootV.findViewById(R.id.layout_work);
		chronoWork = (Chronometer)rootV.findViewById(R.id.chrono_work);
		View view = rootV.findViewById(R.id.inc_work_sum);
		LinearLayout layBooks = (LinearLayout)view.findViewById(R.id.lay_book);
		LinearLayout layMags = (LinearLayout)view.findViewById(R.id.lay_mag);
		LinearLayout layBrocs = (LinearLayout)view.findViewById(R.id.lay_broc);
		LinearLayout layTracts = (LinearLayout)view.findViewById(R.id.lay_tract);
		layBooks.setOnClickListener(workingLayoutClickListener(FOCUS_BOOK));
		layMags.setOnClickListener(workingLayoutClickListener(FOCUS_MAG));
		layBrocs.setOnClickListener(workingLayoutClickListener(FOCUS_BROC));
		layTracts.setOnClickListener(workingLayoutClickListener(FOCUS_TRACT));
		textWorkBook = (TextView)view.findViewById(R.id.text_books);
		textWorkMag = (TextView)view.findViewById(R.id.text_mags);
		textWorkBroc = (TextView)view.findViewById(R.id.text_brochures);
		textWorkTract = (TextView)view.findViewById(R.id.text_tract);
		Button btnWorkEnd = (Button)rootV.findViewById(R.id.btn_work_end);
		btnWorkEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Long elapsedTime = SystemClock.elapsedRealtime() - chronoWork.getBase();
				ministry.setCalStart(calStart);
				ministry.setCalEnd(Calendar.getInstance());
				ministry.setDuration(elapsedTime);
				ministry.setBooks(Integer.valueOf(textWorkBook.getText()+""));
				ministry.setMags(Integer.valueOf(textWorkMag.getText()+""));
				ministry.setBrocs(Integer.valueOf(textWorkBroc.getText()+""));
				ministry.setTracts(Integer.valueOf(textWorkTract.getText()+""));
				startEditActivity(ministry,false);
				
			}
		});
		final Button btnWorkPause = (Button)rootV.findViewById(R.id.btn_work_pause);
		btnWorkPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (chronoState) {
				case CHRONO_TICKING:
					chronoBase = chronoWork.getBase() - SystemClock.elapsedRealtime();
					chronoWork.stop();
					btnWorkPause.setText(getString(R.string.resume));
					chronoState = CHRONO_PAUSED;
					break;
				case CHRONO_PAUSED:
					chronoWork.setBase(chronoBase + SystemClock.elapsedRealtime());
					chronoWork.start();
					btnWorkPause.setText(getString(R.string.pause));
					chronoState = CHRONO_TICKING;
				default:
					break;
				}
			}
		});
		Button btnCancel = (Button)rootV.findViewById(R.id.btn_work_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getString(R.string.my_ministry));
				builder.setMessage(String.format(getString(R.string.confirm_cancel), " "+getString(R.string.preaching)+"?"));
				builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetWorkingPubs();
					}
				});
				builder.setNegativeButton(getString(R.string.nope), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		resetWorkingPubs();
	}
	
	private void setWorkingPubs(int book, int mag, int broc, int tract) {
		ministry.setBooks(book);
		ministry.setMags(mag);
		ministry.setBrocs(broc);
		ministry.setTracts(tract);
		textWorkBook.setText(spanned(HEADER_COLOR, book+""));
		textWorkMag.setText(spanned(HEADER_COLOR, mag+""));
		textWorkBroc.setText(spanned(HEADER_COLOR, broc+""));
		textWorkTract.setText(spanned(HEADER_COLOR, tract+""));
	}
	
	private void resetWorkingPubs(){
		chronoWork.stop();
		layStart.setVisibility(View.VISIBLE);
		layWork.setVisibility(View.GONE);
		ministry = new Ministry();
		setWorkingPubs(0, 0, 0, 0);
	}
	
	private OnClickListener workingLayoutClickListener(final int focus){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = addPlacementDialog(focus);
				Button btnOk = (Button)dialog.findViewById(R.id.btn_ok);
				btnOk.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						setWorkingPubs(numpBooks.getValue(), numpMags.getValue(), numpBrocs.getValue(), numpTracts.getValue());
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		};
	}

	private Dialog addPlacementDialog(int focus) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(getString(R.string.placement));
		dialog.setContentView(R.layout.layout_pubs_nump);
		numpBooks = (NumberPicker)dialog.findViewById(R.id.nump_books);
		numpBooks.setMinValue(0);
		numpBooks.setMaxValue(999);
		numpBooks.setValue(ministry.getBooks());
		numpMags = (NumberPicker)dialog.findViewById(R.id.nump_mags);
		numpMags.setValue(0);
		numpMags.setMaxValue(999);
		numpMags.setValue(ministry.getMags());
		numpBrocs = (NumberPicker)dialog.findViewById(R.id.nump_brocs);
		numpBrocs.setValue(0);
		numpBrocs.setMaxValue(999);
		numpBrocs.setValue(ministry.getBrocs());
		numpTracts = (NumberPicker)dialog.findViewById(R.id.nump_tracts);
		numpTracts.setValue(0);
		numpTracts.setMaxValue(999);
		numpTracts.setValue(ministry.getTracts());
		switch (focus) {
		case FOCUS_BOOK:
			numpBooks.requestFocus();
			break;
		case FOCUS_MAG:
			numpMags.requestFocus();
			break;
		case FOCUS_BROC:
			numpBrocs.requestFocus();
			break;
		case FOCUS_TRACT:
			numpTracts.requestFocus();
			break;
		default:
			break;
		}
		
		Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	private void initPlacements() {
		TextView textHeader = (TextView)rootV.findViewById(R.id.textv_ms_pubs);
		View layBody = rootV.findViewById(R.id.lay_body_pubs);
		textHeader.setOnClickListener(getBodyVisibilityListener(layBody));
		textPubs = (TextView)rootV.findViewById(R.id.textv_ms_pubs);
		textPubsBook = (TextView)rootV.findViewById(R.id.textv_ms_pubs_book);
		textPubsMag = (TextView)rootV.findViewById(R.id.textv_ms_pubs_mag);
		textPubsBroc = (TextView)rootV.findViewById(R.id.textv_ms_pubs_broc);
		textPubsTract = (TextView)rootV.findViewById(R.id.textv_ms_pubs_tract);
		
		int bookCount = monthlyRecord.getBooks();
		int magCount = monthlyRecord.getMags();
		int tractCount = monthlyRecord.getTracts();
		int brocCount = monthlyRecord.getBrocs();
		int pubsCount = bookCount+magCount+tractCount+brocCount;
		setPlacements(pubsCount, bookCount, magCount, brocCount, tractCount);
	}
	
	private void setPlacements(int pubs, int book, int mag, int broc, int tract) {
		textPubs.setText(spannedStrRes(R.string.ms_c_pubs, HEADER_COLOR, pubs+""));
		textPubsBook.setText(spanned(HEADER_COLOR, book+""));
		textPubsMag.setText(spanned(HEADER_COLOR, mag+""));
		textPubsBroc.setText(spanned(HEADER_COLOR, broc+""));
		textPubsTract.setText(spanned(HEADER_COLOR, tract+""));
	}
	
	private void initBs() {
		TextView textHeader = (TextView)rootV.findViewById(R.id.textv_ms_bs);
		View layBody = rootV.findViewById(R.id.lay_body_bs);
		textHeader.setOnClickListener(getBodyVisibilityListener(layBody));
		textBs = (TextView)rootV.findViewById(R.id.textv_ms_bs);
		textBsTime = (TextView)rootV.findViewById(R.id.textv_ms_bs_time);
		textBsRv = (TextView)rootV.findViewById(R.id.textv_ms_bs_rv);
		textBsPubs = (TextView)rootV.findViewById(R.id.textv_ms_bs_pubs);
		View listContainer = rootV.findViewById(R.id.inc_list_bs);
		listHouseholderBs = (ListView)listContainer.findViewById(R.id.list);
		HouseholderAdapterSimple adapterBs = new HouseholderAdapterSimple(getActivity(), getHouseholders(getActivity(), true));
		listHouseholderBs.setAdapter(adapterBs);
		listHouseholderBs.setOnTouchListener(new ListView.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
		listHouseholderBs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Householder hse = (Householder)parent.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), HouseholderEditActivity.class);
				intent.putExtras(hse.getExtras(getActivity(),true));
				startActivityForResult(intent, MINISTRY_EDIT);
			}
		});
		listHouseholderBs.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				Runnable okRun = new Runnable() {
					
					@Override
					public void run() {
						try {
							Householder hse = (Householder)parent.getAdapter().getItem(position);
							HouseholderDb hDb = new HouseholderDb(getActivity(), hse);
							hDb.delete();
							initView(Calendar.getInstance());
							Toast.makeText(getActivity(), getString(R.string.deleted), Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
							Log.e(MainActivity.LOG_TAG, e.getMessage());
						}
					}
				};
				ConfirmDialog confirm = new ConfirmDialog(getActivity(), getString(R.string.delete), 
						String.format(getString(R.string.confirm_delete), "?"), 
						okRun, null);
				return true;
			}
		});
		
		View view = rootV.findViewById(R.id.inc_more_add_bs);
		Button btnAdd = (Button)view.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Ministry currMinistry = new Ministry();
				currMinistry.setType(3);
				startEditActivity(currMinistry,false);
			}
		});
		Button btnMore = (Button)view.findViewById(R.id.btn_more);
		btnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MinistryListActivityBs.class);
				intent.putExtra("cal", cal.getTimeInMillis());
				startActivityForResult(intent,MINISTRY_EDIT);
			}
		});
		
		ImageButton btnUp = (ImageButton)listContainer.findViewById(R.id.btn_up);
		btnUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listHouseholderBs.smoothScrollBy(-50, 500);
			}
		});
		btnUp.setOnLongClickListener(openHouseholderList(true));
		ImageButton btnDown = (ImageButton)listContainer.findViewById(R.id.btn_down);
		btnDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listHouseholderBs.smoothScrollBy(50, 500);
			}
		});
		btnDown.setOnLongClickListener(openHouseholderList(true));
		Button btnAddHouseholder = (Button)listContainer.findViewById(R.id.btn_add);
		btnAddHouseholder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Householder hse = new Householder();
				hse.setBiStudent(1);
				Intent intent = new Intent(getActivity(), HouseholderEditActivity.class);
				intent.putExtras(hse.getExtras(getActivity(),false));
				startActivityForResult(intent, MINISTRY_EDIT);;
			}
		});
		
		setBs(monthlyRecord.getBsDistinct(),monthlyRecord.getBs(), monthlyRecord.getDurationBs(),monthlyRecord.getPubsBs());
	}
	
	private void setBs(int bsDistinct,int bs, Long duration,int pubs) {
		textBs.setText(spannedStrRes(R.string.ms_bs, HEADER_COLOR, bsDistinct+""));
		Long hour = duration/3600000;
		Long minute = (duration%3600000)/60000;
		String time = String.format(getString(R.string.hours_mins), hour,minute);
		textBsTime.setText(spannedStrRes(R.string.ms_c_time, HEADER_COLOR, time));
		textBsRv.setText(spannedStrRes(R.string.ms_c_rv, HEADER_COLOR, bs+""));
		textBsPubs.setText(spannedStrRes(R.string.ms_c_pubs, HEADER_COLOR, pubs+""));
	}
	
	private void initRv() {
		TextView textHeader = (TextView)rootV.findViewById(R.id.textv_ms_rv);
		View layBody = rootV.findViewById(R.id.lay_body_rv);
		textHeader.setOnClickListener(getBodyVisibilityListener(layBody));
		textRv = (TextView)rootV.findViewById(R.id.textv_ms_rv);
		textRvTime = (TextView)rootV.findViewById(R.id.textv_ms_rv_time);
		textRvPubs = (TextView)rootV.findViewById(R.id.textv_ms_rv_pubs);
		textRvBs = (TextView)rootV.findViewById(R.id.textv_ms_rv_bs);
		textRvPend = (TextView)rootV.findViewById(R.id.textv_ms_rv_pending);
		
		View listContainer = rootV.findViewById(R.id.inc_list_rv);
		listHouseholderRv = (ListView)listContainer.findViewById(R.id.list);
		HouseholderAdapterSimple adapterRv = new HouseholderAdapterSimple(getActivity(), getHouseholders(getActivity(), false));
		listHouseholderRv.setAdapter(adapterRv);
		listHouseholderRv.setOnTouchListener(new ListView.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
		listHouseholderRv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), HouseholderEditActivity.class);
				intent.putExtras(((Householder)parent.getAdapter().getItem(position)).getExtras(getActivity(),true));
				startActivityForResult(intent, MINISTRY_EDIT);
			}
		});
		listHouseholderRv.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(final AdapterView<?> parent, View view,
					final int position, long id) {
				Runnable okRun = new Runnable() {
					
					@Override
					public void run() {
						try {
							Householder hse = (Householder)parent.getAdapter().getItem(position);
							HouseholderDb hDb = new HouseholderDb(getActivity(), hse);
							hDb.delete();
							initView(Calendar.getInstance());
							Toast.makeText(getActivity(), getString(R.string.deleted), Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
							Log.e(MainActivity.LOG_TAG, e.getMessage());
						}
					}
				};
				ConfirmDialog confirm = new ConfirmDialog(getActivity(), getString(R.string.delete), 
						String.format(getString(R.string.confirm_delete), "?"), 
						okRun, null);
				return true;
			}
		});
		
		View view = rootV.findViewById(R.id.inc_more_add_rv);
		Button btnAdd = (Button)view.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Ministry currMinistry = new Ministry();
				currMinistry.setType(2);
				startEditActivity(currMinistry,false);
			}
		});
		Button btnMore = (Button)view.findViewById(R.id.btn_more);
		btnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MinistryListActivityRv.class);
				intent.putExtra("cal", cal.getTimeInMillis());
				startActivityForResult(intent,MINISTRY_EDIT);
			}
		});
		
		ImageButton btnUp = (ImageButton)listContainer.findViewById(R.id.btn_up);
		btnUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listHouseholderRv.smoothScrollBy(-50, 500);
			}
		});
		btnUp.setOnLongClickListener(openHouseholderList(false));
		ImageButton btnDown = (ImageButton)listContainer.findViewById(R.id.btn_down);
		btnDown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listHouseholderRv.smoothScrollBy(50, 500);
			}
		});
		btnDown.setOnLongClickListener(openHouseholderList(false));
		Button btnAddHouseholder = (Button)listContainer.findViewById(R.id.btn_add);
		btnAddHouseholder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), HouseholderEditActivity.class);
				intent.putExtras(new Householder().getExtras(getActivity(),false));
				startActivityForResult(intent, MINISTRY_EDIT);
			}
		});
		
		
		setRv(monthlyRecord.getRv(),monthlyRecord.getDurationRv(),monthlyRecord.getPubsRv(),
				monthlyRecord.getBs(),getNextVisitCount(getActivity()));
	}
	
	private void setRv(int rv,Long duration, int pubs, int bs, int pending){
		textRv.setText(spannedStrRes(R.string.ms_rv, HEADER_COLOR, rv+""));
		Long hour = duration/3600000;
		Long minute = (duration%3600000)/60000;
		String time = String.format(getString(R.string.hours_mins), hour,minute);
		textRvTime.setText(spannedStrRes(R.string.ms_c_time, HEADER_COLOR, time));
		textRvPubs.setText(spannedStrRes(R.string.ms_c_pubs, HEADER_COLOR, pubs+""));
		textRvBs.setText(spannedStrRes(R.string.ms_areBs, HEADER_COLOR, bs+""));
		textRvPend.setText(spannedStrRes(R.string.ms_pending, pending>0?"red":PASS_COLOR, pending+""));
	}
	
	private void initGoal() {
		TextView textHeader = (TextView)rootV.findViewById(R.id.text_header_goals);
		View layout = rootV.findViewById(R.id.lay_body_goals);
		textHeader.setOnClickListener(getBodyVisibilityListener(layout));
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeGoal();
			}
		});
		
		View incGoal = rootV.findViewById(R.id.inc_pubs_goal);
		View mssumRv = incGoal.findViewById(R.id.mssum_rv);
		mssumRv.setVisibility(View.GONE);
		View mssumBs = incGoal.findViewById(R.id.mssum_bs);
		mssumBs.setVisibility(View.GONE);
		
		textGoalHours = (TextView)rootV.findViewById(R.id.textv_ms_goal_hours);
		textGoalBs = (TextView)rootV.findViewById(R.id.textv_ms_goal_bs);
		textGoalRv = (TextView)rootV.findViewById(R.id.textv_ms_goal_rv);
		textGoalPubs= (TextView)rootV.findViewById(R.id.textv_ms_goal_pubs);
		
		
		SharedPreferences prefs = getGoalSharedPref();
		int goalHours = prefs.getInt(GOAL_HOURS, 0);
		int goalBs = prefs.getInt(GOAL_BS, 0);
		int goalRv = prefs.getInt(GOAL_RV, 0);
		int goalBooks = prefs.getInt(GOAL_BOOKS, 0);
		int goalMags = prefs.getInt(GOAL_MAGS, 0);
		int goalBrocs = prefs.getInt(GOAL_BROCS, 0);
		int goalTracts = prefs.getInt(GOAL_TRACTS, 0);
		goal = new MinistryGoal(goalHours, goalBs, goalRv, goalBooks, goalMags, goalBrocs, goalTracts);
		setGoal();
	}
	
	private void setGoal() {
		int aHours = Integer.parseInt(""+monthlyRecord.getDuration()/3600000);
		int aBs = monthlyRecord.getBsDistinct();
		int aRv = monthlyRecord.getRv();
		int aPubs = monthlyRecord.getBooks()+monthlyRecord.getMags()+monthlyRecord.getBrocs()+monthlyRecord.getTracts();
		int hours = goal.getHours();
		int bs = goal.getBs();
		int rv = goal.getRv();
		int books = goal.getBooks();
		int mags = goal.getMags();
		int brocs = goal.getBrocs();
		int tracts = goal.getTracts();
		int pubs = books+mags+brocs+tracts;
		textGoalHours.setText(Html.fromHtml(String.format(getString(R.string.ms_goal_hours), "<font color='"+HEADER_COLOR+"'><b>"+hours+"</b></font>", "<font color='"+(aHours>=hours?PASS_COLOR:BODY_COLOR)+"'><b>"+aHours+"</b></font>")));
		textGoalBs.setText(Html.fromHtml(String.format(getString(R.string.ms_goal_bs), "<font color='"+HEADER_COLOR+"'><b>"+bs+"</b></font>", "<font color='"+(aBs>=bs?PASS_COLOR:BODY_COLOR)+"'><b>"+aBs+"</b></font>")));
		textGoalRv.setText(Html.fromHtml(String.format(getString(R.string.ms_goal_rv), "<font color='"+HEADER_COLOR+"'><b>"+rv+"</b></font>", "<font color='"+(aRv>=rv?PASS_COLOR:BODY_COLOR)+"'><b>"+aRv+"</b></font>")));
		textGoalPubs.setText(Html.fromHtml(String.format(getString(R.string.ms_goal_pubs), "<font color='"+HEADER_COLOR+"'><b>"+pubs+"</b></font>", "<font color='"+(aPubs>=pubs?PASS_COLOR:BODY_COLOR)+"'><b>"+aPubs+"</b></font>")));
		View incGoal = rootV.findViewById(R.id.inc_pubs_goal);
		TextView textvBooks = (TextView)incGoal.findViewById(R.id.text_books);
		TextView textvMags = (TextView)incGoal.findViewById(R.id.text_mags);
		TextView textvBrocs = (TextView)incGoal.findViewById(R.id.text_brochures);
		TextView textvTracts = (TextView)incGoal.findViewById(R.id.text_tract);
		int aBooks = monthlyRecord.getBooks();
		int aMags = monthlyRecord.getMags();
		int aBrocs = monthlyRecord.getBrocs();
		int aTracts = monthlyRecord.getTracts();
		textvBooks.setText(Html.fromHtml(String.format("%1$s(%2$s)", "<font color='"+HEADER_COLOR+"'><b>"+books+"</b></font>", "<font color='"+(aBooks>=books?PASS_COLOR:BODY_COLOR)+"'><b>"+aBooks+"</b></font>")));
		textvMags.setText(Html.fromHtml(String.format("%1$s(%2$s)", "<font color='"+HEADER_COLOR+"'><b>"+mags+"</b></font>", "<font color='"+(aMags>=mags?PASS_COLOR:BODY_COLOR)+"'><b>"+aMags+"</b></font>")));
		textvBrocs.setText(Html.fromHtml(String.format("%1$s(%2$s)", "<font color='"+HEADER_COLOR+"'><b>"+brocs+"</b></font>", "<font color='"+(aBrocs>=brocs?PASS_COLOR:BODY_COLOR)+"'><b>"+aBrocs+"</b></font>")));
		textvTracts.setText(Html.fromHtml(String.format("%1$s(%2$s)", "<font color='"+HEADER_COLOR+"'><b>"+tracts+"</b></font>", "<font color='"+(aTracts>=tracts?PASS_COLOR:BODY_COLOR)+"'><b>"+aTracts+"</b></font>")));
	}
	
	private OnLongClickListener openMinistryList(final Button btn) {
		return new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				btn.performClick();
				return true;
			}
		};
	}
	
	private OnLongClickListener openHouseholderList(final boolean isBs) {
		return new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(getActivity(), HouseholderListActivity.class);
				intent.putExtra(Householder.Fields.BS, isBs);
				startActivity(intent);
				return true;
			}
		};
	}
	
 	private SharedPreferences getGoalSharedPref() {
		return getActivity().getSharedPreferences(MinistryFragment.class.getSimpleName(), Context.MODE_PRIVATE);
	}
	
	private void saveGoalSharedPref(int hours, int bs, int rv, int books, int mags, int brocs, int tracts) {
		SharedPreferences prefs = getGoalSharedPref();
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(GOAL_HOURS, hours);
		editor.putInt(GOAL_BS, bs);
		editor.putInt(GOAL_RV, rv);
		editor.putInt(GOAL_BOOKS, books);
		editor.putInt(GOAL_MAGS, mags);
		editor.putInt(GOAL_BROCS, brocs);
		editor.putInt(GOAL_TRACTS, tracts);
		editor.commit();
	}
	
	public static List<Ministry> getMinistries(Context context, String reqDate, int limit) {
		List<Ministry> ministries = new ArrayList<Ministry>();
		PersistanceManager pm = new PersistanceManager(context);
		String sql = "select distinct m.id id,m.date date,m.type type,m.street street,m.duration duration, h.name name, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=1) books, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=2) mags, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=3) brocs, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=4) tracts, "+
				"m.time_start tstart,m.time_end tend,v.remarks "+
				"from ministry m "+
				"left join visits v on m.id = v.ministry_id "+
				"left join placements p on m.id = p.ministry_id "+
				"left join householders h on v.householder_id = h.id "+
				"where m.date like '"+reqDate+"%' order by m.date desc limit "+limit;
		pm.open();
		SQLiteDatabase db = pm.getDb();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Ministry currMinistry = new Ministry();
				currMinistry.setId(cursor.getLong(0));
				currMinistry.setDate(cursor.getString(1));
				currMinistry.setType(cursor.getInt(2));
				currMinistry.setStreet(cursor.getString(3));
				currMinistry.setDuration(cursor.getLong(4));
				currMinistry.setName(cursor.getString(5));
				currMinistry.setBooks(cursor.getInt(6));
				currMinistry.setMags(cursor.getInt(7));
				currMinistry.setBrocs(cursor.getInt(8));
				currMinistry.setTracts(cursor.getInt(9));
				Calendar currCalStart = Calendar.getInstance();
				currCalStart.setTimeInMillis(cursor.getLong(10));
				Calendar currCalEnd = Calendar.getInstance();
				currCalEnd.setTimeInMillis(cursor.getLong(11));
				currMinistry.setCalStart(currCalStart);
				currMinistry.setCalEnd(currCalEnd);
				currMinistry.setRemarks(cursor.getString(12));
				ministries.add(currMinistry);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {
			pm.close();
		}
		return ministries;
	}
	
	public static List<Ministry> getTypeBasedMinistries(Context context, String reqDate, int type,int limit) {
		List<Ministry> ministries = new ArrayList<Ministry>();
		PersistanceManager pm = new PersistanceManager(context);
		String typeStr = type==2?"(m.type=2 or m.type=3)":"m.type=3";
		String sql = "select distinct m.id id,m.date date,m.type type,m.street street,m.duration duration, h.name name, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=1) books, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=2) mags, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=3) brocs, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=4) tracts, "+
				"m.time_start tstart,m.time_end tend,v.remarks "+
				"from ministry m "+
				"left join visits v on m.id = v.ministry_id "+
				"left join placements p on m.id = p.ministry_id "+
				"left join householders h on v.householder_id = h.id "+
				"where m.date like '"+reqDate+"%' and "+typeStr+" order by m.date desc limit "+limit;
		pm.open();
		SQLiteDatabase db = pm.getDb();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Ministry currMinistry = new Ministry();
				currMinistry.setId(cursor.getLong(0));
				currMinistry.setDate(cursor.getString(1));
				currMinistry.setType(cursor.getInt(2));
				currMinistry.setStreet(cursor.getString(3));
				currMinistry.setDuration(cursor.getLong(4));
				currMinistry.setName(cursor.getString(5));
				currMinistry.setBooks(cursor.getInt(6));
				currMinistry.setMags(cursor.getInt(7));
				currMinistry.setBrocs(cursor.getInt(8));
				currMinistry.setTracts(cursor.getInt(9));
				Calendar currCalStart = Calendar.getInstance();
				currCalStart.setTimeInMillis(cursor.getLong(10));
				Calendar currCalEnd = Calendar.getInstance();
				currCalEnd.setTimeInMillis(cursor.getLong(11));
				currMinistry.setCalStart(currCalStart);
				currMinistry.setCalEnd(currCalEnd);
				currMinistry.setRemarks(cursor.getString(12));
				ministries.add(currMinistry);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {
			pm.close();
		}
		return ministries;
	}
	
	public static List<Ministry> getHouseholdersMinistries(Context context, Long hseId) {
		List<Ministry> ministries = new ArrayList<Ministry>();
		PersistanceManager pm = new PersistanceManager(context);
		String sql = "select distinct m.id id,m.date date,m.type type,m.street street,m.duration duration, h.name name, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=1) books, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=2) mags, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=3) brocs, "+
				"(select p.count from placements p where p.ministry_id = m.id and p.type=4) tracts, "+
				"m.time_start tstart,m.time_end tend,v.remarks "+
				"from ministry m "+
				"left join visits v on m.id = v.ministry_id "+
				"left join placements p on m.id = p.ministry_id "+
				"left join householders h on v.householder_id = h.id "+
				"where h.id = "+hseId+" order by m.date desc";
		pm.open();
		SQLiteDatabase db = pm.getDb();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Ministry currMinistry = new Ministry();
				currMinistry.setId(cursor.getLong(0));
				currMinistry.setDate(cursor.getString(1));
				currMinistry.setType(cursor.getInt(2));
				currMinistry.setStreet(cursor.getString(3));
				currMinistry.setDuration(cursor.getLong(4));
				currMinistry.setName(cursor.getString(5));
				currMinistry.setBooks(cursor.getInt(6));
				currMinistry.setMags(cursor.getInt(7));
				currMinistry.setBrocs(cursor.getInt(8));
				currMinistry.setTracts(cursor.getInt(9));
				Calendar currCalStart = Calendar.getInstance();
				currCalStart.setTimeInMillis(cursor.getLong(10));
				Calendar currCalEnd = Calendar.getInstance();
				currCalEnd.setTimeInMillis(cursor.getLong(11));
				currMinistry.setCalStart(currCalStart);
				currMinistry.setCalEnd(currCalEnd);
				currMinistry.setRemarks(cursor.getString(12));
				ministries.add(currMinistry);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {
			pm.close();
		}
		return ministries;
	}
	
	private Ministry getMonthlyRecord(String reqDate) {
		Ministry currMinistry = new Ministry();
		PersistanceManager pm = new PersistanceManager(getActivity());
		String sql = "select m.id id, m.date date,sum(distinct m.duration) duration,sum(distinct case when m.type=2 or m.type=3 then m.duration end) duration_rv, "+
				"sum(distinct case when m.type=3 then m.duration end) duration_bs, "+
				"sum(case when (m.type=2 or m.type=3) and (p.type=1 or p.type=2 or p.type=3 or p.type=4) then p.count end) pubs_rv, "+
				"sum(case when m.type=3 and (p.type=1 or p.type=2 or p.type=3 or p.type=4) then p.count end) pubs_bs, "+
				"count(distinct case when m.type = 2 or m.type=3 then m.id end) rv, count(distinct case when m.type = 3 then m.id end) bs "+
				"from ministry m "+
				"left join visits v on m.id = v.ministry_id "+
				"left join placements p on m.id = p.ministry_id "+
				"where m.date like '"+reqDate+"%' limit 1";
		String sqlPubs = "select sum(case when type = 1 then count end) books, "+
				"sum(case when type = 1 then count end) books,"+
				"sum(case when type = 2 then count end) mags,"+
				"sum(case when type = 3 then count end) brocs,"+
				"sum(case when type = 4 then count end) tracts "+
				"from placements where date like '"+reqDate+"%' limit 1";
		String sqlBsDistinct = "select distinct v.householder_id from visits v "+
				"left join ministry m on v.ministry_id = m.id "+
				"where m.date like '"+reqDate+"%' and v.type = 3 "+
				"group by v.householder_id;";
		pm.open();
		try {
			SQLiteDatabase db = pm.getDb();
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			Cursor cursorPub = db.rawQuery(sqlPubs, null);
			cursorPub.moveToFirst();
			Cursor cursorBs = db.rawQuery(sqlBsDistinct, null);
			currMinistry.setId(cursor.getLong(cursor.getColumnIndex("id")));
			currMinistry.setDate(cursor.getString(cursor.getColumnIndex("date")));
			currMinistry.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
			currMinistry.setDurationRv(cursor.getLong(cursor.getColumnIndex("duration_rv")));
			currMinistry.setDurationBs(cursor.getLong(cursor.getColumnIndex("duration_bs")));
			currMinistry.setBooks(cursorPub.getInt(cursorPub.getColumnIndex("books")));
			currMinistry.setMags(cursorPub.getInt(cursorPub.getColumnIndex("mags")));
			currMinistry.setTracts(cursorPub.getInt(cursorPub.getColumnIndex("tracts")));
			currMinistry.setBrocs(cursorPub.getInt(cursorPub.getColumnIndex("brocs")));
			currMinistry.setPubsRv(cursor.getInt(cursor.getColumnIndex("pubs_rv")));
			currMinistry.setPubsBs(cursor.getInt(cursor.getColumnIndex("pubs_bs")));
			currMinistry.setBs(cursor.getInt(cursor.getColumnIndex("bs")));
			currMinistry.setRv(cursor.getInt(cursor.getColumnIndex("rv")));
			currMinistry.setBsDistinct(cursorBs.getCount());
			cursor.close();
			cursorPub.close();
			cursorBs.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {pm.close();}
		return currMinistry;
	}
	
	public static List<Householder> getHouseholders(Context context, boolean isBs) {
		List<Householder> householders = new ArrayList<Householder>();
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		String sql = "select * from householders "
				+(isBs?"where bible_student=1 ":" ")
				+ "order by next_visit asc";
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Householder hse = new Householder(
						cursor.getLong(cursor.getColumnIndex("id")), 
						cursor.getLong(cursor.getColumnIndex("ministry_id")), 
						cursor.getString(cursor.getColumnIndex("name")), 
						cursor.getString(cursor.getColumnIndex("agegroup")), 
						cursor.getString(cursor.getColumnIndex("gender")), 
						cursor.getString(cursor.getColumnIndex("language")),
						cursor.getLong(cursor.getColumnIndex("next_visit")),
						cursor.getString(cursor.getColumnIndex("remarks")), 
						cursor.getString(cursor.getColumnIndex("phone_main")), 
						cursor.getString(cursor.getColumnIndex("phone_home")), 
						cursor.getString(cursor.getColumnIndex("email")), 
						cursor.getString(cursor.getColumnIndex("street")), 
						cursor.getInt(cursor.getColumnIndex("bible_student")), 
						BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("map_lat"))), 
						BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex("map_long"))));
				householders.add(hse);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {
			pm.close();
		}
		return householders;
	}
	
	public static int getNextVisitCount(Context context) {
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		String sql = "select count() from householders where next_visit>"+Calendar.getInstance().getTimeInMillis();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			cursor.close();
			return count;
		} catch (SQLException e) {
			Log.e(MainActivity.LOG_TAG, e.getMessage());
		} finally {
			pm.close();
		}
		return 0;
	}
	
	private void changeDate(Calendar cal) {
		final DatePickerDialog dpd = new DatePickerDialog(getActivity(), null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		final Calendar currCal = cal;
		dpd.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DatePicker date = dpd.getDatePicker();
				currCal.set(date.getYear(), date.getMonth(), date.getDayOfMonth());
				initView(currCal);
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
	
	private void changeGoal() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.layout_edit_goal);
		dialog.setTitle(R.string.ms_goal);
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_briefcase);
		final EditText editHours = (EditText)dialog.findViewById(R.id.edit_hours);
		final EditText editBs = (EditText)dialog.findViewById(R.id.edit_bs);
		final EditText editRv= (EditText)dialog.findViewById(R.id.edit_rv);
		final EditText editBooks = (EditText)dialog.findViewById(R.id.edit_books);
		final EditText editMags = (EditText)dialog.findViewById(R.id.edit_mags);
		final EditText editBrocs = (EditText)dialog.findViewById(R.id.edit_brocs);
		final EditText editTracts = (EditText)dialog.findViewById(R.id.edit_tracts);
		editHours.setText(goal.getHours()+"");
		editBs.setText(goal.getBs()+"");
		editRv.setText(goal.getRv()+"");
		editBooks.setText(goal.getBooks()+"");
		editMags.setText(goal.getMags()+"");
		editBrocs.setText(goal.getBrocs()+"");
		editTracts.setText(goal.getTracts()+"");
		Button btnOk = (Button)dialog.findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Integer hours = Integer.valueOf(editHours.getText().toString());
				Integer bs = Integer.valueOf(editBs.getText().toString());
				Integer rv = Integer.valueOf(editRv.getText().toString());
				Integer books = Integer.valueOf(editBooks.getText().toString());
				Integer mags = Integer.valueOf(editMags.getText().toString());
				Integer brocs = Integer.valueOf(editBrocs.getText().toString());
				Integer tracts = Integer.valueOf(editTracts.getText().toString());
				goal = new MinistryGoal(hours, bs, rv, books, mags, brocs, tracts);
				setGoal();
				saveGoalSharedPref(hours, bs, rv, books, mags, brocs, tracts);
				dialog.dismiss();
			}
		});
		Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void startEditActivity(Ministry minis, boolean isEdit){
		Intent editIntent = new Intent(getActivity(), MinistryEditActivity.class);
		editIntent.putExtras(minis.getExtras(isEdit));
		startActivityForResult(editIntent,MINISTRY_EDIT);
	}
	
	private OnClickListener getBodyVisibilityListener(final View view){
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (view.getVisibility()) {
				case View.VISIBLE:
					view.setVisibility(View.GONE);
					break;

				default:
					view.setVisibility(View.VISIBLE);
					
				}
			}
		};
	}
	
	private Spanned spanned(String color, String... strParams){
		String text = "<font color='"+color+"'><b>"+strParams[0]+"</b></font>";
		return Html.fromHtml(text);
	}
	
	private Spanned spannedStrRes(int resId, String color, String... strParams){
		String text = String.format(getString(resId), "<font color='"+color+"'><b>"+strParams[0]+"</b></font>");
		return Html.fromHtml(text);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		switch (v.getId()) {
		case R.id.list:
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    menu.setHeaderTitle(getString(R.string.my_ministry));
		    menu.add(Menu.FIRST,CONTEXT_RECENT_EDIT, Menu.NONE, getString(R.string.edit));
		    menu.add(Menu.FIRST,CONTEXT_RECENT_DELETE, Menu.NONE, getString(R.string.delete));
			break;

		default:
			break;
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_RECENT_EDIT:
			Ministry currMinistry = (Ministry)listRecentMinistries.getAdapter().getItem(info.position);
			startEditActivity(currMinistry, true);
			break;
		case CONTEXT_RECENT_DELETE:
			Runnable okRun = new Runnable() {
				
				@Override
				public void run() {
					try {
						Long id = listRecentMinistries.getAdapter().getItemId(info.position);
						MinistryDb mDb = new MinistryDb(getActivity(), ministry);
						mDb.deleteMinistry(id);
						initView(Calendar.getInstance());
						Toast.makeText(getActivity(), getString(R.string.deleted), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
						Log.e(MainActivity.LOG_TAG, e.getMessage());
					}
				}
			};
			ConfirmDialog confirm = new ConfirmDialog(getActivity(), getString(R.string.delete), 
					String.format(getString(R.string.confirm_delete), "?"), 
					okRun, null);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case MINISTRY_EDIT:
			if (resultCode == Activity.RESULT_OK) {
				if (data.getBooleanExtra(INIT_VIEW, false)) initView(Calendar.getInstance());
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
