<section class="content-header">
	<h1>Country Port Map Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Country Port Map</li>
	</ol>
</section>
<!--Port List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Location List show/search inputs -->
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
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-master-countryPort-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Continent</th>
								<th data-index="1">Country</th>
								<th data-index="2">Port</th>
								<th data-index="3">Yard</th>
								<th data-index="4">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.Countries port edit-->
	<div class="modal fade" id="modal-edit-port" tabindex="-1">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Country Port Map</h3>
				</div>
				<div class="modal-body">
					<input type="hidden" id="country" name="country" value="">
					<div class="form-group row">
						<div class="col-md-6 form-group">
							<label>Port</label>
							<div class="element-wrapper">
								<select name="port" multiple="multiple"
									data-placeholder="Select ports"
									class="form-control port select2-tag" style="width: 100%;"
									multiple="multiple">
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>

				</div>
				<div class="modal-footer">
					<button id="save-port-map" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>