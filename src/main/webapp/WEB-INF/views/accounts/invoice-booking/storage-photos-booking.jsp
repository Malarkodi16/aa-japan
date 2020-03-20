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
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Forwarder</label><select id="remitter"
										name="remitter" class="form-control storage-data"
										data-placeholder="Select Remitter" style="width: 100%;">
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
							
							<div class="col-md-1" id="upload-csv-div-btn">
								<div class="form-group ">
									<label></label>
									<div class="input-group mt-5">
										<button type="button" class="btn btn-primary" id="upload-csv">
											Upload Csv</button>
									</div>
								</div>
							</div>
							<div class="col-md-1 hidden" id="enter-data-div-btn">
								<div class="form-group ">
									<label></label>
									<div class="input-group mt-5">
										<button type="button" class="btn btn-primary" id="enter-data">
											Enter data</button>
									</div>
								</div>
							</div>
							<div class="col-md-2 totalTaxCalc">
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
							</div>
						</div>
					</fieldset>
				</div>
				<div class="container-fluid">
					<div class="table-responsive" id="payment-item-table">
						<table class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th colspan="13" bgcolor="#3c8dbc" class="align-center">Payment
										Item</th>
								</tr>
								<tr>
									<th><label>Action</label></th>
									<th>Stock/Chassis No</th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1 pull right"
											style="width: 45px">
											<input type="checkbox" id="storageTaxCheck"
												name="storageTaxCheck" value="1"
												class="form-control minimal" />
										</div> <label class="mt-10">Storage</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="shippingTaxCheck"
												name="shippingTaxCheck" value="1"
												class="form-control minimal" />
										</div> <label class="mt-10">Shipping</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="photoTaxCheck"
												name="photoTaxCheck" value="1" class="form-control minimal" />
										</div> <label class="mt-10">Photo</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="blAmendCombineTaxCheck"
												name="blAmendCombineTaxCheck" value="1"
												class="form-control minimal" />
										</div> <label class="mt-10">Bl Amend/Combine</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="radiationTaxCheck"
												name="radiationTaxCheck" value="1"
												class="form-control minimal" />
										</div> <label class="mt-10">Radiation</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="repairTaxCheck"
												name="repairTaxCheck" value="1" class="form-control minimal" />
										</div> <label class="mt-10">Repair</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="yhcTaxCheck" name="yhcTaxCheck"
												value="1" class="form-control minimal" />
										</div> <label class="mt-10">Yard Handling Charges</label> (YHC)</th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="inspectionTaxCheck"
												name="inspectionTaxCheck" value="1"
												class="form-control minimal" />
										</div> <label class="mt-10">Inspection</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="transportTaxCheck" value="1"
												name="transportTaxCheck" class="form-control minimal" />
										</div> <label class="mt-10">Transport</label></th>
									<th><label>&nbsp;</label>
										<div class="form-control col-md-1" style="width: 45px">
											<input type="checkbox" id="freightTaxCheck" value="1"
												name="freightTaxCheck" class="form-control minimal" />
										</div> <label class="mt-10">Freight</label></th>
									<th><label>Remarks</label></th>
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
												class="form-control stockNo required"
												data-placeholder="Search by Stock No or Chassis No"><option
													value=""></option></select><span class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc" name="amount"
												id="amount" type="text" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0"><span class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="shippingCharges" id="shippingCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="photoCharges" id="photoCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-180px">
											<input class="form-control amount priceCalc"
												name="blAmendCombineCharges" id="blAmendCombineCharges"
												type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<!-- <td><div class="form-group">
											<input class="form-control amount" name="blCombineCharges"
												id="blCombineCharges" type="text" data-a-sign="¥ "
												data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td> -->
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="radiationCharges" id="radiationCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="repairCharges" id="repairCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-230px">
											<input class="form-control amount priceCalc"
												name="yardHandlingCharges" id="yardHandlingCharges"
												type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="inspectionCharges" id="inspectionCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="transportCharges" id="transportCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="freightCharges" id="freightCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><input class="form-control" name="remarks"
										id="remarks" type="text"></td>

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
												class="form-control stockNo required"
												data-placeholder="Search by Stock No or Chassis No"><option
													value=""></option></select><span class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc" name="amount"
												id="amount" type="text" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0"><span class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="shippingCharges" id="shippingCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="photoCharges" id="photoCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-180px">
											<input class="form-control amount priceCalc"
												name="blAmendCombineCharges" id="blAmendCombineCharges"
												type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<!-- <td><div class="form-group">
											<input class="form-control amount" name="blCombineCharges"
												id="blCombineCharges" type="text" data-a-sign="¥ "
												data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td> -->
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="radiationCharges" id="radiationCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="repairCharges" id="repairCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-230px">
											<input class="form-control amount priceCalc"
												name="yardHandlingCharges" id="yardHandlingCharges"
												type="text" data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="inspectionCharges" id="inspectionCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="transportCharges" id="transportCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control amount priceCalc"
												name="freightCharges" id="freightCharges" type="text"
												data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
												class="help-block"></span>
										</div></td>
									<td><div class="form-group div-width-120px">
											<input class="form-control" name="remarks" id="remarks"
												type="text">
										</div></td>

								</tr>
							</tbody>
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
</section>