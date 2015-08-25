package com.crazytech.miniwl.pubs.wt;

import java.util.List;

import com.crazytech.miniwl.pubs.PubsMagObj;

public class PubsWtObj {
	private String _issueDate;
	private List<PubsMagObj> _magLst;
	
	public String getIssueDate() {
		int start = _issueDate.lastIndexOf("_")+1;
		int end = _issueDate.length();
		return _issueDate.substring(start, end);
	}
	
	public String getLongIssueDate() {
		return _issueDate;
	}
	
	public void setIssueDate(String issueDate) {
		_issueDate = issueDate;
	}
	public List<PubsMagObj> getMagLst() {
		return _magLst;
	}
	public void setMagLst(List<PubsMagObj> magLst) {
		_magLst = magLst;
	}
	
	
}
