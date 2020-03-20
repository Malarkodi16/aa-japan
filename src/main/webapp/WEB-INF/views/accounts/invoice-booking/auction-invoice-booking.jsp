<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Purchased</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Booking</span></li>
		<li class="active">Purchased</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success alert-dismissible hidden"
		id="alert-block">
		<button type="button" class="close" id="close-alert-block">×</button>
		<h4>
			<span></span>
		</h4>

	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<input type="hidden" name="screenNameFlag" value="${screenNameFlag}" />
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
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<form action="#" id="form-purchased">
				<div class="container-fluid ">
					<div class="row form-group">
						<div class="col-md-12">
						<div class="pull-right">
							<button type="button" class="btn btn-primary pull-left"
								id="save-purchased-costs">Save</button>
							<button type="button" class="btn btn-primary ml-5"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-confirm" id="save-purchased">Confirm
								Purchased</button>
							<button type="button" class="btn btn-primary ml-5"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-add-payment">Add Payment</button>
							<button type="button" class="btn  btn-primary ml-5" id="addNew"
								data-toggle="modal" data-target="#add-new-category">New
								Payment Category</button>
						</div>
						</div>
					</div>
					<div class="table-responsive">
						<table id="table-purchased"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1">Purchase</br>Date
									</th>
									<th data-index="2">Chassis No.</th>
									<th data-index="3">Model</th>
									<th data-index="4">Shuppin</br>No.
									</th>
									<th data-index="5">Pos No.</th>
									<th data-index="6">Supplier</th>
									<th data-index="7">Auction</br>House
									</th>
									<th data-index="8">Type</th>
									<th data-index="9">Purchase Cost</th>
									<th data-index="10">Commission</th>
									<th data-index="11">Car Tax</th>
									<th data-index="12">Recycle</th>
									<th data-index="13">Others</th>
									<th data-index="14">Total</th>
									<th data-index="15">Purchase Type</th>
									<th data-index="16">Supplier</th>
									<th data-index="17">Auction House</th>
									<th data-index="18">Transport Status</th>
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
									<th>Recycle Amt</br>Total
									</th>
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
									<th class="dt-right"><span
										class="autonumber pagetotal purchaseTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal commisionTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal roadTaxTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal recycleTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal othersTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=taxtotal>
									<th colspan="9" style="text-align: right"></th>
									<th class="dt-right"><span
										class="autonumber pagetotal purchaseTaxTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal commisionTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th class="dt-right"><span
										class="autonumber pagetotal otherChargesTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr id="grandTotal">
									<th colspan="12" style="text-align: right">Total:</th>
									<th class="dt-right" colspan="2"><span
										class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
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
								<div class="col-md-4 ">
									<div class="form-group">
										<label>Auction Ref.No</label>
										<div class="element-wrapper">
											<input type="text" id="auctionRefNo" name="auctionRefNo"
												class="form-control" placeholder="AUCTION REF.NO"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>

							</div>
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label>Remark</label>
										<div class="element-wrapper">
											<input name="remarks" id="remarks" class="form-control"
												placeholder="Enter.." />
										</div>
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
	<div class="modal fade" id="add-new-category">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="add-new-category-form">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Add New Category</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="col-md-6">
								<label class="required">Sub Category</label> <input type="text"
									name="categoryDesc" id="categoryDesc"
									class="form-control required" /> <span class="help-block"></span>
							</div>
							<div class="col-md-6">
								<label class="required">Account Type</label> <select
									name="accountType" id="accountType"
									class="form-control required select2-tag"
									data-placeholder="Account Type">
									<option></option>
								</select> <span class="help-block"></span>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="form-control">
										<input type="checkbox" value="1" name="stockView"
											id="stockView" autocomplete="off"><label class="ml-5">Stock
											View</label>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="form-control">
										<input type="checkbox" value="1" name="claimed" id="claimed"
											autocomplete="off"><label class="ml-5">Claimed</label>
									</div>
								</div>
							</div>
						</div>
						<!-- /.form:form -->
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-create-category"
								type="button">
								<i class="fa fa-fw fa-save"></i>Create
							</button>
							<button class="btn btn-primary" id="btn-close" type="button"
								data-dismiss="modal">
								<i class="fa fa-fw fa-close"></i>Close
							</button>
						</div>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
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
							<input type="hidden" name="claimedStatus" value="" />
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Invoice Item Type</label>
										<div class="element-wrapper">
											<select class="form-control select2" id="invoiceItemType"
												name="invoiceItemType" data-placeholder="Select Type"
												style="width: 100%;">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
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
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-1">
									<div class="form-group">
										<label>TaxInc</label>
										<div class="element-wrapper">
											<input type="checkbox" class="taxInclusive"
												name="taxInclusive" />
										</div>

									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="tax">Tax</label>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber taxPercent"
												name="taxPercent" value="0" data-v-max="100" data-v-min="0"
												data-a-sign=" %" data-p-sign="s" data-m-dec="0">
										</div>

									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="taxAmount">Tax Amount</label>
										<div class="element-wrapper">
											<input type="hidden" class="form-control hiddenTaxAmount"
												name="hiddenTaxAmount" data-m-dec="0"> <input
												id="taxAmount" name="taxAmount" type="text"
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="totalAmount">Total</label>
										<div class="element-wrapper">
											<input id="totalAmount" name="totalAmount" type="text"
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
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

								<div class="col-md-3 hidden" id="reauctionStockWrapper">
									<div class="form-group">
										<label class="required" for="reauctionStock">Stock</label>
										<div class="element-wrapper">
											<select name="reauctionStock" id="reauctionStock"
												class="form-control reauctionStock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3 hidden" id="rikusoStockWrapper">
									<div class="form-group">
										<label class="required" for="transportStock">Stock</label>
										<div class="element-wrapper">
											<select name="transportStock" id="transportStock"
												class="form-control transportStock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3 hidden" id="penaltyStockWrapper">
									<div class="form-group">
										<label class="required" for="penaltyStock">Stock</label>
										<div class="element-wrapper">
											<select name="penaltyStock" id="penaltyStock"
												class="form-control penaltyStock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3 hidden" id="stockViewWrapper">
									<div class="form-group">
										<label class="required" for="stockVisible">Stock</label>
										<div class="element-wrapper">
											<select name="stockVisible" id="stockVisible"
												class="form-control stockVisible"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3 hidden" id="recycleCarTaxPaidStockWrapper">
									<div class="form-group">
										<label for="recycleCarTaxPaidStock">Stock</label>
										<div class="element-wrapper">
											<select name="recycleCarTaxPaidStock"
												id="recycleCarTaxPaidStock"
												class="form-control recycleCarTaxPaidStock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="row form-group hidden" id="takeOutStockWrapper">
									<div class="col-md-4 form-group">
										<label class="required" for="takeOutStock">Stock</label>
										<div class="element-wrapper">
											<select name="takeOutStock" id="takeOutStock"
												class="form-control" style="width: 100%;"
												multiple="multiple"
												data-placeholder="Search by Stock No. or Chassis No."></select>
										</div>
										<span class="help-block"></span>
									</div>
									<div class="col-md-2 form-group">
										<div class="form-group">
											<label>&nbsp;</label>
											<div class="form-control">
												<input type="checkbox" value="1" name="checkbox"
													id="checkbox" autocomplete="off"><label
													class="ml-5">Select all</label>
											</div>
										</div>
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
