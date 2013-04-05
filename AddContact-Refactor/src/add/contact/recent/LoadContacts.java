package add.contact.recent;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Async loader to pull recently added contacts. The loader returns a list
 * of ContactInfo objects which the list adapter in the fragment will 
 * render. Any error will result in returning an empty list.
 * 
 * Load contacts will dedupe the list of contacts based on the lookup key
 * so that only one contact will be shown for a given key. This is because we pull 
 * a large number of contacts and some contact rows may point to the same contact
 * page. 
 *
 * Since android does not store the add date for contacts we use the row order
 * to estimate. It is reasonable to assume the android adds new contacts by 
 * appending rows to the contacts table so we use this as an ordering.
 * 
 * @author Matthew
 */
public class LoadContacts extends AsyncTaskLoader<ArrayList<ContactInfo>>{
	// save the context to get a content resolver
	Context ctx;
	
	public LoadContacts(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public ArrayList<ContactInfo> loadInBackground() {
		Log.i("RecentContacts", "Entering onCreateLoader");
		ArrayList<ContactInfo> results = new ArrayList<ContactInfo>();
		HashSet<String> keys = new HashSet<String>();
		
		/* create uri to look up contacts */
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        /* get important columns */
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.LOOKUP_KEY
        };
        
        /* make sure we only get visible contacts */
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP 
        		+ " = '1'";
        String[] selectionArgs = null;
        /* sort by id in descending order */
        String sortOrder = ContactsContract.Contacts._ID +
        		" COLLATE LOCALIZED DESC";
        
        // perform query
		Cursor c = ctx.getContentResolver().query(uri, projection, selection, 
				selectionArgs, sortOrder);
		
		int count = 1;
		try {
			// parse the returned values to ContactInfo to display
			while(c.moveToNext()) {
				String name = c.getString(c.getColumnIndex(
						ContactsContract.Contacts.DISPLAY_NAME));
				String key = c.getString(c.getColumnIndex(
						ContactsContract.Contacts.LOOKUP_KEY));
				if( !keys.contains(key) ) {
					results.add(new ContactInfo(name, key, count));
					count++;
					keys.add(key);
				}
			}
		} finally {
			c.close();
		}
		
		return results;
	}
	
}