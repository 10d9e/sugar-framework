package org.sugarframework.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.sugarframework.security.SecurityProvider;

public class ShiroSecurityProvider implements SecurityProvider<Subject> {

	@Override
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	@Override
	public boolean isPermitted(Subject subject, String permission) {
		return subject.isPermitted(permission);
	}

	@Override
	public boolean hasRole(Subject subject, String role) {
		return subject.hasRole(role);
	}

}
