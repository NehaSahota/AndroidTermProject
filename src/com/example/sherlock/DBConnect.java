package com.example.sherlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBConnect 
{
	// database name
	private static final String DATABASE_NAME = "UserMission";

	// for interacting with the database
	private SQLiteDatabase database;

	// creates the database
	private DatabaseOpenHelper databaseOpenHelper; 

	// public constructor for DatabaseConnector
	public DBConnect(Context context) 
	{
		// create a new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	}

	// open the database connection
	public void open() throws SQLException 
	{
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	}

	// close the database connection
	public void close() 
	{
		if (database != null){
			database.close(); // close the database connection
		}
	} 

	// inserts a new Mission in the database
	public long insertMission(String name, String gender, String age,  
	String height, String haircolor, String phone, String notes) 
	{
		ContentValues newMission = new ContentValues();
		newMission.put("name", name);
		newMission.put("gender", gender);
		newMission.put("age", age);
		newMission.put("height", height);
		newMission.put("haircolor", haircolor);
		newMission.put("phone", phone);
		newMission.put("notes", notes);

		open(); // open the database
		long rowID = database.insert("missions", null, newMission);
		close(); // close the database
		return rowID;
	} 

	// updates an existing Mission in the database
	public void updateMission(long id, String name, String gender, 
	String age, String height, String haircolor, String phone, String notes) 
	{
		ContentValues editMission = new ContentValues();
		editMission.put("name", name);
		editMission.put("gender", gender);
		editMission.put("age", age);
		editMission.put("height", height);
		editMission.put("haircolor", haircolor);
		editMission.put("phone", phone);
		editMission.put("notes", notes);

		open(); // open the database
		database.update("missions", editMission, "_id=" + id, null);
		close(); // close the database
	} // end method updateMission

	// return a Cursor with all Mission names in the database
	public Cursor getAllMissions() 
	{
		return database.query("missions", new String[] {"_id", "name"}, null, null, null, null, "name");
	} 

	// return a Cursor containing specified Mission's information 
	public Cursor getOneMission(long id) 
	{
		return database.query("missions", null, "_id=" + id, null, null, null, null);
	} 

	// delete the mission specified by the given String name
	public void deleteMission(long id) 
	{
		open(); // open the database
		database.delete("missions", "_id=" + id, null);
		close(); // close the database
	} 

	private class DatabaseOpenHelper extends SQLiteOpenHelper 
	{
		// constructor
		public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
		}

		// creates the Missions table when the database is created
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			// query to create a new table named Missions
			String createQuery = "CREATE TABLE missions" +
			"(_id integer primary key autoincrement," +
			"name TEXT, gender TEXT, age TEXT, " +
			"height TEXT, haircolor TEXT, phone TEXT, notes TEXT);";

			db.execSQL(createQuery); // execute query to create the database
		} 

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
		}
	} // end class DatabaseOpenHelper
} // end class DatabaseConnector

