
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Purchased Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Approval</span></li>
		<li class="active">Purchased Invoice</li>
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
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<label>Supplier</label> <select name="supplier" id="supplier"
							class="form-control supplier select2" data-placeholder="Supplier">
							<option value=""></option>
						</select>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Due Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-due-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Invoice Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-invoice-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
						</div>
					</div>

				</div>

			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25">25</option>
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

					<div class="col-md-8">
						<div class="pull-right">
							<div class="form-group">

								<div class="input-group">
									<button class="btn btn-primary pull-right" type="button"
										id="excel_export_all">
										<i class="fa fa-file-excel-o" aria-hidden="true"> Export
											Excel</i>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-payment-completed"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1">Invoice Date</th>
								<th data-index="2">Payment Voucher No.</th>
								<th data-index="3">Supplier Invoice No</th>
								<th data-index="4">Auction Ref. No</th>
								<th data-index="5">Supplier</th>
								<th data-index="6">Auction House</th>
								<th data-index="7">Due Date</th>
								<th data-index="8">Total Amount</th>
								<th data-index="9">Paid Amount</th>
								<th data-index="10">Balance</th>
								<th data-index="11">Remarks</th>
								<th data-index="12">Action</th>
								<th data-index="13">Auction House</th>
								<th data-index="14">Payment Status</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- /.form:form -->
		<div id="clone-container">
			<div id="invoice-details" class="hide">
				<div class="box-body no-padding bg-darkgray clone-element">
					<div class="table-responsive order-item-container">
						<input type="hidden" name="paymentinvoiceNo" value="" />
						<table class="table table-bordered" style="overflow-x: auto;">
							<thead>
								<tr>
									<th class="align-center ">#</th>
									<th data-index="1" class="align-center">Chassis No.</th>
									<th data-index="2" class="align-center">Type</th>
									<th data-index="3" class="align-center">Purchase Cost</th>
									<th data-index="4" class="align-center">Purchase Cost Tax</th>
									<th data-index="5" class="align-center">Commission</th>
									<th data-index="6" class="align-center">Commission Tax</th>
									<th data-index="7" class="align-center">Road Tax</th>
									<th data-index="8" class="align-center">Recycle</th>
									<th data-index="9" class="align-center">Others</th>
									<th data-index="10" class="align-center">Others Tax</th>
								</tr>
							</thead>
							<tbody>
								<tr class="clone-row hide">
									<td class="s-no"><span></span></td>
									<td class="align-center chassisNo"></td>
									<td class="align-center type"></td>
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
							</tbody>
							<tfoot>
								<tr>
									<td colspan="3" class="align-center"><strong>Total</strong></td>
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
	<div class="modal fade" id="modal-statementUpload-upload"
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
					<input type="hidden" name="code"> <input type="hidden"
						name="invoiceData" /> <input type="hidden" name="rowData" />
					<form id="form-file-upload">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6 ">
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
								<div class="col-md-6 information">
									<p>
										<label>Remit To:</label><span>&nbsp;&nbsp;&nbsp;</span><span
											class="remitTo"></span>
									</p>
									<p>
										<label>Bank:</label><span>&nbsp;&nbsp;&nbsp;</span><span
											class="bank"></span>
									</p>
									<p>
										<label>Amount:</label><span>&nbsp;&nbsp;&nbsp;</span><span
											class="autonumber amount" data-a-sign="¥ " data-m-dec="0"></span>
									</p>
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
					<h4 class="modal-title">
						Payment Details<span> - </span><span class="invoiceNo"></span><span>
							/ </span><span class="supplierName"></span>
					</h4>
				</div>
				<div class="modal-body" id="invoicePaymentDetails">
					<input type="hidden" name="invoiceNo"> <input type="hidden"
						name="rowData" />
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-detail-invoice"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Payment Voucher No.</th>
										<th data-index="1">Date</th>
										<th data-index="2">Bank</th>
										<th data-index="3">Amount</th>
										<th data-index="4">Action</th>
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
