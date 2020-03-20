<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize
	access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER') or hasRole('ADMIN')"
	var="isAdminManager"></sec:authorize>
<section class="content-header">
	<h1>Sales Order</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Sales Order</li>
	</ol>
</section>

<!-- sales order invoice-->


<section class="content">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Sales Order Invoice List</h3>
		</div>
		<div class="box-body">
			<input type="hidden" name="screenNameFlag" value="${screenNameFlag}" />
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
								placeholder="dd-mm-yyyy - dd-mm-yyyy">
						</div>
						<!-- /.input group -->
					</div>
				</div>
				<div class="col-md-6">
					<div class="btn-group pull-right">
						<button type="submit" class="btn btn-warning enable-on-select"
							id="btn-create-shipping" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-shipping-stock" disabled="disabled">Shipping
							Instruction</button>
					</div>
				</div>



			</div>

			<!-- sales invoice show/search inputs -->
			<div class="container-fluid">
				<div class="row">
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
											class="ml-5">All Sales Order</label>

									</div>
								</div>
							</div>
						</c:when>
					</c:choose>
				</div>
			</div>

			<div style="text-align: center;">
				<label> <input name="radioShowTable" type="radio"
					class="minimal" value="0" checked> Stock Wise
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="1"> Invoice Wise
				</label>

			</div>
			<!-- datatable -->

			<div class="container-fluid" id="sales-order-invoice-list">

				<c:choose>
					<c:when test="${isAdminManager}">
						<input type="hidden" value="1" name="isAdminManager">
					</c:when>
					<c:otherwise>
						<input type="hidden" value="0" name="isAdminManager">
					</c:otherwise>
				</c:choose>


				<div class="table-responsive" id="purshase-stock-container">
					<table id="table-sales-order-stock"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Invoice No.</th>
								<th data-index="2" class="align-center">Stock No</th>
								<th data-index="3" class="align-center">Chassis No</th>
								<th data-index="4" class="align-center">Maker</th>
								<th data-index="5" class="align-center">Model</th>
								<th data-index="6" class="align-center">Customer</th>
								<th data-index="7" class="align-center">Consignee</th>
								<th data-index="8" class="align-center">Notify Party</th>
								<th data-index="9" class="align-center">Ordered By</th>
								<th data-index="10" class="align-center">Total</th>
								<th data-index="11" class="align-center">Status</th>
								<th data-index="12" class="align-center">Shipping Status</th>
								<th data-index="13" class="align-center">Sold Date</th>
								<th data-index="14" class="align-center">Vessal Name</th>
								<th data-index="15" class="align-center">ETD</th>
								<th data-index="16" class="align-center">ETA</th>


							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>


				<div class="table-responsive hidden"
					id="purshase-stock-invoice-container">
					<table id="table-sales-order-invoice"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Invoice No.</th>
								<th data-index="2" class="align-center">Customer</th>
								<th data-index="3" class="align-center">Consignee</th>
								<th data-index="4" class="align-center">Notify Party</th>
								<th data-index="5" class="align-center">Payment Type</th>
								<th data-index="6" class="align-center">Ordered By</th>
								<th data-index="7" class="align-center">Grand Total</th>
								<th data-index="8" class="align-center">Sold Date</th>
								<th data-index="9" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>




			</div>
		</div>
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


	<!-- Modal -->

	<div id="clone-container">
		<div id="sales-order-invoice-rearrange" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="salesOrderinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">Maker</th>
								<th data-index="4" class="align-center">Model</th>
								<th data-index="5" class="align-center">Total</th>
								<th data-index="9" class="align-center">Status</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="align-center s-no"><input type="checkbox"
									name="selectBox" /></td>
								<td class="align-center stockNo"><input type="hidden"
									name="stockNo" value="" /><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="dt-right total"><span class="autonumber"
									data-v-min="0" data-m-dec="0"></span></td>
								<td class="align-center status"><span class="label"></span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal Shipping -->
	<div class="modal fade" id="modal-create-shipping-stock">
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
						<button class="btn btn-primary" id="btn-save-shipping-inst"
							type="button">
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


</section>