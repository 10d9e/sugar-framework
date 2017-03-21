package org.sugarframework.component.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sugarframework.component.BackgroundStyle;
import org.sugarframework.component.ParagraphStyle;
import org.sugarframework.component.TextStyle;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Paragraph {

	String value();

	ParagraphStyle style() default ParagraphStyle.DEFAULT;

	TextStyle textStyle() default TextStyle.DEFAULT;

	BackgroundStyle backgroundStyle() default BackgroundStyle.DEFAULT;
}
