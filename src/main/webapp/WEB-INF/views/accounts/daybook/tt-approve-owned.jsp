<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Receipts Approval</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Receipts</span></li>
		<li class="active">Receipts Approval</li>
	</ol>
</section>
<!-- DAYBOOK APPROVE. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li><a href="${contextPath}/daybook/approve"><strong>TT
								Applied List</strong></a></li>
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>TT
								Owned List</strong></a></li>

				</ul>
			</div>
		</div>
		<div class="box-body">
			<div class="container-fluid ">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-created-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="submit" class="btn btn-primary form-control"
								id="btn-search">Search</button>
						</div>
					</div>

				</div>
				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label>Bank</label> <select id="bankFilter" name="bankFilter"
								class="form-control select2-select" style="width: 100%;"
								data-placeholder="Select Bank">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Customer</label>
							<div class="element-wrapper">
								<select class="form-control select2-select" id="customerFilter"
									style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Sales Person</label>
							<div class="element-wrapper">
								<select class="form-control select2-select"
									id="salesPersonFilter" data-placeholder="Sales Staff"
									id="select_sales_staff" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
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
				</div>
				<div class="row form-group">

					<div class="col-md-1 form-inline pull-left">
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
					<div class="col-md-3">
						<div class="has-feedback">
							 <input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"
								autocomplete="off"> <span
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
								<th data-index="0">Receipt No</th>
								<th data-index="1">Chassis No</th>
								<th data-index="2">Type</th>
								<th data-index="3">Remitter</th>
								<th data-index="4">Bank</th>
								<th data-index="5">Amount</th>
								<th data-index="6">Customer</th>
								<th data-index="7">Sales Person</th>
								<th data-index="8">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>