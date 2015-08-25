package com.crazytech.miniwl.pubs;

import android.database.Cursor;

public class PubsMagObj {
	private String _filename;
	private long _id;
	private String _desc;
	private String _imgres;
	private String _locale;
	private String _type;
	private Integer _typeInt;
	private String _url = "";
	private String _issueDate;
	private String _dtStart, _dtEnd;
	
	public PubsMagObj() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PubsMagObj(Cursor cursor, String type) {
		super();
		_filename = cursor.getString(cursor.getColumnIndex("filename"));
		_desc = cursor.getString(cursor.getColumnIndex("description"));
		_locale = cursor.getString(cursor.getColumnIndex("locale"));
		_url = cursor.getString(cursor.getColumnIndex("url"));
		_type = type;
		String issueDate = cursor.getString(cursor.getColumnIndex("issue_date"));
		if (issueDate!=null) _issueDate = issueDate;
		if (_type.equals("wt")){
			_dtStart = cursor.getString(cursor.getColumnIndex("dt_start"));
			_dtEnd = cursor.getString(cursor.getColumnIndex("dt_end"));
		}
	}
	public String getFilename() {
		return _filename;
	}
	public void setFilename(String filename) {
		_filename = filename;
	}
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		_id = id;
	}
	public String getDesc() {
		if(_desc==null||_desc.length()<1)return _filename;
		return _desc;
	}
	public void setDesc(String desc) {
		_desc = desc;
	}
	public String getImgres() {
		return _imgres;
	}
	public void setImgres(String imgres) {
		_imgres = imgres;
	}
	public String getType() {
		return _type;
	}
	public void setType(String type) {
		_type = type;
	}
	public String getLocale() {
		return _locale;
	}
	public void setLocale(String locale) {
		_locale = locale;
	}
	public Integer getTypeInt() {
		return _typeInt;
	}
	public void setTypeInt(Integer typeInt) {
		_typeInt = typeInt;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return _desc+" ("+_filename+")";
	}
	public String getUrl() {
		return _url;
	}
	public void setUrl(String url) {
		_url = url;
	}
	public String getIssueDate() {
		if (_issueDate.indexOf("_")!=-1) return _issueDate.split("_")[2];
		
		return _issueDate;
	}
	public void setIssueDate(String issueDate) {
		_issueDate = issueDate;
	}
	public String getDtStart() {
		return _dtStart;
	}
	public void setDtStart(String dtStart) {
		_dtStart = dtStart;
	}
	public String getDtEnd() {
		return _dtEnd;
	}
	public void setDtEnd(String dtEnd) {
		_dtEnd = dtEnd;
	}
}
