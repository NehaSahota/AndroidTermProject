package com.example.sherlock;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	String classes[] = { "CreateMission", "FindMission"};

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String classString = classes[position];
		try {
			@SuppressWarnings("rawtypes")
			Class myClass = Class.forName("com.example.sherlock." + classString);
			Intent newIntent = new Intent(MainActivity.this, myClass);
			startActivity(newIntent);
		} catch (ClassNotFoundException e) {
			Toast toast = Toast.makeText(this, "" + e, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, classes));
	}

}

