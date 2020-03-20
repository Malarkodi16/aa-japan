<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row form-group" id="document-status-wrapper">
<sec:authorize access="canAccess(96)">
	<div class="col-md-3">
		<a class="statusFilter"
			href="${contextPath}/shipping/document/not-received">
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
<sec:authorize access="canAccess(97)">
	<div class="col-md-3">
		<!-- small box -->
		<a class="statusFilter active-payment"
			href="${contextPath}/shipping/document/received">
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
<sec:authorize access="canAccess(98)">
	<div class="col-md-3">
		<!-- small box -->
		<a href="${contextPath}/shipping/document/export-certificates">
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
<sec:authorize access="canAccess(99)">
	<div class="col-md-3">
		<!-- small box -->
		<a class="statusFilter"
			href="${contextPath}/shipping/document/name-transfer">
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
</div>