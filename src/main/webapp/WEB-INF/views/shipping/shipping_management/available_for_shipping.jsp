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
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>Available
								For Shipping</strong></a></li>
					<li><a href="${contextPath}/shipping/management/requested"><strong>Shipping
								Requested</strong></a></li>
					<li><a href="${contextPath}/shipping/management/container"><strong>Shipping
								Container</strong></a></li>
					<li><a href="${contextPath}/shipping/management/roro"><strong>Shipping
								RORO</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">
			<div class="container-fluid" id="available-for-shipping">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Destination Country</label><select
								class="form-control country-dropdown"
								id="country-filter-available-for-shipping"
								data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Current Location</label><select class="form-control"
								id="current-location-filter-for-shipping" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Shipment Port</label><select class="form-control"
								id="shipment-port-filter-for-shipping" data-placeholder="All">
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
					<div class="col-md-2">
						<div class="form-group">
							<label>&nbsp;</label>
							<div class="form-control">
								<input type="checkbox" id="lastLapVehiclesCheck" name="lastLap"
									class="form-control minimal" value="1"><label
									class="ml-5">Last Lap Vehicles</label>
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<div class="pull-right">
							<div class="form-group">
								<label></label>
								<div class="input-group">
									<button class="btn btn-primary pull-right" type="button"
										id="excel_export_all">
										<i class="fa fa-file-excel-o" aria-hidden="true"> Export
											Excel</i>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row form-group">
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
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text"
								id="table-available-for-shipping-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>

					</div>
					<div class="col-md-8 ">
						<div class="btn-group pull-right">
							<button type="button" class="btn btn-default"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-shipping-instruction"
								data-type="available">Arrange Shipping Instruction</button>

							<button type="button" class="btn btn-default"
								id="btn-arrange-transport" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-transport">Arrange
								Transport</button>
							<button type="button" class="btn btn-default"
								id="btn-arrange-inspection" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#arrange-inspection-modal">Arrange
								Inspection Instruction</button>
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
								<th data-index="3" class="align-center">Purchase Date</th>
								<th data-index="4" class="align-center">Length</th>
								<th data-index="5" class="align-center">Width</th>
								<th data-index="6" class="align-center">Height</th>
								<th data-index="7" class="align-center">Destination Country</th>
								<th data-index="8" class="align-center">Destination Port</th>
								<th data-index="9" class="align-center">Current Location</th>
								<th data-index="10" class="align-center">Shipment Origin /
									Port</th>
								<th data-index="11" class="align-center">Shipment Type</th>
								<th data-index="12" class="align-center">Forwarder</th>
								<th data-index="13" class="align-center">Dest Insp Status</th>
								<th data-index="14" class="align-center">Ins. Available
									Status</th>
								<th data-index="15" class="align-center">Trans. Status</th>								
								<th data-index="16" class="align-center">Doc. Converted
									Date</th>
								<th data-index="17" class="align-center">Staff</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>


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
		<div class="modal-dialog">
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
							<div class="col-md-6">
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
											<label>Notify Party</label> <select name="notifypartyId"
												id="npFirstshippingName"
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
											<label class="required">CFS Yard</label> <select name="yard"
												id="yard"
												class="form-control select2-select shippingData yard"
												style="width: 100%;" data-placeholder="Select Yard">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Payment Type</label>
											<div class="element-wrapper">
												<select id="paymentType" name="paymentType"
													class="form-control select2-select shippingData"
													style="width: 100%;">
													<option value="">Select Payment Type</option>
													<option data-type="PREPAID" value="COLLECT">PREPAID</option>
													<option data-type="COLLECT" value="COLLECT">COLLECT</option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<div class="form-control">
												<input type="checkbox" value="1" name="inspectionFlag"
													class="shippingData" autocomplete="off"><label
													class="ml-5">Arrange Inspection</label>
											</div>
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
</section>
