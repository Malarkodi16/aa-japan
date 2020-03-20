
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Inspection Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Completed</span></li>
		<li class="active">Inspection Invoice</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include page="/WEB-INF/views/accounts/payment-freeze-icons.jsp" />


	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid"
				id="inspection-payment-approval-invoice-list">
				<div class="table-responsive">
					<!-- payment transport show/search inputs -->
					<div class="row form-group">


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
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Inspection</label> <select id="table-filter-inspection"
									class="form-control pull-right"
									data-placeholder="Select Inspection Company">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-md-1 form-inline">
							<div class="form-group">
								<label></label> <select id="table-filter-length"
									class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
						<div class="col-md-3">
							<div class="has-feedback">
								<label></label> <input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"
									autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
					<table id="table-inspection-invoice"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="1" class="align-center">Invoice Date</th>
								<th data-index="2" class="align-center">Supplier Invoice No</th>
								<th data-index="3" class="align-center">Payment Voucher No</th>
								<th data-index="4" class="align-center">Ref No</th>
								<th data-index="5" class="align-center">Inspection Company</th>
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