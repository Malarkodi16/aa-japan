<section class="content-header">
	<h1>GL Transaction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Reports</li>
		<li class="active">GL Transaction</li>
	</ol>
</section>
<!-- trail balance. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="container-fluid ">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-trailBalance-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy">
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>Sub Account</label> <select id="sub-account-filter"
									class="form-control select2-select sub-account"
									style="width: 100%;" data-placeholder="Select Sub Account">
									<option value=""></option>
								</select> <span class="help-block"></span>
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
			</div>
		</form>
		<div class="box-body">
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
					<table id="table-report-trailBalance"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Transaction Id</th>
								<th data-index="1">Ref. No</th>
								<th data-index="2">Date</th>
								<th data-index="3">Code</th>
								<th data-index="4">Account Name</th>
								<th data-index="5">Description</th>
								<th data-index="6">Debit</th>
								<th data-index="7">Credit</th>
								<th data-index="8">Balance</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="6" style="text-align: right"></th>
							<th>Total Debit</th>
							<th>Total Credit</th>
							<th>Total Balance</th>
						</tr>
						<tr class=sum>
							<th colspan="6" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal debitTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal creditTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal balanceTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
						</tr>
					</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>