/**
 * 
 */
package my.zin.rashidi.android.fugumod.utils;

import java.util.concurrent.ExecutionException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * @author shidi
 * @version 1.0.1
 * @since 1.0.0
 */
public class ContentUtils {

	public static TagNode[] getReleaseInfo(String content) {
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(content);
		
		return node.getElementsByName("a", true);
	}
	
	public static String[] getAvailableVersions() {
		String url = "http://fugumod.org/galaxy_nexus/versions.txt";
		String[] versions = new String[] { };
		
		try {
			versions = new GetConnection().execute(url).get().split("\\n");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return versions;
	}
}
