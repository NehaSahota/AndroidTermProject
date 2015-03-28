package com.example.sherlock;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentTransaction;

public class MainActivity extends Activity 
   implements MissionListFragment.MissionListFragmentListener,
      DetailsFragment.DetailsFragmentListener, 
      AddEditDetailsFragment.AddEditFragmentListener
{
   // keys for storing row ID in Bundle passed to a fragment
   public static final String ROW_ID = "row_id"; 
   
   MissionListFragment missionListFragment; // displays mission list
   
   // display missionListFragment when MainActivity first loads
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // return if Activity is being restored, no need to recreate GUI
      if (savedInstanceState != null) 
         return;

      // check whether layout contains fragmentContainer (phone layout);
      // missionListFragment is always displayed
      if (findViewById(R.id.fragmentContainer) != null) 
      {
         // create missionListFragment
         missionListFragment = new MissionListFragment();
         
         // add the fragment to the FrameLayout
         FragmentTransaction transaction = 
            getFragmentManager().beginTransaction();
         transaction.add(R.id.fragmentContainer, missionListFragment);
         transaction.commit(); // causes missionListFragment to display
      }
   }
   
   // called when MainActivity resumes
   @Override
   protected void onResume()
   {
      super.onResume();
      
      // if missionListFragment is null, activity running on tablet, 
      // so get reference from FragmentManager
      if (missionListFragment == null)
      {
         missionListFragment = 
            (MissionListFragment) getFragmentManager().findFragmentById(
               R.id.missionListFragment);      
      }
   }
   
   // display DetailsFragment for selected mission
   @Override
   public void onMissionSelected(long rowID)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displayMission(rowID, R.id.fragmentContainer);
      else // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         displayMission(rowID, R.id.rightPaneContainer);
      }
   }

   // display a mission
   private void displayMission(long rowID, int viewID)
   {
      DetailsFragment detailsFragment = new DetailsFragment();
      
      // specify rowID as an argument to the DetailsFragment
      Bundle arguments = new Bundle();
      arguments.putLong(ROW_ID, rowID);
      detailsFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the DetailsFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, detailsFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes DetailsFragment to display
   }
   
   // display the AddEditFragment to add a new mission
   @Override
   public void onAddMission()
   {
      if (findViewById(R.id.fragmentContainer) != null)
         displayAddEditFragment(R.id.fragmentContainer, null); 
      else
         displayAddEditFragment(R.id.rightPaneContainer, null);
   }
   
   // display fragment for adding a new or editing an existing mission
   private void displayAddEditFragment(int viewID, Bundle arguments)
   {
      AddEditDetailsFragment addEditFragment = new AddEditDetailsFragment();
      
      if (arguments != null) // editing existing mission
         addEditFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the AddEditFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, addEditFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes AddEditFragment to display
   }
   
   // return to mission list when displayed mission deleted
   @Override
   public void onMissionDeleted()
   {
      getFragmentManager().popBackStack(); // removes top of back stack
      
      if (findViewById(R.id.fragmentContainer) == null) // tablet
         missionListFragment.updateMissionList();
   }

   // display the AddEditFragment to edit an existing mission
   @Override
   public void onEditMission(Bundle arguments)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displayAddEditFragment(R.id.fragmentContainer, arguments); 
      else // tablet
         displayAddEditFragment(R.id.rightPaneContainer, arguments);
   }

   // update GUI after new mission or updated mission saved
   @Override
   public void onAddEditCompleted(long rowID)
   {
      getFragmentManager().popBackStack(); // removes top of back stack

      if (findViewById(R.id.fragmentContainer) == null) // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         missionListFragment.updateMissionList(); // refresh missions

         // on tablet, display mission that was just added or edited
         displayMission(rowID, R.id.rightPaneContainer); 
      }
   }   
}


