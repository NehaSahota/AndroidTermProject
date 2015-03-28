package com.example.sherlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddEditDetailsFragment extends Fragment
{
	// callback method implemented by MainActivity  
	public interface AddEditFragmentListener
	{
		// called after edit completed so mission can be redisplayed
		public void onAddEditCompleted(long rowID);
	}

	private AddEditFragmentListener listener; 

	private long rowID; // database row ID of the mission
	private Bundle missionInfoBundle; // arguments for editing a mission

	// EditTexts for mission information
	private EditText nameEditText;
	private EditText heightEditText;
	private EditText genderEditText;
	private EditText haircolorEditText;
	private EditText phoneEditText;
	private EditText ageEditText;
	private EditText notesEditText;

	// set AddEditFragmentListener when Fragment attached   
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		listener = (AddEditFragmentListener) activity; 
	}

	// remove AddEditFragmentListener when Fragment detached
	@Override
	public void onDetach()
	{
		super.onDetach();
		listener = null; 
	}

	// called when Fragment's view needs to be created
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);    
		setRetainInstance(true); // save fragment across config changes
		setHasOptionsMenu(true); // fragment has menu items to display

		// inflate GUI and get references to EditTexts
		View view = 
		inflater.inflate(R.layout.fragment_add_edit, container, false);
		nameEditText = (EditText) view.findViewById(R.id.nameEditText);
		heightEditText = (EditText) view.findViewById(R.id.heightEditText);
		genderEditText = (EditText) view.findViewById(R.id.genderEditText);
		haircolorEditText = (EditText) view.findViewById(R.id.haircolorEditText);
		phoneEditText = (EditText) view.findViewById(R.id.phoneEditText);
		ageEditText = (EditText) view.findViewById(R.id.ageEditText);
		notesEditText = (EditText) view.findViewById(R.id.notesEditText);

		missionInfoBundle = getArguments(); // null if creating new mission

		if (missionInfoBundle != null)
		{
			rowID = missionInfoBundle.getLong(MainActivity.ROW_ID);
			nameEditText.setText(missionInfoBundle.getString("name"));  
			heightEditText.setText(missionInfoBundle.getString("height"));  
			genderEditText.setText(missionInfoBundle.getString("gender"));  
			haircolorEditText.setText(missionInfoBundle.getString("haircolor"));  
			phoneEditText.setText(missionInfoBundle.getString("phone")); 
			ageEditText.setText(missionInfoBundle.getString("age"));  
			notesEditText.setText(missionInfoBundle.getString("notes"));   
		} 

		// set Save mission Button's event listener 
		Button saveMissionButton = 
		(Button) view.findViewById(R.id.saveMissionButton);
		saveMissionButton.setOnClickListener(saveMissionButtonClicked);
		return view;
	}

	// responds to event generated when user saves a mission
	OnClickListener saveMissionButtonClicked = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			if (nameEditText.getText().toString().trim().length() != 0)
			{
				// AsyncTask to save mission, then notify listener 
				AsyncTask<Object, Object, Object> saveMissionTask = new AsyncTask<Object, Object, Object>() 
				{
					@Override
					protected Object doInBackground(Object... params) 
					{
						saveMission(); // save mission to the database
						return null;
					} 

					@Override
					protected void onPostExecute(Object result) 
					{
						// hide soft keyboard
						InputMethodManager imm = (InputMethodManager) 
						getActivity().getSystemService(
						   Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
						getView().getWindowToken(), 0);

						listener.onAddEditCompleted(rowID);
					} 
				}; // end AsyncTask
				
				// save the mission to the database using a separate thread
				saveMissionTask.execute((Object[]) null); 
			} 
			else // required mission name is blank, so display error dialog
			{
			DialogFragment errorSaving = 
			new DialogFragment()
			{
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(R.string.error_message);
					builder.setPositiveButton(R.string.ok, null);                     
					return builder.create();
				}               
			};

			errorSaving.show(getFragmentManager(), "error saving mission");
			} 
		} // end method onClick
	}; // end OnClickListener saveMissionButtonClicked

	// saves mission information to the database
	private void saveMission() 
	{
		// get DatabaseConnector to interact with the SQLite database
		DBConnect databaseConnector = new DBConnect(getActivity());

		if (missionInfoBundle == null)
		{
			// insert the mission information into the database
			rowID = databaseConnector.insertMission(
			nameEditText.getText().toString(),
			genderEditText.getText().toString(),
			ageEditText.getText().toString(), 
            heightEditText.getText().toString(), 
            haircolorEditText.getText().toString(), 
            phoneEditText.getText().toString(), 
            notesEditText.getText().toString());
		} 
		else
		{
			databaseConnector.updateMission(rowID,
	            nameEditText.getText().toString(),
	            genderEditText.getText().toString(),
	            ageEditText.getText().toString(), 
	            heightEditText.getText().toString(), 
	            haircolorEditText.getText().toString(), 
	            phoneEditText.getText().toString(), 
	            notesEditText.getText().toString());
		}
	} // end method saveMission
} // end class AddEditFragment

