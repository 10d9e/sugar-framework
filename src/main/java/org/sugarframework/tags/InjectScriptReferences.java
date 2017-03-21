package org.sugarframework.tags;

import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.sugarframework.context.DefaultContextInitializer;

public class InjectScriptReferences extends TagSupport {

	private static final long serialVersionUID = 1L;

	private Object page;

	public int doStartTag() throws JspException {
		try {

			Set<String> renderings = DefaultContextInitializer.getContext().componentRegistry().renderComponentJavascript();

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
