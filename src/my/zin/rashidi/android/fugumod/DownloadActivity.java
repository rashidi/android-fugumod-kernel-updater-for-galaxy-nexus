/**
 * 
 */
package my.zin.rashidi.android.fugumod;

import static android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE;
import static android.app.DownloadManager.COLUMN_REASON;
import static android.app.DownloadManager.COLUMN_STATUS;
import static android.app.DownloadManager.ERROR_CANNOT_RESUME;
import static android.app.DownloadManager.ERROR_DEVICE_NOT_FOUND;
import static android.app.DownloadManager.ERROR_FILE_ALREADY_EXISTS;
import static android.app.DownloadManager.ERROR_FILE_ERROR;
import static android.app.DownloadManager.ERROR_HTTP_DATA_ERROR;
import static android.app.DownloadManager.ERROR_INSUFFICIENT_SPACE;
import static android.app.DownloadManager.ERROR_TOO_MANY_REDIRECTS;
import static android.app.DownloadManager.ERROR_UNHANDLED_HTTP_CODE;
import static android.app.DownloadManager.ERROR_UNKNOWN;
import static android.app.DownloadManager.PAUSED_QUEUED_FOR_WIFI;
import static android.app.DownloadManager.PAUSED_UNKNOWN;
import static android.app.DownloadManager.PAUSED_WAITING_FOR_NETWORK;
import static android.app.DownloadManager.PAUSED_WAITING_TO_RETRY;
import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_PAUSED;
import static android.app.DownloadManager.STATUS_PENDING;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;
import static android.net.Uri.parse;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStorageDirectory;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.String.format;
import static my.zin.rashidi.android.fugumod.utils.FuguModUtils.isFileExists;
import static my.zin.rashidi.android.fugumod.utils.FuguModUtils.isMatchedSum;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * @author shidi
 * @version 1.3.0
 * @since 1.1.0
 * 
 * Based on tutorial at http://android-er.blogspot.com/2011/07/check-downloadmanager-status-and-reason.html
 */
public class DownloadActivity extends FragmentActivity {

	private final String PREFERENCE_RELEASE_ID = "releaseId";
	private final String DIRECTORY_DOWNLOADS_FULL = format("/%s/%s", getExternalStorageDirectory().getPath(), DIRECTORY_DOWNLOADS);

	private SharedPreferences preferenceManager;
	private DownloadManager downloadManager;
	
	private	String release;
	private String targetUrl;
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_download);
		
		preferenceManager = getDefaultSharedPreferences(this);
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		
		SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.release_ref), 0);
		
		targetUrl = sharedPreferences.getString(getString(R.string.target_url), null);
		release = sharedPreferences.getString(getString(R.string.release_zip), null);

		TextView txtViewRelease = (TextView) findViewById(R.id.textViewRelease);
		txtViewRelease.setText(release.substring(release.lastIndexOf("_") + 1, release.indexOf(".zip")));
		
		if (!isFileExists(getFile())) { requestDownload(release); }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		checkDownloadStatus();
		
		IntentFilter intentFilter = new IntentFilter(ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, intentFilter);
	}
	
	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			checkDownloadStatus();
		}
	};
	
	private void checkDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		long id = preferenceManager.getLong(PREFERENCE_RELEASE_ID, 0);
		query.setFilterById(id);
		
		Cursor cursor = downloadManager.query(query);
		if (cursor.moveToFirst()) {
			int columnIndex = cursor.getColumnIndex(COLUMN_STATUS);
			int status = cursor.getInt(columnIndex);
			int columnReason = cursor.getColumnIndex(COLUMN_REASON);
			int reason = cursor.getInt(columnReason);
			
			switch (status) {
			case STATUS_FAILED:
				String failedReason = "";
				
				switch (reason) {
				case ERROR_CANNOT_RESUME:
					failedReason = "ERROR_CANNOT_RESUME";
					break;

				case ERROR_DEVICE_NOT_FOUND:
					failedReason = "ERROR_DEVICE_NOT_FOUND";
					break;
					
				case ERROR_FILE_ALREADY_EXISTS:
					failedReason = "ERROR_FILE_ALREADY_EXISTS";
					break;
					
				case ERROR_FILE_ERROR:
					failedReason = "ERROR_FILE_ERROR";
					break;
					
				case ERROR_HTTP_DATA_ERROR:
					failedReason = "ERROR_HTTP_DATA_ERROR";
					break;
					
				case ERROR_INSUFFICIENT_SPACE:
					failedReason = "ERROR_INSUFFICIENT_SPACE";
					break;
					
				case ERROR_TOO_MANY_REDIRECTS:
					failedReason = "ERROR_TOO_MANY_REDIRECTS";
					break;
					
				case ERROR_UNHANDLED_HTTP_CODE:
					failedReason = "ERROR_UNHANDLED_HTTP_CODE";
					break;
					
				case ERROR_UNKNOWN:
					failedReason = "ERROR_UNKNOWN";
					break;
				}
				
				displayStatus("FAILED", failedReason);
				break;
				
			case STATUS_PAUSED:
				String pausedReason = "";
				
				switch (reason) {
				case PAUSED_QUEUED_FOR_WIFI:
					pausedReason = "PAUSED_QUEUED_FOR_WIFI";
					break;

				case PAUSED_UNKNOWN:
					pausedReason = "PAUSED_UNKNOWN";
					break;
					
				case PAUSED_WAITING_FOR_NETWORK:
					pausedReason = "PAUSED_WAITING_FOR_NETWORK";
					break;
					
				case PAUSED_WAITING_TO_RETRY:
					pausedReason = "PAUSED_WAITING_FOR_RETRY";
					break;
				}

				displayStatus("Paused", pausedReason);
				break;
				
			case STATUS_PENDING:
				break;
				
			case STATUS_RUNNING:
				break;
				
			case STATUS_SUCCESSFUL:
				
				getCheckSumFile();
				boolean verifiedCheckSum = verifyCheckSum();
				
				if (verifiedCheckSum) {
					Intent intent = new Intent(this, FlashActivity.class);
					startActivity(intent);
				} else {
					displayStatus("Failed", "Checksum does not match");
				}
				break;
			}
		}
	}
	
	private void displayStatus(String status, String reason) {
		
		final TextView tvFlashCompleted = (TextView) findViewById(R.id.textViewFlashCompleted);
		
		if (reason != null) { tvFlashCompleted.setText(format("%s: %s", status, reason)); } 
		
		else { tvFlashCompleted.setText(format("%s", status)); }
	}
		
	private void requestDownload(String filename) {
		
		DownloadManager.Request request = new DownloadManager.Request(parse(format("%s/%s", targetUrl, filename)));
		request.setTitle(format("%s %s", getString(R.string.app_name), "Download"));
		request.setDescription(filename);
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, filename);
		
		long id = downloadManager.enqueue(request);

		Editor editor = preferenceManager.edit();
		editor.putLong(PREFERENCE_RELEASE_ID, id);
		editor.commit();
	}
	
	private void getCheckSumFile() {
	
		if (!isFileExists(getSumFile())) { requestDownload(format("%s.sha256sum", release)); }
	}
	
	private boolean verifyCheckSum() { return isMatchedSum(getFile()); }
	
	private String getFile() { return (release != null) ? format("%s/%s", DIRECTORY_DOWNLOADS_FULL, release) : null; }
	
	private String getSumFile() { return (getFile() != null) ? format("%s.sha256sum", getFile()) : null; } 
}