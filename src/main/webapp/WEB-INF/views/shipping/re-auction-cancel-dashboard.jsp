<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- Re Auction/Cancel Dash Board -->
<div class="row" id="stock-status-wrapper">
<sec:authorize access="canAccess(75)">
	<div class="col-lg-4">
		<a href="${contextPath}/stock/re-auction/list">
			<div class="small-box bg-aqua cancel-nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="reauction-stock">0</h3>
					<p>Re Auction</p>
				</div>
				<div class="icon">
					<i class="fa fa-shopping-cart"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(76)">
	<div class="col-lg-4">
		<a href="${contextPath}/stock/purchase/cancelled/list">
			<div class="small-box bg-red cancel-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="cancel-stock">0</h3>
					<p>Cancelled</p>
				</div>
				<div class="icon">
					<i class="fa fa-close"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
</div>
