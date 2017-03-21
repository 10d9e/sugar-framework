package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Breadcrumb.class)
public class BreadCrumbComponent extends AbstractSugarComponent<Breadcrumb, Object, Field> {

	@Override
	public String render(Breadcrumb annotation, Object value, Field member) {

		String[] vals = ev(annotation.value());

		String out = "<ol class=\"breadcrumb\">";

		for (String s : vals) {
			out += String.format("<li><a href=\"#\">%s</a></li>", s);
		}

		out += "</ol>";

		return out;
	}

}
