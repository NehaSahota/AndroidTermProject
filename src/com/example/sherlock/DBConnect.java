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
	      ContentValues newMovie = new ContentValues();
	      newMovie.put("name", name);
	      newMovie.put("height", height);
	      newMovie.put("gender", gender);
	      newMovie.put("haircolor", haircolor);
	      newMovie.put("age", age);
	      newMovie.put("notes", notes);
	      newMovie.put("year", year);

	      open(); // open the database
	      long rowID = database.insert("movies", null, newMovie);
	      close(); // close the database
	      return rowID;
	   } 

	   // updates an existing contact in the database
	   public void updateMovie(long id, String name, String height, 
	      String gender, String haircolor, String age, String notes, String year) 
	   {
	      ContentValues editMovie = new ContentValues();
	      editMovie.put("name", name);
	      editMovie.put("height", height);
	      editMovie.put("gender", gender);
	      editMovie.put("haircolor", haircolor);
	      editMovie.put("age", age);
	      editMovie.put("notes", notes);
	      editMovie.put("year", year);

	      open(); 
	      database.update("suspects", editMovie, "_id=" + id, null);
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
