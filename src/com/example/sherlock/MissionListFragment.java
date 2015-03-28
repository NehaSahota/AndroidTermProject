package com.example.sherlock;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MissionListFragment extends ListFragment
{
   // callback methods implemented by MainActivity  
   public interface MissionListFragmentListener
   {
      // called when user selects a Mission
      public void onMissionSelected(long rowID);

      // called when user decides to add a Mission
      public void onAddMission();
   }
   
   private MissionListFragmentListener listener; 
   
   private ListView missionListView; // the ListActivity's ListView
   private CursorAdapter missionAdapter; // adapter for ListView
   
   // set MissionListFragmentListener when fragment attached   
   @Override
   public void onAttach(Activity activity)
   {
      super.onAttach(activity);
      listener = (MissionListFragmentListener) activity;
   }

   // remove MissionListFragmentListener when Fragment detached
   @Override
   public void onDetach()
   {
      super.onDetach();
      listener = null;
   }

   // called after View is created
   @Override
   public void onViewCreated(View view, Bundle savedInstanceState)
   {
      super.onViewCreated(view, savedInstanceState);
      setRetainInstance(true); // save fragment across config changes
      setHasOptionsMenu(true); // this fragment has menu items to display

      // set text to display when there are no Missions
      setEmptyText(getResources().getString(R.string.no_missions));

      // get ListView reference and configure ListView
      missionListView = getListView(); 
      missionListView.setOnItemClickListener(viewMissionListener);      
      missionListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      
      // map each Mission's name to a TextView in the ListView layout
      String[] from = new String[] { "name" };
      int[] to = new int[] { android.R.id.text1 };
      missionAdapter = new SimpleCursorAdapter(getActivity(), 
         android.R.layout.simple_list_item_1, null, from, to, 0);
      setListAdapter(missionAdapter); // set adapter that supplies data
   }

   // responds to the user touching a Mission's name in the ListView
   OnItemClickListener viewMissionListener = new OnItemClickListener() 
   {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, 
         int position, long id) 
      {
         listener.onMissionSelected(id); // pass selection to MainActivity
      } 
   }; // end viewMissionListener

   // when fragment resumes, use a GetMissionsTask to load Missions 
   @Override
   public void onResume() 
   {
      super.onResume(); 
      new GetMissionsTask().execute((Object[]) null);
   }

   // performs database query outside GUI thread
   private class GetMissionsTask extends AsyncTask<Object, Object, Cursor> 
   {
      DBConnect databaseConnector = 
         new DBConnect(getActivity());

      // open database and return Cursor for all Missions
      @Override
      protected Cursor doInBackground(Object... params)
      {
         databaseConnector.open();
         return databaseConnector.getAllMissions(); 
      } 

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         missionAdapter.changeCursor(result); // set the adapter's Cursor
         databaseConnector.close();
      } 
   } // end class GetMissionsTask

   // when fragment stops, close Cursor and remove from MissionAdapter 
   @Override
   public void onStop() 
   {
      Cursor cursor = missionAdapter.getCursor(); // get current Cursor
      missionAdapter.changeCursor(null); // adapter now has no Cursor
      
      if (cursor != null) 
         cursor.close(); // release the Cursor's resources
      
      super.onStop();
   } 

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_mission_list_menu, menu);
   }

   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId())
      {
         case R.id.action_add:
            listener.onAddMission();
            return true;
      }
      
      return super.onOptionsItemSelected(item); // call super's method
   }
   
   // update data set
   public void updateMissionList()
   {
      new GetMissionsTask().execute((Object[]) null);
   }
} // end class MissionListFragment


