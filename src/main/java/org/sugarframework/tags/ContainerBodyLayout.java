package org.sugarframework.tags;

import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.sugarframework.Column;
import org.sugarframework.Container;
import org.sugarframework.Row;

public class ContainerBodyLayout extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	private Object page;

	public void setPage(Object page) {
		this.page = page;
	}

	public int doStartTag() throws JspException {

		try {
			pageContext.getOut().println("<div class=\"container\" id=\"main-container\">");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_BODY_BUFFERED;
	}

	public int doAfterBody() throws JspException {

		try {
			BodyContent bc = getBodyContent();
			String content = bc.getString();

			StringWriter containerBuffer = new StringWriter();

			Container container = getTargetClass(page).getAnnotation(Container.class);

			if (container != null) {

				for (Row row : container.rows()) {

					containerBuffer.write(String.format("<div id=\"%s\" class=\"row\">\n", row.value()));

					for (Column column : row.columns()) {
						containerBuffer.append(String.format("\t<div id=\"%s\" class=\"%s\">\n", column.value(),
								column.width()));
						containerBuffer.append("\t\tHello " + column.value() + "\n");
						containerBuffer.append("\t</div>\n");
					}

					containerBuffer.append("</div>\n");

				}
			}

			bc.getEnclosingWriter().print(content);

			bc.getEnclosingWriter().print(containerBuffer.toString());

		} catch (Exception e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().println("</div>");
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

}
