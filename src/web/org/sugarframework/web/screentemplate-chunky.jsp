<%@page import="org.sugarframework.DefaultValue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.sugarframework.View"%>
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

<%
	View screen = (View) session.getAttribute("screenAnnotation");
Map<String, Set<Method>> methods  = (Map<String, Set<Method>>) session.getAttribute("methods");
Set<View> screens = (Set<View>) session.getAttribute("screens");
Context context = (Context) session.getAttribute("context");
Object screenInstance = session.getAttribute("screenInstance");

int bCounter = 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title><%=screen.title()%></title>

<!-- Bootstrap core CSS -->
<link href="assets-boot/css/style/<%=context.style()%>" rel="stylesheet">
<link href="assets-boot/css/docs.css" rel="stylesheet">
<link href="assets-boot/css/justified-nav.css" rel="stylesheet">
<link href="assets-boot/css/datepicker.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="assets-boot/css/jumbotron-narrow.css" rel="stylesheet">
<style>


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

	<div class="container">

		<div class="navbar navbar-default" role="navigation">
			
			<div class="navbar-header">
	          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="navbar-brand" href="#"><%=context.name()%></a>
	        </div>
	        
	   
		
			<div class="navbar-collapse collapse">
			
				<ul class="nav navbar-nav">
	
					<%
							for (View s : screens){
										if (s.equals(screen)){
						%>
						<li class="active"><a href="<%=s.url()%>"><%=s.name()%></a></li>
					<%  }else{%>
						<li><a href="<%=s.url()%>"><%=s.name()%></a></li>
					<%	}
					}%>
	
				</ul>
			</div>	
		
		 </div>
		
		<div class="masthead">
			<ul id="tabs" class="nav nav-justified" data-tabs="tabs">

				<%
				String firstActiveTab = "";
				for (Map.Entry<String, Set<Method>> entry :  methods.entrySet()){
								
					boolean first = true;
					
									for (Method m : entry.getValue()  ){ 
										Action action  = m.getAnnotation(Action.class);
										String actionName;
										if (!action.name().equals("")){
											actionName = action.name();
										}else{
											actionName = m.getName();
										}
										
										if (first == true){
											first = false;
											firstActiveTab = m.getName();
											%>
											<li class="active"><a href="#<%=m.getName()%>" data-toggle="tab"><%=actionName%></a></li>
										<%}else{ %>
											<li><a href="#<%=m.getName()%>" data-toggle="tab"><%=actionName%></a></li>
										
										<%} %>
									
									<%}
				  }%>
			</ul>


				<div id="my-tab-content" class="tab-content">
					<%
					
					for (Map.Entry<String, Set<Method>> entry :  methods.entrySet()){ 
													
							int pCounter = 0;
							for (Method m : entry.getValue() ){ 
								Action action  = m.getAnnotation(Action.class);
								String actionName;
								if (!action.name().equals("")){
									actionName = action.name();
								}else{
									actionName = m.getName();
								}
							%>
							
							<% if (m.getName().equals(firstActiveTab)){ %>
							
								<div class="tab-pane fade active in" bs-title="<%=action.description()%>" id="<%=m.getName()%>">
							
							<%}else{%>
								<div class="tab-pane fade" bs-title="<%=action.description()%>" id="<%=m.getName()%>">
							<%} %>
							
							
							<%if (!action.description().equals("")){ %>
							
							<div class="bs-callout bs-callout-info">
						      <h4><%=action.descriptionTitle()%></h4>
						      <p><%=action.description()%></p>
						    </div>
							<%}else{%>
							<br>
							<%}%>
							
							<form class="form-horizontal" role="form" action="execute" method="post">
								<fieldset>
								<%
									Class<?>[] pTypes = m.getParameterTypes();
									int pCount = 0;
								
									Map<String, Set<Annotation>> parameterAnnos = org.sugarframework.util.Reflector.getAllParameterAnnotations(m);	
									String buttonId = "b-" + bCounter++;
									for (Map.Entry<String, Set<Annotation>> e : parameterAnnos.entrySet()){
																															              	
										String parameterName = e.getKey();
										DefaultValue defaultValue = org.sugarframework.util.Reflector.find(DefaultValue.class, e.getValue());
										String dValue = "";
										if (defaultValue != null){
											dValue = defaultValue.value();
										}
										Validate validator = org.sugarframework.util.Reflector.find(Validate.class, e.getValue());
										String id = "cg-" + pCounter++;
										
										Choice choice = org.sugarframework.util.Reflector.find(Choice.class, e.getValue());
										if (choice !=null){
								%>
										
									<div class="form-group" id="<%=id%>">
										<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
										<div class="col-sm-6">
											<select class="form-control" name="<%=parameterName%>" id="input<%=parameterName%>">
												<%for (String aChoice : choice.value()){ %>
												<option><%=aChoice %></option>
												<%} %>
											</select>
										</div>
									</div>
									<%} else if ( pTypes[pCount].equals(Boolean.class) || pTypes[pCount].equals(boolean.class) ) { %>
									<div class="form-group" id="<%=id%>">
										<div class="col-sm-offset-4 col-sm-6">
											<label class="checkbox"> <input type="checkbox"
												name="<%=parameterName%>" id="input<%=parameterName%>">
												<%=parameterName%>
											</label>
										</div>
									</div>
									<%} else if ( pTypes[pCount].equals(java.util.Date.class) ) { %>
		
									<div class="form-group" id="<%=id%>">
										<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
										<div class="col-sm-4 input-group left-inner-addon">
											<i class="glyphicon glyphicon-calendar"></i>
											<input type="text" class="form-control datepicker" name="<%=parameterName%>"
												id="input<%=parameterName%>" value=""
												data-date-format="mm/dd/yyyy">
										</div>
									</div>
				
									<%} else if ( org.sugarframework.util.Reflector.isBean(pTypes[pCount]) ) { %>
		
									<%@include file="bean.jsp"%>
		
									<%} else { %>
										
									<div class="form-group" id="<%=id%>">
										<label class="col-sm-4 control-label" for="input<%=parameterName%>"><%=parameterName%></label>
										<div class="col-sm-6">
											<input type="text" class="form-control" name="<%=parameterName%>"
												id="input<%=parameterName%>" placeholder="<%=parameterName%>"
												value="<%=dValue%>" <%if(validator != null){ %>
												onblur="validateParameter('<%=validator.value()%>','<%=parameterName%>', $(this), '<%=id%>','<%=dValue%>', '<%=buttonId%>' )"
												<%} %>>
										</div>
									</div>
		
									<%} 
														
									pCount++;
														
									}%>
		
		
									 <div class="form-group">
    									<div class="col-sm-offset-4 col-sm-6">
											<button id="<%=buttonId%>" type="submit"
												class="btn btn-success"><%=actionName%>
												&raquo;
											</button>
										</div>
									</div>
		
									<input type="hidden" name="methodName" value="<%=m.getName()%>">
		
									</fieldset>
								</form>
								
								
							</div>

					<% 		}  
					        
				     }%>


				</div>
				
				
				<div class="footer">
        	<p><%=context.footerMessage()%></p>
   			</div>

			</div>
			
			
		
		</div>
		
		</div>
		<!-- container -->

			<!-- Bootstrap core JavaScript
    ================================================== -->
			<!-- Placed at the end of the document so the pages load faster -->
			<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
			<script src="assets-boot/js/bootstrap.min.js"></script>
			
			<script src="assets-boot/js/bootstrap-datepicker.js"></script>
			
			<!-- noty -->
		<script type="text/javascript" src="assets-boot/js/noty/jquery.noty.js"></script>

		<!-- layouts -->
		<script type="text/javascript" src="assets-boot/js/noty/layouts/bottom.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/bottomCenter.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/bottomLeft.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/bottomRight.js"></script>
		<script type="text/javascript" src="assets-boot/js/noty/layouts/center.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/centerLeft.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/centerRight.js"></script>
		<script type="text/javascript" src="assets-boot/js/noty/layouts/inline.js"></script>
		<script type="text/javascript" src="assets-boot/js/noty/layouts/top.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/topCenter.js"></script>
		<script type="text/javascript" src="assets-boot/js/noty/layouts/topLeft.js"></script>
		<script type="text/javascript"
			src="assets-boot/js/noty/layouts/topRight.js"></script>

		<!-- themes -->
		<script type="text/javascript" src="assets-boot/js/noty/themes/default.js"></script>
			<script type="text/javascript">
		
			var notyLayout = '<%=context.messagePosition()%>';
		
			jQuery(document).ready(function($) {
				$('#tabs').tab();
				$('.datepicker').datepicker();
			});
			
			function validateParameter( mName, paramName, component, id, dValue, buttonId){
				
				var val = component.val();
				
				$.ajax({ 
			         url   : 'validate',
			         type  : 'post',
			         data  : { methodName: mName, parameterName: paramName, value: val },
			         success: function(response){
			             
			             $('#' + id).find('.help-block').remove();
			             $('#' + id).removeClass('has-error');//.addClass('has-success');
			             
			        	
			         },
			         error: function (xhr, ajaxOptions, thrownError) {
			           
			             $('#' + id).find('.help-block').remove();
			             $('#' + id).append("<div class='help-block'>" + thrownError + "</div>");
			             $('#' + id).removeClass('has-success').addClass('has-error');
			         }
			         
			    });
				
			}
			
			String.prototype.format = function() {
				  var args = arguments;
				  return this.replace(/{(\d+)}/g, function(match, number) { 
				    return typeof args[number] != 'undefined'
				      ? args[number]
				      : match
				    ;
				  });
				};

			
			  function alert(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'alert',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			  
			  function error(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'error',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			  
			  function info(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'information',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			  
			  function warning(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'warning',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			  
			  function notification(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'notification',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			  
			  function success(type) {
				  	var n = noty({
				  		text: type,
				  		type: 'success',
				      dismissQueue: true,
				  		layout: notyLayout,
				  		theme: 'defaultTheme'
				  	});
			   }
			
			$("form").submit(function(e){
			    var form = $(this);
			    var geturl;
			    var originalText = form.find('button').text();

			    geturl = $.ajax({ 
			         url   : form.attr('action'),
			         type  : form.attr('method'),
			         data  : form.serialize(),
			         success: function(response){
			        	
			        	  if (geturl.status == 278) {
			        		  //success(geturl.responseText);
			        		  window.location.href = geturl.responseText;
			        	  }else{
			        		  //success('Success!');
			        	  }
			        	  form.find('button').text(originalText);
			        	  form.find('fieldset').removeAttr("disabled");
			        		  
			         },
			         error: function (xhr, ajaxOptions, thrownError) {
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
