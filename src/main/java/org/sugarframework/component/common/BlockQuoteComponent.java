package org.sugarframework.component.common;

import java.lang.reflect.Field;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(BlockQuote.class)
public class BlockQuoteComponent extends AbstractSugarComponent<BlockQuote, Object, Field> {

	@Override
	public String render(BlockQuote anno, Object data, Field member) {

		String[] vals = ev(anno.value());

		String out = String.format("<blockquote class=\"%s\">", anno.reverse() ? "blockquote-reverse" : "");
		out += String.format("<p>%s</p>", vals[0]);

		if (vals.length > 1) {

			out += String.format("<footer>%s</footer>", vals[1]);
		}
		out += "</blockquote>";

		return out;
	}

}
