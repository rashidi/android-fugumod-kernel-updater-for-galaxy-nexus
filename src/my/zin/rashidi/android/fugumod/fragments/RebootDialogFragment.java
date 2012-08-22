/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author shidi
 * @version 1.1.0
 * @since 1.1.0
 */
public class RebootDialogFragment extends DialogFragment {

	private String file;
	
	public RebootDialogFragment() {
		
	}
	
	public RebootDialogFragment(String file) {
		this.file = file;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage("Download is complete. Would you like to reboot into recovery?")
				.setIcon(R.drawable.alert_dark_frame)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						try {
							Runtime.getRuntime().exec("su");
							CommandLine command = new CommandLine("/system/bin/reboot recovery");
							
							new DefaultExecutor().execute(command);
						} catch (ExecuteException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
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
