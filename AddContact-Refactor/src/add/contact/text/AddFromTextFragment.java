package add.contact.text;

import java.util.ArrayList;

import add.contact.util.Const;
import add.contact.util.Util;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

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
	
	TextAdapter adapter;
	
	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated(savedInstanceState);
		// set the adapter
		adapter = new TextAdapter(getActivity());
		setListAdapter( adapter );
		
		// show loading animation
		setListShown(false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(0, null, this).forceLoad();
	}
	

	@Override
	public Loader<ArrayList<TextInfo>> onCreateLoader(int arg0, Bundle arg1) {
		return new LoadTexts( getActivity() );
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<TextInfo>> arg0, 
			ArrayList<TextInfo> results) {
		adapter.clear();
		adapter.addAll(results);
		
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
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
		
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		boolean isPhoneNum = false;
		for( int i = 0; i < Const.COUNTRIES.length && !isPhoneNum; i++ )
		{
			isPhoneNum |= phoneUtil.isPossibleNumber(selectedText.getName(), 
					Const.COUNTRIES[i]);
		}
		
		/* check if the name is actually a number of the name of
		 * a preexisting contact */
		if( isPhoneNum ) {
			//queryCorrectContact(selectedText.getName(), selectedText.getMsg());
			AddFromTextDialog dialog = AddFromTextDialog.create(
					selectedText.getName(), selectedText.getMsg());
			dialog.show(getFragmentManager(), "add_from_text_dialog");
		} else {
			Util.toastMsg(getActivity(),"Contact already exists: "+
						selectedText.getName());
		}
		
	}  

}
