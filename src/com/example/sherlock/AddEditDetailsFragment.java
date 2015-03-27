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

public class AddEditDetailsFragment extends Fragment{
	
	
	 public interface AddEditFragmentListener
	   {
	      
	      public void onAddEditCompleted(long rowID);
	   }
	   
	   private AddEditFragmentListener listener; 
	   
	   private long rowID; 
	   private Bundle suspectInfoBundle; 

	  
	   private EditText nameEditText;
	   private EditText heightEditText;
	   private EditText writerEditText;
	   private EditText genderEditText;
	   private EditText haircolorEditText;
	   private EditText ageEditText;
	   private EditText notesEditText;

	 
	   @Override
	   public void onAttach(Activity activity)
	   {
	      super.onAttach(activity);
	      listener = (AddEditFragmentListener) activity; 
	   }

	
	   @Override
	   public void onDetach()
	   {
	      super.onDetach();
	      listener = null; 
	   }
	   
	
	   @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState)
	   {
	      super.onCreateView(inflater, container, savedInstanceState);    
	      setRetainInstance(true); 
	      setHasOptionsMenu(true); 
	      
	   
	      View view = 
	         inflater.inflate(R.layout.fragment_add_edit, container, false);
	      nameEditText = (EditText) view.findViewById(R.id.nameEditText);
	      heightEditText = (EditText) view.findViewById(R.id.heightEditText);
	     
	      genderEditText = (EditText) view.findViewById(R.id.genderEditText);
	      haircolorEditText = (EditText) view.findViewById(R.id.haircolorEditText);
	      ageEditText = (EditText) view.findViewById(R.id.ageEditText);
	      notesEditText = (EditText) view.findViewById(R.id.notesEditText);

	      suspectInfoBundle = getArguments(); 

	      if (suspectInfoBundle != null)
	      {
	        /* rowID = suspectInfoBundle.getLong(MainActivity.ROW_ID);*/
	         nameEditText.setText(suspectInfoBundle.getString("name"));  
	         heightEditText.setText(suspectInfoBundle.getString("director"));  
	         writerEditText.setText(suspectInfoBundle.getString("writer"));  
	         genderEditText.setText(suspectInfoBundle.getString("actor"));  
	         haircolorEditText.setText(suspectInfoBundle.getString("actress"));  
	         ageEditText.setText(suspectInfoBundle.getString("genre"));  
	         notesEditText.setText(suspectInfoBundle.getString("year"));  
	      } 
	      
	
	      Button saveMovieButton = 
	         (Button) view.findViewById(R.id.saveSuspectButton);
	      saveMovieButton.setOnClickListener(saveMovieButtonClicked);
	      return view;
	   }

	 
	   OnClickListener saveMovieButtonClicked = new OnClickListener() 
	   {
	      @Override
	      public void onClick(View v) 
	      {
	         if (nameEditText.getText().toString().trim().length() != 0)
	         {
	           
	            AsyncTask<Object, Object, Object> saveContactTask = 
	               new AsyncTask<Object, Object, Object>() 
	               {
	                  @Override
	                  protected Object doInBackground(Object... params) 
	                  {
	                     saveContact(); 
	                     return null;
	                  } 
	      
	                  @Override
	                  protected void onPostExecute(Object result) 
	                  {
	                     
	                     InputMethodManager imm = (InputMethodManager) 
	                        getActivity().getSystemService(
	                           Context.INPUT_METHOD_SERVICE);
	                     imm.hideSoftInputFromWindow(
	                        getView().getWindowToken(), 0);

	                     listener.onAddEditCompleted(rowID);
	                  } 
	               }; 
	               
	          
	            saveContactTask.execute((Object[]) null); 
	         } 
	         else 
	         {
	            DialogFragment errorSaving = 
	               new DialogFragment()
	               {
	                  @Override
	                  public Dialog onCreateDialog(Bundle savedInstanceState)
	                  {
	                     AlertDialog.Builder builder = 
	                        new AlertDialog.Builder(getActivity());
	                     builder.setMessage(R.string.error_message);
	                     builder.setPositiveButton(R.string.ok, null);                     
	                     return builder.create();
	                  }               
	               };
	            
	            errorSaving.show(getFragmentManager(), "error saving movie");
	         } 
	      } 
	   }; 

	 
	   private void saveContact() 
	   {
	     
	      DBConnect DBConnect = 
	         new DBConnect(getActivity());

	      if (suspectInfoBundle == null)
	      {
	         
	         rowID = DBConnect.insertSuspect(
	            nameEditText.getText().toString(),
	            heightEditText.getText().toString(), 
	            writerEditText.getText().toString(), 
	            genderEditText.getText().toString(),
	            haircolorEditText.getText().toString(), 
	            ageEditText.getText().toString(), 
	            notesEditText.getText().toString());
	      } 
	      else
	      {
	         DBConnect.updateMovie(rowID,
	            nameEditText.getText().toString(),
	            heightEditText.getText().toString(), 
	            writerEditText.getText().toString(), 
	            genderEditText.getText().toString(),
	            haircolorEditText.getText().toString(), 
	            ageEditText.getText().toString(), 
	            notesEditText.getText().toString());
	      }
	   } 
}
