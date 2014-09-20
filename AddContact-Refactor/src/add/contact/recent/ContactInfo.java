package add.contact.recent;

/**
 * Holder for all info for a contact info row in the most recently added
 * contacts list.
 * 
 * @author Matthew
 */
public class ContactInfo {
	// the index in most-recently-added order
	final int index;
	// row id
	final long id;
	// contacts name
	final String name;
	// lookup key to look up a contact and launch the contact activity
	final String lookup;
	// string to lookup photo
	final String photoUri;
	
	public ContactInfo(String n, String l, long id, String photo, int index) {
		this.name = n;
		this.lookup = l;
		this.id = id;
		this.index = index;
		this.photoUri = photo;
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
