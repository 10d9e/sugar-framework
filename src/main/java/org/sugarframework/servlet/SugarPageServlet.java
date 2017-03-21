package org.sugarframework.servlet;

import static org.sugarframework.util.ClassUtils.getTargetClass;
import static org.sugarframework.util.SecurityUtil.checkPermissions;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sugarframework.Context;
import org.sugarframework.SugarException;
import org.sugarframework.Tuple;
import org.sugarframework.View;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.servlet.helpers.SugarPageContext;
import org.sugarframework.util.FileUtils;
import org.sugarframework.util.HtmlUtil;
import org.sugarframework.util.Reflector;
import org.sugarframework.util.ViewInstanceUtil;

public class SugarPageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Class<?> sugarScreenClass;

	private View screenAnnotation;

	private Set<View> screens;

	private Context context;
	
	private SugarPageContext pageContext = new SugarPageContext();

	public SugarPageServlet(Context context, View screenAnnotation, Class<?> sugarScreenClass, Set<View> views) {

		this.context = context;
		this.screenAnnotation = screenAnnotation;
		this.sugarScreenClass = sugarScreenClass;
		this.screens = views;

	}

	public Class<?> getSugarScreenClass() {
		return sugarScreenClass;
	}

	public void setSugarScreenClass(Class<?> sugarScreenClass) {
		this.sugarScreenClass = sugarScreenClass;
	}

	public View getScreenAnnotation() {
		return screenAnnotation;
	}

	public void setScreenAnnotation(View screenAnnotation) {
		this.screenAnnotation = screenAnnotation;
	}

	public Set<View> getScreens() {
		return screens;
	}

	public void setScreens(Set<View> screens) {
		this.screens = screens;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String field = (String) request.getParameter("field");
		if (field != null) {
			renderSingleComponent(field, request, response);
			return;
		}

		Object screenInstance = null;
		try {
			screenInstance = getScreenInstance(request.getSession());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check page restrictions
		if (!checkPermissions(screenInstance.getClass())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			PrintWriter out = response.getWriter();
			out.println( String.format("User does not have access to resource '%s'", screenAnnotation.value() ) );
			out.close();
		}

		request.getSession().setAttribute("view", screenAnnotation);
		request.getSession().setAttribute("views", screens);
		request.getSession().setAttribute("context", context);
		request.getSession().setAttribute("instance", screenInstance);
		//request.getSession().setAttribute(screenAnnotation.value(), screenInstance);

		//request.getRequestDispatcher(context.template()).forward(request, response);
		
		String templateFile = getServletContext().getRealPath("") + "/" + context.template();
		
		if(!FileUtils.exists(templateFile)){			
			templateFile = FileUtils.findFile(context.template());
		}
				
		List<Tuple<View, Class<?>>> currentViews = (List<Tuple<View, Class<?>>>) request.getServletContext().getAttribute("currentViews");
		
		response.getWriter().print( pageContext.apply(screenAnnotation, currentViews, context, screenInstance, templateFile) );
	}

	
	
	private void renderSingleComponent(String fieldName, HttpServletRequest request, HttpServletResponse response) {
		try {

			final Object screenInstance = ViewInstanceUtil.getScreenInstanceInternal(request.getSession(true), sugarScreenClass);
			
			AccessibleObject fieldOrMethod = Reflector.find(fieldName, getTargetClass(screenInstance));

			String rendering = DefaultContextInitializer.getContext().componentRegistry().renderComponent(fieldOrMethod, screenInstance);

			response.getWriter().println(HtmlUtil.tidy(rendering));

			response.setHeader("Content-Type", "text/plain");
			
			response.setHeader("success", "yes");

		} catch (Exception e) {
			throw new SugarException(e.getMessage(), e);
		}
	}
	
	

	public void setScreenInstance(HttpSession session, Object instance) {
		session.setAttribute(screenAnnotation.value(), instance);
	}

	public Object getScreenInstance(HttpSession session) throws Exception {

		return ViewInstanceUtil.getScreenInstance(session, this, sugarScreenClass);
	}

}
