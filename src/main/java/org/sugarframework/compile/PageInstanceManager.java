package org.sugarframework.compile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sugarframework.util.ClassUtils;

class PageInstanceManager {
	
	private Log log = LogFactory.getLog(PageInstanceManager.class);
	
	private Map<Class<?>, Object> instances = new HashMap<>();
	
	public void addInstance(Object instance){
		log.info("Adding page instance " + instance);
		instances.put( ClassUtils.getTargetClass(instance), instance);
	}
	
	public void getInstance(Class<?> type){
		instances.get(type);
	}
	
	public void remove(Class<?> type){
		instances.remove(type);
	}

}
