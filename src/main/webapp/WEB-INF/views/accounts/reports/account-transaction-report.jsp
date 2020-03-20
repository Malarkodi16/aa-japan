<section class="content-header">
	<h1>Account Transaction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Account Transaction</li>
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

					<div class="col-md-8">
						<div class="pull-right">
							
							<div class="input-group">
								<button class="btn btn-primary pull-right" type="button"
									id="excel_export_all">
									<i class="fa fa-file-excel-o" aria-hidden="true"> Export
										Excel</i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-report-trailBalance"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Code</th>
								<th data-index="1">Account Name</th>
								<th data-index="2">Sub Account Type</th>
								<th data-index="3">Balance</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="3" style="text-align: right"></th>
							<th>Total Balance</th>
						</tr>
						<tr class=sum>
							<th colspan="3" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal totalBalance" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
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
						<div class="row form-group">
							<div class="col-md-1 form-inline">
								<div class="form-group">
									<label></label> <select id="table-filter-subAcc-length"
										class="form-control">
										<option value="10">10</option>
										<option value="25" selected="selected">25</option>
										<option value="100">100</option>
										<option value="1000">1000</option>
									</select>
								</div>
							</div>
							<div class="col-md-4 ml-10">
								<div class="has-feedback">
									<label></label> <input type="text"
										id="table-filter-subAcc-search" class="form-control"
										placeholder="Search by keyword" autocomplete="off"> <span
										class="glyphicon glyphicon-search form-control-feedback"></span>
								</div>
							</div>
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
										id="btn-search">Search</button>
								</div>
							</div>
							<div class="col-md-2 form-inline pull-right">
								<div class="form-group">
									<label></label>
									<div class="input-group">
										<button class="btn btn-primary pull-right" type="button"
											id="excel_export_sub_account">
											<i class="fa fa-file-excel-o" aria-hidden="true"> Export
												Excel</i>
										</button>
									</div>
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
										<th data-index="3">Invoice No</th>
										<th data-index="4">Account Name</th>
										<th data-index="5">Opening Balance</th>
										<th data-index="6">Debit</th>
										<th data-index="7">Credit</th>
										<th data-index="8">Closing Balance</th>
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