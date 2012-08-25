/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.stericson.RootTools.RootTools.isAccessGiven;
import static com.stericson.RootTools.RootTools.log;
import static java.lang.Long.valueOf;
import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static org.apache.commons.io.FilenameUtils.getBaseName;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import my.zin.rashidi.android.fugumod.task.FlashTask;
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
public class FlashDialogFragment extends DialogFragment {

	private String file;
	
	public FlashDialogFragment() {
		
	}
	
	public FlashDialogFragment(String file) {
		this.file = file;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage("Download is complete. Would you like to flash the kernel?")
				.setIcon(R.drawable.alert_dark_frame)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
						
						try {							
							if (isAccessGiven()) {
								String downloadDir = format("%s/%s", "sdcard", DIRECTORY_DOWNLOADS);
								
								String tempDir = format("/%s/%s", downloadDir, getBaseName(file));
								getRuntime().exec(format("%s %s", "mkdir", tempDir));
								
								sleep(1000);

								if (new File(tempDir).exists()) {
									String target = format("/%s/%s", downloadDir, file);
									getRuntime().exec(format("%s %s -d %s", "unzip", target, tempDir));
									
									Long result = new FlashTask().execute(tempDir).get();
									
									if (result == valueOf(0)) {
										String rmCmd = format("%s %s %s", "rm", "-fr", tempDir + "*");
										getRuntime().exec(rmCmd);
										
										makeText(getActivity(), "Flash Success", LENGTH_LONG).show();
									}
								}
							} else {
								log("fuguMod", "Failed to get root access!");
							}
							
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
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
