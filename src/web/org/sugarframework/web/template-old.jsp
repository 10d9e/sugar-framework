<%@page import="org.sugarframework.context.DefaultContextInitializer"%>
<%@page import="org.sugarframework.component.ComponentRegistry"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/sugarTags.tld" prefix="s"%>
<%@ page session="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="org.sugarframework.context.DefaultContextInitializer"%>
<%@page import="org.sugarframework.util.MapUtils"%>
<%@page import="org.sugarframework.DefaultValue"%>
<%@ page import="org.sugarframework.View"%>
<%@ page import="org.sugarframework.Badge"%>
<%@ page import="org.sugarframework.Action"%>
<%@ page import="org.sugarframework.Label"%>
<%@ page import="org.sugarframework.Context"%>
<%@ page import="org.sugarframework.DefaultValue"%>
<%@ page import="org.sugarframework.Validate"%>
<%@ page import="org.sugarframework.Choice"%>
<%@ page import="java.lang.reflect.Method"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.lang.annotation.Annotation"%>

<%
	View screen = (View) session.getAttribute("screenAnnotation");
pageContext.setAttribute("screen", screen);

Map<String, Set<Method>> methods  = (Map<String, Set<Method>>) session.getAttribute("methods");

Set<View> screens = (Set<View>) session.getAttribute("screens");
//pageContext.setAttribute("screens", screens);

Context context = (Context) session.getAttribute("context");
Object screenInstance = session.getAttribute("screenInstance");
pageContext.setAttribute("screenInstance", screenInstance);

DefaultContextInitializer.componentRegistry().onPageLoad();

int bCounter = 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="<%=context.logo() %>">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title><%=screen.title()%></title>

<!-- Bootstrap core CSS -->
<link href="assets-boot/css/style/<%=context.style()%>" rel="stylesheet">
<link href="assets-boot/css/navbar-static-top.css" rel="stylesheet">
<link href="assets-boot/css/datepicker.css" rel="stylesheet">

<link href="assets-boot/css/docs.css" rel="stylesheet">
<link href="assets-boot/css/justified-nav.css" rel="stylesheet">

<!-- Component CSS references -->
<s:inject-stylesheets page="${screenInstance}" />

<!--  
<link href="assets-boot/css/docs.css" rel="stylesheet">
<link href="assets-boot/css/jumbotron-narrow.css" rel="stylesheet">
-->

<style>

.container{
	max-width: 900px;
}

.left-inner-addon {
	position: relative;
}

.left-inner-addon input {
	padding-left: 30px;
}

.left-inner-addon i {
	position: absolute;
	padding: 10px 12px;
}

.handle {
position: relative;
}
  
