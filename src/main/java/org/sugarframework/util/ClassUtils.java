package org.sugarframework.util;

import javassist.util.proxy.ProxyFactory;

public class ClassUtils {

	public static Class<?> getTargetClass(Object candidate) {
		assert candidate != null;

		if( ProxyFactory.isProxyClass(candidate.getClass()) ){
			return candidate.getClass().getSuperclass();
		}
		return candidate.getClass();
	}

}
