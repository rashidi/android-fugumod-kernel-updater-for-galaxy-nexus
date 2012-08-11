/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import java.util.concurrent.ExecutionException;

import my.zin.rashidi.android.fugumod.R;
import my.zin.rashidi.android.fugumod.utils.ContentUtils;
import my.zin.rashidi.android.fugumod.utils.GetConnection;

import org.htmlcleaner.TagNode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestingVersionFragment extends Fragment {
	
	public TestingVersionFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String content = "";
		
		TextView tv = new TextView(getActivity());
		tv.setGravity(Gravity.LEFT);
		
		try {
			content = new GetConnection().execute(getString(R.string.url_4_1_testing)).get();			
			TagNode nodes[] = ContentUtils.getReleaseInfo(content);
			
			for (TagNode node : nodes) {
				String href = node.getAttributes().get("href");
				
				if (href.indexOf("FuguMod") > 0 && href.indexOf("sha") < 0) { 
					tv.setText(href); 
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return tv;
	}

}
