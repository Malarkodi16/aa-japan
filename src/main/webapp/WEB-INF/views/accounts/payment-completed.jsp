<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Payment Completed</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li class="active"><a>Completed</a></li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
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
						<label>Auction House</label> <select name="supplier" id="supplier"
							class="form-control supplier select2"
							data-placeholder="Auction House">
							<option value=""></option>
						</select>
					</div>
					<div class="col-md-2">
						<label>Invoice Date</label> <input
							class="form-control dueDateDatepicker" name="dueDate"
							id="dueDate" placeholder="  dd-mm-yyyy" readonly />
					</div>
					<div class="col-md-2">
						<label>Payment Date</label> <input
							class="form-control paymentDateDatepicker" name="paymentDate"
							id="paymentDate" placeholder="  dd-mm-yyyy" readonly />
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
							<option value="4">Others</option>
						</select><span class="help-block"></span>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="table-responsive">
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
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
				</div>
				<table id="table-payment-completed"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0"><input type="checkbox" id="select-all" /></th>
							<th data-index="1">Date</th>
							<th data-index="2">Invoice No</th>
							<th data-index="3">Remit To</th>
							<th data-index="4">Due Date</th>
							<th data-index="5">Total Amount</th>
							<th data-index="6">Action</th>
							<th data-index="7">Auction House</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
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
								<th class="align-center">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="3" class="align-center">Purchase Cost</th>
								<th data-index="4" class="align-center">Commission</th>
								<th data-index="5" class="align-center">Road Tax</th>
								<th data-index="6" class="align-center">Recycle</th>
								<th data-index="7" class="align-center">Others</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center purchaseCost"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center commision"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center roadTax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center recycle"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center otherCharges"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tbody>
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
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
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
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
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
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
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
								<th data-index="6" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="align-center freightCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center shippingCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center inspectionCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center radiationCharge"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center otherCharges"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model -->
	<div class="modal fade" id="approve-payment">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
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
						<button class="btn btn-primary" id="btn-close"
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
	<!-- /.modal -->

</section>
