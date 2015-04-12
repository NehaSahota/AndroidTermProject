package com.example.sherlock;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MissionDetails extends Activity {
	

	private TextView tvDateTime;
	private TextView tvLocation;
	private TextView tvNotes;
	private String mission_name;
	private String strDateTime;
	private String strLocation;
	private String strNotes;
	
	
	
	private String mission_id;
	public static final String ID = "id";
	private DatabaseHelper dbHelper;	
	private Button btn_delete;
	private Button btn_addSuspect;	
	private Cursor cursor;	
	private Cursor cursor1;

	@Override
	protected void onPause() {
		super.onPause();
		if(dbHelper!=null){
			dbHelper.close();			
		}
		
		finish();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_info);

        tvDateTime = (TextView)findViewById(R.id.tvAddMissionDateTime);
        tvLocation = (TextView)findViewById(R.id.tvAddMissionLocation);
        tvNotes = (TextView)findViewById(R.id.tvNotes);
        
        
        
        Bundle extras = getIntent().getExtras();
		if (extras!= null) {
			mission_id = extras.getString(ID);
			int intMissionId =  Integer.parseInt(mission_id);
			
			dbHelper = new DatabaseHelper(this);
			cursor = dbHelper.getMissionsDetails(intMissionId);			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				
				strDateTime = cursor.getString(0);
				strLocation = cursor.getString(1);
				strNotes = cursor.getString(2);			
		        
		        tvDateTime.setText("Name: "+strDateTime);
		        tvLocation.setText("Location: "+strLocation);
		        tvNotes.setText("Notes: "+strNotes);
				
		       	cursor.moveToNext();
			}
			cursor.close();
			
			LinearLayout suspectLayout = (LinearLayout)findViewById(R.id.suspectLinearLayout);
			suspectLayout.setPadding(10, 0, 0, 10);
			
			
			cursor1 = dbHelper.getSuspectDetails(intMissionId);
			int numberOfSuspects = cursor1.getCount();
			if(numberOfSuspects==0){
				suspectLayout.setVisibility(View.INVISIBLE);
			}
			

			TextView[] tv_suspectName = new TextView[numberOfSuspects];
			TextView[] tv_suspectGender = new TextView[numberOfSuspects];
			TextView[] tv_suspectHeight = new TextView[numberOfSuspects];
			TextView[] tv_suspectAgeRange = new TextView[numberOfSuspects];
			TextView[] tv_suspectDOB = new TextView[numberOfSuspects];
			TextView[] tv_suspectHairColor = new TextView[numberOfSuspects];
			TextView[] tv_suspectComment = new TextView[numberOfSuspects];
			
			
			cursor1.moveToFirst();
			
			int i = 0;
			while (!cursor1.isAfterLast()) {
				tv_suspectName[i]= new TextView(this);
				tv_suspectGender[i]= new TextView(this);
				tv_suspectHeight[i]= new TextView(this);		
				tv_suspectAgeRange[i]= new TextView(this);
				tv_suspectDOB[i]= new TextView(this);
				tv_suspectHairColor[i]= new TextView(this);	
				tv_suspectComment[i]= new TextView(this);	
				
				tv_suspectName[i].setText("Name: "+cursor1.getString(0));
				tv_suspectGender[i].setText("Gender: "+cursor1.getString(1));
				tv_suspectHeight[i].setText("Height: "+cursor1.getString(2));
				tv_suspectAgeRange[i].setText("AgeRange: "+cursor1.getString(3));
				tv_suspectDOB[i].setText("DOB: "+cursor1.getString(4));
				tv_suspectHairColor[i].setText("HairColor: "+cursor1.getString(5));
				tv_suspectComment[i].setText("Comment: "+cursor1.getString(6)+"\n");
				
				suspectLayout.addView(tv_suspectName[i]);
				suspectLayout.addView(tv_suspectGender[i]);
				suspectLayout.addView(tv_suspectHeight[i]);
				suspectLayout.addView(tv_suspectAgeRange[i]);
				suspectLayout.addView(tv_suspectDOB[i]);
				suspectLayout.addView(tv_suspectHairColor[i]);
				suspectLayout.addView(tv_suspectComment[i]);
				
				i++;
		        cursor1.moveToNext();
			}
			cursor1.close();
			
		}
		
		
		btn_delete = (Button)findViewById(R.id.deleteButton);
		btn_delete.setOnClickListener(new View.OnClickListener() {  
	        @Override
			public void onClick(View v)
	            {		        	
	        		if(mission_id!=null){	        	
			        	showAlert("Confirm deletion","OK");
		        	}
	            }		
		});
		
		btn_addSuspect = (Button)findViewById(R.id.addSuspectButton);
		btn_addSuspect.setOnClickListener(new View.OnClickListener() {  
	        @Override
			public void onClick(View v)
	            {		        	
	        		Intent i = new Intent(getBaseContext(), AddSuspect.class);
	        		i.putExtra(AddSuspect.ID, mission_id);
	        		i.putExtra(AddSuspect.NAME, mission_name);
	        	    startActivity(i);
	            }		
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mission_info, menu);
        return true;
    }
    
    public void showAlert(String message, String buttonValue) {
		if(!message.equals("") && !buttonValue.equals("")){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage(message)
	    	       .setCancelable(false)
	    	       .setPositiveButton(buttonValue, new DialogInterface.OnClickListener() {
	    	           @Override
					public void onClick(DialogInterface dialog, int id) {
	    	        	   int intMissionId =  Integer.parseInt(mission_id);
	    	        	   dbHelper.deleteMission(intMissionId);
	    	        	   Intent i = new Intent(getBaseContext(), FindMission.class);
	    	        	   startActivity(i);
	    	           }
	    	       })
	    	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	           @Override
					public void onClick(DialogInterface dialog, int id) {
	    	        	   //
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
			}
	}
}
