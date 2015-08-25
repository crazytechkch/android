package com.crazytech.miniwl.ministry;

import java.text.SimpleDateFormat;

import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class MinistryDb {
	private Context context;
	private Ministry ministry;
	private Long ministryId, visitsId, placementId;
	
	public MinistryDb(Context context, Ministry ministry) {
		super();
		this.ministry = ministry;
		this.context = context;
	}


	protected void insert() throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		ministryId = getNewId("ministry");
		visitsId = getNewId("visits");
		placementId = getNewId("placements");
		db.insertOrThrow("ministry", "", getCvMinistry(false));
		if(ministry.getType()==2||ministry.getType()==3)db.insertOrThrow("visits", "", getCvVisits(false));
		if(ministry.getBooks()>0)db.insertOrThrow("placements", "", getCvPlacements(1, ministry.getBooks(),false));
		if(ministry.getMags()>0)db.insertOrThrow("placements", "", getCvPlacements(2, ministry.getMags(),false));
		if(ministry.getBrocs()>0)db.insertOrThrow("placements", "", getCvPlacements(3, ministry.getBrocs(),false));
		if(ministry.getTracts()>0)db.insertOrThrow("placements", "", getCvPlacements(4, ministry.getTracts(),false));
		pm.close();
	}
	
	protected void update() throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		db.update("ministry", getCvMinistry(true), "id = ?", new String[]{ministry.getId()+""});
		if(pm.rowExists("visits", "ministry_id=?", new String[]{ministry.getId()+""}))
			db.update("visits", getCvVisits(true), "ministry_id = ?", new String[]{ministry.getId()+""});
		else if(ministry.getType()==2||ministry.getType()==3) {
			ContentValues cv = getCvVisits(true);
			cv.put("ministry_id", ministry.getId());
			db.insert("visits" , "", cv);
		}
		updatePlacements(pm, 1);
		updatePlacements(pm, 2);
		updatePlacements(pm, 3);
		updatePlacements(pm, 4);
		pm.close();
	}
	
	protected void deleteMinistry(Long ministryId) throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		db.execSQL("delete from ministry where id = "+ministryId);
		db.execSQL("delete from visits where ministry_id = "+ministryId);
		db.execSQL("delete from placements where ministry_id = "+ministryId);
		pm.close();
	}

	private ContentValues getCvMinistry(boolean isUpdate){
		ContentValues cv = new ContentValues();
		if(!isUpdate)cv.put("id",ministryId);
		cv.put("date", new SimpleDateFormat("yyyy-MM-dd").format(ministry.getCalStart().getTime()));
		cv.put("time_start",ministry.getCalStart().getTimeInMillis());
		cv.put("time_end",ministry.getCalEnd().getTimeInMillis());
		cv.put("type", ministry.getType());
		cv.put("street",ministry.getStreet());
		cv.put("duration", ministry.getDuration());
		return cv;
	}
	
	private ContentValues getCvVisits(boolean isUpdate) {
		ContentValues cv = new ContentValues();
		if(!isUpdate){
			cv.put("id", visitsId);
			cv.put("ministry_id", ministryId);
		}
		if(ministry.getHouseholderId()!=-1L)cv.put("householder_id", ministry.getHouseholderId());
		cv.put("type",ministry.getType());
		cv.put("remarks", ministry.getRemarks());
		return cv;
	}
	
	private void updatePlacements(PersistanceManager pm, int type){
		SQLiteDatabase db = pm.getDb();
		int count = 1;
		switch (type) {
		case 1:count=ministry.getBooks();break;
		case 2:count=ministry.getMags();break;
		case 3:count=ministry.getBrocs();break;
		case 4:count=ministry.getTracts();break;
		default:
			break;
		}
		if(pm.rowExists("placements", "ministry_id=? and type=?", new String[]{ministry.getId()+"",type+""}))
			db.execSQL("update placements set count="+count+" where ministry_id="+ministry.getId()+" and type="+type);
		else {
			ContentValues cv = getCvPlacements(type, count, true);
			cv.put("ministry_id", ministry.getId());
			if (ministry.getType()==2||ministry.getType()==3) {
				Cursor cursor = db.rawQuery("select id from visits where ministry_id="+ministry.getId()+" limit 1", null);
				cursor.moveToFirst();
				Long vId = cursor.getLong(0);
				cv.put("visit_id", vId);
				cursor.close();
			}
			db.insert("placements", "", cv);
		}
	}
	
	private ContentValues getCvPlacements(int type, int count,boolean isUpdate) {
		ContentValues cv = new ContentValues();
		if(!isUpdate){
			cv.put("ministry_id", ministryId);
			if(ministry.getType()==2||ministry.getType()==3)cv.put("visit_id", visitsId);
		}
		if(ministry.getHouseholderId()!=-1L)cv.put("householder_id",ministry.getHouseholderId());
		cv.put("date", new SimpleDateFormat("yyyy-MM-dd").format(ministry.getCalStart().getTime()));
		cv.put("type", type);
		cv.put("count", count);
		return cv;
	}
	
	private Long getNewId(String table) {
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		Long id = 0L;
		try {
			SQLiteDatabase db = pm.getDb();
			Cursor cursor = db.rawQuery("select max(id) id from "+table, null);
			cursor.moveToFirst();
			id = cursor.getLong(cursor.getColumnIndex("id"));
			if(id==null)id=0L;
			cursor.close();
		} catch (SQLException e) {
			Log.e("db error", e.getMessage());
		}
		pm.close();
		return id+=1;
	}
	/*
	private Integer getDuration(){
		Long timeDiff = (ministry.getCalEnd().getTimeInMillis())-(ministry.getCalStart().getTimeInMillis());
		return (timeDiff)>0?Integer.valueOf(timeDiff+""):0;
	}*/
}
