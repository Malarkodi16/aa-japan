<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="row" id="stock-status-wrapper">
	<sec:authorize access="canAccess(62)">
		<div class="col-md-2">
			<a class="statusFilter"
				href="${contextPath}/accounts/invoice/approval/auction"
				data-custom-value="auction">
				<div class="small-box bg-red auction-nav-box blur" data-nav="0">
					<div class="inner">
						<h3 id="count-approval-auction">0</h3>
						<p>Purchased</p>
					</div>
					<div class="icon">
						<i class="fa fa-search"></i>
					</div>
				</div>
			</a>
		</div>
	</sec:authorize>
	<div class="col-md-2">
		<!-- small box -->
		<a class="statusFilter active-payment"
			href="${contextPath}/accounts/invoice/approval/transport"
			data-custom-value="transport">
			<div class="small-box bg-orange auction-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="count-approval-transport">0</h3>
					<p>Transport</p>
				</div>
				<div class="icon">
					<i class="fa fa-truck"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box shippingCotainer | shippingRoRo-->
		<a href="${contextPath}/accounts/invoice/approval/shipping/roro"
			data-custom-value="freightAndShipping">
			<div class="small-box bg-blue auction-nav-box blur" data-nav="2">
				<div class="inner">
					<h3 id="count-approval-freight">0</h3>
					<p>Freight & Shipping</p>
				</div>
				<div class="icon">
					<i class="fa fa-ship"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a class="statusFilter"
			href="${contextPath}/accounts/invoice/approval/genaralExpenses"
			data-custom-value="others">
			<div class="small-box bg-yellow auction-nav-box blur" data-nav="3">
				<div class="inner">
					<h3 id="count-approval-others">0</h3>
					<p>General Expenses</p>
				</div>
				<div class="icon">
					<i class="fa fa-tasks"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/invoice/approval/storageAndPhotos"
			data-custom-value="StorageAndPhotos">
			<div class="small-box bg-aqua auction-nav-box blur" data-nav="4">
				<div class="inner">
					<h3 id="count-approval-storage">0</h3>
					<p>Storage & Photos</p>
				</div>
				<div class="icon">
					<i class="fa fa-hdd-o"></i>
				</div>
			</div>
		</a>
	</div>
	<%-- <div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/invoice/approval/otherDirectExpense"
			data-custom-value="OtherDirectExpense">
			<div class="small-box bg-aqua auction-nav-box blur" data-nav="5">
				<div class="inner">
					<h3 id="count-otherDirectExpense">0</h3>
					<p>Other Direct Expense</p>
				</div>
				<div class="icon">
					<i class="fa fa-hdd-o"></i>
				</div>
			</div>
		</a>
	</div> --%>
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/payment/inspection/invoice/approve"
			data-custom-value="Inspection">
			<div class="small-box bg-green auction-nav-box blur" data-nav="5">
				<div class="inner">
					<h3 id="count-approval-inspection">0</h3>
					<p>Inspection</p>
				</div>
				<div class="icon">
					<i class="fa fa-search"></i>
				</div>
			</div>
		</a>
	</div>
	<%-- <div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/payment/advance"
			data-custom-value="AdvanceAndPrePayment">
			<div class="small-box bg-green auction-nav-box blur" data-nav="5">
				<div class="inner">
					<h3 id="count-prepayment">0</h3>
					<p>Advance & Prepayments</p>
				</div>
				<div class="icon">
					<i class="ion ion-stats-bars"></i>
				</div>
			</div>
		</a>
	</div> --%>
</div>