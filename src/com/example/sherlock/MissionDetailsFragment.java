package com.example.sherlock;




import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MissionDetailsFragment extends Fragment{
	
	
	// callback methods implemented by MainActivity  
	   public interface MissionDetailsFragmentListener
	   {
	      // called when a Mission is deleted
	      public void onMissionDeleted2();
	      
	      // called to pass Bundle of Mission's info for editing
	      public void onEditMission2(Bundle arguments);
	   }
	   
	   private MissionDetailsFragmentListener listener;
	   
	   private long rowID = -1; // selected Mission's rowID
	   private TextView objectiveNameTextView; // displays Mission's name 
	   private TextView objectiveLabelTextView; // displays Mission's objective
	  
	   
	   // set MissionDetailsFragmentListener when fragment attached   
	   @Override
	   public void onAttach(Activity activity)
	   {
	      super.onAttach(activity);
	      listener = (MissionDetailsFragmentListener) activity;
	   }
	   
	   
	   // remove MissionDetailsFragmentListener when fragment detached
	   @Override
	   public void onDetach()
	   {
	      super.onDetach();
	      listener = null;
	   }

	   // called when MissionDetailsFragmentListener view needs to be created

	   
	   @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState)
	   {
	      super.onCreateView(inflater, container, savedInstanceState);  
	      setRetainInstance(true); // save fragment across config changes

	      // if DetailsFragment is being restored, get saved row ID
	      if (savedInstanceState != null) 
	         rowID = savedInstanceState.getLong(MainActivity.ROW_ID);
	      else 
	      {
	         // get Bundle of arguments then extract the Mission's row ID
	         Bundle arguments = getArguments(); 
	         
	         if (arguments != null)
	            rowID = arguments.getLong(MainActivity.ROW_ID);
	      }
	         
	      // inflate DetailsFragment's layout
	      View view = 
	         inflater.inflate(R.layout.fragment_mission_details, container, false);               
	      setHasOptionsMenu(true); // this fragment has menu items to display

	      // get the EditTexts
	      objectiveNameTextView = (TextView) view.findViewById(R.id.objectiveNameTextView);
	      objectiveLabelTextView = (TextView) view.findViewById(R.id.objectiveLabelTextView);
	    
	      return view;
	   }
	   
	   // called when the DetailsFragment resumes
	   
	   // called when the DetailsFragment resumes
	   @Override
	   public void onResume()
	   {
	      super.onResume();
	      new LoadMissionTask().execute(rowID); // load Mission at rowID
	   } 

	   // save currently displayed Mission's row ID
	  
	   @Override
	   public void onSaveInstanceState(Bundle outState) 
	   {
	       super.onSaveInstanceState(outState);
	       outState.putLong(MainActivity.ROW_ID, rowID);
	   }

	   // display this fragment's menu items
	// display this fragment's menu items
	  
	   @Override
	   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	   {
	      super.onCreateOptionsMenu(menu, inflater);
	      inflater.inflate(R.menu.fragment_details_menu, menu);
	   }

	   // handle menu item selections
	   
	   
	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId())
	      {
	         case R.id.action_edit: 
	            // create Bundle containing Mission data to edit
	            Bundle arguments = new Bundle();
	            arguments.putLong(MainActivity.ROW_ID, rowID);
	            arguments.putCharSequence("name", objectiveNameTextView.getText());
	            arguments.putCharSequence("objective", objectiveLabelTextView.getText());
	                   
	            listener.onEditMission2(arguments); // pass Bundle to listener
	            return true;
	         case R.id.action_delete:
	            deleteMission2();
	            return true;
	      }
	      
	      return super.onOptionsItemSelected(item);
	   } 
	   
	   // performs database query outside GUI thread
	   
	   
	   
	   private class LoadMissionTask extends AsyncTask<Long, Object, Cursor> 
	   {
	      DBConnect databaseConnector = 
	         new DBConnect(getActivity());

	      // open database & get Cursor representing specified Mission's data
	      @Override
	      protected Cursor doInBackground(Long... params)
	      {
	         databaseConnector.open();
	         return databaseConnector.getOneMission(params[0]);
	      } 

	      // use the Cursor returned from the doInBackground method
	      @Override
	      protected void onPostExecute(Cursor result)
	      {
	         super.onPostExecute(result);
	         result.moveToFirst(); // move to the first item 
	   
	         // get the column index for each data item
	         int nameIndex = result.getColumnIndex("name");
	         int objectiveIndex = result.getColumnIndex("objective");
	        
	   
	         // fill TextViews with the retrieved data
	         objectiveNameTextView.setText(result.getString(nameIndex));
	         objectiveLabelTextView.setText(result.getString(objectiveIndex));
	         
	   
	         result.close(); // close the result cursor
	         databaseConnector.close(); // close database connection
	      } // end method onPostExecute
	   } // end class LoadMissionTask
	   
	   
	   // delete a Mission
	   private void deleteMission2()
	   {         
	      // use FragmentManager to display the confirmDelete DialogFragment
	      confirmDelete.show(getFragmentManager(), "confirm delete");
	   } 
	   
	   
	   private DialogFragment confirmDelete = 
			      new DialogFragment()
			      {
			         // create an AlertDialog and return it
			         @Override
			         public Dialog onCreateDialog(Bundle bundle)
			         {
			            // create a new AlertDialog Builder
			            AlertDialog.Builder builder = 
			               new AlertDialog.Builder(getActivity());
			      
			            builder.setTitle(R.string.confirm_title); 
			            builder.setMessage(R.string.confirm_message);
			      
			            // provide an OK button that simply dismisses the dialog
			            builder.setPositiveButton(R.string.button_delete,
			               new DialogInterface.OnClickListener()
			               {
			                  @Override
			                  public void onClick(
			                     DialogInterface dialog, int button)
			                  {
			                     final DBConnect databaseConnector = 
			                        new DBConnect(getActivity());
			      
			                     // AsyncTask deletes Mission and notifies listener
			                     AsyncTask<Long, Object, Object> deleteTask =
			                        new AsyncTask<Long, Object, Object>()
			                        {
			                           @Override
			                           protected Object doInBackground(Long... params)
			                           {
			                              databaseConnector.deleteMission(params[0]); 
			                              return null;
			                           } 
			      
			                           @Override
			                           protected void onPostExecute(Object result)
			                           {                                 
			                              listener.onMissionDeleted2();
			                           }
			                        }; // end new AsyncTask
			      
			                     // execute the AsyncTask to delete Mission at rowID
			                     deleteTask.execute(new Long[] { rowID });               
			                  } // end method onClick
			               } // end anonymous inner class
			            ); // end call to method setPositiveButton
			            
			            builder.setNegativeButton(R.string.button_cancel, null);
			            return builder.create(); // return the AlertDialog
			         }
			      }; // end DialogFragment anonymous inner class
}