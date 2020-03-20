<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>TT Allocation List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>TT Allocation</span></li>
		<li class="active">TT Allocation List</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid ">
			<div class="row form-group">
					<div class="col-md-3">
						<div class="has-feedback">
							<label></label> <input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"
								autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label></label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-1 form-inline pull-right">
						<div class="form-group">
							<label></label> <select id="table-filter-length"
								class="form-control">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-ttAllocation"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Date</th>
								<th data-index="1">Remit Type</th>
								<th data-index="2">Remitter</th>
								<th data-index="3">Bank</th>
								<th data-index="4">Amount</th>
								<th data-index="5">Balance</th>
								<th data-index="6">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal fade" id="allocationModal">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h3 class="modal-title">Allocation</h3>
						</div>
						<div class="modal-body">
							<div class="row form-group" id="searchCondition">
								<!-- Customer Wise Filter -->
								<div class="col-md-4">
									<div class="form-group">
										<label>Customer</label> <select
											class="form-control select2 customer" id="custId"
											name="custId" style="width: 100%;"
											data-placeholder="Search by Customer ID, Name, Email">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label>Allocation Type</label><select id="allocationType"
											name="allocationType" class="form-control"
											data-placeholder="Select Allocation Type"
											style="width: 100%;">
											<option value="">Select Allocation Type</option>
											<option value="1">FIFO</option>
											<option value="2">Unit Allocation</option>
											<option value="3">Advance</option>
											<option value="4">Deposit</option>
										</select>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label>Amount</label><input class="form-control autonumber"
											name="amount" type="text" disabled="disabled">
									</div>
									<input class="hide" name="refId" id="refId" type="text">
									<input class="hide" name="amount" id="amount" type="text">
								</div>
							</div>
							<!-- Modal -->
							<div class="mt-10 table-responsive" id="hidden-table">
								<table class="table table-bordered"
									id="Unit-Allocation-details-table" style="width: 100%">
									<thead>
										<tr>
											<th data-index="0" class="align-center">Stock No.</th>
											<th data-index="1" class="align-center">Chassis No.</th>
											<th data-index="2" class="align-center">Amount</th>
											<th data-index="3" class="align-center">Received</th>
											<th data-index="4" class="align-center">Balance</th>
											<th data-index="5" class="align-center">Price Allocation</th>
										</tr>
									</thead>
									<tbody>
										<tr class="clone-row hide">
											<td class="align-center stockNo"></td>
											<td class="align-center chassisNo"></td>
											<td class="align-center amount"></td>
											<td class="align-center received"></td>
											<td class="align-center balance"></td>
											<td class="align-center price"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="modal-footer">
							<button type="submit" id="save-ttAllocation"
								class="btn btn-primary">Allocate</button>
							<button type="submit" id="btn-searchData" data-dismiss="modal"
								class="btn btn-primary">Close</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>