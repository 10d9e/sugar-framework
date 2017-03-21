package org.sugarframework.data.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TypeCheck {

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isBean(Class<?> clazz){
        return !isWrapperType(clazz);
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(Date.class);
        ret.add(Map.class);
        ret.add(Collection.class);
        return ret;
    }

}
