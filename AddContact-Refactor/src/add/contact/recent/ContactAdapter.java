package add.contact.recent;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import add.contact.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.util.Log;
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
	
    /**
     * Load a contact photo thumbnail and return it as a Bitmap,
     * resizing the image to the provided image dimensions as needed.
     * @param photoData photo ID Prior to Honeycomb, the contact's _ID value.
     * For Honeycomb and later, the value of PHOTO_THUMBNAIL_URI.
     * @return A thumbnail Bitmap, sized to the provided width and height.
     * Returns null if the thumbnail is not found.
     */
    private Bitmap loadContactPhotoThumbnail(String photoData) {
        // Creates an asset file descriptor for the thumbnail file.
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri;
            // If Android 3.0 or later
            if (Build.VERSION.SDK_INT
                    >=
                Build.VERSION_CODES.HONEYCOMB) {
                // Sets the URI from the incoming PHOTO_THUMBNAIL_URI
                thumbUri = Uri.parse(photoData);
            } else {
            // Prior to Android 3.0, constructs a photo Uri using _ID
                /*
                 * Creates a contact URI from the Contacts content URI
                 * incoming photoData (_ID)
                 */
                final Uri contactUri = Uri.withAppendedPath(
                        Contacts.CONTENT_URI, photoData);
                /*
                 * Creates a photo URI by appending the content URI of
                 * Contacts.Photo.
                 */
                thumbUri =
                        Uri.withAppendedPath(
                                contactUri, Photo.CONTENT_DIRECTORY);
            }
    
        /*
         * Retrieves an AssetFileDescriptor object for the thumbnail
         * URI
         * using ContentResolver.openAssetFileDescriptor
         */
        afd = context.getContentResolver().
                openAssetFileDescriptor(thumbUri, "r");
        /*
         * Gets a file descriptor from the asset file descriptor.
         * This object can be used across processes.
         */
        FileDescriptor fileDescriptor = afd.getFileDescriptor();
        // Decode the photo file and return the result as a Bitmap
        // If the file descriptor is valid
        if (fileDescriptor != null) {
            // Decodes the bitmap
            return BitmapFactory.decodeFileDescriptor(
                    fileDescriptor, null, null);
            }
        // If the file isn't found
        } catch (FileNotFoundException e) {
            /*
             * Handle file not found errors
             */
        	Log.i("ContactAdapter", "Filenot found");
        // In all cases, close the asset file descriptor
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }
}
