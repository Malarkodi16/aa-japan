
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>LC Invoice Create</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>LC Management</span></li>
		<li class="active">Create Invoice</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header">
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<select id="custId" class="form-control select2"
							style="width: 100%;"
							data-placeholder="Search by Customer ID, Name, Email">
							<option value=""></option>
						</select> <span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-2">
					<button type="button" class="btn btn-primary" type="button"
						id="btn-searchData" style="width: 80px;">Search</button>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div class="container-fluid" id="invoice-list-container">
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
					<div class="col-md-2 form-inline pull-right">
						<button type="button" class="btn btn-block btn-primary"
							type="button" id="btn-create-lc" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-lc">Create LC</button>
					</div>
				</div>

				<div class="table-responsive">
					<table id="table-invoice-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Invoice No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">HS Code</th>
								<th data-index="4" class="align-center">Maker/Model</th>
								<th data-index="5" class="align-center">Year</th>
								<th data-index="5" class="align-center">Ins. Certificate No</th>
								<th data-index="5" class="align-center">Export Certificate No</th>
								<th data-index="5" class="align-center">FOB</th>
								<th data-index="5" class="align-center">Insurance</th>
								<th data-index="5" class="align-center">Inspection</th>
								<th data-index="5" class="align-center">Freight</th>
								<th data-index="6" class="align-center">Amount</th>

							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="12"></th>
								<th><span class="autonumber" data-a-sign="¥ "
									data-m-dec="0" data-v-min="0"></span></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 	.model -->
	<div class="modal fade" id="modal-create-lc">
		<div class="modal-dialog modal-lg" style="width: 90%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>

					<h4 class="modal-title">Create LC</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid form-step" style="display: none">
						<form id="update-lc-form">
							<input type="hidden" name="proformaInvoiceId" value="" /> <input
								type="hidden" name="customerId" value="" /> <input
								type="hidden" name="consigneeName" value="" /> <input
								type="hidden" name="notifyPartyName" value="" />
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label>CONSIGNEE</label><select name="consignee"
											class="form-control select2" style="width: 100%;"
											data-placeholder="Select Consignee">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<div class="form-group">
											<label>NOTIFY PARTY</label> <select name="notifyParty"
												class="form-control select2" style="width: 100%;"
												data-placeholder="Select Notify Party">
												<option value=""></option>
											</select> <span class="help-block"></span>
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
									<div class="col-md-12">
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
					<button id="btn-previous-step" class="btn btn-primary"
						onclick="containerAllcationStepNextPrev(-1)">
						<i class="fa fa-fw fa-backward"></i> Previous
					</button>
					<button id="btn-next-step" class="btn btn-primary"
						onclick="containerAllcationStepNextPrev(1)">
						<i class="fa fa-fw fa-forward"></i> Next
					</button>
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Create
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
	<div id="cloneElements" class="hidden">
		<div class="container-fluid lc-items">
			<input type="hidden" name="stockNo" value="" />
	
			 <input type="hidden"
				name="scheduleText" value="" /><input type="hidden"
				name="proformaInvoiceId" value="" />
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label>Chassis No</label> <input type="text" name="chassisNo"
							class="form-control" disabled="disabled" />
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
						<label>SHIPPING MARKS</label> <select id="shippingMarksName"
							name="shippingMarksName" class="form-control pull-right"
							data-placeholder="Select Name">
							<option value=""></option>
						</select>
						<textarea class="form-control" id="shippingMarks"
							name="shippingMarks"></textarea>
					</div>
				</div>
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

				<!-- <div class="col-md-2">
					<div class="form-group">
						<label>Amount Received</label> <input type="text"
							name="amountReceived" class="form-control autonumber"
							data-a-sign="¥ " data-m-dec="0" data-v-min="0"><span
							class="help-block"></span>
					</div>
				</div> -->
			</div>
			<hr>
		</div>

	</div>
	<!-- /.model -->
</section>
