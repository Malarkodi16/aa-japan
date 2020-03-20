<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<section class="content-header	">
	<h1>Last Lap Vehicles</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Last lap Vehicles</li>
	</ol>
</section>

<section class="content">
	<div class="box box-solid">
		<div class="box-body">
			<!-- sales invoice show/search inputs -->
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
			<!-- Datatable -->
			<div class="container-fluid ">
				<div class="table-responsive">
					<table id="table-lastLapVehicles"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1">Stock No.</th>
								<th data-index="2">Chassis No.</th>
								<th data-index="3">Model</th>
								<th data-index="4">First Registration Date</th>
								<th data-index="5">Destination Country</th>
								<th data-index="6">Last lap Date</th>
								<th data-index="7">Inspection Status</th>
								<th data-index="8">Shipping Status</th>
								<th data-index="8">Warning Date</th>
								<th data-index="8">Expiry Date</th>
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