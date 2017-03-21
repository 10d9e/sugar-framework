package org.sugarframework.component.common;

import java.lang.reflect.AccessibleObject;
import java.util.Collection;

import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(UnorderedList.class)
public class UnorderedListComponent extends AbstractSugarComponent<UnorderedList, Collection<String>, AccessibleObject> {

	@Override
	public String render(UnorderedList annotation, Collection<String> value, AccessibleObject member) {

		String out = String.format("<ul class=\"%s\">", annotation.style());

		for (String s : value) {
			out += String.format("<li class=\"list-group-item list-group-item-%s\">%s</li>", annotation.style(), s);
		}

		out += "</ul>";

		return out;
	}

}
