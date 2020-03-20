<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Income By Customer</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Inventory Reports</span></li>
		<li class="active">Income By Customer</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-3">
						<div class="form-group">
							<label>Customer</label> <select
								class="form-control select2 customer" id="custId" name="custId"
								style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
				</div>
				<!-- <div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-customer-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
								</div>
								/.input group
							</div>
						</div> -->
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
							<label></label> <input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"
								autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-income-by-customer"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Customer</th>
								<th data-index="1">No. of Stock</th>
								<th data-index="2">Purchase Price</th>
								<th data-index="3">Selling Price</th>
								<th data-index="4">Margin</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="2" style="text-align: right"></th>
							<th>Purchase Price(JPY) Total</th>
							<th>Selling Price(JPY)Total</th>
							<th>Margin(JPY)Total</th>
							
						</tr>
						<tr class=sum>
							<th colspan="2" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal totalPurchasePrice" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal totalSellingPrice" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal totalMargin"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							
						</tr>
					</tfoot>
					</table>
				</div>
			</div>
		</div>
		<!-- /.form:form -->
	</div>
	<div id="clone-container">
		<div id="stock-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive sales-item-container">
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Chassis No.</th>
								<th data-index="2" class="align-center">Sales Date</th>
								<th data-index="3" class="align-center">Purchase Price</th>
								<th data-index="4" class="align-center">Selling Price</th>
								<th data-index="5" class="align-center">Margin</th>

							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center salesDate"></td>
								<td class="dt-right purchasePrice"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right sellingPrice"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right margin"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>

							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

</section>
