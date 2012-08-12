/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;
import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
						DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.format("%s%s", url, release)));
						request.setDescription(String.format("Downloading %s", release));
						request.allowScanningByMediaScanner();
						request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
						request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, release);
						
						DownloadManager manager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
						manager.enqueue(request);
						
						dialog.dismiss();
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
