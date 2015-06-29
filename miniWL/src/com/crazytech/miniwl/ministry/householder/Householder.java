package com.crazytech.miniwl.ministry.householder;

import java.math.BigDecimal;
import java.text.DateFormat.Field;
import java.util.Calendar;

import com.crazytech.miniwl.MainActivity;
import com.crazytech.miniwl.PersistanceManager;
import com.crazytech.miniwl.R;
import com.crazytech.miniwl.ministry.Ministry;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Householder {
	private Long id, ministryId,nextVisit;
	private String name, age, gender, symbol, language, country, 
		remarks, houseNo, phoneMain, phoneHome, email, toDiscuss
		,street;
	private Integer biStudent=0;
	private BigDecimal mapLat, mapLong;
	
	
	
	public Householder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Householder(Long id, Long ministryId, String name, String age,
			String gender, String language,
			Long nextVisit,
			String remarks, String phoneMain, String phoneHome,
			String email, String street, Integer biStudent,
			BigDecimal mapLat, BigDecimal mapLong) {
		super();
		this.id = id;
		this.ministryId = ministryId;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.language = language;
		this.nextVisit = nextVisit;
		this.remarks = remarks;
		this.phoneMain = phoneMain;
		this.phoneHome = phoneHome;
		this.email = email;
		this.street = street;
		this.biStudent = biStudent;
		this.mapLat = mapLat;
		this.mapLong = mapLong;
	}

	public Householder(Long id, String name, String gender, String houseNo,
			String street, Integer biStudent, BigDecimal mapLat, BigDecimal mapLong) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.houseNo = houseNo;
		this.street = street;
		this.biStudent = biStudent;
		this.mapLat = mapLat;
		this.mapLong = mapLong;
	}
	
	public Householder(Long id, String name, String gender, String houseNo,
			String street, Integer biStudent) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.houseNo = houseNo;
		this.street = street;
		this.biStudent = biStudent;
	}
	
	public Householder(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public Bundle getExtras(Context context, boolean isEdit) {
		Bundle bundle = new Bundle();
		if(isEdit){bundle.putLong(Fields.ID, getId());
		bundle.putLong(Fields.MINIS_ID, getMinistryId());
		}else {
			try {
				bundle.putLong(Fields.ID, getNewId(context));
			} catch (SQLException e) {
				Log.e(MainActivity.LOG_TAG, e.getMessage());
				Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_LONG).show();
			}
			
		}
		bundle.putBoolean(Ministry.IS_EDIT, isEdit);
		bundle.putString(Fields.NAME, getName());
		bundle.putString(Fields.AGE, getAge());
		bundle.putString(Fields.GENDER, getGender());
		bundle.putBoolean(Fields.BS, getBiStudent()==1?true:false);
		bundle.putString(Fields.LANG, getLanguage());
		bundle.putString(Fields.REMARKS, getRemarks());
		bundle.putString(Fields.ADDRESS, getStreet());
		bundle.putString(Fields.PHONE1, getPhoneMain());
		bundle.putString(Fields.PHONE2, getPhoneHome());
		bundle.putString(Fields.EMAIL, getEmail());
		bundle.putLong(Fields.NEXT_VISIT, getNextVisit());
		bundle.putDouble(Fields.LAT, getMapLat().doubleValue());
		bundle.putDouble(Fields.LONG, getMapLong().doubleValue());
		return bundle;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getNewId(Context context)throws SQLException{
		PersistanceManager pm = new PersistanceManager(context);
		pm.open();
		SQLiteDatabase db = pm.getDb();
		Cursor cursor = db.rawQuery("select max(id) from householders", null);
		cursor.moveToFirst();
		Long maxId = cursor.getLong(0);
		cursor.close();
		pm.close();
		return maxId+1;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMinistryId() {
		return ministryId!=null?ministryId:-1;
	}
	public void setMinistryId(Long ministryId) {
		this.ministryId = ministryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age!=null?age:"0+";
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender!=null?gender:"m";
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getPhoneMain() {
		return phoneMain;
	}
	public void setPhoneMain(String phoneMain) {
		this.phoneMain = phoneMain;
	}
	public String getPhoneHome() {
		return phoneHome;
	}
	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToDiscuss() {
		return toDiscuss;
	}
	public void setToDiscuss(String toDiscuss) {
		this.toDiscuss = toDiscuss;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public BigDecimal getMapLat() {
		return mapLat!=null?mapLat:BigDecimal.valueOf(0);
	}
	public void setMapLat(BigDecimal mapLat) {
		this.mapLat = mapLat;
	}
	public BigDecimal getMapLong() {
		return mapLong!=null?mapLong:BigDecimal.valueOf(0);
	}
	public void setMapLong(BigDecimal mapLong) {
		this.mapLong = mapLong;
	}
	public Integer getBiStudent() {
		return biStudent;
	}
	public void setBiStudent(Integer biStudent) {
		this.biStudent = biStudent;
	}

	public Long getNextVisit() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, +1);
		return nextVisit!=null?nextVisit:cal.getTimeInMillis();
	}

	public void setNextVisit(Long nextVisit) {
		this.nextVisit = nextVisit;
	}
	
	public static class Fields {
		public static final String ID = "id";
		public static final String MINIS_ID = "ministry_id";
		public static final String NAME = "name";
		public static final String AGE = "agegroup";
		public static final String GENDER = "gender";
		public static final String BS = "bible_student";
		public static final String LANG = "language";
		public static final String REMARKS = "remarks";
		public static final String PHONE1 = "phone_main";
		public static final String PHONE2 = "phone_home";
		public static final String EMAIL = "email";
		public static final String ADDRESS = "street";
		public static final String NEXT_VISIT = "next_visit";
		public static final String LAT = "map_lat";
		public static final String LONG = "map_long";
	}
}
