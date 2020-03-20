<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Transport</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Booking</span></li>
		<li class="active">Transport</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid" id="transport-invoice-create">
				<div class="table-responsive">
					<!-- payment transport show/search inputs -->
					<div class="row form-group">
						<div class="col-md-2">
							<div class="form-group">
								<label>Transporter</label> <select id="table-filter-transporter"
									class="form-control pull-right"
									placeholder="Select Transporter">
									<option value=""></option>
								</select>
							</div>
						</div>

						<div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>Request Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-date" placeholder="Select Date Range"
										readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="purchase-date-form-group">
								<label>Purchase Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-purchase-date"
										placeholder="Select Date Range" readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="arrival-date-form-group">
								<label>Arrival Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-arrival-date" placeholder="Select Date Range"
										readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>




					</div>
					<div class="row form-group">
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
									placeholder="Search by keyword" autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-8">
							<div class="pull-right">

								<button type="button" class="btn btn-primary"
									data-toggle="modal" data-backdrop="static"
									data-target="#modal-add-payment">
									<i class="fa fa-fw fa-plus"></i>Add Payment
								</button>
								<button type="button" class="btn  btn-primary" id="addNew"
									data-toggle="modal" data-target="#add-new-category">New
									Payment Category</button>
								<button type="button" class="btn btn-primary"
									id="approvePayment" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-create-invoice">
									<i class="fa fa-fw fa-file-pdf-o"></i>Book Payment
								</button>
							</div>
						</div>
					</div>



					<table id="table-transport-completed"
						class="table table-bordered table-striped" style="width: 100%;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Request Date</th>
								<th data-index="2" class="align-center">Purchase Date</th>
								<th data-index="3" class="align-center">Chassis No</th>
								<th data-index="4" class="align-center">Maker</th>
								<th data-index="5" class="align-center">Model</th>
								<th data-index="6" class="align-center">Transporter</th>
								<th data-index="7" class="align-center">Pickup Location</th>
								<th data-index="8" class="align-center">Drop Location</th>
								<th data-index="9" class="align-center">Arrival Date</th>
								<th data-index="10" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-add-payment">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Payment</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formAddPayment">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Transporter</label>
										<div class="element-wrapper">
											<select id="transporter"
												class="form-control select2 required" name="transporter"
												data-placeholder="Select Transporter" style="width: 100%;">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Invoice Date</label>
										<div class="element-wrapper">
											<input type="text" id="invoiceDate" name="invoiceDate"
												class="form-control datepicker required"
												placeholder="DD-MM-YYYY" readonly="readonly">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<div class="form-group">
										<label class="required">Category</label>
										<div class="element-wrapper">
											<select id="category" name="category"
												class="form-control select2" style="width: 100%;"
												data-placeholder="Select Category">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required" for="amount">Amount</label>
										<div class="element-wrapper">
											<input id="amount" name="amount" type="text"
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-1">
									<div class="form-group">
										<label>TaxInc</label>
										<div class="element-wrapper">
											<input type="checkbox" class="taxInclusive"
												name="taxInclusive" />
										</div>

									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="tax">Tax</label>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber taxPercent"
												name="taxPercent" value="0" data-v-max="100" data-v-min="0"
												data-a-sign=" %" data-p-sign="s" data-m-dec="0">
										</div>

									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="taxAmount">Tax Amount</label>
										<div class="element-wrapper">
											<input type="hidden" class="form-control hiddenTaxAmount"
												name="hiddenTaxAmount" data-m-dec="0"> <input
												id="taxAmount" name="taxAmount" type="text"
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="totalAmount">Total</label>
										<div class="element-wrapper">
											<input id="totalAmount" name="totalAmount" type="text"
												class="form-control autonumber" data-v-min="0"
												data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 hidden" id="stockView">
									<div class="form-group">
										<label class="required" for="stockView">Stock</label>
										<div class="element-wrapper">
											<select name="stock" id="stock" class="form-control stock"
												data-placeholder="Search by Stock No. or Chassis No."><option
													value=""></option></select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="add-payment" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>
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
							<div class="col-md-4">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="form-control">
										<input type="checkbox" value="1" name="stockView"
											id="stockView" autocomplete="off"><label class="ml-5">Stock
											View</label>
									</div>
								</div>
							</div>
						</div>
						<!-- /.form:form -->
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-create-category"
								type="button">
								<i class="fa fa-fw fa-save"></i>Create
							</button>
							<button class="btn btn-primary" id="btn-close"
								data-dismiss="modal" type="button">
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
	<!-- modal accept -->
	<div class="modal fade" id="modal-create-invoice">
		<div class="modal-dialog modal-lg" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<div class="description-text pull-right transporter-name bold-text"></div>
					<h4 class="modal-title">Please Enter Invoice Details</h4>
				</div>
				<form id="form-transport-invoice-create">
					<input type="hidden" name="transporter" value="">
					<div class="modal-body">
						<div class="table-container" style="max-height: 400px">

							<table class="table margin-bottom-none">
								<thead>
									<tr>
										<th class="align-center" style="width: 10px"></th>
										<th class="align-center" style="width: 10px">#</th>
										<th class="align-center">Chassis No.</th>
										<th class="align-center">Maker</th>
										<th class="align-center">Model</th>
										<th class="align-center">Pickup Location</th>
										<th class="align-center">Drop Location</th>
										<th class="align-center input-amount">Amount</th>
										<th class="align-center">Tax</th>
										<th class="align-center">Tax %</th>
										<th class="align-center">TaxAmount</th>
										<th class="align-center">TotalAmount</th>
										<th class="align-center">Estimated Amount</th>
									</tr>
								</thead>
								<tbody>
									<tr class="clone hide">
										<td><button type="button" class="close btn-remove-item"
												aria-label="Close">
												<span aria-hidden="true"><i
													class="fa fa-fw fa-remove"></i></span>
											</button></td>
										<td class="sno"><span class="sno"></span><input
											type="hidden" name="rowData" /></td>
										<td class="align-center chassisNo"></td>
										<td class="align-center maker"></td>
										<td class="align-center model"></td>
										<td class="align-center pickupLocation"></td>
										<td class="align-center dropLocation"></td>
										<td class="align-center amount"><input name="amount"
											data-validation="number"
											class="form-control autonumber required" data-a-sign="¥ "
											data-m-dec="0" data-v-min="0" /></td>
										<td class="align-center"><input type="checkbox"
											class="taxInclusive" name="taxInclusive" /></td>
										<td class="align-center"><input type="text"
											class="form-control autonumber taxPercent required"
											name="taxPercent" value="10" data-v-max="100" data-v-min="0"
											data-a-sign=" %" data-p-sign="s" data-m-dec="0"> <span
											class="help-block"></span></td>
										<td class="align-center"><input type="hidden"
											class="form-control hiddenTaxAmount" name="hiddenTaxAmount"
											data-m-dec="0"> <input type="text"
											class="form-control autonumber taxAmount" name="taxAmount"
											data-a-sign="¥ " data-v-min="0" data-m-dec="0"> <span
											class="help-block"></span></td>
										<td class="align-center"><input type="text"
											class="form-control autonumber totalAmount"
											name="totalAmount" data-a-sign="¥ " data-v-min="0"
											data-m-dec="0" disabled> <span class="help-block"></span>
										</td>
										<td class="align-center actualAmt"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="summary-container">
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ " class="total pull-right"></span>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total Tax</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ "
										class="total-tax pull-right"></span>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total(Total + Tax)</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ "
										class="total-tax-included pull-right"></span>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-md-12">
									<div class="form-inline pull-right">
										<div class="form-group">
											<strong class="mr-5">Ref.No</strong> <input
												class="form-control" name="refNo" />

										</div>
										<div class="form-group ml-5">
											<strong class="mr-5">Due Date</strong> <input
												class="form-control datepicker" placeholder="dd-mm-yyyy"
												name="dueDate" readonly="readonly" />

										</div>
										<div class="form-group ml-5">
											<strong class="mr-5">Invoice Date</strong> <input
												class="form-control datepicker" placeholder="dd-mm-yyyy"
												name="invoiceDate" readonly="readonly" />

										</div>

									</div>
								</div>

							</div>
						</div>
					</div>
				</form>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-save-invoice"
							class="btn btn-primary">Create</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Model -->
</section>