</style>

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../docs-assets-boot/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<div class="navbar navbar-default navbar-static-top" role="navigation">

		<div id="nav-container" class="container">

		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>

			<a class="navbar-brand" href="#"> <img style="max-height: 20px"
				src="<%=context.logo() %>"> <%=context.value()%>
			</a>

		</div>

		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<c:forEach var="scr" items='${screens}'>
					<c:choose>
						<c:when test='${scr == screen}'>
							<li class="active">
						</c:when>
						<c:otherwise>
							<li>
						</c:otherwise>
					</c:choose>

					<a href="<s:annotationValue annotation="${scr}" field="url"/>">
						<span
						class="<s:annotationValue annotation="${scr}" field="icon"/>"></span>
						<s:annotationValue annotation="${scr}" field="value" /> <c:if
							test="${scr == screen}">
							<s:badge page="${screenInstance}" />
						</c:if>
					</a>
					</li>
				</c:forEach>

			</ul>
		</div>
		
		</div>

	</div>

	<div id="main-container" class="container">
	
		<div id="alert-container"></div>

			<%  String firstActiveTab = "";
				int methodCount = MapUtils.size(methods);
				
				if(methodCount > 0){
		%><ul id="tabs" class="nav nav-tabs" data-tabs="tabs"><%		
					
				}
				
				for (Map.Entry<String, Set<Method>> entry :  methods.entrySet()){
						
					boolean first = true;
					
									for (Method m : entry.getValue()  ){ 
										Action action  = m.getAnnotation(Action.class);
										String actionName;
										if (!action.value().equals("")){
											actionName = action.value();
										}else{
											actionName = m.getName();
										}
											
										if(methodCount == 1){
										    
										    
											// only one method, lets render differently
											int pCounter = 0;
											if (!action.description().equals("")){ %>

			<legend><%=actionName%></legend>
			<div class="bs-component">
				<div class="list-group">
					<a href="#" class="list-group-item">
						<h4 class="list-group-item-heading"><%=action.descriptionTitle()%></h4>
						<p class="list-group-item-text"><%=action.description()%></p>
					</a>
				</div>
			</div>

			<%}%>
			<br>
			<s:top-components page="${screenInstance}" />
			<%@include file="form.jsp"%>
			<%
												}else{
																			
																			if (first == true){
																				first = false;
																				firstActiveTab = m.getName();
											%>
			<li class="active"><a href="#<%=m.getName()%>" data-toggle="tab"><%=actionName%></a></li>
			<%
											}else{
										%>
			<li><a href="#<%=m.getName()%>" data-toggle="tab"><%=actionName%></a></li>

			<%
																					} 
																												
																												}
																				%>

			<%
																			}
																				  }
																		%>
		</ul>


		<div id="my-tab-content" class="tab-content">

			<%if(methodCount != 1){%>
			<br>
			<s:top-components page="${screenInstance}" />
			<%}%>

			<%
					
					for (Map.Entry<String, Set<Method>> entry :  methods.entrySet()){ 
													
							int pCounter = 0;
							for (Method m : entry.getValue() ){ 
							
							    
							   
								Action action  = m.getAnnotation(Action.class);
								String actionName;
								if (!action.value().equals("")){
									actionName = action.value();
								}else{
									actionName = m.getName();
								}
							%>

			<% if (m.getName().equals(firstActiveTab)){ %>
			<div class="tab-pane fade active in"
				bs-title="<%=action.description()%>" id="<%=m.getName()%>">

				<%}else{%>
				<div class="tab-pane fade" bs-title="<%=action.description()%>"
					id="<%=m.getName()%>">
					<%} %>


					<%if (!action.description().equals("")){ %>
					<br>
					<div class="bs-component">
						<div class="list-group">
							<a href="#" class="list-group-item">
								<h4 class="list-group-item-heading"><%=action.descriptionTitle()%></h4>
								<p class="list-group-item-text"><%=action.description()%></p>
							</a>
						</div>
					</div>

					<%}else{%>
					<br>
					<%}%>

					<% if (methodCount > 1){ %>

					<%@include file="form.jsp"%>

					<%} %>

				</div>

				<% 		}  
					        
				     }%>


			</div>

			<br>
			
			<s:bottom-components page="${screenInstance}" />
			
			<s:container page="${screenInstance}"/>


			<div class="footer">
				<p class="text-muted"><%=context.footerMessage()%>
					- built with <a href="#">Sugar Framework</a>
				</p>
			</div>

		</div>

	</div>

	<!-- container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<script type="text/javascript" language="javascript"
		src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script src="assets-boot/js/bootstrap.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
	
	<script src="assets-boot/js/bootstrap-datepicker.js"></script>
	<script src="assets-boot/js/layout.js"></script>

	<!-- Component script references -->
	<s:inject-scripts page="${screenInstance}" />

	<script type="text/javascript">
		$(document).ready(function() {

			<s:inject-document-ready page="${screenInstance}"/>

			$('#tabs').tab();
			$('.datepicker').datepicker();

			// validate parameters if filled in
			$("input").each(function() {
				if (this.value != "") {
					this.focus();
					this.blur();
				}
			});
			
			
			layout();

		});

		function validateParameter(mName, paramName, component, id, dValue,
				buttonId) {

			var form = component.parent().parent().parent();

			var val = component.val();

			$.ajax({
				url : 'validate',
				type : 'post',
				data : {
					methodName : mName,
					parameterName : paramName,
					value : val
				},
				success : function(response) {

					$('#' + id).find('.help-block').remove();
					$('#' + id).removeClass('has-error');

					var found = false;

					form.find(".has-error").each(function() {
						found = true;
					});

					if (found == false) {
						form.find('button').removeAttr("disabled");
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {

					$('#' + id).find('.help-block').remove();
					$('#' + id).find('.groupy')
							.append(
									"<div class='help-block'>" + thrownError
											+ "</div>");
					$('#' + id).removeClass('has-success')
							.addClass('has-error');

					form.find('button').attr("disabled", "disabled");

				}

			});

		}

		String.prototype.format = function() {
			var args = arguments;
			return this.replace(/{(\d+)}/g, function(match, number) {
				return typeof args[number] != 'undefined' ? args[number]
						: match;
			});
		};

		function success(message) {
			$("#alert-container")
					.append(
							'<div class="alert alert-success alert-dismissable">'
									+ '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'
									+ message + '</div>');
		}

		function error(message) {
			$("#alert-container")
					.append(
							'<div class="alert alert-danger alert-dismissable">'
									+ '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'
									+ message + '</div>');
		}

		$("form").submit(function(e) {
			var form = $(this);
			var geturl;
			var originalText = form.find('button').text();

			geturl = $.ajax({
				url : form.attr('action'),
				type : form.attr('method'),
				data : form.serialize(),
				success : function(response) {

					if (geturl.status == 278) {
						window.location.href = geturl.responseText;
					} else {
						if (geturl.responseText) {
							success(geturl.responseText);
						}
					}
					form.find('button').text(originalText);
					form.find('fieldset').removeAttr("disabled");

				},
				error : function(xhr, ajaxOptions, thrownError) {
					error(thrownError);
					form.find('button').text(originalText);
					form.find('fieldset').removeAttr("disabled");
				}

			});

			form.find('button').text("Executing...");
			form.find('fieldset').attr("disabled", "disabled");

			return false;
		});
	</script>

</body>
</html>
