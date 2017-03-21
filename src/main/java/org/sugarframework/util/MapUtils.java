package org.sugarframework.util;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapUtils {

	public static int size(final Map<String, Set<Method>> methods) {

		int c = 0;

		for (Entry<String, Set<Method>> e : methods.entrySet()) {
			c += e.getValue().size();
		}

		return c;

	}

}
