
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>

<section class="content-header">
	<h1>CR Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>BL Management</span></li>
		<li class="active">CR</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">

				<div class="row">
					<div class="col-md-3">
						<div class="form-group">
							<label>Customer</label>
							<div class="element-wrapper">
								<select class="form-control" id="custId" style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Consignee</label> <select name="consigneeId"
								id="consigneeId" class="form-control" style="width: 100%;"
								data-placeholder="Select Consignee">
								<option value=""></option>
							</select> <span class="help-block"></span>
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
					<div class="col-md-2">
						<div class="form-group">
							<label>Country</label><select class="form-control"
								id="country-filter-available-for-shipping" name="destCountry"
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
				</div>
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Vessel</label>
							<div class="element-wrapper">
								<select class="form-control" id="vessalAndVoyageNo"
									data-placeholder="Select Vessel" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>ETD</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-etd-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>ETA</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-eta-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-1 pull-left">
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
					<div class="col-md-8 form-inline pull-right">

						<div class="pull-right">
							<div class="form-group">
								<button class="btn btn-primary" name="update-bl-amend-in-group"
									id="update-bl-amend-in-group" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-update-cr-amend">Bl Amend</button>
								<button class="ml-5 btn btn-primary" data-backdrop="static"
									data-keyboard="false" data-toggle="modal"
									data-target="#modal-update-bl-no">Update Bl No.</button>
							</div>
						</div>
					</div>

				</div>
			</div>
			<!-- table start -->
			<div class="table-responsive">
				<table id="table-cr-data" class="table table-bordered table-striped"
					style="width: 160%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0" style="width: 10px"><input
								type="checkbox" id="select-all" /></th>
							<th data-index="1">Chassis No</th>
							<th data-index="2">Customer</th>
							<th data-index="3">Staff</th>
							<th data-index="4">Final Destination</th>
							<th data-index="5">Vessel No</th>
							<th data-index="6">Vessel Name</th>
							<th data-index="7">Lot No</th>
							<th data-index="8">BL No</th>
							<th data-index="9">ETD</th>
							<th data-index="10">ETA</th>
							<th data-index="11">Sold Amount</th>
							<th data-index="12">Amount Received</th>
							<th data-index="13">Balance Amount</th>
							<th data-index="14">Rec/Sur Status</th>
							<th data-index="15">Document Amount</th>
							<th data-index="16">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<!-- 	.modal -->
	<div class="modal fade" id="modal-update-cr-amend">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update</h3>
				</div>
				<div class="modal-body">
					<form id="bl-amend-update">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Customer</label> <select id="customer"
											class="form-control" style="width: 100%;" name="customer"
											data-placeholder="Search by Customer ID, Name, Email">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Consignee</label> <select
											id="consignee" name="consignee"
											class="form-control consignee"
											data-placeholder="Select Consignee" style="width: 100%;">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<!-- <div class="col-md-6">
									<div class="form-group">
										<label>Bl No.</label>
										<div class="element-wrapper">
											<input type="text" id="blNo" name="blNo" class="form-control"
												placeholder="Bl NO" data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div> -->
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="update-single-cr"
						class="single-update btn btn-primary hidden">
						<em class="fa fa-fw fa-save"></em> update
					</button>
					<button id="update-multiple-cr"
						class="multiple-update btn btn-primary hidden">
						<em class="fa fa-fw fa-save"></em> update
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 	.modal -->
	<div class="modal fade" id="modal-update-bl-no">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update</h3>
				</div>
				<div class="modal-body">
					<form id="bl-amend-update">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label>Bl No.</label>
										<div class="element-wrapper">
											<input type="text" id="blNo" name="blNo" class="form-control"
												placeholder="Bl NO" data-m-dec="0" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="update-single-bl-no"
						class="single-update-bl-no btn btn-primary hidden">
						<em class="fa fa-fw fa-save"></em> update
					</button>
					<button id="update-multiple-bl-no"
						class="multiple-update-bl-no btn btn-primary hidden">
						<em class="fa fa-fw fa-save"></em> update
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- modal invoice payments -->
	<div class="modal fade" id="modal-cr-transaction-list">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Transaction</h4>
				</div>
				<div class="modal-body" id="crTransactionDetails">
					<input type="hidden" name="shippingInstructionId"> <input
						type="hidden" name="rowData" />
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-cr-transaction-data"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="1">Shipping Instruction Id</th>
										<th data-index="2">Customer</th>
										<th data-index="3">Consignee</th>
										<th data-index="4">Bl Date</th>
										<th data-index="5">Bl By</th>
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


	<!-- 	.modal -->
	<div class="modal fade" id="modal-update-Rec-Sur-status">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update</h3>
				</div>
				<div class="modal-body">
					<form id="bl-amend-update">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label>Receive/ Surrender</label>
										<div class="element-wrapper">
											<select id="recSurStatus" name="recSurStatus"
												class="form-control" style="width: 100%;"
												data-placeholder="Select Status" tabindex="15">
												<option value="">
												<option>
												<option value="1">RECEIVE</option>
												<option value="2">SURRENDER</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="update-status" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> update
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>