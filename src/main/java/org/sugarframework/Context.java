package org.sugarframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sugarframework.data.DatasourceRegistry;
import org.sugarframework.data.DefaultDatasourceRegistry;
import org.sugarframework.security.SecurityProvider;
import org.sugarframework.security.shiro.ShiroSecurityProvider;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Context {
	String value() default "@Context.name";

	String urlContext() default "";

	String logo() default "assets-boot/img/sugar32.ico";

	String port() default "8080";

	String image() default "";

	String secure() default "true";

	String template() default "template.sugar";

	String style() default "bootstrap";

	String footerMessage() default "© Company 2016";

	String[] resourceDirectories() default {};

	String resourceBundle() default "messages";
	
	Class<? extends DatasourceRegistry> datasourceRegistry() default DefaultDatasourceRegistry.class;
	
	Class<? extends SecurityProvider> securityProvider() default ShiroSecurityProvider.class;

}
