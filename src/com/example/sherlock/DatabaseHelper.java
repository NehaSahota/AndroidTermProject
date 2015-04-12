package com.example.sherlock;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sherlock";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database;
	//constructor
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		database = getWritableDatabase();		
	}
	//creates tables if they don't already exists
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE mission_tbl( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"date_and_time DATE, location_lat TEXT, " +
				"location_lon TEXT, location_placename TEXT, comments TEXT, " +
				"active_status TEXT);");
		
		db.execSQL("CREATE TABLE suspect_tbl( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"mission_id INTEGER, fullname TEXT, " +
				"gender TEXT, height INTEGER, age_range TEXT, hair_color TEXT, " +
				"dob DATA, comments TEXT, active_status TEXT, FOREIGN KEY (mission_id) REFERENCES mission_tbl(_id));");
		
	}
	//onUpgrade, it drops table and recreate them
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " old data lost");
		db.execSQL("DROP TABLE IF EXISTS mission_tbl");
		db.execSQL("DROP TABLE IF EXISTS suspect_tbl");
		onCreate(db);
	}
	
	public long insertMissionData(String date_and_time,  
			String location_placename, String notes) {
		ContentValues missionRowValues = new ContentValues();
		missionRowValues.put("date_and_time", date_and_time);
		missionRowValues.put("location_placename", location_placename);
		missionRowValues.put("comments", notes);
		missionRowValues.put("active_status", "1");		
		return database.insertOrThrow("mission_tbl", null, missionRowValues);
	}
	// this method inserts new suspect's suspect details, it is stored againts mission's unique ID

	public long insertMissionSuspect(
			int mission_id, String fullname, String gender, 
			String height, String age_range, 
			String hair_color, String dob,
			String comments, String active_status) {	
		ContentValues suspectRowValues = new ContentValues();
		suspectRowValues.put("fullname", fullname);
		suspectRowValues.put("gender", gender);
		suspectRowValues.put("height", height);
		suspectRowValues.put("age_range", age_range);
		suspectRowValues.put("hair_color", hair_color);
		suspectRowValues.put("dob", dob);
		suspectRowValues.put("comments", comments);
		suspectRowValues.put("active_status", active_status);	
		suspectRowValues.put("mission_id", mission_id);
		return database.insertOrThrow("suspect_tbl", null, suspectRowValues);
	}
	
	//searches missions by name on database and returns a Cursor

	
	public Cursor findMissions(String queryString) {
		return database.query(
				"mission_tbl", 
				new String[] { "_id", "date_and_time", "location_placename", "comments" },
				"date_and_time like \"%"+queryString+"%\"", 
				null, null, null, null); 
	}
	//returns all missions (for upload purpose) in a Cursor

	
	public Cursor getAllMissions() {
		return database.query(
				"mission_tbl", 
				new String[] { "_id", "date_and_time", "location_placename", "comments" },
				null, null, null, null, null); 
	}
	//returns mission details against an ID

	
	public Cursor getMissionsDetails(int mission_id) {
		return database.query(
				"mission_tbl", 
				new String[] { "date_and_time", "location_placename", "comments"  },
				"_id = \""+mission_id+"\"", 
				null, null, null, null); 
	}
	//returns suspect's suspect details against an ID

	
	public Cursor getSuspectDetails(int mission_id) {
		return database.query(
				"suspect_tbl", 
				new String[] { "fullname", "gender", "height", "age_range", "hair_color", "dob", "comments"},
				"mission_id = \""+mission_id+"\"", 
				null, null, null, null); 
	}
	//deletes suspect using ID
	public void deleteMission(int mission_id) {
		database.execSQL("DELETE from suspect_tbl WHERE mission_id = "+mission_id+"");
		database.execSQL("DELETE from mission_tbl WHERE _id = "+mission_id+"");
	}
}
