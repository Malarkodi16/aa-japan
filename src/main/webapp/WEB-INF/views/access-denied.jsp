
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>AAJ | Login</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<jsp:include page="/WEB-INF/views/common/link.jsp" />
</head>
<body class="hold-transition login-page">
	<div class="wrapper">
		<div class="error-page">
			<h2 class="headline text-yellow">403</h2>
			<div class="error-content">
				<h3>
					<i class="fa fa-warning text-yellow"></i> Oops! Access Denied.
				</h3>
				<p>
					You are not allowed to enter this page.<a href="${contextPath }/login">return to login</a>
				</p>
			</div>
			<!-- /.error-content -->
		</div>
		<!-- /.error-page -->
	</div>
	<jsp:include page="/WEB-INF/views/common/script.jsp" />
</body>
</html>
