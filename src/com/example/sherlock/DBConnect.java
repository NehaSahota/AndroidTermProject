package com.example.sherlock;




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class DBConnect {
	
	private static final String DATABASE_NAME = "UserSuspects";
    
	
	   private SQLiteDatabase database; 
	   private DatabaseOpenHelper databaseOpenHelper; 
	 
	   public DBConnect(Context context) 
	   {
	      
	      databaseOpenHelper = 
	         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	   }

	   
	   public void open() throws SQLException 
	   {
	      
	      database = databaseOpenHelper.getWritableDatabase();
	   }

	  
	   public void close() 
	   {
	      if (database != null)
	         database.close(); 
	   } 

	   public long insertSuspect(String name, String height, String gender,  
	      String haircolor, String age, String notes, String year) 
	   {
	      ContentValues newSuspect = new ContentValues();
	      newSuspect.put("name", name);
	      newSuspect.put("height", height);
	      newSuspect.put("gender", gender);
	      newSuspect.put("haircolor", haircolor);
	      newSuspect.put("age", age);
	      newSuspect.put("notes", notes);
	      newSuspect.put("year", year);

	      open(); // open the database
	      long rowID = database.insert("movies", null, newSuspect);
	      close(); // close the database
	      return rowID;
	   } 

	   // updates an existing suspect in the database
	   public void updateSuspect(long id, String name, String height, 
	      String gender, String haircolor, String age, String notes, String year) 
	   {
	      ContentValues editSuspect = new ContentValues();
	      editSuspect.put("name", name);
	      editSuspect.put("height", height);
	      editSuspect.put("gender", gender);
	      editSuspect.put("haircolor", haircolor);
	      editSuspect.put("age", age);
	      editSuspect.put("notes", notes);
	      editSuspect.put("year", year);

	      open(); 
	      database.update("suspects", editSuspect, "_id=" + id, null);
	      close(); 
	   } 

	  
	   public Cursor getAllSuspects() 
	   {
	      return database.query("suspects", new String[] {"_id", "name"}, 
	         null, null, null, null, "name");
	   } 

	 
	   public Cursor getOneSuspect(long id) 
	   {
	      return database.query(
	         "suspects", null, "_id=" + id, null, null, null, null);
	   } 

	  
	   public void deleteSuspect(long id) 
	   {
	      open();
	      database.delete("suspects", "_id=" + id, null);
	      close(); 
	   } 
	   
	   private class DatabaseOpenHelper extends SQLiteOpenHelper 
	   {
	      
	      public DatabaseOpenHelper(Context context, String name,
	         CursorFactory factory, int version) 
	      {
	         super(context, name, factory, version);
	      }

	     
	      @Override
	      public void onCreate(SQLiteDatabase db) 
	      {
	        
	         String createQuery = "CREATE TABLE suspects" +
	            "(_id integer primary key autoincrement," +
	            "name TEXT, height TEXT, gender TEXT, " +
	            "haircolor TEXT, age TEXT, notes TEXT, year TEXT);";
	                  
	         db.execSQL(createQuery); 
	      } 

	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	          int newVersion) 
	      {
	      }
	   } 


}
