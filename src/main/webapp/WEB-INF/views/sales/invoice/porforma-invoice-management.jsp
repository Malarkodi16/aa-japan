<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="canAccess(117)">
<section class="content-header">
	<h1>Proforma Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Invoice Management</span></li>
		<li class="active">Proforma Invoice</li>
	</ol>
</section>
</sec:authorize>
<section class="content">
<sec:authorize access="canAccessAny(117,118)">
	<jsp:include page="/WEB-INF/views/sales/invoice/invoice-dashboard.jsp" />
</sec:authorize>
<sec:authorize access="canAccess(117)">
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Proforma Invoice</h3>
		</div>
		<div class="box-body">

			<!-- Date Filter -->
			<div class="row form-group">
				<div class="container-fluid">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Invoice Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-invoice-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy">
							</div>
							<!-- /.input group -->
						</div>
					</div>

					<!-- Customer Wise Filter -->

					<div class="row form-group">
						<div class="col-md-3">
							<div class="form-group">
								<label>Customer</label> <select class="form-control customer"
									id="custId" style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
					</div>
				</div>
			</div>




			<div class="container-fluid" id="proforma-invoice-details">

				<!-- porforma invoice show/search inputs -->
				<div class="row form-group">
				<div class="col-md-1 pull-left">
				<select id="table-filter-length"
								class="form-control input-sm">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
							</select>
							</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-6 form-inline pull-right">
						<div class="pull-right">
							<button class="btn btn-primary " type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
							
						</div>
					</div>
				</div>
				<!-- Performa Invoice Datatable -->
				<div class="table-responsive">
					<table id="table-proformainvoice"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1">Date</th>
								<th data-index="2">Invoice No.</th>
								<th data-index="3">Customer</th>
								<th data-index="4">Consignee</th>
								<th data-index="5">Notify Party</th>
								<th data-index="6">Payment Type</th>
								<th data-index="7">Total</th>
								<th data-index="8"  class="align-center">Action</th>
								<th data-index="9">CustomerId</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>
</section>