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
	<h1>Tax</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payable &amp; Receivable Reports</span></li>
		<li><span>Claim</span></li>
		<li class="active">Tax</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/claim/claimstatus.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
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
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword" autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<!-- table start -->
					<div style="text-align: center;">
						<label> <input name="radioReceivedFilter" type="radio"
							class="minimal radioReceivedFilter" value="0" checked="checked">
							Receivable
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal radioReceivedFilter" value="2">
							Received
						</label>
					</div>

				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-tax"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Stock No.</th>
								<th>Chassis No.</th>
								<th>Shuppin No.</th>
								<th>Purchase Cost</th>
								<th>Commission Cost</th>
								<th>Purchase Cost Tax</th>
								<th>Commission Cost Tax</th>
								<th>Purchase Status</th>
								<th>Commission Status</th>
								<th>Claim</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="4" style="text-align: right"></th>
								<th>Purchase Cost Total</th>
								<th>Commission Cost Total</th>
								<th>Purchase Cost Tax Total</th>
								<th>Commission Cost Tax Total</th>
								<th></th>
							</tr>
							<tr class=sum>
								<th colspan="4" style="text-align: right"></th>
								<th class="dt-right"><span
									class="autonumber pagetotal purchaseCostTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal commissionTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal purchaseCostTaxTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal commissionTaxTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>
