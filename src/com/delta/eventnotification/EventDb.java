package com.delta.eventnotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDb {

	public static final String KEY_DATE = "_date";
	public static final String KEY_NAME = "_name";
	public static final String KEY_TIME = "_time";
	public static final String KEY_DESC = "_desc";
	public static final String KEY_VENUE = "_venue";
	// public static final String KEY_CHEMAIL = "_chemail";
	// public static final String KEY_CHCALL = "_chcall";

	private static final String DATABASE_NAME = "MyEventDb";
	private static final String DATABASE_TABLE = "Events";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private Context ourContext;
	private static SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" + KEY_NAME
					+ " TEXT NOT NULL, " + KEY_DATE + " TEXT, " + KEY_TIME
					+ " TEXT, " + KEY_VENUE + " TEXT, " + KEY_DESC + " TEXT);");
			// db.execSQL("create table MyEvents(_event text not null, _date text, _time text, _priority varchar(10));");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			ourDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(ourDatabase);

		}

	}

	public EventDb(Context c) {
		this.ourContext = c;
		ourHelper = new DbHelper(ourContext);
	}

	public EventDb open() throws SQLException {
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String name, String date, String time, String desc,
			String venue) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_DATE, date);
		cv.put(KEY_TIME, time);
		cv.put(KEY_DESC, desc);
		cv.put(KEY_VENUE, venue);
		// cv.put(KEY_CHEMAIL, chemail);
		// cv.put(KEY_CHSMS, chsms);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);

	}

	// public String getData() {
	public List getData() {
		// TODO Auto-generated method stub
		List docList = new ArrayList();
		String[] columns = new String[] { KEY_NAME, KEY_DATE, KEY_TIME,
				KEY_VENUE, KEY_DESC };
		// KEY_EMAIL, KEY_PHNO, KEY_CHCALL, KEY_CHEMAIL, KEY_CHSMS};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String result = "";
		System.out.println("no of records: " + c.getCount());
		int iEvent = c.getColumnIndex(KEY_NAME);
		int iDate = c.getColumnIndex(KEY_DATE);
		int iTime = c.getColumnIndex(KEY_TIME);
		int iDesc = c.getColumnIndex(KEY_DESC);
		int iVenue = c.getColumnIndex(KEY_VENUE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			// result = result + c.getString(iEvent) + " " + c.getString(iDate)
			// + " " + c.getString(iTime) + " " + c.getString(iP) + "\n" ;
			HashMap temp = new HashMap();
			temp.put("Name", c.getString(iEvent).toUpperCase());
			temp.put("Date", c.getString(iDate));
			temp.put("Time", c.getString(iTime));
			temp.put("Desc", c.getString(iDesc));
			temp.put("Venue", c.getString(iVenue));

			docList.add(temp);

		}
		return docList;
	}

	public void delete(String hMap) {
		// TODO Auto-generated method stub

		ourDatabase.delete(DATABASE_TABLE, "_name='" + hMap + "'", null);

	}
}