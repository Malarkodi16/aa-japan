<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Purchase Confirmed Vehicles</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Purchase Confirmed</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">

					<div class="col-md-2">
						<div class="form-group">
							<label>Purchase Type</label> <select id="purchaseType"
								name="supplierType" class="form-control" style="width: 100%;">
								<option value="">All</option>
								<option data-type="auction" value="auction">Auction</option>
								<option data-type="supplier" value="local">Local</option>
								<option data-type="supplier" value="overseas">Overseas</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								class="form-control select2" style="width: 100%;"
								disabled="disabled">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-control"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedInfoPos">POS No.</label> <select
									id="purchasedInfoPos" class="form-control" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<em class="fa fa-calendar"></em>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-purchased-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>

							<!-- /.input group -->
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
					<div class="pull-right">
						<div class="col-md-2">
							<button type="button" class="btn btn-primary pull-left"
								id="btn-arrange-transport" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-transport">Arrange
								Transport</button>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-inline">
					<div class="col-md-2">
						<div class="form-group">
							<div class="pull-left">
								<div>
									<label>Purchase Range</label>
								</div>
								
								<select id="table-month-filter" name="table-month-filter"
									class="form-control">
									<option value="lastmonth">Last Month</option>
									<option value="thismonth" selected="selected">This
										Month</option>
									<option value="last3months">Last 3 Months</option>
									<option value="period">Period</option>
									<option value="alldata">All Data</option>
								</select>
								<div></div>
							</div>
						</div>
					</div>
					<div class="col-md-8 hidden" id="period-data">
						<div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>From</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-from" placeholder="dd-mm-yyyy"
										readonly="readonly">
								</div>
								<span class="help-block"></span>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="created-date-form-group">
								<label>To</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-to" placeholder="dd-mm-yyyy"
										readonly="readonly">
								</div>
								<span class="help-block"></span>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<div>
									<label>&nbsp;</label>
								</div>
								<div>
									<button type="button" class="btn btn-primary"
										id="btn-search-data">Search</button>
								</div>

							</div>
						</div>
					</div>

					
				</div>
				<div class="table-responsive">
					<table id="table-purchaseConfirmed"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1">Purchase Date</th>
								<th data-index="2">Chassis No.</th>
								<th data-index="3">Model</th>
								<th data-index="4">Shuppin No.</th>
								<th data-index="5">POS No.</th>
								<th data-index="6">Auction/Supplier</th>
								<th data-index="7">Auction House</th>
								<th data-index="8">Pickup Location</th>
								<th data-index="9">Drop Location</th>
								<th data-index="10">Plate No.</th>
								<th data-index="11">Destination Country</th>
								<th data-index="12">Purchase Type</th>
								<th data-index="13">Transportation Status</th>
								<th data-index="14" class="align-center">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model -->
	<div class="modal fade" id="modal-arrange-transport">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">
						Arrange Transport<span class="pull-right"><span>Credit
								Balance : </span><span class="autonumber" id="creditBalance"
							data-a-sign="¥ " data-m-dec="0"></span>&nbsp;&nbsp;&nbsp;</span>
					</h4>
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
