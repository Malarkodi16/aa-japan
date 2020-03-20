<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Transportation</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Transportation</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<!-- table start -->
			<div style="text-align: center;" class="form-group">
				<label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="0" checked="checked">
					Transport Requested
				</label><label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="2"> Transport Confirmed
				</label><label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="3">Transport In Transit
				</label><label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="5"> Transport Delivery
					Confirm
				</label><label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="4"> Transport Delivered
				</label> <label class="mr-5 ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="1"> Transport
					Rearrangement
				</label>
			</div>
			<div class="container-fluid box box-solid" id="transport-details">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Transporter</label> <select id="transporterSelect"
								name="transporterSelect" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-purchased-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="created-date-form-group">
							<label>Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-created-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								name="supplierSelect" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-controlm select2"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
				</div>

				<div class="row form-group">
					<div class="col-md-1 form-inline">

						<select id="table-filter-length" class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>

					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search-chassisNo"
								class="form-control" placeholder="Search ChassisNo"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-12">
						<div class="pull-right">
							<button type="button"
								class="btn btn-primary btn-download-multiple-invoice"
								id="btn-download-multiple-invoice" data-format="pdf">Download
								PDF</button>
							<button type="button"
								class="btn btn-primary btn-download-multiple-invoice"
								id="btn-download-multiple-invoice" data-format="excel">Download
								Excel</button>
						</div>
					</div>
				</div>
				<div class="row from-group">
					<div class="col-md-6">
						<h3 class="pull-left">
							<span>Total Items</span>&nbsp;&nbsp;&nbsp;<span
								id="transportItemCount">100</span>
						</h3>
					</div>

				</div>
				<div class="table-responsive">
					<table id="table-transport"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Invoice No.</th>
								<th data-index="2" class="align-center">Transporter ID</th>
								<th data-index="3" class="align-center">Transporter Name</th>
								<th data-index="4" class="align-center">Transporter Name</th>

							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
			<div class="container-fluid hidden box box-solid"
				id="transport-confirm-details">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Transporter</label> <select id="transporterSelectConfirm"
								name="transporterSelectConfirm" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select>
						</div>

					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-confirm-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-confirm-filter-purchased-date"
									placeholder="dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="confirm-created-date-form-group">
							<label>Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-confirm-filter-created-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="supplierConfirm"
								name="supplierConfirm" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="confirmAuctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="auctionHouseConfirm">Auction House</label> <select
									id="auctionHouseConfirm" class="form-controlm select2"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<!-- <div class="row form-group">
					
				</div> -->
				<div class="row form-group">
					<div class="col-md-1 form-inline">

						<select id="table-confirm-filter-length"
							class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>

					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-confirm-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>

						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-confirm-filter-search-chassisNo"
								class="form-control" placeholder="Search Chassis No"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>

						</div>
					</div>

				</div>
				<div class="row">
					<h3 class="pull-left">
						<span>Total Items</span>&nbsp;&nbsp;&nbsp;<span
							id="confirmTransportItemCount"></span>
					</h3>
				</div>
				<div class="table-responsive">
					<table id="table-confirm-transport"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" class="align-center">Invoice No.</th>
								<th data-index="1" class="align-center">Transporter ID</th>
								<th data-index="2" class="align-center">Transporter Name</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
			<div class="container-fluid hidden box box-solid"
				id="transport-delivery-confirmed">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Transporter</label> <select
								id="transporterDeliveryConfirmed"
								name="transporterDeliveryConfirmed" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select>
						</div>

					</div>


				</div>


				<div class="row form-group">
					<div class="col-md-1 form-inline ">
						<div class="form-group">

							<select id="table-delivery-confirmed-filter-length"
								class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-delivery-confirmed-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>

					</div>

					<div class="pull-right">
						<button class="mr-5 btn btn-primary pull-right"
							data-toggle="modal" data-target="#modal-delivered"
							data-backdrop="static" data-keyboard="false">
							<i class="fa fa-fw fa-check"></i>Complete Transport
						</button>

					</div>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered"
						id="table-transport-delivery-confirmed" style="width: 100%">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all"
									autocomplete="off" class=""></th>
								<th>Purchased Date</th>
								<th>Chassis No.</th>
								<th>Model</th>
								<th>Auction/</br>Supplier
								</th>
								<th>AuctionHouse</th>
								<th>Pickup Loc.</th>
								<th>Drop Loc.</th>
								<th>Shuppin No.</th>
								<th>Final Destination</th>
								<th>Arrival Date</th>
								<th>Charge</th>

								<th>T Count</th>

							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>

				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>
			<div class="container-fluid hidden box box-solid"
				id="transport-completed">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Transporter</label> <select
								id="transporterSelectCompleted"
								name="transporterSelectCompleted" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option></option>
							</select>
						</div>

					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-complete-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-complete-filter-purchased-date"
									placeholder="dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="completed-created-date-form-group">
							<label>Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-completed-filter-created-date"
									placeholder="dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="supplierCompleted"
								name="supplierCompleted" class="form-control select2"
								style="width: 100%;" data-placeholder="All">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="completedAuctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="auctionHouseCompleted">Auction House</label> <select
									id="auctionHouseCompleted" class="form-controlm select2"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
				</div>

				<div class="row form-group">
					<div class="col-md-1 form-inline">

						<select id="table-completed-filter-length"
							class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>

					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-completed-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>

						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-completed-filter-search-chassisNo"
								class="form-control" placeholder="Search Chassis No"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>

						</div>
					</div>

				</div>

				<div class="row">
					<h3 class="pull-left">
						<span>Total Items</span>&nbsp;&nbsp;&nbsp;<span
							id="completeTransportItemCount"></span>
					</h3>
				</div>
				<div class="table-responsive">
					<table id="table-completed-transport"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" class="align-center">Invoice No.</th>
								<th data-index="1" class="align-center">Transporter ID</th>
								<th data-index="2" class="align-center">Transporter Name</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>

			<div class="container-fluid hidden box box-solid"
				id="transport-details-rearrange">
				<div class="row form-group"></div>
				<div class="row form-group">
					<div class="col-md-1 form-inline">

						<select id="table-filter-length-rearrange"
							class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search-rearrange"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="pull-right">
						<div class="col-md-3">
							<button type="button" class="btn btn-primary pull-left"
								id="btn-arrange-transport" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-transport">Arrange
								Transport</button>
						</div>
					</div>
				</div>

				<div class="table-responsive">
					<table id="table-transport-rearrange"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Stock No.</th>
								<th data-index="2" class="align-center">ShuppinNo</th>
								<th data-index="3" class="align-center">Chassis No.</th>
								<th data-index="4" class="align-center">Category</th>
								<th data-index="5" class="align-center">Model</th>
								<th data-index="6" class="align-center">Pickup Loc</th>
								<th data-index="7" class="align-center">Drop Loc</th>
								<th data-index="8" class="align-center">Dest.Country</th>
								<th data-index="9" class="align-center">Reason</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="overlay" style="display: none;">
					<i class="fa fa-refresh fa-spin"></i>
				</div>
			</div>

		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model arrange transport -->
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
						<button class="btn btn-primary " id="btn-create-transport-order"
							value="submit">
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
	<!-- Edit Transport -->
	<div class="modal fade" id="modal-edit-transport">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Transport</h4>
					<input type="hidden" name="selectedDate" class="edit" /> <input
						type="hidden" name="selectedtype" class="edit" /> <input
						type="hidden" name="pickupDate" class="edit" /> <input
						type="hidden" name="deliveryDate" class="edit" /> <input
						type="hidden" name="pickupTime" class="edit" /> <input
						type="hidden" name="deliveryTime" class="edit" />
				</div>
				<div class="modal-body">
					<jsp:include page="/WEB-INF/views/shipping/edit-transport.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-edit-transport-order"
							value="submit">
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
	<!-- modal accept -->
	<div class="modal fade" id="modal-accept">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Please Enter Invoice Details</h4>
				</div>
				<form id="acceptForm">
					<div class="modal-body">
						<input type="hidden" id="rowData" data-json="" />
						<table class="table margin-bottom-none">
							<thead>
								<tr>
									<th class="align-center" style="width: 10px"></th>
									<th class="align-center" style="width: 10px">#</th>
									<th class="align-center">Shuppin No.</th>
									<th class="align-center">Chassis No.</th>
									<th class="align-center">Pickup Location</th>
									<th class="align-center">Drop Location</th>
									<th class="align-center input-date">Arrival Date</th>
									<th class="align-center input-amount">Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr class="clone hide">
									<td><button type="button" class="close btn-remove-item"
											aria-label="Close">
											<span aria-hidden="true"><i class="fa fa-fw fa-remove"></i></span>
										</button></td>
									<td class="sno"></td>
									<td class="align-center shuppinNo">
									<td class="align-center hide stockNo"><span></span> <input
										type="hidden" name="stockNo" /><input type="hidden"
										name="orderId" /><input type="hidden" name="transporterId" /></td>
									<td class="align-center chassisNo"></td>
									<td class="align-center pickupLocation"></td>
									<td class="align-center dropLocation"></td>
									<td class="align-center"><div>
											<input class="form-control input-date datepicker required"
												placeholder="DD-MM-YYYY" name="etd" /><span
												class="help-block"></span>
										</div></td>
									<td class="align-center"><input name="amount"
										data-validation="number"
										class="form-control autonumber required" data-m-dec="0"
										data-a-sign="¥ " /></td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td class="align-right" colspan="7"><strong>Total</strong></td>
									<td class="align-center" id="subTotal"><span
										data-m-dec="0" data-a-sign="¥ "></span></td>
								</tr>
								<tr>
									<td class="align-right" colspan="7"><strong>Total
											Tax Included</strong></td>
									<td class="align-center" id="taxIncuded"><span
										data-m-dec="0" data-a-sign="¥ "></span></td>
								</tr>
								<tr>
									<td class="align-right" colspan="7"><strong>Ref.No</strong></td>
									<td class="align-center"><input class="form-control"
										name="invoiceNo" /></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</form>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-save-invoice"
							class="btn btn-primary">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<div class="modal fade" id="modal-delivered">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Confirm Delivery</h4>
				</div>
				<form id="acceptForm">
					<div class="modal-body">
						<input type="hidden" id="rowData" data-json="" />
						<table class="table margin-bottom-none">
							<thead>
								<tr>
									<th class="align-center" style="width: 10px"></th>
									<th class="align-center" style="width: 10px">#</th>
									<th class="align-center">Shuppin No.</th>
									<th class="align-center">Chassis No.</th>
									<th class="align-center">Pickup Location</th>
									<th class="align-center">Drop Location</th>
									<th class="align-center input-date">Arrival Date</th>
									<th class="align-center input-amount">Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr class="clone hide">
									<td><button type="button" class="close btn-remove-item"
											aria-label="Close">
											<span aria-hidden="true"><i class="fa fa-fw fa-remove"></i></span>
										</button></td>
									<td class="sno"></td>
									<td class="align-center shuppinNo">
									<td class="align-center hide stockNo"><span></span> <input
										type="hidden" name="stockNo" /><input type="hidden"
										name="orderId" /><input type="hidden" name="transporterId" /></td>
									<td class="align-center chassisNo"></td>
									<td class="align-center pickupLocation"></td>
									<td class="align-center dropLocation"></td>
									<td class="align-center"><div>
											<input class="form-control input-date datepicker required"
												placeholder="DD-MM-YYYY" name="etd" /><span
												class="help-block"></span>
										</div></td>
									<td class="align-center"><input name="amount"
										data-validation="number"
										class="form-control autonumber required" data-m-dec="0"
										data-a-sign="¥ " /></td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td class="align-right" colspan="7"><strong>Total</strong></td>
									<td class="align-center" id="subTotal"><span
										data-m-dec="0" data-a-sign="¥ "></span></td>
								</tr>
								<tr>
									<td class="align-right" colspan="7"><strong>Total
											Tax Included</strong></td>
									<td class="align-center" id="taxIncuded"><span
										data-m-dec="0" data-a-sign="¥ "></span></td>
								</tr>

							</tfoot>
						</table>
					</div>
				</form>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-save-invoice"
							class="btn btn-primary">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<div id="clone-container">
		<div id="transport-order-item" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="transportOrderNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Purchased
									Date</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Chassis
									No.</th>

								<th style="width: 100px" class="align-center bg-ghostwhite">Model</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Auction/</br>Supplier
								</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">AuctionHouse</th>
								<th style="width: 200px" class="align-center bg-ghostwhite">Pickup
									Loc.</th>
								<th style="width: 200px" class="align-center bg-ghostwhite">Drop
									Loc.</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Shuppin
									No.</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Final
									Destination</th>
								<th style="width: 100px"
									class="align-center bg-ghostwhite arrival-date">Arrival
									Date</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Charge</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Remarks</th>
								<th style="width: 100px"
									class="align-center transportationCount bg-ghostwhite">T
									Count</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Status</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Invoice
									Status</th>
								<th style="width: 100px"
									class="align-center action bg-ghostwhite">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide" data-json="">
								<td class="s-no"></td>
								<td class="purchasedDate"></td>
								<td class="align-center stockNo"><input type="hidden"
									name="stockNo" value="" /><span></span></td>
								<td class="model"></td>
								<td class="auctionCompany"></td>
								<td class="auctionHouse"></td>
								<td class="pickupLocation"></td>
								<td class="dropLocation"></td>
								<td class="lotNo"></td>
								<td class="finalDestination"></td>
								<td class="etd arrival-date"></td>
								<td class="dt-right charge"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="remarks"></td>
								<td class="transportationCount"></td>
								<td class="align-center status"><span class="label"></span></td>
								<td class="align-center stockStatus"><span class="label"></span></td>
								<td class="align-center action"></td>
							</tr>

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

</section>
