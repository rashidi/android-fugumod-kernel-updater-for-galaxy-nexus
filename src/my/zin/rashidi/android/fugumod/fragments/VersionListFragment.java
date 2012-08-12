/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import my.zin.rashidi.android.fugumod.R;
import my.zin.rashidi.android.fugumod.utils.ContentUtils;
import my.zin.rashidi.android.fugumod.utils.GetConnection;

import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class VersionListFragment extends ListFragment {

	private String url = null;
	
	public VersionListFragment() {
		
	}
	
	public VersionListFragment(String url) {
		this.url = url;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();
		
		if (activity != null) {			
			List<String> releases = new ArrayList<String>();
			
			try {
				String content = new GetConnection(activity).execute(url).get();
				TagNode nodes[] = ContentUtils.getReleaseInfo(content);
				
				for (TagNode node : nodes) {
					String href = node.getAttributes().get("href");
					
					if (href.indexOf("FuguMod") > 0 && href.indexOf("sha") < 0) {
						releases.add(href);
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			ListAdapter listAdapter = new ArrayAdapter<String>(activity, R.layout.row, releases);
			setListAdapter(listAdapter);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String release = String.valueOf(getListView().getItemAtPosition(position));
		
		DialogFragment confirmationDialog = new ConfirmationDialogFragment(url, release);
		confirmationDialog.show(getFragmentManager(), "dialog");
	}
	
}
