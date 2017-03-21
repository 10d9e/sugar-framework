package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(ComponentGroup.class)
public class ComponentGroupComponent extends AbstractSugarComponent<ComponentGroup, Object, Field> {

	@Override
	public String render(ComponentGroup anno, Object data, Field member) {
		
		String[] vals = ev(anno.value());

		String out = "<address>";
		out += String.format("<strong>%s</strong><br>", vals[0]);

		for (int i = 1; i < vals.length; i++) {
			out += vals[i] + "<br>";
		}

		out += "</address>";

		return out;
	}

}
