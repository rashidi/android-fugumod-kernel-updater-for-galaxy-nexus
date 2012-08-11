/**
 * 
 */
package my.zin.rashidi.android.fugumod.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 * 
 * Based on http://stackoverflow.com/questions/8669903/android-reading-the-html-of-a-webpage-into-a-string
 */
public class GetConnection extends AsyncTask<String, Void, String>{
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	/**
	 * 0 - TextView
	 * 1 - String url
	 */
	protected String doInBackground(String... params) {
		return this.getContent(params[0]);
	}
	
	private String getContent(String url) {
		String content = null;
		
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
		super.onPostExecute(result);
	}

}
