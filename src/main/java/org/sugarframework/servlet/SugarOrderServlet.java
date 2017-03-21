package org.sugarframework.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugarframework.util.SourceUtil;

public class SugarOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		
		String json = req.getParameter("components");
		
		Object instance = req.getSession(true).getAttribute("instance");
				
		try {
			SourceUtil.updateComponentOrder(json, instance);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
