<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu" data-widget="tree">
			<sec:authorize access="canAccess(100)">
				<li class="save-side-menu-state"
					data-menu-parent="mysales-dashboard" data-menu-child=""
					data-role="parent"><a href="${contextPath }/inquiry/listview">
						<i class="fa fa-dashboard"></i> <span>My Sales</span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(106)">
				<li class="save-side-menu-state" data-menu-parent="stock-search"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/sales/stock/stock-search"> <i
						class="fa fa-binoculars"></i> <span>Stock Search</span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(112)">
				<li class="treeview" data-menu-parent="customer-management"
					data-role="parent"><a href="#"> <i class="fa fa-users"></i>
						<span>Customer Management</span> <span
						class="pull-right-container"> <i
							class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(113)">
							<li class="save-side-menu-state"
								data-menu-parent="customer-management"
								data-menu-child="customer-management-1" data-role="child"><a
								href="${contextPath }/customer/list"><i
									class="fa fa-circle-o"></i> Customer List</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(114)">
							<li class="save-side-menu-state"
								data-menu-parent="customer-management"
								data-menu-child="customer-management-3" data-role="child"><a
								href="${contextPath }/sales/customer-transaction"> <i
									class="fa fa-circle-o"></i> <span>Customer Transaction</span>
							</a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="canAccess(115)">
				<li class="save-side-menu-state" data-menu-parent="special-user"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/sales/special-user"><i
						class="fa fa-strikethrough"></i>Special User</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(116)">
				<li class="save-side-menu-state"
					data-menu-parent="invoice-management" data-menu-child=""
					data-role="parent"><a
					href="${contextPath }/invoice/porforma-invoice-management"> <i
						class="fa fa fa-file-pdf-o"></i> <span>Invoice Management</span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(119)">
				<li class="save-side-menu-state"
					data-menu-parent="shipment-schedule" data-menu-child=""
					data-role="parent"><a
					href="${contextPath }/sales/view-shipment"> <i
						class="fa fa-ship"></i> <span>Shipment Schedule</span>
				</a></li>
			</sec:authorize>

			<%-- <li class="save-side-menu-state" data-menu-child=""
				data-role="parent"><a href="${contextPath }/sales/sales-home">
					<i class="fa fa-birthday-cake"></i> <span>Sales Home</span>
			</a></li> --%>
			<sec:authorize access="canAccess(120)">
				<li class="save-side-menu-state" data-menu-parent="stock-tracker"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/sales/custom/report"><i
						class="fa fa-file-text-o"></i> <span>Stock Organizer</span></a></li>
			</sec:authorize>
			<li class="treeview"><a href="#"> <i class="fa fa-clone"></i>
					<span>BL Management</span> <span class="pull-right-container">
						<i class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<li><a href="${contextPath }/accountsBL/page/sales"><i
							class="fa fa-circle-o"></i>BL Management</a></li>
					<li><a href="${contextPath }/accountsBL/cr-management/page/sales"><i
							class="fa fa-circle-o"></i>CR Management</a></li>
				</ul></li>
				
				<li><a href="${contextPath }/sales/stockInfo"><i
				class="fa fa-info"></i><span>Stock Info</span></a></li>
		</ul>

	</section>
	<!-- /.sidebar -->
</aside>
