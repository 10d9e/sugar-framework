package org.sugarframework.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.sugarframework.SugarException;
import org.sugarframework.Tuple;
import org.sugarframework.compile.CharSequenceJavaFileObject;
import org.sugarframework.compile.ClassFileManager;
import org.sugarframework.context.DefaultContextInitializer;

public final class CompilerUtil {
	
	public static final String findDebugSourcePath(final Class<?> clazz) {

		String javaSource = clazz.getName().replace(".", "/") + ".java";

		// try to find the source based on context source directory
		for (String src : DefaultContextInitializer.getContext().srcDirectories()) {

			String sourceCheck = src + "/" + javaSource;

			if (Files.exists(Paths.get(sourceCheck))) {
				return sourceCheck;
			}
		}

		return null;

	}

	public static final Map<String, Object> compileAndLoadDirectory(String directory) throws Exception {

		final Map<String, Object> m = new HashMap<>();

		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.{java}");

		Path startPath = Paths.get(directory);
		Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				Path name = file.getFileName();
				if (name != null && matcher.matches(name)) {

					String filename = Paths.get(file.toUri()).normalize().toString();

					try {
						Tuple<String, Object> no = compileAndLoadJava(filename);
						if (no.second != null) {
							m.put(no.first, no.second);
						}

					} catch (Exception e) {
						throw new SugarException(e.getMessage(), e);
					}
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) {
				return FileVisitResult.CONTINUE;
			}
		});

		return m;

	}

	public static final Tuple<String, Object> compileAndLoadJava(String sourcePath) throws Exception {

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager sfm = compiler.getStandardFileManager(null, null, null);
		JavaFileManager fileManager = new ClassFileManager(sfm);
		List<File> sourceFileList = new ArrayList<File>();
		sourceFileList.add(new File(sourcePath));
		Iterable<? extends JavaFileObject> compilationUnits = sfm.getJavaFileObjectsFromFiles(sourceFileList);

		compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

		String fullClassName = ((ClassFileManager) fileManager).getClassName();

		Class<?> target = fileManager.getClassLoader(null).loadClass(fullClassName);

		Object instance = null;
		if (!target.isInterface()) {
			instance = target.newInstance();
		}

		return new Tuple<String, Object>(fullClassName, instance);

	}

	public static final Object compileAndLoadJava(final Class<?> clazz) throws Exception {

		String sourcePath = CompilerUtil.findDebugSourcePath(clazz);

		return compileAndLoadJava(sourcePath, clazz.getName());
	}

	public static final Object compileAndLoadJava(String sourcePath, String className) throws Exception {

		List<String> lines = Files.readAllLines(Paths.get(sourcePath), Charset.defaultCharset());

		final StringBuilder source = new StringBuilder();

		for (String line : lines) {
			source.append(line + "\n");
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));

		// Dynamic compiling requires specifying
		// a list of "files" to compile. In our case
		// this is a list containing one "file" which is in our case
		// our own implementation (see details below)
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(className, source));

		// We specify a task to the compiler. Compiler should use our file
		// manager and our list of "files".
		// Then we run the compilation with call()
		compiler.getTask(null, fileManager, null, null, null, jfiles).call();

		Class<?> target = fileManager.getClassLoader(null).loadClass(className);

		Object instance = null;
		if (!target.isInterface()) {
			instance = target.newInstance();
		}

		return instance;
	}

}
