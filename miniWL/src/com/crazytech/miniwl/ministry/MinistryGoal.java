package com.crazytech.miniwl.ministry;

public class MinistryGoal {
	private int _hours, _bs, _rv, _books, _mags, _brocs, _tracts;
	
	public MinistryGoal(int hours, int bs, int rv, int books, int mags,
			int brocs, int tracts) {
		super();
		_hours = hours;
		_bs = bs;
		_rv = rv;
		_books = books;
		_mags = mags;
		_brocs = brocs;
		_tracts = tracts;
	}

	public int getHours() {
		return _hours;
	}

	public void setHours(int hours) {
		_hours = hours;
	}

	public int getBs() {
		return _bs;
	}

	public void setBs(int bs) {
		_bs = bs;
	}

	public int getRv() {
		return _rv;
	}

	public void setRv(int rv) {
		_rv = rv;
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
	
	
}
