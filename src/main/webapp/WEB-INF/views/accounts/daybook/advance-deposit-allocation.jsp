<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Advcance Deposit Allocation List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Receipts</span></li>
		<li class="active">Advcance Deposit Allocation List</li>

	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li><a href="${contextPath}/daybook/tt-allocation"><strong>TT
								Applied List</strong></a></li>
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>Advcance
								Deposit Allocation</strong></a></li>

				</ul>
			</div>
		</div>
		<!-- <div class="box-body"> -->
		<div class="container-fluid ">
			<div class="row">
				<div class="col-md-2">
					<div class="form-group" id="date-form-group">
						<label>Remit Date</label>
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

				<div class="col-md-2">
					<div class="form-group">
						<label>Bank</label> <select id="bankFilter" name="bankFilter"
							class="form-control select2-tag" style="width: 100%;"
							data-placeholder="Select Bank">
							<option value=""></option>
						</select>
					</div>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-md-1 form-inline pull-left">
					<div class="form-group">
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
						<input type="text" id="table-filter-search" class="form-control"
							placeholder="Search by keyword" autocomplete="off"> <span
							class="glyphicon glyphicon-search form-control-feedback"></span>
					</div>
				</div>

			</div>
			<div class="table-responsive">
				<table id="table-ttApprove"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0">Date</th>
							<th data-index="1">Receipt No</th>
							<th data-index="2">Chassis No</th>
							<th data-index="3">Type</th>
							<th data-index="4">Remitter</th>
							<th data-index="5">Bank</th>
							<th data-index="6">Amount</th>
							<th data-index="7">Allocated Amount</th>
							<th data-index="8">Balance</th>
							<th data-index="9">Customer</th>
							<th data-index="10">Sales Person</th>
							<th data-index="11">Action</th>
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
							<input type="hidden" class="form-control" id="currency"
								name="currency"> <input type="hidden"
								class="form-control" id="exchangeRate1" name="exchangeRate1">
							<input type="hidden" class="form-control" id="exchangeRate2"
								name="exchangeRate2">
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
										data-placeholder="Select Allocation Type" style="width: 100%;">
										<option value="">Select Allocation Type</option>
										<option value="1">FIFO</option>
										<option value="2">Unit Allocation</option>
									</select>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>Source Amount</label><input
										class="form-control autonumber" name="amount" type="text"
										disabled="disabled">
								</div>
								<input class="hide" name="refId" id="refId" type="text">
								<input class="hide" name="amount" id="amount" type="text">
								<input class="hide" name="transId" id="transId" type="text">
								<input class="hide" name="customerId" id="customerId" type="text">
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>Allocated Amount</label><input
										class="form-control autonumber" name="allocatedAmount"
										type="text" disabled="disabled">
								</div>
								<input class="hide" name="allocatedAmount" id="allocatedAmount"
									type="text">
							</div>

							<div class="col-md-4">
								<div class="form-group">
									<label>Balance Amount</label><input
										class="form-control autonumber" name="balanceAmount"
										type="text" disabled="disabled">
								</div>
								<input class="hide" name="balanceAmount" id="balanceAmount"
									type="text">
							</div>
						</div>
						<!-- <div class="row hidden" id="rateFilter">
							<div class="col-md-4">
								<div class="form-group">
									<label>Exchange rate</label><select id="exchangeRate"
										name="exchangeRate" class="form-control"
										data-placeholder="Select Exchange Rate" style="width: 100%;">
										<option value="">Select Exchange Rate</option>
										<option value="1">Exchange Rate</option>
										<option value="2">Sales Exchange Rate</option>
										<option value="3">Special Exchange Rate</option>
									</select>
								</div>
							</div>
							<div class="col-md-4" id="actual">
								<div class="form-group">
									<label>Actual Amount</label><input
										class="form-control autonumber" name="actualAmount"
										type="text" disabled="disabled" data-a-sign="¥ "
										data-m-dec="0">
								</div>
								<input class="hide" name="actualAmount" id="actualAmount"
									type="text">
							</div>
						</div> -->
						<!-- Modal -->
						<div class="mt-10 table-responsive" id="hidden-table">
							<table class="table table-bordered"
								id="Unit-Allocation-details-table" style="width: 100%">
								<thead>
									<tr>
										<th data-index="0" class="align-center">#</th>
										<th data-index="1" class="align-center">Stock No.</th>
										<th data-index="2" class="align-center">Chassis No.</th>
										<th data-index="3" class="align-center">Etd</th>
										<th data-index="4" class="align-center">Amount</th>
										<th data-index="5" class="align-center">Received</th>
										<th data-index="6" class="align-center">Balance</th>
										<th data-index="7" class="align-center">Price Allocation</th>
									</tr>
								</thead>
								<tbody>
									<tr class="clone-row hide">
										<td class="align-center select"></td>
										<td class="align-center stockNo"></td>
										<td class="align-center chassisNo"></td>
										<td class="align-center etd"></td>
										<td class="align-center amount"></td>
										<td class="align-center received"></td>
										<td class="align-center balance"></td>
										<td class="align-center price"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="mt-10 table-responsive hidden" id="hidden-table-fifo">
							<table class="table table-bordered"
								id="Fifo-Allocation-details-table" style="width: 100%">
								<thead>
									<tr>
										<th data-index="0" class="align-center">#</th>
										<th data-index="1" class="align-center">Stock No.</th>
										<th data-index="2" class="align-center">Chassis No.</th>
										<th data-index="3" class="align-center">Amount</th>
										<th data-index="4" class="align-center">Received</th>
										<th data-index="5" class="align-center">Balance</th>
										<th data-index="6" class="align-center">Price Allocation</th>
									</tr>
								</thead>
								<tbody>
									<tr class="clone-row hide">
										<td class="align-center select"></td>
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
		<!-- Model -->
		<div class="modal fade" id="modal-refund-amount">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title">Amount Refund</h3>
					</div>
					<div class="modal-body">
						<form id="amount-refund-form">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-4 ">
										<div class="form-group">
											<label class="required">Amount</label>
											<div class="element-wrapper">
												<input type="text" id="amount" name="amount"
													class="form-control autonumber required amount"
													placeholder="Enter Amount"> <input class="hide"
													name="refId" id="refId" type="text"> <input
													class="hide" name="transId" id="transId" type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>

								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button id="approve" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- </div> -->
	</div>
</section>