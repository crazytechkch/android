package com.crazytech.miniwl.ministry.householder;

import com.crazytech.miniwl.PersistanceManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HouseholderDb {
	private Context context;
	private Householder householder;
	private Long id;
	
	public HouseholderDb(Context context, Householder householder) {
		super();
		this.context = context;
		this.householder = householder;
	}
	
	public void insert() throws SQLException {
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		db.insertOrThrow("householders", "", getContentValues(false));
		pm.close();
	}
	
	public void update() throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		db.update("householders", getContentValues(true), "id=?", new String[]{householder.getId()+""});
		pm.close();
	}
	
	public void delete() throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		db.execSQL("delete from householders where id="+householder.getId());
		pm.close();
	}
	
	private ContentValues getContentValues(boolean isUpdate){
		ContentValues cv = new ContentValues();
		cv.put(Householder.Fields.ID, householder.getId());
		if(householder.getMinistryId()!=-1)cv.put(Householder.Fields.MINIS_ID, householder.getMinistryId());
		cv.put(Householder.Fields.NAME, householder.getName());
		cv.put(Householder.Fields.AGE, householder.getAge());
		cv.put(Householder.Fields.GENDER, householder.getGender());
		cv.put(Householder.Fields.LANG, householder.getLanguage());
		cv.put(Householder.Fields.NEXT_VISIT, householder.getNextVisit());
		cv.put(Householder.Fields.REMARKS, householder.getRemarks());
		cv.put(Householder.Fields.PHONE1, householder.getPhoneMain());
		cv.put(Householder.Fields.PHONE2, householder.getPhoneHome());
		cv.put(Householder.Fields.EMAIL, householder.getEmail());
		cv.put(Householder.Fields.ADDRESS, householder.getStreet());
		cv.put(Householder.Fields.BS, householder.getBiStudent());
		cv.put(Householder.Fields.LAT, householder.getMapLat().doubleValue());
		cv.put(Householder.Fields.LONG, householder.getMapLong().doubleValue());
		return cv;
	}
}
