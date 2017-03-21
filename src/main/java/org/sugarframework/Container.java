package org.sugarframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sugarframework.component.ColumnWidth;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Container {

	boolean fluid() default false;

	Row[] rows() default { @Row(value = "row1", columns = { @Column(value = "container1", width = ColumnWidth.TWELVE) }) };
}
