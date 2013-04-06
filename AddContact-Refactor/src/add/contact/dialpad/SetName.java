package add.contact.dialpad;

import add.contact.R;
import add.contact.util.Const;
import add.contact.util.Util;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * SetName
 * @author Matt
 *
 * This activity allows the user to set the name which will be sent to
 * new contacts added through the dialpad.
 * 
 * The layout is a simple text input with a button to submit. A pop up 
 * will alert the user that the name has been successfully set and 
 * remind them of the set value.
 * 
 * This is called by the toggle button if there is no set name and the 
 * set name button on the home page.
 */
public class SetName extends Activity implements OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/* establish the UI */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);
        
        /* attempt to find and set the users name */
        EditText name_input = (EditText) this.findViewById(R.id.nameInput);
        String name_attempt = Util.getUserName(getContentResolver());
        if( name_attempt != null && !name_attempt.equals("") )
        {
        	TextView found_name_msg = (TextView) this.findViewById(R.id.found_name_msg);
        	found_name_msg.setText(getString(R.string.found_name ));
        	name_input.setText(name_attempt);
        }
        
        /* set the onclick handler */
        Button setName = (Button) this.findViewById(R.id.saveNameBtn);
        setName.setOnClickListener(this);
        Button cancelBtn = (Button) this.findViewById(R.id.cancelNameBtn);
        cancelBtn.setOnClickListener(this);
    }
    
    /**
     * save the name we want to send to the preferences file
     */
    private void save() {
		String new_name = ((EditText) SetName.this.findViewById(
				R.id.nameInput)).getText().toString();
		
		/* if they have selected to have no name, reset to const */
		if(new_name.equals("")) {
			new_name = Const.NO_NAME;
			Util.toastMsg(SetName.this, "Enter a name.");
			return;
		} else {
			/* access the preference, edit and commit new pref */
	    	SharedPreferences settings = getSharedPreferences(
	    			Const.PREFS_NAME, 0);
	    	Editor pref_editor = settings.edit();
	    	pref_editor.putString("name", new_name);
	    	pref_editor.apply();
	    	
	    	/* tell the user the name that was set */
	    	Util.toastMsg(SetName.this, "Name set to "+ 
	    	new_name +".");
	    	/* everything done, close the app */
	    	finish();
		}
	}
    
	@Override
	public void onClick(View v) {
		switch( v.getId() ) {
		case R.id.cancelNameBtn:
			finish();
			break;
		case R.id.saveNameBtn:
			save();
			break;
		}
	}    	
}