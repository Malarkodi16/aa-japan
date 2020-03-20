<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="stock-status-wrapper">
	<div class="col-md-3 col-xs-6">
		<a class="statusFilter" href="${contextPath}/accounts/invoice/approval/auction"
			data-custom-value="auction">
			<div class="small-box auction-nav-box bg-red" data-nav="0">
				<div class="inner">
					<h3 id="total-auction">
						<span class="autoNumeric" data-a-sign="¥ " data-m-dec="0">0</span>
					</h3>
					<p>Auction</p>
				</div>
				<div class="icon">
					<i class="fa fa-search"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2 col-xs-6">
		<a class="statusFilter active-payment"
			href="${contextPath}/accounts/invoice/approval/transport"
			data-custom-value="transport">
			<div class="small-box auction-nav-box bg-orange" data-nav="1">
				<div class="inner">
					<h3 id="total-transport">
						<span class="autoNumeric" data-a-sign="¥ " data-m-dec="0">0</span>
					</h3>
					<p>Transport</p>
				</div>
				<div class="icon">
					<i class="fa fa-truck"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-3 col-xs-6">
		<a href="${contextPath}/accounts/invoice/approval/shipping/roro"
			data-custom-value="freightAndShipping">
			<div class="small-box auction-nav-box bg-blue" data-nav="2">
				<div class="inner">
					<h3 id="total-freight-shipping">
						<span class="autoNumeric" data-a-sign="¥ " data-m-dec="0">0</span>
					</h3>
					<p>Freight &amp; Shipping</p>
				</div>
				<div class="icon">
					<i class="ion ion-android-boat"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2 col-xs-6">
		<a class="statusFilter" href="${contextPath}/accounts/invoice/approval/genaralExpenses"
			data-custom-value="others">
			<div class="small-box auction-nav-box bg-yellow" data-nav="3">
				<div class="inner">
					<h3 id="total-general-expense">
						<span class="autoNumeric" data-a-sign="¥ " data-m-dec="0">0</span>
					</h3>
					<p>General Expense</p>
				</div>
				<div class="icon">
					<i class="fa fa-tasks"></i>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2 col-xs-6">
		<a href="${contextPath}/accounts/invoice/approval/storageAndPhotos"
			data-custom-value="StorageAndPhotos">
			<div class="small-box auction-nav-box bg-aqua" data-nav="4">
				<div class="inner">
					<h3 id="total-storage">
						<span class="autoNumeric" data-a-sign="¥ " data-m-dec="0">0</span>
					</h3>
					<p>Storage &amp; Photos</p>
				</div>
				<div class="icon">
					<i class="fa fa-hdd-o"></i>
				</div>
			</div>
		</a>
	</div>
</div>