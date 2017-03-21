package org.sugarframework.util;

import static org.sugarframework.context.DefaultContextInitializer.getContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.sugarframework.component.ComponentException;

public class EvalUtil {

	public static <C> C[] ev(String[] input) {
		return ev(input, null);
	}

	public static <C> C ev(String input) {
		return ev(input, null);
	}

	@SuppressWarnings("unchecked")
	public static <C> C[] ev(String[] input, Object meObject) {
		List<C> r = new ArrayList<C>();

		for (String s : input) {
			r.add((C) ev(s, meObject));
		}

		return (C[]) r.toArray(new String[r.size()]);
	}

	@SuppressWarnings("unchecked")
	public static <C> C ev(String input, Object meObject) {

		try {
			if (input.startsWith("${")) {

				String formattedInput = input.replace("${", "");
				formattedInput = removeLastChar(formattedInput);

				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("groovy");

				Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

				if (meObject != null) {
					bindings.put("me", meObject);
				}
				String script = String.format("%s %s", "return ", formattedInput);
				C rVal = (C) engine.eval(script, bindings);

				return rVal;

			} else if (input.startsWith("#{")) {

				Matcher m = Pattern.compile("\\#\\{(\\S+)\\}").matcher(input);

				while (m.find()) {
					String rep = getContext().getBundle().getString(m.group(1));
					return ev(rep, meObject);
				}

			}

			return (C) input;

		} catch (ScriptException e) {
			throw new ComponentException(e);
		}
	}

	private static String removeLastChar(String str) {
		return str.substring(0, str.length() - 1);
	}

}
