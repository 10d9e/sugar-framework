package org.sugarframework.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class BindingProcessor {

    @SuppressWarnings("hiding")
    static final class Tuple<List, String> {
        public List values;
        public String sql;

        public Tuple(List values, String sql) {
            super();
            this.values = values;
            this.sql = sql;
        }

    }

    public static Tuple<List<?>, String> format(String formatString, Map<String, Object> bindings) throws Exception {

        Pattern pattern = Pattern.compile("\\{(.+?)\\}");

        Matcher matcher = pattern.matcher(formatString);

        StringBuffer buffer = new StringBuffer();

        List<Object> values = new ArrayList<>();

        matcher.reset();

        while (matcher.find()) {

            String token = matcher.group(1);

            Object bean = resolve(token, bindings);

            matcher.appendReplacement(buffer, "?");

            values.add(bean);
        }
        matcher.appendTail(buffer);

        return new Tuple<List<?>, String>(values, buffer.toString());
    }

    private static Object resolve(String token, Map<String, Object> bindings) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        if (token.contains(".")) {
            String[] statement = token.split("\\.");
            Object bean = bindings.get(statement[0]);
            return getProperty(bean, statement[1]);
        }
        else {

            return bindings.get(token);
        }

    }

    private static Object getProperty(Object bean, String token) throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(token);
        field.setAccessible(true);

        return field.get(bean);
    }


}
