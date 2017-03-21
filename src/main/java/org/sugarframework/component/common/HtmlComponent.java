package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Html.class)
public class HtmlComponent extends AbstractSugarComponent<Html, Void, Field> {

	@Override
	public String render(Html anno, Void data, Field member) {
		return ev(anno.value());
	}

}
