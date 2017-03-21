package org.sugarframework.component.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.sugarframework.Choice;
import org.sugarframework.DefaultValue;
import org.sugarframework.Hidden;
import org.sugarframework.Label;
import org.sugarframework.SugarComponent;
import org.sugarframework.SugarException;
import org.sugarframework.TextArea;
import org.sugarframework.Validate;
import org.sugarframework.component.AbstractSugarComponent;
import org.sugarframework.security.Password;
import org.sugarframework.util.BeanUtil;
import org.sugarframework.util.EvalUtil;
import org.sugarframework.util.Reflector;

@SugarComponent(Action.class)
public class ActionComponent extends AbstractSugarComponent<Action, Void, Method> {

	@Override
	public String render(Action anno, Void data, Method method) {

		String rendering = "";

		String actionName;
		if (!anno.value().equals("")) {
			actionName = anno.value();
		} else {
			actionName = method.getName();
		}

		rendering += "<form class=\"form-horizontal\" role=\"form\" action=\"execute\" method=\"post\">";
		rendering += "<fieldset>";

		Class<?>[] pTypes = method.getParameterTypes();
		int pCount = 0;

		Map<String, Set<Annotation>> parameterAnnos = Reflector.getAllParameterAnnotations(method);
		String buttonId = "b-" + new Random().nextInt();

		for (Map.Entry<String, Set<Annotation>> e : parameterAnnos.entrySet()) {

			String parameterName = EvalUtil.ev(e.getKey());

			TextArea textArea = Reflector.find(TextArea.class, e.getValue());
			boolean isTextArea = false;
			if (textArea != null) {
				isTextArea = true;
			}

			DefaultValue defaultValue = Reflector.find(DefaultValue.class, e.getValue());
			Object dValue = new String("");
			if (defaultValue != null) {
				dValue = ev(defaultValue.value());
			}
			Validate validator = Reflector.find(Validate.class, e.getValue());
			String id = "cg-" + new Random().nextInt();

			Choice choice = Reflector.find(Choice.class, e.getValue());
			if (choice != null) {
				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						parameterName, parameterName);
				rendering += "<div class=\"col-sm-6\">";
				rendering += String.format("<select class=\"form-control\" name=\"%s\" id=\"input%s\">", parameterName,
						parameterName);
				for (String aChoice : choice.value()) {
					rendering += String.format("<option>%s</option>", aChoice);
				}
				rendering += "</select>";
				rendering += "</div>";
				rendering += "</div>";

			} else if (pTypes[pCount].equals(Boolean.class) || pTypes[pCount].equals(boolean.class)) {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += "<div class=\"col-sm-offset-4 col-sm-6\">";
				rendering += String.format(
						"<label class=\"checkbox\"> <input type=\"checkbox\" name=\"%s\" id=\"input%s\"> %s",
						parameterName, parameterName, parameterName);
				rendering += "</label>";
				rendering += "</div>";
				rendering += "</div>";

			} else if (pTypes[pCount].equals(java.util.Date.class)) {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						parameterName, parameterName);
				rendering += "<div class=\"groupy col-sm-4 left-inner-addon\">";
				rendering += "<i class=\"glyphicon glyphicon-calendar\"></i> ";
				rendering += String
						.format("<input type=\"text\" class=\"form-control datepicker\" name=\"%s\"	id=\"input%s\" value=\"\" data-date-format=\"mm/dd/yyyy\">",
								parameterName, parameterName);
				rendering += "</div>";
				rendering += "</div>";

			} else if (Reflector.isBean(pTypes[pCount])) {

				// TODO

				// <%@include file="bean.jsp"%>
				try {
					rendering += renderBean(pTypes[pCount], parameterName, id, dValue, buttonId, validator);
				} catch (IntrospectionException x) {
					throw new SugarException(x.getMessage(), x);
				}

			} else if (isTextArea) {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						parameterName, parameterName);
				rendering += "<div class=\"groupy col-sm-6\">";
				rendering += String.format(
						"	<textarea class=\"form-control input-md input-lg-when-xs\" rows=\"8\" name=\"%s\"",
						parameterName);
				rendering += String.format("	    id=\"input%s\" placeholder=\"%s\" value=\"%s\"", parameterName,
						parameterName, dValue);
				if (validator != null) {
					rendering += String.format("onblur=\"validateParameter('%s','%s', $(this), '%s','%s','%s' )\"",
							validator.value(), parameterName, id, dValue, buttonId);
				}
				rendering += ">";
				rendering += dValue;
				rendering += "</textarea>";
				rendering += "</div>";
				rendering += "</div>";

			} else {
				
				Password password = Reflector.find(Password.class, e.getValue());


				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						parameterName, parameterName);
				rendering += "<div class=\"groupy col-sm-6\">";
				if(password != null){
					rendering += String.format("<input type=\"password\" class=\"form-control\" name=\"%s\"", parameterName);
				}else{
					rendering += String.format("<input type=\"text\" class=\"form-control\" name=\"%s\"", parameterName);
				}
				rendering += String.format("	    id=\"input%s\" placeholder=\"%s\" value=\"%s\" ", parameterName,
						parameterName, dValue);
				if (validator != null) {
					rendering += String.format("onblur=\"validateParameter('%s','%s', $(this), '%s','%s','%s' )\"",
							validator.value(), parameterName, id, dValue, buttonId);
				}
				rendering += ">";
				rendering += "</div>";
				rendering += "</div>";
			}

			pCount++;

		}

		rendering += "<div class=\"form-group\">";
		rendering += "<div class=\"col-sm-offset-4 col-sm-6\">";
		rendering += String.format("<button id=\"%s\" type=\"submit\" class=\"btn btn-default\">%s", buttonId,
				actionName);
		rendering += "&raquo; </button>";
		rendering += "</div>";
		rendering += "</div>";

		rendering += String.format("<input type=\"hidden\" name=\"methodName\" value=\"%s\">", method.getName());

		rendering += "</fieldset>";
		rendering += "</form>";

		return rendering;
	}

	private String renderBean(Class<?> beanClass, String parameterName, String id, Object dValue, String buttonId,
			Validate validator) throws IntrospectionException {
		String rendering = "";

		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

		String beanName = beanInfo.getBeanDescriptor().getDisplayName();

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		rendering += "<style>";
		rendering += ".bs-example:after {";
		rendering += String.format("content: \"%s\";", parameterName);
		rendering += "}</style>";

		rendering += "<div class=\"bs-example\">";

		for (PropertyDescriptor pd : propertyDescriptors) {

			String displayName = pd.getDisplayName();
			String labelValue = displayName;
			
			String inputId = parameterName + "." + displayName;

			Field field = BeanUtil.find(beanClass, pd);
			if (field != null) {

				if (field.isAnnotationPresent(Hidden.class)) {
					continue;
				}

				Label label = field.getAnnotation(Label.class);
				if (label != null) {
					labelValue = EvalUtil.ev(label.value());
				}
			}
			Class<?> propertyType = pd.getPropertyType();

			// remove access to the class property
			if (propertyType.equals(Class.class)) {
				continue;
			}

			if (propertyType.equals(Boolean.class) || propertyType.equals(boolean.class)) {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += "<div class=\"col-sm-offset-4 col-sm-6\">";
				rendering += "<label class=\"checkbox\"> <input type=\"checkbox\"";
				rendering += String.format("name=\"%s\" id=\"input%s\">", inputId, displayName);
				rendering += String.format("%s </label>", labelValue);
				rendering += "</div>";
				rendering += "</div>";

			} else if (propertyType.equals(java.util.Date.class)) {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						displayName, labelValue);
				rendering += "<div class=\"groupy col-sm-4 left-inner-addon\">";
				rendering += "<i class=\"glyphicon glyphicon-calendar\"></i>";
				rendering += String.format(
						"<input type=\"text\" class=\"form-control datepicker\" name=\"%s\" id=\"input%s\" value=\"\"",
						inputId, displayName);
				rendering += "data-date-format=\"mm/dd/yyyy\">";
				rendering += "</div>";
				rendering += "</div>";
			} else if (org.sugarframework.util.Reflector.isBean(propertyType)) {

				// TODO, this has to be reconsidered, as 
				
			} else {

				rendering += String.format("<div class=\"form-group\" id=\"%s\">", id);
				rendering += String.format("<label class=\"col-sm-4 control-label\" for=\"input%s\">%s</label>",
						displayName, labelValue);
				rendering += "<div class=\"col-sm-6\">";

				rendering += String.format("<input type=\"text\" class=\"form-control\" name=\"%s\"", inputId);
				rendering += String.format("	    id=\"input%s\" placeholder=\"%s\" value=\"%s\" ", displayName,
						labelValue, dValue);
				if (validator != null) {
					rendering += String.format("onblur=\"validateParameter('%s','%s', $(this), '%s','%s','%s' )\"",
							validator.value(), displayName, id, dValue, buttonId);
				}
				rendering += ">";
				rendering += "</div>";
				rendering += "</div>";

			}
		}

		rendering += "</div>";

		return rendering;
	}

}
