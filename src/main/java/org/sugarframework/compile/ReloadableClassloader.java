package org.sugarframework.compile;

import java.util.Map;

public class ReloadableClassloader extends ClassLoader {
	private Map<String, byte[]> classes;

	public ReloadableClassloader(Map<String, byte[]> classes) {
		this.classes = classes;
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classBytes = classes.get(name);
		if (classBytes == null)
			throw new ClassNotFoundException(name);
		Class<?> cl = defineClass(name, classBytes, 0, classBytes.length);
		if (cl == null)
			throw new ClassNotFoundException(name);
		return cl;
	}
}
