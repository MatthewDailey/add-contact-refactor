package add.contact.text;

import java.util.ArrayList;

import add.contact.util.Const;
import add.contact.util.Util;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Async loader to pull most recent texts.
 * 
 * @author Matthew
 */
public class LoadTexts extends AsyncTaskLoader<ArrayList<TextInfo>> {

	// save context for content resolver 
	private ContentResolver cr;
	
	public LoadTexts(Context context) {
		super(context);
		cr = context.getContentResolver();
	}

	@Override
	public ArrayList<TextInfo> loadInBackground() {
		/* order the messages in decending order by date */
		String sortOrder = "date COLLATE LOCALIZED DESC LIMIT 50" ;
		/* get only incoming messages */
		String mask = "type='1'";
		/* project over relevant columns of hte table */
		String[] projection = {"body", "address", "date" };
		/* query to get cursor over the resulting rows */

		ArrayList<TextInfo> recentTexts = new ArrayList<TextInfo>(); 
		
		try {
			Cursor c = cr.query(Const.SMS_LOCATION, projection, mask, null, sortOrder);
	
			/* add at most 50 most recent text messages and create TextInfo
			 * objects out of them to return.  */
			int cnt = 0;
			while( c.moveToNext() && cnt < 50)
			{
				try {
					TextInfo ti = new TextInfo();
					/* get the address which is the phone number then getName() to
					 * try to load the actual name of that contact if it exists 
					 * already */
					ti.setName(Util.getNameFromNumber(c.getString(c.getColumnIndex("address")), cr));
					/* simply load the body of the text */
					ti.setMsg(c.getString(c.getColumnIndex("body")));
					recentTexts.add(ti);
					cnt++;
				} catch (Exception e) {
					// Continue and do nothing. Text won't load but app won't crash.
				}
			}
		} catch (Exception e) {
			// Do nothing.
		}
		
		return recentTexts;
	}

}
