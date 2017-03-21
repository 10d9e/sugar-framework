package org.sugarframework.component;

import static org.sugarframework.util.ClassUtils.getTargetClass;
import static org.sugarframework.util.HtmlUtil.tidy;
import static org.sugarframework.util.SecurityUtil.checkPermissions;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.sugarframework.BindReturnData;
import org.sugarframework.Live;
import org.sugarframework.SugarComponent;
import org.sugarframework.SugarException;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.util.Reflector;

public final class ComponentRegistry {
	
	private Log log = LogFactory.getLog(getClass());

	public interface FilterValid {
		boolean filter(AccessibleObject field, AbstractSugarComponent<Annotation, Object, AccessibleObject> component);
	}

	public interface ComponentRenderAction {
		String render(Object page, AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation annotation, AccessibleObject field);
	}

	private Map<Class<? extends Annotation>, AbstractSugarComponent<?, Object, AccessibleObject>> componentMap = new HashMap<Class<? extends Annotation>, AbstractSugarComponent<?, Object, AccessibleObject>>();

	@SuppressWarnings("unchecked")
	public void initialize(Reflections reflections) throws Exception {

		Set<Class<?>> components = reflections.getTypesAnnotatedWith(
				SugarComponent.class);

		log.info("Scanning for components ...");
		for (Class<?> c : components) {

			Object newInstance = c.newInstance();

			AbstractSugarComponent<Annotation, Object, AccessibleObject> component = (AbstractSugarComponent<Annotation, Object, AccessibleObject>) newInstance;

			component.initialize();

			SugarComponent sc = newInstance.getClass().getAnnotation(SugarComponent.class);

			componentMap.put(sc.value(), (AbstractSugarComponent<?, Object, AccessibleObject>) newInstance);

			log.info(String.format("%s [ %s ]", newInstance, sc.value()));

		}
	}

	public Set<String> onPageLoad(Object page) {

		Set<String> rSet = new HashSet<>();

		for (Map.Entry<Class<? extends Annotation>, AbstractSugarComponent<?, Object, AccessibleObject>> e : componentMap.entrySet()) {

			AbstractSugarComponent component = e.getValue();
			component.onPageLoad(page);
		}
		return rSet;
	}

	public Set<String> renderComponentJavascript() {

		Set<String> rSet = new HashSet<>();

		for (Map.Entry<Class<? extends Annotation>, AbstractSugarComponent<?, Object, AccessibleObject>> e : componentMap.entrySet()) {

			AbstractSugarComponent component = e.getValue();
			rSet.addAll(component.getJavaScripts());
		}
		return rSet;
	}

	public Set<String> renderComponentStylesheets() {
		Set<String> rSet = new HashSet<>();

		for (Map.Entry<Class<? extends Annotation>, AbstractSugarComponent<?, Object, AccessibleObject>> e : componentMap.entrySet()) {

			AbstractSugarComponent component = e.getValue();
			rSet.addAll(component.getStyleSheets());
		}
		return rSet;
	}

	public Set<AbstractSugarComponent<?, Object, AccessibleObject>> find(Object page) {

		Set<AbstractSugarComponent<?, Object, AccessibleObject>> found = new HashSet<AbstractSugarComponent<?, Object, AccessibleObject>>();

		for (Annotation anno : getTargetClass(page).getAnnotations()) {

			if (componentMap.containsKey(anno)) {
				found.add(componentMap.get(anno));
			}
		}

		return found;
	}

	public String renderComponent(final AccessibleObject field, final Object p) throws Exception {

		List<String> renderings = processComponents(p, new FilterValid() {

			@Override
			public boolean filter(AccessibleObject f, AbstractSugarComponent<Annotation, Object, AccessibleObject> component) {
				boolean sameField = f.equals(field);
				return sameField;
			}
		}, new ComponentRenderAction() {

			@Override
			public String render(Object page, AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation annotation,
					AccessibleObject field) {
				return DefaultContextInitializer.getContext().componentRegistry().renderComponent(component, annotation,
						field, page);
			}
		});

		return renderings.get(0);

	}

	public String renderComponent(AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation annotation,
			AccessibleObject accessibleObject, Object page) {

		Object value = null;
		try {
			if(accessibleObject instanceof Field){
				
				value = ((Field)accessibleObject).get(page);
				
			}else if(accessibleObject instanceof Method){
				
				Method method = (Method) accessibleObject;
				
				if(method.isAnnotationPresent(BindReturnData.class)){
					
					value = method.invoke(page);
				}

			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			
			throw new SugarException(e.getMessage(), e);
		}

		Live live = accessibleObject.getAnnotation(Live.class);
		
		if (live != null) {
			
			component.setRefreshSeconds(live.refresh());
		}

		String rendering = "";
		if (checkPermissions(accessibleObject)) {
			rendering = component.doRender(annotation, value, accessibleObject);
		}
		
		return tidy(rendering);

	}

	public String renderComponent(final Object p, final AbstractSugarComponent<Annotation, Object, AccessibleObject> component)
			throws Exception {
		List<String> renderings = processComponents(p, new FilterValid() {

			@Override
			public boolean filter(AccessibleObject field, AbstractSugarComponent<Annotation, Object, AccessibleObject> c) {

				if (c == component) {
					return true;
				}
				return false;
			}
		}, new ComponentRenderAction() {

			@Override
			public String render(Object page, AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation annotation,
					AccessibleObject field) {
				return renderComponent(component, annotation, field, page);
			}

		});

		return renderings.get(0);
	}

	public List<String> processComponents(Object p, FilterValid filter) throws Exception {

		return processComponents(p, filter, new ComponentRenderAction() {

			@Override
			public String render(Object page, AbstractSugarComponent<Annotation, Object, AccessibleObject> component, Annotation annotation,
					AccessibleObject field) {
				return null;
			}

		});
	}

	public List<String> processComponents(Object page, ComponentRenderAction action) throws Exception {
		return processComponents(page, new FilterValid() {

			@Override
			public boolean filter(AccessibleObject field, AbstractSugarComponent<Annotation, Object, AccessibleObject> component) {
				return true;
			}
		}, action);
	}
	
	private Set<AccessibleObject> getFieldsAndMethods(Class<?> clazz){

		Set<AccessibleObject> fieldsAndMethods = new HashSet<>();
		
		for (AccessibleObject m : clazz.getDeclaredFields()){
			fieldsAndMethods.add(m);
		}
		for (AccessibleObject m : clazz.getDeclaredMethods()){
			fieldsAndMethods.add(m);
		}
		
		return fieldsAndMethods;
	}
	
	public List<String> processComponents(Object page, FilterValid filter, ComponentRenderAction action)
			throws Exception {
		
		List<String> renderings = new ArrayList<String>();
		
		for (AccessibleObject accessibleObject : Reflector.order( getFieldsAndMethods(getTargetClass(page)) )) {
			accessibleObject.setAccessible(true);

			for (Annotation anno : accessibleObject.getAnnotations()) {

				Class<? extends Annotation> annoClass = anno.annotationType();

				if (componentMap.containsKey(annoClass)) {
					@SuppressWarnings("unchecked")
					AbstractSugarComponent<Annotation, Object, AccessibleObject> component = (AbstractSugarComponent<Annotation, Object, AccessibleObject>) componentMap
							.get(annoClass);

					if (filter.filter(accessibleObject, component)) {

						component.setParent(page);

						String rendering = action.render(page, component, anno, accessibleObject);

						renderings.add(rendering);

					}
				}
			}
		}

		return renderings;
	}

}
