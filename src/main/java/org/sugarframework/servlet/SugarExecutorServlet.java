package org.sugarframework.servlet;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.sugarframework.util.ClassUtils.getTargetClass;
import static org.sugarframework.context.DefaultContextInitializer.CONVERTER;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugarframework.ExceptionHandler;
import org.sugarframework.MessageOnException;
import org.sugarframework.MessageOnSuccess;
import org.sugarframework.RedirectOnSuccess;
import org.sugarframework.ReloadOnSuccess;
import org.sugarframework.SugarException;
import org.sugarframework.View;
import org.sugarframework.aspect.Aspect;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.util.EvalUtil;
import org.sugarframework.util.FormUtil;
import org.sugarframework.util.Reflector;

import com.google.common.base.Predicates;

public class SugarExecutorServlet extends HttpServlet {

	static final long serialVersionUID = 1L;

	static final short SUGAR_ERROR = 279;

	private static final short SUGAR_REDIRECT = 278;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

		View currentScreen = (View) req.getSession().getAttribute("view");

		String methodName = (String) req.getParameter("methodName");

		Object screen = req.getSession().getAttribute(currentScreen.value());

		@SuppressWarnings("unchecked")
		Map<?, ?> parameterMap = new HashMap<Object, Object>(req.getParameterMap());

		parameterMap.remove("methodName");

		String messageOnException = null;

		try {

			for (Method method : getTargetClass(screen).getDeclaredMethods()) {

				if (!method.getName().equals(methodName)) {
					continue;
				}

				Class<?>[] types = method.getParameterTypes();

				Object[] args = new Object[types.length];

				int c = 0;

				for (Map.Entry<String, Class<?>> e : Reflector.getParameters(method).entrySet()) {
					String key = EvalUtil.ev(e.getKey());
					Class<?> type = e.getValue();

					if (type.equals(Boolean.class) || type.equals(boolean.class)) {
						if (parameterMap.get(key) == null) {
							args[c++] = Boolean.FALSE;
							continue;
						}
					}

					Object realValue = null;

					if (Reflector.isBean(type)) {
						realValue = handleBeanType(parameterMap, key, type);
					} else {
						String value = ((String[]) parameterMap.get(key))[0];
						if("".equals(value) && java.util.Date.class.equals(type)){
							realValue = null;
						}else{
							realValue = CONVERTER.convert(value, type);
						}

					}

					args[c++] = realValue;
				}

				messageOnException = getMessageOnException(method);

				handleAspects(screen, method, args);

				method.invoke(screen, args);

				boolean willRedirect = checkRedirect(req, resp, method);

				String successMessage = getMessageOnSuccess(method);

				if (successMessage != null && !willRedirect) {
					try {
						resp.getWriter().append(successMessage);
					} catch (IOException e) {
						throw new SugarException(e.getMessage(), e);
					}
				} else if (checkReload(method)) {
					resp.addHeader("SugarLocation", currentScreen.url());
					resp.getOutputStream().print(currentScreen.url());
					resp.setStatus(SUGAR_REDIRECT);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();

			try {
				handleExceptionDelegates(e, screen);
				String errorMessage = String.format("%s : %s", "Error", e.getMessage());
				if (messageOnException != null) {
					errorMessage = messageOnException;
				}

				resp.getOutputStream().print(errorMessage);
				resp.setStatus(SUGAR_ERROR);

			} catch (Exception x) {
				throw new SugarException(e.getMessage(), x);
			}

		}

	}

	private void handleAspects(Object page, Method method, Object[] args) {

		for (Annotation a : method.getDeclaredAnnotations()) {

			if (a.annotationType().isAnnotationPresent(Aspect.class)) {
				DefaultContextInitializer.getContext().aspectRegistry().handle(a, page, method, args);
			}
		}

	}

	private Object handleBeanType(Map<?, ?> parameterMap, String key, Class<?> type) {

		Map<String, Object> map = new HashMap<>();

		for (Entry<?, ?> e : parameterMap.entrySet()) {

			String name = (String) e.getKey();

			if (name.startsWith(key)) {

				String[] ss = name.split("\\.");

				String objectName = ss[0];
				String pName = ss[1];

				if (objectName.equals(key)) {
					map.put(pName, e.getValue());
				}
			}
		}

		return FormUtil.populate(type, map);

	}

	private boolean checkReload(Method method) {
		ReloadOnSuccess redirectOnSuccess = method.getAnnotation(ReloadOnSuccess.class);
		return redirectOnSuccess != null;
	}

	private boolean checkRedirect(HttpServletRequest req, HttpServletResponse resp, Method method) throws Exception {

		RedirectOnSuccess redirectOnSuccess = method.getAnnotation(RedirectOnSuccess.class);

		if (redirectOnSuccess != null) {

			String pageKey = redirectOnSuccess.value();

			@SuppressWarnings("unchecked")
			Set<View> screens = (Set<View>) req.getSession().getAttribute("views");

			View found = null;
			for (View page : screens) {
				if (page.value().equals(pageKey)) {
					found = page;
					break;
				}
			}

			if (found == null) {
				throw new Exception("Could not redirect to page with @RedirectOnSuccess - pageName = " + pageKey);
			}

			resp.addHeader("SugarLocation", found.url());
			resp.getOutputStream().print(found.url());
			resp.setStatus(SUGAR_REDIRECT);
		}

		return redirectOnSuccess != null;
	}

	private void handleExceptionDelegates(Exception e, Object screen) throws Exception {

		@SuppressWarnings("unchecked")
		Set<Method> exceptionHandlers = getAllMethods(getTargetClass(screen),
				Predicates.and(withAnnotation(ExceptionHandler.class)));

		for (Method method : exceptionHandlers) {

			for (Annotation a : method.getAnnotations()) {
				method.invoke(screen, e);
			}

		}

	}

	private final String getMessageOnSuccess(final Method m) {
		String message = null;
		MessageOnSuccess messageOnException = m.getAnnotation(MessageOnSuccess.class);
		if (messageOnException != null) {
			message = messageOnException.value();
		}
		return message;
	}

	private final String getMessageOnException(final Method m) {

		String message = null;
		MessageOnException messageOnException = m.getAnnotation(MessageOnException.class);
		if (messageOnException != null) {
			message = messageOnException.value();
		}
		return message;
	}

}
