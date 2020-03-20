<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Exchange Rate List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Receipts</span></li>
		<li class="active">Exchange Rate List</li>

	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid ">
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
					<table id="table-list-exchange-rate"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1">Created Date</th>
								<th data-index="2">Created By</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>


	<!-- Exchange Rate Format Details -->
	<div id="exchange-rate-format" class="hidden">
		<div class="container-fluid exchange-rate-detail-view">
			<div class="box-body no-padding bg-darkgray">
				<table class="table">
					<thead>
						<tr>
							<th style="width: 25px">#</th>
							<th class="align-center">Currency</th>
							<th class="align-center">Exchange Rate</th>
							<th class="align-center">Sales Exchange Rate</th>
							<th class="align-center">Special Exchange Rate</th>
						</tr>
					</thead>
					<tbody>
						<tr class="clone-row hide">
							<td class="align-center s-no"><span></span></td>
							<td class="align-center currency"></td>
							<td class="align-center exchangeRate"></td>
							<td class="align-center salesExchangeRate"></td>
							<td class="align-center specialExchangeRate"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</section>