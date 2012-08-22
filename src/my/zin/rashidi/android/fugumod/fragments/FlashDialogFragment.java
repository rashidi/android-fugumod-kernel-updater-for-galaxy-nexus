/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.ExecuteException;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

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
						
						try {
							
							/**
							 * Steps involved:
							 * 
							 * 		1. Request for SU
							 * 		2. Create a temporary directory. E.g. fuguMod
							 * 		3. Unzip downloaded file into temporary directory
							 * 		4. run dd if=/sdcard/Download/fuguMod/boot.img of=/dev/block/platform/omap/omap_hsmmc.0/by-name/boot
							 * 		5. reboot
							 */
							
							Process process = Runtime.getRuntime().exec("su");
							
							if (isRoot(process)) {
								
								String tempDir = String.format("/%s/%s/%s", "sdcard", DIRECTORY_DOWNLOADS, "fuguMod");
								Runtime.getRuntime().exec(String.format("%s %s", "mkdir", tempDir));
								
								Thread.sleep(1000);
								
								if (new File(tempDir).exists()) {
									String target = String.format("/%s/%s/%s", "sdcard", DIRECTORY_DOWNLOADS, file);
									Runtime.getRuntime().exec(String.format("%s %s -d %s", "unzip", target, tempDir));
								}
							} else {
								Log.i("fuguMod", "Failed to get root access.");
							}
							
						} catch (ExecuteException e) {
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
	
	private boolean isRoot(Process process) {
		
		boolean retVal = false;
		DataOutputStream output = new DataOutputStream(process.getOutputStream());
		DataInputStream input = new DataInputStream(process.getInputStream());
		
		if (output != null && input != null) {
			
			try {
				output.writeBytes("id\n");
				output.flush();
				
				@SuppressWarnings("deprecation")
				String uid = input.readLine();
				boolean exitSu = false;
				
				if (uid.contains("uid=0")) {
					retVal = true;
					exitSu = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return retVal;
	}
}
