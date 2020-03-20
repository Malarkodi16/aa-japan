<section class="content-header">
	<h1>General Ledger</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Reports</li>
		<li class="active">GL Report</li>
	</ol>
</section>
<!-- trail balance. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid ">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-date" placeholder="dd-mm-yyyy - dd-mm-yyyy">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="button" class="btn btn-primary form-control"
								id="btn-search">Search</button>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid ">
				<div class="row form-group">
				<div class="col-md-1 form-inline pull-left">
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
								class="form-control" placeholder="Search by keyword">
							<span class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-6 form-inline pull-right">
						<div class="pull-right">
						<button class="btn btn-primary " type="button"
								id="btn-export-excel">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-report-gl"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"></th>
								<th data-index="1"></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="genaral-ledger-transaction" class="hidden">
		<div class="container-fluid details-container">
			<table class="table">
				<thead>
					<tr>
						<th>#</th>
						<th>Code</th>
						<th>Reporting Category</th>
						<th>Account</th>
						<th>Sub Account</th>
						<th>Balance</th>
					</tr>
				</thead>
				<tbody>
					<tr class="clone-row hide">
						<td class="sNo"></td>
						<td class="code"></td>
						<td class="reportingCategory"></td>
						<td class="account"></td>
						<td class="subAccount"></td>
						<td class="dt-right balance"><span class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</section>