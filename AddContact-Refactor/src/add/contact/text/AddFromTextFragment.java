package add.contact.text;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import add.contact.text.LoadTexts;
import add.contact.util.Util;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

/**
 * Fragment view for adding a new contact from a recent text message. The
 * fragment displays a list of recent texts. The texts are displayed with
 * either the number or name (if the user already has a contact with this 
 * number) and the contents of the message.
 * 
 * When a user clicks a text, if the number is not already a contact, they
 * will be prompted if they want to add a new contact with the number of 
 * the clicked text and the name the content of the message. This is useful
 * if a person texts you their name. 
 * 
 * Implements LoaderCallbacks to allow async load of texts
 * 
 * @author Matthew
 *
 */
public class AddFromTextFragment extends ListFragment implements
		LoaderCallbacks<ArrayList<TextInfo>> {
	
	CustomTextBaseAdapter adapter;
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated(savedInstanceState);

		// show loading animation
		setListShown(false);
		
		// set the adapter
		adapter = new CustomTextBaseAdapter(getActivity());
		setListAdapter( adapter );

		// start loading
		getLoaderManager().initLoader(0, null, this).forceLoad();
	}
	

	@Override
	public Loader<ArrayList<TextInfo>> onCreateLoader(int arg0, Bundle arg1) {
		return new LoadTexts( getActivity() );
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<TextInfo>> arg0, 
			ArrayList<TextInfo> results) {
		adapter.setList(results);
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<TextInfo>> arg0) {
		// TODO Auto-generated method stub
	}

    /**
     * Build the on click listener to check if the name of the 
     * text is a contact name or a phone number so we only add
     * new contacts. 
     */
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		/* get TextInfo object represented by the list item */
		TextInfo selectedText = (TextInfo) adapter.getItem(position);
		
		/* check if the name is actually a number of the name of
		 * a preexisting contact */
		if( Util.isInteger(selectedText.getName()) ) {
			queryCorrectContact(selectedText.getName(), selectedText.getMsg());
		} else {
			Util.toastMsg(getActivity(),"Contact already exists: "+
						selectedText.getName());
		}
		
	}  
	
   	/**
	 * method to double check that the user selected the desired 
	 * text.
	 */
    private void queryCorrectContact(final String name,	final String msg) {
    	/* create an alert dialog */
    	AlertDialog.Builder builder = new AlertDialog.
    			Builder(getActivity());
    	
    	/* build the alert */
    	builder.setMessage("Add contact "+ msg +"?")
    	       .setCancelable(false)
    	       /* yes-button code */
    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	    	   /* user selected the correct contact so we
    	    	    * add the contact and pop a toast message.
    	    	    * then close the activity.
    	    	    */
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   /* try to add contact and alert user if 
    	        	    * we fail with runtime. */
    	        	   try {
    	        		   Util.addContact(getActivity(), 
   	        					name, msg );
    	        		   /* try to send text and alert user
    	        		    * if we fail.    */
    	        		   try {
    	        			   Util.toastMsg(getActivity(), 
   	        					"Added Contact: "+msg);
    	        		   } catch(Exception e) {
    	        			   Util.toastMsg(getActivity(),
    	        					 "Failed to send name to" +
    	        					   "new contact.");
    	        		   }
    	        	   } catch(Exception e) {
    	        		   Util.toastMsg(getActivity(),
    	        				   "Failed to add contact.");
    	        	   }
    	        	   getActivity().finish();
    	        	   dialog.cancel();
    	           }
    	       })
    	       /* no-button code */
    	       .setNegativeButton("No", new DialogInterface.
    	    		   OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   /* change the button back to off and leave 
    	        	    * dialog */
    	                dialog.cancel();
    	           }
    	       });
    	
    	/* Build the alert and show it */
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
