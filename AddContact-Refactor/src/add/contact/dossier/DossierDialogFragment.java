package add.contact.dossier;

import add.contact.util.Const;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class DossierDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Get Dossier?");
		builder.setMessage("Dossier is our partner contact management app.");
		builder.setPositiveButton("Show it to me!", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, 
				    		Uri.parse("market://details?id=" + Const.DOSSIER_PACKAGE)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW,
				    		Uri.parse("http://play.google.com/store/apps/details?id=" + Const.DOSSIER_PACKAGE)));
				}
			}
		});
		builder.setNegativeButton("Not now.",null);
		
		return builder.create();
	}
	
}
