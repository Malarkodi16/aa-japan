<section class="content-header">
	<h1>Inventory Value Report</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Inventory Value</li>
	</ol>
</section>
<!-- Inventory Value. -->
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
							<label for="maker">Maker</label> <select
								class="form-control maker-dropdown" name="makers" id="makers"
								data-placeholder="Makers">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="model">Model</label> <select class="form-control"
								name="models" id="models" data-placeholder="Models">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchase Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-purchase-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="currentLocation">Current Location</label> <select
								class="form-control" name="currentLocation" id="currentLocation"
								data-placeholder="Current Location">
								<option value=""></option>
							</select>
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

				</div>
				<div class="table-responsive">
					<table id="table-report-inventory-value"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Stock No</th>
								<th data-index="1">Chassis No</th>
								<th data-index="2">Maker</th>
								<th data-index="3">Model</th>
								<th data-index="4">Purchase Date</th>
								<th data-index="5">Reserved</th>
								<th data-index="6">Reserved By</th>
								<th data-index="7">Current Location</th>
								<th data-index="8">Purchase Cost</th>
								<th data-index="9">Commission Cost</th>
								<th data-index="10">Other Charges</th>
								<th data-index="11">Inventory Cost</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="8" style="text-align: right"></th>
								<th>Purchase Cost Total</th>
								<th>Commission Total</th>
								<th>Others Total</th>
								<th>Inventory Total</th>
							</tr>
							<tr class=sum>
								<th colspan="8" style="text-align: right"></th>
								<th class="dt-right"><span
									class="autonumber pagetotal purchaseTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal commisionTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal othersTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th class="dt-right"><span
									class="autonumber pagetotal inventoryCost" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-inventoryCost-details">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Inventory Cost Details</h4>
				</div>
				<div class="modal-body" id="inventoryCostBody">
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-detail-inventoryCost"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="1">Type</th>
										<th data-index="2">Amount</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>