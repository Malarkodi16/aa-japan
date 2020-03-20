<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Location Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Location Management</li>
	</ol>
</section>
<!--Location Fee List-->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="${contextPath }/master/location/create">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-3 pull-right mt-5">
							<div class="form-group">
								<button class="btn btn-primary pull-right" id="create-location">Create
									New Location</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
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
					<table id="table-master-location-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Type</th>
								<th data-index="1">Company</th>
								<th data-index="2">Address</th>
								<th data-index="3">Phone</th>
								<th data-index="4">Fax</th>
								<th data-index="5">Atsukai</th>
								<th data-index="6">Tantousha</th>
								<th data-index="7">Shipment Type</th>
								<th data-index="8">Shipment Origin Port</th>
								<th data-index="9">Action</th>
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