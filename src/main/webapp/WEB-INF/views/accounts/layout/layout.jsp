<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/tld/jsonutils.tld" prefix="jsonUtils"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('ADMIN')" var="isAdmin"></sec:authorize>
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
<link rel="shortcut icon"
	href="https://images.aajapancars.com/favicons/favicon.ico">

<script type="text/javascript">
	document.onreadystatechange = function() {
		var state = document.readyState
		if (state == 'complete') {
			$('#spinner').hide()
		}
	}
	if(${isAdmin}){
		setTimeout(function() {
	        $.ajax({
	            url: myContextPath + '/check/exchange.json',
	            type: 'GET',
	            success: function(isExist) {
	                if (!isExist == true) {
	                    $("#exchangerate-modal").modal('show');
	                }
	            },
	            error: function(jqXHR, textStatus, errorThrown) {
	                console.log(textStatus, errorThrown);

	            }
	        });
	    }, 1000);
	}
	
</script>
</head>
<body class="hold-transition skin-blue fixed sidebar-mini">
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
		src="${contextPath }/resources/assets/custom/js/accounts/dashboard.js"></script>
	<script
		src="${contextPath }/resources/assets/custom/js/accounts/payment-count.js"></script>
	<script
		src="${contextPath }/resources/assets/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
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
			setState()
			$('.auction-nav-box').on(
					'click',
					function() {
						localStorage.setItem(
								"aaj-payment-dashboard-active-nav", $(this)
										.data('nav'));
					})
			$('.claim-nav-box').on(
					'click',
					function() {
						localStorage.setItem("aaj-claim-dashboard-active-nav",
								$(this).data('nav'));
					})
			$('.complete-nav-box').on(
					'click',
					function() {
						localStorage.setItem(
								"aaj-payment-completed-dashboard-active-nav",
								$(this).data('nav'));
					})
			$('.freeze-nav-box').on(
					'click',
					function() {
						localStorage.setItem(
								"aaj-payment-freeze-dashboard-active-nav", $(
										this).data('nav'));
					})

		});
		function setState() {

			if (window.location.pathname == myContextPath
					+ '/accounts/auction-payment') {
				localStorage.setItem("aaj-payment-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/accounts/claim/tax') {
				localStorage.setItem("aaj-claim-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/accounts/auction-payment-done') {
				localStorage.setItem(
						"aaj-payment-completed-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/accounts/auction-payment-freezed') {
				localStorage.setItem("aaj-payment-freeze-dashboard-active-nav",
						'0');

			}
			if (window.location.pathname == myContextPath
					+ '/accounts/invoice/booking/auction') {
				localStorage.setItem("aaj-payment-dashboard-active-nav", '0');

			}
			if (window.location.pathname == myContextPath
					+ '/accounts/invoice/approval/auction') {
				localStorage.setItem("aaj-payment-dashboard-active-nav", '0');

			}
			//payment dashboard
			var dashboardNav = localStorage
					.getItem("aaj-payment-dashboard-active-nav");
			var navBox = $('#stock-status-wrapper').find(
					'div.auction-nav-box[data-nav="' + dashboardNav + '"]');
			navBox.addClass('border-solid').removeClass('blur');

			//claim dashboard

			var dashboardClaimNav = localStorage
					.getItem("aaj-claim-dashboard-active-nav");
			var navClaimBox = $('#stock-status-wrapper').find(
					'div.claim-nav-box[data-nav="' + dashboardClaimNav + '"]');
			navClaimBox.addClass('border-solid').removeClass('blur');

			//Payment Completed dashboard

			var dashboardPaymentNav = localStorage
					.getItem("aaj-payment-completed-dashboard-active-nav");
			var navCompleteBox = $('#stock-status-wrapper').find(
					'div.complete-nav-box[data-nav="' + dashboardPaymentNav
							+ '"]');
			navCompleteBox.addClass('border-solid').removeClass('blur');
			//Payment Freeze dashboard

			var dashboardFreezeNav = localStorage
					.getItem("aaj-payment-freeze-dashboard-active-nav");
			var navFreezeBox = $('#stock-status-wrapper')
					.find(
							'div.freeze-nav-box[data-nav="'
									+ dashboardFreezeNav + '"]');
			navFreezeBox.addClass('border-solid').removeClass('blur');
		}
	</script>
	<script type="text/javascript">
		try {
			$.fn.dataTable.ext.errMode = 'none';
		} catch (err) {
			console.log("datatable not found")
		}
	</script>

</body>
</html>