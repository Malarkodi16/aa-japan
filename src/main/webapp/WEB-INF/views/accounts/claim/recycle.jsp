<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Recycle</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Claim</span></li>
		<li class="active">Recycle</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/claim/claimstatus.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
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
									style="width: 100%;" multiple="multiple">
									<option value=""></option>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedInfoPos">POS No.</label> <select
									id="purchasedInfoPos" class="form-control" style="width: 100%;">
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
									<em class="fa fa-calendar"></em>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-recycle-claim"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="receivedClaimdate-form-group">
							<label>Received Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<em class="fa fa-calendar"></em>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-received-date"
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
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
						</div>
				<div class="col-md-3">
						<div class="has-feedback">
						<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"
								autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
				
				<div class="row form-group">
					<!-- table start -->
					<div style="text-align: center;">
						<label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="0" checked="checked">
							Available
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="1"> Applied
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="2"> Received
						</label>
					</div>
				</div>
				<div class="row form-group">

					<div class="col-md-6 ">
						<label>&nbsp;</label>
						<div class="form-inline">
							<form method="post" enctype="multipart/form-data"
								action="${contextPath}/documents/recycle/claim/receive/uploadExcelFile"
								id="form-received-upload" class="hidden">
								<input type="file" name="file" class="form-control"
									accept=".xlsx" />
								<button type="submit" class="btn btn-primary form-control">
									<em class="fa fa-fw fa-upload"></em>Upload
								</button>
							</form>
						</div>
					</div>
					<!-- 					<div class="col-md-3"> -->
					<!-- 						<label>&nbsp;</label> -->
					<!-- 					</div> -->


					<div class="col-md-6 ">
						<div class="btn-group pull-right">
							<button class="btn btn-primary hidden" type="button"
								id="update_amount">
								<i class="fa fa-save"> Update Received Amount</i>
							</button>
							<button class="btn btn-primary" type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o"> Export Excel</i>
							</button>
						</div>

					</div>
				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-recycle"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1">Purchase Date</th>
								<th data-index="2">Chassis No</th>
								<th data-index="3">Destination Country</th>
								<th data-index="4">Destination Port</th>
								<th data-index="5">Veh Type</th>
								<th data-index="6">Recycle Amount</th>
								<th data-index="7">Charges</th>
								<th data-index="8">Interest</th>
								<th data-index="9">Received Amount</th>
								<th data-index="10">Submit Date</th>
								<th data-index="11">Received Date</th>

							</tr>
						</thead>
						<tbody>
						</tbody>

						<tfoot>
							<tr class=sum>
								<th colspan="6" style="text-align: right">Total</th>
								<th class="dt-right"><span
									class="autonumber pagetotal recycleTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th colspan="2" style="text-align: right"></th>
								<th class="dt-right"><span
									class="autonumber pagetotal receivedTotal" data-a-sign="¥ "
									data-m-dec="0">0</span></th>
								<th colspan="2" style="text-align: right"></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>