package org.sugarframework.util;

import org.sugarframework.View;

public final class ViewUtil {

	public static String url(View view){
		String url = view.url();
		if (url.equals("")) {
			url = view.value();
		}
		return url;
	}
	
}
