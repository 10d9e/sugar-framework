package org.sugarframework.component.common;

import java.lang.reflect.Field;
import java.util.Map;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.util.ComponentUtil;

@SugarComponent(ButtonGroup.class)
public class ButtonGroupComponent extends AbstractSugarComponent<ButtonGroup, Map<String, String>, Field> {

	@Override
	public String render(ButtonGroup anno, Map<String, String> data, Field member) {
		String out = "<div class=\"btn-group\">";

		for (Map.Entry<String, String> e : data.entrySet()) {

			String text = e.getKey();
			// String navigation = e.getValue();

			out += String.format("<button type=\"button\" class=\"btn btn-%s\">%s</button>", anno.style(), text);
		}

		out += "</div>";

		return ComponentUtil.embedInPanel(out, anno.style());
	}

}
