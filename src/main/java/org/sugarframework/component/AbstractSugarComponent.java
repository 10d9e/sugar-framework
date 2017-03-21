package org.sugarframework.component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.util.EvalUtil;

import com.google.common.io.ByteStreams;

public abstract class AbstractSugarComponent<T extends Annotation, V extends Object, R extends AccessibleObject> {
	
	private Log log = LogFactory.getLog(getClass()); 

	private Object meObject;

	private Set<String> javaScripts = new HashSet<>();

	private Set<String> styleSheets = new HashSet<>();

	private boolean live = false;

	private int refreshSeconds = 5;
	
	protected void initialize() {
	}

	public void onPageLoad(Object page) {
	}

	public String doRender(T annotation, V data, R object) {
		String rendering = String.format("<div id='%s' class='sugar-component'>%s</div>", ((Member) object).getName(),
				render(annotation, data, object));
			
		return rendering;
	}

	protected abstract String render(T annotation, V data, R member);

	public void addJavascriptReference(final String file) {
		javaScripts.add(String.format("<script type=\"text/javascript\" language=\"javascript\" src=\"%s\"></script>",
				file));
	}

	public void addStyleSheetReference(final String file) {
		styleSheets.add(String.format("<link href=\"%s\" rel=\"stylesheet\">", file));
	}

	protected String onDocumentReady(T annotation, V data, Member field) {
		return "";
	}

	public String doDocumentReady(T annotation, V data, Member field) {
		return onDocumentReady(annotation, data, field);
	}

	protected <C> C[] ev(String[] input) {
		return EvalUtil.ev(input, meObject);
	}

	protected <C> C ev(String input) {
		return EvalUtil.ev(input, meObject);
	}

	public Object getParent() {
		return meObject;
	}

	public void setParent(Object parent) {
		this.meObject = parent;
	}

	public Set<String> getJavaScripts() {
		return javaScripts;
	}

	public void setJavaScripts(Set<String> javaScripts) {
		this.javaScripts = javaScripts;
	}

	public Set<String> getStyleSheets() {
		return styleSheets;
	}

	public void setStyleSheets(Set<String> styleSheets) {
		this.styleSheets = styleSheets;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	protected String readFileOnClasspath(String path) throws IOException{
		byte[] array = ByteStreams.toByteArray(AbstractSugarComponent.class.getResourceAsStream(path));

		return new String(array);
	}

	public String getLiveJavascript(Member object) {
		
		try {
			String template = readFileOnClasspath("live.template.js");
			
			String layout = "";
			if (DefaultContextInitializer.getContext().isDevelopmentMode() && DefaultContextInitializer.getContext().developmentMode().editLayout()) {
				layout += String.format("layoutSugarComponent($('#%s'));", object.getName()) + "\n";
			}
			
			String reload = String.format(template, object.getName(), object.getName(), layout);
			
			return reload;
		} catch (IOException e) {
			throw new ComponentException(e.getMessage(), e);
		}
	}

	public int getRefreshSeconds() {
		return refreshSeconds;
	}

	public void setRefreshSeconds(int refreshSeconds) {
		this.refreshSeconds = refreshSeconds;
	}
}
