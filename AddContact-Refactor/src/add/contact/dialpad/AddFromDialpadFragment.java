package add.contact.dialpad;

import add.contact.R;
import add.contact.util.Const;
import add.contact.util.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

/**
 * Fragment to add a contact from the dialpad directly. This fragment contains
 * a very simple UI. Two inputs labeled "name" and "number" with a check-box 
 * labeled "Send my name" as well as a save and cancel button. This will be the 
 * left most of the sliding fragments and consequently the first one the user 
 * encounters.
 * 
 * A user will enter a name and phone number into the inputs then click Save
 * to add a new contact. If the "send my name" box is checked the app will 
 * automatically send the user's name to the new number. If the user has not 
 * set a name to send when they check the box, a dialog will prompt them to do so.
 * 
 * The cancel button will close the application entirely. The app will close
 * after a contact is added successfully
 * 
 * Implements OnClickListener to catch button clicks.
 * 
 * @author Matthew Dailey
 */
public class AddFromDialpadFragment extends Fragment implements OnClickListener {

	// button to save a new contact
	private Button saveBtn;
	// button to close the app
	private Button cancelBtn;
	// check box to send name to a new contact
	private CheckBox shouldSendCheck;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.dialpad_fragment, container, false);
		
		// set buttons the be called by clicking on this framgnet
		saveBtn = (Button) layout.findViewById(R.id.saveNameBtn);
		saveBtn.setOnClickListener(this);
		cancelBtn = (Button) layout.findViewById(R.id.cancelNameBtn);
		cancelBtn.setOnClickListener(this);
		shouldSendCheck = (CheckBox) layout.findViewById(R.id.shouldSendBox);
		shouldSendCheck.setOnCheckedChangeListener(shouldSendCheckChange);
		
		return layout;
	}

	/**
	 * save the contact. make sure the name and number are valid
	 * @param v view being clicked.
	 */
	public void saveOnClick() {
		EditText nameText = (EditText) getActivity().findViewById(R.id.nameInput);
		String name = nameText.getText().toString();
		
    	// validate name input, otherwise pop up a reminder 
    	if(name.length() < 1)
    	{
    		Util.toastMsg(getActivity(), "Enter a contact name.");
    		return;
    	}
		
    	// get the phone number
		EditText phoneText = (EditText) getActivity().findViewById(R.id.numberInput);
		String phone = phoneText.getText().toString();
    	
		// check if the phone number is 10 digit int 
    	if( !Util.isInteger(phone) ) {
    		Util.toastMsg(getActivity(), 
    				"Check that phone number is an integer.");
    		return;
    	}
    	
    	// add the contact and send the message, then close the 
    	// activity. Catch any exception and alert gracefully 
    	try {
    		Util.addContact(getActivity(), phone, name);
    		
    		// attempt to send text, if no exception was thrown adding 
    		if( shouldSendCheck.isChecked() ) {
	    		try {
		    		Util.sendText(phone, getName());
		    	} catch(Exception e) {
		    		Util.toastMsg(getActivity(), "Failed to " +
		    				"send text to new contact." );
		    		Log.e("ERROR", "Failed to send text to new contact.",e);
		    	}
    		}
    	} catch(Exception e) {
    		Util.toastMsg(getActivity(), 
    				"Failed to add contact.");
    		Log.e("ERROR", "Failed to add contact", e);
    		
    	}
    	
    	// close activity
    	getActivity().finish();
	}
	
	/**
	 * Cancel adding a new contact -> close the activity
	 * @param v being clicked
	 */
	public void cancelOnClick() {
		getActivity().finish();
	}
	
    /**
     * Check to see if a name is set in the application preferences 
     * and retrieve it, otherwise return a default string.
     * 
     * @return - user name if it exist otherwise, default string NO_NAME
     */
    private String getName()
    {
    	SharedPreferences settings = getActivity().getSharedPreferences(Const.PREFS_NAME, 0);
        
        /* get the name, or a useful message */
        String name = settings.getString("name", Const.NO_NAME);
        
        return name;
    }

	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
		case R.id.saveNameBtn:
			saveOnClick();
			break;
		case R.id.cancelNameBtn:
			cancelOnClick();
			break;
		default:
			return;
		}
	}
	
	/**
	 * Override this to close the text input when we are focused on an input 
	 * but switch visible fragments
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);

	    // Make sure that we are currently visible
	    if (this.isVisible()) {
	        // If we are becoming invisible, then...
	        if (!isVisibleToUser) {
	            Log.d("MyFragment", "Not visible anymore.  Hiding keyboard.");
	            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(
	            		Context.INPUT_METHOD_SERVICE);
	            mgr.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	        } else {
	        	InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(
	        			Context.INPUT_METHOD_SERVICE);
	        	mgr.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
	        }
	    }
	}
	
	/**
	 * Custom checkbox change listener for the send my name check box. This will 
	 * give a dialog prompting the user to either launch the SetName activity to
	 * set a name to send. If the user chooses not to launch the activity, the box
	 * will uncheck itself.
	 */
	private OnCheckedChangeListener shouldSendCheckChange = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, 
				boolean isChecked) {
			/* if the user is trying to send message, and no name is set
			 * ask them to set one. */
			if(isChecked && getName().equals(Const.NO_NAME))
			{
		    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    	
		    	/* create intent to start the name setting activity */
		    	final Intent i = new Intent();
		    	i.setClass(getActivity(), SetName.class);
		    	
		    	/* set up alert builder */
		    	builder.setMessage("There is no name associated with this app. " +
		    			"Would you like to add one?")
		    	       .setCancelable(false)
		    	       /* add a yes button */
		    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		    	       {
		    	           public void onClick(DialogInterface dialog, int id) 
		    	           {
		    	                dialog.cancel();
		    	                /* launch the set name activity */
		    	                startActivity(i);
		    	           }
		    	       })
		    	       /* add a no button */
		    	       .setNegativeButton("No", new DialogInterface.OnClickListener() 
		    	       {
		    	           public void onClick(DialogInterface dialog, int id) 
		    	           {
		    	        	   /* change the button back to off and leave dialog */
		    	        	   ((CheckBox)getActivity().findViewById(
		    	        			   R.id.shouldSendBox)).setChecked(false);
		    	                dialog.cancel();
		    	           }
		    	       });
		    	
		    	/* build and show the alert */
		    	AlertDialog alert = builder.create();
		    	alert.show();
			}
		}
	};
	
	
}
