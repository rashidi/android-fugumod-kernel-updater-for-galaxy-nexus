/**
 * 
 */
package my.zin.rashidi.android.fugumod;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStorageDirectory;
import static com.stericson.RootTools.RootTools.debugMode;
import static com.stericson.RootTools.RootTools.getShell;
import static com.stericson.RootTools.RootTools.getWorkingToolbox;
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;
import static java.lang.String.format;
import static my.zin.rashidi.android.fugumod.utils.FuguModUtils.isFileExists;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stericson.RootTools.CommandCapture;

/**
 * @author shidi
 * @version 1.2.0
 * @since 1.2.0
 */
public class FlashActivity extends FragmentActivity {

	private final String DIRECTORY_DOWNLOADS_FULL = format("/%s/%s", getExternalStorageDirectory().getPath(), DIRECTORY_DOWNLOADS);
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
				
		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.release_ref), 0);
		String release = sharedPreferences.getString(getString(R.string.release_zip), null);
		
		TextView tvStatus = (TextView) findViewById(R.id.textViewStatus);
		tvStatus.setText("Flashing ");
		
		TextView txtViewRelease = (TextView) findViewById(R.id.textViewRelease);
		txtViewRelease.setText(release.substring(release.lastIndexOf("_") + 1, release.indexOf(".zip")));
		
		if ((release != null) && isFileExists(format("%s/%s", DIRECTORY_DOWNLOADS_FULL, release))) { 
			flashImage(release); 
		}
	}
	
	private void flashImage(final String release) {
		
		Button btnFlash = (Button) findViewById(R.id.buttonFlashKernel);
		Button btnReboot = (Button) findViewById(R.id.buttonReboot);
		
		if (!isRootAvailable()) { displayStatus("Flash Failed", "Root privilege is required.", true); } 
		
		else if (!isBusyboxAvailable()) { displayStatus("Flash Failed", "Busybox is required.", true); }
		
		btnFlash.setEnabled(true);
		btnReboot.setEnabled(true);
		
		btnFlash.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String targetDir = format("%s/%s/", DIRECTORY_DOWNLOADS_FULL, release.replace(".zip", ""));
				String image = format("%s/%s", targetDir, "boot.img");
				
				try {
					debugMode = true;
					
					getShell(true).add(
							new CommandCapture(0, 
									format("mkdir %s", targetDir), 
									format("unzip %s/%s -d %s", DIRECTORY_DOWNLOADS_FULL, release, targetDir),
									format("%s dd if=%s of=/dev/block/platform/omap/omap_hsmmc.0/by-name/boot", getWorkingToolbox(), image),
									format("rm -r %s", targetDir)
							)).waitForFinish();

					displayStatus(getString(R.string.flash_completed), null, false);
					
				} catch (Exception e) {
					displayStatus("Flashing failed", e.getLocalizedMessage(), true);
					Log.e(getString(R.string.app_name), "Flashing failed: ", e);
				}
			}
		});
		
		btnReboot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					getShell(true).add(new CommandCapture(1, "reboot"));
				} catch (IOException e) {
					displayStatus("Reboot failed", e.getLocalizedMessage(), true);
					Log.e(getString(R.string.app_name), "Reboot failed: ", e);
				} catch (TimeoutException e) {
					displayStatus("Reboot failed", e.getLocalizedMessage(), true);
					Log.e(getString(R.string.app_name), "Reboot failed: ", e);
				}
			}
		});
	}
	
	private void displayStatus(String status, String reason, boolean error) {
		
		final TextView tvFlashCompleted = (TextView) findViewById(R.id.textViewFlashCompleted);
		
		if (reason != null) { tvFlashCompleted.setText(format("%s: %s", status, reason)); } 
		
		else { tvFlashCompleted.setText(format("%s", status)); }
		
		if (error) { return; }
	}

}
