<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- Document Dash Board -->
<div class="row" id="stock-status-wrapper">
<sec:authorize access="canAccess(91)">
	<div class="col-lg-4">
		<a href="${contextPath}/shipping/bl/document-draft">
			<div class="small-box bg-aqua auction-nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="draft-bl">0</h3>
					<p>Waiting for Draft B/L</p>
				</div>
				<div class="icon">
					<i class="fa fa-edit"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
<sec:authorize access="canAccess(92)">
	<div class="col-lg-4">
		<a href="${contextPath}/shipping/bl/document-original">
			<div class="small-box bg-red auction-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="original-bl">0</h3>
					<p>Waiting for Original B/L</p>
				</div>
				<div class="icon">
					<i class="fa fa-file"></i>
				</div>
			</div>
		</a>
	</div>
</sec:authorize>
</div>


