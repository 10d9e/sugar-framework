package org.sugarframework.security;

public interface SecurityProvider<T> {
	
	T getSubject();
	
	boolean isPermitted(T subject, String permission);
	
	boolean hasRole(T subject, String role);

}
