package org.sugarframework.security;

import static org.sugarframework.context.DefaultContextInitializer.getContext;

import java.lang.reflect.Method;

import org.sugarframework.SugarAspectHandler;
import org.sugarframework.aspect.AbstractAspectHandler;

@SugarAspectHandler(RestrictPermission.class)
public class RestrictPermissionHandler extends AbstractAspectHandler<RestrictPermission> {

	@Override
	public void handle(Object page, RestrictPermission annotation, Method method, Object[] args) {
		
		Object subject = getContext().securityProvider().getSubject();
		
		boolean permission = false;
		for(String perm : annotation.value()){

			if(getContext().securityProvider().isPermitted(subject, perm)){
				permission = true;
			}
		}
		
		if(!permission){
			throw new AuthorizationException(String.format("%s does not have permission to access resource [%s]", subject, method));
		}

	}
}
