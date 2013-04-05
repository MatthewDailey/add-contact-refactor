package add.contact.recent;

/**
 * Holder for all info for a contact info row in the most recently added
 * contacts list.
 * 
 * @author Matthew
 */
public class ContactInfo {
	// the index in most-recently-added order
	int index;
	// contacts name
	String name;
	// lookup key to look up a contact and launch the contact activity
	String lookup;
	
	public ContactInfo(String n, String l, int index) {
		name = n;
		lookup = l;
		this.index = index;
	}
	
	/**
	 * toString is called by the list adapter to choose the text to display
	 * in the recent contacts list. In this case it is the index and the
	 * name
	 */
	@Override
	public String toString() {
		return index + ". " + name;
	}
}
