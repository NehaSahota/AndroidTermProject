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

public class AddEditMissionFragment extends Fragment {

	
	// callback method implemented by MainActivity  
		public interface AddEditMissionFragmentListener
		{
			// called after edit completed so mission can be redisplayed
			public void onAddEditCompleted(long rowID);
			
		}
			private AddEditMissionFragmentListener listener; 

			private long rowID; // database row ID of the mission
			private Bundle missionInfoBundle; // arguments for editing a mission

			// EditTexts for mission information
			private EditText missionNameEditText;
			private EditText objectiveEditText;
			
			// set AddEditMissionFragmentListener when Fragment attached   
			@Override
			public void onAttach(Activity activity)
			{
				super.onAttach(activity);
				listener = (AddEditMissionFragmentListener) activity; 
			}
			
			
			// remove AddEditMissionFragmentListener when Fragment detached
			@Override
			public void onDetach()
			{
				super.onDetach();
				listener = null; 
			}

			
			
			
			
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
			{
				super.onCreateView(inflater, container, savedInstanceState);    
				setRetainInstance(true); // save fragment across config changes
				setHasOptionsMenu(true); // fragment has menu items to display

				// inflate GUI and get references to EditTexts
				
				View view = 
				inflater.inflate(R.layout.fragment_add_edit_mission, container, false);
				missionNameEditText = (EditText) view.findViewById(R.id.missionNameEditText);
				objectiveEditText = (EditText) view.findViewById(R.id.objectiveEditText);
				

				missionInfoBundle = getArguments(); // null if creating new mission

				if (missionInfoBundle != null)
				{
					rowID = missionInfoBundle.getLong(MainActivity.ROW_ID);
					missionNameEditText.setText(missionInfoBundle.getString("name"));  
					objectiveEditText.setText(missionInfoBundle.getString("objective"));  
					
				} 

				// set Save mission Button's event listener 
				Button saveMissionButton2 = 
				(Button) view.findViewById(R.id.saveObjectiveButton);
				saveMissionButton2.setOnClickListener(saveObjectiveButtonClicked);
				return view;
			}
			
			
			
			// responds to event generated when user saves a mission
			OnClickListener saveObjectiveButtonClicked = new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if (missionNameEditText.getText().toString().trim().length() != 0)
					{
						// AsyncTask to save mission, then notify listener 
						AsyncTask<Object, Object, Object> saveMissionTask = new AsyncTask<Object, Object, Object>() 
						{
							@Override
							protected Object doInBackground(Object... params) 
							{
								saveMission2(); // save mission to the database
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
			private void saveMission2() 
			{
				// get DatabaseConnector to interact with the SQLite database
				DBConnect databaseConnector = new DBConnect(getActivity());

				if (missionInfoBundle == null)
				{
					// insert the mission information into the database
					rowID = databaseConnector.insertMission2(
					missionNameEditText.getText().toString(),
					objectiveEditText.getText().toString());
					
				} 
				else
				{
					databaseConnector.updateMission2(rowID,
							missionNameEditText.getText().toString(),
							objectiveEditText.getText().toString());
			           
				}
			} // end method saveMission
		
}
