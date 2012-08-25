/**
 * 
 */
package my.zin.rashidi.android.fugumod.task;

import static com.stericson.RootTools.RootTools.getShell;
import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.lang.Thread.sleep;

import java.io.File;
import java.io.IOException;

import android.os.AsyncTask;

import com.stericson.RootTools.CommandCapture;

/**
 * @author shidi
 * @version 1.1.0
 * @since 1.1.0
 */
public class FlashTask extends AsyncTask<String, Long, Long> {

	private String tempDir;
	
	@Override
	protected Long doInBackground(String... params) {
		
		this.tempDir = params[0];
		String image = format("%s/%s", tempDir, "boot.img");
		
		if (new File(image).exists()) {
			String bootTarget = "/dev/block/platform/omap/omap_hsmmc.0/by-name/boot";
			String flashCmd = format("%s %s %s", "dd", format("%s%s","if=", image), format("%s%s", "of=", bootTarget));
			
			try {
				sleep(1000);
				
				getShell(true).add(new CommandCapture(0, flashCmd)).waitForFinish();
				
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return valueOf(0);
	}
}
