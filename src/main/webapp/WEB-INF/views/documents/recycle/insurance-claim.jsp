<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Insurance Claim</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Insurance Claim</li>
	</ol>
</section>
<section class="content">
	<jsp:include
		page="/WEB-INF/views/documents/recycle/recycle-dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row">
					<!-- table start -->
					<div style="text-align: center;">
						<label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="1" checked="checked">
							Applied
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="2"> Received
						</label>
					</div>
				</div>

				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-insurance-claim-filter-length"
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
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-3">
						<button class="btn btn-primary" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-insurance-upload">
							<em class="fa fa-fw fa-upload"></em>Upload
						</button>
					</div>
				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-insurance"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Insurance Apply Date</th>
								<th data-index="1">Chassis No</th>
								<th data-index="2">Insurance Company</th>
								<th data-index="3">Insurance No</th>
								<th data-index="4">Owner Address</th>
								<th data-index="5">Owner Name</th>
								<th data-index="6">From Year</th>
								<th data-index="7">From Month</th>
								<th data-index="8">From Date</th>
								<th data-index="9">To Year</th>
								<th data-index="10">To Month</th>
								<th data-index="11">ToDate</th>
								<th data-index="12">Period</th>
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
	<div class="modal fade" id="modal-insurance-upload"
		style="z-index: 1000000">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Upload Insurance</h3>
				</div>
				<div class="modal-body">
					<form id="form-file-upload">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-6 ">
									<div class="form-group ">
										<label for="invoice_img">Insurance</label>
										<div class="file-element-wrapper">
											<input type="file" id="excelUploaded" name="excelUploaded"
												accept=".xlsx" />
										</div>

										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label>Insurance Apply Date</label>
										<div class="element-wrapper">
											<input type="text" id="insuranceApplyDate"
												name="insuranceApplyDate"
												class="form-control required datepicker"
												placeholder="Insurance Apply Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="save-excel" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Upload
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>