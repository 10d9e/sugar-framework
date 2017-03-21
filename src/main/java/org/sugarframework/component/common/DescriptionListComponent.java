package org.sugarframework.component.common;

import java.lang.reflect.Field;
import java.util.Map;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentException;
import org.sugarframework.util.Structures;

@SugarComponent(DescriptionList.class)
public class DescriptionListComponent extends AbstractSugarComponent<DescriptionList, Object, Field> {

	@Override
	public String render(DescriptionList annotation, Object data, Field member) {

		String out = String.format("<dl class=\"%s\">", annotation.horizontal() ? "dl-horizontal" : "");

		try {
			Map<String, String> value = ev(annotation.value());
			for (Map.Entry<String, String> e : value.entrySet()) {
				out += String.format("<dt>%s</dt> <dd>%s</dd>", e.getKey(), e.getValue());
			}

		} catch (Exception e) {
			throw new ComponentException(e.getMessage(), e);
		}

		out += "</dl>";

		return out;
	}

	public Map<String, String> map(String str) {
		return Structures.map(str);
	}

}
