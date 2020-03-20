<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Owned Transaction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>TT Allocation</span></li>
		<li class="active">Owned Transaction</li>
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
					<table id="table-ownttAllocation"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th style="width: 50px" data-index="0">Remit Date</th>
								<th style="width: 50px" data-index="1">Remitter</th>
								<th style="width: 50px" data-index="2">Bank</th>
								<th style="width: 50px" data-index="3">Amount</th>
								<th style="width: 50px" data-index="4">Remark</th>
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