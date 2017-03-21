package org.sugarframework.tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TodayTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String mFormat;

	public void setFormat(String pFormat) {
		mFormat = pFormat;
	}

	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			Date today = new Date();
			SimpleDateFormat dateFormatter = new SimpleDateFormat(mFormat);
			out.print(dateFormatter.format(today));

		} catch (IOException ioe) {
			throw new JspException("Error: " + ioe.getMessage());
		}
		return SKIP_BODY;
	}

}
