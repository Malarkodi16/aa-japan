<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header ">
	<h1>Cost of Sales</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Cost of Sales</li>
	</ol>
</section>
<section class="content ">
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<%-- <jsp:include page="/WEB-INF/views/accounts/cost-of-sales-filters.jsp" /> --%>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Sales Date</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control datepicker"
									id="salesDate" placeholder="dd-mm-yyyy" readonly>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-2 form-inline pull-right">
						<div class="pull-right">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered table-striped"
						id="table-cost-of-sales" role="grid"
						aria-describedby="table_cost_of_sales">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Stock No.</th>
								<th>Chassis No.</th>
								<th>Stock Type</th>
								<th>Selling Price</th>
								<th>Exchage Rate Selling Price</th>
								<th>Purchase Price</th>
								<th>Margin</th>
								<th>ETD</th>
								<th>ETA</th>
								<th>Purchase Date</th>
								<th>Sales Date</th>
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