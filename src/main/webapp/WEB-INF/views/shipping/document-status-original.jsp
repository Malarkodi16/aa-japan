<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>Document Status Original</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Document Status</span></li>
		<li class="active">Original</li>
	</ol>
</section>

<section class="content">
	<jsp:include page="/WEB-INF/views/shipping/dashboard-document.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-3">
						<label>Customer</label> <select class="form-control customer"
							id="custId" style="width: 100%;"
							data-placeholder="Search by Customer ID, Name, Email">
							<option value=""></option>
						</select> <span class="help-block"></span>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="bl-status">
						<label>Status</label>
							<select name="blStatus" class="form-control select2 status"
								data-placeholder="Status">
								<option value=""></option>
								<option value="0">Not Received</option>
								<option value="1">Received</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row form-group">
					
					
					<div class="col-md-1 pull-left">
						<select id="table-filter-length" class="form-control">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>
					<div class="col-md-3 pull-left">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-8">
					<div class="pull-right">
						<button type="button" id="update-status" class="btn btn-primary">
							<i class="fa fa-fw fa-save" title="BL Status Update"></i> Update
							Status
						</button>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="table-responsive">
					<table id="table-document-original-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th>Stock Id</th>
								<th>Customer</th>
								<th>Ship</th>
								<th>Origin Country</th>
								<th>Origin Port</th>
								<th>Destination Country</th>
								<th>Destination Port</th>
								<th>ETD</th>
								<th>ETA</th>
								<th class="align-center">Status</th>
								<th>CustomerId</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Stock Modal -->
	<div class="modal fade" id="modal-stock-details">
		<!-- /.modal-dialog -->
		<div class="modal-dialog modal-lg" style="min-width: 100%;  margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Details</h4>
				</div>
				<div class="modal-body " id="modal-stock-details-body"
					></div>
			</div>
			<!-- /.modal-content -->
		</div>
	</div>
	<div id="cloneable-items">
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>
</section>