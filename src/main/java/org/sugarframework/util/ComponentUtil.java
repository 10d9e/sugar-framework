package org.sugarframework.util;

import org.sugarframework.component.Style;

public final class ComponentUtil {

	public static String embedInPanel(String content) {
		return embedInPanel(content, Style.STYLE_DEFAULT);
	}

	public static String embedInPanel(String content, Style style) {
		return String.format("<div class=\"panel panel-%s\"> <div class=\"panel-body\">%s</div></div>", style, content);
	}

	public static String embedInPanel(String heading, String content, Style style) {
		return String
				.format("<div class=\"panel panel-%s\"> <div class=\"panel-heading\">%s</div> <div class=\"panel-body\">%s</div></div>",
						style, heading, content);
	}

}
