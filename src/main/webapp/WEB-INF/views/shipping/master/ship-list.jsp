<section class="content-header">
	<h1>Ship Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Ship Management</li>
	</ol>
</section>
<!--Ship List-->
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
						<button class="btn btn-primary" id="new_ship" type="button"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-add-ship">Add Ship Name</button>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Ship List show/search inputs -->
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
					<table id="table-master-ship-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0" name="shipId">Ship Id</th>
								<th data-index="1" name="name">Ship Name</th>
								<th data-index="2" name="action">Action</th>
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

<div class="modal fade" id="modal-add-ship">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title">Add Ship Name</h3>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<form action="#" id="ship-form">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="shipId" name="shipId" value="">
									<div class="form-group">
										<label class="required">Ship Name</label>
										<div class="element-wrapper">
											<input type="text" id="name" name="name" class="form-control"
												placeholder="Ship Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button id="add-ship" class="btn btn-primary">
					<em class="fa fa-fw fa-save"></em> Add
				</button>
				<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
					<em class="fa fa-fw fa-close"></em> Close
				</button>
			</div>
		</div>
	</div>
</div>
