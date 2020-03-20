<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Transporter Fee Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Transporter Fee Management</li>
	</ol>
</section>

<!--Tranaspoter Fee List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="${contextPath }/transport/transporter/fee/create">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-3 pull-right mt-5">
							<div class="form-group">
								<button class="btn btn-primary pull-right"
									id="create-transporter">Create Transport Charge</button>
							</div>
						</div>
						
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<div class="row">
					<div class="form-group">
						<div class="col-md-2">
							<label>Transporter</label> <select
								class="form-control transporter" id="transporter"
								style="width: 100%;" data-placeholder="Select Transporter">
								<option value=""></option>
							</select>
						</div>
						<div class="col-md-2">
							<label>From Location</label> <select class="form-control from"
								id="from" style="width: 100%;"
								data-placeholder="Select From Location">
								<option value=""></option>
							</select>
						</div>
						<div class="col-md-2">
							<label>To Location</label> <select class="form-control to"
								id="to" style="width: 100%;"
								data-placeholder="Select To Location">
								<option value=""></option>
							</select>
						</div>

					</div>
				</div>
			</div>
			<div class="container-fluid form-group mt-25">
				<!-- Tranaspoter Fee List show/search inputs -->
				<div class="row">
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
					<div class="col-md-2 pull-right mt-8">
							<div class="form-group">
							<button type="button" class="btn btn-primary" data-toggle="modal" data-backdrop="static" data-target="#add-new-trn">
								<i class="fa fa-fw fa-plus"></i>Add New Transporter
							</button>
							</div>
						</div>
				</div>

				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-transporter-fee-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" class="align-center">Transporter</th>
								<th data-index="1" class="align-center">From Location</th>
								<th data-index="2" class="align-center">To Location</th>
								<th data-index="3" class="align-center">Transport Category</th>
								<th data-index="4" class="align-center">Amount</th>
								<th data-index="5" class="align-center">Action</th>
								<th data-index="6" class="align-center">Transporter Id</th>
								<th data-index="7" class="align-center">From Id</th>
								<th data-index="8" class="align-center">To Id</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Model -->
	<div class="modal fade" id="add-new-trn">
		<div class="modal-dialog">
			<div class="modal-content">
				<form id="add-new-trn-form">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Add New Transporter</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="col-md-6">
								<label class="required">Transporter Name</label> <input type="text"
									name="trn_name" id="trn_name"
									class="form-control required" /> <span class="help-block"></span>
							</div>
						</div>
						<!-- /.form:form -->
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-create-trn">
								<i class="fa fa-fw fa-save"></i>Create
							</button>
							<button class="btn btn-primary" id="btn-close"
								data-dismiss="modal">
								<i class="fa fa-fw fa-close"></i>Close
							</button>
						</div>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<!-- Model -->
</section>