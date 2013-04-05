package add.contact.util;

import android.net.Uri;

public class Const {
	
	/* name of preference file to look up chosen name to send */
    public static final String PREFS_NAME = "NameFile";
    /* message to send if no name is set */
    public static final String NO_NAME = "No name is set in Add Contact but " +
    		"here is my number";
	/* uri location of the sms message on the phone.
	 * note that this is not standardized and there is no standard API
	 * for handling SMS message so this may not be correct. 	 */
	public static final Uri SMS_LOCATION = Uri.parse("content://sms");
}
