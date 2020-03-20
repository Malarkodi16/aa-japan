<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Inspection Booking</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking &amp;
				Approval</span></li>
		<li><span><em class=""></em>Booking</span></li>
		<li class="active">Inspection</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block-success"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<jsp:include
		page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid" id="inspection-invoice-create">
				<div class="table-responsive">
					<!-- payment inspection show/search inputs -->
					<div class="row form-group">
						<div class="col-md-2">
									<div class="form-group">
										<label>Inspection</label> <select id="table-filter-inspection"
											class="form-control pull-right"
											data-placeholder="Select Inspection Company">
											<option value=""></option>
										</select>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group" id="date-form-group">
										<label>Purchased Date</label>
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<input type="text" class="form-control pull-right"
												id="table-filter-purchased-date"
												placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
										</div>
									</div>
								</div>
								<div class="col-md-3">
							<div class="form-group" id="inspection-sent-date-form-group">
								<label>Inspection Sent Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-inspection-sent-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
								</div>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="inspection-date-form-group">
								<label>Inspection Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-inspection-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
								</div>
							</div>
						</div>
					</div>
					<div class="row form-group">
					
						<div class="col-md-3">
							<div class="form-group" id="issue-date-form-group">
								<label>Issue Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-issue-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
								</div>
							</div>
						</div>
					
					</div>
					<div class="row form-group">
					
					<div class="col-md-1 form-inline">
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
						
								<div class="col-md-8">
								<div class="pull-right">
									<button type="button" class="btn btn-primary pull-right"
										id="approvePayment" data-backdrop="static"
										data-keyboard="false" data-toggle="modal"
										data-target="#modal-create-invoice">
										<i class="fa fa-fw fa-file-pdf-o"></i>Book Payment
									</button>
								</div>
					</div>
					</div>
					<table id="table-inspection-booking"
						class="table table-bordered table-striped" style="width: 100%;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Inspection Sent
									Date</th>
								<th data-index="2" class="align-center">Purchase Date</th>
								<th data-index="3" class="align-center">Chassis No</th>
								<th data-index="4" class="align-center">Country</th>
								<th data-index="5" class="align-center">Company</th>
								<th data-index="6" class="align-center">Inspection Date</th>
								<th data-index="7" class="align-center">Document Status</th>
								<th data-index="8" class="align-center">Document Sent Date</th>
								<th data-index="9" class="align-center">Status</th>
								<th data-index="10" class="align-center">Certificate No</th>
								<th data-index="11" class="align-center">Issued Date</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- modal accept -->
	<div class="modal fade" id="modal-create-invoice">
		<div class="modal-dialog modal-lg" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<div
						class="description-text pull-right inspectionCompany bold-text"></div>
					<h4 class="modal-title">Please Enter Invoice Details</h4>
				</div>
				<form id="form-inspection-invoice-create">
					<input type="hidden" name="inspectionCompanyId" value="">
					<input type="hidden" name="inspectionCompany" value="">
					<div class="modal-body">
						<div class="table-container" style="max-height: 400px">

							<table class="table margin-bottom-none">
								<thead>
									<tr>
										<th class="align-center" style="width: 75px"></th>
										<th class="align-center" style="width: 10px">#</th>
										<th class="align-center">Chassis No.</th>
										<th class="align-center">Maker</th>
										<th class="align-center">Model</th>
										<th class="align-center">Certificate No</th>
										<th class="align-center">Issued Date</th>
										<th class="align-center input-amount">Amount</th>
										<th class="align-center">Tax</th>
										<th class="align-center">Tax %</th>
										<th class="align-center">TaxAmount</th>
										<th class="align-center">TotalAmount</th>
									</tr>
								</thead>
								<tbody>
									<tr class="clone hide">
										<td><button type="button" class="add btn-add-item"
												aria-label="Close">
												<span aria-hidden="true"><i class="fa fa-plus"></i></span>
											</button>
											<button type="button" class="close btn-remove-item"
												aria-label="Close">
												<span aria-hidden="true"><i
													class="fa fa-fw fa-remove"></i></span>
											</button></td>
										<td class="sno"><span class="sno"></span><input
											type="hidden" name="rowData" /></td>
										<td class="align-center chassisNo"></td>
										<td class="align-center maker"></td>
										<td class="align-center model"></td>
										<td class="align-center certificateNo"></td>
										<td class="align-center dateOfIssue"></td>
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
									</tr>

									<tr class="clone-cancel-stock hide">
										<td><button type="button" class="add btn-add-item"
												aria-label="Close">
												<span aria-hidden="true"><i class="fa fa-plus"></i></span>
											</button>
											<button type="button" class="close btn-remove-item"
												aria-label="Close">
												<span aria-hidden="true"><i
													class="fa fa-fw fa-remove"></i></span>
											</button></td>
										<td class="sno"><span class="sno"></span><input
											type="hidden" name="rowData" /></td>
										<td class="align-center chassisNo"><div
												class="form-group">
												<select name="stockNo" id="stockNo"
													class="form-control stockNo required"
													data-placeholder="Search by Stock No or Chassis No"><option
														value=""></option></select><span class="help-block"></span>
											</div></td>
										<td class="align-center maker"></td>
										<td class="align-center model"></td>
										<td class="align-center certificateNo"></td>
										<td class="align-center dateOfIssue"></td>
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