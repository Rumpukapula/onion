<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<title>Login page</title>
		
	</head>

	<body>
	
		<div class="login">
			<c:if test="${error == true}">
				<b class="text-danger">Invalid login or password.</b>
				<br/><br/>
			</c:if>
			
			<form role="form" method="POST" action="<c:url value='j_spring_security_check'/>">
				<div class="form-group">
					<label for="j_username">Username</label>
					<input type="text" class="form-control" name="j_username" id="j_username" placeholder="Username" size="30" maxlength="40"  />
				</div>
				
				<div class="form-group">
					<label for="j_password">Password</label>
					<input type="password" class="form-control" name="j_password" id="j_password" size="30" maxlength="32" placeholder="Password"/>
				</div>
				<button type="submit" class="btn btn-default">Login</button>
			</form>
		</div>
	</body>
</html>