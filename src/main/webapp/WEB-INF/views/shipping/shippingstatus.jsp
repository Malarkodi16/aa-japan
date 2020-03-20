<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Shipping Status</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Shipping Status</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<!-- <div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			table start
			<div class="container-fluid" id="shipping-requested">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Forwarder</label><select id="forwarderFilter"
								class="form-control select2 arrangeData" name="forwarder"
								style="width: 100%;" data-placeholder="All">

							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Country</label><select
								class="form-control country-dropdown"
								id="country-filter-from-sales" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Port</label><select class="form-control"
								id="port-filter-from-sales" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Type</label><select id="shipmentTypeFilter"
								class="form-control data-to-save select2" name="shippingType"
								data-placeholder="All">
								<option value=""></option>
								<option value="1">RORO</option>
								<option value="2">CONTAINER</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>&nbsp;</label>
							<div>
								<button type="button" class="btn btn-primary form-control"
									type="button" id="btn-searchData" style="width: 80px;">Search</button>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-shipping-requested"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Ship and shipping company</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<table id="table-shipping-requested"
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
								<th data-index="7" class="align-center">Country</th>
								<th data-index="8" class="align-center">Destination Port</th>
								<th data-index="9" class="align-center">LC No.</th>
								<th data-index="10" class="align-center">Reserve Date</th>
								<th data-index="11" class="align-center">Vessel</th>
								<th data-index="12" class="align-center">ETD</th>
								<th data-index="13" class="align-center">ETA</th>
								<th data-index="14" class="align-center">Status</th>
								<th data-index="15" class="align-center">Remarks</th>
								<th data-index="16" class="align-center">DHL No.</th>
								<th data-index="17" class="align-center">Shipping
									Instruction Date</th>
								<th data-index="18" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div> -->
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Shipping Status List</h3>
		</div>
		<div class="box-body">
			<%-- <div class="row form-group">
				<div class="col-md-3">
					 <div class="form-group" id="date-form-group">
						<label>Reserve Date</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control pull-right"
								id="table-filter-shipping-date"
								placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
						</div>
						<!-- /.input group -->
					</div>
				</div>
				<c:choose>
					<c:when test="${isAdminManager}">
						<div class="row form-group">
							<div class="col-md-3 form-inline pull-left">
								<div class="has-feedback pull-left">
									<input type="checkbox" id="showMine" name="showMine"><label
										class="ml-5">Own Sales Order Only</label>

								</div>
							</div>
						</div>
					</c:when>
				</c:choose> 
			</div> --%>
			<!-- </div>     -->
		</div>
		<div class="container-fluid ">
			<div class="row form-group">
				<div class="col-md-2">
					<div class="form-group">
						<label>Country</label> <select class="form-control"
							name="countryFilter" id="countryFilter"
							data-placeholder="Country Filter">
							<option value=""></option>
						</select>

					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Staff</label> <select class="form-control"
							name="staffFilter" id="staffFilter"
							data-placeholder="Staff Filter">
							<option value=""></option>
						</select>

					</div>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-md-1 form-inline">
					<div class="pull-left">
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
						 <input type="text" id="table-filter-search"
							class="form-control" placeholder="Search by keyword"> <span
							class="glyphicon glyphicon-search form-control-feedback"></span>
					</div>

				</div>

			</div>
			<input type="hidden" name="screenNameFlag" value="${screenNameFlag}" />
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
							<th data-index="20" class="align-center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
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
	<div id="shipping-requested-details-view" class="hidden">
		<div class="container-fluid detail-view">
			<div class="box-body no-padding bg-darkgray">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 10px">#</th>
							<th>Stock No.</th>
							<th>Chassis No.</th>
							<th>Shipment Type</th>
							<th>Forwarder</th>
							<th>ETD</th>
							<th>ETA</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody>
						<tr class="clone-row hide">
							<td class="s-no"></td>
							<td class="stockNo"></td>
							<td class="chassisNo"></td>
							<td class="shipmentType"></td>
							<td class="forwarder"></td>
							<td class="etd"></td>
							<td class="eta"></td>
							<td class="status"><span class="label"></span></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</section>
