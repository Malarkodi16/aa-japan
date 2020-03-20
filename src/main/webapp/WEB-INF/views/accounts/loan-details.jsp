
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>View Loan</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Loan Management</span></li>
		<li class="active">View Loan</li>
	</ol>
</section>

<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group" id="searchCondition">
					<div class="col-md-2">
						<div class="form-group">
							<label>Bank</label> <select id="bank" name="bank"
								class="form-control" data-placeholder="Select Bank"
								style="width: 100%;">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-3 mt-5">
						<br>
						<button type="submit" id="btn-searchData" class="btn btn-primary">Search</button>

					</div>
				</div>
			</div>
			<div class="container-fluid ">
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
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
					<table id="table-viewloan"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Bank</th>
								<th data-index="1">Loan Id</th>
								<th data-index="2">Reference</th>
								<th data-index="3">Loan Date</th>
								<th data-index="4">Loan Type</th>
								<th data-index="5">Loan Term</th>
								<th data-index="6">Interest</th>
								<th data-index="7">Start Date</th>
								<th data-index="8">Capital Amount</th>
								<th data-index="9">Total Payable</th>
								<th data-index="10">Closing Balance</th>
								<th data-index="11">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
								<tr>
									<th colspan="8" style="text-align: right"></th>
									<th>Capital Amount(JPY) Total</th>
									<th>Total Payable(JPY)Total</th>
									<th>Closing Balance(JPY)Total</th>
									<th></th>
								</tr>
								<tr class=sum>
									<th colspan="8" style="text-align: right"></th>
									<th class="dt-right"><span
										class="autonumber pagetotal capitalAmountTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal payableTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal closingBalanceTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th></th>
									</tr>
								</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>


	<!-- /.form:form -->

</section>