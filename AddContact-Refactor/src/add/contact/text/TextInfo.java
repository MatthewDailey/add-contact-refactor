package add.contact.text;

/**
 * Holder for all the info for a text in the recent texts view.
 * 
 * @author Matthew
 */
public class TextInfo {
	// name of the contact (or number in the case the user
	// does not already have the contact
	private String name;
	// contents of text message (the name of new contact to 
	// be added.
	private String msg;
	
	public void setMsg(String m) {
		this.msg = m;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getName() {
		return this.name;	
	}

	public String getMsg() {
		return this.msg;
	}
	
	
}
