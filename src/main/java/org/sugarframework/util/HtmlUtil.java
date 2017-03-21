package org.sugarframework.util;


public final class HtmlUtil {

	public static String tidy(String html) {
		return html;
		// return Jsoup.clean(html, Whitelist.relaxed().
		// addAttributes(":all", "style", ":all","id", ":all", "class") );
	}

}
