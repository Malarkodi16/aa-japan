<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>General Expenses</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li class="active"><a>General Expenses</a></li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">


					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier</label> <select id="table-filter-remitter"
								class="form-control pull-right"
								data-placeholder="Select Remiter">
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
			<div class="row form-group">
				<div class="col-md-1 form-inline pull-left">
					<select id="table-filter-length" class="form-control">
						<option value="10">10</option>
						<option value="25" selected="selected">25</option>
						<option value="100">100</option>
						<option value="1000">1000</option>
					</select>
				</div>
				<div class="col-md-3">
					<div class="has-feedback">
						<input type="text" id="table-filter-search" class="form-control"
							placeholder="Search by keyword" autocomplete="off"> <span
							class="glyphicon glyphicon-search form-control-feedback"></span>
					</div>
				</div>
					<div class="col-md-8">

						<div class="pull-right">
							
							<button type="button" class="btn btn-primary" data-toggle="modal"
								data-backdrop="static" data-target="#add-payment">
								<i class="fa fa-fw fa-plus"></i>Add Payment
							</button>
							<button type="button" class="btn  btn-primary" id="addNew"
								data-toggle="modal" data-target="#add-new-category">New
								Ledger</button>
							<a
								href="${contextPath}/accounts/invoice/booking/genaralExpenses/export"><button
									type="button" class="btn btn-primary">Export Excel</button></a>
						</div>
					</div>
			</div>
			<div class="table-responsive">
				<!-- /. DataTable Start -->
				<table id="table-payment-others-invoice"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<!-- <th data-index="0"><input type="checkbox" id="select-all" /></th> -->
							<th data-index="0" style="width: 10px">#</th>
							<th data-index="1" class="align-center">Date</th>
							<!-- <th data-index="2" class="align-center">Due Date</th> -->
							<th data-index="2" class="align-center">Invoice No</th>
							<th data-index="3" class="align-center">Sup Invoice No</th>
							<th data-index="4" class="align-center">Invoice Type</th>
							<th data-index="5" class="align-center">Supplier</th>
							<th data-index="6" class="align-center">Due Date</th>
							<th data-index="7" class="align-center">Total Amount</th>
							<th data-index="8" class="align-center">Invoice Upload</th>
							<th data-index="9" class="align-center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<!-- /. DataTable End -->
	</div>
	</div>
	<div class="modal fade" id="add-payment">
		<div class="modal-dialog modal-lg" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">General Expenses</h4>
				</div>
				<div class="modal-body">
					<form id="other-form">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Remit To</label>
									<div class="input-group">
										<div class="element-wrapper">
											<select name="remitter" id="remitTo"
												class="form-control select2 with-remit other-paymentdata other-data supplier"
												data-placeholder="Remit To">
												<option value=""></option>
											</select>
										</div>
										<div class="input-group-addon" title="Add General Supplier"
											data-backdrop="static" data-keyboard="false"
											data-toggle="modal" data-target="#add-gen-supplier">
											<i class="fa fa-plus"></i>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>

							<!-- <div class="col-md-3" id="remit-select">
								<div class="form-group">
									<label class="required">Remit To</label> <select
										name="remitter" id="remitTo"
										class="form-control select2 with-remit other-paymentdata other-data supplier"
										data-placeholder="Remit To">
										<option value=""></option>
									</select>
									<div class="remit-input-container hidden">
										<input type="text" class="form-control remit-input other-data"
											name="remitterOthers" /> <a
											class="show-hand-cursor show-dropdown">show dropdown</a>
									</div>
									<span class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Invoice No</label><input type="text"
										name="refNo" id="" class="form-control other-data" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker other-data"
											name="date" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Due Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker other-data"
											name="dueDate" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="table-responsive" id="payment-item-table"
								style="max-height: 380px;">
								<table class="table table-bordered table-striped">
									<thead>
										<tr>
											<th colspan="11" bgcolor="#3c8dbc" class="align-center">Payment
												Item</th>
										</tr>
										<tr>
											<th><label class="required">Category</label></th>
											<th><label>Description</label></th>
											<th><label class="required">Amount(¥)</label></th>
											<th><label>Tax</label></th>
											<th><label>Tax %</label></th>
											<th><label>TaxAmount</label></th>
											<th><label>TotalAmount</label></th>
											<th><label>Source Currency</label></th>
											<th><label>Amount</label></th>
											<th><label>Exchange rate</label></th>
											<th><label>Action</label></th>
										</tr>
									</thead>
									<tbody class="payment-row-clone-container">
										<tr class="clone-row hide">

											<td class="payment-category-select"><div
													class="element-wrapper">
													<div class="form-group">
														<select name="category"
															class="form-control with-category category required"
															id="category" data-placeholder="Select Category"><option
																value=""></option></select><span class="help-block"></span>
														<div class="category-input-container hidden">
															<input type="text" name="categoryOthers"
																class="form-control category-input"
																placeholder="Enter.." /> <a
																class="show-hand-cursor show-dropdown">show dropdown</a><span
																class="help-block"></span>
														</div>
													</div>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control" name="description">
												</div></td>
											<td>
												<div class="element-wrapper">
													<input type="text"
														class="form-control autonumber amountInYen required"
														name="amountInYen" data-a-sign="¥ " data-m-dec="0"><span
														class="help-block"></span>
												</div>
											</td>
											<td>
												<div class="element-wrapper">
													<input type="checkbox" class="taxInclusive"
														name="taxInclusive" />
												</div>
											</td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber taxPercent required"
														name="taxPercent" data-v-max="100" data-v-min="0"
														data-a-sign=" %" data-p-sign="s" data-m-dec="0"> <span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="hidden" class="form-control hiddenTaxAmount"
														name="hiddenTaxAmount" data-m-dec="0"> <input
														type="text" class="form-control autonumber taxAmount"
														name="taxAmount" data-a-sign="¥ " data-m-dec="0">
													<span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber totalAmount"
														name="totalAmount" data-a-sign="¥ " data-m-dec="0"
														disabled> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control"
														name="sourceCurrency"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control autonumber amount"
														name="amount" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber exchangeRate"
														name="exchangeRate" data-v-min="0" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td style="width: 150px"><button type="button"
													class="btn btn-primary btn-sm add-payment-row"
													id="add-payment-row">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-payment-row"
													id="delete-payment-row">
													<i class="fa fa-close"></i>
												</button></td>
										</tr>
										<tr class="invoice-item">
											<td class="payment-category-select"><div
													class="element-wrapper">
													<div class="form-group">
														<select name="category"
															class="form-control with-category category required"
															id="category" data-placeholder="Select Category"><option
																value=""></option></select><span class="help-block"></span>
														<div class="category-input-container hidden">
															<input type="text" name="categoryOthers"
																class="form-control category-input"
																placeholder="Enter.." /> <a
																class="show-hand-cursor show-dropdown">show dropdown</a><span
																class="help-block"></span>
														</div>
													</div>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control" name="description">
												</div></td>
											<td>
												<div class="element-wrapper">
													<input type="text"
														class="form-control autonumber amountInYen required"
														name="amountInYen" data-a-sign="¥ " data-m-dec="0"><span
														class="help-block"></span>
												</div>
											</td>
											<td>
												<div class="element-wrapper">
													<input type="checkbox" class="taxInclusive"
														name="taxInclusive" />
												</div>
											</td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber taxPercent required"
														name="taxPercent" value="10" data-v-max="100"
														data-v-min="0" data-a-sign=" %" data-p-sign="s"
														data-m-dec="0"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="hidden" class="form-control hiddenTaxAmount"
														name="hiddenTaxAmount" data-m-dec="0"> <input
														type="text" class="form-control autonumber taxAmount"
														name="taxAmount" data-a-sign="¥ " data-m-dec="0">
													<span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber totalAmount"
														name="totalAmount" data-a-sign="¥ " data-m-dec="0"
														disabled> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control "
														name="sourceCurrency"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control autonumber amount"
														name="amount" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber exchangeRate "
														name="exchangeRate" data-v-min="0" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td style="width: 150px"><button type="button"
													class="btn btn-primary btn-sm add-payment-row"
													id="add-payment-row">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-payment-row"
													id="delete-payment-row">
													<i class="fa fa-close"></i>
												</button></td>
										</tr>
									<tfoot>
										<tr>
											<td class="align-right" colspan="2"><strong>Total</strong></td>
											<td class="align-center amountInYenTotal"><span
												class="autonumber amountInYenTotal" data-m-dec="0"
												data-a-sign="¥ "></span></td>
											<td></td>
											<td></td>
											<td class="align-center taxTotal"><span
												class="autonumber taxTotal" data-m-dec="0" data-a-sign="¥ "></span></td>
											<td class="align-center footerTotal"><span
												class="autonumber footerTotal" data-m-dec="0"
												data-a-sign="¥ "></span></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tfoot>
									</tbody>
								</table>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="save-other-payments">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<div id="others-clone-container">
		<div id="others-invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="0" class="align-center">Category</th>
								<th data-index="1" class="align-center">Description</th>
								<th data-index="2" class="align-center">Amount(¥)</th>
								<th data-index="3" class="align-center">TaxAmount</th>
								<th data-index="4" class="align-center">TotalAmount</th>
								<th data-index="5" class="align-center">Source Currency</th>
								<th data-index="6" class="align-center">Amount</th>
								<th data-index="7" class="align-center">Exchange rate</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center category"></td>
								<td class="align-center description"></td>
								<td class="align-center amountInYen"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center taxAmount"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center totalAmount"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center currency"></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center exchangeRate"><span
									class="autonumber" data-m-dec="0"></span></td>
							</tr>
						<tfoot>
							<tr>
								<td colspan="3"><strong class="pull-right">Total</strong></td>
								<td class="align-center amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center taxTotal"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center taxIncluded"><span
									class="autonumber" data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tfoot>
						</tbody>
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
												accept="image/x-png,image/gif,image/jpeg, application/pdf, .xlsx, .xls, .csv" />
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
	<div class="modal fade" id="add-new-category">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="add-new-category-form">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Add New Category</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="col-md-6">
								<label class="required">Sub Category</label> <input type="text"
									name="categoryDesc" id="categoryDesc"
									class="form-control required" /> <span class="help-block"></span>
							</div>
							<div class="col-md-6">
								<label class="required">Account Type</label> <select
									name="accountType" id="accountType"
									class="form-control required select2-tag"
									data-placeholder="Account Type">
									<option></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<!-- /.form:form -->
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-create-category">
								<i class="fa fa-fw fa-save"></i>Create
							</button>
							<button class="btn btn-primary" id="btn-close"
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
	<!-- /.modal -->
	<!-- Model -->
	<div class="modal fade" id="add-gen-supplier" style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="add-new-general-supplier">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Add New General Supplier</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="col-md-6">
								<label class="required">Supplier Name</label> <input type="text"
									name="suppliername" id="suppliername" class="form-control"
									required="required" /> <span class="help-block"></span>
							</div>
						</div>
						<!-- /.form:form -->
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " type="button"
								id="btn-create-general-supplier">
								<i class="fa fa-fw fa-save"></i>Create
							</button>
							<button class="btn btn-primary" id="btn-close"
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
	<!-- /.modal -->
	<div class="modal fade" id="edit-payment">
		<div class="modal-dialog modal-lg" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">General Expenses</h4>
				</div>
				<div class="modal-body">
					<form id="other-form">
						<div class="row">
							<input type="hidden" class="other-data" name="invoiceNo">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Remit To</label>
									<div class="input-group">
										<div class="element-wrapper">
											<select name="remitter" id="remitTo"
												class="form-control select2 with-remit other-paymentdata other-data supplier"
												data-placeholder="Remit To">
												<option value=""></option>
											</select>
										</div>
										<div class="input-group-addon" title="Add General Supplier"
											data-backdrop="static" data-keyboard="false"
											data-toggle="modal" data-target="#add-gen-supplier">
											<i class="fa fa-plus"></i>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>

							<!-- <div class="col-md-3" id="remit-select">
								<div class="form-group">
									<label class="required">Remit To</label> <select
										name="remitter" id="remitTo"
										class="form-control select2 with-remit other-paymentdata other-data supplier"
										data-placeholder="Remit To">
										<option value=""></option>
									</select>
									<div class="remit-input-container hidden">
										<input type="text" class="form-control remit-input other-data"
											name="remitterOthers" /> <a
											class="show-hand-cursor show-dropdown">show dropdown</a>
									</div>
									<span class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Reference No</label><input type="text"
										name="refNo" id="" class="form-control other-data" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker other-data"
											name="date" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Due Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker other-data"
											name="dueDate" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="table-responsive" id="edit-payment-item-table">
								<table class="table table-bordered table-striped"
									style="width: 100%; overflow: scroll;">
									<thead>
										<tr>
											<th colspan="11" bgcolor="#3c8dbc" class="align-center">Payment
												Item</th>
										</tr>
										<tr>
											<th><label class="required">Category</label></th>
											<th><label>Description</label></th>
											<th><label class="required">Amount(¥)</label></th>
											<th><label>Tax</label></th>
											<th><label>Tax %</label></th>
											<th><label>TaxAmount</label></th>
											<th><label>TotalAmount</label></th>
											<th><label>Source Currency</label></th>
											<th><label>Amount</label></th>
											<th><label>Exchange rate</label></th>
											<th><label>Action</label></th>
										</tr>
									</thead>
									<tbody class="edit-payment-row-clone-container">
										<tr class="clone-row hide">

											<td class="payment-category-select"><div
													class="element-wrapper">
													<div class="form-group">
														<input type="hidden" name="id" /> <select name="category"
															class="form-control with-category category required"
															id="category" data-placeholder="Select Category"><option
																value=""></option></select><span class="help-block"></span>
														<div class="category-input-container hidden">
															<input type="text" name="categoryOthers"
																class="form-control category-input"
																placeholder="Enter.." /> <a
																class="show-hand-cursor show-dropdown">show dropdown</a><span
																class="help-block"></span>
														</div>
													</div>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control" name="description">
												</div></td>
											<td>
												<div class="element-wrapper">
													<input type="text"
														class="form-control autonumber amountInYen required"
														name="amountInYen" data-a-sign="¥ " data-m-dec="0"><span
														class="help-block"></span>
												</div>
											</td>
											<td>
												<div class="element-wrapper">
													<input type="checkbox" class="taxInclusive"
														name="taxInclusive" />
												</div>
											</td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber taxPercent required"
														name="taxPercent" data-v-max="100" data-v-min="0"
														data-a-sign=" %" data-p-sign="s" data-m-dec="0"> <span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="hidden" class="form-control hiddenTaxAmount"
														name="hiddenTaxAmount" data-m-dec="0"> <input
														type="text" class="form-control autonumber taxAmount"
														name="taxAmount" data-a-sign="¥ " data-m-dec="0">
													<span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber totalAmount"
														name="totalAmount" data-a-sign="¥ " data-m-dec="0"
														disabled> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control"
														name="sourceCurrency"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control autonumber amount"
														name="amount" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber exchangeRate"
														name="exchangeRate" data-v-min="0" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td style="width: 150px"><button type="button"
													class="btn btn-primary btn-sm add-payment-row"
													id="add-payment-row">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-payment-row"
													id="delete-payment-row">
													<i class="fa fa-close"></i>
												</button></td>
										</tr>
										<tr class="invoice-item">
											<td class="payment-category-select"><div
													class="element-wrapper">
													<div class="form-group">
														<input type="hidden" name="id" /> <select name="category"
															class="form-control with-category category required"
															id="category" data-placeholder="Select Category"><option
																value=""></option></select><span class="help-block"></span>
														<div class="category-input-container hidden">
															<input type="text" name="categoryOthers"
																class="form-control category-input"
																placeholder="Enter.." /> <a
																class="show-hand-cursor show-dropdown">show dropdown</a><span
																class="help-block"></span>
														</div>
													</div>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control" name="description">
												</div></td>
											<td>
												<div class="element-wrapper">
													<input type="text"
														class="form-control autonumber amountInYen required"
														name="amountInYen" data-a-sign="¥ " data-m-dec="0"><span
														class="help-block"></span>
												</div>
											</td>
											<td>
												<div class="element-wrapper">
													<input type="checkbox" class="taxInclusive"
														name="taxInclusive" />
												</div>
											</td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber taxPercent required"
														name="taxPercent" value="10" data-v-max="100"
														data-v-min="0" data-a-sign=" %" data-p-sign="s"
														data-m-dec="0"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="hidden" class="form-control hiddenTaxAmount"
														name="hiddenTaxAmount" data-m-dec="0"> <input
														type="text" class="form-control autonumber taxAmount"
														name="taxAmount" data-a-sign="¥ " data-m-dec="0">
													<span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber totalAmount"
														name="totalAmount" data-a-sign="¥ " data-m-dec="0"
														disabled> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control "
														name="sourceCurrency"> <span class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text" class="form-control autonumber amount"
														name="amount" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td><div class="element-wrapper">
													<input type="text"
														class="form-control autonumber exchangeRate "
														name="exchangeRate" data-v-min="0" data-m-dec="0"><span
														class="help-block"></span>
												</div></td>
											<td style="width: 150px"><button type="button"
													class="btn btn-primary btn-sm add-payment-row"
													id="add-payment-row">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-payment-row"
													id="delete-payment-row">
													<i class="fa fa-close"></i>
												</button></td>
										</tr>
									<tfoot>
										<tr>
											<td class="align-right" colspan="2"><strong>Total</strong></td>
											<td class="align-center amountInYenTotal"><span
												class="autonumber amountInYenTotal" data-m-dec="0"
												data-a-sign="¥ "></span></td>
											<td></td>
											<td></td>
											<td class="align-center taxTotal"><span
												class="autonumber taxTotal" data-m-dec="0" data-a-sign="¥ "></span></td>
											<td class="align-center footerTotal"><span
												class="autonumber footerTotal" data-m-dec="0"
												data-a-sign="¥ "></span></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tfoot>


								</table>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="save-other-payments">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>