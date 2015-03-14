<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html ng-app="onionApp">
	<head>
	    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
		
		<link rel="icon" type="image/png" href="resources/img/onion-ico.png">
		
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.css">
		
		<!-- ng-table default styling -->
		<!--
		<link rel="stylesheet" href="resources/css/ng-table.css">
		-->
		 
	    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
		 
		<link rel="stylesheet" href="resources/css/bootstrap-custom.css">
	     
	    <title>OnionApp</title>
	</head>
	
	<body>
	
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
				  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button>
				  <a class="navbar-brand" href="#">OnionApp</a>
				</div>
				
				
				<div class="collapse navbar-collapse">
				  <ul class="nav navbar-nav">
				    <li class="active"><a href="#/player-stats">Players</a></li>
				    <li><a href="#/hand-stats">Hands</a></li>
				  </ul>
				</div>
			</div>
		</div>
		<div class="container">
			<!-- here be all the content -->
			<div class="col-md-9" ng-view></div>
			
			<!-- sidebar -->
			<div class="col-md-3">
				<div class="form-group">
					<a href="<c:url value="j_spring_security_logout"/>">Logout</a></p>				
				</div>
				
				<div>
					<h4>The database holds ...</h4>
					<p>X players</p>
					<p>Z played hands</p>
					<p>Y actions</p>
				</div>
				
				<hr>
				
				<div>
					<h4>Submit PokerStars hand history:</h4>
					<form role="form" method="POST" action="upload" enctype="multipart/form-data">
						<div class="form-group">
						<input id="handHistoryInput" type="file" name="file">
						</div>
						
						<button type="submit" class="btn btn-primary">Upload</button>
					</form>
				</div>
			</div>
		</div>
		
		<div class="container">
		<hr>

		<footer>
			<p>&copy; Lauri Rummukainen 2014</p>
		</footer>
		
		</div>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
		<script type='text/javascript' src="http://code.highcharts.com/highcharts.js"></script>
		
		<!-- Angular scripts -->
		<script src="resources/js/angular.js"></script>
		<script src="resources/js/angular-route.js"></script>
		<script src="resources/js/angular-resource.js"></script>
		<script src="resources/js/angular-cookies.min.js"></script>
		<script src="resources/js/highcharts-ng.js"></script>
		<script src="resources/js/ng-table.js"></script>
		
		<!-- Toastr - for cool notifications -->
		<script src="resources/js/toastr.js"></script>
		
		<!-- UI bootstrap -->
		<script src="resources/js/ui-bootstrap.js"></script>
		
		<!-- Own scripts -->
		<script src="resources/js/app.js"></script>
		<script src="resources/js/controllers.js"></script>
	</body>
</html>