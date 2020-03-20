<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Name Transfer</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Name Transfer</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/documents/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid ">
		<div class="box-body">
			<div class="row">
				<!-- table start -->
				<div style="text-align: center;">
					<label> <input name="radioShowTable" type="radio"
						class="minimal" value="2" checked="checked"> Name Transfer
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="3"> Domestic
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="5">Parts
					</label> <label class="ml-5"> <input name="radioShowTable"
						type="radio" class="minimal" value="6"> Shuppin
					</label>
				</div>

			</div>
			<div class="container-fluid" id="name-transfer-container">

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
								<input type="text" name="purchaseDate"
									id="table-filter-purchased-date" class="form-control"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly" />
							</div>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-name-transfer-filter-length"
								class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-name-transfer-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-8">
						<button type="button" class="btn btn-primary ml-5 pull-right"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-update-docType" id="updateDocType">Update
							Document Details</button>
						<button type="button" class="btn btn-primary ml-5 pull-right"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-update-reauction" id="updateReauctionDetails"
							style="display: none;">Update Reauction Details</button>

					</div>


				</div>
				<div class="table-responsive">
					<table id="table-name-transfer"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Purchased Date</th>
								<th data-index="2" class="align-center">Shuppin No.</th>
								<th data-index="3" class="align-center">Stock No.</th>
								<th data-index="4" class="align-center">Chassis No.</th>
								<th data-index="5" class="align-center">Auction/Supplier</th>
								<th data-index="6" class="align-center">Auction House</th>
								<th data-index="7" class="align-center">First Reg.Date</th>
								<th data-index="8" class="align-center">Received Doc Type</th>
								<th data-index="9" class="align-center">Doc Received Date</th>
								<th data-index="10" class="align-center">Converted Date</th>
								<th data-index="11" class="align-center">Road Tax</th>
								<th data-index="12" class="align-center">Insurance</th>
								<th data-index="13" class="align-cent er">Re-Convert</th>
								<th data-index="14" class="align-center">Purchase Type</th>
								<th data-index="15" class="align-center">Supplier Code</th>
								<th data-index="16" class="align-center">Auction House Id</th>
								<th data-index="17" class="align-center">Number Plate Recvd
									Date</th>
								<th data-index="18" class="align-center">Plate No.</th>
								<th data-index="19" class="align-center">Reauction Date</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!-- Domestic Datatable starts -->
			<!-- <div class="container-fluid" id="domestic-container">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Purchase Type</label> <select id="purchaseTypeDomestic"
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
							<label>Supplier/Auction</label> <select
								id="purchasedSupplierDomestic" class="form-control select2"
								style="width: 100%;" disabled="true">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFieldsDomestic" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouseDomestic" class="form-control"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group-domestic">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="purchaseDate"
									id="table-filter-purchased-date-domestic" class="form-control"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly" />
							</div>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-6 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-domestic-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-6 form-inline pull-right">
						<div class="pull-right">
							<select id="table-domestic-filter-length"
								class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-domestic"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Purchased Date</th>
								<th data-index="2" class="align-center">Shuppin No.</th>
								<th data-index="3" class="align-center">Stock No.</th>
								<th data-index="4" class="align-center">Chassis No.</th>
								<th data-index="5" class="align-center">Auction/Supplier</th>
								<th data-index="6" class="align-center">Auction House</th>
								<th data-index="7" class="align-center">First Reg.Date</th>
								<th data-index="8" class="align-center">Received Doc Type</th>
								<th data-index="9" class="align-center">Doc Received Date</th>
								<th data-index="10" class="align-center">Number Plate Recvd
									Date</th>
								<th data-index="11" class="align-center">Plate No.</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div> -->

		</div>
	</div>
	<div class="modal fade" id="modal-update-docType">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update Document</h4>
				</div>
				<div class="modal-body">
					<form id="docType-update">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6" id="docConvertNameTransfer">
									<div class="form-group">
										<label class="required">Doc Convert Type</label>
										<div class="element-wrapper">
											<select id="documentConvertTo" name="documentConvertTo"
												class="form-control select2-select" style="width: 100%;">
												<option value="">Select Document Type</option>
												<option value="1">EXPORT CERTIFICATE</option>
												<option value="3">DOMESTIC</option>
												<option value="4">MASHO</option>
												<option value="5">PARTS</option>
												<option value="6">SHUPPIN</option>
												<option value="7">NOT RECEIVED</option>
												<option value="8">RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6 hidden" id="docConvertDomestic">
									<div class="form-group">
										<label class="required">Doc Convert Type</label>
										<div class="element-wrapper">
											<select id="documentConvertTo" name="documentConvertTo"
												class="form-control select2-select" style="width: 100%;">
												<option value="">Select Document Type</option>
												<option value="1">EXPORT CERTIFICATE</option>
												<option value="2">NAME TRANSFER</option>
												<option value="4">MASHO</option>
												<option value="5">PARTS</option>
												<option value="6">SHUPPIN</option>
												<option value="7">NOT RECEIVED</option>
												<option value="8">RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6 hidden" id="docConvertParts">
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
												<option value="6">SHUPPIN</option>
												<option value="7">NOT RECEIVED</option>
												<option value="8">RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6 hidden" id="docConvertShuppin">
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
												<option value="7">NOT RECEIVED</option>
												<option value="8">RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<!-- <div class="col-md-6 hidden" id="docConvertReceived">
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
												<option value="7">NOT RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6 hidden" id="docConvertNotReceived">
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
												<option value="8">RECEIVED</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div> -->
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="update-doc" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
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
	<!-- /name-transfer -->
	<!-- 	.modal -->
	<div class="modal fade" id="modal-reconvert">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form id="reconvert-update">
						<div class="container-fluid">
							<div class="row">

								<div class="col-md-4 ">
									<div class="form-group">
										<label class="required">Date</label>
										<div class="element-wrapper">
											<input type="text" id="reconvert-date" name="reconvertDate"
												class="form-control datepicker" placeholder="Reconvert Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="required">Convert To</label>
										<div class="element-wrapper">
											<select id="converTo" name="convertTo" class="form-control"
												style="width: 100%;">
												<option value="">Convert To</option>
												<option value="Name Transfer">Name Transfer</option>
												<option value="Export Certificate">Export
													Certificates</option>
												<option value="Domestic">Domestic</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="reconvert-doc" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Reconvert
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 	/.modal update re-auction -->

	<!-- 	.modal -->
	<div class="modal fade" id="modal-update-reauction">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<form id="reauction-update">
						<div class="container-fluid">
							<div class="row">

								<div class="col-md-6">
									<div class="form-group">
										<label>Supplier / Auction</label> <select
											id="reAuctionSupplier" class="form-control select2"
											style="width: 100%;">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label>Auction House</label> <select
											id="reAuctionAuctionHouse" class="form-control"
											style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
								</div>
								<div class="col-md-4 ">
									<div class="form-group">
										<label class="required">Doc Sent Date</label>
										<div class="element-wrapper">
											<input type="text" id="reauction-date" name="reauctionDate"
												class="form-control datepicker" placeholder="Reauction Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-reauction" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 	/.modal -->
</section>