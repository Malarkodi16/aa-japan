<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Payment Processing</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li class="active">Payment Processing</li>
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
						<div id="purchasedSupplierWrapper" class="supplirFilter">
							<label>Supplier</label> <select name="supplier" id="supplier"
								class="form-control supplier select2"
								data-placeholder="Select Supplier">
								<option value=""></option>
							</select>
						</div>
						<div id="transportSupplierWrapper" class="supplirFilter"
							style="display: none;">
							<label>Supplier</label> <select name="transportSupplier"
								id="transportSupplier" class="form-control select2"
								data-placeholder="Select Supplier">
								<option value=""></option>
							</select>
						</div>
						<div id="forwarderSupplierWrapper" class="supplirFilter"
							style="display: none;">
							<label>Supplier</label> <select name="forwarderSupplier"
								id="forwarderSupplier" class="form-control select2"
								data-placeholder="Select Supplier">
								<option value=""></option>
							</select>
						</div>
						<div id="genaralSupplierWrapper" class="supplirFilter"
							style="display: none;">
							<label>Supplier</label> <select name="genaralSupplier"
								id="genaralSupplier" class="form-control select2"
								data-placeholder="Select Supplier">
								<option value=""></option>
							</select>
						</div>
						<div id="inspectionCompanyWrapper" class="supplirFilter"
							style="display: none;">
							<label>Inspection Company</label> <select
								name="inspectionCompany" id="inspectionCompany"
								class="form-control select2"
								data-placeholder="Select Inspection Company">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<label>Due Date</label> <input
							class="form-control dueDateDatepicker" name="dueDate"
							placeholder="  dd-mm-yyyy" readonly />
					</div>
					<div class="col-md-2">
						<label>Invoice Date</label> <input
							class="form-control invoiceDateDatepicker" name="invoiceDate"
							placeholder="  dd-mm-yyyy" readonly />
					</div>
				</div>
				<div class="row form-group">

					<div class="col-md-3">
						<select id="invoiceTypeFilter" class="form-control select2"
							name="invoiceType" data-placeholder="Select Invoice Type">
							<option value=""></option>
							<option value="0" selected="selected">Purchase</option>
							<option value="1">Transport</option>
							<option value="2">Storage & Photos</option>
							<option value="3">Freight Shipping</option>
							<option value="4">General Expense</option>
							<option value="5">Inspection</option>
						</select><span class="help-block"></span>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="table-responsive">
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

						<div class="col-md-3 pull-right">
							<button class="btn btn-primary pull-right" type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
							<button type="button" class="btn btn-primary" id="approvePayment"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-approve-payment">
								<i class="fa fa-fw fa-check"></i>Complete Payment
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
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1">Invoice Date</th>
								<th data-index="2">Supplier Invoice No</th>
								<th data-index="3">Invoice No</th>
								<th data-index="4">Auction Ref No</th>
								<th data-index="5">Remit To</th>
								<th data-index="6">Auction House</th>
								<th data-index="7">Due Date</th>
								<th data-index="8">Total Amount</th>
								<th data-index="9">Paid Amount</th>
								<th data-index="10">Balance</th>
								<th data-index="11">Total Amount ($)</th>
								<th data-index="12">Balance($)</th>
								<th data-index="13">Auction House</th>
								<th data-index="14">Remarks</th>
								<th data-index="15">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr id="grandTotal">
								<th colspan="8" style="text-align: right">Total:</th>
								<th class="dt-right"><span class="autonumber pagetotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotalpaid"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotalbalance" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right hidden dollarValue"><span
									class="autonumber totalDollarAmount" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right hidden dollarValue"><span
									class="autonumber totalDollarBalance" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
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
		<div id="invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive roro-item-container-details">
					<input type="hidden" name="invoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Quantity</th>
								<th data-index="2" class="align-center">Description</th>
								<th data-index="3" class="align-center">USD</th>
								<th data-index="4" class="align-center">ZAR</th>
								<th data-index="5" class="align-center">Unit Price</th>
								<th data-index="5" class="align-center">Amount</th>

							</tr>
						</thead>
						<tbody>

							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center quantity"></td>
								<td class="align-center description"></td>
								<td class="dt-right usd"><span class="autonumber"
									data-a-sign="$ " data-m-dec="0"></span></td>
								<td class="dt-right zar"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right unitPrice"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div id="payment-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">ChassisNo</th>
								<th data-index="2" class="align-center">LotNo</th>
								<th data-index="3" class="align-center">Type</th>
								<th data-index="4" class="align-center">Purchase Cost</th>
								<th data-index="5" class="align-center">Purchase Cost Tax</th>
								<th data-index="6" class="align-center">Commission</th>
								<th data-index="7" class="align-center">Commission Tax</th>
								<th data-index="8" class="align-center">Road Tax</th>
								<th data-index="9" class="align-center">Recycle</th>
								<th data-index="10" class="align-center">Others</th>
								<th data-index="11" class="align-center">Others Tax</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center lotNo"></td>
								<td class="align-center type"></td>
								<td class="dt-right purchaseCost"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right purchaseCostTax"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right commision"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right commisionTax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right roadTax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right recycle"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right otherCharges"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right otherChargesTax"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4" class="align-center"><strong>Total</strong></td>
								<td class="dt-right purchaseCost"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right purchaseCostTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right commision"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right commisionTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right roadTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right recycle"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right otherCharges"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right otherChargesTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
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
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4"><span class="pull-right"><strong>Total</strong></span></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right taxamount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>


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
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center category"></td>
								<td class="align-center description"></td>
								<td class="align-center amountInYen"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center taxAmount"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center totalAmount"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center currency"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center exchangeRate"><span
									class="autonumber" data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="3"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center taxTotal"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center taxIncluded"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
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


	<!-- t_inv  Table Format Details -->
	<div id="tinv-clone-container">
		<div id="payment-tinv-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive tinvorder-item-container">
					<input type="hidden" name="tinvinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Category</th>
								<th data-index="2" class="align-center">Description</th>
								<th data-index="3" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center category"></td>
								<td class="align-center description"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="3"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- Forwarder Invoice  Table Format Details -->
	<div id="forwarder-clone-container">
		<div id="payment-forwarder-approve-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive forwarder-item-container">
					<input type="hidden" name="forwarderinvoiceNo" value="" />
					<!-- <table class="table table-bordered" style="overflow-x: auto;">
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
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center storage"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center shipping"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center photoCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center blAmendCombineCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center radiationCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center repairCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center yardHandlingCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center inspectionCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center transportCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center freightCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center remarks"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="13"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
						</tbody>
					</table> -->
				</div>
				<div class="table-clone hidden" id="table-cloning-element">
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr class="header">
								<th class="align-center ">#</th>
								<th data-index="0" class="align-center">Chassis No</th>

							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
							</tr>
							<tfoot>
							<tr>
								<td class="footerColumn"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
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
														<th data-index="2" class="align-center">Chassis No</th>
														<th data-index="3" class="align-center">Freight (¥)</th>
														<th data-index="4" class="align-center">Freight ($)</th>
														<th data-index="5" class="align-center">Shipping</th>
														<th data-index="5" class="align-center">Inspection</th>
														<th data-index="5" class="align-center">Radiation</th>
														<th data-index="5" class="align-center">Other Charges</th>
													</tr>
												</thead>
												<tbody>
													<tr class="clone-row hide">
														<td class="s-no"><span></span></td>
														<td class="align-center chassisNo"></td>
														<td class="align-center freightCharge"><span
															class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="align-center freightChargeUsd"><span
															class="autonumber" data-a-sign="$ " data-m-dec="0"></span></td>
														<td class="align-center shippingCharge"><span
															class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="align-center inspectionCharge"><span
															class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="align-center radiationCharge"><span
															class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="align-center otherCharges"><span
															class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
							<!-- /.form:form -->
							<!-- Model -->
							<div class="modal fade" id="modal-container-invoice-items">
								<div class="modal-dialog modal-lg">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
											<h3 class="modal-title">Invoice Stock</h3>
										</div>
										<div class="modal-body">
											<table class="table table-hover" id="table-invoice-items"
												style="width: 100%">
												<thead>
													<tr>
														<th>Chassis No</th>
														<th>Maker</th>
														<th>Model</th>
														<th>M3</th>
														<th>Amount</th>
													</tr>
												</thead>
												<tbody></tbody>
											</table>
										</div>
									</div>
								</div>
							</div>

							<!-- Inspection Invoice Table Format Details -->
							<div id="inspection-clone-container">
								<div id="payment-inspection-approve-details" class="hide">
									<div class="box-body no-padding clone-element">
										<div class="table-responsive">
											<input type="hidden" name="invoiceNo" value="" />
											<table class="table table-bordered" style="overflow-x: auto;">
												<thead>
													<tr>
														<th class="align-center">#</th>
														<th data-index="0" class="align-center">Stock No</th>
														<th data-index="1" class="align-center">Chassis No</th>
														<th data-index="2" class="align-center">Maker</th>
														<th data-index="3" class="align-center">Model</th>
														<th data-index="4" class="align-center">Amount</th>
														<th data-index="5" class="align-center">Tax</th>
														<th data-index="6" class="align-center">Total Amount</th>
													</tr>
												</thead>
												<tbody>
													<tr class="clone-row hide">
														<td class="s-no"><span></span></td>
														<td class="align-center stockNo"></td>
														<td class="align-center chassisNo"></td>
														<td class="align-center maker"></td>
														<td class="align-center model"></td>
														<td class="dt-right amount"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="dt-right tax"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="dt-right total"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
													</tr>
												</tbody>
												<tfoot>
													<tr>
														<td colspan="5"><strong class="pull-right">Total</strong></td>
														<td class="dt-right amount"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="dt-right tax"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
														<td class="dt-right total"><span class="autonumber"
															data-a-sign="&yen; " data-m-dec="0"></span></td>
													</tr>
												</tfoot>
											</table>
										</div>
									</div>
								</div>
							</div>

							<!-- Model -->
							<div class="modal fade" id="modal-approve-payment">
								<div class="modal-dialog modal-lg">
									<div class="modal-content">
										<div class="modal-header">
											<!-- <button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button> -->
											<h4 class="modal-title bank-balance pull-right hidden">
												Current Balance : <span class="amount" data-a-sign="¥ "
													data-m-dec="0"></span>
											</h4>
											<h4 class="modal-title">Payment Details</h4>
										</div>
										<div class="modal-body">
											<jsp:include
												page="/WEB-INF/views/accounts/approve-payment-modal.jsp" />
										</div>
										<div class="modal-footer">
											<div class="pull-right">
												<button class="btn btn-primary " id="approve">
													<i class="fa fa-fw fa-save"></i>Approve Payment
												</button>
												<button class="btn btn-default" id="btn-close"
													data-dismiss="modal">
													<i class="fa fa-fw fa-close"></i>Close
												</button>
											</div>
										</div>
									</div>
									<!-- /.modal-content -->
								</div>
								<!-- /.modal-dialog -->
							</div>
							<!-- Model -->
							<div class="modal fade" id="modal-approve-payment-items">
								<div class="modal-dialog modal-lg">
									<div class="modal-content">
										<div class="modal-header">

											<h4 class="modal-title bank-balance pull-right hidden">
												Current Balance : <span class="amount" data-a-sign="¥ "
													data-m-dec="0"></span>
											</h4>
											<h4 class="modal-title">Payment Details</h4>

										</div>
										<div class="modal-body">
											<form action="#" id="payment-detail-form">
												<div class="container-fluid payment-processing-step"
													style="display: none">
													<ul class="list-group list-group-unbordered"
														id="itemContainer">

													</ul>
												</div>
												<div class="container-fluid payment-processing-step"
													id="payment-container" style="display: none">
													<div class="row">
														<input type="hidden" name="ids" /> <input type="hidden"
															name="totalAmountInYen" /> <input type="hidden"
															name="totalFreightInYen" /> <input type="hidden"
															name="totalFreightInUsd" />
														<div class="col-md-3">
															<label class="required">Payment Type</label> <select
																id="paymentType" name="paymentType"
																class="form-control select2 required"
																data-placeholder="Select Payment Type"
																style="width: 100%;">
																<option value=""></option>
																<option value="1">Freight (¥)</option>
																<option value="2">Freight ($)</option>
																<option value="3">Pay All (¥)</option>
															</select><span class="help-block"></span>
														</div>
														<div class="col-md-3">
															<label class="required">Bank</label> <select name="bank"
																id="bank" data-placeholder="Select Bank"
																class="form-control select2-select bank"
																style="width: 100%">
																<option></option>
															</select> <span class="help-block"></span>
														</div>
														<div class="col-md-3">
															<div class="form-group">
																<label class="required">Payment Date</label>
																<div class="input-group">
																	<div class="input-group-addon">
																		<i class="fa fa-fw fa-calendar"></i>
																	</div>
																	<input type="text" name="approvedDate"
																		id="approvedDate"
																		class="form-control required entryDate datepicker"
																		placeholder="dd-mm-yyyy" readonly="readonly" />
																</div>
																<span class="help-block"></span>
															</div>
														</div>

														<div class="col-md-3">
															<div class="form-group">
																<label class="required">Amount</label> <input
																	type="text" name="amount" id="amount"
																	class="form-control autonumeric required"
																	data-a-sign="¥ " data-m-dec="0"> <span
																	class="help-block"></span>
															</div>
														</div>
														<div class="col-md-3">
															<div class="form-group">
																<label>Remarks</label>
																<textarea name="remarks" id="remarks"
																	class="form-control" placeholder="Enter.."></textarea>
															</div>
														</div>
													</div>
												</div>

											</form>


										</div>
										<div class="modal-footer">
											<div class="pull-right">
												<button id="btn-previous-step" class="btn btn-primary"
													onclick="paymentProcessingStepNextPrev(-1)">
													<i class="fa fa-fw fa-backward"></i> Previous
												</button>
												<button id="btn-next-step" class="btn btn-primary"
													onclick="paymentProcessingStepNextPrev(1)">
													<i class="fa fa-fw fa-forward"></i> Next
												</button>
												<button class="btn btn-primary " id="approve">
													<i class="fa fa-fw fa-save"></i>Approve Payment
												</button>
												<button class="btn btn-default" id="btn-close"
													data-dismiss="modal">
													<i class="fa fa-fw fa-close"></i>Close
												</button>
											</div>
										</div>
									</div>
									<!-- /.modal-content -->
								</div>
								<!-- /.modal-dialog -->
							</div>

							<!-- Model -->
							<div class="modal fade" id="modal-invoice-upload">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
											<h3 class="modal-title">Upload Invoice</h3>
										</div>
										<div class="modal-body">
											<input type="hidden" name="invoiceRefNo"> <input
												type="hidden" name="refNo"> <input type="hidden"
												name="invoiceId">
											<form id="form-file-upload">
												<div class="container-fluid">
													<div class="row">
														<div class="col-md-12 ">
															<div class="form-group ">
																<label for="invoice_img">Auction Invoice</label>
																<div class="file-element-wrapper">
																	<input type="file" id="invoiceFile" name="invoiceFile"
																		data-directory="auction_invoice"
																		accept="image/x-png,image/gif,image/jpeg" />
																</div>

																<span class="help-block"></span>
															</div>
														</div>
													</div>
												</div>
											</form>
										</div>
										<div class="modal-footer">
											<button id="upload" class="btn btn-primary">
												<i class="fa fa-fw fa-save"></i> Upload
											</button>
											<button id="btn-close" data-dismiss="modal"
												class="btn btn-primary">
												<i class="fa fa-fw fa-close"></i> Close
											</button>
										</div>
									</div>
								</div>
							</div>

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
												width="100%" style="height: 500px" class="image_preview"></div>
										
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
	<!-- Model -->
	<div class="modal fade" id="modal-edit-dueDate">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
												aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Payment Details</h3>
				</div>
				<div class="modal-body">
					<form id="payment-dueDate-edit">
						<div class="container-fluid">
							<div class="row">
								<input type="hidden" name="invoiceNo">
								<div class="col-md-4 ">
									<div class="form-group">
										<label class="required">Due Date</label>
										<div class="element-wrapper">
											<input type="text" id="dueDate" name="dueDate"
																		class="form-control required datepicker"
																		placeholder="Due Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-dueDate" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="hidden" id="cloneable-wrapper">
		<ul class="list-group list-group-unbordered">
			<li class="list-group-item" id="paymentProcessingItems">
				<div class="row">
					<div class="col-md-3">
						<input type="hidden" name="invoiceId" /> <b class="chassisNo"></b>
					</div>
					<div class="col-md-3">
						<div class="element-wrapper">
							<select name="blStatus" class="form-control select2"
														style="width: 100%;" data-placeholder="Select Status">
								<option value=""></option>
								<option value="1">RECEIVE</option>
								<option value="2">SURRENDER</option>
								<option value="3">HOLD</option>
							</select> <span class="help-block"></span>
						</div>
					</div>
				</div>
			</li>
		</ul>
	</div>
</section>
