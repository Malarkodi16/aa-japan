<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Car Tax</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Claim</span></li>
		<li class="active">Car Tax</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/claim/claimstatus.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
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
									id="table-filter-carTax-claim"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="form-group">
					<div class="col-md-1 form-inline pull-left">
							<div class="form-group">
								<select id="table-filter-length" class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
						<div class="col-md-3 pull-left">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-3 pull-right">
							<button type="button" class="btn btn-primary claim-button"
								name="btn-claim">
								<i class="fa fa-fw fa-money"></i> Claim
							</button>
							<button type="button" class="btn btn-primary ml-5 claim-button"
								data-target="#modal-carTax-claim" data-toggle="modal"
								data-backdrop="static">
								<i class="fa fa-fw fa-floppy-o"></i> Update Claim Details
							</button>
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
					<table id="table-claim-carTax"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1">Stock No</th>
								<th data-index="2">Chassis No</th>
								<th data-index="3">Purchase Date</th>
								<th data-index="4">Amount</th>
								<th data-index="5">CarTax Received</th>
								<th data-index="6">Received Date</th>
								<th data-index="7">Status</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr class=sum>
								<th colspan="4" style="text-align: right">Total</th>
								<th class="dt-right"><span
									class="autonumber pagetotal totalAmount" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								 
								<th class="dt-right"><span
									class="autonumber pagetotal carTaxTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
									<th style="text-align: right"></th>
								 <th  style="text-align: right"></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
		<!-- modal -->
		<div class="modal fade" id="modal-carTax-claim">
			<div class="modal-dialog ">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title">Update Claim Details</h3>
					</div>
					<div class="modal-body">
					<form id="update-car-tax-form">
						<input type="hidden" id="rowData" data-json="" value="">

						<div class="row">

							<div class="col-md-4">
								<div class="form-group" id="date-form-group">
									<label>Received Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker"
											id="received-date" placeholder="dd-mm-yyyy">
									</div>
									<!-- /.input group -->
								</div>
							</div>

						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Remit Auction</label> <select id="remitAuction"
										name="remitAuction" class="form-control required select2 "
										placeholder="Select Remit Auction" style="width: 100%;">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group" id="date-form-group">
									<label>Remit Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker" id="remit-date"
											placeholder="dd-mm-yyyy" readonly="readonly">
									</div>
									<!-- /.input group -->
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="totalAmount">Total Amount</label> <input
										type="text" class="form-control autonumber" id="totalAmount"
										name="totalAmount" data-a-sign="¥ " data-m-dec="0"
										readonly="readonly">
								</div>
							</div>

						</div>
						<div id="append-row" class="hidden new-row"></div>
						<div class="row hidden" id="clone-row-hide">
							<input type="hidden" name="id">
							<div class="col-md-4">
								<div class="form-group">
									<label for="chassisNo">Chassis No</label> <input type="text"
										class="form-control" id="chassisNo" name="chassisNo" disabled>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="actualAmount">Actual Amount</label> <input
										type="text" class="form-control autonumber"
										id="actualAmount" name="actualAmount" data-a-sign="¥ "
										data-m-dec="0" disabled>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="amount">Received Amount</label> <input type="text"
										class="form-control autonumber" id="amount" name="amount"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>

						</div>
						<div class="summary-container">
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total Actual Amount</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ " class="total-actual pull-right"></span>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total Received Amount</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ "
										class="total-received pull-right"></span>
								</div>
							</div>

						</div>
					</form>
					</div>
					<div class="modal-footer">
						<div class="vehicle">
							<strong class="pull-left">No.Of.Vehicles :</strong> <strong><span
								class="no-of-vehicles pull-left"></span></strong>
						</div>
						<button id="save-carTax-modal" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>

			</div>
		</div>
	</div>

</section>