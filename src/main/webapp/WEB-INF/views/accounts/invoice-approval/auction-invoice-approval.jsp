<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<section class="content-header">
	<h1>Purchased Invoice</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Approval</span></li>
		<li class="active">Purchased Invoice</li>
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
	<sec:authorize access="canAccess(62)">
		<div class="box box-solid">
			<div class="box-header"></div>
			<div class="box-body">
				<div class="container-fluid">
					<div class="row form-group">
						
						<div class="col-md-2">
							<div class="form-group">
								<label>Supplier/Auction</label> <select id="auctionCompany"
									class="form-control select2" style="width: 100%;">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
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
					<div class="row form-group">
					<div class="col-md-1 pull-left">
							<div class="form-group">
								<select id="table-filter-length"
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
								<input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"
									autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search-chassisNo"
									class="form-control" placeholder="Search by chassis No"
									autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-4 pull-right">
							<sec:authorize access="canAccess(63)">
								<button type="button" class="btn btn-primary pull-right"
									id="approvePayment" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-approve-payment">
									<i class="fa fa-fw fa-check"></i>Approve Payment
								</button>
							</sec:authorize>
						</div>
					</div>
					<!-- table start -->
					<div class="table-responsive">
						<table id="table-auction-payment"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<!-- <th data-index="0"><input type="checkbox" id="select-all" /></th> -->
									<th data-index="0" style="width: 10px">#</th>
									<th data-index="1">Invoice Date</th>
									<th data-index="2">Supplier Invoice No</th>
									<th data-index="3">Auction Ref. No</th>
									<th data-index="4">Remit To</th>
									<th data-index="5">Due Date</th>
									<th data-index="6">Total Amount</th>
									<th data-index="7">Invoice Upload</th>
									<th data-index="8">Supplier ID</th>
									<th data-index="9">Remarks</th>
									<th data-index="10">Action</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- /.form:form -->
			<!-- Model -->
			<div class="modal fade" id="addPaymentModal">
				<div class="modal-dialog" style="width: 80%">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title">Payment Information</h4>
						</div>
						<div class="modal-body">
							<%-- 					<jsp:include page="/WEB-INF/views/shipping/arrange-transport.jsp" /> --%>
						</div>
						<div class="modal-footer">
							<div class="pull-right">
								<button class="btn btn-primary " id="btn-create-order">
									<i class="fa fa-fw fa-save"></i>Create Order
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
		</div>
		<!-- /.modal -->
		<div id="clone-container">
			<div id="invoice-details" class="hide">
				<div class="box-body no-padding bg-darkgray clone-element">
					<div class="table-responsive order-item-container">
						<input type="hidden" name="paymentinvoiceNo" value="" />
						<table class="table table-bordered" style="overflow-x: auto;">
							<thead>
								<tr>
									<th class="align-center ">#</th>
									<th data-index="1" class="align-center">Chassis No</th>
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
											<label>Auction Ref.No</label>
											<div class="element-wrapper">
												<input type="text" id="auctionRefNo" name="auctionRefNo"
													class="form-control auctionRefNo">
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
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Model -->
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
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
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
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
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
							width="100%" style="height: 500px"
							class="img-responsive image_preview" />
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- Model -->
		<div class="modal fade" id="modal-cancellation-amount">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title">Add Cancellation Charge</h3>
					</div>
					<div class="modal-body">
						<form id="payment-detail-form">
							<div class="container-fluid">
								<div class="row">
									<input type="hidden" name="invoiceId"> <input
										type="hidden" name="purchaseInvoiceId">
									<div class="col-md-5 ">
										<label>Stock</label>
										<div class="form-group">
											<select name="stockNo" id="stockNo"
												class="form-control stockNo required"
												data-placeholder="Search by Stock No or Chassis No"><option
													value=""></option></select><span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-4 ">
										<label>Amount</label>
										<div class="form-group">
											<input class="form-control required amount" name="amount"
												id="amount" type="text" data-a-sign="¥ " data-m-dec="0"><span
												class="help-block"></span>
										</div>
									</div>

								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button id="charge-save" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- modal cancel remark -->
		<div class="modal fade" id="modal-remark">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Please enter remark</h4>
					</div>
					<div class="modal-body">
						<form id="remarkForm">
							<div class="form-group">
								<textarea name="cancellationRemarks"
									class="form-control required" rows="3" placeholder="Enter ..."></textarea>
							</div>
						</form>

					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button type="button" id="btn-remark-submit"
								class="btn btn-primary">Submit</button>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
	</sec:authorize>
</section>
