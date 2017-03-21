package org.sugarframework.util;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withName;
import static org.reflections.ReflectionUtils.withPrefix;
import static org.sugarframework.util.ClassUtils.getTargetClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sugarframework.Label;
import org.sugarframework.Order;
import org.sugarframework.SugarException;
import org.sugarframework.Validator;
import org.sugarframework.View;
import org.sugarframework.context.DefaultContextInitializer;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class Reflector {
	
	private static Log log = LogFactory.getLog(Reflector.class);
	
	public static AccessibleObject find(String name, Class<?> clazz){
		
		for (Field field : clazz.getDeclaredFields()){
			if(field.getName().equals(name)){
				return field;
			}
		}
		
		for(Method method : clazz.getDeclaredMethods()){
			if(method.getName().equals(name)){
				return method;
			}
		}
		
		return null;
		
	}

	public static <T> Object copy(T src, T dest) throws Exception {

		for (Field sourceField : getTargetClass(src).getDeclaredFields()) {

			if (Modifier.isFinal(sourceField.getModifiers())) {
				continue;
			}

			sourceField.setAccessible(true);
			Object sourceValue = sourceField.get(src);

			Field destField;
			try {
				destField = dest.getClass().getDeclaredField(sourceField.getName());
			} catch (NoSuchFieldException e) {
				// no field in the destination, its ok, just continue;
				continue;
			}

			if (destField != null) {
				destField.setAccessible(true);
				destField.set(dest, sourceValue);
			}
		}

		return dest;

	}

	public static void setUrl(View screenAnnotation) throws Exception {

		String url = screenAnnotation.url();
		if (url.equals("")) {
			url = screenAnnotation.value().replace(" ", "-");
			Method m = screenAnnotation.getClass().getMethod("url");
			m.setAccessible(true);
		}

	}

	public static Object instance(Class<?> clazz) throws InstantiationException, IllegalAccessException {

		if (Modifier.isAbstract(clazz.getModifiers())) {
			// get the subclass
			Set<?> subTypes = DefaultContextInitializer.getContext().reflections().getSubTypesOf(clazz);

			if (subTypes.size() > 1) {
				System.err.println("Ambiguous error, unable to determine which Page class to instance " + subTypes);
			} else {
				return instance((Class<?>) subTypes.iterator().next());
			}
		}

		return clazz.newInstance();

	}

	public static <T extends AnnotatedElement> T[] order(T[] targets) {

		Arrays.sort(targets, comparator());

		return targets;
	}

	public static <T extends AnnotatedElement> Collection<T> order(Collection<T> targets) {

		List<T> sorted = new ArrayList<T>(targets);

		Collections.sort(sorted, comparator());

		return sorted;
	}

	@SuppressWarnings("rawtypes")
	private static Comparator COMPARATOR;

	@SuppressWarnings("unchecked")
	private static <T extends AnnotatedElement> Comparator<T> comparator() {
		if (COMPARATOR == null) {

			COMPARATOR = new Comparator<T>() {

				@Override
				public int compare(T first, T second) {

					// order first by Order Value

					Order firstAnno = first.getAnnotation(Order.class);

					Order secondAnno = second.getAnnotation(Order.class);

					if (firstAnno != null && secondAnno == null) {
						return -1;
					}
					if (firstAnno == null && secondAnno != null) {
						return 1;
					}

					if (firstAnno != null && secondAnno != null) {
						if (firstAnno.value() > secondAnno.value()) {
							return 1;
						} else if (firstAnno.value() < secondAnno.value()) {
							return -1;
						}
					}

					// if the order annotation is not present, order by class
					// name
					if (firstAnno == null && secondAnno == null) {

						return first.toString().compareTo(second.toString());
					}

					return 0;
				}
			};
		}
		return COMPARATOR;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> Set<T> getParameterAnnotations(Class<T> annotationType, Method method) {

		Set<T> rSet = new HashSet<T>();

		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] ann : annotations) {

			for (Annotation a : ann) {
				if (a.annotationType().equals(annotationType)) {
					rSet.add((T) a);
				}
			}
		}
		return rSet;

	}

	public static <T extends Annotation> Map<String, Set<Annotation>> getAllParameterAnnotations(Method method) {

		Map<String, Set<Annotation>> rMap = new LinkedHashMap<String, Set<Annotation>>();

		Class<?>[] pTypes = method.getParameterTypes();

		int i = 0;

		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] ann : annotations) {

			boolean added = false;
			Set<Annotation> annoSet = new HashSet<Annotation>();
			for (Annotation a : ann) {
				annoSet.add(a);
			}

			for (Annotation a : ann) {
				if (a.annotationType().equals(Label.class)) {
					rMap.put(((Label) a).value(), annoSet);
					added = true;
				}
			}

			if (!added) {
				rMap.put(String.format("Parameter %s [%s]", i, pTypes[i]), annoSet);
				// rMap.put("", annoSet);
			}
			i++;
		}
		return rMap;

	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T find(Class<T> annotationType, Collection<Annotation> annotations) {

		for (Annotation a : annotations) {
			if (a.annotationType().equals(annotationType)) {
				return (T) a;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Boolean executeValidateMethod(final String methodName, Object screen, Object value) {

		Set<Method> validateMethods = getAllMethods(getTargetClass(screen), withName(methodName));

		for (Method method : validateMethods) {

			try {

				Boolean isValid = (Boolean) method.invoke(screen, value);

				if (!isValid) {
					Validator sugarValidator = method.getAnnotation(Validator.class);
					if (sugarValidator != null) {
						log.error(sugarValidator.invalidMessage());
					}
				}
				return isValid;

			} catch (Exception e) {
				throw new SugarException(e.getMessage(), e);
			}

		}
		return false;
	}

	public static boolean isBean(Class<?> castType) {

		if (castType.equals(int.class) || castType.equals(Integer.class) || castType.equals(double.class)
				|| castType.equals(Double.class) || castType.equals(byte.class) || castType.equals(Byte.class)
				|| castType.equals(short.class) || castType.equals(Short.class) || castType.equals(long.class)
				|| castType.equals(Long.class) || castType.equals(double.class) || castType.equals(Double.class)
				|| castType.equals(float.class) || castType.equals(Float.class) || castType.equals(boolean.class)
				|| castType.equals(Boolean.class) || castType.equals(char.class) || castType.equals(Character.class)
				|| castType.equals(Date.class) || castType.equals(String.class)) {
			return false;
		}

		return true;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T> T hardCast(Object castTarget, Class<T> castType) {

		String stringValue = castTarget.toString();
		if (!castType.equals(String.class) && !castType.equals(Date.class) && "".equals(stringValue)) {
			stringValue = "0";
		}

		if (castType.equals(int.class) || castType.equals(Integer.class)) {
			return (T) Integer.valueOf(stringValue);
		} else if (castType.equals(double.class) || castType.equals(Double.class)) {
			return (T) Double.valueOf(stringValue);
		} else if (castType.equals(byte.class) || castType.equals(Byte.class)) {
			return (T) Byte.valueOf(stringValue);
		} else if (castType.equals(short.class) || castType.equals(Short.class)) {
			return (T) Short.valueOf(stringValue);
		} else if (castType.equals(long.class) || castType.equals(Long.class)) {
			return (T) Long.valueOf(stringValue);
		} else if (castType.equals(double.class) || castType.equals(Double.class)) {
			return (T) Double.valueOf(stringValue);
		} else if (castType.equals(float.class) || castType.equals(Float.class)) {
			return (T) Float.valueOf(stringValue);
		} else if (castType.equals(boolean.class) || castType.equals(Boolean.class)) {
			if (castTarget.equals("on")) {
				return (T) Boolean.TRUE;
			}
			return (T) Boolean.valueOf(stringValue);
		} else if (castType.equals(char.class) || castType.equals(Character.class)) {
			String rValue = new String();
			rValue += castTarget;
			return (T) rValue;
		} else if (castType.equals(Date.class)) {
			return (T) new Date(Date.parse(stringValue));
		}
		return castType.cast(castTarget);

	}

	public static Map<String, Class<?>> getParameters(Method method) {

		Map<String, Class<?>> rSet = new LinkedHashMap<String, Class<?>>();

		Class<?>[] pTypes = method.getParameterTypes();

		int i = 0;

		Annotation[][] annotations = method.getParameterAnnotations();
		for (Annotation[] ann : annotations) {

			boolean added = false;
			for (Annotation a : ann) {
				if (a.annotationType().equals(Label.class)) {

					Label l = (Label) a;
					rSet.put(l.value(), pTypes[i]);
					added = true;
				}
			}

			if (!added) {
				rSet.put(String.format("Parameter %s [%s]", i, pTypes[i]), pTypes[i]);
			}

			i++;
		}

		return rSet;

	}

	@SuppressWarnings("unchecked")
	public static Set<Method> getBeanMethods(final Class<?> type) {
		return getAllMethods(
				type,
				Predicates.and(withModifier(Modifier.PUBLIC), Predicates.not(withModifier(Modifier.STATIC)),
						Predicates.not(withPrefix("set")), Predicates.not(withPrefix("get")), new Predicate<Method>() {
							public boolean apply(Method method) {
								return method.getDeclaringClass().equals(type);
							}
						}));
	}

}
