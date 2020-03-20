<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Inspection Company Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Inspection Company Management</li>
	</ol>
</section>
<!--Port List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="${contextPath }/master/inspection/inspectionCompany">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row form-group">

						<div class="col-md-3 pull-right mt-5">
							<div class="form-group">
								<button class="btn btn-primary pull-right"
									id="create-inspection-company">Create New Inspection
									Company</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Transporter List show/search inputs -->
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
					<table id="table-master-inspection-company-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Inspection Company</th>
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
		<div id="inspection-company-list-details-view" class="hidden">
			<div class="container-fluid inspection-company-detail-view">
				<input type="hidden" name="code" value="" />
				<div class="box-body no-padding bg-darkgray">
					<table class="table">
						<thead>
							<tr>
								<th style="width: 25px">#</th>
								<th class="align-center">Location Name</th>
								<th class="align-center">Zip</th>
								<th class="align-center">Tel</th>
								<!-- <th class="align-center">Subcategory</th> -->
								<th class="align-center">Fax</th>
								<th class="align-center">Contact Person</th>
								<th class="align-center">Address</th>
								<th class="align-center" style="width: 150px;">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="align-center s-no"><span></span><input
									type="hidden" name="locationId" value=""></td>
								<td class="align-center locationName"></td>
								<td class="align-center zip"></td>
								<td class="align-center tel"></td>
								<!-- <td class="align-center subcategory"></td> -->
								<td class="align-center fax"></td>
								<td class="align-center contactPerson"></td>
								<td class="align-center address"></td>
								<td class="action align-center">
									<button class="ml-5 btn btn-danger btn-xs" id="delete-location"
										title="delete">
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
</section>
<!-- <form action="#" id="inspection-company-form">
	<div class="modal fade" id="modal-add-inspection-company">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Transporter</h4>
				</div>
				<div class="modal-body">
					<div class="box-body">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6">
									<input type="hidden" id="code" name="code" value="">
									<div class="form-group">
										<label class="required">Name</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-forward"></i>
												</div>
												<input name="name" id="name"
													placeholder="Enter Transporter Name" type="text"
													class="form-control" />
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
					/.box-body
					<div class="box-footer ">
						<div class="pull-right">
							<button type="button" class="btn btn-primary "
								id="save-transporter">
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
			/.modal-content
		</div>
		/.modal-dialog
	</div>
</form> -->
<!-- /.modal -->