
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>LC Invoice List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>LC Management</span></li>
		<li><span>List Invoice</span></li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<input type="hidden" id="lcNo" value="${lcNo}">
	<div class="box box-solid">
		<div class="box-header">
			<%-- <div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>List</strong></a></li>
					<li><a href="${contextPath}/accounts/lc/list"><strong>Lc
								Applied</strong></a></li>
				</ul>
			</div> --%>
		</div>
		<div class="box-body">
			<div class="container-fluid" id="invoice-list-container">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Customer</label> <select class="form-control customer"
								id="custselectId" style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Bank</label> <select id="bankFilter"
								class="form-control select2" style="width: 100%;"
								data-placeholder="Search by Bank">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<label>Issue Date</label> <input class="form-control datepicker"
							name="issueDateFilter" placeholder="dd-mm-yyyy" readonly=""
							autocomplete="off">
					</div>
					<div class="col-md-2">
						<label>Expiry Date</label> <input class="form-control datepicker"
							name="expiryDateFilter" placeholder="dd-mm-yyyy" readonly=""
							autocomplete="off">
					</div>

				</div>
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
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

				<div style="text-align: center;">
					<label> <input name="radioReceivedFilter" type="radio"
						class="minimal" value="0" checked="checked"> LIST
					</label> <label class="ml-5"> <input name="radioReceivedFilter"
						type="radio" class="minimal" value="1"> LC APPLIED
					</label>
				</div>
				<div class="table-responsive">
					<table id="table-invoice-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">LC No</th>
								<th data-index="2" class="align-center">Customer Name</th>
								<th data-index="3" class="align-center">Issue Date</th>
								<th data-index="4" class="align-center">Expiry Date</th>
								<th data-index="5" class="align-center">Bank</th>
								<th data-index="6" class="align-center">Amount</th>
								<th data-index="7" class="align-center">Bill of ExchangeNo</th>
								<th data-index="8" class="align-center">Consignee</th>
								<th data-index="9" class="align-center">Consignee Address</th>
								<th data-index="10" class="align-center">Notify Party</th>
								<th data-index="11" class="align-center">Notify Address</th>
								<th data-index="12" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 	Modal -->
	<div class="modal fade" id="modalUpdateDHL">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update DHL</h4>
				</div>
				<div class="modal-body">
					<form action="#" id="formUpdateDHL">
						<div class="container-fluide">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label for="dhlNo" class="required">DHL No</label> <input
											class="form-control" id="dhlNo" name="dhlNo" /><span
											class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class=" pull-right">
						<button type="button" class="btn btn-primary" id="btn-save">Save
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- 	.model -->
	<div class="modal fade" id="modal-apply-lc">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Apply LC</h4>
				</div>
				<div class="modal-body">
					<form action="#" id="formLcApply">
						<div class="container-fluide">
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label for="methodOfProcess" class="required">Methods
											Of Process</label> <select class="form-control" id="methodOfProcess"
											name="methodOfProcess"
											data-placeholder="Select Methods Of Process">
											<option value=""></option>
											<option value="1">Negotiation Basis</option>
											<option value="2">Collection Basis</option>
										</select><span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="yenBank" class="required">Bank</label> <select
											class="form-control _required" id="yenBank" name="yenBank"
											data-placeholder="Select Bank">
											<option value=""></option>
										</select><span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class=" pull-right">
						<button type="button" class="btn btn-primary" id="btn-save">Save
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- 	.model -->
	<div class="modal fade" id="modal-lc-update" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update LC</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label>CONSIGNEE</label> <input name="consigneeName"
									class="form-control" style="width: 100%;" /> <span
									class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<div class="form-group">
									<label>NOTIFY PARTY</label> <input name="notifyPartyName"
										class="form-control select2" style="width: 100%;" /> <span
										class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<div class="form-group">
									<label>NOTIFY PARTY ADDRESS</label>
									<textarea class="form-control" name="npAddress"></textarea>
								</div>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>CONSIGNEE ADDRESS</label>
								<textarea class="form-control" name="cAddress"></textarea>
							</div>
						</div>
					</div>


					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label>SHIPPING TERMS</label>
								<textarea class="form-control" name="shippingTerms"></textarea>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>Beneficiary Certify</label>
								<textarea class="form-control beneficiaryCertify"
									name="beneficiaryCertify"></textarea>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>Emission Remarks</label>
								<textarea rows="6" class="form-control licenseDoc"
									name="licenseDoc"></textarea>
							</div>
						</div>

					</div>
				</div>
				<div class="modal-footer">
					<div class=" pull-right">
						<button type="button" class="btn btn-primary" id="btn-save-lc">Save
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- 	.model -->
	<div class="modal fade" id="modal-edit-lc">
		<div class="modal-dialog modal-lg" style="width: 90%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>

					<h4 class="modal-title">Edit LC</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid form-step" style="display: none">
						<form id="update-lc-form">

							<input type="hidden" name="lcInvoiceNo" value="" /> <input
								type="hidden" name="customerId" value="" /> <input
								type="hidden" name="consigneeName" value="" /> <input
								type="hidden" name="notifyPartyName" value="" />
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label>CONSIGNEE</label><input name="consignee"
											class="form-control" style="width: 100%;" /> <span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<div class="form-group">
											<label>NOTIFY PARTY</label> <input name="notifyParty"
												class="form-control" style="width: 100%;" /> <span
												class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<div class="form-group">
											<label>NOTIFY PARTY ADDRESS</label>
											<textarea class="form-control" name="npAddress"></textarea>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>CONSIGNEE ADDRESS</label>
										<textarea class="form-control" name="cAddress"></textarea>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<label for="lc-no" class="required">LC No</label> <input
											type="text" class="form-control _required" id="lc-no"
											placeholder="Enter LC No" name="lcNo"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="bank" class="required">Bank</label> <select
											class="form-control _required" id="bank" name="bankId"
											data-placeholder="Select Bank">
											<option value=""></option>
										</select><span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="validity" class="required">Issue Date</label> <input
											type="text" class="form-control datepicker _required"
											id="issueDate" placeholder="dd-MM-yyyy" name="issueDate"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="validity" class="required">Expiry Date</label> <input
											type="text" class="form-control datepicker _required"
											id="expiryDate" placeholder="dd-MM-yyyy" name="expiryDate"><span
											class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3">
									<div class="form-group">
										<label for="amount" class="required">Amount</label> <input
											type="text" class="form-control autonumber _required"
											id="amount" placeholder="Enter Amount" data-a-sign="¥ "
											data-m-dec="0" data-v-min="0" name="amount"><span
											class="help-block"></span>
									</div>
								</div>

							</div>
							<div class="row">

								<!-- <div class="col-md-2">
									<div class="form-group">
										<label>SHIPPING TERMS NAME</label>
										<textarea class="form-control" name="shippingTermsName"></textarea>
									</div>
								</div> -->

								<div class="col-md-2">
									<div class="form-group">
										<label>SHIPPING TERMS NAME</label> <select
											id="shippingTermsName" name="shippingTermsName"
											class="form-control pull-right"
											data-placeholder="Select Name">
											<option value=""></option>
										</select>
									</div>
								</div>

								<div class="col-md-3">
									<div class="form-group">
										<label>SHIPPING TERMS</label>
										<textarea class="form-control" id="shippingTerms"
											name="shippingTerms" placeholder="shipping Terms "></textarea>
									</div>
								</div>

								<div class="col-md-7">
									<div class="form-group">
										<label>Beneficiary Certify</label>
										<textarea class="form-control beneficiaryCertify"
											name="beneficiaryCertify"></textarea>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-group">
										<label>Emission Remarks</label>
										<textarea rows="6" class="form-control licenseDoc"
											name="licenseDoc"></textarea>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="container-fluid form-step" style="display: none">
						<form id="invoiceItemForm">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-3">
										<div class="form-group">
											<select id="searchProforma" name="searchProforma"
												class="form-control" data-placeholder="Search Proforma"
												style="width: 100%">
												<option value=""></option>
											</select>

										</div>
									</div>
									<div class="col-md-1">
										<button id="btn-add" type="button"
											class="btn btn-sm btn-primary" style="width: 75px">Add</button>
									</div>
									<div class="col-md-8">
										<h4 class="pull-right">
											<span>Total : </span><span
												class="autonumber totalAmountEntered" data-a-sign="¥ "
												data-m-dec="0" data-v-min="0">0</span>
										</h4>
									</div>
								</div>
							</div>
							<div class="container-fluid" id="itemContainer"></div>


						</form>
					</div>
					<!-- Circles which indicates the steps of the form: -->
					<div style="text-align: center; margin-top: 40px;">
						<span class="step"></span> <span class="step"></span>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn-previous-step" class="btn btn-primary">
						<i class="fa fa-fw fa-backward"></i> Previous
					</button>
					<button id="btn-next-step" class="btn btn-primary">
						<i class="fa fa-fw fa-forward"></i> Next
					</button>
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<div class="modal fade" id="modal-lc-item-update" tabindex="-1">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update Stock Details</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="invoiceId">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label>Maker</label> <input type="text" class="form-control"
									name="maker">
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>Model</label> <input type="text" class="form-control"
									name="model">
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>HS Code</label> <input type="text" class="form-control"
									name="hsCode">
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>SHIPPED PER (VESSEL)</label> <input type="text"
									class="form-control" name="perVessel">
							</div>
						</div>
					</div>
					<div class="row">

						<div class="col-md-3">
							<div class="form-group">
								<label>FROM</label> <input type="text" class="form-control"
									name="from">
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>TO</label> <input type="text" class="form-control"
									name="to">
							</div>
						</div>

						<div class="col-md-3">
							<div class="form-group">
								<label>BANK SENT DATE</label>
								<!-- Optional Field -->
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control datepicker"
										name="bankSentDate" placeholder="dd-mm-yyyy">
								</div>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>SHIPPING MARKS</label>
								<textarea class="form-control" name="shippingMarks"></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class=" pull-right">
						<button type="button" class="btn btn-primary"
							id="btn-save-lc-item">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<div id="cloneElements" class="hidden">
		<div class="container-fluid lc-items">
			<button type="button" class="close btn-remove-item"
				aria-label="Close" aria-invalid="false" data-flag="-1">
				<span aria-hidden="true"><i class="fa fa-fw fa-remove"></i></span>
			</button>
			<input type="hidden" name="stockNo" value="" /><input type="hidden"
				name="stockNo" value="" /><input type="hidden" name="scheduleText"
				value="" /> <input type="hidden" name="proformaInvoiceId" value="" />
			<input type="hidden" name="isCancelled" value="false" /> <input
				type="hidden" name="isNew" value="false" />
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label>Chassis No</label> <input type="text" name="chassisNo"
							class="form-control" disabled="disabled" /> <input type="text"
							name="proformaInvoiceNo" class="form-control mt-5" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Maker</label> <input type="text" name="maker"
							class="form-control" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Model</label> <input type="text" name="model"
							class="form-control" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>HS Code</label> <input type="text" name="hsCode"
							class="form-control" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Customer</label> <select name="customerId"
							class="form-control" style="width: 100%;"
							data-placeholder="Search by Customer ID, Name, Email">
							<option value=""></option>
						</select> <span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>SHIPPED PER (VESSEL)</label><input type="text"
							class="form-control" name="vessel"><span
							class="help-block"></span>
					</div>
				</div>
			</div>
			<div class="row">

				<div class="col-md-2">
					<div class="form-group">
						<label>FROM</label> <input type="text" class="form-control"
							name="from">
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>TO</label> <input type="text" class="form-control"
							name="to">
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>SAILING DATE</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control datepicker"
								name="sailingDate" placeholder="dd-mm-yyyy">
						</div>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>BANK SENT DATE</label>
						<!-- Optional Field -->
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control datepicker"
								name="bankSentDate" placeholder="dd-mm-yyyy">
						</div>
					</div>
				</div>

				<div class="col-md-2">
					<div class="form-group">
						<label>SHIPPING MARKS</label> <select id="shippingMarksName"
							name="shippingMarksName" class="form-control pull-right"
							data-placeholder="Select Name">
							<option value=""></option>
						</select>
					
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>SHIPPING MARKS TEXT</label> <textarea class="form-control" id="shippingMarks"
							name="shippingMarks"></textarea>
						
					</div>
				</div>

			</div>
			<div class="row">

				<div class="col-md-1">
					<div class="form-group">
						<label>FOB</label> <input type="text" id="fob" name="fob"
							class="form-control calculation autonumber" data-m-dec="0"
							data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
						<span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-1">
					<div class="form-group">
						<label>Insurance</label> <input type="text" id="insurance"
							name="insurance" class="form-control calculation autonumber"
							data-m-dec="0" data-a-sign="&yen; " style="width: 88px;" /> <span
							class="help-block"></span>
					</div>
				</div>

				<div class="col-md-1">
					<div class="form-group">
						<label>Freight</label> <input type="text" id="freight"
							name="freight" class="form-control calculation autonumber"
							data-m-dec="0" data-a-sign="&yen; " style="width: 88px;" /> <span
							class="help-block"></span>
					</div>
				</div>
				<div class="col-md-1">
					<div class="form-group">
						<label>Total</label> <input type="text" name="amountReceived"
							class="form-control autonumber" data-m-dec="0"
							data-a-sign="&yen; " style="width: 100px;" />
					</div>
				</div>


			</div>

			<hr>
		</div>

	</div>
	<div class="hide" id="clone-element-container">
		<div class="box-body no-padding bg-darkgray invoice-item-container">
			<div class="table-responsive order-item-container">
				<table class="table table-bordered" style="overflow-x: auto;">
					<thead>
						<tr>
							<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
							<th style="width: 60px" class="align-center bg-ghostwhite">Stock
								No</th>
							<th class="align-center bg-ghostwhite">Chassis No</th>
							<th class="align-center bg-ghostwhite">HS Code</th>
							<th class="align-center bg-ghostwhite">Maker</th>
							<th class="align-center bg-ghostwhite">Model</th>
							<th class="align-center bg-ghostwhite">Ins. Certificate No</th>
							<th class="align-center bg-ghostwhite">Export Certificate Nos</th>
							<th class="align-center bg-ghostwhite">FOB</th>
							<th class="align-center bg-ghostwhite">Insurance</th>
							<th class="align-center bg-ghostwhite">Inspection</th>
							<th class="align-center bg-ghostwhite">Total</th>
							<th class="align-center bg-ghostwhite">Customer</th>
							<th class="align-center bg-ghostwhite">Action</th>
						</tr>
					</thead>
					<tbody>
						<tr class="hide clone-row">
							<td class="align-center s-no"><input type="checkbox"
								name="selectBox" /><input type="hidden" name="maker" /><input
								type="hidden" name="model" /><input type="hidden" name="hsCode" /><input
								type="hidden" name="invoiceId" value="" /> <input type="hidden"
								name="shippingTerms" /><input type="hidden"
								name="shippingMarks" /> <input type="hidden" name="perVessel" />
								<input type="hidden" name="from" /> <input type="hidden"
								name="to" /> <input type="hidden" name="sailingDate" /> <input
								type="hidden" name="schedule" /></td>
							<td class="align-center stockNo"></td>
							<td class="align-center chassisNo"></td>
							<td class="align-center hsCode"></td>
							<td class="align-center maker"></td>
							<td class="align-center model"></td>
							<td class="align-center inspectionCertificateNo"></td>
							<td class="align-center exportCertificateNo"></td>
							<td class="align-center fob"><span class="autonumber"
								data-a-sign="&yen; " data-m-dec="0"></span></td>
							<td class="align-center insurance"><span class="autonumber"
								data-a-sign="&yen; " data-m-dec="0"></span></td>
					     	<td class="align-center freight"><span class="autonumber"
								data-a-sign="&yen; " data-m-dec="0"></span></td>
							<td class="dt-right amount"><span class="autonumber"
								data-a-sign="&yen; " data-m-dec="0"></span></td>
							<td class="align-center customerId"></td>
							<td class="align-center action"><a href="#"
								class="btn btn-primary btn-xs" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-lc-item-update"><i
									class="fa fa-fw fa-pencil"></i></a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</section>
