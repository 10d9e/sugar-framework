package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Descriptor.class)
public class DescriptorComponent extends AbstractSugarComponent<Descriptor, Object, Field> {

	@Override
	public String render(Descriptor anno, Object data, Field member) {

		String[] vals = ev(anno.value());

		String format = "<div class=\"bs-component\"><div class=\"list-group\"> <a href=\"#\" class=\"list-group-item\">"
				+ "<h4 class=\"list-group-item-heading\">%s</h4> <p class=\"list-group-item-text\">%s</p> </a> </div> </div>";

		return String.format(format, vals[0], vals[1]);
	}
}
