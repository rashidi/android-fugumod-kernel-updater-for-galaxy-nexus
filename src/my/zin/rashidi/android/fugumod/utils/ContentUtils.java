/**
 * 
 */
package my.zin.rashidi.android.fugumod.utils;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * @author shidi
 * @version 1.0.0
 * @since 1.0.0
 */
public class ContentUtils {

	@SuppressWarnings("unchecked")
	public static TagNode[] getReleaseInfo(String content) {
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(content);
		
		return node.getElementsByName("a", true);
	}
}
