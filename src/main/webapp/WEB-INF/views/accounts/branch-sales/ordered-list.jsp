<section class="content-header">
	<h1>Branch Sales Order List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Branch Sales Order List</li>
	</ol>
</section>
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
							<label></label><select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					
				</div>
				<div class="table-responsive">
					<table id="table-branch-salesOrder-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Customer Name</th>
								<th data-index="1">Company</th>
								<th data-index="2">Email</th>
								<th data-index="3">Mobile No</th>
								<th data-index="4">Country</th>
								<th data-index="5">Port</th>
								<th data-index="6">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Stock Details -->
	<div id="clone-container">
		<div id="branch-sales-order" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center bg-ghostwhite">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">Maker</th>
								<th data-index="4" class="align-center">Model</th>
								<th data-index="5" class="align-center">Total</th>
								<th data-index="6" class="align-center">Sold Date</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"></td>
								<td class="align-center stockNo"><input type="hidden"
									name="stockNo" value="" /><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="align-center total"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="align-center salesDate"></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>