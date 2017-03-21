package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(SugarLabel.class)
public class SugarLabelComponent extends AbstractSugarComponent<SugarLabel, String, Field> {

	@Override
	public String render(SugarLabel annotation, String value, Field member) {
		return String.format("<span class=\"label label-%s\">%s</span>", annotation.style(), ev(annotation.value()));
	}

}
