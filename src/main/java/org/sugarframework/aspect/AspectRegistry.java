package org.sugarframework.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.sugarframework.SugarAspectHandler;

public final class AspectRegistry {

	private Log log = LogFactory.getLog(getClass());

	private Map<Class<?>, AbstractAspectHandler> handlers = new HashMap<>();

	public void initialize(Reflections reflections) throws InstantiationException, IllegalAccessException {
		log.info("Registering aspect handlers");

		for (Class<? extends AbstractAspectHandler> handler : reflections.getSubTypesOf(
				AbstractAspectHandler.class)) {

			Object newInstance = handler.newInstance();

			SugarAspectHandler aspectHandler = newInstance.getClass().getAnnotation(SugarAspectHandler.class);

			log.info(String.format("%s [ %s ]", handler, aspectHandler.value()));

			handlers.put(aspectHandler.value(), (AbstractAspectHandler) newInstance);
		}
	}
	
	public void handle(Annotation annotation, Object page, Method method, Object[]args){
		
		AbstractAspectHandler<Annotation> handler = getHandler(annotation.annotationType());
		
		handler.handle(page, annotation, method, args);
		
	}
	
	private AbstractAspectHandler<Annotation> getHandler(Class<?> annotation){
		return handlers.get(annotation);
	}

}
