<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize
	access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER') or hasRole('ADMIN')"
	var="isAdminManager"></sec:authorize>
<section class="content-header">
	<h1>Shipping Status</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Status</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Sales Status List</h3>
		</div>
		<div class="box-body">
			<div class="row form-group">
				<div class="col-md-3">
					<div class="form-group" id="date-form-group">
						<label>Reserve Date</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control pull-right"
								id="table-filter-reserve-date"
								placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
						</div>
						<!-- /.input group -->
					</div>
				</div>
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
					<div class="form-group" id="date-instruction-form-group">
						<label>Shipping Instruction Date</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control pull-right"
								id="table-filter-instruction-date"
								placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
						</div>
						<!-- /.input group -->
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Country</label> <select class="form-control"
							name="countryFilter" id="countryFilter"
							data-placeholder="Country Filter">
							<option value=""></option>
						</select>

					</div>
				</div>

			</div>
			<div class="row form-group">
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
										class="ml-5">All Shipping Status</label>

								</div>
							</div>
						</div>
					</c:when>
				</c:choose>
			</div>
			<!-- </div>     -->
		</div>
		<div class="container-fluid ">
			<div class="table-responsive">
				<table id="table-shipping-instruction"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0" style="width: 10px"><input
								type="checkbox" id="select-all" /></th>
							<th data-index="1" class="align-center">Request Date</th>
							<th data-index="2" class="align-center">Stock No.</th>
							<th data-index="3" class="align-center">Chassis No.</th>
							<th data-index="4" class="align-center">Customer</th>
							<th data-index="5" class="align-center">Consignee</th>
							<th data-index="6" class="align-center">Notify Party</th>
							<th data-index="7" class="align-center">Staff</th>
							<th data-index="8" class="align-center">Country</th>
							<th data-index="9" class="align-center">Destination Port</th>
							<th data-index="10" class="align-center">LC No.</th>
							<th data-index="11" class="align-center">Reserve Date</th>
							<th data-index="12" class="align-center">Vessel</th>
							<th data-index="13" class="align-center">ETD</th>
							<th data-index="14" class="align-center">ETA</th>
							<th data-index="15" class="align-center">Status</th>
							<th data-index="16" class="align-center">Remarks</th>
							<th data-index="17" class="align-center">DHL No.</th>
							<th data-index="18" class="align-center">Shipping
								Instruction Date</th>
							<th data-index="19" class="align-center">Purchase Price</th>
							<th data-index="20" style="width: 100px" class="align-center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- /.modal Shipping -->
	<div class="modal fade" id="modal-edit-shipping-instruction">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Shipping Instruction</h4>
				</div>
				<div class="modal-body">
					<jsp:include
						page="/WEB-INF/views/sales/editshippinginstruction.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary" id="btn-edit-shipping"
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
	
	<!-- edit customer -->
	
	<div class="modal fade" id="modal-change-customer">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Edit Customer</h3>
				</div>
				<div class="modal-body">
					<form id="customer-edit-form">
						<div class="container-fluid">
						<div class="row">
						<input type="hidden" id="id" name="id" />
						<div class="col-md-4">
						<div class="form-group">
							<label class="required">Customer</label><select name="customerId"
								id="customerCode" class="form-control select2 invoiceData"
								style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
						<span class="help-block"></span>
					</div>
							
							<div class="col-md-3">
								<div class="form-group ">
									<label class="required">Consignee</label>
									<div class="input-group">
										<div class="elemenr-wrapper" style="width: 80%;">
											<select name="editConsigneeId" id="editcFirstshippingName" 
												class="form-control select2-select shippingData valid-required "
												 data-placeholder="Select Consignee" style="width: 150px;">
												<option value=""></option>
											</select>
										</div>
										<div class="input-group-addon" title="Add Consignee"
											data-backdrop="static" data-keyboard="false"
											data-toggle="modal" data-target="#add-consigner" style="width: 20%;">
											<i class="fa fa-plus"></i>
										</div>
									</div>
									<span class="help-block"></span>
								</div>

							</div>
							
							<div class="col-md-3">
								<div class="form-group ">
									<label class="required">Notify Party</label>
									<div class="input-group">
										<div class="elemenr-wrapper" style="width: 80%;">
											<select name="editNotifypartyId" id="editnpFirstshippingName" 
												class="form-control select2-select shippingData"
												 data-placeholder="Select Notify Party" style="width: 150px;">
												<option value=""></option>
											</select>
										</div>
										<div class="input-group-addon" title="Add Notify Party"
											data-backdrop="static" data-keyboard="false"
											data-toggle="modal" data-target="#add-ntfy-party" style="width: 20%;">
											<i class="fa fa-plus"></i>
										</div>
									</div>
									<span class="help-block"></span>
								</div>

							</div>	
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="change-customer-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- add comsignee model -->
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
												class="form-control" placeholder="Address" required="required">
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
											<input type="text" id="notifyPartyName" name="notifyPartyName"
												class="form-control" placeholder="Notify Party Name"
												required="required">
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
	
	
	<!-- Stock Modal -->
	<div class="modal fade" id="modal-stock-details">
		<!-- /.modal-dialog -->
		<div class="modal-dialog modal-lg"
			style="min-width: 100%; margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Details</h4>
				</div>
				<div class="modal-body " id="modal-stock-details-body"></div>
			</div>
			<!-- /.modal-content -->
		</div>
	</div>
	<div id="cloneable-items">
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>
	<!-- /./. start Model -->
	<div class="modal fade" id="modal-shipping-user-details">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update user details</h3>
				</div>
				<div class="modal-body">
					<form id="user-details-form">
						<div class="container-fluid">
							<div class="row">
								<input type="hidden" name="stockNo">
								<div class="col-md-3">
									<label class="required">User</label>
									<div class="form-group">
										<input class="form-control required" name="shippingUser"
											id="shippingUser" type="text"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">User Id</label>
									<div class="form-group">
										<input class="form-control required" name="shippingId"
											id="shippingId" type="text"><span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Tel</label>
									<div class="form-group">
										<input class="form-control required shippingTel"
											name="shippingTel" id="shippingTel" type="text"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Hs Code</label>
									<div class="form-group">
										<input class="form-control required" name="hsCode" id="hsCode"
											type="text"><span class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="userDetails-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./. end Model /./-->

	<!-- /./.start modal Request from sales-->
	<div class="modal fade" id="modal-cancel-stock" tabindex="-1">
	
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Cancel Stock</h3>
				</div>
				<div class="modal-body">
					<input type="hidden" id="rowData" data-json="" value="">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="remarks">Remarks</label>
								<textarea class="form-control" id="remarks" name="remarks"
									data-a-sign="¥ " data-m-dec="0" placeholder="Enter Remarks"></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="save-remark-modal" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>