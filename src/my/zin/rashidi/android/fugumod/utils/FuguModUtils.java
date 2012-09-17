/**
 * 
 */
package my.zin.rashidi.android.fugumod.utils;

import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.os.Environment;

/**
 * @author shidi
 * @version 1.1.0
 * @since 1.0.0
 */
public class FuguModUtils {

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
	
	public static boolean isMatchedSum(String release) {
		String file = String.format("/%s/%s/%s", "sdcard", Environment.DIRECTORY_DOWNLOADS, release);
		
		try {
			InputStream input = new FileInputStream(file);
			
			String actual = new String(Hex.encodeHex(DigestUtils.sha256(input)));
			String expected = readFileToString(new File(format("%s.sha256sum", file))).split(" ")[0];
			
			return expected.equals(actual);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return true;
	}
}