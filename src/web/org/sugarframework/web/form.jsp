<%@page import="org.sugarframework.DefaultValue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.sugarframework.util.EvalUtil"%>	
<%@ page import="org.sugarframework.View"%>
<%@ page import="org.sugarframework.TextArea"%>
<%@ page import="org.sugarframework.Action"%>
<%@ page import="org.sugarframework.Label"%>
<%@ page import="org.sugarframework.Context"%>
<%@ page import="org.sugarframework.DefaultValue"%>
<%@ page import="org.sugarframework.Validate"%>
<%@ page import="org.sugarframework.Choice"%>
<%@ page import="java.lang.reflect.Method"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.lang.annotation.Annotation"%>


<form class="form-horizontal" role="form" action="execute" method="post">
	<fieldset>
		<%
			Class<?>[] pTypes = m.getParameterTypes();
			int pCount = 0;

			Map<String, Set<Annotation>> parameterAnnos = org.sugarframework.util.Reflector
					.getAllParameterAnnotations(m);
			String buttonId = "b-" + bCounter++;
			for (Map.Entry<String, Set<Annotation>> e : parameterAnnos
					.entrySet()) {

				String parameterName = EvalUtil.ev( e.getKey() );
				
				TextArea textArea = org.sugarframework.util.Reflector
					.find(TextArea.class, e.getValue());
				boolean isTextArea = false;
				if (textArea != null){
				    isTextArea = true;
				}
				
				DefaultValue defaultValue = org.sugarframework.util.Reflector
						.find(DefaultValue.class, e.getValue());
				Object dValue = new String("");
				if (defaultValue != null) {
					dValue = EvalUtil.ev( defaultValue.value(), screenInstance );
				}
				Validate validator = org.sugarframework.util.Reflector.find(
						Validate.class, e.getValue());
				String id = "cg-" + pCounter++;

				Choice choice = org.sugarframework.util.Reflector.find(
						Choice.class, e.getValue());
				if (choice != null) {
		%>

		<div class="form-group" id="<%=id%>">
			<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
			<div class="col-sm-6">
				<select class="form-control" name="<%=parameterName%>" id="input<%=parameterName%>">
					<%
						for (String aChoice : choice.value()) {
					%>
					<option><%=aChoice%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		<%
			} else if (pTypes[pCount].equals(Boolean.class)
						|| pTypes[pCount].equals(boolean.class)) {
		%>
		<div class="form-group" id="<%=id%>">
			<div class="col-sm-offset-4 col-sm-6">
				<label class="checkbox"> <input type="checkbox"
					name="<%=parameterName%>" id="input<%=parameterName%>"> <%=parameterName%>
				</label>
			</div>
		</div>
		<%
			} else if (pTypes[pCount].equals(java.util.Date.class)) {
		%>

		<div class="form-group" id="<%=id%>">
			<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
			<div class="groupy col-sm-4 left-inner-addon">
				<i class="glyphicon glyphicon-calendar"></i> <input type="text"
					class="form-control datepicker" name="<%=parameterName%>"
					id="input<%=parameterName%>" value="" data-date-format="mm/dd/yyyy">
			</div>
		</div>

		<%
			} else if (org.sugarframework.util.Reflector
						.isBean(pTypes[pCount])) {
		%>

			<%@include file="bean.jsp"%>

		<%
			} else {
				if (isTextArea){%>
				
				<div class="form-group" id="<%=id%>">
					<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
					<div class="groupy col-sm-6">
						<textarea class="form-control input-md input-lg-when-xs" rows="8"
								name="<%=parameterName%>"
								id="input<%=parameterName%>" placeholder="<%=parameterName%>"
								value="<%=dValue%>" <%if (validator != null) {%>
								onblur="validateParameter('<%=validator.value()%>','<%=parameterName%>', $(this), '<%=id%>','<%=dValue%>', '<%=buttonId%>' )"
								<%}%>><%=dValue%>
						</textarea>
					</div>
				</div>
				
	
			  <%}else{%>
			  
				<div class="form-group" id="<%=id%>">
					<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
					<div class="groupy col-sm-6">
						<input type="text" class="form-control" name="<%=parameterName%>"
							id="input<%=parameterName%>" placeholder="<%=parameterName%>"
							value="<%=dValue%>" <%if (validator != null) {%>
							onblur="validateParameter('<%=validator.value()%>','<%=parameterName%>', $(this), '<%=id%>','<%=dValue%>', '<%=buttonId%>' )"
							<%}%>>
					</div>
				</div>
	
			<%  }
			}

				pCount++;

			}
		%>


		<div class="form-group">
			<div class="col-sm-offset-4 col-sm-6">
				<button id="<%=buttonId%>" type="submit" class="btn btn-default"><%=actionName%>
					&raquo;
				</button>
			</div>
		</div>

		<input type="hidden" name="methodName" value="<%=m.getName()%>">

	</fieldset>
</form>