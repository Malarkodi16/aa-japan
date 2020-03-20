<section class="content-header">
	<h1>Trial Balance</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Trial Balance</li>
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
					<div class="col-md-2">
						<!-- <div class="form-group" id="date-form-group">
								<label>Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-trailBalance-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy">
								</div>
								/.input group
							</div> -->
						<div class="form-group">
							<label>Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-date" placeholder="dd-mm-yyyy">
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
						class="table table-bordered table-striped" style="width: 100%;">
						<thead>
							<tr>
								<th colspan="2"></th>
								<th colspan="2">Opening Balance</th>
								<th colspan="2">Month Movement</th>
								<th colspan="2">Ending Balance</th>
							</tr>
							<tr>
								<th data-index="0">Account No</th>
								<th data-index="1">Description / Account Name</th>
								<th data-index="2">Debit</th>
								<th data-index="3">Credit</th>
								<th data-index="4">Debit</th>
								<th data-index="5">Credit</th>
								<th data-index="6">Debit</th>
								<th data-index="7">Credit</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>

								<th colspan="2" data-index="0"></th>
								<th class="dt-right" data-index="1"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right" data-index="2"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right" data-index="3"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right" data-index="4"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right" data-index="5"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right" data-index="6"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0">0</span></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-subaccount-details">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Sub Account Details</h4>
				</div>
				<div class="modal-body" id="subAccountBody">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-3"></div>
							<div class="col-md-4">
								<div class="form-group" id="date-form-group">
									<label>Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control pull-right"
											id="table-filter-subAccountDetail-date"
											placeholder="dd-mm-yyyy - dd-mm-yyyy">
									</div>
									<!-- /.input group -->
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>&nbsp;</label>
									<button type="button" class="btn btn-primary form-control"
										id="btn-date-search">Search</button>
								</div>
							</div>
						</div>
						<div class="table-responsive">
							<table id="table-detail-subAccount"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Code</th>
										<th data-index="1">Date</th>
										<th data-index="2">Transaction Id</th>
										<th data-index="3">Account Name</th>
										<th data-index="4">Opening Balance</th>
										<th data-index="5">Debit</th>
										<th data-index="6">Credit</th>
										<th data-index="7">Closing Balance</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>