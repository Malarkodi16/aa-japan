<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section class="content-header">
	<h1>Freight & Shipping</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Approval</span></li>
		<li class="active">Freight & Shipping</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/payment-completed-icons.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
			<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label class="required">Forwarder</label> <select
								class="form-control "
								id="forwarderFilter" data-placeholder="Select Forwarder"
								style="width: 100%" name="forwarder">
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
					
				</div>
				<div class="table-responsive" id="roro-payment-completed">
					<table id="table-roro-payment-completed"
						class="table table-bordered table-striped" style="width: 100%;">
						<thead>
							<tr>
								<th data-index="1">Invoice No</th>
								<th data-index="2">Payment Voucher No</th>								
								<th data-index="3">Date</th>
								<th data-index="4">Remit To</th>
								<th data-index="5">Due Date</th>
								<th data-index="6">Total Amount (¥)</th>
								<th data-index="7">Total Amount ($)</th>
								<th data-index="8">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- clone container for freight adn shipping -->
	<div id="clone-container">
		<div id="roro-container-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive roro-item-container-details">
					<input type="hidden" name="invoiceNo" value="" />
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
								<td class="dt-right freightCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right freightChargeUsd"><span
									class="autonumber" data-a-sign="$ " data-m-dec="0"></span></td>
								<td class="dt-right shippingCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right inspectionCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right radiationCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right otherCharges"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
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
	</div>
	<!-- Modal Cancel Payment -->
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
</section>