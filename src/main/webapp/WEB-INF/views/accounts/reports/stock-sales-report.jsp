<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Stock Sales Report</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Reports</span></li>
		<li class="active">Stock Sales</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Invoice Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-invoice-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Maker</label> <select id="maker-filter"
								class="form-control" style="width: 100%;"
								data-placeholder="Select Maker">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Model</label> <select id="model-filter"
								class="form-control" style="width: 100%;"
								data-placeholder="Select Model">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="submit" class="btn btn-primary form-control"
								id="btn-search">Search</button>
						</div>
					</div>
				</div>
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
			</div>
			<!-- table start -->
			<form action="#" id="form-purchased">
				<div class="container-fluid ">
					<div class="table-responsive">
						<table id="table-report"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th colspan="2"></th>
									<th colspan="4" class="align-center bg-aqua">Purchase Info</th>
									<th class="align-center bg-blue">Sales Info</th>
								</tr>
								<tr>
									<th>Stock No</th>
									<th>Invoice Date</th>
									<th class="bg-aqua">Purchase Cost</th>
									<th class="bg-aqua">Commission</th>
									<th class="bg-aqua">Others</th>
									<th class="bg-aqua">Total</th>
									<th class=" bg-blue">Total</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr class=totalFooter>
									<th colspan="2" style="text-align: right">Total</th>
									<th colspan="3"></th>
									<th class =  "dt-right"><span class="autonumber pagetotal purchaseInfoTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th class =  "dt-right" ><span class="autonumber pagetotal salesInfoTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- Model -->
	<div class="modal fade" id="modal-stock-details">
		<div class="modal-dialog modal-lg"
			style="min-width: 100%; margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Details</h4>
				</div>
				<div class="modal-body " id="modal-stock-details-body"></div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
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
