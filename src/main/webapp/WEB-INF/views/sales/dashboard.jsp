<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="stock-status-wrapper">
	<!-- Content Header (Page header) -->
<sec:authorize access="canAccess(101)">
	<div class="col-md-2">
		<a href="${contextPath}/inquiry/listview"> <!-- small box -->
			<div class="small-box bg-aqua nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="inquiry-count">0</h3>
					<p>Inquiry</p>
				</div>
				<div class="icon">
					<i class="fa fa-headphones"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(102)">
	<div class="col-md-2">
		<a href="${contextPath}/sales/proformainvoice"> <!-- small box -->
			<div class="small-box bg-green nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="porforma-count">0</h3>
					<p>Proforma Invoice</p>
				</div>
				<div class="icon">
					<i class="fa fa-file-pdf-o"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(103)">
	<div class="col-md-2">
		<a href="${contextPath}/sales/reserved"> <!-- small box -->
			<div class="small-box bg-red nav-box blur" data-nav="2">
				<div class="inner">
					<h3 id="reserved-count">0</h3>
					<p>Reserved</p>
				</div>
				<div class="icon">
					<i class="fa fa-bookmark-o"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(104)">
	<div class="col-md-2">
		<a href="${contextPath}/sales/sales-order-invoice-list"> <!-- small box -->
			<div class="small-box bg-yellow nav-box blur" data-nav="4">
				<div class="inner">
					<h3 id="sales-count">0</h3>
					<p>Sold</p>
				</div>
				<div class="icon">
					<i class="fa fa-shopping-cart"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
	<!-- ./col -->
<sec:authorize access="canAccess(105)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/sales/status">
			<div class="small-box bg-purple nav-box blur" data-nav="5">
				<div class="inner">
					<h3 id="status-count">0</h3>
					<p>Shipping Status</p>
				</div>
				<div class="icon">
					<i class="fa fa-check-circle"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
</div>
