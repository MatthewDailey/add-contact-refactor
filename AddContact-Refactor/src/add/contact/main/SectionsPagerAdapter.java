package add.contact.main;

import add.contact.dialpad.AddFromDialpadFragment;
import add.contact.recent.ViewRecentContactsFragment;
import add.contact.text.AddFromTextFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the sliding fragments for the main view. Here they are
     * hard coded. 0 is the left most fagment.
     */
    @Override
    public Fragment getItem(int position) {
    	if( position == 0 ) {
    		return new AddFromDialpadFragment();
    	} else if ( position == 1 ) {
    		return new AddFromTextFragment();
    	} else if ( position == 2 ) {
    		return new ViewRecentContactsFragment();
    	}
    	// should never reach here
    	return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    /**
     * Titles for the main tabs
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Enter a contact";
            case 1:
                return "Click a text to add contact";
            case 2:
                return "Recently Added";
        }
        return null;
    }
}