package org.sugarframework.util;

import java.util.Map;

import com.google.common.base.Splitter;

public final class Structures {

	/**
	 * Use: <code>
	 * Map<String, String> map = splitToMap("A = 4,H = X,PO = 87");
	 * </code>
	 * 
	 * @param in
	 * @return
	 */
	public static Map<String, String> map(String in) {
		return Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(in);
	}

	public static Iterable<String> split(String in) {
		return Splitter.on(",").omitEmptyStrings().trimResults().split(in);
	}
}
