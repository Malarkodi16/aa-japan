<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Export Certificates</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Export Certificates</li>
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
			<form action="#" id="form-export-fob">
				<div class="container-fluid">
					<div class="form-group" id="export-certificate-status">
						<div style="text-align: center;">
							<label> <input name="exportCertificateStatusFilter"
								type="radio" class="minimal" value="0" checked> IN ALAIN
							</label> <label class="ml-5"> <input
								name="exportCertificateStatusFilter" type="radio"
								class="minimal" value="1"> SHIPPING COMPANY
							</label><label class="ml-5"> <input
								name="exportCertificateStatusFilter" type="radio"
								class="minimal" value="2"> INSPECTION COMPANY
							</label>
						</div>
					</div>
					<div class="form-group hidden"
						id="shipping-company-document-sent-status">
						<div style="text-align: center;">
							<label> <input name="docSentStatusFilter" type="radio"
								class="minimal" value="0" checked> ORIGINAL SENT
							</label> <label class="ml-5"> <input name="docSentStatusFilter"
								type="radio" class="minimal" value="1"> EMAIL SENT
							</label>
						</div>
					</div>
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

						<div class="col-md-2">
							<div class="form-group" id="date-form-group-conv-date">
								<label>Converted Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-converted-date" placeholder="dd-mm-yyyy"
										readonly="readonly">
								</div>
								<!-- /.input group -->
							</div>
						</div>
					</div>
					<div class="row form-group">

						<div class="col-md-1 form-inline pull-left">
							<div class="pull-left">
								<select id="table-export-certificates-filter-length"
									class="form-control ">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text"
									id="table-export-certificates-filter-search"
									class="form-control" placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>

						</div>


						<div class="col-md-8">
							<div class="pull-right">
							
									<button type="button" class="btn btn-primary"
										data-toggle="modal" data-backdrop="static"
										data-keyboard="false" data-target="#modal-send-document"
										id="btn-send-document">Send Document</button>
								
								<button type="button" class="btn btn-primary ml-5"
									id="takeToRikuji" data-toggle="modal"
									data-target="#modal-rikuji-remark" data-backdrop="static"
									data-keyboard="false">Take To Rikuji</button>
								<button type="button" class="btn btn-primary ml-5 hidden"
									id="exportCrReceived">Export CR Received</button>
								<button type="button" class="btn btn-primary ml-5 hidden"
									id="nonExportedCrReceived">Non Exported CR Received</button>
								<button type="button" class="btn btn-primary ml-5 pull-right"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#modal-update-docType"
									id="updateDocType">Update Document Details</button>
							</div>
						</div>

					</div>

					<div class="table-responsive">
						<table id="table-export-certificates"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1" class="align-center">Chassis No.</th>
									<th data-index="2" class="align-center">Convert To</th>
									<th data-index="3" class="align-center">Converted Date</th>
									<th data-index="4" class="align-center">FOB</th>
									<th data-index="5" class="align-center">FOR</th>
									<th data-index="6" class="align-center">Final Port</th>
									<th data-index="7" class="align-center">Send to FWT</th>
									<th data-index="8" class="align-center">Document Status</th>
									<th data-index="9" class="align-center">Doc Received Date</th>
									<th data-index="10" class="align-center">Plate No.</th>
									<th data-index="11" class="align-center">Plate Received
										Date</th>
									<th data-index="12" class="align-center">Road Tax</th>
									<th data-index="13" class="align-center">Insurance</th>
									<th data-index="14" class="align-center">Re-Convert</th>
									<th data-index="15" class="align-center">Export Pdf</th>
									<th data-index="16" class="align-center"></th>
									<th data-index="17" class="align-center"></th>
									<th data-index="18" class="align-center"></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</form>
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
												<option value="0">NOT RECEIVED</option>
												<option value="1">RECEIVED</option>
												<option value="2">NAME TRANSFER</option>
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
	<div class="modal fade" id="modal-export-certificate-details">
		<div class="modal-dialog modal-lg"
			style="min-width: 100%; margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<%-- ${contextPath}/documents/tracking/exportCerficate/create --%>
				<div class="modal-body table-responsive"
					id="modal-export-certificate-details-body"
					style="background-color: lightblue;"></div>
				<div class="modal-footer">
					<div class='btn-toolbar pull-right'>
						<button type="button" id="modelSave" class="btn btn-primary"
							data-value="table-received">Save changes</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<div id="cloneable-items">
		<div id="export-certificate-details-html" class="hide">
			<div class="export-certificate-details">
				<jsp:include
					page="/WEB-INF/views/documents/export-certificate-modal-pdf.jsp" />
			</div>
		</div>
	</div>
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
	<!-- 	/.modal -->
	<!-- 	.modal -->
	<div class="modal fade" id="modal-send-document">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Document Send Details</h4>
				</div>
				<div class="modal-body">
					<form id="form-document-send-details">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Send To</label>
										<div class="element-wrapper">
											<select name="exportCertificateStatus" class="form-control"
												style="width: 100%;">
												<option value="1">Shipping Company</option>
												<option value="2">Inspection Company</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group" id="shipping-company">
										<label class="required">Shipping Company</label>
										<div class="element-wrapper">
											<select name="shippingCompanyId" class="form-control"
												style="width: 100%;" data-placeholder="Shipping Company">
											</select>
										</div>
										<span class="help-block"></span>
									</div>
									<div class="form-group hidden" id="inspection-company">
										<label class="required">Inspection Company</label>
										<div class="element-wrapper">
											<select name="inspectionCompanyId" class="form-control"
												style="width: 100%;" data-placeholder="Inspection Company">
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Document Send Date</label>
										<div class="element-wrapper">
											<input type="text" name="docSendDate"
												class="form-control datepicker" placeholder="dd-MM-yyyy">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3" id="documet-sent-wrapper">
									<div class="form-group">
										<label>&nbsp;</label>
										<div class="form-control">
											<label class="mr-5"> <input type="checkbox"
												name="docOriginalSent" class="minimal"> Original
											</label> <label class="ml-5"> <input type="checkbox"
												name="docEmailSent" class="minimal"> Email
											</label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" name="save"
							style="width: 100px">Save</button>
						<button class="btn btn-primary" name="close" data-dismiss="modal"
							style="width: 100px">Close</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 	/.modal -->
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