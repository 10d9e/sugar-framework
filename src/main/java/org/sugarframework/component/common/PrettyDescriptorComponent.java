package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(PrettyDescriptor.class)
public class PrettyDescriptorComponent extends AbstractSugarComponent<PrettyDescriptor, Object, Field> {

	@Override
	public String render(PrettyDescriptor anno, Object data, Field member) {

		String[] vals = ev(anno.value());

		String format = "<div class=\"bs-callout bs-callout-%s\"> <h4>%s</h4> <p>%s</p> </div>";

		return String.format(format, anno.style(), vals[0], vals[1]);
	}

}
