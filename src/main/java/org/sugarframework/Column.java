package org.sugarframework;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.sugarframework.component.ColumnWidth;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String value();

	ColumnWidth width();
}
