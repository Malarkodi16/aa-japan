<!-- # Copyright (c) 2018 - AAJ
# @Author: Rajlakshman(Nexware)
# @Date: 
# @Last Modified by: Rajlakshman(Nexware)
# @Last Modified time: 2018-11-15
-->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Radiation</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Claim</span></li>
		<li class="active">Radiation</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/claim/claimstatus.jsp" />
	<div class="box">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Purchase Type</label> <select id="purchaseType"
								name="supplierType" class="form-control" style="width: 100%;">
								<option value="">All</option>
								<option data-type="auction" value="auction">Auction</option>
								<option data-type="supplier" value="local">Local</option>
								<option data-type="supplier" value="overseas">Overseas</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								class="form-control select2" style="width: 100%;"
								disabled="true">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-control"
									style="width: 100%;" multiple="multiple">
									<option value=""></option>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedInfoPos">POS No.</label> <select
									id="purchasedInfoPos" class="form-control" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<em class="fa fa-calendar"></em>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-purchased-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
				</div>
				<div class="row">
					<div style="text-align: center;">
						<label> <input name="radioReceivedFilter" type="radio"
							class="minimal" value="0" checked="checked"> Receivable
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal" value="2"> Received
						</label>
					</div>

				</div>

				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-radiation"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Stock No</th>
								<th>Chassis No</th>
								<th>Shipping Company</th>
								<th>Forwarder</th>
								<th>Received date</th>
								<th>Radiation charges</th>
								<th>Received Amount</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>