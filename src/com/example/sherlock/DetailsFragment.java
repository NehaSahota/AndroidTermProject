package com.example.sherlock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
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

public class DetailsFragment extends Fragment
{
   // callback methods implemented by MainActivity  
   public interface DetailsFragmentListener
   {
      // called when a Mission is deleted
      public void onMissionDeleted();
      
      // called to pass Bundle of Mission's info for editing
      public void onEditMission(Bundle arguments);
   }
   
   private DetailsFragmentListener listener;
   
   private long rowID = -1; // selected Mission's rowID
   private TextView nameTextView; // displays Mission's name 
   private TextView heightTextView; // displays Mission's height
   private TextView genderTextView; // displays Mission's gender
   private TextView haircolorTextView; // displays Mission's haircolor
   private TextView phoneTextView; // displays Mission's phone
   private TextView ageTextView; // displays Mission's age
   private TextView notesTextView; // displays Mission's notes
   
   // set DetailsFragmentListener when fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (DetailsFragmentListener) activity;
   }
   
   // remove DetailsFragmentListener when fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null;
   }

   // called when DetailsFragmentListener's view needs to be created
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
         inflater.inflate(R.layout.fragment_details, container, false);               
      setHasOptionsMenu(true); // this fragment has menu items to display

      // get the EditTexts
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      heightTextView = (TextView) view.findViewById(R.id.heightTextView);
      genderTextView = (TextView) view.findViewById(R.id.genderTextView);
      haircolorTextView = (TextView) view.findViewById(R.id.haircolorTextView);
      phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
      ageTextView = (TextView) view.findViewById(R.id.ageTextView);
      notesTextView = (TextView) view.findViewById(R.id.notesTextView);
      return view;
   }
   
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
            arguments.putCharSequence("name", nameTextView.getText());
            arguments.putCharSequence("height", heightTextView.getText());
            arguments.putCharSequence("gender", genderTextView.getText());
            arguments.putCharSequence("haircolor", haircolorTextView.getText());
            arguments.putCharSequence("phone", phoneTextView.getText());
            arguments.putCharSequence("age", ageTextView.getText());
            arguments.putCharSequence("notes", notesTextView.getText());            
            listener.onEditMission(arguments); // pass Bundle to listener
            return true;
         case R.id.action_delete:
            deleteMission();
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
         int heightIndex = result.getColumnIndex("height");
         int genderIndex = result.getColumnIndex("gender");
         int haircolorIndex = result.getColumnIndex("haircolor");
         int phoneIndex = result.getColumnIndex("phone");
         int ageIndex = result.getColumnIndex("age");
         int notesIndex = result.getColumnIndex("notes");
   
         // fill TextViews with the retrieved data
         nameTextView.setText(result.getString(nameIndex));
         heightTextView.setText(result.getString(heightIndex));
         genderTextView.setText(result.getString(genderIndex));
         haircolorTextView.setText(result.getString(haircolorIndex));
         phoneTextView.setText(result.getString(phoneIndex));
         ageTextView.setText(result.getString(ageIndex));
         notesTextView.setText(result.getString(notesIndex));
   
         result.close(); // close the result cursor
         databaseConnector.close(); // close database connection
      } // end method onPostExecute
   } // end class LoadMissionTask

   // delete a Mission
   private void deleteMission()
   {         
      // use FragmentManager to display the confirmDelete DialogFragment
      confirmDelete.show(getFragmentManager(), "confirm delete");
   } 

   // DialogFragment to confirm deletion of Mission
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
                              listener.onMissionDeleted();
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
} // end class DetailsFragment

