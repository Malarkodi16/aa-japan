<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="document-status-wrapper">
	<sec:authorize access="canAccess(130)">
		<div class="col-md-3">
			<a class="statusFilter" href="${contextPath}/documents/recycle/claim">
				<div class="small-box bg-red recycle-nav-box blur" data-nav="0">
					<div class="inner">
						<h3 id="recycle-claim-count">0</h3>
						<p>
							Recycle Claim <br> Applied / Received
						</p>
					</div>
					<div class="icon">
						<i class="fa fa-search"></i>
					</div>
				</div>
			</a>
		</div>
	</sec:authorize>
	<sec:authorize access="canAccess(131)">
		<div class="col-md-3">
			<!-- small box -->
			<a href="${contextPath}/documents/recycle/claim/insurance">
				<div class="small-box bg-orange recycle-nav-box blur" data-nav="1">
					<div class="inner">
						<h3 id="count-insurance">0</h3>
						<p>Insurance</p>
					</div>
					<div class="icon">
						<em class="fa fa-suitcase"></em>
					</div>
				</div>
			</a>
		</div>
	</sec:authorize>
</div>