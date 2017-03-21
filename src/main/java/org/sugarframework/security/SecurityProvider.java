package org.sugarframework.security;

public interface SecurityProvider {
	
	Object getSubject();
	
	boolean isPermitted(Object subject, String permission);
	
	boolean hasRole(Object subject, String role);

}
