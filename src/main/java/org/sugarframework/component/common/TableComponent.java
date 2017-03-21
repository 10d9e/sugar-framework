package org.sugarframework.component.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Collection;

import org.sugarframework.Hidden;
import org.sugarframework.Label;
import org.sugarframework.SugarComponent;
import org.sugarframework.component.AbstractSugarComponent;

@SugarComponent(Table.class)
public class TableComponent extends AbstractSugarComponent<Table, Collection<?>, AccessibleObject> {

	@Override
	public void initialize() {
		addJavascriptReference("assets-boot/js/jquery.dataTables.js");
		// addJavascriptReference("//cdn.datatables.net/plug-ins/e9421181788/integration/bootstrap/3/dataTables.bootstrap.js");

		super.initialize();
	}

	@Override
	public String render(Table anno, Collection<?> data, AccessibleObject member) {

		String out = "";

		try {
			if (data.size() < 1) {
				return "";
			}

			// table header first
			Object firstObject = data.toArray()[0];
			if (firstObject != null) {
				Class<?> beanClass = firstObject.getClass();
				BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				out += "<table class=\"table table-hover display table-responsive\"> <thead> <tr>";

				for (PropertyDescriptor pd : propertyDescriptors) {
					Class<?> propertyType = pd.getPropertyType();
					// remove access to the class property
					if (propertyType.equals(Class.class)) {
						continue;
					}

					String displayName = pd.getDisplayName();

					Field f = org.sugarframework.util.BeanUtil.find(beanClass, pd);
					if (f != null) {
						
						if (f.isAnnotationPresent(Hidden.class)) {
							continue;
						}
						
						Label label = f.getAnnotation(Label.class);
						if (label != null) {
							displayName = ev(label.value());
						}
					}

					out += String.format("<th>%s</th>", displayName);

				}

				out += "</tr> </thead>";
			}

			out += "<tbody>";

			for (Object item : data) {

				Class<?> beanClass = firstObject.getClass();
				BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				out += "<tr>";
				for (PropertyDescriptor pd : propertyDescriptors) {
					Class<?> propertyType = pd.getPropertyType();
					// remove access to the class property
					if (propertyType.equals(Class.class)) {
						continue;
					}

					Field f = org.sugarframework.util.BeanUtil.find(beanClass, pd);
					f.setAccessible(true);
					
					if (f.isAnnotationPresent(Hidden.class)) {
						continue;
					}
					
					Object value = f.get(item);
					if (value == null) {
						value = new String();
					}

					out += String.format("<td>%s</td>", value);

				}

				out += "</tr>";
			}

			out += "</tbody></table>";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return out;
	}

	@Override
	public String onDocumentReady(Table anno, Collection<?> data, Member field) {

		String js =
		String.format("$('#%s > table').dataTable( {", field.getName()) + "\n"
				+ String.format("  paging: %s," + "\n", anno.paging())
				+ String.format("  paginate: %s," + "\n", anno.paginate())
				+ String.format("  searching: %s," + "\n", anno.searching()) + "\n"
				+ String.format("  ordering: %s," + "\n", anno.ordering()) + "\n" + "} );" + "\n"
				+ "$( this ).removeClass( 'dataTable' ); ";

		return js;
	}

}
