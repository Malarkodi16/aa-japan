<section class="content-header">
	<h1>Supplier Statement</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Reports</li>
		<li class="active">Supplier Statement</li>
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
				<div class="row">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-transaction-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Supplier</label> <select id="supplier-filter"
								class="form-control select2-select" style="width: 100%;"
								data-placeholder="Select Supplier">
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
					<div class="col-md-5">
						<h3 class="pull-right">
							Opening Balance : <span id="opening-balance" class="autonumber"
								data-a-sign="¥ " data-m-dec="0">N/A</span>
						</h3>
					</div>
				</div>
			</div>
			<div class="container-fluid ">
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
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
				<div class="table-responsive">
					<table id="table-report-supplierStatement"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Date</th>
								<th data-index="1">Invoice No</th>
								<th data-index="2">Description</th>
								<th data-index="3">Debit</th>
								<th data-index="4">Credit</th>
								<th data-index="5">Balance</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="3" style="text-align: right"></th>
							<th>Total Debit</th>
							<th>Total Credit</th>
							<th>Total Balance</th>
						</tr>
						<tr class=sum>
							<th colspan="3" style="text-align: right"></th>
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