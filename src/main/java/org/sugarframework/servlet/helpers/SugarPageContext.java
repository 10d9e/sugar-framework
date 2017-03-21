package org.sugarframework.servlet.helpers;

import static org.sugarframework.util.SecurityUtil.checkPermissions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.sugarframework.Context;
import org.sugarframework.Tuple;
import org.sugarframework.View;
import org.sugarframework.context.DefaultContextInitializer;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;

public class SugarPageContext {

	private Handlebars handlebars = new Handlebars();
	{
		handlebars.prettyPrint(true);
		handlebars.registerHelper(RestrictionsHelper.NAME, RestrictionsHelper.INSTANCE);
		handlebars.registerHelper(StylesheetHelper.NAME, StylesheetHelper.INSTANCE);
		handlebars.registerHelper(JavascriptHelper.NAME, JavascriptHelper.INSTANCE);
		handlebars.registerHelper(EqualsHelper.NAME, EqualsHelper.INSTANCE);
		handlebars.registerHelper(ComponentsHelper.NAME, ComponentsHelper.INSTANCE);
		handlebars.registerHelper(ContainerHelper.NAME, ContainerHelper.INSTANCE);
		handlebars.registerHelper(DocumentReadyHelper.NAME, DocumentReadyHelper.INSTANCE);
	}

	private static class RestrictionsHelper implements Helper<Object> {

		public static final Helper<Object> INSTANCE = new RestrictionsHelper();

		public static final String NAME = "ifPermitted";

		@Override
		public CharSequence apply(Object context, Options options) throws IOException {

			Tuple<View, Class<?>> view = (Tuple<View, Class<?>>) context;
			
			if(checkPermissions(view.second)){
				return options.fn();
			}else{
				return options.inverse();
			}
		}

	}

	private static class StylesheetHelper implements Helper<Object> {

		public static final Helper<Object> INSTANCE = new StylesheetHelper();

		public static final String NAME = "stylesheets";

		@Override
		public CharSequence apply(Object context, Options options) throws IOException {
			Set<String> renderings = DefaultContextInitializer.getContext().componentRegistry()
					.renderComponentStylesheets();

			String renderedOutput = "";
			for (String rendering : renderings) {
				renderedOutput += rendering;
			}
			return renderedOutput;
		}

	}

	private static class JavascriptHelper implements Helper<Object> {

		public static final Helper<Object> INSTANCE = new JavascriptHelper();

		public static final String NAME = "javascript";

		@Override
		public CharSequence apply(Object context, Options options) throws IOException {
			Set<String> renderings = DefaultContextInitializer.getContext().componentRegistry()
					.renderComponentJavascript();

			String renderedOutput = "";
			for (String rendering : renderings) {
				renderedOutput += rendering;
			}
			return renderedOutput;
		}

	}

	private static class EqualsHelper implements Helper<Object> {

		public static final Helper<Object> INSTANCE = new EqualsHelper();

		public static final String NAME = "equals";

		@Override
		public CharSequence apply(final Object context, final Options options) throws IOException {

			if (context.equals(options.param(0))) {
				return options.fn();
			} else {
				return options.inverse();
			}
		}
	}

	public boolean getDevelopmentMode() {
		return DefaultContextInitializer.getContext().developmentMode() != null;
	}

	public boolean getEditLayout() {
		return DefaultContextInitializer.getContext().developmentMode().editLayout();
	}

	public String apply(View view, List<Tuple<View, Class<?>>> views, Context context, Object screenInstance,
			String templateFile) throws IOException {

		String templateContents = new String(Files.readAllBytes(Paths.get(templateFile)));

		Template template = handlebars.compileInline(templateContents);

		com.github.jknack.handlebars.Context ctx = com.github.jknack.handlebars.Context.newBuilder(this)
				.resolver(MapValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE, MethodValueResolver.INSTANCE)
				.combine("view", view).combine("views", views).combine("context", context)
				.combine("instance", screenInstance).build();

		String page = template.apply(ctx);

		return page;
	}

}
