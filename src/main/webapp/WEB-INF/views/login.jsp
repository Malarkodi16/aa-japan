
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
<!--<meta name="viewport" content="width=device-width, initial-scale=1">-->
<jsp:include page="/WEB-INF/views/common/link.jsp" />
</head>
<!-- onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="" -->
<body class="hold-transition login-page">
<div class="login-box">
  <div class="login-logo">
    <a href="#">AA Japan</a>
  </div>
  <!-- /.login-logo -->
  <div class="login-box-body">
  
   <c:if test="${not empty error}">
   
   <div class="alert alert-danger alert-dismissible">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                Username or Password is Wrong.
              </div>
</c:if>
         
          
   
 
    <p class="login-box-msg">Sign in to start your session</p>

   <form method="POST" action="${contextPath}/login" class="form-signin" id="loginform">
      <div class="form-group has-feedback">
      <div style="color: green">${logout}</div>
        <input type="text" name="username" class="form-control" placeholder="Username">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
      </div>
      <div class="form-group has-feedback">
        <input type="password" name="password" class="form-control" placeholder="Password">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
      </div>
      <div class="row">
        <!-- /.col -->
        <div class="col-xs-4 pull-right">
          <button class="btn btn-primary btn-block btn-flat">Sign In</button>
        </div>
        <!-- /.col -->
      </div>
    </form>

   

  </div>
  <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<jsp:include page="/WEB-INF/views/common/script.jsp" />
</body>
</html>
