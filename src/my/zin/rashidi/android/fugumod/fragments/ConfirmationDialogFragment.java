/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import my.zin.rashidi.android.fugumod.task.DownloadFilesTask;
import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
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

						new DownloadFilesTask(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED, getActivity(), getFragmentManager()).execute(url, String.format("%s.sha256sum", release));
						new DownloadFilesTask(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED, getActivity(), getFragmentManager()).execute(url, release);
						
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
