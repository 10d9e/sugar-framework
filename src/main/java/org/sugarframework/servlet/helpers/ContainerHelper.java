package org.sugarframework.servlet.helpers;

import static org.sugarframework.util.ClassUtils.getTargetClass;
import static org.sugarframework.util.SecurityUtil.checkPermissions;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sugarframework.Column;
import org.sugarframework.Container;
import org.sugarframework.Row;
import org.sugarframework.SugarException;
import org.sugarframework.WithinColumn;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentRegistry.ComponentRenderAction;
import org.sugarframework.component.ComponentRegistry.FilterValid;
import org.sugarframework.context.DefaultContextInitializer;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

class ContainerHelper implements Helper<Object> {

	public static final Helper<Object> INSTANCE = new ContainerHelper();

	public static final String NAME = "container";
	
	public Map<String, List<String>> getRenderingsMap(Object page) {

		final Map<String, List<String>> renderingsMap = new HashMap<>();

		try {
			DefaultContextInitializer.getContext().componentRegistry().processComponents(page, new FilterValid() {

				@Override
				public boolean filter(AccessibleObject field, AbstractSugarComponent<Annotation, Object, AccessibleObject> component) {
					return field.isAnnotationPresent(WithinColumn.class);
				}
			}, new ComponentRenderAction() {

				@Override
				public String render(Object thePage, AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation fieldAnnotation,
						AccessibleObject field) {

					Object value = null;
					try {
						if(field instanceof Field){
							value = ((Field)field).get(thePage);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new SugarException(e.getMessage(), e);
					}
					WithinColumn within = field.getAnnotation(WithinColumn.class);
					
					String rendering = "";
					if (checkPermissions(field)) {
						rendering = component.doRender(fieldAnnotation, value, field);
					}

					List<String> renderings = renderingsMap.get(within.value());
					if (renderings == null) {
						renderings = new ArrayList<>();
					}
					renderings.add(rendering);
					renderingsMap.put(within.value(), renderings);

					return "";
				}
			});
			
			return renderingsMap;
			
		} catch (Exception e) {
			throw new SugarException(e.getMessage(), e);
		}
	}

	@Override
	public CharSequence apply(final Object page, final Options options) throws IOException {

		StringWriter containerBuffer = new StringWriter();

		Container container = getTargetClass(page).getAnnotation(Container.class);

		if (container != null) {

			for (Row row : container.rows()) {

				containerBuffer.write(String.format("<div id=\"%s\" class=\"row\">\n", row.value()));

				for (Column column : row.columns()) {
					containerBuffer.append(String.format("\t<div id=\"%s\" class=\"%s\">\n", column.value(),
							column.width()));
					// containerBuffer.append("\t\tHello " + column.value()
					// + "\n");

					for (String rendering : getRenderingsMap(page).get(column.value())) {
						containerBuffer.append(rendering);
					}

					containerBuffer.append("\t</div>\n");
				}

				containerBuffer.append("</div>\n");

			}
		}

		return containerBuffer.toString();

	}
}

