package com.crazytech.miniwl.ministry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;

public class Ministry {
	private Calendar _calStart, _calEnd;
	private Long id,_duration,durationBs,durationRv,_householderId;

	private int _type, _books, _mags, _brocs, _tracts,bs,bsDistinct,rv;
	private int pubsRv,pubsBs;
	private String date,name,_street,_remarks;
	
	public static final String ID = "id";
	public static final String CAL_START = "calstart";
	public static final String CAL_END = "calend";
	public static final String DATE = "date";
	public static final String DURATION = "duration";
	public static final String PERS_ID = "persid";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String BOOKS = "books";
	public static final String MAGS = "mags";
	public static final String BROCS = "brocs";
	public static final String TRACTS = "tracts";
	public static final String STREET = "street";
	public static final String REMARKS = "remarks";
	public static final String IS_EDIT = "edit";

	public Bundle getExtras(boolean isEdit) {
		Bundle extras = new Bundle();
		if(isEdit)extras.putLong(ID, getId());
		extras.putBoolean(IS_EDIT, isEdit);
		extras.putLong(CAL_START, getCalStart().getTimeInMillis());
		extras.putLong(CAL_END, getCalEnd().getTimeInMillis());
		extras.putString(DATE, getDate());
		extras.putLong(DURATION, getDuration());
		extras.putLong(PERS_ID, getHouseholderId());
		extras.putString(NAME, getName());
		extras.putInt(TYPE, getType());
		extras.putInt(BOOKS, getBooks());
		extras.putInt(BROCS, getBrocs());
		extras.putInt(MAGS, getMags());
		extras.putInt(TRACTS, getTracts());
		extras.putString(STREET, getStreet());
		extras.putString(REMARKS, getRemarks());
		return extras;
	}
	
	public Calendar getCalStart() {
		return _calStart!=null?_calStart:Calendar.getInstance();
	}
	public void setCalStart(Calendar calStart) {
		_calStart = calStart;
	}
	public Calendar getCalEnd() {
		return _calEnd!=null?_calEnd:Calendar.getInstance();
	}
	public void setCalEnd(Calendar calEnd) {
		_calEnd = calEnd;
	}
	public Long getDuration() {
		return _duration!=null?_duration:0L;
	}
	public void setDuration(Long duration) {
		_duration = duration;
	}
	public int getType() {
		return _type;
	}
	public void setType(int type) {
		_type = type;
	}
	public int getBooks() {
		return _books;
	}
	public void setBooks(int books) {
		_books = books;
	}
	public int getMags() {
		return _mags;
	}
	public void setMags(int mags) {
		_mags = mags;
	}
	public int getBrocs() {
		return _brocs;
	}
	public void setBrocs(int brocs) {
		_brocs = brocs;
	}
	public int getTracts() {
		return _tracts;
	}
	public void setTracts(int tracts) {
		_tracts = tracts;
	}
	public String getRemarks() {
		return _remarks!=null?_remarks:"";
	}
	public void setRemarks(String remarks) {
		_remarks = remarks;
	}
	public String getStreet() {
		return _street;
	}
	public void setStreet(String street) {
		_street = street;
	}
	public Long getHouseholderId() {
		return _householderId!=null?_householderId:-1L;
	}
	public void setHouseholderId(Long householderId) {
		_householderId = householderId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBs() {
		return bs;
	}

	public void setBs(int bs) {
		this.bs = bs;
	}

	public int getRv() {
		return rv;
	}

	public void setRv(int rv) {
		this.rv = rv;
	}

	public int getBsDistinct() {
		return bsDistinct;
	}

	public void setBsDistinct(int bsDistinct) {
		this.bsDistinct = bsDistinct;
	}
	public Long getDurationBs() {
		return durationBs;
	}
	
	public void setDurationBs(Long durationBs) {
		this.durationBs = durationBs;
	}
	
	public Long getDurationRv() {
		return durationRv;
	}
	
	public void setDurationRv(Long durationRv) {
		this.durationRv = durationRv;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["
				+ "{id:"+getId()+"},"
				+ "{date:"+getDate()+"},"
				+ "{calStart:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(getCalStart().getTime())+"},"
				+ "{calEnd:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(getCalEnd().getTime())+"},"
				+ "{householder_id:"+getHouseholderId()+"},"
				+ "{name:"+getName()+"},"
				+ "{type:"+getType()+"},"
				+ "{books:"+getBooks()+"},"
				+ "{mags:"+getMags()+"},"
				+ "{brocs:"+getBrocs()+"},"
				+ "{tracts:"+getTracts()+"},"
				+ "{bs:"+getBs()+"},"
				+ "{rv:"+getRv()+"},"
				+ "{street:"+getStreet()+"},"
				+ "{remarks:"+getRemarks()+"}"
				+ "]";
	}

	public int getPubsRv() {
		return pubsRv;
	}

	public void setPubsRv(int pubsRv) {
		this.pubsRv = pubsRv;
	}

	public int getPubsBs() {
		return pubsBs;
	}

	public void setPubsBs(int pubsBs) {
		this.pubsBs = pubsBs;
	}
}
