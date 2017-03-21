package org.sugarframework.context;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.DispatcherType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.reflections.Reflections;
import org.sugarframework.Context;
import org.sugarframework.DevelopmentMode;
import org.sugarframework.SugarException;
import org.sugarframework.Tuple;
import org.sugarframework.View;
import org.sugarframework.aspect.AspectRegistry;
import org.sugarframework.component.ComponentRegistry;
import org.sugarframework.data.DatasourceRegistry;
import org.sugarframework.security.SecurityProvider;
import org.sugarframework.servlet.SugarExecutorServlet;
import org.sugarframework.servlet.SugarLayoutServlet;
import org.sugarframework.servlet.SugarOrderServlet;
import org.sugarframework.servlet.SugarPageServlet;
import org.sugarframework.servlet.SugarValidatorServlet;
import org.sugarframework.util.CompilerUtil;
import org.sugarframework.util.Defaults;
import org.sugarframework.util.Reflector;
import org.sugarframework.util.SourceUtil;
import org.sugarframework.util.ViewUtil;

public class DefaultContext {

	private Log log = LogFactory.getLog(DefaultContextInitializer.class);

	private final String[] SCAN_PACKAGES = { "org.sugarframework.component", "org.sugarframework.security" };

	private Set<String> resourceDirectories = new HashSet<String>();

	private Reflections reflections;

	private ResourceBundle bundle;

	private ComponentRegistry registry = new ComponentRegistry();

	private AspectRegistry aspectRegistry = new AspectRegistry();

	private String contextClassName;

	private Context context;

	private DevelopmentMode developmentMode;

	private Set<String> srcDirectories = new HashSet<>();

	private WebAppContext webContext = new WebAppContext();

	private DatasourceRegistry datasourceRegistry;

	private SecurityProvider securityProvider;
	
	private Map<Context, Collection<Class<?>>> screens = new HashMap<>();

	public DefaultContext(Class<?> contextClass) {

		try {
			Object testObject = contextClass.newInstance();

			context = testObject.getClass().getAnnotation(Context.class);

			if (context == null) {
				context = Defaults.of(Context.class);
			}

			if (context == null) {
				System.err.println("Could not find value @Context annotation on class " + testObject.getClass());
			}

			developmentMode = testObject.getClass().getAnnotation(DevelopmentMode.class);

			if (developmentMode != null) {

				log.info("Loading Context in DEVELOPMENT mode...");

				log.info("Estimating source directories");
				for (String src : SourceUtil.estimateSourceDirectories(contextClass, developmentMode.srcDirectories())) {
					log.info(src);
					srcDirectories.add(src);
				}

			}

			try {
				bundle = ResourceBundle.getBundle(context.resourceBundle());
			} catch (Exception e) {
				log.info(String.format("Warning : Could not find resource bundle [ %s ]",
						context.resourceBundle()));
			}

			resourceDirectories.add(contextClass.getPackage().getName());

			for (String rd : context.resourceDirectories()) {
				resourceDirectories.add(rd);
			}

			contextClassName = contextClass.getPackage().getName();

			reflections = new Reflections(contextClassName, SCAN_PACKAGES);

			registry.initialize(reflections);

			aspectRegistry.initialize(reflections);

			datasourceRegistry = context.datasourceRegistry().newInstance();

			securityProvider = context.securityProvider().newInstance();

			startHttpServer();

		} catch (Exception e) {
			throw new SugarException(e.getMessage(), e);
		}
	}
	
	public Set<String> resourceDirectories(){
		return resourceDirectories;
	}
	
	public Map<Context, Collection<Class<?>>> getScreens() {
		return screens;
	}

	public SecurityProvider securityProvider() {
		return securityProvider;
	}

	public Set<String> srcDirectories() {
		return srcDirectories;
	}

	public AspectRegistry aspectRegistry() {
		return aspectRegistry;
	}

	public DevelopmentMode developmentMode() {
		return developmentMode;
	}

	public DatasourceRegistry datasourceRegistry() {
		return datasourceRegistry;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public boolean isDevelopmentMode() {
		return developmentMode != null;
	}

	public Context getContext() {
		return context;
	}

	public Reflections reflections() {
		return reflections;
	}

	public ComponentRegistry componentRegistry() {
		return registry;
	}

	private void wrapTypeAnnotationWithProxy(AnnotatedElement element) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {

		for (Annotation page : element.getDeclaredAnnotations()) {

			Annotation annotation = (Annotation) Proxy.newProxyInstance(
					DefaultContextInitializer.class.getClassLoader(), new Class[] { page.annotationType() },
					new ResourceBundleHandler<Annotation>(page));

			Field field = Class.class.getDeclaredField("annotations");
			field.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) field
					.get(element);
			annotations.put(annotation.annotationType(), annotation);

		}

	}

	private void preProcessAnnotations(Set<Class<?>> sugarScreens) {

		try {

			for (final Class<?> sugarScreen : sugarScreens) {
				wrapTypeAnnotationWithProxy(sugarScreen);
			}

		} catch (Exception e) {
			throw new SugarException(e.getMessage(), e);
		}
	}

