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

DefaultContextInitializer.getContext().componentRegistry().onPageLoad(screenInstance);

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
<link href="assets-boot/css/sugar.css" rel="stylesheet">

<!-- Component CSS references -->
<s:inject-stylesheets page="${screenInstance}" />

<!--  
<link href="assets-boot/css/docs.css" rel="stylesheet">
<link href="assets-boot/css/jumbotron-narrow.css" rel="stylesheet">
-->

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../docs-assets-boot/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<c:choose>
<c:when test="${screen.static == false}">
  
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
				
				<c:if test="${scr.static == false}">
				
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
						<s:annotationValue annotation="${scr}" field="value" />
						
					</a>
					</li>
					
				</c:if>
					
				</c:forEach>
								
			</ul>
		</div>
		
		</div>

	</div>

</c:when>

<c:otherwise>
	</br>
</c:otherwise>
</c:choose>

    <div id="alert-container" class="container"></div>
	<div id="main-container" class="container sugar-container">
	
			<s:all-components page="${screenInstance}"></s:all-components>
	
			<s:container page="${screenInstance}"/>

			<div class="footer">
				<p class="text-muted"><%=context.footerMessage()%>
					- built with <a href="#">Sugar Framework</a>
				</p>
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
	<script src="assets-boot/js/validate.js"></script>
	<script src="assets-boot/js/forms.js"></script>

	<!-- Component script references -->
	<s:inject-scripts page="${screenInstance}" />

	<script type="text/javascript">
		$(document).ready(function() {

			<s:inject-document-ready page="${screenInstance}"/>

			$('#tabs').tab();
			$('.datepicker').datepicker();

			validatePageInputs();
			
			<% if( DefaultContextInitializer.getContext().developmentMode()!=null && DefaultContextInitializer.getContext().developmentMode().editLayout() ){%>
				layout();
			<% }%>

		});

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

		
	</script>

</body>
</html>
