<section class="content-header">
	<h1>Profit And Loss</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Profit &amp; Loss</li>
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
						<div class="form-group">
							<label>Date Range</label> <select id="selectDateRange"
								name="selectDateRange" class="form-control"
								data-placeholder="Select Date Range" style="width: 100%;">
								<option></option>
								<option value=0>Between Dates</option>
								<option value=1>Last Financial Year</option>
								<option value=2>Current Finanacial Year</option>
								<option value=3>Last 6 Months</option>
								<option value=4>Last 3 Months</option>
							</select>
						</div>
					</div>
					<div class="col-md-3 hidden" id="betweenDatesSelected">
						<div class="form-group" id="date-form-group">
							<label>Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-profit-loss-date"
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
				</div>
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
								id="btn-export-excel">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-report-profitAndLoss"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Account Name</th>
								<th data-index="1">PTD Amount</th>
								<th data-index="2">YTD Amount</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr class="total">
								<th>Profit or Loss</th>
								<th class="dt-right ptdTotal"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></th>
								<th class="dt-right ytdTotal"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></th>
							</tr>
							<tr class="totalTax">
								<th><div class="col-md-3">Profit or Loss After Tax</div>
									<div class="col-md-2">
										<input class="form-control tax-percentange" type=number
											value=8 name="tax" />
									</div></th>
								<th class="dt-right ptdTotalTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></th>
								<th class="dt-right ytdTotalTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Balance Sub Account Details -->
	<div class="hide" id="clone-element-container">
		<div
			class="box-body no-padding bg-darkgray profit-loss-item-container">
			<div class="table-responsive">
				<table class="table table-bordered" style="overflow-x: auto;">
					<thead>
						<tr>
							<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
							<th class="align-center bg-ghostwhite">Code</th>
							<th class="align-center bg-ghostwhite">Sub Account</th>
							<th class="align-center bg-ghostwhite">PTD Amount</th>
							<th class="align-center bg-ghostwhite">YTD Amount</th>
						</tr>
					</thead>
					<tbody>
						<tr class="hide clone-row">
							<td class="align-center s-no"><span></span></td>
							<td class="align-center code"></td>
							<td class="align-center subAccount"></td>
							<td class="dt-right ptdAmount"><span class="autonumber"
								data-a-sign="¥ " data-m-dec="0">0</span></td>
							<td class="dt-right ytdAmount"><span class="autonumber"
								data-a-sign="¥ " data-m-dec="0">0</span></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</section>