	public void reload(SugarPageServlet callee) {

		try {

			Map<String, Object> newObjects = new HashMap<>();

			for (String srcDir : srcDirectories()) {
				newObjects = CompilerUtil.compileAndLoadDirectory(srcDir);
			}

			Set<Class<?>> sugarScreens = new HashSet<>();

			for (Map.Entry<String, Object> e : newObjects.entrySet()) {

				Object instance = e.getValue();

				if (instance.getClass().isAnnotationPresent(View.class)) {
					sugarScreens.add(instance.getClass());
				}
			}

			removeServlets(webContext, SugarPageServlet.class);

			reflections = new Reflections(contextClassName, SCAN_PACKAGES);

			processInternal(sugarScreens, callee);

		} catch (Exception e) {
			throw new SugarException(e.getMessage(), e);
		}

	}

	private void removeServlets(WebAppContext webAppContext, Class<?> servlet) throws Exception {

		ServletHandler handler = webAppContext.getServletHandler();

		List<ServletHolder> servlets = new ArrayList<ServletHolder>();

		Set<String> names = new HashSet<String>();

		for (ServletHolder holder : handler.getServlets()) {

			if (servlet.isInstance(holder.getServlet())) {
				names.add(holder.getName());
			} else {
				servlets.add(holder);
			}
		}

		List<ServletMapping> mappings = new ArrayList<ServletMapping>();

		for (ServletMapping mapping : handler.getServletMappings()) {

			if (!names.contains(mapping.getServletName())) {
				mappings.add(mapping);
			}
		}

		handler.setServletMappings(mappings.toArray(new ServletMapping[0]));
		handler.setServlets(servlets.toArray(new ServletHolder[0]));

	}
	
	private void processInternal(Set<Class<?>> sugarScreens, SugarPageServlet callee) {

		preProcessAnnotations(sugarScreens);

		Collection<Class<?>> orderedSugarScreens = Reflector.order(sugarScreens);
		
		List<Tuple<View, Class<?>>> currentViews = new ArrayList<>();

		Set<View> screens = new LinkedHashSet<View>();
		for (Class<?> sugarScreen : orderedSugarScreens) {
			View screenAnnotation = sugarScreen.getAnnotation(View.class);
			screens.add(screenAnnotation);
			
			currentViews.add(new Tuple<View, Class<?>>(screenAnnotation, sugarScreen));
		}
		
		// setting currentScreen classes
		webContext.setAttribute("currentViews", currentViews);

		for (Class<?> sugarScreen : orderedSugarScreens) {

			View screenAnnotation = sugarScreen.getAnnotation(View.class);

			String url = ViewUtil.url(screenAnnotation);

			webContext.addServlet(new ServletHolder(new SugarPageServlet(context, screenAnnotation, sugarScreen,
					screens)), "/" + url);

			if (callee != null && callee.getSugarScreenClass().getName().equals(sugarScreen.getName())) {
				callee.setContext(context);
				callee.setScreenAnnotation(screenAnnotation);
				callee.setSugarScreenClass(sugarScreen);
				callee.setScreens(screens);
			}

		}
	}

	private void process() {

		Set<Class<?>> sugarScreens = reflections.getTypesAnnotatedWith(View.class);

		screens.put(context, sugarScreens);

		processInternal(sugarScreens, null);

	}

	private String[] build() throws MalformedURLException, IOException {

		log.info("Scanning for resources ...");

		List<String> resources = new ArrayList<String>();

		resources.add(DefaultContextInitializer.class.getClassLoader().getResource("org/sugarframework/web")
				.toExternalForm());

		for (String path : resourceDirectories) {
			String fullPath = new String(path).replace('.', '/');
			log.info("Adding user defined resource path - " + fullPath);
			resources.add(DefaultContextInitializer.class.getClassLoader().getResource(fullPath).toExternalForm());
		}

		return resources.toArray(new String[resources.size()]);
	}

	private void startHttpServer() throws Exception {

		String contextName = context.urlContext();

		int port = Integer.valueOf(context.port());

		final Server server = new Server(port);

		final String CONTEXTPATH = "/" + contextName;

	//	webContext.setDescriptor("WEB-INF/web.xml");
		webContext.setBaseResource(new ResourceCollection(build()));
		webContext.setContextPath(CONTEXTPATH);
		webContext.setParentLoaderPriority(true);
		webContext.addServlet(new ServletHolder(new SugarExecutorServlet()), "/execute");
		webContext.addServlet(new ServletHolder(new SugarValidatorServlet()), "/validate");
		webContext.addServlet(new ServletHolder(new SugarLayoutServlet()), "/editlayout");
		webContext.addServlet(new ServletHolder(new SugarOrderServlet()), "/editorder");
		
	//	webContext.addEventListener(new SugarViewSessionListener());

		// Shiro specific
		webContext.addEventListener(new EnvironmentLoaderListener());
		FilterHolder filterHolder = new FilterHolder();
		filterHolder.setFilter(new ShiroFilter());
		EnumSet<DispatcherType> types = EnumSet.allOf(DispatcherType.class);
		webContext.addFilter(filterHolder, "/*", types);

		process();

		server.setHandler(webContext);

		webContext.preConfigure();

		server.start();

	}

}
