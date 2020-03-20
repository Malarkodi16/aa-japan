<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Transport Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Invoice Booking & Approval</span></li>
		<li class="active">Transport Approval</li>
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
			<div class="container-fluid" id="transport-invoice-list">
				<div class="table-responsive">
					<!-- payment transport show/search inputs -->
					<div class="row form-group">
						<div class="col-md-2 pull-left">
							<div class="form-group">
								<label>Transporter</label> <select id="table-filter-transporter"
									class="form-control pull-right"
									placeholder="Select Transporter">
									<option value=""></option>
								</select>
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
							<div class="form-group" id="invoice-date-form-group">
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
								<input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"
									autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>


						<div class="col-md-8">
							<div class="pull-right">
								<button type="button" class="btn btn-primary pull-right"
									id="approvePayment" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-approve-payment">
									<i class="fa fa-fw fa-check"></i>Approve Payment
								</button>
							</div>

						</div>
					</div>
				
				<table id="table-transport-invoice"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<!-- <th data-index="0"><input type="checkbox" id="select-all" /></th> -->
							<th data-index="0" style="width: 10px">#</th>
							<th data-index="1" class="align-center">Due Date</th>
							<th data-index="2" class="align-center">Invoice Date</th>
							<th data-index="3" class="align-center">Remit To</th>
							<th data-index="4" class="align-center">Total Amount</th>
							<th data-index="5" class="align-center">Invoice Upload</th>
							<th data-index="6" class="align-center">Action</th>

						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</div>
	<!-- Transport Invoice Table Format Details -->
	<div id="transport-clone-container">
		<div id="payment-transport-approve-details" class="hide">
			<div class="box-body no-padding clone-element">
				<div class="table-responsive transportorder-item-container">
					<input type="hidden" name="transportinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center">#</th>
								<th data-index="0" class="align-center">Stock No</th>
								<th data-index="1" class="align-center">Chassis No</th>
								<th data-index="2" class="align-center">Maker</th>
								<th data-index="3" class="align-center">Model</th>
								<th data-index="4" class="align-center">Pickup Location</th>
								<th data-index="5" class="align-center">Drop Location</th>
								<th data-index="6" class="align-center">Amount</th>
								<th data-index="7" class="align-center">Tax</th>
								<th data-index="8" class="align-center">Total Amount</th>
								<th data-index="9" class="align-center">Status</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="align-center pickupLocation"></td>
								<td class="align-center dropLocation"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right tax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right total"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center stockStatus"><span class="label"></span></td>

							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7"><strong class="pull-right">Total</strong></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right tax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right total"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td></td>
							</tr>
						</tfoot>
					</table>
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
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
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