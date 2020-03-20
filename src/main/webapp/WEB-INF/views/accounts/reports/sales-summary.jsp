<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>Sales Summary</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Reports</span></li>
		<li class="active">Sales Summary</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="alert alert-success" id="alert-block"
			style="display: none"></div>
		<div class="box-header with-border">
			<input type="hidden" id="request-from-customer-filter-mobile-no"
				value="${mobileNo }"> <input type="hidden"
				id="request-from-customer-filter-city" value="${city }">
			<div class="container-fluid">
				<div class="row">
					<div class="form-group">
						<div class="col-md-3">
							<div class="form-group">
								<label>Customer</label> <select class="form-control customer"
									id="custId" style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>Sales Person</label> <select
									class="form-control salesPerson" id="select_sales_staff"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Maker</label> <select id="maker-filter"
									class="form-control" style="width: 100%;"
									data-placeholder="All">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Model</label> <select id="model-filter"
									class="form-control" style="width: 100%;"
									data-placeholder="All">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Sales Person Location</label> <select
									id="sales-location-filter" class="form-control"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>

					</div>
				</div>
				<div class="row form-group">
					<div class="form-group">
						<div class="col-md-2">
							<div class="form-group">
								<label>Destination Country</label> <select
									class="form-control destinationCountry" id="destinationCountry"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Destination Port</label> <select
									class="form-control destinationPort" id="destinationPort"
									style="width: 100%;" data-placeholder="All">
									<option value=""></option>
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
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="soldDate-form-group">
								<label>Sold Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-sold-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Stock Type</label> <select id="purchaseType"
									name="supplierType" class="form-control" style="width: 100%;"
									data-placeholder="All">
									<option value="">
									<option>
									<option value="1">Bidding
									<option>
									<option value="0">Stock
									<option>
								</select><span class="help-block"></span>
							</div>
						</div>
					</div>
				</div>
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
					<div class="col-md-3 form-inline pull-right">
						<div class="pull-right">
							<button class="btn btn-primary " type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
						</div>
					</div>
				</div>
			</div>


			<div class="box-body">
				<div class="container-fluid">
					<!-- table start -->
					<div class="table-responsive">
						<table id="table-sales-summary"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="1" class="align-center">Customer</th>
									<th data-index="2" class="align-center">Sales Person</th>
									<th data-index="3" class="align-center">Maker/Model</th>
									<th data-index="4" class="align-center">Chassis No</th>
									<th data-index="5" class="align-center">Selling Price</th>
									<th data-index="5" class="align-center">Exchange Rate</th>
									<th data-index="6" class="align-center">Selling Price(JPY)</th>
									<th data-index="7" class="align-center">Cost Of Goods(JPY)</th>
									<th data-index="8" class="align-center">Profit(JPY)</th>
									<th data-index="9" class="align-center">Purchase Date</th>
									<th data-index="10" class="align-center">Sold Date</th>
									<th data-index="11" class="align-center">Margin %</th>
									<th data-index="12" class="align-center">Dest.
										Country/Port</th>
									<th data-index="11" class="align-center">ETD</th>
									<th data-index="11" class="align-center">ETA</th>
									<th data-index="13" class="align-center">Sales Person
										Location</th>
									<th data-index="14" class="align-center">Payment Status</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="5" style="text-align: right"></th>
									<th>Selling Price(JPY) Total</th>
									<th>Cost Of Goods(JPY)Total</th>
									<th>Profit(JPY)Total</th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=sum>
									<th colspan="5" style="text-align: right"></th>
									<th class="dt-right"><span
										class="autonumber pagetotal sellingPriceTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal costOfgoodsTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal profitTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>

								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modal-sales-details">
		<div class="modal-dialog modal-lg" style="width: 90%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Sales Summary Details</h4>
				</div>
				<form id="form-sales-summary">
					<input type="hidden" name="stockNo" value="">
					<div class="modal-body">
						<div class="table-container" style="max-height: 400px">

							<table class="table margin-bottom-none">
								<thead>
									<tr>
										<th class="align-center">Chassis No.</th>
										<th class="align-center">Purchase Cost</th>
										<th class="align-center">Purchase Tax</th>
										<th class="align-center">Commision</th>
										<th class="align-center">Com.Tax</th>
										<th class="align-center">Recycle</th>
										<th class="align-center">Road Tax</th>
										<th class="align-center">Others</th>
										<th class="align-center">OthersTax</th>
										<th class="align-center">Transport</th>
										<th class="align-center">freight</th>
										<th class="align-center">Shipping</th>
										<th class="align-center">Insurance</th>
										<th class="align-center">Radiation</th>
										<th class="align-center">Inspection</th>
									</tr>
								</thead>
								<tbody>
									<tr class="clone">
										<td class="align-center chassisNo"></td>
										<td class="align-center purchaseCost"><span
											class="autonumber amount" name="purchaseCost"
											data-a-sign="¥ " data-m-dec="0"></span></td>
										<td class="align-center purchaseCostTax"><span
											class="autonumber amount" name="purchaseCostTax"
											data-a-sign="¥ " data-m-dec="0"></span></td>
										<td class="align-center commision"><span
											class="autonumber amount" name="commision" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center commisionTax"><span
											class="autonumber amount" name="commisionTax"
											data-a-sign="¥ " data-m-dec="0"></span></td>
										<td class="align-center recycle"><span
											class="autonumber amount" name="recycle" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center roadTax"><span
											class="autonumber amount" name="roadTax" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center otherCharges"><span
											class="autonumber amount" name="otherCharges"
											data-a-sign="¥ " data-m-dec="0"></span></td>
										<td class="align-center otherChargesTax"><span
											class="autonumber amount" name="otherChargesTax"
											data-a-sign="¥ " data-m-dec="0"></span></td>
										<td class="align-center transport"><span
											class="autonumber amount" name="transport" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center freight"><span
											class="autonumber amount" name="freight" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center shipping"><span
											class="autonumber amount" name="shipping" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center insurance"><span
											class="autonumber amount" name="insurance" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center radiation"><span
											class="autonumber amount" name="radiation" data-a-sign="¥ "
											data-m-dec="0"></span></td>
										<td class="align-center inspection"><span
											class="autonumber amount" name="inspection" data-a-sign="¥ "
											data-m-dec="0"></span></td>

									</tr>
								</tbody>
							</table>
						</div>
						<div class="summary-container">
							<div class="row form-group">
								<div class="col-md-10">
									<strong class="pull-right">Total</strong>
								</div>
								<div class="col-md-2">
									<span data-m-dec="0" data-a-sign="¥ " class="total pull-right"></span>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="modal-footer"></div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>