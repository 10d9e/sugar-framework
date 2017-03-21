package org.sugarframework.test.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface HelloWorld {
	String heading() default "Hello World Title";
    String message() default "Hello World";
}
