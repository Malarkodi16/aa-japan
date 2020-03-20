<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="stock-status-wrapper">
	<!-- Content Header (Page header) -->
<sec:authorize access="canAccess(117)">
	<div class="col-md-2">
		<a href="${contextPath}/invoice/porforma-invoice-management"> <!-- small box -->
			<div class="small-box bg-aqua invoice-nav-box blur" data-nav="0">
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
<sec:authorize access="canAccess(118)">
	<div class="col-md-2">
		<a href="${contextPath}/invoice/sales-invoice-management"> 
			<div class="small-box bg-green invoice-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="sales-count">0</h3>
					<p>Sales Invoice</p>
				</div>
				<div class="icon">
					<i class="fa fa-file-pdf-o"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
</div>
