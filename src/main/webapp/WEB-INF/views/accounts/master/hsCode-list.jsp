<section class="content-header">
	<h1>HS Code Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active">HS Code Management</li>
	</ol>
</section>
<!--HS Code List-->
<section class="content">
	<div class="alert" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="row form-group">
					<div class="col-md-6">
						<div class="form-inline">
							<input type="file" id="excelUploaded" name="excelUploaded"
								class="form-control" data-directory="excelUploaded_dir"
								accept=".xlsx,.xls" />
							<button type="button" class="btn btn-primary" id="save-excel">Save
								Excel</button>
						</div>
					</div>
					
				</div>
				<div class="row">
				<div class="col-md-2">
							<div class="form-group">
								<label>Category</label> <select id="table-filter-category"
									name="category" class="form-control pull-right"
									data-placeholder="Select Category">
									<option value=""></option>
								</select>
							</div>
						</div>
				</div>
				
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Forwarder List show/search inputs -->
				<div class="row form-group">
					<div class="form-group">
						


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
					<div class="col-md-8">
					<div class="pull-right">
						<button class="btn btn-primary" id="new_hsCode" type="button"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-add-hsCode">Create HS Code</button>
					</div>
					</div>
				</div>
				</div>
				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-master-hsCode-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0" name="CC">CC</th>
								<th data-index="1" name="category">Category</th>
								<th data-index="2" name="subCategory">Sub Category</th>
								<th data-index="3" name="hscode">HS Code</th>
								<th data-index="4" name="action">Action</th>
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

<form action="#" id="hsCode-form">
	<div class="modal fade" id="modal-add-hsCode">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create HS Code</h4>
				</div>
				<div class="modal-body">
					<div class="box-body">
						<div class="container-fluid">
							<div class="row">
								<input type="hidden" name="code" id="code" class="form-control" />
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">CC</label>
										<div class="element-wrapper">
												
												<input name="cc" id="cc" placeholder="Enter CC" type="text"
													class="form-control autonumber" />
											
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Category</label>
										<div class="element-wrapper">
												
												<select class="form-control select2" id="categoryDrop"
													name="category" data-placeholder="Select Category">
													<option value=""></option>
												</select>
											
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Sub Category</label>
										<div class="element-wrapper">
										<select class="form-control select2" id="subCategoryDrop"
													name="subCategory" data-placeholder="Select Sub Category" style="width:100%">
													<option value=""></option>
												</select>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">HS Code</label>
										<div class="element-wrapper">
												
												<input name="hsCode" id="hsCode" placeholder="Enter HS Code"
													type="text" class="form-control" />
											</div>
											<span class="help-block"></span>

									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- /.box-body -->
					<div class="box-footer ">
						<div class="pull-right">
							<button type="button" class="btn btn-primary " id="save-hsCode">
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
	</div>
</form>
<!-- /.modal -->