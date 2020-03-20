<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Storage & Photos</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li class="active">Storage & Photos</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block-success"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="alert alert-warning" id="alert-block-failure"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">

		<div class="box-body">
			<form id="storage-photos-form">
				<div class="container-fluid" id="storagePhotos">
					<fieldset>
						<input type="hidden" id="invoiceNo" name="invoiceNo"
							class="form-control storage-data" value="${invoiceNo}">
						<div class="row form-group">
							<!-- <div class="col-md-2">
								<div class="form-group">
									<label class="required">Forwarder</label><select id="remitter"
										name="remitter" class="form-control storage-data"
										data-placeholder="Select Remitter" style="width: 100%;">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-2">
								<label class="required">Invoice Type</label> <select
									id="invoiceTypeFilter"
									class="form-control storage-data select2" name="invoiceType"
									data-placeholder="Select Invoice Type">
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
								<div id="purchasedSupplierWrapper" class="supplirFilter">
									<label class="required">Supplier</label> <select
										name="supplier" id="supplier"
										class="form-control storage-data supplier select2"
										data-placeholder="Select Supplier">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
								<div id="transportSupplierWrapper" class="supplirFilter"
									style="display: none;">
									<label class="required">Supplier</label> <select
										name="transportSupplier" id="transportSupplier"
										class="form-control storage-data select2"
										data-placeholder="Select Supplier">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
								<div id="forwarderSupplierWrapper" class="supplirFilter"
									style="display: none;">
									<label class="required">Supplier</label> <select
										name="forwarderSupplier" id="forwarderSupplier"
										class="form-control storage-data select2"
										data-placeholder="Select Supplier">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
								<div id="genaralSupplierWrapper" class="supplirFilter"
									style="display: none;">
									<label class="required">Supplier</label> <select
										name="genaralSupplier" id="genaralSupplier"
										class="form-control storage-data select2"
										data-placeholder="Select Supplier">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
								<div id="inspectionCompanyWrapper" class="supplirFilter"
									style="display: none;">
									<label class="required">Inspection Company</label> <select
										name="inspectionCompany" id="inspectionCompany"
										class="form-control storage-data select2"
										data-placeholder="Select Inspection Company">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Currency</label> <select id="currency"
										name="currency" class="form-control storage-data"
										data-placeholder="Select currency" style="width: 100%;">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3 hidden" id="exchangeRateDiv">
								<div class="form-group">
									<label class="required">Exchange rate</label><select
										id="exchangeRate" name="exchangeRate"
										class="form-control storage-data"
										data-placeholder="Select Exchange Rate" style="width: 100%;">
										<option value="">Select Exchange Rate</option>
										<option value="1">Exchange Rate</option>
										<option value="2">Sales Exchange Rate</option>
										<option value="3">Special Exchange Rate</option>
										<option value="4">Others</option>
									</select><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2 hidden" id="exRate1">
								<div class="form-group">
									<label class="required">Exch.Rate</label><input
										class="form-control storage-data autonumber"
										name="exchangeRateValue" type="text" id="exchangeRateValue">
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Reference No</label><input
										class="form-control storage-data" name="refNo" id="refNo"
										type="text"><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Invoice Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text"
											class="form-control datepicker storage-data"
											name="invoiceDate" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Due Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text"
											class="form-control datepicker storage-data" name="dueDate"
											placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row form-group">

							<!-- <div class="col-md-1" id="upload-csv-div-btn">
								<div class="form-group ">
									<label></label>
									<div class="input-group mt-5">
										<button type="button" class="btn btn-primary" id="upload-csv">
											Upload Csv</button>
									</div>
								</div>
							</div> -->
							<div class="col-md-1 hidden" id="enter-data-div-btn">
								<div class="form-group ">
									<label></label>
									<div class="input-group mt-5">
										<button type="button" class="btn btn-primary" id="enter-data">
											Enter data</button>
									</div>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Type</label>
									<div class="element-wrapper">
										<select class="form-control select2-select"
											name="requested-type-filter" id="requested-type-filter"
											data-placeholder="Select Type" multiple>
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="element-wrapper">
										<button type="button" class="btn  btn-primary" id="showFields">Show</button>
										<button type="button" class="btn  btn-primary hidden"
											id="editFields">Clear</button>
									</div>
									<span class="help-block"></span>
								</div>
							</div>

							<!-- <div class="col-md-2 totalTaxCalc">
								<div class="form-group">
									<label class="required">Total W/O Tax</label><input
										class="form-control totalTaxCalc amount" id="totalWithoutTax"
										type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"
										readonly="readonly">
								</div>
							</div>
							<div class="col-md-2 totalTaxCalc">
								<div class="form-group">
									<label class="required">Total Tax</label><input
										class="form-control totalTaxCalc amount" id="totalTax"
										type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"
										readonly="readonly">
								</div>
							</div>
							<div class="col-md-2 totalTaxCalc">
								<div class="form-group">
									<label class="required">All Total</label><input
										class="form-control totalTaxCalc amount" id="allTotal"
										type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"
										readonly="readonly">
								</div>
							</div> -->
							<div class="col-md-2 pull-right">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="element-wrapper">
										<button type="button" class="btn  btn-primary" id="addNew"
											data-toggle="modal" data-target="#add-new-category">New
											Ledger</button>
									</div>
									<span class="help-block"></span>
								</div>

							</div>
						</div>
					</fieldset>
				</div>
				<div class="container-fluid">
					<div class="table-responsive hidden" id="payment-item-table">

					</div>
					<div class="table-clone hidden" id="table-cloning-element">
						<table class="table table-bordered table-striped"
							style="width: 200%; overflow: scroll;">
							<thead>
								<tr class="columnHead">
									<th bgcolor="#3c8dbc" class="align-center head-column">Payment
										Item</th>
								</tr>
								<tr class="header">
									<th><label>Action</label></th>
									<th>Stock/Chassis No</th>
								</tr>
							</thead>
							<tbody class="payment-row-clone-container">
								<tr class="clone-row hide">
									<td><div class="form-group div-width-120px">
											<button type="button"
												class="btn btn-primary btn-sm add-payment-row">
												<i class="fa fa-plus"></i>
											</button>
											<button type="button"
												class="btn btn-danger btn-sm delete-payment-row">
												<i class="fa fa-close"></i>
											</button>
										</div></td>
									<td><div class="form-group">
											<select name="stockNo" id="stockNo"
												class="form-control stockNo chargeData required"
												data-placeholder="Search by Stock No or Chassis No"><option
													value=""></option></select><span class="help-block"></span>
										</div></td>
								</tr>
								<tr class="first-row">
									<td><div class="form-group div-width-120px">
											<button type="button"
												class="btn btn-primary btn-sm add-payment-row"
												id="add-payment-row">
												<i class="fa fa-plus"></i>
											</button>
											<button type="button"
												class="btn btn-danger btn-sm delete-payment-row"
												id="delete-payment-row">
												<i class="fa fa-close"></i>
											</button>
										</div></td>
									<td><div class="form-group">
											<select name="stockNo" id="stockNo"
												class="form-control stockNo chargeData required"
												data-placeholder="Search by Stock No or Chassis No"><option
													value=""></option></select><span class="help-block"></span>
										</div></td>
								</tr>
							</tbody>
							<tfoot>
								<tr class=sum>
									<th colspan="2" style="text-align: right">Total W/O Tax</th>
									<th class="dt-right"><span
										class="autonumber amount pagetotal totalWithOutTax"
										data-a-sign="¥ " data-m-dec="0"></span></th>
								</tr>
								<tr class=taxtotal>
									<th colspan="2" style="text-align: right">Total Tax</th>
									<th class="dt-right"><span
										class="autonumber amount pagetotal taxTotal" data-a-sign="¥ "
										data-m-dec="0"></span></th>
								</tr>
								<tr class="grandTotal">
									<th colspan="2" style="text-align: right">Total:</th>
									<th class="dt-right"><span name="totalAmount"
										class="autonumber amount pagetotal totalAmount"
										data-a-sign="¥ " data-m-dec="0"></span></th>
								</tr>
							</tfoot>
						</table>
					</div>
					<div class="hidden" id="upload-csv-div">
						<table class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th colspan="4" bgcolor="#3c8dbc" class="align-center">Upload
										CSV</th>
								</tr>
								<tr>
									<th>Select Payment Type</th>
									<th>Tax Inc.</th>
									<th>Upload Csv</th>
									<th colspan="6"></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="width: 400px;">
										<div class="form-group">
											<select id="paymentFor" name="paymentFor"
												class="form-control" style="width: 100%;"
												data-placeholder="Select Payment Type">
												<option value=""></option>
												<option value="STORAGE">STORAGE</option>
												<option value="SHIPPING">SHIPPING</option>
												<option value="PHOTO">PHOTO</option>
												<option value="BL AMENDCOMBINE">BL AMEND/COMBINE</option>
												<option value="RADIATION">RADIATION</option>
												<option value="REPAIR">REPAIR</option>
												<option value="YARD HANDLING CHARGES">YARD HANDLING
													CHARGES (YHC)</option>
												<option value="INSPECTION">INSPECTION</option>
												<option value="TRANSPORT">TRANSPORT</option>
												<option value="FREIGHT">FREIGHT</option>
											</select> <span class="help-block"></span>
										</div>
									</td>
									<td style="width: 100px;"><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="taxCheck" name="taxCheck"
												class="form-control minimal" />
										</div> <label class="mt-10"></label></td>
									<td style="width: 400px;"><div class="form-group ">
											<div class="form-group ">
												<input type="file" id="csvUploaded" name="csvUploaded"
													data-directory="csvUploaded_dir" accept=".csv" /> <span
													class="help-block"></span>
											</div>
										</div></td>
									<td colspan="6"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="box-footer ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary"
							id="save-storagePhotos">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button type="reset" class="btn btn-primary "
							onclick="location.reload();" id="rest-form">Reset</button>
					</div>
				</div>
			</form>
		</div>
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
</section>