package add.contact.text;

import add.contact.R;
import add.contact.util.Util;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddFromTextDialog extends DialogFragment {
	
	private String name;
	private String msg;
	
	public static AddFromTextDialog create(String name, String msg) {
		AddFromTextDialog dialog = new AddFromTextDialog();
		dialog.setName(name);
		dialog.setMsg(msg);
		return dialog;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add contact?");
		builder.setPositiveButton("Save", null);
		builder.setNegativeButton("Cancel", null);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogContent = inflater.inflate(R.layout.add_from_text_dialog, null);
		builder.setView(dialogContent);
		
		final EditText edit = (EditText) dialogContent.findViewById(R.id.name);
		edit.setText(msg);
		
		final AlertDialog d =  builder.create();
		d.setOnShowListener(new DialogInterface.OnShowListener() {
		    @Override
		    public void onShow(DialogInterface dialog) {

		        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
						Log.i("DIALOG", "Clicked ok.");
						msg = edit.getText().toString();
						/* try to add contact and alert user if 
						 * we fail with runtime. */
						try {
							Util.addContact(getActivity(), 
									name, msg );
							Util.toastMsg(getActivity(), 
									"Added Contact: "+msg);
							getActivity().finish();

						} catch(Exception e) {
							Util.toastMsg(getActivity(),
									"Failed to add contact.");
						}
						
		                //Dismiss once everything is OK.
		                d.dismiss();
		            }
		        });
		    }});
		return d;
	}
}
