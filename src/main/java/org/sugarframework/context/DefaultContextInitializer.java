package org.sugarframework.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DefaultContextInitializer {
	
	private static Log log = LogFactory.getLog(DefaultContextInitializer.class);
	
	private static DefaultContext context;
	
	public static DefaultContext getContext(){
		return context;
	}
	
	public static void startContext(Class<?> contextClass) {
		context = new DefaultContext(contextClass);
		log.info("Context started.");
	}
	
}
