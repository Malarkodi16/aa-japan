<section class="content-header">
	<h1>Forwarder Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Forwarder Management</li>
	</ol>
</section>
<!--Port List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="pull-right">
						<button class="btn btn-primary" id="new_forwarder" type="button"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-add-forwarder">Create Forwarder</button>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Forwarder List show/search inputs -->
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
					<table id="table-master-forwarder-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Code</th>
								<th data-index="1">Name</th>
								<th data-index="2">Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>

<div class="modal fade" id="modal-add-forwarder">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">Forwarder</h4>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<div class="container-fluid">
						<form action="#" id="forwarder-form">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="${code}">
									<div class="form-group">
										<label class="required">Name</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-forward"></i>
												</div>
												<input name="name" id="name"
													placeholder="Enter Forwarder Name" type="text"
													class="form-control" />
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
				<!-- /.box-body -->
				<div class="box-footer ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary " id="save-forwarder">
							<i class="fa fa-fw fa-save"></i>Save
						</button>
						<button type="button" class="btn btn-primary "
							data-dismiss="modal">
							<i class="fa fa-fw fa-save"></i>Close
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
</form>
<!-- /.modal -->