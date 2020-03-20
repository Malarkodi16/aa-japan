<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Shipping</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Shipping</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li><a href="${contextPath}/shipping/management/available"><strong>Available
								For Shipping</strong></a></li>
					<li><a href="${contextPath}/shipping/management/requested"><strong>Shipping
								Requested</strong></a></li>
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>Shipping
								Container</strong></a></li>
					<li><a href="${contextPath}/shipping/management/roro"><strong>Shipping
								RORO</strong></a></li>
				</ul>
			</div>
		</div>

		<div class="box-body">

			<!-- table start -->
			<div style="text-align: center;" class="form-group"
				id="container-shipment-filter">
				<label> <input name="containerAllcationStatus" type="radio"
					class="minimal" value="0" checked>&nbsp;&nbsp;Container
					Requested
				</label> <label class="ml-5"> <input name="containerAllcationStatus"
					type="radio" class="minimal" value="1">&nbsp;&nbsp;Container
					Confirmed
				</label> <label class="ml-5"> <input name="containerAllcationStatus"
					type="radio" class="minimal" value="2">&nbsp;&nbsp;Vessel
					Confirmed
				</label>
			</div>
			<div class="container-fluid" id="shipping-container">
				<div class="row form-group">
					<div class="col-md-1 form-inline">
						<div class="pull-left">
							<select id="table-shipping-container-filter-length"
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
							<input type="text" id="table-shipping-container-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>

					</div>
				</div>
				<div class="table-responsive">
					<table id="table-shipping-container"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Ship and Shipping Company</th>
								<th>Shipping Company Number</th>
								<th>Ship Number</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
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
		<div id="shipping-container-allocation-table-allocatted" class="hide">
			<div class="box-body no-padding clone-element">
				<ul class="list-group" id="item-container">
					<li class="list-group-item clone-row hide" data-json=""><span
						class="chassisNo"></span><span class="pull-right action"><a
							href="#" class="ml-5 btn btn-danger btn-xs btn-remove"><i
								class="fa fa-fw fa-remove"></i>Remove</a></span></li>
				</ul>
			</div>
		</div>
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
		<div id="modal-arrange-shippment-item" class="hidden"></div>
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>

	<div id="modal-arrange-shippment-item-available" class="hidden">
		<div class="container-fluid clone-item item">
			<input type="hidden" name="shippingInstructionData" value="" />
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label>Stock No.</label> <input type="text" class="form-control"
							name="stockNo" disabled>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Chassis No.</label> <input type="text" class="form-control"
							name="chassisNo" disabled>
					</div>
				</div>
				<div class="col-md-3 customer-data-block">
					<div class="form-group">
						<label>Customer</label> <select
							class="form-control select2-select customer-data"
							name="customerId"
							data-placeholder="Search by Customer ID, Name, Email">
							<option value=""></option>
						</select>
					</div>
				</div>
				<div class="col-md-3 customer-data-block">
					<div class="form-group ">
						<label>Consignee</label> <select
							class="form-control select2-select customer-data"
							name="consigneeId" data-placeholder="Search Consignee">
							<option value=""></option>
						</select>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 pull-left">
					<div class="customer-data-block pull-left">
						<div class="form-group">
							<label>Notify Party</label> <select
								class="form-control select2-select customer-data"
								name="notifypartyId" data-placeholder="Search Notify Party">
								<option value=""></option>
							</select>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<table
						class="table frwdr-charge-table table-bordered table-striped ">
						<thead>
							<tr>
								<th class="align-center">Freight</th>
								<th class="align-center">Shipping</th>
								<th class="align-center">Inspection</th>
								<th class="align-center">Radiation</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="frwdr-charge freight"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0">0</span></td>
								<td class="frwdr-charge shipping"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0">0</span></td>
								<td class="frwdr-charge inspection"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></td>
								<td class="frwdr-charge radiation"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0">0</span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="shipping-requested-details-view" class="hidden">
		<div class="container-fluid detail-view">
			<input type="hidden" name="shipId" value="" />
			<div class="box-body no-padding bg-darkgray">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 10px">#</th>
							<th class="align-center">Stock No.</th>
							<th class="align-center">Chassis No.</th>
							<th class="align-center">Shipment Type</th>
							<th class="align-center">Forwarder</th>
							<th class="align-center">ETD</th>
							<th class="align-center">ETA</th>
							<th class="align-center">Status</th>
							<th class="align-center action" style="width: 150px;">Action</th>
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
							<td class="status align-center"><span class="label"></span></td>
							<td class="action align-center"><a href="#"
								class="ml-5 btn btn-success btn-xs" title="Accept Shipping"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#accept-shipping-request-modal"><i
									class="fa fa-fw fa-check"></i></a> <!-- <a href="#"
								class="ml-5 btn btn-warning btn-xs" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-shipping" data-type="reschedule"
								title="Reschedule"><i class="fa fa-fw fa-calendar"></i></a> -->
								<a href="#" class="ml-5 btn btn-danger btn-xs cancel-shipping"
								title="Cancel"><i class="fa fa-fw fa-close"></i></a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div id="shipping-container-allocation-table-allocatted" class="hide">
		<div class="box-body no-padding clone-element">
			<ul class="list-group" id="item-container">
				<li class="list-group-item clone-row hide" data-json=""><span
					class="chassisNo"></span><span class="pull-right action"><a
						href="#" class="ml-5 btn btn-danger btn-xs btn-remove"><i
							class="fa fa-fw fa-remove"></i>Remove</a></span></li>
			</ul>
		</div>
	</div>

	<div id="shipping-container-stock-details-view" class="hidden">
		<div class="container-fluid item-container">
			<table class="table">
				<thead>
					<tr>
						<th style="width: 10px">#</th>
						<th>Stock No.</th>
						<th>Chassis No.</th>
						<th>Purchased Date</th>
						<th>Maker</th>
						<th>Model</th>
						<th>Doc. COnverted Date</th>
						<th class="action">Action</th>
					</tr>
				</thead>
				<tbody>
					<tr class="clone-row hide">
						<td class="s-no"></td>
						<td class="stockNo"></td>
						<td class="chassisNo"></td>
						<td class="purchaseDate"></td>
						<td class="maker"></td>
						<td class="model"></td>
						<td class="documentConvertedDate"></td>
						<td class="action"><a href="#"
							class="ml-5 btn btn-primary btn-xs replace-shipping"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-container-shipping-replace-stock"><i
								class="fa fa-fw fa-exchange"></i>Replace</a><a href="#"
							class="ml-5 btn btn-danger btn-xs cancel-shipping" title="Cancel"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-update-remark"><i
								class="fa fa-fw fa-close"></i></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="modal fade" id="modal-container-allocation">
		<div class="modal-dialog modal-lg" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Allocate Container</h3>
				</div>
				<div class="modal-body" style="max-height: 500px">
					<div class="container-fluid container-allocation-step"
						style="display: none">
						<form id="form-container-allocation-step-1">
							<div class="container-fluid item">
								<input type="hidden" name="shippingInstructionData" value="" />
								<div class="row">
									<!-- <div class="col-md-3">
										<div class="form-group">
											<label class="required">Origin Country</label> <select
												class="form-control country-dropdown data-to-save select2-select"
												name="orginCountry" data-placeholder="Select Country">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Origin Port</label> <select
												class="form-control data-to-save select2-select"
												name="orginPort" data-placeholder="Select Port">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div> -->
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Dest. Country</label> <select
												class="form-control data-to-save country-dropdown select2-select"
												name="destCountrySelect" data-placeholder="Select Country">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Dest. Port</label> <select
												class="form-control select2-select" name="destPortSelect"
												data-placeholder="Select Port">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Vessel<span
												class="schedule-container hidden ml-5"><span
													class="badge bg-green eta">ETA: <span class="date"></span></span></span></label>
											<select class="form-control data-to-save select2-select"
												name="scheduleId" data-placeholder="Select Vessel"
												data-json="">
												<option value=""></option>
											</select><span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Freight Forwarder</label> <select
												class="form-control readonly data-to-save select2-select"
												name="forwarder" data-placeholder="Select Forwarder">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
								</div>

							</div>
						</form>
					</div>
					<div class="container-fluid container-allocation-step"
						style="display: none">
						<div class="row">
							<div class="col-md-2">
								<select class="form-control input-sm" name="containers"
									data-placeholder="Select Container">
								</select>
							</div>
							<div class="col-md-2">
								<a href="#" class="btn btn-success btn-sm"
									id="btn-allocate-container"> Allocate </a>
							</div>
							<div class="col-md-2">
								<div class="has-feedback pull-left">
									<input type="text"
										id="table-container-allocation-filter-search"
										class="form-control" placeholder="search.." autocomplete="off">
									<span class="glyphicon glyphicon-search form-control-feedback"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<table class="table table-hover"
									id="table_container_allocation_available" width="100px">
									<thead>
										<tr>
											<th>#</th>
											<th>Chassis No.</th>
											<th>Maker/Model</th>
											<th>Length</th>
											<th>Width</th>
											<th>Height</th>
											<th>M3</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div class="col-md-6">
								<table class="table table-hover"
									id="table_container_allocation_allocated" width="100%">
									<thead>
										<tr>
											<th>Container</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
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
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.start modal replace stock-->
	<div class="modal fade" id="modal-container-shipping-replace-stock">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Replace Stock</h3>
				</div>
				<div class="modal-body">
					<form action="">
						<input type="hidden" id="rowData" data-json="" value="">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<select name="stockNo" id="stockNo"
										class="form-control stockNo"
										data-placeholder="Search by Stock No. or Chassis No."
										required="required"><option value=""></option></select> <span
										class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.confirm container-->
	<div class="modal fade" id="modal-container-shipping-confirm">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Container Details</h3>
				</div>
				<div class="modal-body">
					<form>
						<input type="hidden" id="rowData" data-json="" value="">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="containerName">Container No.</label> <input
										type="text" class="form-control" id="containerName"
										name="containerName"><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="slaNo">SLA No.</label> <input type="text"
										class="form-control" id="slaNo" name="slaNo"><span
										class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Confirm
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.update bl-->
	<div class="modal fade" id="modal-update-vessel-confirmed">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Shipment Details</h3>
				</div>
				<div class="modal-body">
					<form>
						<input type="hidden" id="rowData" data-json="" value="">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="blNo">BL No.</label> <input type="text"
										class="form-control" id="blNo" name="blNo"><span
										class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Update vessel in container confirmed -->
	<div class="modal fade" id="modal-update-vessel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update Vessel</h3>
				</div>
				<div class="modal-body">
					<form id="update-vessel-form">
						<input type="hidden" id="rowData" data-json="" value="">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="required">Vessel<span
										class="schedule-container hidden ml-5"><span
											class="badge bg-green eta">ETA: <span class="date"></span></span></span></label>
									<select
										class="form-control required data-to-save select2-select"
										name="scheduleId" data-placeholder="Select Vessel"
										data-json="">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="btn-save-vessel" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.start modal Request from sales-->
	<div class="modal fade" id="modal-update-remark" tabindex="-1">
		<div class="modal-dialog ">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update Remarks</h3>
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
