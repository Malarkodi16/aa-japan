<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Other Direct Expense List</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Approval</span></li>
		<li class="active">Other Direct Expense</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-approval/invoice-approval-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="container-fluid ">
			<div class="row form-group">
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
			<div class="table-responsive">
				<!-- TForwarder invoice show/search inputs -->
				<div class="row form-group">
					<div class="form-group">
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
				<div class="col-md-3">
					<div class="has-feedback">
						 <input type="text"
							id="table-filter-search-chassisNo" class="form-control"
							placeholder="Search by chassis No" autocomplete="off"> <span
							class="glyphicon glyphicon-search form-control-feedback"></span>
					</div>
				</div>
						<div class="col-md-4 pull-right">
							<div class="btn-group pull-right">
								<button type="button" class="btn btn-primary"
									id="approvePayment" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-approve-payment">
									<i class="fa fa-fw fa-check"></i>Approve Payment
								</button>
							</div>
						</div>

					</div>
				</div>
				<table id="table-storage-photos-forwarder-invoice"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<!-- <th data-index="0"><input type="checkbox" id="select-all" /></th> -->
							<th data-index="0" style="width: 10px">#</th>
							<th data-index="1" class="align-center">Date</th>
							<th data-index="2" class="align-center">Invoice No</th>
							<th data-index="3" class="align-center">Invoice Type</th>
							<th data-index="4" class="align-center">Remit To</th>
							<th data-index="5" class="align-center">Due Date</th>
							<th data-index="6" class="align-center">Total Amount</th>
							<th data-index="7" class="align-center">Invoice Upload</th>
							<th data-index="8" class="align-center">Remitter</th>
							<th data-index="9" class="align-center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- Model -->
	<div id="storage-clone-container">
		<div id="storage-invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
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
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="dt-right storage"><span class="autonumber"
									data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right shipping"><span class="autonumber"
									data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right photoCharges"><span class="autonumber"
									data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right blAmendCombineCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right radiationCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right repairCharges"><span class="autonumber"
									data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right yardHandlingCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right inspectionCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right transportCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="dt-right freightCharges"><span
									class="autonumber" data-a-sign="� " data-m-dec="0"></span></td>
								<td class="align-center remarks"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="� " data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="13"><strong class="pull-right">Total</strong></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tfoot>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modal-edit-details">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Edit Details</h3>
				</div>
				<div class="modal-body">
					<form id="edit-payment-form">
						<div class="container-fluid">

							<div class="row">
								<input type="hidden" name="invoiceNo" id="invoiceNo"
									class="form-control required invoiceNo" />
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
								<div class="col-md-4 ">
									<div class="form-group">
										<label>Ref.No</label>
										<div class="element-wrapper">
											<input type="text" id="refNo" name="refNo"
												class="form-control refNo">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4 ">
									<div class="form-group">
										<label>Remarks</label>
										<div class="element-wrapper">
											<input type="text" id="remarks" name="remarks"
												class="form-control remarks">
										</div>
										<span class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="edit" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Approve payment Modal -->
	<div class="modal fade" id="modal-approve-payment">
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
					<form id="payment-detail-form">
						<div class="container-fluid">

							<div class="row">
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
					<button id="approve" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
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
					<input type="hidden" name="invoiceId">
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
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
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
	<!-- Model -->
</section>