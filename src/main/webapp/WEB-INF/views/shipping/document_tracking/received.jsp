<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Received</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Received</li>
	</ol>
</section>
<section class="content">
	<jsp:include
		page="/WEB-INF/views/shipping/document_tracking/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="form-group">
				<div style="text-align: center;">
					<label> <input name="radioShowTable" type="radio"
						class="minimal" value="0" checked> Received
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="1"> Rikuji Converted
					</label>
				</div>
			</div>
			<div class="container-fluid ">

				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Purchase Type</label> <select id="purchaseType"
								name="supplierType" class="form-control" style="width: 100%;">
								<option value="">All</option>
								<option data-type="auction" value="auction">Auction</option>
								<option data-type="supplier" value="local">Local</option>
								<option data-type="supplier" value="overseas">Overseas</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								class="form-control select2" style="width: 100%;"
								disabled="true">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-control"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-document-received-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy">
							</div>
							<!-- /.input group -->
						</div>
					</div>



				</div>



				<!-- Received List show/search inputs -->

				<div class="row form-group">
				<div class="col-md-1 form-inline pull-left">
							<div class="form-group">
								<select id="table-filter-length" class="form-control">
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
					<div class="form-group" id="buttonShowHide">
						<div class="col-md-8">
						<div class="pull-right">
							<button type="button" class="btn btn-primary ml-5"
								id="takeToRikuji" data-toggle="modal"
								data-target="#modal-rikuji-remark" data-backdrop="static"
								data-keyboard="false">Take To Rikuji</button>
							<button type="button" class="btn btn-primary pull-left"
								id="save-fob-costs">Save Fob</button>
							<button type="button" class="btn btn-primary ml-5 hidden"
								id="updateDocumentFromRikuji">Update Document</button>
						</div>
						</div>
						
					</div>
				</div>

				<!-- datatable -->
				<form action="#" id="receivedForm">
					<div class="table-responsive">
						<table id="table-received-list"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1">Chassis No</th>
									<th data-index="2">Received Doc type</th>
									<th data-index="3" class="align-center">FOB</th>
									<th data-index="4">Doc Received Date</th>
									<th data-index="5">Plate No</th>
									<th data-index="6">Plate No Rec. date</th>
									<th data-index="7">Convert To</th>
									<th data-index="8">Converted Date</th>
									<th data-index="9">Action</th>
									<th data-index="10">Purchased Date</th>

								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</form>

			</div>
		</div>
	</div>
	<!-- Stock details Start-->
	<div class="modal fade" id="modal-stock-details">
		<!-- /.modal-dialog -->
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
	<!-- Stock details end-->
	<!-- modal cancel remark -->
	<div class="modal fade" id="modal-remark">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Please enter remark</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<textarea name="documentRemarks" class="form-control" rows="3"
							placeholder="Enter ..."></textarea>
					</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" id="btn-remark-submit"
							class="btn btn-primary">Submit</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- modal rikuji remark -->
	<div class="modal fade" id="modal-rikuji-remark">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Please enter remark</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<form id="docType-update">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-6 ">
										<div class="form-group">
											<label class="required">Document Date</label>
											<div class="element-wrapper">
												<input type="text" id="rikujiUpdateToOneDate"
													name="rikujiUpdateToOneDate"
													class="form-control datepicker1"
													placeholder="Rikuji Update Date">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label>Rikuji Remarks</label>
											<div class="element-wrapper">
												<textarea placeholder="Rikuji Remarks" name="rikujiRemarks"
													class="form-control">
											</textarea>
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
					<div class="pull-right">
						<button type="button" id="btn-rikuji-remark-submit"
							class="btn btn-primary">Submit</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>