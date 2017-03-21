package org.sugarframework.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class AbstractAspectHandler <T extends Annotation> {
		
	public abstract void handle(Object page, T annotation, Method method, Object[] args);

}
