package org.sugarframework.security;

import static org.sugarframework.context.DefaultContextInitializer.getContext;

import java.lang.reflect.Method;

import org.sugarframework.SugarAspectHandler;
import org.sugarframework.aspect.AbstractAspectHandler;

@SugarAspectHandler(RestrictRole.class)
public class RestrictRoleHandler extends AbstractAspectHandler<RestrictRole> {

	@Override
	public void handle(Object page, RestrictRole annotation, Method method, Object[] args) {
		
		Object subject = getContext().securityProvider().getSubject();
		
		boolean permission = false;
		for(String role : annotation.value()){

			if(getContext().securityProvider().hasRole(subject, role)){
				permission = true;
			}
		}
		
		if(!permission){
			throw new AuthorizationException(String.format("%s does not have permission to access resource [%s]", subject, method));
		}

	}
}
