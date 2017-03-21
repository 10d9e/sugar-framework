package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Image.class)
public class ImageComponent extends AbstractSugarComponent<Image, Object, Field> {

	@Override
	public String render(Image anno, Object data, Field member) {
		Object o = ev(anno.value());
		return String.format("<img src=\"%s\" class=\"img-responsive\" alt=\"Responsive image\">", o);
	}

}
