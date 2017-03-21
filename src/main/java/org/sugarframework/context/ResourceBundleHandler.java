package org.sugarframework.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ResourceBundleHandler<T extends Annotation> implements InvocationHandler {

	private static final Method OBJECT_EQUALS = getObjectMethod("equals", Object.class);

	private static final Method OBJECT_HASHCODE = getObjectMethod("hashCode");

	private final T _implementation;

	ResourceBundleHandler(final T implementation) {
		_implementation = implementation;
	}

	public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
		if (OBJECT_EQUALS == method) {
			return equalsInternal(proxy, objects[0]);
		}

		if (OBJECT_HASHCODE == method) {
			return _implementation.hashCode();
		}

		try {

			Object value = method.invoke(_implementation, objects);
			if (value instanceof String && !method.getName().equals("toString")) {
				value = process(value.toString());
			}
			return value;
		} catch (InvocationTargetException itx) {
			throw itx.getTargetException();
		}
	}

	private String process(String s) {
		Matcher m = Pattern.compile("\\#\\{(\\S+)\\}").matcher(s);

		StringBuffer sb = new StringBuffer();

		while (m.find()) {
			String rep = DefaultContextInitializer.getContext().getBundle().getString(m.group(1));
			m.appendReplacement(sb, rep);
		}
		m.appendTail(sb);

		return sb.toString();
	}

	private boolean equalsInternal(Object me, Object other) {
		if (other == null) {
			return false;
		}
		if (other.getClass() != me.getClass()) {
			// Not same proxy type; false.
			// This may not be true for other scenarios.
		}
		InvocationHandler handler = Proxy.getInvocationHandler(other);
		if (!(handler instanceof ResourceBundleHandler)) {
			// the proxies behave differently.
			return false;
		}
		return ((ResourceBundleHandler<?>) handler)._implementation.equals(_implementation);
	}

	private static Method getObjectMethod(String name, Class<?>... types) {
		try {
			// null 'types' is OK.
			return Object.class.getMethod(name, types);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
