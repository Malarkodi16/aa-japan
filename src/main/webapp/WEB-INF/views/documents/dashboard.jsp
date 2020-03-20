<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row form-group" id="document-status-wrapper">
<sec:authorize access="canAccess(122)">
	<div class="col-md-2">
		<a class="statusFilter"
			href="${contextPath}/documents/tracking/not-received">
			<div class="small-box bg-red nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="notReceived-count">0</h3>
					<p>Not Received</p>
				</div>
				<div class="icon">
					<i class="fa fa-search"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(123)">
	<div class="col-md-2">
		<!-- small box -->
		<a class="statusFilter active-payment"
			href="${contextPath}/documents/tracking/received">
			<div class="small-box bg-orange nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="received-count">0</h3>
					<p>Received</p>
				</div>
				<div class="icon">
					<i class="fa fa-truck"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(124)">
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/documents/tracking/export-certificates">
			<div class="small-box bg-blue nav-box blur" data-nav="2">
				<div class="inner">
					<h3 id="certificate-count">0</h3>
					<p>Export Certificates</p>
				</div>
				<div class="icon">
					<i class="fa fa-fighter-jet"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(125)">
	<div class="col-md-2">
		<!-- small box -->
		<a class="statusFilter"
			href="${contextPath}/documents/tracking/name-transfer">
			<div class="small-box bg-yellow nav-box blur" data-nav="3">
				<div class="inner">
					<h3 id="nameTransfer-count">0</h3>
					<p>Name Transfer</p>
				</div>
				<div class="icon">
					<i class="fa fa-exchange"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(126)">
	<div class="col-md-2">
	<!-- small box -->
		<a href="${contextPath}/documents/tracking/cancelled-list">
			<div class="small-box bg-red nav-box blur" data-nav="4">
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