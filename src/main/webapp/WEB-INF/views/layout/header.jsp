<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<header class="main-header">
	<!-- Logo -->
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<a href="${contextPath }/a/dashboard" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
			<span class="logo-mini"><b>AA</b>J</span> <!-- logo for regular state and mobile devices -->
			<span class="logo-lg"><b>AA</b>Japan</span>
		</a>
	</sec:authorize>
	<sec:authorize
		access="hasAnyRole('ROLE_SHIPPING','ROLE_SALES','ROLE_ACCOUNTS','ROLE_DOCUMENTS')">
		<a href="#" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
			<span class="logo-mini"><b>AA</b>J</span> <!-- logo for regular state and mobile devices -->
			<span class="logo-lg"><b>AA</b>Japan</span>
		</a>
	</sec:authorize>
	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top">
		<!-- Sidebar toggle button-->
		<a href="#" class="sidebar-toggle" data-toggle="push-menu"
			role="button"> <span class="sr-only">Toggle navigation</span>
		</a>
		<!-- Navbar Right Menu -->
		<div class="navbar-custom-menu">
			<ul class="nav navbar-nav">
				<li class="dropdown messages-menu" id="notification-container"><a
					href="#" class="dropdown-toggle" data-toggle="dropdown"> <i
						class="fa fa-bell-o"></i> <span class="label label-warning"
						id="unread-count"></span>
				</a>
					<ul class="dropdown-menu">
						<li class="header">You have <span id="count"></span>
							notifications
						</li>
						<li>
							<div class="menu">
								<table id="messageContainer"
									class="table table-bordered table-striped"
									style="width: 100%; overflow: scroll;">
									<thead>
										<tr>
											<th style="width : 50%">User Name</th>
											<!-- <th style="width : 50%">Created Date</th> -->
										</tr>
									</thead>
									<tbody>

									</tbody>
								</table>
							</div>
						</li>

					</ul></li>
				<li class="dropdown user user-menu"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"> <span
						class="hidden-xs" id="userInfo"><sec:authentication
								property="principal.login.user.fullname" /></span>
				</a>
					<ul class="dropdown-menu">
						<!-- Menu Footer-->
						<li class="user-footer">
							<form action="${contextPath}/logout" method="post">
								<div class="pull-right">
									<a class="save-side-menu-state"
										data-menu-parent="shipping-dashboard" data-menu-child=""><button
											type="submit" class="btn btn-default btn-flat">Sign
											out</button></a>
								</div>
							</form>
						</li>
					</ul></li>
			</ul>
		</div>
	</nav>
	<div id="cloneable-items" class="hide">
		

	</div>
</header>
