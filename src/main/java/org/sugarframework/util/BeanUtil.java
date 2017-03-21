package org.sugarframework.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.sugarframework.SugarException;

public final class BeanUtil {

	public static Field find(Class<?> clazz, PropertyDescriptor pd) {

		if (pd.getPropertyType().equals(Class.class)) {
			return null;
		}

		Field field = null;
		try {
			field = clazz.getDeclaredField(pd.getName());
		} catch (NoSuchFieldException e) {
			throw new SugarException(e.getMessage(), e);
		}
		return field;

	}

}
