<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Freight & Shipping</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Approval</span></li>
		<li><span><em class=""></em>Freight & Shipping</span></li>
		<li class="active"><a>Container Invoice</a></li>
	</ol>
</section>
<section class="content">
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-approval/invoice-approval-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">

					<li><a
						href="${contextPath}/accounts/invoice/approval/shipping/roro"><strong>RORO</strong></a></li>
					<li class="active"><a
						href="${contextPath}/accounts/invoice/approval/shipping/container"><strong>CONTAINER</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">
			<div class="table-responsive" id="roro-invoice-created">

				<div class="row form-group">
					<div class="form-group">
						
						<div class="col-md-2">
							<div class="form-group">
							<label>Forwarder</label>
								<select class="form-control" id="container-filter-frwdr"
									data-placeholder="Select Forwarder" style="width: 100%"
									name="forwarder">
									<option></option>
								</select><span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
						<label>Due Date</label>
							<input class="form-control dueDateDatepicker" name="dueDate"
								id="dueDate" placeholder="Filter Due Date " readonly />
						</div>
						

					</div>
				</div>
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
						<div class="col-md-2">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword" autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-9 form-group pull-right">
							<button type="button" class="btn btn-primary pull-right"
								id="approvePayment" data-backdrop="static" data-keyboard="false"
								data-toggle="modal" data-target="#modal-approve-payment">
								<i class="fa fa-fw fa-check"></i>Approve Payment
							</button>
						</div>
				</div>
				<table id="table-roro-invoice-created"
					class="table table-bordered table-striped" style="width: 100%;">
					<thead>
						<tr>

							<th data-index="0">#</th>
							<th data-index="1">Date</th>
							<th data-index="2">Invoice No</th>
							<th data-index="3">Remit To</th>
							<th data-index="4">Due Date</th>
							<th data-index="5">Total Amount</th>
							<th data-index="6">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>

		</div>
	</div>
	<!-- Approve Payment -->
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
									data-a-sign="R " data-m-dec="0"></span></td>
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
										<label for="invoice_img">Invoice</label>
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
</section>