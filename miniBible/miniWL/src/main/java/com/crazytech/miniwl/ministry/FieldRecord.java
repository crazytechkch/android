package com.crazytech.miniwl.ministry;

import android.os.Bundle;


public class FieldRecord {
	private Long id,householderId;
	private String date, street, name,remarks;
	private Long timeStart, timeEnd;
	private Integer bookCount, brochureCount, magCount, tractCount, visitCount, bibleStudyCount, isField,isVisit,bsDistinct, type;
	private Long duration;
	
	public FieldRecord(Long id, String date, Long timeStart, Long timeEnd, Integer bookCount,
			Integer brochureCount, Integer magCount, Integer tractCount, Integer visitCount,
			Integer bibleStudyCount, Long duration) {
		super();
		this.id = id;
		this.date = date;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.bookCount = bookCount;
		this.brochureCount = brochureCount;
		this.magCount = magCount;
		this.tractCount = tractCount;
		this.visitCount = visitCount;
		this.bibleStudyCount = bibleStudyCount;
		this.duration = duration;
	}
	
	public FieldRecord(Long id, String date, Long timeStart, Long timeEnd, String street, Integer bookCount,
			Integer brochureCount, Integer magCount, Integer tractCount, Integer visitCount,
			Integer bibleStudyCount, Long duration, String name) {
		super();
		this.id = id;
		this.date = date;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.street = street;
		this.bookCount = bookCount;
		this.brochureCount = brochureCount;
		this.magCount = magCount;
		this.tractCount = tractCount;
		this.visitCount = visitCount;
		this.bibleStudyCount = bibleStudyCount;
		this.duration = duration;
		this.name = name;
	}
	
	
	public FieldRecord(Long id, String date, Long timeStart) {
		super();
		this.id = id;
		this.date = date;
		this.timeStart = timeStart;
	}


	public FieldRecord(String date, Long timeStart, Integer bookCount, Integer brochureCount,
			Integer magCount, Integer tractCount, Integer visitCount, Integer bibleStudyCount,
			Long duration) {
		super();
		this.date = date;
		this.timeStart = timeStart;
		this.bookCount = bookCount;
		this.brochureCount = brochureCount;
		this.magCount = magCount;
		this.tractCount =tractCount;
		this.visitCount = visitCount;
		this.bibleStudyCount = bibleStudyCount;
		this.duration = duration;
	}

	public FieldRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Bundle getExtras() {
		Bundle extras = new Bundle();
		extras.putLong(Ministry.CAL_START, getTimeStart());
		extras.putLong(Ministry.CAL_END, getTimeEnd());
		extras.putLong(Ministry.DURATION, getDuration());
		extras.putLong(Ministry.PERS_ID, getHouseholderId());
		extras.putString(Ministry.NAME, getName());
		extras.putInt(Ministry.TYPE, getType());
		extras.putInt(Ministry.BOOKS, getBookCount());
		extras.putInt(Ministry.BROCS, getBrochureCount());
		extras.putInt(Ministry.MAGS, getMagCount());
		extras.putInt(Ministry.TRACTS, getTractCount());
		extras.putString(Ministry.STREET, getStreet());
		extras.putString(Ministry.REMARKS, getRemarks());
		return extras;
	}

	public Long getId() {
		return id!=null?id:0L;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTimeStart() {
		return timeStart!=null?timeStart:0L;
	}
	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}
	public Long getTimeEnd() {
		return timeEnd!=null?timeEnd:0L;
	}
	public void setTimeEnd(Long timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Integer getBookCount() {
		return bookCount!=null?bookCount:0;
	}
	public void setBookCount(Integer bookCount) {
		this.bookCount = bookCount;
	}
	public Integer getBrochureCount() {
		return brochureCount!=null?brochureCount:0;
	}
	public void setBrochureCount(Integer brochureCount) {
		this.brochureCount = brochureCount;
	}
	public Integer getMagCount() {
		return magCount!=null?magCount:0;
	}
	public void setMagCount(Integer magCount) {
		this.magCount = magCount;
	}
	public Integer getVisitCount() {
		return visitCount!=null?visitCount:0;
	}
	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}
	public Integer getBibleStudyCount() {
		return bibleStudyCount!=null?bibleStudyCount:0;
	}
	public void setBibleStudyCount(Integer bibleStudyCount) {
		this.bibleStudyCount = bibleStudyCount;
	}
	public Long getDuration() {
		return duration!=null?duration:0L;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getDate() {
		return date!=null?date:"";
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getStreet() {
		return street!=null?street:"";
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getIsField() {
		return isField;
	}

	public void setIsField(Integer isField) {
		this.isField = isField;
	}

	public Integer getIsVisit() {
		return isVisit;
	}

	public void setIsVisit(Integer isVisit) {
		this.isVisit = isVisit;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return id+";"+date+";"+street+";"+timeStart+";"+timeEnd+";"+bookCount+";"+brochureCount+";"+magCount+";"+visitCount+";"+bibleStudyCount+";"+duration;
	}

	public Integer getTractCount() {
		return tractCount!=null?tractCount:0;
	}

	public void setTractCount(Integer tractCount) {
		this.tractCount = tractCount;
	}

	public Integer getBsDistinct() {
		return bsDistinct!=null?bsDistinct:0;
	}

	public void setBsDistinct(Integer bsDistinct) {
		this.bsDistinct = bsDistinct;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getHouseholderId() {
		return householderId!=null?householderId:-1L;
	}

	public void setHousholderId(Long householderId) {
		this.householderId = householderId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
