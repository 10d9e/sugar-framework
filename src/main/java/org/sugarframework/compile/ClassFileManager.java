package org.sugarframework.compile;

import java.io.IOException;
import java.security.SecureClassLoader;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

@SuppressWarnings("rawtypes")
public class ClassFileManager extends ForwardingJavaFileManager {
	/**
	 * Instance of JavaClassObject that will store the compiled bytecode of our
	 * class
	 */

	private JavaClassObject jclassObject;

	private String className;

	/**
	 * Will initialize the manager with the specified standard java file manager
	 * 
	 * @param standardManger
	 */
	@SuppressWarnings("unchecked")
	public ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
	}

	private class DynamicClassLoader extends SecureClassLoader {

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			if (jclassObject != null && className.equals(name)) {
				byte[] b = jclassObject.getBytes();
				return super.defineClass(name, jclassObject.getBytes(), 0, b.length);
			} else {
				return super.findClass(name);
			}
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (jclassObject != null && className.equals(name)) {

				byte[] b = jclassObject.getBytes();
				return super.defineClass(name, jclassObject.getBytes(), 0, b.length);
			} else {
				return super.loadClass(name);
			}
		}
	}

	private DynamicClassLoader dynamicClassLoader = new DynamicClassLoader();

	/**
	 * Will be used by us to get the class loader for our compiled class. It
	 * creates an anonymous class extending the SecureClassLoader which uses the
	 * byte code created by the compiler and stored in the JavaClassObject, and
	 * returns the Class for it
	 */
	@Override
	public ClassLoader getClassLoader(Location location) {
		return dynamicClassLoader;
	}

	/**
	 * Gives the compiler an instance of the JavaClassObject so that the
	 * compiler can write the byte code into it.
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
			throws IOException {
		jclassObject = new JavaClassObject(className, kind);

		this.className = className;

		return jclassObject;
	}

	public String getClassName() {
		return className;
	}

}
