package org.sugarframework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.sugarframework.SugarException;

public class FormUtil {
	
	
	static {
		DateConverter dateConverter = new DateConverter(null);
		dateConverter.setPattern("MM/dd/yyyy");
		ConvertUtils.register(dateConverter, java.util.Date.class);
	}

	public static <T> T populate(Class<T> clazz, Map<String, ?> parameterMap) {
		T object = null;
		try {

			object = (T) clazz.newInstance();
			BeanUtils.populate(object, parameterMap);

		} catch (InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			throw new SugarException(e.getMessage(), e);
		}
		return object;
	}
}