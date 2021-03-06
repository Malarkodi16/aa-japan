<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Domestic</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Domestic</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/documents/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid ">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-3">
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
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text"
								id="table-name-transfer-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
						<div class="col-md-1 form-inline">
							<div class="pull-left">
								<select id="table-name-transfer-filter-length"
									class="form-control">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
								</select>
							</div>
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
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-stock-details">
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
		<!-- /.modal-dialog -->
	</div>
	<div id="cloneable-items">
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
	</div>
</section> --%>