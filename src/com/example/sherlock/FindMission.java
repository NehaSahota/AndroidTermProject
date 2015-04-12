package com.example.sherlock;


import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FindMission extends ListActivity {

	private DatabaseHelper dbHelper;
	private EditText et_search;

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
		setContentView(R.layout.list_screen);
		dbHelper = new DatabaseHelper(this);
		et_search = (EditText)findViewById(R.id.et_search);
		displayResults("");
		et_search.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {	
				String queryStr = et_search.getText().toString();
				if (!queryStr.equals("")){
					displayResults(queryStr);
				} 
			}
	
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
	
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}			
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			LinearLayout thisLayout = (LinearLayout)v;
			TextView tv = (TextView)thisLayout.findViewById(R.id.tv_id);
			String mission_id = tv.getText().toString();
			
			if(!mission_id.equals("")){
				Intent intent = new Intent(this, MissionDetails.class);
				intent.putExtra(MissionDetails.ID, mission_id);
				startActivity(intent);				
			}
			
		} catch (Exception e) {
			Toast toast = Toast.makeText(this, "" + e, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void displayResults(String queryStr){
		Cursor results = dbHelper.findMissions(queryStr);		
		String[] columnNames ={ "date_and_time", "location_placename", "_id"};
		int[] displayNames={  R.id.tv_fullname1, R.id.tv_gender1, R.id.tv_id};		
		SimpleCursorAdapter records = new SimpleCursorAdapter(this, R.layout.list_record_row, results, 
				columnNames, displayNames);
		setListAdapter(records);
		startManagingCursor(results);
	}	
}
