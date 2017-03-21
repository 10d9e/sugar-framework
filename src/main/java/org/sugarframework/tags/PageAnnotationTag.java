package org.sugarframework.tags;

import static org.sugarframework.util.ClassUtils.getTargetClass;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.sugarframework.View;
import org.sugarframework.util.ViewUtil;

public class PageAnnotationTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private Object annotation;

	private String field;

	public int doStartTag() throws JspException {
		try {
			Object value;
			
			
			if("url".equals(field) && annotation instanceof View){
				value = ViewUtil.url((View) annotation);
			}else{
			
				value = getTargetClass(annotation).getMethod(field).invoke(annotation);

			}
			if (value != null) {
				JspWriter out = pageContext.getOut();
				out.print(value);
			}

		} catch (Exception e) {
			throw new JspException("Error: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setAnnotation(Object annotation) {
		this.annotation = annotation;
	}
}
