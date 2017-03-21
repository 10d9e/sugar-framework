package org.sugarframework.component.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sugarframework.component.ListStyle;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface UnorderedList {
	ListStyle style() default ListStyle.DEFAULT;
}
