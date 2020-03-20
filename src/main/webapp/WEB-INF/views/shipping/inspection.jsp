<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Inspection</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Inspection</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="form-group" id="avail-vehicle">
				<div style="text-align: center;">
					<label> <input name="radioShowTable" type="radio"
						class="minimal" value="0" checked> Available Vehicles
					</label> <label> <input name="radioShowTable" type="radio"
						class="minimal" value="1"> Inspection Requested
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="2"> Inspection
						Arranged Vehicles
					</label><label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="3"> Inspection
						Completed
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="4"> Inspection Failed
					</label>
				</div>
			</div>
			<div class="container-fluid available-vehicles-filter">
				<div class="row form-group" id="avail-vehicle">
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								class="form-control select2" style="width: 100%;"
								data-placeholder="All">
								<option></option>
							</select> <span class="help-block"></span>
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
					<div class="col-md-2">
						<div class="form-group">
							<label>Destination Country</label> <select
								id="filter-destination-country"
								class="form-control select2 destinationCountryFilter"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Current Location</label> <select id="filter-location"
								class="form-control select2" style="width: 100%;"
								data-placeholder="All" multiple="multiple">
								<option></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Masho Copy Received Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-masho-copy-date-available"
									placeholder="dd-mm-yyyy" readonly="readonly">
							</div>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-3">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-control"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
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
				</div>
			</div>
			<div class="container-fluid hidden"
				id="instruction-destination-filter">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Destination Country</label> <select
								id="filter-destination-country-instruction"
								class="form-control select2 destinationCountryFilter"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid inspection-arranged-filter">
				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label>Type</label> <select id="type" name="type"
								class="form-control" style="width: 100%;">
								<option value="">All</option>
								<option value="0">AAJ</option>
								<option value="1">Forwarder</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Masho Copy Received Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-masho-copy-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
						</div>
					</div>
					<div class="col-md-3 hidden" id="inspection-company">
						<div class="form-group">
							<label>Inspection Company</label> <select
								id="inspectionSelectFilter"
								class="form-control select2 arrangeData"
								name="inspectionCompany" style="width: 100%;"
								data-placeholder="Select Company">
							</select>
						</div>
						<span class="help-block"></span>
					</div>
					<div class="col-md-3 hidden" id="forwarder-company">
						<div class="form-group">
							<label>Forwarder</label> <select id="forwarderFilter"
								class="form-control select2 arrangeData" name="forwarder"
								style="width: 100%;" data-placeholder="All">
							</select> <span class="help-block"></span>
						</div>
					</div>

				</div>
			</div>
			<!-- table start -->
			<div class="row form-group">
				<div class="col-md-12 hidden" id="inspection-completed-div">
					<!-- <div class="pull-right"> -->
					<!-- <div class="col-md-2">
						<label>Inspection Date</label> <input
							class="form-control inspectionDatepicker"
							name="inspectionDateFilter" placeholder="  dd-mm-yyyy" readonly />
					</div> -->

					<div class="col-md-3">
						<div class="form-group" id="date-form-group-inspection">
							<label>Inspection Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<em class="fa fa-calendar"></em>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-inspection-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Destination Country</label> <select
								id="filter-destCountry-second-instruction"
								class="form-control select2 destinationCountryFilter"
								data-placeholder="Destination Country">

							</select> <span class="help-block"></span>
						</div>
					</div>



					<div class="col-md-7">
						<div class="pull-right">
							<button type="button" class="btn btn-primary pull-right"
								id="arrange-sec-instruction-btn" data-toggle="modal"
								data-keyboard="false" data-backdrop="static"
								data-target="#arrange-instruction-modal">Arrange Second
								Inspection</button>
						</div>
					</div>
				</div>
			</div>

			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-1 form-inline">
						<div class="form-group">
							<select id="table-filter-length" class="form-control">
								<option value="10">10</option>
								<option value="25">25</option>
								<option value="100" selected="selected">100</option>
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
					<div class="col-md-8 pull-right" id="arrange-instruction">
						<div class="pull-right">
							<button type="button" class="btn btn-primary"
								id="upload-photo-btn">
								<i class="fa fa-fw fa-plus"></i>Photo Received
							</button>
							<button type="button" class="btn btn-primary" data-toggle="modal"
								data-keyboard="false" data-backdrop="static"
								data-target="#mashoCopyReceivedModal">
								<i class="fa fa-fw fa-plus"></i>Masho Copy Received
							</button>
							<button type="button" class="btn btn-primary"
								id="inspectionAvailablExport">Export Excel</button>
							<button type="button" class="btn btn-primary"
								id="arrange-instruction-btn" data-toggle="modal"
								data-keyboard="false" data-backdrop="static"
								data-target="#arrange-instruction-modal">Arrange
								Inspection</button>

						</div>

					</div>
					<div class="col-md-8 hidden" id="inspection-btn">
						<div class="pull-right">
							<button type="button" class="btn btn-primary" id="upload-photo">
								<i class="fa fa-fw fa-plus"></i>Photo Received
							</button>
							<button type="button" class="btn btn-primary" data-toggle="modal"
								data-keyboard="false" data-backdrop="static"
								data-target="#inspectionRequestedMashoCopyReceivedModal">
								<i class="fa fa-fw fa-plus"></i>Masho Copy Received
							</button>
							<button type="button" class="btn btn-primary"
								id="inspectionRequestedExport">Export Excel</button>
							<button type="button" class="btn btn-primary"
								id="arrange-inspection" data-toggle="modal"
								data-backdrop="static" data-keyboard="false"
								data-target="#arrange-inspection-modal">Arrange
								Inspection</button>
						</div>
					</div>
					<div class="col-md-8 hidden" id="passed-vehicles">
						<div class="form-inline ">
							<div class="radio mr-5">
								<label> <input type="radio" name="inspectionStatus"
									class="minimal" value="1" checked="checked"> Show All
								</label>
							</div>
							<div class="radio ml-5">
								<label> <input type="radio" name="inspectionStatus"
									class="minimal" value="5"> Passed Vehicles
								</label>
							</div>
						</div>
					</div>

				</div>
				<div class="row form-group">
					<div class="col-md-3 form-inline hidden"
						id="transport-not-complete">
						<div class="form-group">
							<label>&nbsp;</label>
							<div class="form-control">
								<input type="checkbox" id="showMine" name="showMine"
									class="form-control minimal"><label class="ml-5">Transport
									Not Complete Vehicles</label>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive box box-solid"
					id="avail-vehicle-container">
					<div class="overlay" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<table id="table-inspection"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Chassis No</th>
								<th>Model</th>
								<th>Dest Country</th>
								<th>Staff</th>
								<th>Year</th>
								<th>Color</th>
								<th>Final Port</th>
								<th>Supplier</th>
								<th>Shuppin</th>
								<th>Photo</th>
								<th>Masho Copy</th>
								<th>Trans. Delivery Date</th>
								<th>Est Depart.</th>
								<th>ETD</th>
								<th>Vessal Name</th>
								<th>Transporter</th>
								<th>Transport Status</th>
								<th>Delivery Date</th>
								<th>Inspection Status</th>
								<th>Shipping Status</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
				<div class="table-responsive hidden box box-solid"
					id="avail-instruction-vehicle-container">
					<div class="overlay" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<table id="table-inspection-instruction"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Chassis No</th>
								<th>Model</th>
								<th>Year</th>
								<th>Color</th>
								<th>Final Port</th>
								<th>Dest. Country</th>
								<th>Supplier</th>
								<th>Shuppin</th>
								<th>Photo</th>
								<th>Masho Copy</th>
								<th>Trans. Delivery Date</th>
								<th>Staff</th>
								<th>Est Depart.</th>
								<th>ETD</th>
								<th>Vessal Name</th>
								<th>Transporter</th>
								<th>Transport Status</th>
								<th>Delivery Date</th>
								<th>Status</th>
								<th>Shipping Status</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="table-responsive hidden box box-solid"
					id="arrange-vehicle-container">
					<div class="overlay" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<table id="table-arrange"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"></th>

							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="table-responsive hidden box box-solid"
					id="instruction-completed-vehicle-container">
					<div class="overlay" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<table id="table-completed"
						class="table table-bordered table-striped"
						style="width: 120%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Chassis No</th>
								<th>Model</th>
								<th>Purchase Date</th>
								<th>Year</th>
								<th>Color</th>
								<th>Final Port</th>
								<th>Destination Country</th>
								<th>Destination Port</th>
								<th>Inspection Country</th>
								<th>Supplier</th>
								<th>Shuppin</th>
								<th>Photo</th>
								<th>Staff</th>
								<th>Est Depart.</th>
								<th>Transporter</th>
								<th>Forwarder/Inspection</th>
								<th>Inspection Date</th>
								<th>Date of Issue</th>
								<th>Doc Sent Date</th>
								<th>Doc Status</th>
								<th>Certificate No</th>
								<th>Engine No</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="table-responsive hidden box box-solid"
					id="inspection-cancelled">
					<div class="overlay" style="display: none;">
						<i class="fa fa-refresh fa-spin"></i>
					</div>
					<table id="table-inspection-cancelled"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Chassis No</th>
								<th>Model</th>
								<th>Maker</th>
								<th>Destination Country</th>
								<th>Forwarder/Inspection Company</th>
								<th>Inspection Date</th>
								<th>Current Location</th>
								<th>Staff</th>
								<th>Est Depart.</th>
								<th>Reason</th>
								<th>Remark</th>
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
	<div class="modal fade" id="mashoCopyReceivedModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Masho Copy Received</h4>
				</div>
				<div class="modal-body">
					<form id="form-mashoCopyReceivedModal">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Date</label> <input readonly="readonly"
										type="text" class="form-control datepicker"
										name="mashiCopyReceivedDate" placeholder="dd-mm-yyyy"
										autocomplete="off"> <span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" id="save">Save</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="inspectionRequestedMashoCopyReceivedModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Masho Copy Received</h4>
				</div>
				<div class="modal-body">
					<form id="form-mashoCopyReceivedModal">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Date</label> <input readonly="readonly"
										type="text" class="form-control datepicker"
										name="mashiCopyReceivedDate" placeholder="dd-mm-yyyy"
										autocomplete="off"> <span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" id="save">Save</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="inspectionArrangedMashoCopyReceivedModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Masho Copy Received</h4>
				</div>
				<div class="modal-body">
					<form id="form-mashoCopyReceivedModal">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Date</label> <input readonly="readonly"
										type="text" class="form-control datepicker"
										name="mashiCopyReceivedDate" placeholder="dd-mm-yyyy"
										autocomplete="off"> <span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" id="save">Save</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="arrange-instruction-modal">
		<div class="modal-dialog">
			<div class="modal-content" id="arrange-instruction-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Arrange Inspection</h4>
				</div>
				<div class="modal-body">
					<form id="form-arrange-inspection-instruction">

						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="required">Destination Country</label>
									<div class="element-wrapper">
										<select id="destCountry" class="form-control select2"
											name="destCountry" style="width: 100%;"
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
							id="instruction-submit">Arrange Inspection</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="arrange-inspection-modal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content" id="arrange-inspection-content">
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
							<div class="col-md-6" id="selectedDate">
								<div class="col-md-6">
									<div class="form-group">
										<label class="ml-5"> <input
											name="inspectionCompanyFlag" id="aajValue" type="radio"
											class="minimal schedule-data" value="0" checked> AAJ
										</label>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="ml-5"> <input
											name="inspectionCompanyFlag" id="forwarder" type="radio"
											class="minimal schedule-data" value="1"> Forwarder
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Destination Country</label>
									<div class="element-wrapper">
										<select id="destCountry"
											class="form-control select2 arrangeData" name="country"
											style="width: 100%;" data-placeholder="Select Country">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4 forwarder-company hidden">
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
							<div class="col-md-4 inspection-company">
								<div class="form-group">
									<label class="required">Inspection Company</label>
									<div class="element-wrapper">
										<select id="inspectionSelect"
											class="form-control select2 arrangeData"
											name="inspectionCompany" style="width: 100%;"
											data-placeholder="Select Company">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4 inspection-company">
								<div class="form-group">
									<label class="required">Location</label>
									<div class="element-wrapper">
										<select id="inspectionLocationSelect"
											class="form-control select2" name="inspectionLocation"
											style="width: 100%;" data-placeholder="Select Location">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Inspection Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker"
											name="inspectionDate" placeholder="dd-mm-yyyy">
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
							id="inspection-submit">Arrange Inspection</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="rearrange-inspection-modal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content" id="rearrange-inspection-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Re Arrange Inspection</h4>
				</div>
				<div class="modal-body">
					<form id="form-rearrange-inspection">
						<div class="row form-group">
							<div style="text-align: center;">
								<label> <input name="radioInspectionDetail" type="radio"
									class="minimal" value="0" checked> Arrange Inspection
								</label> <label class="ml-5"> <input name="radioInspectionDetail"
									type="radio" class="minimal" value="1"> Move To
									Inspetion Requested
								</label>
							</div>
						</div>
						<input type="hidden" name="code" id="code">
						<input type="hidden" name="stockNo" id="stockNo">
						<input type="hidden" name="instructionId" id="instructionId">
						<input type="hidden" name="destinationCountry" id="destinationCountry">
						<div class="row">
							<input type="hidden" name="code" id="code" class="form-control" />
							<div class="col-md-6" id="selectedDate">
								<div class="col-md-6">
									<div class="form-group">
										<label class="ml-5"> <input
											name="inspectionCompanyFlag" id="aajValue" type="radio"
											class="minimal schedule-data" value="0"> AAJ
										</label>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="ml-5"> <input
											name="inspectionCompanyFlag" id="forwarder" type="radio"
											class="minimal schedule-data" value="1"> Forwarder
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div id="destCountryhide" class="form-group">
									<label class="required">Destination Country</label>
									<div class="element-wrapper">
										<select id="destCountry"
											class="form-control select2 rearrangeData" name="country"
											style="width: 100%;" data-placeholder="Select Country">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4 forwarder-company hidden" id="forwarderIdhide">
								<div class="form-group">
									<label class="required">Forwarder</label>
									<div class="element-wrapper">
										<select id="forwarderId"
											class="form-control select2 rearrangeData" name="forwarder"
											style="width: 100%;" data-placeholder="Select Forwarder">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4 inspection-company" id="inspectionIdHide">
								<div class="form-group">
									<label class="required">Inspection Company</label>
									<div class="element-wrapper">
										<select id="inspectionId"
											class="form-control select2 rearrangeData"
											name="inspectionCompany" style="width: 100%;"
											data-placeholder="Select Company">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4 inspection-company" id="inspectionLocationIdHide">
								<div class="form-group">
									<label class="required">Location</label>
									<div class="element-wrapper">
										<select id="inspectionLocationId" class="form-control select2"
											name="inspectionLocation" style="width: 100%;"
											data-placeholder="Select Location">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4" id="inspDateHide">
								<div class="form-group">
									<label class="required">Inspection Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker" id="inspDate"
											name="inspectionDate" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4" id="commentHide">
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
					<button type="button" class="btn btn-primary hidden"
							id="save-request-submit">Save</button>
						<button type="button" class="btn btn-primary"
							id="reinspection-submit">ReArrange Inspection</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="arrange-transport-modal">
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
	<div class="modal fade" id="modal-inspection">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Inspection Details</h4>
				</div>
				<div class="modal-body" id="rowData" data-json="">
					<form id="form-inspection-details">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Date of Issue</label>
									<div class="element-wrapper">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<input type="text" name="dateOfIssue"
												class="form-control inspection-save-data pull-right datepicker reset-on-close" />
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label id="requiredRemove" class="required">Certificate
										No.</label>
									<div class="element-wrapper">
										<input type="text" name="certificateNo"
											class="form-control inspection-save-data pull-right" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<fieldset>
							<legend>Stock Detail</legend>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label>Chassis No</label>
										<div class="element-wrapper">
											<input type="text" name="chassisNo"
												class="form-control stock-details-to-save object pull-right reset-on-close">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Engine No</label>
										<div class="input-group">
											<div class="element-wrapper">
												<input type="text" name="engineNo"
													class="form-control stock-details-to-save object pull-right reset-on-close">
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Color</label>
										<div class="element-wrapper">
											<select id="color" name="color"
												class="form-control stock-details-to-save object select2 reset-on-close"
												style="width: 100%;" data-placeholder="Select Color">
												<option></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>CC</label>
										<div class="element-wrapper">
											<input type="text" name="cc"
												class="form-control stock-details-to-save object pull-right reset-on-close">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required" for="maker">Maker</label>
										<div class="element-wrapper">
											<select name="maker"
												class="form-control select-select2 stock-details-to-save object "
												style="width: 100%;" data-placeholder="Select Maker">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required" for="model">Model</label>
										<div class="element-wrapper">
											<select name="model"
												class="form-control select-select2 stock-details-to-save object "
												style="width: 100%;" data-placeholder="Select Model">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Reg. Date</label>
										<div class="element-wrapper">
											<input type="text" name="firstRegDate"
												class="form-control year-month-picker stock-details-to-save object pull-right reset-on-close"
												placeholder="YYYY/MM">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>HS Code</label>
										<div class="element-wrapper">
											<input type="text" name="hsCode"
												class="form-control stock-details-to-save object pull-right reset-on-close">
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label>Mileage</label>
										<div class="element-wrapper">
											<input type="text" name="mileage"
												class="form-control stock-details-to-save object pull-right reset-on-close">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Remarks</label>
										<div class="element-wrapper">
											<textarea name="remarks"
												class="form-control stock-details-to-save object pull-right reset-on-close"></textarea>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<h5 class="form-group">
										<b>Equipment</b>
									</h5>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="A/C" /><span
												class="ml-5">A/C</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/S" /><span
												class="ml-5">P/S</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/W" /><span
												class="ml-5">P/W</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="S/R" /><span
												class="ml-5">S/R</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="A/W" /><span
												class="ml-5">A/W</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="ABS" /><span
												class="ml-5">ABS</span>
											</label>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="AIR BAG" /><span
												class="ml-5">Air Bag</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="4WD" /><span
												class="ml-5">4WD</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/M" /><span
												class="ml-5">P/M</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="TV" /><span
												class="ml-5">TV</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="PD" /><span
												class="ml-5">PD</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="NV" /><span
												class="ml-5">NV</span>
											</label>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="R/S" /><span
												class="ml-5">R/S</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="F/LAMP" /><span
												class="ml-5">F/LAMP</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="CD" /><span
												class="ml-5">CD</span>
											</label>
										</div>
									</div>
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary"
							id="inspection-details-save">Update</button>
						<button class="btn btn-primary" id="btn-inspection-close"
							data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal document update -->
	<div class="modal fade" id="modal-inspection-document">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<form id="form-modal-inspection-document">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Document Sent Details</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="id" value="" />
						<div class="container-fluid" id="document-data-container">
							<div class="row form-group">
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Sent Date</label>
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<div class="element-wrapper">
												<input type="text" name="documentSentDate"
													class="form-control pull-right documentDatepicker"
													id="documentation-date" placeholder="dd-mm-yyyy">
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Document Type</label>
										<div class="element-wrapper">
											<select id="document-sent" class="form-control select2 "
												name="inspectionStatus" style="width: 100%;"
												data-placeholder="Select Document">
												<option value=""></option>
												<option value="0">Not Sent</option>
												<option value="1">Copy</option>
												<option value="2">Original</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button type="button" class="btn btn-primary"
								id="document-save-data">Update</button>
							<button class="btn btn-primary" id="btn-document-close"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- Modal document update multiple -->
	<div class="modal fade" id="modal-inspection-document-multiple">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<form id="form-inspection-document-multiple">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Document Sent Details</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid" id="document-data-container">
							<div class="row form-group">
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Sent Date</label>
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<div class="element-wrapper">
												<input type="text" name="documentSentDate"
													class="form-control pull-right documentDatepicker"
													id="documentation-date" placeholder="dd-mm-yyyy">
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Document Type</label>
										<div class="element-wrapper">
											<select id="document-sent" class="form-control select2 "
												name="inspectionStatus" style="width: 100%;"
												data-placeholder="Select Document">
												<option value=""></option>
												<option value="0">Not Sent</option>
												<option value="1">Copy</option>
												<option value="2">Original</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button type="button" class="btn btn-primary"
								id="document-save-multiple-data">Update</button>
							<button class="btn btn-primary" id="btn-document-close"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<!-- modal reason -->
	<div class="modal fade" id="modal-reason">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Please enter reason</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="data" data-json="" value="" />
					<div class="form-group">
						<textarea name="reason" class="form-control" rows="3"
							placeholder="Enter ..."></textarea>
					</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-reason-submit"
							class="btn btn-primary">Submit</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- modal -->
	<div class="modal fade" id="modal-cancelledRemark">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update Remark</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="data" data-json="" value="" />
					<div class="form-group">
						<textarea name="remark" class="form-control" rows="3"
							placeholder="Enter ..."></textarea>
					</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-reason-submit"
							class="btn btn-primary">Update</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
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

	<!-- modal failed inspection -->
	<div class="modal fade" id="modal-failed-history">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						Payment Details<span> - </span><span class="invoiceNo"></span><span>
							/ </span><span class="supplierName"></span>
					</h4>
				</div>
				<div class="modal-body" id="inspectionHistoryDetails">
					<input type="hidden" name="inspectionCode"> <input
						type="hidden" name="rowData" />
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-detail-invoice"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Inspection Created Date</th>
										<th data-index="1">Inspection Date</th>
										<th data-index="2">Destination Counrtry</th>
										<th data-index="3">Inspected Country</th>
										<th data-index="4">Forwarder/Inspection Company</th>
										<th data-index="5">Location</th>
										<th data-index="6">Inspection Flag</th>
										<th data-index="7">Remarks</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>
	<div id="clone-container">
		<div id="inspection-order-item" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive inspection-order-item-container">
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>

								<th data-index="0" class="sno">#</th>
								<th data-index="1" class="chassisNo">Chassis No</th>
								<th data-index="2" class="model">Model</th>
								<th data-index="3" class="purchasedDate">Purchase</br>Date
								</th>
								<th data-index="4" class="year">Year</th>
								<th data-index="5" class="color">Color</th>
								<th data-index="6" class="finalPort">Final Port</th>
								<th data-index="7" class="supplier">Supplier</th>
								<th data-index="8" class="shuppin">Shuppin</th>
								<th data-index="9" class="photo">Photo</th>
								<th data-index="10" class="book">Staff</th>
								<th data-index="11" class="estimatedDeparture">Est Depart.</th>
								<th data-index="12" class="transporter">Transporter</th>
								<th data-index="13" class="frwdr-inspection-company">Forwarder/</br>Inspection
								</th>
								<th data-index="14" class="sentDate">Inspection</br>Date
								</th>
								<th data-index="15" class="mashoCopy">Masho Copy</br> Received
									Date
								</th>
								<th data-index="16" class="dateOfIssue">Date of Issue</th>
								<th data-index="17" class="docSentDate">Doc</br>Sent Date
								</th>
								<th data-index="18" class="docStatus">Doc Status</th>
								<th data-index="18" class="transportStatus">Transport
									Status</th>
								<th data-index="19" class="certificateNo">Certificate No.</th>
								<th data-index="20" class="engineNo">Engine No</th>
								<th data-index="21" class="action" width="150px">Action</th>
								<th data-index="22" class="remark">Remark</th>

							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide" data-json="">
								<td data-index="0" class="sno"><input type="checkbox"
									name="selectBox" /></td>
								<td data-index="1" class="chassisNo"></td>
								<td data-index="2" class="model"></td>
								<td data-index="3" class="purchasedDate"></td>
								<td data-index="4" class="year"></td>
								<td data-index="5" class="color"></td>
								<td data-index="6" class="finalPort"></td>
								<td data-index="7" class="supplier"></td>
								<td data-index="8" class="shuppin"></td>
								<td data-index="9" class="photo"></td>
								<td data-index="10" class="book"></td>
								<td data-index="11" class="estimatedDeparture"></td>
								<td data-index="12" class="transporter"></td>
								<td data-index="13" class="frwdr-inspection-company"></td>
								<td data-index="14" class="sentDate"></td>
								<td data-index="15" class="mashoCopy"></td>
								<td data-index="16" class="dateOfIssue"></td>
								<td data-index="17" class="docSentDate"></td>
								<td data-index="18" class="docStatus"></td>
								<td data-index="18" class="transportStatus"></td>
								<td data-index="19" class="certificateNo"></td>
								<td data-index="20" class="engineNo"></td>
								<td data-index="21" class="action" width="100px"></td>
								<td data-index="22" class="remark"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>