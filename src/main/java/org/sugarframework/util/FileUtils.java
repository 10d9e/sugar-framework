package org.sugarframework.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.sugarframework.context.DefaultContextInitializer;

import com.google.common.io.ByteStreams;

public class FileUtils {

	public static String readFileOnClasspath(String path) throws IOException {
		byte[] array = ByteStreams.toByteArray(FileUtils.class.getResourceAsStream(path));

		return new String(array);
	}

	public static String findFile(String filename) {
		
		for(String srcDir : DefaultContextInitializer.getContext().srcDirectories()){
		
			for (String res : DefaultContextInitializer.getContext().resourceDirectories()) {
	
				String sourceCheck = srcDir + "/" + res + "/" + filename;
					
				if (exists(sourceCheck)) {
					return sourceCheck;
				}
			}
		
		}
		return null;
	}
	
	public static boolean exists(final String path){
		return Files.exists(Paths.get(path));
	}

}
