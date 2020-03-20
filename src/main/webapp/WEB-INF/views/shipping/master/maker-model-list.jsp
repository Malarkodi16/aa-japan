<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Maker Model Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Maker Model Management</li>
	</ol>
</section>

<!--Tranaspoter Fee List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="${contextPath }/master/maker">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row form-group">

						<div class="col-md-3 pull-right mt-5">
							<div class="form-group">
								<button class="btn btn-primary pull-right" id="create-maker">Create
									New Maker</button>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-inline">
								<input type="file" id="excelUploaded" name="excelUploaded"
									class="form-control" data-directory="excelUploaded_dir"
									accept=".xlsx,.xls" />
								<button class="btn btn-primary" id="save-excel">Save
									Excel</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Maker/Model List show/search inputs -->
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
					<div class="col-md-5 form-inline pull-right">
						<div class="pull-right">
							<button class="btn btn-primary btn-sm" type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>

						</div>
					</div>
				</div>
				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-master-maker-model-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Vehicle Name</th>
								<th>Maker</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div id="maker-model-list-details-view" class="hidden">
			<div class="container-fluid maker-model-detail-view">
				<input type="hidden" name="makerModelCode" value="" />
				<div class="box-body no-padding bg-darkgray">
					<table class="table">
						<thead>
							<tr>
								<th style="width: 25px">#</th>
								<th class="align-center auction-house">Model Name</th>
								<th class="align-center">Category</th>
								<th class="align-center">Transport Category</th>
								<!-- <th class="align-center">Subcategory</th> -->
								<th class="align-center">M3</th>
								<th class="align-center">Length</th>
								<th class="align-center">Width</th>
								<th class="align-center">Height</th>
								<th class="align-center" style="width: 150px;">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="align-center s-no"><span></span><input
									type="hidden" name="code" value=""><input type="hidden"
									name="modelList" value=""></td>
								<td class="align-center modelName"></td>
								<td class="align-center category"></td>
								<td class="align-center transportCategory"></td>
								<!-- <td class="align-center subcategory"></td> -->
								<td class="align-center m3"></td>
								<td class="align-center length"></td>
								<td class="align-center width"></td>
								<td class="align-center height"></td>
								<td class="action align-center">
									<button class="ml-5 btn btn-info btn-xs" type="button"
										title="Edit Model" id="model-edit" data-toggle="modal"
										data-target="#modal-edit-model" name="editButton" value="edit">
										<i class="fa fa-fw fa-edit"></i>
									</button>
									<button class="ml-5 btn btn-danger btn-xs delete-model"
										id="delete-model" title="delete">
										<i class="fa fa-fw fa-close"></i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.maker model edit-->
	<div class="modal fade" id="modal-add-model">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Model</h3>
				</div>
				<div class="modal-body">
					<form id="modal-add-model-form">
						<input type="hidden" id="code" name="code" value="">
						<div class="row form-group model-row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Model Name</label>
									<div class="element-wrapper">
										<input type="text" name="modelName"
											class="form-control data-required model">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<!-- <div class="col-md-3 subModel">
								<div class="row">
									<div class="form-group">
										<label>Sub Model</label>
										<div class="element-wrapper">
											<select name="subModelName" multiple="multiple"
												data-placeholder="Enter Sub Model"
												class="form-control model select2-tag" style="width: 100%;"
												multiple="multiple">
											</select>
										</div>
										<span class="help-block"></span>
									</div>
									<div class="col-md-4"></div>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Category</label>
									<div class="element-wrapper">
										<select
											class="form-control select2-select data-required categorySelect"
											name="category" id="category"
											data-placeholder="Select Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<!-- <div class="col-md-3">
								<div class="form-group">
									<label class="required">Sub Category</label>
									<div class="element-wrapper">
										<select
											class="form-control select2-select data-required subcategory"
											name="subcategory" id="subcategory"
											data-placeholder="Select Sub Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">M3</label>
									<div class="element-wrapper">
										<input type="text" name="m3"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Length</label>
									<div class="element-wrapper">
										<input type="text" name="length"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Width</label>
									<div class="element-wrapper">
										<input type="text" name="width"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>

						<div class="row">

							<div class="col-md-2">
								<div class="form-group">
									<label>Height</label>
									<div class="element-wrapper">
										<input type="text" name="height"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-m-sep="" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Transport Category</label>
									<div class="element-wrapper">
										<select
											class="form-control select2-select data-required transportCategorySelect"
											name="transportCategory" id="transportCategory"
											data-placeholder="Transport Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-model" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./.maker model edit-->
	<div class="modal fade" id="modal-edit-model">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Model</h3>
				</div>

				<div class="modal-body">
					<form id="modal-edit-model-form">
						<!-- to check duplicate sub Category -->
						<input type="hidden" name="makerName" value=""> <input
							type="hidden" name="modelId" value=""> <input
							type="hidden" name="category" value="">
						<div class="row form-group model-row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Model Name</label>
									<div class="element-wrapper">
										<input type="text" name="modelName"
											class="form-control data-required model">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Category</label>
									<div class="element-wrapper">
										<select class="form-control select2-select categorySelect"
											name="category" id="category"
											data-placeholder="Select Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">M3</label>
									<div class="element-wrapper">
										<input type="text" name="m3"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Length</label>
									<div class="element-wrapper">
										<input type="text" name="length"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Width</label>
									<div class="element-wrapper">
										<input type="text" name="width"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<!-- <div class="col-md-6">
								<div class="form-group">
									<label class="required">Sub Category</label>
									<div class="element-wrapper">
										<select class="form-control select2-select subcategory"
											name="subcategory" id="subcategory"
											data-placeholder="Select Sub Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div> -->
						</div>
						<div class="row">

							<div class="col-md-2">
								<div class="form-group">
									<label>Height</label>
									<div class="element-wrapper">
										<input type="text" name="height"
											class="form-control model autonumber data-required"
											data-m-dec="0" data-m-sep="" data-a-sep="">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Transport Category</label>
									<div class="element-wrapper">
										<select
											class="form-control select2-select data-required transportCategorySelect"
											name="transportCategory" id="transportCategory"
											data-placeholder="Transport Category" style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="edit-model" class="btn btn-primary">
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