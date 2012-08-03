/**
 * 
 */
package my.zin.rashidi.android.fugumod;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class GetConnection extends AsyncTask<Object, Void, String>{

	private TextView tv;
	private String content = null;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	/**
	 * 0 - TextView
	 * 1 - String url
	 */
	protected String doInBackground(Object... params) {
		this.tv = (TextView) params[0];
		return this.getContent(String.valueOf(params[1]));
	}
	
	private String getContent(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			
			 content = EntityUtils.toString(response.getEntity());
		} catch	(ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}

	@Override
	protected void onPostExecute(String result) {
		tv.setText(content);
	}

}
