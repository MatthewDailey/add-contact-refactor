package add.contact.recent;

import java.io.InputStream;

import add.contact.R;
import add.contact.util.Util;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final ContactInfo info = getItem(position);
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null /* do not attach */);
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
		
		holder.quickContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					try {
						holder.quickContact.onClick(v);
					} catch (ActivityNotFoundException e) {
						// If quick contact fails, try the whole contact page.
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri uri = Uri.withAppendedPath(
								ContactsContract.Contacts.
								CONTENT_LOOKUP_URI, info.lookup);
						intent.setData(uri);
						context.startActivity(intent);
					}
				} catch (Exception e) {
					Util.toastMsg(context, "Sorry! Unable to load your contact.");
				}
			}
		});
		
		return convertView;
	}
	
    private Bitmap loadContactPhoto(ContentResolver cr, Uri uri) {
    	try {
    		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
    		return BitmapFactory.decodeStream(input);
    	} catch (Exception e) {
    		// Error loading image, choose the default.
    		return null;
    	}
    }
	
}
