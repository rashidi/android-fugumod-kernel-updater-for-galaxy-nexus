/**
 * 
 */
package my.zin.rashidi.android.fugumod.fragments;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import my.zin.rashidi.android.fugumod.GetConnection;
import my.zin.rashidi.android.fugumod.R;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class StableVersionFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String content = "";
		
		TextView tv = new TextView(getActivity());
		tv.setGravity(Gravity.LEFT);
		
		try {
			content = new GetConnection().execute(getString(R.string.url_4_1_stable)).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		tv.setText(content);
		return tv;
	}
	
}
