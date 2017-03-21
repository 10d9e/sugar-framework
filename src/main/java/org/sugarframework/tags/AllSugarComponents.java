package org.sugarframework.tags;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sugarframework.WithinColumn;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.component.ComponentRegistry.ComponentRenderAction;
import org.sugarframework.component.ComponentRegistry.FilterValid;
import org.sugarframework.context.DefaultContextInitializer;

public class AllSugarComponents extends TagSupport {
	
	private Log log = LogFactory.getLog(getClass());

	private static final long serialVersionUID = 1L;

	private Object page;

	public int doStartTag() throws JspException {
		try {

			List<String> renderings = DefaultContextInitializer.getContext().componentRegistry().processComponents(page,
					new FilterValid() {

						@Override
						public boolean filter(AccessibleObject field, AbstractSugarComponent<Annotation, Object, AccessibleObject> component) {
							return !field.isAnnotationPresent(WithinColumn.class);
						}
					}, new ComponentRenderAction() {

						@Override
						public String render(Object thePage, AbstractSugarComponent<Annotation, Object, AccessibleObject> component,
								Annotation fieldAnnotation, AccessibleObject field) {
							return DefaultContextInitializer.getContext().componentRegistry().renderComponent(component,
									fieldAnnotation, field, thePage);
						}
					});

			if (!renderings.isEmpty()) {

				JspWriter out = pageContext.getOut();

				for (String rendering : renderings) {
					out.print(rendering);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JspException("Error: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	public void setPage(Object page) {
		this.page = page;
	}
}
