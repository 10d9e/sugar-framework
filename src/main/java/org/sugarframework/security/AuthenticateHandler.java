package org.sugarframework.security;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.sugarframework.SugarAspectHandler;
import org.sugarframework.aspect.AbstractAspectHandler;

@SugarAspectHandler(Authenticate.class)
public class AuthenticateHandler extends AbstractAspectHandler<Authenticate> {

	private boolean hasAnnotation(Annotation[] annos, Class<?> type) {
		for (Annotation a : annos) {
			if (a.annotationType().equals(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(Object page, Authenticate annotation, Method method, Object[] args) {

		String username = null;
		String password = null;
		int i = 0;
		for (Object parameter : args) {

			if (hasAnnotation(method.getParameterAnnotations()[i], Principal.class)) {
				username = parameter.toString();
			}

			if (hasAnnotation(method.getParameterAnnotations()[i], Password.class)) {
				password = parameter.toString();
			}
			i++;
		}
		
		if(username == null){
			throw new org.sugarframework.security.AuthenticationException("@Prinicipal parameter annotation not found on method " + method);
		}

		if(password == null){
			throw new org.sugarframework.security.AuthenticationException("@Password parameter annotation not found on method " + method);

		}

		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			token.setRememberMe(true);

			Subject currentUser = SecurityUtils.getSubject();

			currentUser.login(token);
						
		} catch (AuthenticationException e) {
			throw new org.sugarframework.security.AuthenticationException(e);
		}

	}
}
