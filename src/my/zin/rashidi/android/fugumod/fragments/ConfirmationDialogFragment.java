/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import static my.zin.rashidi.android.fugumod.updater.R.string.release_ref;
import static my.zin.rashidi.android.fugumod.updater.R.string.release_zip;
import static my.zin.rashidi.android.fugumod.updater.R.string.target_url;
import my.zin.rashidi.android.fugumod.updater.DownloadActivity;
import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfirmationDialogFragment extends DialogFragment {

	private String url;
	private String release;
	
	public ConfirmationDialogFragment() {
		
	}
	
	public ConfirmationDialogFragment(String url, String release) {
		this.url = url;
		this.release = release;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage(String.format("Download %s?", release))
				.setIcon(R.drawable.alert_dark_frame)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						
						SharedPreferences preferences = getActivity().getSharedPreferences(getString(release_ref), 0);
						SharedPreferences.Editor editor = preferences.edit();
						
						editor.putString(getString(target_url), url);
						editor.putString(getString(release_zip), String.format("%s", release));
						editor.commit();

						Intent intent = new Intent(getActivity(), DownloadActivity.class);
						startActivity(intent);
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		
		return builder.create();
	}

}
