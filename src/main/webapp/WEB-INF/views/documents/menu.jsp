<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu" data-widget="tree">
			<sec:authorize access="canAccess(121)">
				<li class="save-side-menu-state"
					data-menu-parent="document-tracking" data-menu-child=""
					data-role="parent"><a
					href="${contextPath}/documents/tracking/not-received"> <i
						class="fa fa-book"></i> <span>Document Tracking</span> <span
						class="pull-right-container"> </span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(127)">
				<li class="save-side-menu-state" data-menu-parent="cr-received"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/documents/tracking/cr-received"><i
						class="fa fa-get-pocket"></i> <span>CR Received</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(128)">
				<li class="save-side-menu-state"
					data-menu-parent="document-export-cerficate-tracking"
					data-menu-child="" data-role="parent"><a
					href="${contextPath}/documents/tracking/export-cerficate-tracking">
						<i class="fa fa-book"></i> <span>Export Certificate
							Tracking</span> <span class="pull-right-container"> </span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(129)">
				<li class="save-side-menu-state" data-menu-parent="recycle-claim"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/documents/recycle/claim"><i
						class="fa fa-recycle"></i> <span>Claim</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(132)">
				<li class="save-side-menu-state"
					data-menu-parent="year-of-manufacture" data-menu-child=""
					data-role="parent"><a
					href="${contextPath }/documents/year-of-manufacture/view"><i
						class="fa fa-cogs"></i> <span>Year of Manfacture</span></a></li>
			</sec:authorize>
			<li class="save-side-menu-state" data-menu-parent="stock-tracker"
				data-menu-child="" data-role="parent"><a
				href="${contextPath }/documents/tracking/custom/report"><i
					class="fa fa-file-text-o"></i> <span>Stock Organizer</span></a></li>
			<li><a href="${contextPath }/documents/tracking/stockInfo"><i
					class="fa fa-info"></i><span>Stock Info</span></a></li>
		</ul>

	</section>
	<!-- /.sidebar -->
</aside>
