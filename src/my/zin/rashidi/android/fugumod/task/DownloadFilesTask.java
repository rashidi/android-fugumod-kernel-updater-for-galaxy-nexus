/**
 * 
 */
package my.zin.rashidi.android.fugumod.task;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import my.zin.rashidi.android.fugumod.receiver.DownloadIntentReceiver;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * @author shidi
 * @version 1.1.0
 * @since 1.1.0
 */
public class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

	private String url;
	private String release;
	private int visibility;
	private Activity activity;
	
	public DownloadFilesTask() {
		
	}
	
	public DownloadFilesTask(int visibility, Activity activity) {
		this.visibility = visibility;
		this.activity = activity;
	}
	
	@Override
	protected Long doInBackground(String... params) {
		this.url = params[0];
		this.release = params[1];
		
		DownloadManager manager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
		registerReceiver();
		
		return manager.enqueue(getRequest());
	}
	
	private DownloadManager.Request getRequest() {
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.format("%s/%s", url, release)));
		request.setDescription(String.format("Downloading %s", release));
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(visibility);
		request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, release);
		
		return request;
	}
	
	private void registerReceiver() {
		DownloadIntentReceiver receiver = new DownloadIntentReceiver();
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		
		activity.registerReceiver(receiver, filter);
	}

}
