package org.sugarframework.util;

import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sugarframework.Initialize;
import org.sugarframework.View;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.servlet.SugarPageServlet;

public final class ViewInstanceUtil {
	
	private static final Log log = LogFactory.getLog(ViewInstanceUtil.class);
	
	public static void initializeSessionInstances(HttpSession session) throws Exception{
		
		Collection<Class<?>> viewClasses = (Collection<Class<?>>) session.getAttribute("currentViews");
				
		for(Class<?> view : viewClasses){
			getScreenInstanceInternal(session, view);
		}
	}

	public static Object getScreenInstance(HttpSession session, SugarPageServlet servlet, Class<?> pageClass)
			throws Exception {

		if (DefaultContextInitializer.getContext().isDevelopmentMode()) {

			DefaultContextInitializer.getContext().reload(servlet);

			Object previousInstance = getScreenInstanceInternal(session, pageClass);

			Object newInstance = CompilerUtil.compileAndLoadJava(pageClass);

			if (previousInstance != null) {
				return Reflector.copy(previousInstance, newInstance);
			} else {
				return newInstance;
			}

		} else {

			return getScreenInstanceInternal(session, pageClass);
		}
	}

	public static Object getScreenInstanceInternal(HttpSession session, Class<?> pageClass) throws Exception {

		View screenAnnotation = pageClass.getAnnotation(View.class);

		Object instance = session.getAttribute(screenAnnotation.value());
		if (instance == null) {

			instance = pageClass.newInstance();
			
			DefaultContextInitializer.getContext().datasourceRegistry().initialize(instance);
			
			for(Method method : getTargetClass(instance).getMethods()) {
			    if(method.isAnnotationPresent(Initialize.class)) {
			    	try {
						method.invoke(instance);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
			    }
			}
			
			session.setAttribute(screenAnnotation.value(), instance);
		}
		
		
				
		return instance;
	}
}
