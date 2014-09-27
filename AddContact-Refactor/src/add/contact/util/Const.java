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
	
	public static final String DOSSIER_PACKAGE = "basic.dossier";
	
	
	public static final String[] COUNTRIES = new String[]{"US","GB","IN","DE","CA","ZA","NL","LB","AT",
		"AF","AX","AL","DZ","AS","AD","AO","AI","AQ","AG"
		,"AR","AM","AW","AU","AZ","BS","BH","BD","BB","BY","BE","BZ","BJ","BM","BT","BO","BA","BW",
		"BV","BR","IO","BN","BG","BF","BI","KH","CM","CV","KY","CF","TD","CL","CN","CX","CC","CO","KM",
		"CG","CD","CK","CR","CI","HR","CU","CY","CZ","DK","DJ","DM","DO","EC","EG","SV","GQ","ER","EE","ET",
		"FK","FO","FJ","FI","FR","GF","PF","TF","GA","GM","GE","GH","GI","GR","GL","GD","GP","GU","GT",
		"GG","GN","GW","GY","HT","HM","VA","HN","HK","HU","IS","IN","ID","IR","IQ","IE","IM","IL","IT","JM",
		"JP","JE","JO","KZ","KE","KI","KP","KR","KW","KG","LA","LV","LS","LR","LY","LI","LT","LU","MO",
		"MK","MG","MW","MY","MV","ML","MT","MH","MQ","MR","MU","YT","MX","FM","MD","MC","MN","ME","MS","MA",
		"MZ","MM","NA","NR","NP","AN","NC","NZ","NI","NE","NG","NU","NF","MP","NO","OM","PK","PW","PS",
		"PA","PG","PY","PE","PH","PN","PL","PT","PR","QA","RE","RO","RU","RW","SH","KN","LC","PM","VC","WS",
		"SM","ST","SA","SN","RS","SC","SL","SG","SK","SI","SB","SO","GS","ES","LK","SD","SR","SJ","SZ",
		"SE","CH","SY","TW","TJ","TZ","TH","TL","TG","TK","TO","TT","TN","TR","TM","TC","TV","UG","UA","AE",
		"UM","UY","UZ","VU","VE","VN","VG","VI","WF","EH","YE","ZM","ZW"};
}
