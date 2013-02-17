/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStorageDirectory;
import static java.lang.String.format;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.sort;
import static my.zin.rashidi.android.fugumod.updater.R.string.release_ref;
import static my.zin.rashidi.android.fugumod.updater.R.string.release_zip;
import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import my.zin.rashidi.android.fugumod.updater.R;
import my.zin.rashidi.android.fugumod.updater.FlashActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author shidi
 * @version 1.3.0
 * @since 1.2.0
 */
public class DownloadedList extends ListFragment {

	private final String DIRECTORY_DOWNLOADS_FULL = format("/%s/%s", getExternalStorageDirectory().getPath(), DIRECTORY_DOWNLOADS);
	
	public DownloadedList() {
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String release = String.valueOf(getListView().getItemAtPosition(position));
		
		SharedPreferences preferences = getActivity().getSharedPreferences(getString(release_ref), 0);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString(getString(release_zip), String.format("%s", release));
		editor.commit();

		Intent intent = new Intent(getActivity(), FlashActivity.class);
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Activity activity = getActivity();
		Collection<File> files = listFiles(new File(DIRECTORY_DOWNLOADS_FULL), new String[] { "zip" }, false);
		List<String> fuguZips = new ArrayList<String>();
		
		for (File file : files) {
			if (file.getName().contains("kernel_FuguMod")) { fuguZips.add(file.getName()); }
		}
		
		sort(fuguZips, reverseOrder());
		
		ListAdapter listAdapter = new ArrayAdapter<String>(activity, R.layout.row, fuguZips);
		setListAdapter(listAdapter);
	}
}
