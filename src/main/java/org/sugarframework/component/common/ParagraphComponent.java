package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Paragraph.class)
public class ParagraphComponent extends AbstractSugarComponent<Paragraph, Object, Field> {

	@Override
	public String render(Paragraph anno, Object data, Field member) {
		String out = "";

		out += String.format("<p class=\"%s %s %s\">", anno.style(), anno.textStyle(), anno.backgroundStyle());
		out += ev(anno.value());
		out += "</p>";

		return out;
	}

}
