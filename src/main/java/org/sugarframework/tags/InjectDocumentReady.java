package org.sugarframework.tags;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.sugarframework.Live;
import org.sugarframework.SugarException;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentRegistry.ComponentRenderAction;
import org.sugarframework.context.DefaultContextInitializer;

public class InjectDocumentReady extends TagSupport {

	private static final long serialVersionUID = 1L;

	private Object page;

	public int doStartTag() throws JspException {
		try {

			final Set<String> checkSet = new HashSet<>();

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

				JspWriter out = pageContext.getOut();

				for (String rendering : renderings) {
					out.print(rendering);
				}
			}

		} catch (Exception e) {
			throw new JspException("Error: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	public void setPage(Object page) {
		this.page = page;
	}
}
