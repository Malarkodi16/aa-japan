<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<sec:authorize access="canAccess(96)">
	<section class="content-header">
		<h1>Not Received</h1>
		<ol class="breadcrumb">
			<li><span><i class="fa fa-home"></i> Home</span></li>
			<li class="active">Not Received</li>
		</ol>
	</section>
</sec:authorize>
<section class="content">
	<sec:authorize access="canAccessAny(96,97,98,99)">
		<jsp:include
			page="/WEB-INF/views/shipping/document_tracking/dashboard.jsp" />
	</sec:authorize>
	<sec:authorize access="canAccess(96)">
		<div class="alert alert-success" id="alert-block"
			style="display: ${message==null||message==''?'none':'block'}">
			<strong>${message}</strong>
		</div>
		<div class="box box-solid">
			<div class="box-header"></div>
			<div class="box-body">
				<div class="container-fluid" id="not-received-details">
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
										id="table-filter-purchased-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-md-1 form-inline pull-left">
							<div class="pull-left">
								<select id="table-filter-length" class="form-control input-sm">
									<option value="10">10</option>
									<option value="25">25</option>
									<option value="100">100</option>
									<option value="1000" selected="selected">1000</option>
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

						<div class="col-md-8">
							<button type="button" class="btn btn-primary ml-5 pull-right"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-update-docType" id="updateDocType">Update
								Document Details</button>

						</div>
					</div>
					<form action="#" id="notReceivedForm">
						<div class="table-responsive">
							<table id="table-notReceived"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0" style="width: 10px"><input
											type="checkbox" id="select-all" /></th>
										<th data-index="1" class="align-center">Purchased Date</th>
										<th data-index="4" class="align-center">Chassis No.</th>
										<th data-index="5" class="align-center">Auction/Supplier</th>
										<th data-index="6" class="align-center">Auction House</th>
										<th data-index="2" class="align-center">Shuppin No.</th>
										<th data-index="3" class="align-center">Stock No.</th>
										<th data-index="7" class="align-center">First Reg Date</th>

										<th data-index="10" class="align-center">Destination
											Country</th>
										<th data-index="11" class="align-center">Destination Port</th>
										<th data-index="12" class="align-center">Purchase Type</th>
										<th data-index="13" class="align-center">Supplier Code</th>
										<th data-index="14" class="align-center">Auction HouseId</th>
										<th data-index="15" class="align-center">Mileage</th>
										<th data-index="8" class="align-center">Doc. Rec. Date</th>
										<th data-index="8" class="align-center">Plate No.</th>
										<th data-index="9" class="align-center">Plate No. Rec.
											Dt.</th>
										<th data-index="16" class="align-center">Remarks</th>
										<th data-index="17" class="align-center">Action</th>
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
		<!-- 	.modal Update Document Detail-->
		<div class="modal fade" id="modal-update-docType">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<div class="col-md-6">
							<h4>
								<span id="count"></span> Stock has been Selected
							</h4>
						</div>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<form id="docType-update">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="required">Received Doc Type</label>
											<div class="element-wrapper">
												<select id="docType" name="docType" class="form-control"
													style="width: 100%;">
													<option value="">Select Document Type</option>
													<option data-type="masho" value="0">MASHO</option>
													<option data-type="shaken" value="1">SHAKEN</option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-6 ">
										<div class="form-group">
											<label class="required">Document Date</label>
											<div class="element-wrapper">
												<input type="text" id="doc-received-date"
													name="receivedDate" class="form-control datepicker1"
													placeholder="Document Date">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6" id="docConvertWithMasho">
										<div class="form-group">
											<label class="required">Doc Convert Type</label>
											<div class="element-wrapper">
												<select id="documentConvertTo" name="documentConvertTo"
													class="form-control select2-select" style="width: 100%;">
													<option value="">Select Document Type</option>
													<option value="1">EXPORT CERTIFICATE</option>
													<option value="2">NAME TRANSFER</option>
													<option value="3">DOMESTIC</option>
													<option value="4">MASHO</option>
													<option value="5">PARTS</option>
													<option value="6">SHUPPIN</option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-6" id="docConvertWithoutMasho">
										<div class="form-group">
											<label class="required">Doc Convert Type</label>
											<div class="element-wrapper">
												<select id="documentConvertTo" name="documentConvertTo"
													class="form-control select2-select" style="width: 100%;">
													<option value="">Select Document Type</option>
													<option value="1">EXPORT CERTIFICATE</option>
													<option value="2">NAME TRANSFER</option>
													<option value="3">DOMESTIC</option>
													<option value="5">PARTS</option>
													<option value="6">SHUPPIN</option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group hidden">
											<label>Document Remarks</label>
											<div class="element-wrapper">
												<textarea id="documentRemarks" placeholder="documentRemarks"
													name="documentRemarks" class="form-control">
											</textarea>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button id="update-doc" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="stock-edit">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<div class="row form-inline">
							<div class="col-md-6">
								<h4>Stock Detail</h4>
							</div>
							<div class="col-md-3 pull-right">
								<button type="button" class="close pull-right"
									data-dismiss="modal" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
						</div>
					</div>
					<div class="modal-body ">
						<form id="stock-edit-detail">
							<div class="container-fluid">
								<div class="row form-group">
									<input type="hidden" name="stockNo" id="stock-stockNo" />
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">First Reg</label> <input type="text"
												name="sFirstRegDate" class="form-control year-month-picker"
												placeholder="YYYY/MM" />
										</div>
										<span class="help-block"></span>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Plate No</label> <input type="text"
												name="oldNumberPlate" class="form-control required" /> <span
												class="help-block"></span>
										</div>

									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Plate Received Date</label> <input
												type="text" name="plateNoReceivedDate"
												class="form-control datepicker1 required" /> <span
												class="help-block"></span>
										</div>

									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Mileage</label> <input type="text"
												name="mileage" class="form-control" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Chassis No</label> <input type="text"
												name="chassisNo" id="chassisNo" class="form-control" />
										</div>
										<span class="help-block"></span>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Remark</label>
											<textarea cols="4" name="documentRemarks"
												class="form-control"></textarea>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button id="update-stock-detail" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
	</sec:authorize>
	<!-- 	/.modal Update Document Detail -->
</section>