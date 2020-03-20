<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Payment Tracking</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li class="active"><a>Tracking</a></li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<%-- 	<jsp:include page="/WEB-INF/views/accounts/auction-icons.jsp" /> --%>
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						
							<label>Invoice Type</label> <select id="invoiceTypeFilter"
								class="form-control select2" name="invoiceType"
								data-placeholder="Select Invoice Type" style="width: 100px">
								<option value=""></option>
								<option value="0">PURCHASE</option>
								<option value="1">TRANSPORT</option>
								<option value="2">STORAGE AND PHOTOS</option>
								<option value="3">FREIGHT AND SHIPPING</option>
								<option value="4">GENERAL EXPENSE</option>
								<option value="5">INSPECTION</option>
							</select>
						
					</div>
					<div class="col-md-2 hidden" id="supplierFilterDiv">
						<label>Remitter</label> <select name="supplier" id="supplier"
							class="form-control supplier select2"
							data-placeholder="Select Remitter">
							<option value=""></option>
						</select>
					</div>
					<div class="col-md-2 hidden" id="transporterFilterDiv">
						<label>Remitter</label> <select name="transporter"
							id="transporter" class="form-control transporter select2"
							data-placeholder="Select Remitter">
							<option value=""></option>
						</select>
					</div>
					<div class="col-md-2 hidden" id="forwarderFilterDiv">
						<div class="form-group">
							<label>Remitter</label><select id="forwarderFilter"
								name="forwarderFilter" class="form-control select2"
								data-placeholder="Select Remitter" style="width: 100%;">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2 hidden" id="genaralSupplierFilterDiv">
						<div class="form-group">
							<label>Remitter</label> <select id="genaralSupplierFilter"
								class="form-control pull-right"
								data-placeholder="Select Remitter">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2 hidden" id="inspectionCompanyFilterDiv">
						<div class="form-group">
							<label>Inspection</label> <select id="inspectionCompanyFilter"
								class="form-control pull-right"
								data-placeholder="Select Inspection Company">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2 hidden" id="auctionHouseDiv"><label>Auction House</label>
							<select name="auctionHouseId" id="auctionHouseId"
								class="form-control auctionHouseId select2"
								data-placeholder="Auction House">
								<option value=""></option>
							</select>
						</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Invoice Date</label>
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
					
					<div class="col-md-1">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="submit" class="btn btn-primary form-control"
								id="btn-search">Search</button>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
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
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-2 pull-right">
							<button class="btn btn-primary pull-right" type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
						</div>
						
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-auction-payment"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Invoice Date</th>
								<th data-index="1">Transaction No</th>
								<th data-index="2">Supplier Invoice No</th>
								<th data-index="3">Ref No</th>
								<th data-index="4">Remit To</th>
								<th data-index="5">Total Amount (¥)</th>
								<th data-index="6">Total Amount ($)</th>
								<th data-index="7">Paid Amount (¥)</th>
								<th data-index="8">Paid Amount ($)</th>
								<th data-index="9">Balance (¥)</th>
								<th data-index="10">Balance ($)</th>
								<th data-index="11">Approved Date</th>
								<th data-index="12">Approved By</th>
								<th data-index="13">Status</th>
								<th data-index="14">Action</th>
								<th data-index="15">Auction House</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr class=sum>
								<th colspan="5" style="text-align: right"></th>
								<th class="dt-right"><span
									class="autonumber pagetotal amountTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal amountTotalUsd" data-a-sign="$ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal paidAmount" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal paidAmountUsd" data-a-sign="$ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal balance" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal balanceUsd" data-a-sign="$ "
									data-m-dec="0">0</span></th>
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
		</div>
	</div>
	<!-- Purchase Invoice Table Format Details -->
	<div id="clone-container">
		<div id="payment-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th data-index="0" class="align-center ">#</th>
								<th data-index="1" class="align-center">Payment Voucher No.</th>
								<th data-index="2" class="align-center">Payment Date</th>
								<th data-index="3" class="align-center">Bank</th>
								<th data-index="4" class="align-center">Amount</th>
								<th data-index="5" class="align-center">Action</th>
								<!-- <th data-index="1" class="align-center">ChassisNo</th>
								<th data-index="1" class="align-center">Type</th>
								<th data-index="2" class="align-center">Purchase Cost</th>
								<th data-index="3" class="align-center">Purchase Cost Tax</th>
								<th data-index="4" class="align-center">Commission</th>
								<th data-index="5" class="align-center">Commission Tax</th>
								<th data-index="6" class="align-center">Road Tax</th>
								<th data-index="7" class="align-center">Recycle</th>
								<th data-index="8" class="align-center">Others</th> -->
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="paymentVoucherNo"><span></span></td>
								<td class="date"><span></span></td>
								<td class="bank"><span></span></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="action"></td>


								<!-- <td class="align-center chassisNo"></td>
								<td class="align-center type"></td>
								<td class="align-center purchaseCost"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center purchaseCostTax"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center commision"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center commisionTax"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center roadTax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center recycle"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center otherCharges"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td> -->
							</tr>
						</tbody>
						<!-- <tfoot>
							<tr>
								<td colspan="3" class="align-center"><strong>Total</strong></td>
								<td class="align-center purchaseCost"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center purchaseCostTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center commision"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center commisionTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center roadTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center recycle"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center otherCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						</tfoot> -->
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Transport Invoice Table Format Details -->
	<div id="transport-clone-container">
		<div id="payment-transport-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive transportorder-item-container">
					<input type="hidden" name="transportinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Chassis No</th>
								<th data-index="2" class="align-center">Maker</th>
								<th data-index="3" class="align-center">Model</th>
								<th data-index="4" class="align-center">Amount</th>
								<th data-index="5" class="align-center">Tax</th>
								<th data-index="6" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right taxamount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center action"></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4"><span class="pull-right"><strong>Total</strong></span></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right taxamount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- t_inv  Table Format Details -->
	<!-- <div id="tinv-clone-container">
		<div id="payment-tinv-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive tinvorder-item-container">
					<input type="hidden" name="tinvinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Chassis No.</th>
								<th data-index="2" class="align-center">Category</th>
								<th data-index="3" class="align-center">Remarks</th>
								<th data-index="4" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center category"></td>
								<td class="align-center remarks"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="4"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div> -->

	<!-- 	<div> -->
	<div id="others-clone-container">
		<div id="others-invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="0" class="align-center">Category</th>
								<th data-index="1" class="align-center">Description</th>
								<th data-index="2" class="align-center">Amount(¥)</th>
								<th data-index="3" class="align-center">TaxAmount</th>
								<th data-index="4" class="align-center">TotalAmount</th>
								<th data-index="5" class="align-center">Source Currency</th>
								<th data-index="6" class="align-center">Amount</th>
								<th data-index="7" class="align-center">Exchange rate</th>
								<th data-index="8" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center category"></td>
								<td class="align-center description"></td>
								<td class="dt-right amountInYen"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right taxAmount"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right totalAmount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center currency"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right exchangeRate"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center action"></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="3"><strong class="pull-right">Total</strong></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right taxTotal"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right taxIncluded"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tfoot>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 	</div> -->

	<!-- Forwarder Invoice  Table Format Details -->
	<div id="forwarder-clone-container">
		<div id="payment-forwarder-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive forwarder-item-container">
					<input type="hidden" name="forwarderinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="0" class="align-center">Chassis No</th>
								<th data-index="1" class="align-center">Storage</th>
								<th data-index="2" class="align-center">Shipping</th>
								<th data-index="3" class="align-center">Photo</th>
								<th data-index="4" class="align-center">Bl AmendCombine</th>
								<th data-index="5" class="align-center">Radiation</th>
								<th data-index="6" class="align-center">Repair</th>
								<th data-index="7" class="align-center">Yard Handling
									Charges (YHC)</th>
								<th data-index="8" class="align-center">Inspection</th>
								<th data-index="9" class="align-center">Transport</th>
								<th data-index="10" class="align-center">Freight</th>
								<th data-index="11" class="align-center">Remarks</th>
								<th data-index="12" class="align-center">Amount</th>
								<th data-index="13" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="dt-right storage"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right shipping"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right photoCharges"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right blAmendCombineCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right radiationCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right repairCharges"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right yardHandlingCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right inspectionCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right transportCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right freightCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center remarks"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center action"></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="13"><strong class="pull-right">Total</strong></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td></td>
							</tr>
						</tfoot>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Forwarder Invoice  Table Format Details -->
	<div id="freightshipping-clone-container">
		<div id="payment-freightshipping-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive freightshipping-item-container">
					<input type="hidden" name="freightshippinginvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Freight Charge</th>
								<th data-index="3" class="align-center">Shipping Charge</th>
								<th data-index="4" class="align-center">Inspection Charge</th>
								<th data-index="5" class="align-center">Radiation Charge</th>
								<th data-index="6" class="align-center">Other Charges</th>
								<th data-index="7" class="align-center">Amount</th>
								<th data-index="8" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="dt-right freightCharge"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right shippingCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right inspectionCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right radiationCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right otherCharges"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center action"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model -->
	<div class="modal fade" id="modal-invoice-preview">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<button type="button" id="btn-print"
						class="btn btn-default pull-right">Print</button>
					<h4 class="modal-title">Invoice</h4>
				</div>
				<div class="modal-body">
					<embed
						src="${contextPath}/resources/assets/images/no-image-icon.png"
						width="100%" style="height: 500px" class="image_preview">
				</div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- modal invoice payments -->
	<div class="modal fade" id="modal-invoice-payments">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Payment Details</h4>
				</div>
				<div class="modal-body" id="invoicePaymentDetails">
					<input type="hidden" name="invoiceNo">
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-detail-invoice"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Date</th>
										<th data-index="1">Bank</th>
										<th data-index="2">Amount</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>
