package com.example.sherlock;




//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
//import android.util.Log;
//import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

public class CreateMission extends Activity {

	public static final String ID = "suspectId";
	public static final String NAME = "suspectName";
	//private String mission_name="";
	//private int intMissionId=0;
	//private TextView tvMissionName;
	private TextView tvDateTime;
	private TextView tvLocation;
	private TextView tvNotes;
	private EditText etDateTime;
	private EditText etLocation;
	private EditText etNotes;
	private Button btnAddMovement;
	private DatabaseHelper dbHelper;
	private String strDateTime;
	private String strLocation;
	private String strNotes;
	//private static final String ns = null;
	 
	//onPause the object of DatabaseHelper is closed 
	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	//this method loads activity and its layout, it also auto detects data & time as well as user location
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        //tvMissionName = (TextView)findViewById(R.id.tvAddMissionFullname);
        etDateTime = (EditText)findViewById(R.id.etAddMissionDateTime);
        etLocation = (EditText)findViewById(R.id.etAddMissionLocation);
        etNotes = (EditText)findViewById(R.id.etNotes);
        tvDateTime = (TextView)findViewById(R.id.tvAddMissionDateTime);
        tvLocation = (TextView)findViewById(R.id.tvAddMissionLocation);
        tvNotes = (TextView)findViewById(R.id.tvNotes);
        btnAddMovement = (Button)findViewById(R.id.addMissionButton1);
        dbHelper = new DatabaseHelper(this);
		btnAddMovement.setOnClickListener(new View.OnClickListener() {  
	        @Override
			public void onClick(View v)
	            {		        	
	        		tvDateTime.setTextColor(Color.BLACK);
	        		tvLocation.setTextColor(Color.BLACK);
	        		tvNotes.setTextColor(Color.BLACK);

	        		strDateTime = etDateTime.getText().toString();
	        		strLocation = etLocation.getText().toString();
	        		strNotes = etNotes.getText().toString();
		        	
		        	boolean valid = true;		        	
		        	if(strDateTime.equals("")){
		        		tvDateTime.setTextColor(Color.RED);
		        		etDateTime.setError("Enter date and time");
		        		valid = false;
		        	}
		        	if(strLocation.equals("")){
		        		tvLocation.setTextColor(Color.RED);
		        		etLocation.setError("Enter location");
		        		valid = false;
		        	}
		        	if(strNotes.equals("")){
		        		tvNotes.setTextColor(Color.RED);
		        		etNotes.setError("Enter location");
		        		valid = false;
		        	}
		        	if(valid){
		        		showAlert("Mission details stored","OK");	        		
		        	}
	            }		
		});
	    }
    //menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
    }
	//displays data to the user before storing to the database for confiimation
	public void showAlert(String message, String buttonValue) {
		if(!message.equals("") && !buttonValue.equals("")){
			AlertDialog.Builder builder = new AlertDialog.Builder(CreateMission.this);
	    	builder.setMessage(message +"\n\nDateTime: "+strDateTime+"\nLocation: "+strLocation+
	    			"\nNotes: "+strNotes)
	    	       .setCancelable(false)
	    	       .setPositiveButton(buttonValue, new DialogInterface.OnClickListener() {
	    	           @Override
					public void onClick(DialogInterface dialog, int id) {
	    	        	   dbHelper.insertMissionData(strDateTime, strLocation, strNotes);		        	
				    	        	AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(CreateMission.this);
				    	        	confirmBuilder.setMessage("Mission created")
				    	   	    	       .setCancelable(false)
				    	   	    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				    	   	    	           @Override
											public void onClick(DialogInterface dialog, int id) {
				    	   	    	        	  //
				    	   	    	           }
				    	   	    	       });
				    	   	    	AlertDialog confirmAlert = confirmBuilder.create();
				    	   	    	confirmAlert.show();
				    	   	    	etDateTime.setText("");
				    	   	    	etLocation.setText("");
				    	   	    	etNotes.setText("");   		        	
	    	           }
	    	       })
	    	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	           @Override
					public void onClick(DialogInterface dialog, int id) {
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
			}
		}
}




