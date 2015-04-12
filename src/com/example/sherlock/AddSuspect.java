package com.example.sherlock;

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
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class AddSuspect extends Activity {
	
	private EditText et_fullname;
	private Spinner sp_gender;
	private EditText et_height;
	private Spinner sp_ageRange;
	private EditText et_dateOfBirth;
	private Spinner sp_hairColor;
	private EditText et_comment;
	private Button btn_save;	
	private TextView tv_fullname;
	private TextView tv_gender;
	private TextView tv_height;
	private TextView tv_ageRange;
	private TextView tv_dateOfBirth;
	private String strFullname;
	private String strGender;
	private String strHeight;
	private String strAgeRange;
	private String strDob;
	private String strHairColor;
	private String strComment;
	private DatabaseHelper dbHelper;
	//private String mission_name="";
	public static final String ID = "missionId";
	public static final String NAME = "missionName";
	private int intMissionId=0;
	//private TextView tvmissionName;
	
	//on activity pause it is necessary to close database for security reason.
	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper!=null){
			dbHelper.close();
		}
	}
	//creates and displays form to fill up, validates and sends for storage to next method
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_suspect);
		sp_gender = (Spinner) findViewById(R.id.spinnerGender);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerGender, 
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_gender.setAdapter(adapter);
		sp_ageRange = (Spinner) findViewById(R.id.spinnerAgeRange);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.spinnerAgeRange, 
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_ageRange.setAdapter(adapter2);	
		sp_hairColor = (Spinner) findViewById(R.id.spinnerHairColor);
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.spinnerHairColor, 
				android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_hairColor.setAdapter(adapter3);		
		et_fullname = (EditText)findViewById(R.id.et_fullname);
		sp_gender = (Spinner)findViewById(R.id.spinnerGender);
		et_height = (EditText)findViewById(R.id.et_height);
		sp_ageRange = (Spinner)findViewById(R.id.spinnerAgeRange);
		et_dateOfBirth = (EditText)findViewById(R.id.et_dob);
		sp_hairColor = (Spinner)findViewById(R.id.spinnerHairColor);
		et_comment = (EditText)findViewById(R.id.et_comment);		
		btn_save = (Button)findViewById(R.id.saveButton);		
		tv_fullname = (TextView)findViewById(R.id.tv_fullname);
		tv_gender = (TextView)findViewById(R.id.tv_gender);
		tv_height = (TextView)findViewById(R.id.tv_height);
		tv_ageRange = (TextView)findViewById(R.id.tv_ageRange);
		tv_dateOfBirth = (TextView)findViewById(R.id.tv_dob);	
		dbHelper = new DatabaseHelper(this);	
        Bundle extras = getIntent().getExtras();
		if (extras!= null) {
			String suspect_id = extras.getString(ID);
			intMissionId =  Integer.parseInt(suspect_id);
		}

		btn_save.setOnClickListener(new View.OnClickListener() {  
	        @Override
			public void onClick(View v)
	            {		        	
	        		tv_fullname.setTextColor(Color.BLACK);
	        		tv_gender.setTextColor(Color.BLACK);
	        		tv_height.setTextColor(Color.BLACK);
	        		tv_ageRange.setTextColor(Color.BLACK);
	        		tv_dateOfBirth.setTextColor(Color.BLACK);
	        		
	        		strFullname = et_fullname.getText().toString();
		        	strGender = sp_gender.getSelectedItem().toString();
		        	strHeight = et_height.getText().toString();
		        	strAgeRange = sp_ageRange.getSelectedItem().toString();
		        	strDob = et_dateOfBirth.getText().toString();
		        	strHairColor = sp_hairColor.getSelectedItem().toString();
		        	strComment = et_comment.getText().toString(); 
		        	
		        	boolean valid = true;		        	
		        	if(strFullname.equals("")){
		        		tv_fullname.setTextColor(Color.RED);
		        		et_fullname.setError("Enter your fullname");
		        		valid = false;
		        	}
		        	if(strGender.equals("")){
		        		tv_gender.setTextColor(Color.RED);
		        		valid = false;
		        	}
		        	if(strHeight.equals("")){
		        		tv_height.setTextColor(Color.RED);
		        		et_height.setError("Enter height in cm");
		        		valid = false;
		        	}		        	
		        	if(strAgeRange.equals("")){
		        		tv_ageRange.setTextColor(Color.RED);
		        		valid = false;
		        	}
		        	if(!strDob.equals("") && !strDob.matches("\\d{2}/\\d{2}/\\d{4}")){
		        		tv_dateOfBirth.setTextColor(Color.RED);
		        		et_height.setError("Enter DOB in DD/MM/YYYY format");
		        		valid = false;
		        	}
		        	if(valid){
		        		showAddSuspectAlert();	
		        	}
	            }		
		});
	}
	//menu options method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_suspect, menu);
        return true;
    }
	//displays data confirmation before adding data on to database file
    public void showAddSuspectAlert() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Confirm Suspect\n\nName: "+strFullname+"\nGender: "
			+strGender+"\nHeight: "+strHeight+" cm\nAge Range: "+strAgeRange+"\nDOB: "
			+strDob+"\nHair Color: "+strHairColor+"\nComment: "+strComment)
 	       		   .setCancelable(false)
	    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	           @Override
					public void onClick(DialogInterface dialog, int id) {
	    	        	   dbHelper.insertMissionSuspect(intMissionId, strFullname, strGender, strHeight, 
	    	        			   strAgeRange, strHairColor, strDob, strComment, "1");
	    	        	   AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(AddSuspect.this);
		    	        	confirmBuilder.setMessage("Suspect added")
		    	   	    	       .setCancelable(false)
		    	   	    	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	   	    	           @Override
									public void onClick(DialogInterface dialog, int id) {
		    	   	    	        	  //
		    	   	    	           }
		    	   	    	       });
		    	   	    	AlertDialog confirmAlert = confirmBuilder.create();
		    	   	    	confirmAlert.show();
	    	        	   	et_fullname.setText("");
		   		        	sp_gender.setSelection(0);
		   		        	et_height.setText("");
		   		        	sp_ageRange.setSelection(0);
		   		        	et_comment.setText("");
		   		        	et_dateOfBirth.setText("");	
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
