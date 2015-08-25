package com.crazytech.miniwl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	DatabaseHelper(Context context, String dbName, CursorFactory cf, int dbVersion) {
        super(context, dbName, cf, dbVersion);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(createTableWt());
		db.execSQL(createTablePubs("pubs_g",false));
		db.execSQL(createTablePubs("pubs_okm",false));
		db.execSQL(createTablePubs("pubs_books",false));
		db.execSQL(createTablePubs("pubs_brochures",false));
		db.execSQL(createTablePubs("pubs_yearbooks",false));
		db.execSQL(createTablePubs("pubs_booklets",false));
		db.execSQL(createTableMinistry(false));
		db.execSQL(createTableHouseholders(false));
		db.execSQL(createTablePlacements(false));
		db.execSQL(createTableVisits(false));
		db.execSQL(createTableMinistryGoal());
		db.execSQL(createTableReports());
		
	}
	
	private String createTableMinistry (boolean temp) {
		String col = "id integer primary key autoincrement not null,"+
				"date timestamp,"+
				"time_start integer, time_end integer,"+
				"type integer,"+
				"street text,"+
				"duration integer";
		String newCol = "";
		String str = "";
		if (!temp) {
			str = "create table ministry ("+col+newCol+");";
		} else {
			str = "create table tmp_ministry ("+col+");";
		}
		return str;
	}
	
	private String createTableHouseholders(boolean temp) {
		String col = "id integer primary key autoincrement not null,"+
				"ministry_id integer,"+
				"name varchar(100),"+
				"agegroup varchar(10),"+
				"gender varchar(1),"+
				"symbol varchar(2),"+
				"bible_student integer,"+
				"language varchar(100),"+
				"country varchar(100),"+
				"remarks text,"+
				"house_no varchar(100),"+
				"phone_main varchar(25),"+
				"phone_home varchar(25),"+
				"email varchar(50),"+
				"next_visit timestamp,"+
				"to_discuss text,"+
				"map_lat decimal(5,5),"+
				"map_long decimal(5,5),"+
				"street text";
		String newCol = "";
		String str = "";
		if (!temp) {
			str = "create table householders ("+col+newCol+");";
		} else {
			str = "create table tmp_householders ("+col+");";
		}
		return str;
	}
	
	private String createTablePlacements (boolean temp) {
		String col = "id integer primary key autoincrement not null,"+
				"description varchar(200),"+
				"file_dir varchar(300),"+
				"type integer,"+
				"count integer,"+
				"date timestamp,"+
				"visit_id integer,"+
				"ministry_id integer,"+
				"householder_id integer";
		String newCol = "";
		String str = "";
		if (!temp) {
			str = "create table placements ("+col+newCol+");";
		} else {
			str = "create table tmp_placements ("+col+");";
		}
		return str;
	}
	
	private String createTableVisits (boolean temp) {
		String col = "id integer primary key autoincrement not null,"+
				"householder_id integer,"+
				"ministry_id integer,"+
				"type integer,"+
				"remarks text";
		String newCol = "";
		String str = "";
		if (!temp) {
			str = "create table visits ("+col+newCol+");";
		} else {
			str = "create table tmp_visits ("+col+");";
		}
		return str;
	}
	
	private String createTableMinistryGoal(){
		return "create table if not exists ministry_goal (id integer primary key autoincrement not null, "+
			"date timestamp,hours integer, bs integer, rv integer, "+
			"books integer,mags integer,brocs integer,tracts integer);";
	}
	
	private String createTableReports () {
		String col = "id integer primary key autoincrement not null,"+
				"date timestamp,"+
				"books integer,"+
				"brochures integer,"+
				"mags integer,"+
				"rv integer,"+
				"bs integer,"+
				"hours integer";
		String newCol = "";
		String str =  "create table reports ("+col+newCol+");";		
		return str;
	}
	
	
	
	private String createTableWt () {
		String sql = "create table pubs_wt ("
				+"filename text,"
				+"description text,"
				+"locale text,"
				+"issue_date text,"
				+"dt_start timestamp,"
				+"dt_end timestamp,"
				+"url text,"
				+"imgurl text"
				+")";
		return sql;
	}
	
	private String createTablePubs(String table, boolean temp) {
		String col = "create table "+table+" ("
//				"id integer primary key autoincrement not null,"
				+
				"filename text,"
				+"description text,"
				+"locale text,"
				+"issue_date text,"
//				+"created_at timestamp not null,"
//				+"updated_at timestamp"
				+"url text,"
				+"imgurl text"
				+")";
		
		return col;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/*
		 * ----------------
		 * new in version 2
		 * added column dt_start, dt_end for pubs_wt
		 */
		
		Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
		switch (oldVersion) {
		case 1:changeVer2(db);changeVer3(db);changeVer4(db);changeVer5(db);break;
		case 2:changeVer3(db);changeVer4(db);changeVer5(db);break;
		case 3:changeVer4(db);changeVer5(db);break;
		case 4:changeVer5(db);
		default:break;
		}
	}
	
	private void changeVer2 (SQLiteDatabase db) {
		db.execSQL("alter table pubs_wt rename to tmp_pubs_wt;");
		db.execSQL(createTableWt());
		db.execSQL("insert into pubs_wt (filename,description,locale,issue_date,url) " +
				"select filename,description,locale,issue_date,url from tmp_pubs_wt;");
		db.execSQL("drop table if exists tmp_pubs_wt;");
	}
	
	private void changeVer3 (SQLiteDatabase db) {
		db.execSQL(createTablePubs("pubs_okm",false));
	}
	
	private void changeVer4 (SQLiteDatabase db) {
		db.execSQL("alter table pubs_wt add column imgurl text;");
		db.execSQL("alter table pubs_g add column imgurl text;");
		db.execSQL("alter table pubs_okm add column imgurl text;");
		db.execSQL("alter table pubs_books add column imgurl text;");
		db.execSQL("alter table pubs_booklets add column imgurl text;");
		db.execSQL("alter table pubs_brochures add column imgurl text;");
		db.execSQL("alter table pubs_yearbooks add column imgurl text;");
	}
	
	private void changeVer5 (SQLiteDatabase db) {
		db.execSQL("alter table ministry add column type;");
		db.execSQL("update ministry set type = 1 where field = 1;");
		db.execSQL("update ministry set type = 2 where visit = 1;");
		db.execSQL("update ministry set type = 3 where study = 1;");
		db.execSQL("update ministry set type = 4 where informal = 1;");
		db.execSQL("alter table ministry rename to tmpmin;");
		db.execSQL("create table ministry (id integer primary key autoincrement not null, date timestamp, time_start integer, time_end integer, type integer, street text, duration integer);");
		db.execSQL("insert into ministry (id, date, time_start, time_end, type, street,duration) select id, date, time_start, time_end, type, street, duration from tmpmin;");
		db.execSQL("drop table if exists tmpmin;");
		db.execSQL("alter table visits add column type integer;");
		db.execSQL("update visits set type = 2 where return = 1;");
		db.execSQL("update visits set type = 3 where study = 1;");
		db.execSQL("alter table visits rename to tmpvisits;");
		db.execSQL("create table visits (id integer primary key autoincrement not null, ministry_id integer,householder_id integer,type integer, remarks text);");
		db.execSQL("insert into visits (id,ministry_id,householder_id,type,remarks) select id,ministry_id,householder_id,type,remarks from tmpvisits;");
		db.execSQL("drop table if exists tmpvisits;");
		db.execSQL("create table if not exists ministry_goal (id integer primary key autoincrement not null, date timestamp,hours integer, bs integer, rv integer, books integer,mags integer,brocs integer,tracts integer);");
		db.execSQL("create table placement2 (id integer primary key autoincrement not null, description varchar(200), file_dir varchar(300),type integer, count integer, date timestamp, visit_id integer, ministry_id integer,householder_id integer);");
		db.execSQL("insert into placement2 (description,type,count,date,visit_id,ministry_id,householder_id) select description,1,book,date,visit_id,ministry_id,householder_id from placements where book > 0;");
		db.execSQL("insert into placement2 (description,type,count,date,visit_id,ministry_id,householder_id) select description,2,mag,date,visit_id,ministry_id,householder_id from placements where mag > 0;");
		db.execSQL("insert into placement2 (description,type,count,date,visit_id,ministry_id,householder_id) select description,3,brochure,date,visit_id,ministry_id,householder_id from placements where brochure > 0;");
		db.execSQL("insert into placement2 (description,type,count,date,visit_id,ministry_id,householder_id) select description,4,tract,date,visit_id,ministry_id,householder_id from placements where tract > 0;");
		db.execSQL("update placement2 set date = (select ministry.date from ministry where ministry.id = placement2.ministry_id) where exists (select date from ministry where ministry.id = placement2.ministry_id);");
		db.execSQL("drop table if exists placements;");
		db.execSQL("alter table placement2 rename to placements;");
	}
}
