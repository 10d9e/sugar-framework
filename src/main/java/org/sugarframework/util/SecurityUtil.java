package org.sugarframework.util;

import static org.sugarframework.context.DefaultContextInitializer.getContext;

import java.lang.reflect.AccessibleObject;

import org.sugarframework.security.RestrictPermission;
import org.sugarframework.security.RestrictRole;

public final class SecurityUtil {
	
	public static final boolean checkPermissions(Class<?> object){
		
		boolean permission = true;

		if (object.isAnnotationPresent(RestrictRole.class)) {

			Object subject = getContext().securityProvider().getSubject();
			
			RestrictRole restrictRole = object.getAnnotation(RestrictRole.class);

			for (String role : restrictRole.value()) {

				if(!getContext().securityProvider().hasRole(subject, role)){
					permission = false;
				}
			}
		}


		if (object.isAnnotationPresent(RestrictPermission.class)) {
			
			Object subject = getContext().securityProvider().getSubject();
			
			RestrictPermission restrictPermission = object.getAnnotation(RestrictPermission.class);

			for (String perm : restrictPermission.value()) {

				if(!getContext().securityProvider().isPermitted(subject, perm)){
					permission = false;
				}
			}
		}

		return permission;
		
	}
	

	public static final boolean checkPermissions(AccessibleObject object) {

		boolean permission = true;

		if (object.isAnnotationPresent(RestrictRole.class)) {

			Object subject = getContext().securityProvider().getSubject();
			
			RestrictRole restrictRole = object.getAnnotation(RestrictRole.class);

			for (String role : restrictRole.value()) {

				if(!getContext().securityProvider().hasRole(subject, role)){
					permission = false;
				}
			}
		}


		if (object.isAnnotationPresent(RestrictPermission.class)) {
			
			Object subject = getContext().securityProvider().getSubject();
			
			RestrictPermission restrictPermission = object.getAnnotation(RestrictPermission.class);

			for (String perm : restrictPermission.value()) {

				if(!getContext().securityProvider().isPermitted(subject, perm)){
					permission = false;
				}
			}
		}

		return permission;
	}
	
}
