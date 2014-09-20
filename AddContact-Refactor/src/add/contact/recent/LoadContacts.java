package add.contact.recent;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
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

    /* get important columns */
    private static final String[] projection =
        {
            Contacts._ID,
            Contacts.LOOKUP_KEY,
            (Build.VERSION.SDK_INT >=
             Build.VERSION_CODES.HONEYCOMB) ?
                    Contacts.DISPLAY_NAME_PRIMARY :
                    Contacts.DISPLAY_NAME,
            (Build.VERSION.SDK_INT >=
             Build.VERSION_CODES.HONEYCOMB) ?
                    Contacts.PHOTO_THUMBNAIL_URI :
                    /*
                     * Although it's not necessary to include the
                     * column twice, this keeps the number of
                     * columns the same regardless of version
                     */
                    Contacts._ID
        };
	
    /*
     * As a shortcut, defines constants for the
     * column indexes in the Cursor. The index is
     * 0-based and always matches the column order
     * in the projection.
     */
    // Column index of the _ID column
    private int idIndex = 0;
    // Column index of the LOOKUP_KEY column
    private int lookupKeyIndex = 1;
    // Column index of the display name column
    private int displayNameIndex = 2;
    /*
     * Column index of the photo data column.
     * It's PHOTO_THUMBNAIL_URI for Honeycomb and later,
     * and _ID for previous versions.
     */
    private int photoDataIndex =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            3 :
            0;
    
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
		
		// check there are actually results.
		if (c != null) {
			int count = 1;
			try {
				// parse the returned values to ContactInfo to display
				while(c.moveToNext()) {
					String name = c.getString(displayNameIndex);
					String key = c.getString(lookupKeyIndex);
					long id = c.getLong(idIndex);
					String photoUri = c.getString(photoDataIndex);
					if( !keys.contains(key) ) {
						results.add(new ContactInfo(name, key, id, photoUri, count));
						count++;
						keys.add(key);
					}
				}
			} finally {
				c.close();
			}
		}
		
		return results;
	}
	
}