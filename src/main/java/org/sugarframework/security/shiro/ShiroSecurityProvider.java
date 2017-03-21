package org.sugarframework.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.sugarframework.security.SecurityProvider;

public class ShiroSecurityProvider implements SecurityProvider {

	@Override
	public Object getSubject() {
		return SecurityUtils.getSubject();
	}

	@Override
	public boolean isPermitted(Object subject, String permission) {
		return ((Subject)subject).isPermitted(permission);
	}

	@Override
	public boolean hasRole(Object subject, String role) {
		return ((Subject)subject).hasRole(role);
	}

}
