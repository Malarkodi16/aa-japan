<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu" data-widget="tree">
			<sec:authorize access="canAccess(64)">
				<li class="save-side-menu-state"
					data-menu-parent="shipping-dashboard" data-menu-child=""
					data-role="parent"><a href="${contextPath }/stock/purchased"><i
						class="fa fa-dashboard"></i> <span>Dashboard</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(71)">
				<li class="treeview" data-menu-parent="stock-management"
					data-role="parent"><a href="#"> <i class="fa  fa-cube"></i>
						<span>Stock Management</span> <span class="pull-right-container">
							<i class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(72)">
							<li class="save-side-menu-state"
								data-menu-parent="stock-management"
								data-menu-child="stock-management-1" data-role="child"><a
								href="${contextPath }/stock/stock-entry"><i
									class="fa fa-circle-o"></i>Stock Entry</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(73)">
							<li class="save-side-menu-state"
								data-menu-parent="stock-management"
								data-menu-child="stock-management-2" data-role="child"><a
								href="${contextPath }/stock/stock-search"><i
									class="fa fa-circle-o"></i>Stock Search</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(74)">
							<li class="save-side-menu-state"
								data-menu-parent="stock-management"
								data-menu-child="stock-management-3" data-role="child"><a
								href="${contextPath }/stock/re-auction/list"><i
									class="fa fa-circle-o"></i>ReAuction/Cancelled</a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="canAccess(77)">
				<li class="treeview" data-menu-parent="shipment-management"
					data-role="parent"><a href="#"> <i class="fa fa-truck"></i>
						<span>Shipment Management</span> <span
						class="pull-right-container"> <i
							class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(78)">
							<li class="save-side-menu-state"
								data-menu-parent="shipment-management"
								data-menu-child="shipment-management-1" data-role="child"><a
								href="${contextPath }/shipping/shipmentschedule"><i
									class="fa fa-circle-o"></i>Create Schedule</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(79)">
							<li class="save-side-menu-state"
								data-menu-parent="shipment-management"
								data-menu-child="shipment-management-2" data-role="child"><a
								href="${contextPath }/shipping/schedule/list"><i
									class="fa fa-circle-o"></i>View Schedule</a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="canAccess(80)">
				<li class="treeview" data-menu-parent="administration"
					data-role="parent"><a href="#"> <i class="fa fa-users"></i>
						<span>Administration</span> <span class="pull-right-container">
							<i class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
					<ul class="treeview-menu">
						<sec:authorize access="canAccess(81)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-2" data-role="child"><a
								href="${contextPath}/a/supplier/list"><i
									class="fa fa-circle-o"></i>Supplier Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(82)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-3" data-role="child"><a
								href="${contextPath}/master/list"><i class="fa fa-circle-o"></i>Maker-Model
									Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(83)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-4" data-role="child"><a
								href="${contextPath }/transport/transporter/fee/list"><i
									class="fa fa-circle-o"></i>Transport Fee Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(84)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-5" data-role="child"><a
								href="${contextPath }/master/location/list"><i
									class="fa fa-circle-o"></i>Location Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(85)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-6" data-role="child"><a
								href="${contextPath }/master/port/list"><i
									class="fa fa-circle-o"></i>Port Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(86)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-7" data-role="child"><a
								href="${contextPath }/master/country/port/list"><i
									class="fa fa-circle-o"></i>Country Port Map</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(87)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-8" data-role="child"><a
								href="${contextPath }/master/forwarder/list"><i
									class="fa fa-circle-o"></i>Forwarder Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(88)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-9" data-role="child"><a
								href="${contextPath }/master/trn/list"><i
									class="fa fa-circle-o"></i>Transporter Management</a></li>
						</sec:authorize>
						<sec:authorize access="canAccess(89)">
							<li class="save-side-menu-state"
								data-menu-parent="administration"
								data-menu-child="administration-10" data-role="child"><a
								href="${contextPath }/master/inspection/list"><i
									class="fa fa-circle-o"></i>Inspection Comp Management</a></li>
						</sec:authorize>
						<li class="save-side-menu-state" data-menu-parent="administration"
							data-menu-child="administration-11" data-role="child"><a
							href="${contextPath }/master/auctionGradeExterior/list"><i
								class="fa fa-circle-o"></i>Auction Grade Exterior Manage</a></li>

						<li class="save-side-menu-state" data-menu-parent="administration"
							data-menu-child="administration-12" data-role="child"><a
							href="${contextPath }/master/auctionGradeInterior/list"><i
								class="fa fa-circle-o"></i>Auction Grade Interior Manage</a></li>

						<li class="save-side-menu-state" data-menu-parent="administration"
							data-menu-child="administration-14" data-role="child"><a
							href="${contextPath }/master/shippingCompany/list"><i
								class="fa fa-circle-o"></i>Shipping Company Management</a></li>

						<li class="save-side-menu-state" data-menu-parent="administration"
							data-menu-child="administration-15" data-role="child"><a
							href="${contextPath }/master/ship/list"><i
								class="fa fa-circle-o"></i>Ship Management</a></li>
						<li class="save-side-menu-state" data-menu-parent="administration"
							data-menu-child="administration-16" data-role="child"><a
							href="${contextPath }/master/stockType/list"><i
								class="fa fa-circle-o"></i>Stock Model Type</a></li>
					</ul></li>
			</sec:authorize>
			<sec:authorize access="canAccess(90)">
				<li class="save-side-menu-state" data-menu-parent="document-status"
					data-menu-child="" data-role="parent"><a
					href="${contextPath}/shipping/bl/document-draft"> <i
						class="fa fa-file"></i> <span>Document Status</span> <span
						class="pull-right-container"> </span>
				</a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(93)">
				<li class="save-side-menu-state" data-menu-parent="last-lap"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/shipping/last-lap-vehicles"><i
						class="fa fa-car"></i> <span>Last Lap Vehicles</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(94)">
				<li class="save-side-menu-state" data-menu-parent="stock-tracker"
					data-menu-child="" data-role="parent"><a
					href="${contextPath }/shipping/custom/report"><i
						class="fa fa-file-text-o"></i> <span>Stock Organizer</span></a></li>
			</sec:authorize>
			<sec:authorize access="canAccess(95)">
				<li class="save-side-menu-state"
					data-menu-parent="document-tracking" data-menu-child=""
					data-role="parent"><a
					href="${contextPath }/shipping/document/not-received"><i
						class="fa fa-book"></i> <span>Document Tracking</span></a></li>
			</sec:authorize>
			<li><a href="${contextPath }/shipping/transport/approval"><i
					class="fa fa-circle-o"></i> <span>Transport Approval</span></a></li>
			<li><a href="${contextPath }/shipping/stockInfo"><i
					class="fa fa-info"></i><span>Stock Info</span></a></li>
		</ul>
	</section>
	<!-- /.sidebar -->
</aside>
