package add.contact.recent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * fragment displaying recently added contacts page of the app. The page is a 
 * enumerated list of contact names. Clicking on an individual will open 
 * their contacts page. This is the 3rd and right-most fragment in the slide
 * list.
 * 
 * In fact the fragment will not show exactly most recently added but actually
 * most likely recently updated. This is because andriod does not simply store 
 * add date. See the comment in LoadContacts for more info about this.
 * 
 * The fragment is loaded when it is adjacent to the viewed fragment. Therefore,
 * the load will kick-off when AddFromText is visible. 
 * 
 * Implements LoaderCallbacks to allow an asynchronous load of the contact info.
 * 
 * @author Matthew
 */
public class ViewRecentContactsFragment extends ListFragment implements
	LoaderCallbacks<ArrayList<ContactInfo>>{

	// adapter to hold contact info for recent contacts
	ContactAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// show loading animation
		setListShown(false);

		adapter = new ContactAdapter(getActivity());
		setListAdapter( adapter );
		getListView().setSelector(android.R.color.transparent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(0, null, this).forceLoad();		
	}
	
	/**
	 * when clicked launch the individual contact's page
	 */
    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i("FragmentComplexList", "Item clicked: " + id);

        //adapter.handleClick(v);
        
//        ContactInfo info = adapter.getItem(position);
//        
//		/* use the key to launch contact lookup activity */
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		Uri uri = Uri.withAppendedPath(
//				ContactsContract.Contacts.
//				CONTENT_LOOKUP_URI, info.lookup);
//		intent.setData(uri);
//		
//		// start for result so we can reload page on return in case
//		// the list is altered (ie contact deletion)
//		startActivityForResult(intent, 1);
    }

    /**
     * When the user launches a view contacts page he may be deleting a contact
     * which was added in error or editting a contacts name (perhaps from a
     * text he just received). In this case, we want to refresh the list to make
     * sure it displays the most up to date data. 
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if( requestCode == 1 ) {
	    	setListShown(false);
	    	getLoaderManager().initLoader(0, null, this).forceLoad();
    	}
    }

	@Override
	public Loader<ArrayList<ContactInfo>> onCreateLoader(int id, Bundle arg1) {
		Log.i("RecentContacts", "Entering onCreateLoader");
		return new LoadContacts(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<ContactInfo>> loader, ArrayList<ContactInfo> data) {
		Log.i("RecentContacts", "Entering onLoadFinished");
		// clear the adapter and add new contacts whenever load finishes so that
		// any updates are processed
		adapter.clear();
		adapter.addAll( data );
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<ContactInfo>> arg0) {
		Log.i("RecentContacts", "Entering onLoaderReset");
		adapter.clear();
	}
}
