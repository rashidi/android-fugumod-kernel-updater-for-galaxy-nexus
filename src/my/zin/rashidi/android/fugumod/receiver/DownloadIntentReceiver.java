/**
 * 
 */
package my.zin.rashidi.android.fugumod.receiver;

import my.zin.rashidi.android.fugumod.fragments.RebootDialogFragment;
import my.zin.rashidi.android.fugumod.utils.FuguModUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * @author shidi
 * @version 1.1.0
 * @since 1.1.0
 */
public class DownloadIntentReceiver extends BroadcastReceiver {

	private String release;
	private FragmentManager fragmentManager;
	
	public DownloadIntentReceiver() {
		super();
	}
	
	public DownloadIntentReceiver(String release, FragmentManager fragmentManager) {
		super();
		
		this.release = release;
		this.fragmentManager = fragmentManager;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (FuguModUtils.isMatchedSum(release)) {
			DialogFragment dialog = new RebootDialogFragment(release);
			dialog.show(fragmentManager, "dialog");
			
			context.unregisterReceiver(this);
		}
	}

}
