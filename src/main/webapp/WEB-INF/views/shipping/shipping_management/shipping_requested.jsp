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
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>Shipping
								Requested</strong></a></li>
					<li><a href="${contextPath}/shipping/management/container"><strong>Shipping
								Container</strong></a></li>
					<li><a href="${contextPath}/shipping/management/roro"><strong>Shipping
								RORO</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">

			<div class="container-fluid" id="request-from-sales-container">
				<input type="hidden" id="request-from-sales-filter-id"
					value="${shippingRequestId }">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Forwarder</label>
							<div class="element-wrapper">
								<select class="form-control select2-select"
									name="requested-forwarder-filter"
									data-placeholder="Select Forwarder" multiple>
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Destination Country</label><select
								class="form-control country-dropdown"
								id="country-filter-from-sales" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Orgin Port</label><select class="form-control"
								id="orgin-port-filter-from-sales" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Current Location</label>
							<div class="element-wrapper">
								<select class="form-control select2-select"
									id="transport-location-filter" data-placeholder="All">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Estimated Departure</label>
							<div class="element-wrapper">
								<select class="form-control select2-select"
									id="estimated-departure-filter" data-placeholder="All">
									<option value="">Select Est Depart.</option>
									<option value="0">Immediate</option>
									<option value="1">Next Available</option>
									<option value="2">Preferred Month</option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Staff</label>
							<div class="element-wrapper">
								<select class="form-control" data-placeholder="Sales Staff"
									id="select_staff" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<!-- <div class="col-md-2">
						<div class="pull-right">
							<div class="form-group">
								<label></label>
								<div class="input-group">
									
								</div>
							</div>
						</div>
					</div> -->
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

					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-request-from-sales-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>



					</div>
					<div class="col-md-3 form-inline">
						<div class="form-group">

							<div class="form-control">
								<input type="checkbox" id="showBlankForwarder"
									name="showBlankForwarder" /><label class="ml-5">Show
									Blank Forwarder</label>
							</div>
						</div>
					</div>


					<div class="col-md-4 pull-right">
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
						<button class="btn btn-primary" type="button" id="export_excel">
							<i class="fa fa-file-excel-o" aria-hidden="true"> Export
								Excel</i>
						</button>
					</div>
				</div>
				<div class="row form-group">
					<div style="text-align: center;">
						<label> <input type="radio" name="shippingType"
							class="minimal" value="1" checked="checked"> RORO
						</label> <label> <input type="radio" name="shippingType"
							class="minimal" value="2"> CONTAINER
						</label><label> <input type="radio" name="shippingType"
							class="minimal" value="0"> NONE
						</label>
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
								<th data-index="1" class="align-center">Ship Ins Date</th>
								<th data-index="2" class="align-center">Est Depart.</th>
								<th data-index="3" class="align-center">Stock No.</th>
								<th data-index="4" class="align-center">Chassis No.</th>
								<th data-index="5" class="align-center">Purchase Date</th>
								<th data-index="6" class="align-center">Customer</th>
								<th data-index="7" class="align-center">Consignee</th>
								<th data-index="8" class="align-center">Notify Party</th>
								<th data-index="9" class="align-center">Destination
									Country/Port</th>
								<th data-index="10" class="align-center">First Reg. Date</th>
								<th data-index="11" class="align-center">Sales Person</th>
								<th data-index="12" class="align-center">Destination
									Country</th>
								<th data-index="13" class="align-center">Destination Port</th>
								<!-- ./. added by krishna -->
								<th data-index="14" class="align-center">Forwarder</th>
								<th data-index="15" class="align-center">Current Location</th>
								<th data-index="16" class="align-center">Current Port</th>
								<th data-index="17" class="align-center">Remarks</th>
								<th data-index="18" class="align-center">Doc. Converted
									Date</th>
								<th data-index="19" class="align-center">Action</th>
								<th data-index="20" class="align-center">Shipping Type</th>

								<!-- ././ added by krishna -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

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
								<div class="col-md-2">
									<div class="form-group">
										<label>Origin Port</label> <select
											class="form-control data-to-save originPort-dropdown select2-select"
											name="orginPort" data-placeholder="Select Port">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label>Dest. Country (<a name="change">change</a>)
										</label> <select
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
										<label>Vessel<span
											class="schedule-container hidden ml-5"><span
												class="badge bg-green eta">ETA: <span class="date"></span></span></span></label>
										<select class="form-control data-to-save select2-select"
											name="scheduleId" data-placeholder="Select Vessel"
											data-json="">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Freight Forwarder</label> <select
											class="form-control data-to-save select2-select"
											name="forwarder" data-placeholder="Select Forwarder"
											required="required">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2" style="display: none;">
									<div class="form-group">
										<label>Type</label> <select
											class="form-control data-to-save select2-select"
											name="shippingTypeSelect" data-placeholder="Select Type"
											required="required">
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
												class="form-control required data-to-save country-dropdown select2-select"
												name="destCountrySelect" data-placeholder="Select Country">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Dest. Port</label> <select
												class="form-control required select2-select" name="destPortSelect"
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
												class="form-control required data-to-save select2-select"
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
