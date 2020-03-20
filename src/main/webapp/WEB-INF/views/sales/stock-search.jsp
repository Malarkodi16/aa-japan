<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<script>
	var userId = "<sec:authentication property="principal.login.userId" />"
</script>
<section class="content-header ">
	<h1>Stock Search</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active"><span>Stock Search</span></li>
	</ol>
</section>
<!-- stock. -->
<section class="content ">
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<%-- <jsp:include page="/WEB-INF/views/sales/dashboard.jsp" /> --%>
	<jsp:include page="/WEB-INF/views/sales/stock-filters.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="row form-group">
				<div class="col-md-2 pull-left">
					<div class="form-group">
						<select class="form-control" id="filter-account">
							<option value="0">All</option>
							<option value="1">AAJ</option>
							<option value="2">SOMO</option>
						</select>
					</div>
				</div>
				<div class="col-md-10">
					<div class="btn-group pull-right">
						<button type="button" class="btn btn-default" id="btn-lock">Lock</button>
						<button type="button" class="btn btn-default" id="btn-unlock">Unlock</button>
						<button type="button" class="btn btn-default" data-toggle="modal"
							data-backdrop="static" data-keyboard="false"
							data-target="#modal-reserve" id="btn-reserve">Reserve</button>
						<button type="button" class="btn btn-default hide"
							id="btn-unreserve">Unreserve</button>
						<button type="button" class="btn btn-default"
							id="btn-export-excel">Export Excel</button>
						<button type="button" class="btn btn-default"
							id="btn-create-proforma" data-backdrop="static"
							data-keyboard="false" data-toggle="modal"
							data-target="#modal-create-proforma">Invoice</button>
					</div>
				</div>
				<div class="col-md-9 pull-right">
					<div class="form-inline">
						<div class="form-group">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="0" checked>&nbsp;&nbsp;Available
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="1">&nbsp;&nbsp;Reserved
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="2">&nbsp;&nbsp;Sold
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="3">&nbsp;&nbsp;Reserved &
									Sold
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="4">&nbsp;&nbsp; Invoice Not
									Confirmed
								</label>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="box-body">
			<!-- table start -->
			<form action="#" id="form-purchased">
				<div class="container-fluid ">
					<div class="row form-group">
						<div class="col-md-1 form-inline pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
					<!-- <div class="dt-buttons">
						<button class="dt-button buttons-collection buttons-colvis"
							tabindex="0" aria-controls="example" type="button"
							aria-haspopup="true" aria-expanded="false">
							<span>Column visibility</span>
						</button>
					</div> -->
					<div class="table-responsive">
						<table id="table-stock"
							class="table table-bordered table-striped display responsive nowrap"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<!-- <th style="text-align: center; width: 20px;"><input
										type="checkbox" id="select-all" /></th> -->
									<th>#</th>
									<th>Purchase Date</th>
									<th>Stock No.</th>
									<th>Chassis No.</th>
									<th>Model Type</th>
									<th>Maker</th>
									<th>Model</th>
									<th>Grade</th>
									<th>Selling Price</th>
									<th>Year</th>
									<th>Buying Price</th>

									<th>Shipment Type</th>
									<th>Options</th>
									<th>Customer</th>
									<th>Action</th>
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
	<!-- /.form:form -->
	<!-- modal -->
	<div class="modal fade" id="modal-reserve">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<form id="reserveForm">
					<div class="modal-header">
						<div class="row no-padding">
							<div class="col-md-5">
								<h4 class="modal-title">Reserve Stock</h4>
							</div>
							<div class="pull-left col-md-4">
								<div class="form-group">
									<select id="custId" name="custId" class="form-control select2"
										style="width: 100%;"
										data-placeholder="Search by Customer ID, Name, Email">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3 pull-right">
								<select class="form-control select2-select readonly"
									data-placeholder="Select Currency" id="currency"
									name="currency">
									<option value=""></option>
								</select><span class="help-block"></span>
							</div>
						</div>
					</div>
					<div class="modal-body">
						<table class="table table-striped" id="table-reserve-item">
							<thead>
								<tr>
									<th>Stock No.</th>
									<th>Maker/Model</th>
									<th>Chassis No</th>
									<th>Minimum Selling Price</th>
									<th width="150px">Reserve Price</th>
								</tr>
							</thead>
							<tbody class="item-container">
							</tbody>
						</table>
					</div>
					<div class="box-footer">
						<div class="row">

							<div class="col-md-12">
								<div class="pull-right">
									<button type="button" class="btn btn-primary" id="btn-reserve">Reserve</button>
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- Model Create Proforma Invoice -->
	<div class="modal fade" id="modal-create-proforma">
		<div class="modal-dialog modal-lg" style="width: 90%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Generate Porforma Invoice</h4>
				</div>
				<div class="modal-body">
					<jsp:include page="/WEB-INF/views/sales/createproformainvoice.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary "
							id="btn-generate-proforma-invoice">
							<i class="fa fa-fw fa-save"></i>Create Order
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<!-- Stock Modal -->
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
	<!-- Price Calculator -->
	<div class="modal fade" id="modal-price-calc">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Price Calculator</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="totalYen">Our Cost in ¥</label> <input type="text"
										class="form-control autonumber" id="totalYen" data-a-sign="¥ "
										data-m-dec="0" disabled="disabled">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="totalDollar">Our Cost in $</label> <input
										type="text" class="form-control autonumber" id="totalDollar"
										data-a-sign="$ " data-m-dec="0" disabled="disabled">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="purchasePrice">Purchase Cost</label> <input
										type="text" class="form-control autonumber price-calc"
										id="purchasePrice" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="purchasePriceTax">Tax 10%</label> <input
										type="text" class="form-control autonumber price-calc"
										id="purchasePriceTax" data-a-sign="%" data-v-max="100"
										data-p-sign="s" data-v-min="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="recycle">Recycle</label> <input type="text"
										class="form-control autonumber price-calc" id="recycle"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="m3">M3</label> <input type="text"
										class="form-control autonumber price-calc" id="m3"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="transport">Transport</label> <input type="text"
										class="form-control autonumber price-calc" id="transport"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="shippingCharges">Shipping Charge</label> <input
										type="text" class="form-control autonumber price-calc"
										id="shippingCharges" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="courier">Courier</label> <input type="text"
										class="form-control autonumber price-calc" id="courier"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="inspection">Inspection</label> <input type="text"
										class="form-control autonumber price-calc" id="inspection"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="marineInsurance">Marine Insurance</label> <input
										type="text" class="form-control autonumber price-calc"
										id="marineInsurance" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="otherCharges">Other Price</label> <input
										type="text" class="form-control autonumber price-calc"
										id="otherCharges" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>
