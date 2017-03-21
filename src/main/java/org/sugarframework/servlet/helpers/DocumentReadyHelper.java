package org.sugarframework.servlet.helpers;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

import org.sugarframework.Live;
import org.sugarframework.SugarException;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentRegistry.ComponentRenderAction;
import org.sugarframework.context.DefaultContextInitializer;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

class DocumentReadyHelper implements Helper<Object> {
	
	public static final Helper<Object> INSTANCE = new DocumentReadyHelper();

	public static final String NAME = "documentReady";

	@Override
	public CharSequence apply(Object page, Options options) throws IOException {
		
		String renderedOutput = "";
		
		try {
			
			List<String> renderings = DefaultContextInitializer.getContext().componentRegistry().processComponents(page,
					new ComponentRenderAction() {

						@Override
						public String render(Object thePage, AbstractSugarComponent<Annotation, Object, AccessibleObject> component,
								Annotation fieldAnnotation, AccessibleObject object) {

							Object value = null;
							try {
								if(object instanceof Field)
								value = ((Field)object).get(thePage);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								throw new SugarException(e.getMessage(), e);
							}

							String rendering = component.doDocumentReady(fieldAnnotation, value,(Member) object);

							Live live = object.getAnnotation(Live.class);
							if (live != null) {
								rendering += component.getLiveJavascript((Member)object);
							}
							return rendering;
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
