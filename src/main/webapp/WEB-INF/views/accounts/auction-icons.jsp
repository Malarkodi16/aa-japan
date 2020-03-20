<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="stock-status-wrapper">

	<div class="col-md-2">
		<a class="statusFilter" href="${contextPath}/accounts/auction-payment"
			data-custom-value="auction">
			<div class="small-box bg-red auction-nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="count-auction">0</h3>
					<p>Auction</p>
				</div>
				<div class="icon">
					<i class="fa fa-search"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a class="statusFilter active-payment"
			href="${contextPath}/accounts/payment/transport"
			data-custom-value="transport">
			<div class="small-box bg-orange auction-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="count-transport">0</h3>
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
		<a href="${contextPath}/freight/shippingRoRo"
			data-custom-value="freightAndShipping">
			<div class="small-box bg-blue auction-nav-box blur" data-nav="2">
				<div class="inner">
					<h3 id="count-freight">0</h3>
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
		<a class="statusFilter" href="${contextPath}/accounts/payment/others"
			data-custom-value="others">
			<div class="small-box bg-yellow auction-nav-box blur" data-nav="3">
				<div class="inner">
					<h3 id="count-others">0</h3>
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
		<a href="${contextPath}/accounts/payment/storage-and-photos/list"
			data-custom-value="StorageAndPhotos">
			<div class="small-box bg-aqua auction-nav-box blur" data-nav="4">
				<div class="inner">
					<h3 id="count-storage">0</h3>
					<p>Storage & Photos</p>
				</div>
				<div class="icon">
					<i class="fa fa-hdd-o"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
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
	</div>
</div>