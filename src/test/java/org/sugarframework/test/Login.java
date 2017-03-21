package org.sugarframework.test;

import org.sugarframework.Label;
import org.sugarframework.Order;
import org.sugarframework.RedirectOnSuccess;
import org.sugarframework.View;
import org.sugarframework.component.common.Action;
import org.sugarframework.component.common.Descriptor;
import org.sugarframework.security.Authenticate;
import org.sugarframework.security.Password;
import org.sugarframework.security.Principal;

@View(url="login", value = "login", isStatic = true)
public class Login {
	
	@Order(0) @Descriptor(value={"#{login.page.title}", "#{login.page.desc}"}) Void descriptor;

	@Order(1) @Authenticate
	@Action("Login")
	@RedirectOnSuccess("Restrict")
	public void login(
			@Principal
			@Label("Username") 
			String username, 
			
			@Password 
			@Label("Password") 
			String password){
		System.out.println( String.format("Login as %s [%s]", username, password) );
	}
	
}
