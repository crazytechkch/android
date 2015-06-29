package com.crazytech.miniwl;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class PersistanceManager {
	private SQLiteDatabase _db;
	private DatabaseHelper _dbHelper;
	private Context _context;
	public static final String DATABASE_NAME = "miniWLDB";
	public static final int DATABASE_VERSION = 5;
	
	public PersistanceManager(Context context) {
		super();
		_context = context;
	}
	
	public PersistanceManager open() throws SQLException{
		_dbHelper = new DatabaseHelper(this._context, DATABASE_NAME, null, DATABASE_VERSION);
		_db = _dbHelper.getWritableDatabase();
		return this;
	}
	
	public void onCreate(){
		_dbHelper.onCreate(_db);
	}
	
	/*
	 * Checks whether data exists in database
	 * Uses SQLite Cursor rawQuery("SELECT column FROM table where column = ?", String[] params)
	 * return true if cursor.getCount > 0
	 * else return false
	 */
	public boolean rowExists(String table, String whereClause, String[] params){
		String[] where = whereClause.split("\\?");
		whereClause = "";
		for (int i = 0; i < params.length; i++) {
			whereClause+=(where[i]+params[i]);
		}
		String query = "SELECT EXISTS(SELECT 1 FROM "+table+" WHERE "+whereClause+" LIMIT 1)";
		Cursor cursor = _db.rawQuery(query, null);
		cursor.moveToFirst();
		int result = cursor.getInt(0);
		cursor.close();
		return result==1 ? true:false;
	}
	
	public void recreate(String table) {
		getDb().execSQL("DROP TABLE IF EXISTS "+table);
		getDb().execSQL("create table "+table+" ("
				+"id integer primary key autoincrement,"
				+"filename text,"
				+"description text,"
				+"imgres text,"
				+"locale text,"
				+"issue_date text,"
				+"created_at timestamp,"
				+"updated_at timestamp);");
	}
	
	public void logException(Exception e) {
		Toast.makeText(_context, _context.getString(R.string.failed), Toast.LENGTH_LONG).show();
		Log.e("db error", e.getMessage());
	}
	
	public void close(){
		_dbHelper.close();
	}

	public SQLiteDatabase getDb() {
		return _db;
	}

	public void setDb(SQLiteDatabase db) {
		_db = db;
	}

	public DatabaseHelper getDbHelper() {
		return _dbHelper;
	}

	public void setDbHelper(DatabaseHelper dbHelper) {
		_dbHelper = dbHelper;
	}
	
	
}
