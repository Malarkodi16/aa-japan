<section class="content-header">
	<h1>Balance Sheet Statement</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Balance Sheet Statement</li>
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
						<div class="form-group">
							<label>As Of Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
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
					<table id="table-report-balanceStatement"
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
						<tr>
							<th colspan="2" style="text-align: right"></th>
							<th>YTD Total Amount</th>
						</tr>
							<tr class=sum>
							<th colspan="2" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal soldAmountTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
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
			class="box-body no-padding bg-darkgray sub-account-item-container">
			<div class="table-responsive sub-account-container">
				<table class="table table-bordered" style="overflow-x: auto;">
					<thead>
						<tr>
							<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
							<th class="align-center bg-ghostwhite">Code</th>
							<th class="align-center bg-ghostwhite">Sub Account</th>
							<!-- <th class="align-center bg-ghostwhite">PTD Amount</th> -->
							<th class="align-center bg-ghostwhite">YTD Amount</th>
						</tr>
					</thead>
					<tbody>
						<tr class="hide clone-row">
							<td class="align-center s-no"><span></span></td>
							<td class="align-center code"></td>
							<td class="align-center subAccount"></td>
							<!-- <td class="align-center ptdAmount"></td> -->
							<td class="dt-right ytdAmount"><span class="autonumber"
								data-a-sign="¥ " data-m-dec="0"></span></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</section>