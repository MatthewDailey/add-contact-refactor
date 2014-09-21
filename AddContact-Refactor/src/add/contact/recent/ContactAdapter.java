package add.contact.recent;

import java.io.InputStream;

import add.contact.R;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<ContactInfo> {

	private final LayoutInflater inflater;
	private final Context context;
	
	public ContactAdapter(Context context) {
		super(context, 0);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	private class ViewHolder {
		TextView displayName;
		QuickContactBadge quickContact;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ContactInfo info = getItem(position);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.displayName = (TextView) convertView.findViewById(
					R.id.displayname);
			holder.quickContact = (QuickContactBadge) convertView.findViewById(
					R.id.quickcontact);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.displayName.setText(info.name);
		
		final Uri contactUri = Contacts.getLookupUri(info.id, info.lookup);
		holder.quickContact.assignContactUri(contactUri);
		Bitmap bm = null;
		if (info.photoUri != null) {
			bm = loadContactPhoto(context.getContentResolver(), contactUri);
		}
		if (bm != null) {
			holder.quickContact.setImageBitmap(bm);
		} else {
			holder.quickContact.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_action_happy));
		}
		
		return convertView;
	}
	
	public void handleClick(View clickedView) {
		ViewHolder holder = (ViewHolder) clickedView.getTag();
		holder.quickContact.onClick(holder.quickContact);
	}
	
    private Bitmap loadContactPhoto(ContentResolver cr, Uri uri) {
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }
	
}
