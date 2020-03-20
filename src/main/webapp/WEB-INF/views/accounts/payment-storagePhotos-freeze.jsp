<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Storage & Photos Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Completed</span></li>
		<li class="active">Storage & Photos</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/payment-freeze-icons.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
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
						<div class="form-group">
							<label>Forwarder</label><select id="forwarderFilter"
								name="forwarderFilter" class="form-control"
								data-placeholder="Select Remitter" style="width: 100%;">
								<option value=""></option>
							</select>
						</div>
					</div>
				</div>
			</div>

			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="form-group" id="buttonShowHide">
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
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-auction-payment"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Date</th>
								<th data-index="2" class="align-center">Invoice No</th>
								<th data-index="4" class="align-center">Remit To</th>
								<th data-index="5" class="align-center">Due Date</th>
								<th data-index="6" class="align-center">Total Amount</th>
								<th data-index="7" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<div id="clone-container">
		<div id="invoice-details" class="hide">
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
								<td class="dt-right remarks"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="13"><strong class="pull-right">Total</strong></td>
								<td class="dt-right amount"><span class="autonumber"
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
</section>
