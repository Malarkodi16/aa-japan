<section class="content-header">
	<h1>Bank Balance Transaction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Reports</span></li>
		<li class="active">Bank Balance Transaction</li>
	</ol>
</section>
<!-- Bank balance. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="container-fluid ">
					<div class="row form-group">
						<div class="col-md-3">
							<div class="form-group">
								<label>Bank</label> <select id="bank" name="bank"
									class="form-control select2-tag" style="width: 100%;"
									data-placeholder="Select Bank">
									<option value=""></option>
								</select>
							</div>
						</div>
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


						<div class="col-md-1">
							<div class="form-group">
								<label>&nbsp;</label>
								<button type="button" class="btn btn-primary form-control"
									id="btn-search">Search</button>
							</div>
						</div>
						<!-- 						<div class="col-md-3"> -->
						<!-- 							<div class="bank-balance hidden"> -->
						<!-- 								Current Balance : <span class="amount" data-a-sign="¥ " -->
						<!-- 									data-m-dec="0"></span> -->
						<!-- 							</div> -->
						<!-- 						</div> -->

					</div>
					<div class="row form-group">
						<div class="col-md-2">
							<div class="form-group hidden">
								<label>&nbsp;</label>
								<div class="form-control">
									<input type="checkbox" id="unClearedBalanceCheck"
										name="unClearedBalance" class="form-control" value="1"><label>UnCleared
										Balance</label>
								</div>
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
						<select id="table-filter-length" class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
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
								<th data-index="0">Date</th>
								<th data-index="1">Opening Balance</th>
								<th data-index="2">Debit</th>
								<th data-index="3">Credit</th>
								<th data-index="4">Closing Balance</th>
								<th data-index="5"></th>
								<th data-index="6">Description</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="1" style="text-align: right"></th>
								<th>Total Opening Balance</th>
								<th>Total Debit</th>
								<th>Total Credit</th>
								<th>Total Closing Balance</th>
								<th></th>
							</tr>
							<tr class=sum>
								<th colspan="1" style="text-align: right"></th>
								<th class="dt-right"><span
									class="autonumber pagetotal openingBalanceTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal debitTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal creditTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal closingBalanceTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>