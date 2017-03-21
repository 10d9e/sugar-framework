package org.sugarframework.servlet;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withName;
import static org.sugarframework.context.DefaultContextInitializer.ERROR_THROWING_CONVERTER;
import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugarframework.SugarException;
import org.sugarframework.Validator;
import org.sugarframework.util.FormUtil;
import org.sugarframework.util.Reflector;

import com.google.common.base.Predicates;

public class SugarValidatorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final short SUGAR_VALIDATION_ERROR = 277;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

		String methodName = (String) req.getParameter("methodName");
		String parameterName = (String) req.getParameter("parameterName");
		String value = req.getParameter("value");
		Object screen = req.getSession().getAttribute("instance");
		try {

			@SuppressWarnings("unchecked")
			Set<Method> validateMethods = getAllMethods(getTargetClass(screen), Predicates.and(withName(methodName)));

			for (Method method : validateMethods) {

				Class<?> rType = method.getReturnType();
				if (!rType.equals(Boolean.class) && !rType.equals(boolean.class)) {
					throw new Exception(String.format(
							"Return value of SugarValidator method %s.%s must be of type Boolean", method
									.getDeclaringClass().getName(), method.getName()));
				}

				Class<?> pType = method.getParameterTypes()[0];
				Object realValue = null;
				if (Reflector.isBean(pType)) {
					realValue = handleBeanType(req.getParameterMap(), parameterName, pType);
				} else if (!"".equals(value)){
					//realValue = Reflector.hardCast(value, pType);
					realValue = ERROR_THROWING_CONVERTER.convert(value, pType);
				}

				method.setAccessible(true);
				Boolean isValid = false;
				try{
					isValid = (Boolean) method.invoke(screen, realValue);
				}catch(InvocationTargetException e){
					resp.getOutputStream().print(String.format( String.format("Error executing Validation method : '%s.%s'", screen.getClass().getName(), method.getName()  )) );
					if(realValue == null){
						resp.getOutputStream().print(" (Possible null check error)");
					}
					resp.setStatus(SUGAR_VALIDATION_ERROR);
					return;
				}
				if (!isValid) {
					Validator sugarValidator = method.getAnnotation(Validator.class);
					if (sugarValidator != null) {
						try {		
							resp.getOutputStream().print(sugarValidator.value());
							resp.setStatus(SUGAR_VALIDATION_ERROR);
							
						} catch (IOException e) {
							throw new SugarException(e.getMessage(), e);
						}
					}
				}

			}

		} catch (Exception e) {

			try {
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			} catch (IOException e1) {
				throw new SugarException(e.getMessage(), e1);
			}
		}

	}

	private Object handleBeanType(Map<String, ?> parameterMap, String key, Class<?> type) {

		return FormUtil.populate(type, parameterMap);

	}

}
