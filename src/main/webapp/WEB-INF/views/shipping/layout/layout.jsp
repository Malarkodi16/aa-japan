<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<tiles:importAttribute name="javascripts" />
<tiles:importAttribute name="stylesheets" />
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><tiles:insertAttribute name="title"></tiles:insertAttribute></title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- Bootstrap 3.3.7 -->
<link rel="stylesheet"
	href="${contextPath }/resources/assets/bower_components/bootstrap/dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet"
	href="${contextPath }/resources/assets/bower_components/font-awesome/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet"
	href="${contextPath }/resources/assets/bower_components/Ionicons/css/ionicons.min.css">
<!-- stylesheets -->
<c:forEach var="css" items="${stylesheets}">
	<link rel="stylesheet" type="text/css" href="<c:url value="${css}"/>">
</c:forEach>
<script>
	var myContextPath = "${pageContext.request.contextPath}"
</script>
<!-- Theme style -->
<link rel="stylesheet"
	href="${contextPath }/resources/assets/dist/css/AdminLTE.min.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${contextPath }/resources/assets/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet"
	href="${contextPath }/resources/assets/custom/css/custom.css">
<link rel="stylesheet"
	href="${contextPath }/resources/assets/dist/css/StockImagePreview.css">
<link rel="shortcut icon" href="https://images.aajapancars.com/favicons/favicon.ico">
<script type="text/javascript">
	document.onreadystatechange = function() {
		var state = document.readyState
		if (state == 'complete') {
			$('#spinner').hide()
		}
	}
</script>

</head>
<body class="hold-transition skin-blue sidebar-mini fixed">
	<div id="spinner" class="modal fade in" role="dialog"
		style="display: block; z-index: 10000000;">
		<div class="modal-dialog" align="center">
			<i class="fa fa-refresh fa-spin fa-3x fa-fw" style="margin-top: 20%"></i>
			<span class="sr-only">Loading...</span>
		</div>
	</div>
	<div class="wrapper">
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="menu" />
		<div class="content-wrapper">
			<tiles:insertAttribute name="body" />
		</div>
		<tiles:insertAttribute name="footer" />
	</div>
	<!-- jQuery 3 -->
	<script
		src="${contextPath }/resources/assets/bower_components/jquery/dist/jquery.min.js"></script>
	<!-- Bootstrap 3.3.7 -->
	<script
		src="${contextPath }/resources/assets/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
	<!-- FastClick -->
	<script
		src="${contextPath }/resources/assets/bower_components/fastclick/lib/fastclick.js"></script>
	<!-- AdminLTE App -->
	<script src="${contextPath }/resources/assets/dist/js/adminlte.min.js"></script>
	<script
		src="${contextPath }/resources/assets/plugins/jquery-validation/jquery.validate.js"></script>
	<script
		src="${contextPath }/resources/assets/custom/js/common/common-functions.js"></script>
	<script
		src="${contextPath }/resources/assets/plugins/jquery-i18n-properties-1.2.7/jquery.i18n.properties.js"></script>
	<script
		src="${contextPath }/resources/assets/bower_components/stomp-sockjs/sockjs-0.3.4.min.js"></script>
	<script
		src="${contextPath }/resources/assets/bower_components/stomp-sockjs/stomp.min.js"></script>
	<script
		src="${contextPath }/resources/assets/custom/js/common/notification.js"></script>

	<!-- scripts -->
	<c:forEach var="script" items="${javascripts}">
		<script src="<c:url value="${script}"/>"></script>
	</c:forEach>
	<script type="text/javascript">
		$(function() {
			//update side menu state
			setState()
			//save side menu toggle state to local storage
			$('.save-side-menu-state').on(
					'click',
					function() {
						localStorage.setItem("aaj-menu-parent", $(this).data(
								'menu-parent'));
						localStorage.setItem("aaj-menu-child", $(this).data(
								'menu-child'));
					})
			
			//highlight nav box
			$('.nav-box').on(
					'click',
					function() {
						localStorage.setItem(
								"aaj-shipping-dashboard-active-nav", $(this)
										.data('nav'));
					})

			$('.auction-nav-box').on(
					'click',
					function() {
						localStorage.setItem(
								"aaj-document-dashboard-active-nav", $(this)
										.data('nav'));
					})

			$('.cancel-nav-box').on(
					'click',
					function() {
						localStorage.setItem("aaj-cancel-dashboard-active-nav",
								$(this).data('nav'));
					})

		});
		function setState() {
			if (window.location.pathname == myContextPath + '/stock/purchased') {
				localStorage.setItem("aaj-menu-parent", 'shipping-dashboard');
				localStorage.setItem("aaj-menu-child", '');
				localStorage.setItem("aaj-shipping-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/shipping/bl/document-draft') {
				localStorage.setItem("aaj-document-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/stock/re-auction/list') {
				localStorage.setItem("aaj-cancel-dashboard-active-nav", '0');

			}
			var parent = localStorage.getItem("aaj-menu-parent");
			var child = localStorage.getItem("aaj-menu-child");
			var parentEle = $('li[data-menu-parent="' + parent
					+ '"][data-role="parent"]');
			parentEle.addClass('active menu-open');
			parentEle.find(
					'li[data-menu-child="' + child + '"][data-role="child"]')
					.addClass('active');
			/* if (localStorage.getItem("sidebar-collapse") == 'true') {
				$("body").addClass('sidebar-collapse')
			} else {
				$("body").removeClass('sidebar-collapse')
			} */

			//status dashboard
			var dashboardNav = localStorage
					.getItem("aaj-shipping-dashboard-active-nav");
			var navBox = $('#stock-status-wrapper').find(
					'div.nav-box[data-nav="' + dashboardNav + '"]');
			navBox.addClass('border-solid').removeClass('blur');
			//document dashboard
			var reauctiondashboardNav = localStorage
					.getItem("aaj-document-dashboard-active-nav");
			var navBoxreauction = $('#stock-status-wrapper').find(
					'div.auction-nav-box[data-nav="' + reauctiondashboardNav
							+ '"]');
			navBoxreauction.addClass('border-solid').removeClass('blur');

			//calcel dashboard
			var canceldashboardNav = localStorage
					.getItem("aaj-cancel-dashboard-active-nav");
			var navBoxcancel = $('#stock-status-wrapper')
					.find(
							'div.cancel-nav-box[data-nav="'
									+ canceldashboardNav + '"]');
			navBoxcancel.addClass('border-solid').removeClass('blur');
		}
	</script>
	<script type="text/javascript">
		try {
		  $.fn.dataTable.ext.errMode = 'none';
		}catch(err) {
			console.log("datatable not found")
		}
	</script>
</body>
</html>