<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<section class="content-header">
	<h1>Reserve Stock</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Reserved</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="alert alert-warning" id="alert-block-warning"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<sec:authorize
		access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER') or hasRole('ADMIN')"
		var="isAdminManager"></sec:authorize>
	<sec:authorize
		access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER')"
		var="isAccountFreeze"></sec:authorize>
	<div class="box box-solid">

		<div class="box-body">
			<div class="container-fluid ">
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
							<label>Reserved Date</label>
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



					<div class="btn-group pull-right">
						<button type="button" class="btn btn-info enable-on-select"
							id="btn-create-proforma" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-proforma" disabled="disabled">Create
							Porforma Invoice</button>
						<button type="submit" class="btn btn-dark" id="btn-unReserve"
							disabled="disabled">Un Reserve</button>
						<button type="submit" class="btn btn-warning enable-on-select"
							id="btn-create-shipping" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-shipping" disabled="disabled">Shipping
							Instruction</button>
						<button type="submit" class="btn btn-primary enable-on-select"
							id="btn-create-sales" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-sales" disabled="disabled">Sales
							order</button>
						<button type="submit" class="btn btn-info enable-on-select"
							data-backdrop="static" id="changeBiddingSalesPerson"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-change-sales-person" disabled="disabled">Change
							Sales Person</button>
					</div>
				</div>

				<div class="row form-group">
					<div class="col-md-1">
						<select id="table-filter-length" class="form-control input-sm"
							style="width: 80px">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<div class="form-group">
								<input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${isAdminManager}">
							<div class="col-md-3 form-inline">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="form-control">
										<input type="checkbox" id="showMine" name="showMine"
											class="form-control minimal"><label class="ml-5">Show
											All Reserved Vehicles</label>
									</div>
								</div>
							</div>
						</c:when>
					</c:choose>
					

					<div class="col-md-5">
						<div class="pull-right">
							<button class="btn btn-primary" type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
						</div>
					</div>
				</div>
			</div>

		<div style="text-align: center;">
		<div class="radio">
							<label><input class="minimal" type="radio"
								name="isBidding" value="-1" checked="checked" />&nbsp;&nbsp;All</label>&nbsp;&nbsp;&nbsp;&nbsp;
							<label><input class="minimal" type="radio"
								name="isBidding" value="1" />&nbsp;&nbsp;Bidding</label>&nbsp;&nbsp;&nbsp;&nbsp;
							<label><input class="minimal" type="radio"
								name="isBidding" value="0" />&nbsp;&nbsp;Stock</label>
						</div>
		</div>
			<c:choose>
				<c:when test="${isAdminManager}">
					<input type="hidden" value="1" name="isAdminManager">
				</c:when>
				<c:otherwise>
					<input type="hidden" value="0" name="isAdminManager">
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isAccountFreeze}">
					<input type="hidden" value="1" name="isAccountFreeze">
				</c:when>
				<c:otherwise>
					<input type="hidden" value="0" name="isAccountFreeze">
				</c:otherwise>
			</c:choose>
			<input type="hidden" value="" name="accountFreeze"> <input
				type="hidden" value="" name="accountFreezeCustomer">
			<div class="table-responsive">
				<table id="table-stock" class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th><input type="checkbox" id="select-all" /></th>
							<th>Stock No</th>
							<th>Chassis No</th>
							<th>Category</th>
							<th>Maker</th>
							<th>Model</th>
							<th>Dest Country/Port</th>
							<th>Reserve Price</th>
							<th>Reserve Date</th>
							<th>Reserved Ageing</th>
							<th>Reserved By</th>
							<th>Customer</th>
							<th>Selling Price</th>
							<th>Selling Price ($)</th>
							<th>Action</th>
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
					<jsp:include page="/WEB-INF/views/sales/createproformainvoice.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary "
							id="btn-generate-proforma-invoice">
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
						<button class="btn btn-primary" id="btn-save-shipping"
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
	<!-- Stock Modal -->
	<div class="modal fade" id="modal-stock-details">
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
		<!-- /.modal-dialog -->
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

	<!-- Model -->
	<div class="modal fade" id="modal-edit-reserve-price">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Reserve Details</h3>
				</div>
				<div class="modal-body">
					<form id="reseve-detail-form">
						<div class="container-fluid">
							<div class="row">
								<div class=" col-md-4">
									<label class="required">Customer</label>
									<div class="form-group">
										<select id="custId" name="custId" class="form-control select2"
											style="width: 100%;"
											data-placeholder="Search by Customer ID, Name, Email">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4">
									<label class="required">Currency</label> <select
										class="form-control select2-select readonly"
										data-placeholder="Select Currency" id="currency"
										name="currency">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
								<div class="col-md-4 ">
									<label class="required">Reserve Price</label>
									<div class="element-wrapper">
										<input type="text" id="reservePrice" name="reservePrice"
											class="form-control autonumber required" data-a-sign="¥ ">
										<input type="hidden" id="minimumSellingPrice"
											name="minimumSellingPrice"> <input type="hidden"
											id="minSellingPriceInDollar" name="minSellingPriceInDollar">
									</div>
									<span class="help-block"></span>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="savePrice" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Model -->

	<!-- /.modal Change sales person -->
	<div class="modal fade" id="modal-change-sales-person">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Change Sales Person</h4>
				</div>
				<div class="modal-body">
					<form id="change-sales-person-form">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<label class="required">Sales person</label>
									<div class="form-group">
										<select id="salesPersonId" name="salesPersonId"
											class="form-control select2" style="width: 100%;"
											data-placeholder="Search by Sales person">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-change-sales-person">
							<i class="fa fa-fw fa-save"></i> Save
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
	<div class="modal fade" id="add-sales-consigner" style="z-index: 1000000">
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
											<input type="text" id="salesConsigneeName" name="salesConsigneeName"
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
											<input type="text" id="salesConsigneeAddr" name="salesConsigneeAddr"
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
	<div class="modal fade" id="add-sales-ntfy-party" style="z-index: 1000000">
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
											<input type="text" id="salesNotifyPartyName" name="salesNotifyPartyName"
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
<script type="text/javascript">
	
</script>
<!--

//-->
</script>
