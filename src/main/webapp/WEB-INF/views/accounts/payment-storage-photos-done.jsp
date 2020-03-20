<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Storage & Photos Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Approval</span></li>
		<li class="active">Storage & Photos</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include page="/WEB-INF/views/accounts/payment-completed-icons.jsp" />
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<label class="required">Invoice Type</label> <select
							id="invoiceTypeFilter" class="form-control storage-data select2"
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
					<div class="col-md-2">
						<label>Forwarder</label> <select name="forwarder" id="forwarder"
							class="form-control forwarder select2"
							data-placeholder="Select Forwarder">
							<option value=""></option>
						</select>
					</div>
					<div class="col-md-2">
						<label>Due Date</label> <input
							class="form-control dueDateDatepicker" name="dueDate"
							id="dueDate" placeholder="  dd-mm-yyyy" readonly />
					</div>
					<div class="col-md-2">
						<label>Payment Date</label> <input
							class="form-control paymentDateDatepicker" name="paymentDate"
							id="paymentDate" placeholder="  dd-mm-yyyy" readonly />
					</div>
				</div>
			</div>
			<div class="container-fluid ">
				<!-- TForwarder invoice show/search inputs -->
				<div class="row form-group">
					<div class="col-md-1 form-inline">
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
								placeholder="Search by keyword" autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>

				<div class="table-responsive">
					<table id="table-storage-photos-completed"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Date</th>
								<th data-index="2" class="align-center">Payment Voucher No.</th>
								<th data-index="3" class="align-center">Supplier Invoice No</th>
								<th data-index="4" class="align-center">Ref No</th>
								<th data-index="5" class="align-center">Remit To</th>
								<th data-index="6" class="align-center">Due Date</th>
								<th data-index="7" class="align-center">Total Amount</th>
								<th data-index="8">Balance Amount</th>
								<th data-index="9">Action</th>
								<th data-index="10">Auction House</th>
								<th data-index="11">Payment Status</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="storage-clone-container">
		<div id="storage-completed-invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
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
								<td class="dt-right storage"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right shipping"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right photoCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right blAmendCombineCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right radiationCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right repairCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
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
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td></td>
								<td class="align-right" colspan="12"><strong>Total</strong></td>
								<td class="dt-right amountTotal"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>

							</tr>
						</tfoot>
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
	<div class="modal fade" id="modal-payment-cancel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Payment Cancel</h4>
				</div>
				<form id="payment-cancel-form">
					<div class="modal-body">

						<div class="container-fluid">
							<div class="row form-group">
								<input type="hidden" name="invoiceNo" value="">
								<div class="col-md-4">
									<label class="required">Remarks</label>
									<textarea class="form-control" name="cancelledRemarks" cols="4"
										rows="2" placeholder="Remarks..."></textarea>
									<span class="help-block"></span>
								</div>
							</div>
						</div>

					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="cancel-payment">
								<i class="fa fa-fw fa-save"></i>Cancel Payment
							</button>
							<button class="btn btn-default" id="btn-close"
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

	<!-- Model -->
	<div class="modal fade" id="modal-invoice-preview">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Invoice</h4>
				</div>
				<div class="modal-body">
					<embed
						src="${contextPath}/resources/assets/images/no-image-icon.png"
						width="100%" style="height: 500px" class="image_preview">
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Modal bank -->
	<div class="modal fade" id="modal-bank-invoice-preview"
		style="z-index: 1000000">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Bank Statement</h4>
				</div>
				<div class="modal-body">
					<img src="${contextPath}/resources/assets/images/no-image-icon.png"
						width="100%" style="max-height: 500px" name="image_preview"
						class="img-responsive" alt="stock images">
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Model -->
	<div class="modal fade" id="modal-statement-upload"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Upload Bank Statement</h3>
				</div>
				<div class="modal-body">
					<input type="hidden" name="code">
					<form id="form-file-upload">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-12 ">
									<div class="form-group ">
										<label for="invoice_img">Bank Statement</label>
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
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
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
										<th data-index="0">Payment Voucher No</th>
										<th data-index="0">Date</th>
										<th data-index="1">Bank</th>
										<th data-index="2">Amount</th>
										<th data-index="3">Action</th>
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