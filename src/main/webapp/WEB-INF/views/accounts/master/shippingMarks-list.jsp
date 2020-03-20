<section class="content-header">
	<h1>Shipping Marks Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active">Shipping Marks</li>
	</ol>
</section>
<!--Shipping Marks List-->
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
						<button class="btn btn-primary" id="new_shippingMarks"
							type="button" data-backdrop="static" data-keyboard="false"
							data-toggle="modal" data-target="#modal-add-shippingMarks">Add
							Shipping Marks</button>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Shipping Marks List show/search inputs -->
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
				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-master-shippingMarks-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0" name="marksId" class="align-center">Marks
									ID</th>
								<th data-index="1" name="name" class="align-center">Name</th>
								<th data-index="2" name="shippingMarks" class="align-center">Shipping
									Marks</th>
								<th data-index="3" name="action" class="align-center">Action</th>
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

<div class="modal fade" id="modal-add-shippingMarks">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title">Add Shipping Marks</h3>
			</div>
			<div class="modal-body">
				<div class="box-body">
					<form action="#" id="shippingMarks-form">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="marksId" name="marksId" value="">
									<div class="form-group">
										<label class="required">Name</label>
										<div class="element-wrapper">
											<input type="text" id="name" name="name" class="form-control"
												placeholder="Name" required="required">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Shipping Marks</label>
										<div class="element-wrapper">
											<!-- <input type="text" id="shippingMarks" name="shippingMarks"
												class="form-control" placeholder="Shipping Marks"> -->
											<textarea name=shippingMarks cols="35px" rows="5px"
												placeholder="Shipping Marks" id="shippingMarks"></textarea>
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
				<button id="save-shippingMarks" class="btn btn-primary">
					<em class="fa fa-fw fa-save"></em> Add
				</button>
				<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
					<em class="fa fa-fw fa-close"></em> Close
				</button>
			</div>
		</div>
	</div>
</div>
