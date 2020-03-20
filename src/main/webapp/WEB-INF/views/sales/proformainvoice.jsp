<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize
	access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER') or hasRole('ADMIN')"
	var="isAdminManager"></sec:authorize>
<section class="content-header">
	<h1>Proforma Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Proforma Invoice</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid" id="proforma-invoice-details">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Customer</label> <select id="custselectId"
								class="form-control select2" style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Invoice Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-invoice-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
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
					<c:choose>
						<c:when test="${isAdminManager}">
							<div class="row form-group">
								<div class="col-md-3 form-inline pull-left">
									<div class="has-feedback pull-left">
										<input type="checkbox" id="showMine" name="showMine"><label
											class="ml-5">All Proforma Issued Vehicles</label>

									</div>
								</div>
							</div>
						</c:when>
					</c:choose>

				</div>

				<c:choose>
					<c:when test="${isAdminManager}">
						<input type="hidden" value="1" name="isAdminManager">
					</c:when>
					<c:otherwise>
						<input type="hidden" value="0" name="isAdminManager">
					</c:otherwise>
				</c:choose>
				<div class="table-responsive">
					<table id="table-proformainvoice"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Created Date</th>
								<th data-index="2" class="align-center">Invoice No.</th>
								<th data-index="3" class="align-center">Customer</th>
								<th data-index="4" class="align-center">Consignee</th>
								<th data-index="5" class="align-center">Notify Party</th>
								<th data-index="6" class="align-center">Invoice by</th>
								<th data-index="7" class="align-center">Payment Type</th>
								<th data-index="8" class="align-center" style="width: 100px">Total</th>
								<th data-index="9" class="align-center" style="width: 150px">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Model Create Proforma Invoice -->
	<div class="modal fade" id="modal-create-proforma">
		<div class="modal-dialog modal-lg" style="width: 90%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Generate Porforma Invoice</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="invoiceId" />
					<jsp:include page="/WEB-INF/views/sales/editproformainvoice.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-save-proforma-invoice">
							<i class="fa fa-fw fa-save"></i>Save
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
	<!-- /.modal Create Sales Invoice -->
	<div class="modal fade" id="modal-create-sales">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Generate Sales Invoice</h4>
				</div>
				<div class="modal-body">
					<jsp:include page="/WEB-INF/views/sales/createsalesorder.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-generate-sales-invoice">
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
	<!-- Model download Invoice -->
	<div class="modal fade" id="modal-download-invoice">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Select Bank</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="invoiceId" />
					<div class="form-group row">
						<div class="col-md-6">
							<label>Currency</label>
							<div class="form-group">
								<select name="currency1" id="currency1"
									class="form-control required select2-select currency"
									data-placeholder="Select Curency"><option value=""></option></select><span
									class="help-block"></span>
							</div>
						</div>
						<div id="bankOne" class="col-md-6 hidden">
							<label>Bank</label>
							<div class="form-group">
								<select name="bank1" id="bank1"
									class="form-control required select2-select bank"
									data-placeholder="Select Bank"><option value=""></option></select><span
									class="help-block"></span>
							</div>
						</div>
					</div>
					<div class="form-group row">
						<div class="col-md-6">
							<label>Currency</label>
							<div class="form-group">
								<select name="currency2" id="currency2"
									class="form-control required select2-select currency"
									data-placeholder="Select Curency"><option value=""></option></select><span
									class="help-block"></span>
							</div>
						</div>
						<div id="bankTwo" class="col-md-6 hidden">
							<label>Bank</label>
							<div class="form-group">
								<select name="bank2" id="bank2"
									class="form-control required select2-select bank"
									data-placeholder="Select Bank"><option value=""></option></select><span
									class="help-block"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-download-invoice">
							<i class="fa fa-fw fa-save"></i>Save
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

	<!-- /.modal Shipping -->
	<div class="modal fade" id="modal-create-shipping">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create Shipping Instruction</h4>
				</div>
				<div class="modal-body">
					<jsp:include
						page="/WEB-INF/views/sales/createshippinginstruction.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-save-shipping">
							<i class="fa fa-fw fa-save"></i>Save
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
	<div class="modal fade" id="add-consigner" style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Consignee</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formAddConsignee">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Consignee Name</label>
										<div class="element-wrapper">
											<input type="text" id="consigneeName" name="consigneeName"
												class="form-control" placeholder="Consignee Name"
												required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="consigneeAddr" name="consigneeAddr"
												class="form-control" placeholder="Address"
												required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-consig" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-consig" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Notify Party Model -->
	<div class="modal fade" id="add-ntfy-party" style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Notify Party</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formAddNotifyParty">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Notify Party Name</label>
										<div class="element-wrapper">
											<input type="text" id="notifyPartyName"
												name="notifyPartyName" class="form-control"
												placeholder="Notify Party Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="notifyPartyAddr"
												name="notifyPartyAddr" class="form-control"
												placeholder="Address" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-NP" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-NP" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>

	<!-- add comsignee model -->
	<div class="modal fade" id="add-sales-consigner"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Consignee</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formSalesAddConsignee">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Consignee Name</label>
										<div class="element-wrapper">
											<input type="text" id="salesConsigneeName"
												name="salesConsigneeName" class="form-control"
												placeholder="Consignee Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="salesConsigneeAddr"
												name="salesConsigneeAddr" class="form-control"
												placeholder="Address" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-sales-consig" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-sales-consig" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Notify Party Model -->
	<div class="modal fade" id="add-sales-ntfy-party"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Notify Party</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formSalesAddNotifyParty">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Notify Party Name</label>
										<div class="element-wrapper">
											<input type="text" id="salesNotifyPartyName"
												name="salesNotifyPartyName" class="form-control"
												placeholder="Notify Party Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="salesNotifyPartyAddr"
												name="salesNotifyPartyAddr" class="form-control"
												placeholder="Address" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-sales-NP" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-sales-NP" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>

	<!-- add comsignee model -->
	<div class="modal fade" id="add-porfoma-consigner"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Consignee</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formPorfomaAddConsignee">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Consignee Name</label>
										<div class="element-wrapper">
											<input type="text" id="porfomaConsigneeName"
												name="porfomaConsigneeName" class="form-control"
												placeholder="Consignee Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="porfomaConsigneeAddr"
												name="porfomaConsigneeAddr" class="form-control"
												placeholder="Address" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-porfoma-consig" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-porfoma-consig" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Notify Party Model -->
	<div class="modal fade" id="add-porfoma-ntfy-party"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Add Notify Party</h3>
				</div>
				<div class="modal-body">
					<form action="#" id="formPorfomaAddNotifyParty">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Notify Party Name</label>
										<div class="element-wrapper">
											<input type="text" id="porfomaNotifyPartyName"
												name="porfomaNotifyPartyName" class="form-control"
												placeholder="Notify Party Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Address</label>
										<div class="element-wrapper">
											<input type="text" id="porfomasNotifyPartyAddr"
												name="porfomaNotifyPartyAddr" class="form-control"
												placeholder="Address" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>

						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-porfoma-NP" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Add
					</button>
					<button id="btn-close-porfoma-NP" data-dismiss="modal"
						class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
		<!-- /.modal-dialog -->
	</div>

</section>
