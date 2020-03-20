<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div class="row" id="stock-status-wrapper">
<sec:authorize access="canAccess(65)">
	<div class="col-md-2">
		<a href="${contextPath}/stock/purchased">
			<div class="small-box bg-aqua nav-box blur box box-solid" data-nav="0">
				<div class="inner">
					<h3 id="purchased-count">0</h3>
					<p>Purchased</p>
				</div>
				<div class="icon">
					<i class="fa fa-shopping-cart"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(66)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/stock/purchase-confirmed">
			<div class="small-box bg-green nav-box blur box box-solid" data-nav="1">
				<div class="inner">
					<h3 id="purchase-confirmed-count">0</h3>
					<p>Purchase Confirmed</p>
				</div>
				<div class="icon">
					<i class="fa fa-thumbs-o-up"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(67)">
	<div class="col-md-2">
		<a href="${contextPath}/transport/list">
			<div class="small-box bg-red nav-box blur box box-solid" data-nav="2">
				<div class="inner">
					<h3 id="transportation-count">0</h3>
					<p>Transportation</p>
				</div>
				<div class="icon">
					<i class="fa fa-truck"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(68)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/inspection/inspection">
			<div class="small-box bg-orange nav-box blur box box-solid" data-nav="3">
				<div class="inner">
					<h3 id="inspection-count">0</h3>
					<p>Inspection</p>
				</div>
				<div class="icon">
					<i class="fa fa-search"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(69)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/shipping/management/available">
			<div class="small-box bg-blue nav-box blur box box-solid" data-nav="4">
				<div class="inner">
					<h3 id="shipping-arrangement-count">0</h3>
					<p>Shipping</p>
				</div>
				<div class="icon">
					<i class="fa fa-ship"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(70)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/shipping/status">
			<div class="small-box bg-purple nav-box blur box box-solid" data-nav="5">
				<div class="inner">
					<h3 id="shipping-status-count">0</h3>
					<p>Shipping Status</p>
				</div>
				<div class="icon">
					<i class="fa fa-check-circle"></i>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
</div>
