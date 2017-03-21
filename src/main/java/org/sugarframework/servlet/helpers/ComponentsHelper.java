package org.sugarframework.servlet.helpers;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.List;

import org.sugarframework.WithinColumn;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentRegistry.ComponentRenderAction;
import org.sugarframework.component.ComponentRegistry.FilterValid;
import org.sugarframework.context.DefaultContextInitializer;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

class ComponentsHelper implements Helper<Object> {

	public static final Helper<Object> INSTANCE = new ComponentsHelper();

	public static final String NAME = "components";

	@Override
	public CharSequence apply(final Object page, final Options options) throws IOException {

		List<String> renderings;

		String renderedOutput = "";
		try {
			renderings = DefaultContextInitializer.getContext().componentRegistry()
					.processComponents(page, new FilterValid() {

						@Override
						public boolean filter(AccessibleObject field,
								AbstractSugarComponent<Annotation, Object, AccessibleObject> component) {
							return !field.isAnnotationPresent(WithinColumn.class);
						}
					}, new ComponentRenderAction() {

						@Override
						public String render(Object thePage,
								AbstractSugarComponent<Annotation, Object, AccessibleObject> component,
								Annotation fieldAnnotation, AccessibleObject field) {
							return DefaultContextInitializer.getContext().componentRegistry()
									.renderComponent(component, fieldAnnotation, field, thePage);
						}
					});

			if (!renderings.isEmpty()) {

				for (String rendering : renderings) {
					renderedOutput += rendering;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return renderedOutput;
	}
}
