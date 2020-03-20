	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Purchased Vehicles</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li class="active">Purchased Vehicles</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<input type="hidden" name="screenNameFlag" value="${screenNameFlag}" />
				<div class="row form-group">
					<div class="col-md-3">
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
									style="width: 100%;" multiple="multiple" data-placeholder="All">
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
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<form action="#" id="form-purchased">
				<div class="container-fluid ">
					<div class="row">
						<div class="col-md-6">
							<button type="button" class="btn btn-primary pull-left"
								id="save-purchased-costs">Save</button>
							<button type="button" class="btn btn-primary ml-5"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-confirm" id="save-purchased">Confirm
								Purchased</button>
							<button type="button" class="btn btn-primary ml-5"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-add-payment">Add Payment</button>
						</div>
						<div class="col-md-2 form-inline pull-right">
							<div class="pull-right">
								<select id="table-filter-length" class="form-control input-sm">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
					</div>
					<div class="table-responsive">
						<table id="table-purchased"
							class="table table-bordered table-striped"
							style="width: 200%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1">Purchase Date</th>
									<th data-index="2">Chassis No.</th>
									<th data-index="3">Model</th>
									<th data-index="4">Shuppin No.</th>
									<th data-index="5">Pos No.</th>
									<th data-index="6">Auction/Supplier</th>
									<th data-index="7">Auction House</th>
									<th data-index="8">Type</th>
									<th data-index="9">Purchase Cost</th>
									<th data-index="10">Commission</th>
									<th data-index="11">Road Tax</th>
									<th data-index="12">Recycle</th>
									<th data-index="13">Others</th>
									<th data-index="14">Total</th>
									<th data-index="15">Purchase Type</th>
									<th data-index="16">Supplier</th>
									<th data-index="17">Auction House</th>
									<th data-index="18">Transportation Status</th>
									<th data-index="19">Action</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="9" style="text-align: right"></th>
									<th>Purchase Cost Total</th>
									<th>Commission Total</th>
									<th>Road Tax Total</th>
									<th>Recycle Amount Total</th>
									<th>Others Total</th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=sum>
									<th colspan="9" style="text-align: right"></th>
									<th><span class="autonumber pagetotal purchaseTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th><span class="autonumber pagetotal commisionTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th><span class="autonumber pagetotal roadTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th><span class="autonumber pagetotal recycleTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th><span class="autonumber pagetotal othersTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=taxtotal>
									<th colspan="9" style="text-align: right"></th>
									<th><span class="autonumber pagetotal purchaseTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th><span class="autonumber pagetotal commisionTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr id="grandTotal">
									<th colspan="12" style="text-align: right">Total:</th>
									<th colspan="2"><span class="autonumber pagetotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- 	.modal -->
	<div class="modal fade" id="modal-confirm">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Invoice Details</h3>
				</div>
				<div class="modal-body">
					<form id="purchase-invoice-save">
						<div class="container-fluid">
							<div class="row">
								<!-- <div class="col-md-6">
									<div class="form-group">
										<label class="required">Invoice No.</label>
										<div class="element-wrapper">
											<input type="text" id="input-invoice-no" name="invoiceNo"
												class="form-control required" placeholder="Invoice No"
												readonly="readonly">
										</div>
										<span class="help-block"></span>
									</div>
								</div> -->
								<input type="hidden" name="subTotalValue" value="">
								<div class="col-md-4 ">
									<div class="form-group">
										<label class="required">Due Date</label>
										<div class="element-wrapper">
											<input type="text" id="input-invoice-due-date" name="dueDate"
												class="form-control required datepicker"
												placeholder="Due Date" readonly="readonly">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4 ">
									<div class="form-group">
										<label>Invoice Total</label>
										<div class="element-wrapper">
											<input type="text" id="invoiceTotal" name="invoiceTotal"
												class="form-control autonumber" placeholder="INVOICE TOTAL"
												data-a-sign="¥ " data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-invoice" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 	/.modal -->
	<!-- 	.modal -->
	<div class="modal fade" id="modal-add-payment">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Payment</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formAddPayment">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Invoice Item Type</label>
										<div class="element-wrapper">
											<select class="form-control" id="invoiceItemType"
												name="invoiceItemType" data-placeholder="Select Type"
												style="width: 100%;">
												<option value=""></option>
												<option value="1">Penalty Charges</option>
												<option value="2">Document Penalty</option>
												<option value="3">Late Penalty</option>
												<option value="4">Road Tax Claimed</option>
												<option value="5">Cancellation Charge</option>
												<option value="6">Cash Back</option>
												<option value="7">Rikuso Payment</option>
												<option value="8">Membership</option>
											</select> <span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Invoice Date</label>
										<div class="element-wrapper">
											<input type="text" id="invoiceDate" name="invoiceDate"
												class="form-control datepicker required"
												placeholder="DD-MM-YYYY" readonly="readonly">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<div class="form-group">
										<label class="required">Supplier/Auction</label>
										<div class="element-wrapper">
											<select id="purchasedSupplier" name="purchasedSupplier"
												class="form-control select2" style="width: 100%;"
												data-placeholder="Select Supplier">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<div class="form-group">
										<label class="required">Auction House</label>
										<div class="element-wrapper">
											<select id="purchasedAuctionHouse"
												name="purchasedAuctionHouse" class="form-control select2"
												style="width: 100%;" data-placeholder="Select Auction House">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required" for="amount">Amount</label>
										<div class="element-wrapper">
											<input id="amount" name="amount" type="text"
												class="form-control autonumber" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 hidden" id="cancelledStockWrapper">
									<div class="form-group">
										<label class="required" for="cancelledStock">Stock</label>
										<div class="element-wrapper">
											<select name="cancelledStock" id="cancelledStock"
												class="form-control cancelledStock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="add-payment" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Stock Modal -->
	<div class="modal fade" id="modal-stock-details">
		<!-- /.modal-dialog -->
		<div class="modal-dialog modal-lg"
			style="min-width: 100%; margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Details</h4>
				</div>
				<div class="modal-body " id="modal-stock-details-body"></div>
			</div>
			<!-- /.modal-content -->
		</div>
	</div>
	<div id="cloneable-items">
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>
</section>
