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
					<li class="active"><a href="${contextPath}/shipping/management/available"><strong>Available
								For Shipping</strong></a></li>
					<li><a href="${contextPath}/shipping/management/requested"><strong>Shipping Requested</strong></a></li>
					<li><a href="#"><strong>Shipping Container</strong></a></li>
					<li><a href="#"><strong>Shipping RORO</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">
			<div style="text-align: center;" class="form-group">
				<label> <input name="radioShowTable" type="radio"
					class="minimal" value="0" checked>&nbsp;&nbsp;Available For
					Shipping
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="1">&nbsp;&nbsp;Shipping
					Requested
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="2">&nbsp;&nbsp;Shipping
					Container
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="3">&nbsp;&nbsp;Shipping
					RORO
				</label>
			</div>
			<!-- table start -->
			<div style="text-align: center;" class="form-group hidden"
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
			<div style="text-align: center;" class="form-group hidden"
				id="roro-shipment-filter">
				<label> <input name="roroAllcationStatus" type="radio"
					class="minimal" value="0" checked>&nbsp;&nbsp;Arranged
				</label> <label class="ml-5"> <input name="roroAllcationStatus"
					type="radio" class="minimal" value="1">&nbsp;&nbsp;Confirmed
				</label> <label class="ml-5"> <input name="roroAllcationStatus"
					type="radio" class="minimal" value="2">&nbsp;&nbsp;Cancelled
				</label>
			</div>
			<div class="container-fluid" id="available-for-shipping">
				<div class="row form-group">
					<!-- <div class="col-md-2">
						<div class="form-group">
							<label>Origin Port</label><select class="form-control japanPorts"
								id="originJapanPorts-filter" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div> -->

					<div class="col-md-2">
						<div class="form-group">
							<label>Country</label><select
								class="form-control country-dropdown"
								id="country-filter-available-for-shipping"
								data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Port</label><select class="form-control"
								id="port-filter-for-shipping" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>&nbsp;</label>
							<div class="form-control">
								<input type="checkbox" id="lastLapVehiclesCheck" name="lastLap"
									class="form-control" value="1"><label class="ml-5">Last
									Lap Vehicles</label>
							</div>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-available-for-shipping-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
						<div class="col-md-1 form-inline">
							<div class="pull-left">
								<select id="table-available-for-shipping-filter-length"
									class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-9 ">
						<div class="btn-group pull-right">
							<button type="button" class="btn btn-default"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-shipping-instruction"
								data-type="available">Arrange Shipping Instruction</button>
							<!-- <button type="button" class="btn btn-default"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-shipping-available"
								data-type="available">Arrange Shipping</button> -->
							<button type="button" class="btn btn-default"
								id="btn-arrange-transport" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-transport">Arrange
								Transport</button>
							<button type="button" class="btn btn-default"
								id="btn-arrange-inspection" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#arrange-inspection-modal">Arrange
								Inspection</button>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-available-for-shipping"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Stock No.</th>
								<th data-index="2" class="align-center">Chassis No.</th>
								<th data-index="3" class="align-center">Length</th>
								<th data-index="4" class="align-center">Width</th>
								<th data-index="5" class="align-center">Height</th>
								<th data-index="6" class="align-center">Destination Country</th>
								<th data-index="7" class="align-center">Destination Port</th>
								<th data-index="8" class="align-center">Current Location</th>
								<th data-index="9" class="align-center">Shipment Origin /
									Port</th>
								<th data-index="10" class="align-center">Destination
									Inspection Status</th>
								<th data-index="11" class="align-center">Inspection
									Available Status</th>
								<th data-index="12" class="align-center">Transportation
									Status</th>
								<th data-index="13" class="align-center">Last Lap Vehicle</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="container-fluid hidden" id="request-from-sales-container">
				<input type="hidden" id="request-from-sales-filter-id"
					value="${shippingRequestId }">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Forwarder</label>
							<div class="element-wrapper">
								<select class="form-control select2-select"
									name="requested-forwarder-filter"
									data-placeholder="Select Forwarder">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
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
						<div class="form-group" id="date-form-group">
							<label>Departure Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-request-from-sales-departure-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group-arrival">
							<label>Arrival Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-request-from-sales-arrival-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<!-- <div class="col-md-2">
						<div class="form-group">
							<label>Shipment Type</label> <select
								id="table-filter-request-from-sales-type"
								class="form-control data-to-save select2-select valid-required-fields"
								name="shippingType" data-placeholder="Select Type">
								<option value=""></option>
								<option value="1">RORO</option>
								<option value="2">CONTAINER</option>
							</select> <input type="hidden" name="shippingType" class="data-to-save"
								value="" /><span class="help-block"></span>
						</div>
					</div> -->
				</div>
				<div class="row form-group">
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-request-from-sales-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>

						<div class="col-md-1 form-inline">
							<div class="pull-left">
								<select id="table-request-from-sales-filter-length"
									class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>

					</div>
					<div class="col-md-2 form-inline"></div>
					<div class="col-md-4 form-inline">
						<div class="radio mr-5">
							<label> <input type="radio" name="shippingType"
								class="minimal" value="1" checked="checked"> RORO
							</label>
						</div>
						<div class="radio ml-5">
							<label> <input type="radio" name="shippingType"
								class="minimal" value="2"> CONTAINER
							</label>
						</div>
					</div>
					<div class="col-md-3 pull-right">
						<button type="button" id="btn-roro-shipping"
							class="btn btn-block btn-primary pull-right"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-arrange-shipping" data-type="sales"
							style="width: 180px;">Arrange Shipping [RORO]</button>
						<button type="button" id="btn-container-shipping"
							class="btn btn-block btn-primary hidden pull-right"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-container-allocation" data-type="create"
							style="width: 200px;">Arrange Shipping [CONTAINER]</button>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-request-from-sales"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Stock No.</th>
								<th data-index="2" class="align-center">Chassis No.</th>
								<th data-index="3" class="align-center">Customer</th>
								<th data-index="4" class="align-center">Consignee</th>
								<th data-index="5" class="align-center">Notify Party</th>
								<th data-index="6" class="align-center">Destination
									Country/Port</th>
								<th data-index="7" class="align-center">Shipment Type</th>
								<th data-index="8" class="align-center">Sales Person</th>
								<th data-index="9" class="align-center">Destination Country</th>
								<th data-index="10" class="align-center">Destination Port</th>
								<!-- ./. added by krishna -->
								<th data-index="11" class="align-center">Estimated
									Departure</th>
								<th data-index="12" class="align-center">Estimated Arrival</th>
								<th data-index="13" class="align-center">Last Location</th>
								<th data-index="14" class="align-center">Remarks</th>
								<th data-index="15" class="align-center">Action</th>
								<th data-index="16" class="align-center">Shipping Type</th>
								<!-- ././ added by krishna -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="container-fluid hidden" id="shipping-requested">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Shipping Company</label><select class="form-control "
								id="shipping-company-filter" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Ship</label><select class="form-control" id="ship-filter"
								data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-shipping-requested-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
						<div class="col-md-1 form-inline">
							<div class="pull-left">
								<select id="table-shipping-requested-filter-length"
									class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
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
								<th>Shipping Company Number</th>
								<th>Ship Number</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="container-fluid hidden" id="shipping-container">
				<div class="row form-group">
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-shipping-container-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
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
	<!-- Model arrange shipping for available stocks -->
	<div class="modal fade" id="modal-arrange-shipping-available">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<div class="row form-inline">
						<div class="col-md-5">
							<h4 class="modal-title">Arrange Shipping</h4>
						</div>
						<div class="col-md-4 form-inline">
							<div class="radio mr-5">
								<label> <input type="radio" name="btnIsCustomer"
									class="minimal" value="0" checked="checked"> Customer
								</label>
							</div>
							<div class="radio ml-5">
								<label> <input type="radio" name="btnIsCustomer"
									class="minimal" value="1"> Branch
								</label>
							</div>
						</div>
						<div class="col-md-3">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<fieldset id="transport-schedule-details">
						<legend>
							<span>Shipping Details</span><span
								class="schedule-container pull-right hidden"> <span
								class="badge bg-light-blue etd form-group">ETD: <span
									class="date"></span>
							</span><span></span> <span class="badge bg-green eta form-group">ETA:<span
									class="date"></span></span></span>
						</legend>
						<div class="container-fluid">
							<form id="shipping-arrangement-form">
								<div class="row">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Origin Country</label> <select
												class="form-control select2-select country-dropdown data-to-save valid-required-fields"
												name="orginCountry" data-placeholder="Select Country">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Origin Port</label> <select
												class="form-control data-to-save select2-select valid-required-fields"
												name="orginPort" data-placeholder="Select Port">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Dest. Country</label> <select
												class="form-control data-to-save country-dropdown select2-select valid-required-fields"
												name="destCountrySelect" data-placeholder="Select Country">
												<option value=""></option>
											</select> <input type="hidden" class="data-to-save" name="destCountry"
												value="" /> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Dest. Port</label> <select
												class="form-control select2-select valid-required-fields"
												name="destPortSelect" data-placeholder="Select Port">
												<option value=""></option>
											</select> <input type="hidden" class="data-to-save" name="destPort"
												value="" /> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="yardFields">
										<div class="form-group">
											<label>Yard</label> <select name="yardSelect"
												class="form-control select2-select" style="width: 100%;"
												data-placeholder="Select Yard">
												<option value=""></option>
											</select> <input type="hidden" class="data-to-save" name="yard"
												value="" /><span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Type</label> <select
												class="form-control data-to-save select2-select valid-required-fields"
												name="shippingTypeSelect" data-placeholder="Select Type">
												<option value=""></option>
												<option value="1">RORO</option>
												<option value="2">CONTAINER</option>
											</select> <input type="hidden" name="shippingType"
												class="data-to-save" value="" /> <span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="row">
											<div class="col-md-6">
												<div class="form-group">
													<label class="required">Vessel</label> <select
														class="form-control data-to-save select2-select valid-required-fields"
														name="scheduleId" data-placeholder="Select Vessel"
														data-json="">
														<option value=""></option>
													</select><span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-6">
												<div class="form-group">
													<label class="required">Freight Forwarder</label> <select
														class="form-control data-to-save select2-select valid-required-fields"
														name="forwarder" data-placeholder="Select Forwarder">
														<option value=""></option>
													</select><span class="help-block"></span>
												</div>
											</div>
										</div>
										<div class="row branch-details hidden">
											<div class="col-md-4 ">
												<div class="form-group">
													<label>Branch Customer</label> <select class="form-control"
														name="bCustomerId"
														data-placeholder="Search by Customer ID, Name, Email">
														<option value=""></option>
													</select>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">
													<label>Consignee</label> <select
														class="form-control select2-select" name="bConsigneeId"
														data-placeholder="Search Consignee">
														<option value=""></option>
													</select>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">
													<label>Notify Party</label> <select
														class="form-control select2-select" name="bNotifypartyId"
														data-placeholder="Search Notify Party">
														<option value=""></option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-2">
										<label>&nbsp;</label>
									</div>
								</div>
							</form>
						</div>
					</fieldset>
					<fieldset id="stock-details">
						<legend>Stock Details</legend>
						<div id="item-container" style="max-height: 300px"></div>
					</fieldset>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-shipping-request">
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
	<!-- /.modal -->
	<!-- Model arrange shipping for Sales and rearrange -->
	<div class="modal fade" id="modal-arrange-shipping">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Arrange Shipping</h4>
				</div>
				<div class="modal-body">
					<form id="shipping-arrangement-stock-form">
						<div class="container-fluid clone-item item">
							<input type="hidden" name="shippingInstructionData" value="" />
							<div class="row">
								<!-- <div class="col-md-2">
									<div class="form-group">
										<label class="required">Origin Country</label> <select
											class="form-control country-dropdown data-to-save select2-select valid-required-fields"
											name="orginCountry" data-placeholder="Select Country">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Origin Port</label> <select
											class="form-control data-to-save select2-select valid-required-fields"
											name="orginPort" data-placeholder="Select Port">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div> -->
								<div class="col-md-2">
									<div class="form-group">
										<label>Dest. Country</label> <select
											class="form-control data-to-save country-dropdown select2-select"
											name="destCountrySelect" data-placeholder="Select Country">
											<option value=""></option>
										</select> <input type="hidden" class="data-to-save" name="destCountry"
											value="" />
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label>Dest. Port</label> <select
											class="form-control select2-select" name="destPortSelect"
											data-placeholder="Select Port">
											<option value=""></option>
										</select> <input type="hidden" class="data-to-save" name="destPort"
											value="" />
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Vessel<span
											class="schedule-container hidden ml-5"><span
												class="badge bg-green eta">ETA: <span class="date"></span></span></span></label>
										<select
											class="form-control data-to-save select2-select valid-required-fields"
											name="scheduleId" data-placeholder="Select Vessel"
											data-json="">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Freight Forwarder</label> <select
											class="form-control data-to-save select2-select readonly valid-required-fields"
											name="forwarder" data-placeholder="Select Forwarder">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label>Type</label> <select
											class="form-control data-to-save select2-select valid-required-fields"
											name="shippingTypeSelect" data-placeholder="Select Type">
											<option value=""></option>
											<option value="1">RORO</option>
											<option value="2">CONTAINER</option>
										</select> <input type="hidden" value="" name="shippingType"
											class="data-to-save" /><span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row" id="yardFields">
								<div class="col-md-4">
									<div class="form-group">
										<label>Yard</label> <select
											class="form-control select2-select" name="yardSelect"
											data-placeholder="Select Yard">
											<option value=""></option>
										</select> <input type="hidden" class="data-to-save" name="yard"
											value="" />
									</div>
								</div>
							</div>
							<div class="row col-md-12">
								<table class="table table-bordered table-striped"
									id="stock-table-details">
									<thead>
										<tr>
											<th class="align-center ">#</th>
											<th class="align-center">Stock No</th>
											<th class="align-center">Chassis No</th>
											<th class="align-center">Maker</th>
											<th class="align-center">Model</th>
											<th class="align-center">M3</th>
											<th class="align-center">length</th>
											<th class="align-center">Width</th>
											<th class="align-center">Height</th>
										</tr>
									</thead>
									<tbody>
										<tr class="clone-row hide">
											<td class="s-no"><input type="hidden"
												name=shippingInstructionId value="" /><span></span></td>
											<td class="frwdr-charge stockNo"></td>
											<td class="frwdr-charge chassisNo"></td>
											<td class="frwdr-charge maker"></td>
											<td class="frwdr-charge model"></td>
											<td class="frwdr-charge m3"></td>
											<td class="frwdr-charge length"></td>
											<td class="frwdr-charge width"></td>
											<td class="frwdr-charge height"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-shipping-request">
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
	<!-- /.modal -->
	<!-- 	Modal arrange transport -->
	<div class="modal fade" id="modal-arrange-transport">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Arrange Transport</h4>
				</div>
				<div class="modal-body">
					<jsp:include page="/WEB-INF/views/shipping/arrange-transport.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-transport-order">
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
	<!-- /.modal -->
	<!-- 	modal arrange inspection -->
	<div class="modal fade" id="arrange-inspection-modal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content" id="arrange-transport">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Arrange Inspection</h4>
				</div>
				<div class="modal-body">
					<form id="form-arrange-inspection">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Destination Country</label>
									<div class="element-wrapper">
										<select id="countrySelect"
											class="form-control country-dropdown select2 arrangeData"
											name="country" style="width: 100%;"
											data-placeholder="Select Country">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Forwarder</label>
									<div class="element-wrapper">
										<select id="forwarderCode"
											class="form-control select2 arrangeData" name="forwarder"
											style="width: 100%;" data-placeholder="Select Forwarder">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>Comment</label>
									<div class="element-wrapper">
										<textarea name="comment" class="form-control" rows="2"
											cols="2"></textarea>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary"
							id="inspection-submit">Save</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
	<!-- 	modal arrange inspection -->
	<div class="modal fade" id="accept-shipping-request-modal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Shipping Details</h4>
				</div>
				<div class="modal-body">
					<form id="shipping-arrangement-form-dhl">
						<input type="hidden" id="shipmentRequestId" class="form-control"
							value="" /> <input type="hidden" id="shipId"
							class="form-control" value="" /> <input type="hidden"
							id="scheduleId" class="form-control" value="" />
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">DHL No.</label>
									<div class="element-wrapper">
										<input type="text" name="dhlNo" class="form-control" value="" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" id="btn-save">Save</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal -->
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
						<th>Maker</th>
						<th>Model</th>
						<th class="action">Action</th>
					</tr>
				</thead>
				<tbody>
					<tr class="clone-row hide">
						<td class="s-no"></td>
						<td class="stockNo"></td>
						<td class="chassisNo"></td>
						<td class="maker"></td>
						<td class="model"></td>
						<td class="action"><a href="#"
							class="ml-5 btn btn-primary btn-xs" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-container-shipping-replace-stock"><i
								class="fa fa-fw fa-exchange"></i>Replace</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	</div>
	<div class="modal fade" id="modal-arrange-shipping-instruction">
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
					<form id="shipping-instruction-form">
						<div id="item-shipping-container">
							<div class="item-shipping">
								<div class="row form-group">
									<input type="hidden" name="stockNo" id="stockNo"
										class="form-control shippingData" value=""> <input
										type="hidden" name="stockId" id="stockId"
										class="form-control shippingData" value="">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Customer</label> <select
												id="customerName" class="form-control" style="width: 100%;"
												name="customerName"
												data-placeholder="Search by Customer ID, Name, Email">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Consignee</label> <select
												name="consigneeId" id="cFirstshippingName"
												class="form-control select2-select shippingData"
												style="width: 100%;" data-placeholder="Select Consignee">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Notify Party</label> <select
												name="notifypartyId" id="npFirstshippingName"
												class="form-control select2-select shippingData"
												style="width: 100%;" data-placeholder="Select Notify Party">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>

									<div class="col-md-2">
										<label class="required">Destination Country</label> <select
											name="destCountry" id="destcountry"
											class="form-control select2-select shippingData country"
											style="width: 100%;"
											data-placeholder="Select Destination Country">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Port</label> <select name="destPort"
												id="destport"
												class="form-control select2-select shippingData port"
												style="width: 100%;"
												data-placeholder="Select Destination Port">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="yardFields">
										<div class="form-group">
											<label class="required">Yard</label> <select name="yard"
												id="yard"
												class="form-control select2-select shippingData yard"
												style="width: 100%;" data-placeholder="Select Yard">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>

								</div>
								<div class="row form-group">
									<div class="col-md-6" id="estimatedData">
										<div class="form-group">
											<div class="col-md-3">
												<div class="form-group">
													<label class="ml-5"> <input name="estimatedType"
														id="immediate" type="radio" class="minimal estimated-data"
														value="0" checked="checked"> Immediate
													</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">
													<label class="ml-5"> <input name="estimatedType"
														id="next-available" type="radio"
														class="minimal estimated-data" value="1"> Next
														Available
													</label>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group">
													<label class="ml-5"> <input name="estimatedType"
														id="preferredMonth" type="radio"
														class="minimal estimated-data" value="2">
														Preferred Month
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-6" id="estimatedDate">
										<div class="form-group">
											<div class="col-md-6">
												<div class="form-group">
													<label class="required">Estimated Departure</label> <input
														name="estimatedDeparture"
														class="form-control shippingData datePicker"
														placeholder="mm/yyyy" readonly="readonly" /> <span
														class="help-block"></span>
												</div>
											</div>
											<div class="col-md-6">
												<div class="form-group">
													<label class="required">Estimated Arrival</label> <input
														name="estimatedArrival"
														class="form-control shippingData datePicker"
														placeholder="mm/yyyy" readonly="readonly" /> <span
														class="help-block"></span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary "
							id="btn-save-shipping-instruction">
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
	<!-- /./.end modal Request from sales-->
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
											<label class="required">Vessel<span
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
										data-placeholder="Search by Stock No. or Chassis No."><option
											value=""></option></select> <span class="help-block"></span>
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
</section>
