<%@page import="java.beans.PropertyDescriptor"%>
<%@page import="java.beans.BeanInfo"%>
<%@page import="java.beans.Introspector"%>
<%@page import="org.sugarframework.util.Reflector"%>
<%@page import="java.lang.reflect.Field"%>
<%@ page import="org.sugarframework.Label"%>
<%@ page import="org.sugarframework.util.EvalUtil"%>

<%
	Class<?> beanClass = pTypes[pCount];
	
	BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

	String beanName = beanInfo.getBeanDescriptor().getDisplayName();

	PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
%>

<style>

.bs-example:after {
  content: "<%=parameterName%>";
}

</style>

<div class="bs-example" >

	<%
		for (PropertyDescriptor pd : propertyDescriptors) {
				
			//String displayName = pd.getDisplayName();
			//Class<?> propertyType = pd.getPropertyType();
			
			 String displayName = pd.getDisplayName();
			 String labelValue = displayName;
			    
			 Field field = org.sugarframework.util.BeanUtil.find(beanClass, pd);
		     if (field != null){
		      	Label label = field.getAnnotation(Label.class);
		       	if (label != null){
		       		labelValue = EvalUtil.ev(label.value());
		       	}
		     }
		     Class<?> propertyType = pd.getPropertyType();
			
			
			// remove access to the class property
			if ( propertyType.equals(Class.class)){
		continue;
			}
			
			if (propertyType.equals(Boolean.class) || propertyType.equals(boolean.class) ) {
	%>
			
									<div class="form-group" id="<%=id%>">
										<div class="col-sm-offset-4 col-sm-6">
											<label class="checkbox"> <input type="checkbox"
												name="<%=displayName%>" id="input<%=displayName%>">
												<%=labelValue%>
											</label>
										</div>
									</div>
				
				<%} else if ( propertyType.equals(java.util.Date.class) ) { %>
				
									<div class="form-group" id="<%=id%>">
										<label class="col-sm-4 control-label" for="input<%=displayName%>"><%=labelValue%></label>
										<div class="groupy col-sm-4 left-inner-addon">
											<i class="glyphicon glyphicon-calendar"></i>
											<input type="text" class="form-control datepicker" name="<%=displayName%>"
												id="input<%=displayName%>" value=""
												data-date-format="mm/dd/yyyy">
										</div>
									</div>
				<%} else if ( org.sugarframework.util.Reflector.isBean(propertyType) ) { %>
			
				
			
				<%} else { %>
			
								<div class="form-group" id="<%=id%>">
										<label class="col-sm-4 control-label" for="input<%=displayName%>"><%=labelValue%></label>
										<div class="col-sm-6">
											<input type="text" class="form-control" name="<%=displayName%>"
												id="input<%=displayName%>" placeholder="<%=labelValue%>"
												value="<%=dValue%>" <%if(validator != null){ %>
												onblur="validateParameter('<%=validator.value()%>','<%=displayName%>', $(this), '<%=id%>','<%=dValue%>', '<%=buttonId%>' )"
												<%} %>>
										</div>
								 </div>

	<%
				}
		}
	%>

</div>