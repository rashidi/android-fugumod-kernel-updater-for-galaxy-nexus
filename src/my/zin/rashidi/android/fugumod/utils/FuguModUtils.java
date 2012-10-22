/**
 * 
 */
package my.zin.rashidi.android.fugumod.utils;

import static com.stericson.RootTools.RootTools.getWorkingToolbox;
import static com.stericson.RootTools.RootTools.sendShell;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.stericson.RootTools.RootToolsException;

/**
 * @author shidi
 * @version 1.2.0
 * @since 1.0.0
 */
public class FuguModUtils {

	public static TagNode[] getReleaseInfo(String content) {
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(content);
		
		return node.getElementsByName("a", true);
	}
	
	public static String[] getTabs() {
		String url = "http://fugumod.org/galaxy_nexus/versions.txt";
		List<String> tabs = new ArrayList<String>();
		
		tabs.add("Downloaded");
		String[] versions = new String[] { };
		
		try {
			versions = new GetConnection().execute(url).get().split("\\n");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if (versions.length > 0) { tabs.addAll(Arrays.asList(versions)); };
		
		return tabs.toArray(new String[0]);
	}
	
	public static boolean isMatchedSum(String file) {
		
		try {
			String expected = readFileToString(new File(format("%s.sha256sum", file))).split(" ")[0];
			
			String cmd = format("%s sha256sum %s", getWorkingToolbox(), file);
			String actual = sendShell(cmd, 0).get(0).split(" ")[0];

			return expected.equals(actual);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RootToolsException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean isFileExists(String filename) {
		File file = new File(filename);
		return file.exists();
	}
}
