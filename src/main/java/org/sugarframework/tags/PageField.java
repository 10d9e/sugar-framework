package org.sugarframework.tags;

import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.lang.annotation.Annotation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PageField extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String fieldName;

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();

			Object object = pageContext.getSession().getAttribute("screenAnnotation");

			Annotation annotation = (Annotation) pageContext.getSession().getAttribute("screenAnnotation");

			Object value = getTargetClass(annotation).getMethod(fieldName).invoke(object);

			if (value != null) {
				out.print(value);
			}

		} catch (Exception ioe) {
			throw new JspException("Error: " + ioe.getMessage());
		}

		return SKIP_BODY;
	}

}
