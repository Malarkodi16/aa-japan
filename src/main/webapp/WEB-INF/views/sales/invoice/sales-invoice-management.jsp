<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<section class="content-header">
	<h1>Sales Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Invoice Management</span></li>
		<li class="active">Sales Invoice</li>
	</ol>
</section>

<section class="content">
	<jsp:include page="/WEB-INF/views/sales/invoice/invoice-dashboard.jsp" />
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Sales Invoice</h3>
		</div>
		<div class="box-body">
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



			<!-- datatable -->

			<div class="container-fluid" id="sales-order-invoice-list">
				<!-- sales invoice show/search inputs -->
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
				<div class="table-responsive">
					<table id="table-sales-order-invoice"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1">Invoice No.</th>
								<th data-index="2">Customer</th>
								<th data-index="3">Consignee</th>
								<th data-index="4">Notify party</th>
								<th data-index="5">Payment Type</th>
								<th data-index="6">Created Date</th>
								<th data-index="7">CustomerId</th>
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


	<!-- Modal -->

	<div id="clone-container">
		<div id="sales-order-invoice-rearrange" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="salesOrderinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center bg-ghostwhite">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">Maker</th>
								<th data-index="4" class="align-center">Model</th>
								<th data-index="5" class="align-center">Fob</th>
								<th data-index="6" class="align-center">Shipping</th>
								<th data-index="7" class="align-center">Freight</th>
								<th data-index="8" class="align-center">Insurance</th>
								<th data-index="9" class="align-center">Status</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"></td>
								<td class="align-center stockNo"><input type="hidden"
									name="stockNo" value="" /><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="dt-right fob"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right shipping"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right freight"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="dt-right insurance"><span class="autonumber"
									data-m-dec="0"></span></td>
								<td class="align-center status"><span class="label"></span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>