<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header ">
	<h1>Special User</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active"><span>Special User</span></li>
	</ol>
</section>
<!-- stock. -->
<section class="content ">
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<jsp:include page="/WEB-INF/views/sales/stock-filters.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="row form-group">
				<!-- <div class="col-md-5">
						<div class="col-md-3">
						<button type="button" class="btn btn-primary pull-left"
							id="save-stock">Update Stock</button>
					</div>
				</div> -->
				
				<div class="col-md-2 pull-left">
					<div class="form-group">
						<select class="form-control" id="filter-account">
							<option value="0">All</option>
							<option value="1">AAJ</option>
							<option value="2">SOMO</option>
						</select>
					</div>
				</div>
				<div class="col-md-7 pull-right">
					<div class="form-inline">
						<div class="form-group">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="0" checked>&nbsp;&nbsp;New
									Arrival
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="1">&nbsp;&nbsp;Available in
									stock
								</label>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="box-body">
			<!-- table start -->
			<form action="#" id="form-stock">
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
						<div class="col-md-8">
						<div class=" pull-right">
							<button type="button" class="btn btn-primary ml-5 pull-left"
								data-target="#modal-update-min-selling-price"
								data-toggle="modal" data-backdrop="static">
								<i class="fa fa-fw fa-floppy-o"></i> Update Price
							</button>
						</div>
						</div>
					</div>
					<div class="table-responsive">
						<table id="table-stock"
							class="table table-bordered table-striped display"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0" style="text-align: center; width: 20px;"><input
										type="checkbox" id="select-all" /></th>
									<th data-index="1" class="align-center">Stock No</th>
									<th data-index="2" class="align-center" style="width: 100px;">Chassis
										No</th>
									<th data-index="3" class="align-center" style="width: 100px;">Category</th>
									<th data-index="4" class="align-center" style="width: 100px;">Maker/Model<!-- <span class=autonumber data-a-sign="¥ "></span> -->
									</th>
									<th data-index="5" class="align-center">Minimum Selling
										Price</th>
									<th data-index="6" class="align-center">Dest Country</th>
									<th data-index="7" class="align-center">Dest Port</th>
									<th data-index="8" class="align-center">Price Calc</th>
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
	<!-- Price Calculator -->
	<div class="modal fade" id="modal-price-calc">
		<div class="modal-dialog modal-lg">
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
						<input type="hidden" name="stockNo" />
						<div class="row">
							<div class="col-md-3">
								<div class="form-group has-warning">
									<label for="totalYen" class="control-label">Min Selling
										Price in ¥</label> <input type="text" class="form-control autonumber"
										id="totalYen" data-a-sign="¥ " data-m-dec="0"
										disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group has-warning">
									<label for="totalDollar" class="control-label">Min
										Selling Price in $</label> <input type="text"
										class="form-control autonumber" id="totalDollar"
										data-a-sign="$ " data-m-dec="2" disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group has-warning">
									<label for="offerPrice" class="control-label">Web Price</label>
									<input type="text" class="form-control autonumber price-calc"
										id="offerPrice" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="totalDollar">Exchange Rate</label> <input
										type="text" class="form-control autonumber" id="exchangeRate"
										data-m-dec="0" disabled="disabled">
								</div>
							</div>

						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="purchasePrice">Purchase Cost</label> <input
										type="text" class="form-control autonumber price-calc"
										id="purchasePrice" data-a-sign="¥ " data-m-dec="0"
										disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="purchaseCostTaxAmount">Tax</label> <input
										type="text" class="form-control autonumber price-calc"
										id="purchaseCostTaxAmount" data-a-sign="¥ " data-v-min="0"
										disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="commission">Commission</label> <input type="text"
										class="form-control autonumber price-calc" id="commission"
										data-a-sign="¥ " data-m-dec="0" disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="commisionTaxAmount">Tax</label> <input type="text"
										class="form-control autonumber price-calc"
										id="commisionTaxAmount" data-a-sign="¥ " data-v-min="0"
										disabled="disabled">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="recycle">Recycle</label> <input type="text"
										class="form-control autonumber price-calc" id="recycle"
										data-a-sign="¥ " data-m-dec="0" disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="recycle">Road Tax</label> <input type="text"
										class="form-control autonumber price-calc" id="roadTax"
										data-a-sign="¥ " data-m-dec="0" disabled="disabled">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="otherCharges">Others</label> <input type="text"
										class="form-control autonumber price-calc" id="otherCharges"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="transportation">Transportation</label> <input
										type="text" class="form-control autonumber price-calc"
										id="transportation" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="shippingCharge">Shipping</label> <input type="text"
										class="form-control autonumber price-calc" id="shippingCharge"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="inspectionCharge">Inspection</label> <input
										type="text" class="form-control autonumber price-calc"
										id="inspectionCharge" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="radiationCharge">Radiation</label> <input
										type="text" class="form-control autonumber price-calc"
										id="radiationCharge" data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="m3">M3</label> <input type="text"
										class="form-control autonumber price-calc" id="m3"
										data-m-dec="0" disabled="disabled">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="freightPerM3">Freight/m3</label> <input type="text"
										class="form-control autonumber price-calc" id="freightPerM3"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="freightCharge">Freight</label> <input type="text"
										class="form-control autonumber price-calc" id="freightCharge"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="courierCharge">Courier</label> <input type="text"
										class="form-control autonumber price-calc" id="courierCharge"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="profit">Profit</label> <input type="text"
										class="form-control autonumber price-calc" id="profit"
										data-a-sign="¥ " data-m-dec="0">
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="excludingProfit">Excluding Profit</label> <input
										type="text" class="form-control autonumber price-calc"
										id="excludingProfit" data-a-sign="¥ " data-m-dec="0"
										readonly="readonly">
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label></label>
									<div class="form-control">
										<input type="checkbox" id="showStock" name="showStock"
											class="form-control" value=1 checked><label
											class="ml-5">&nbsp; Show Stock</label>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label></label>
									<div class="form-control">
										<input type="checkbox" id="excludeProfit" name="excludeProfit"
											class="form-control checkExcludeProfit" value=1><label
											class="ml-5">&nbsp; Exclude Profit</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn-update" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Update
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<div class="modal fade" id="modal-update-min-selling-price">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Update</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<form id="update-price">
							<input type="hidden" name="stockNo" />
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label for="totalYen" class="required">Min Selling
											Price in ¥</label> <input type="text"
											class="form-control autonumber required" id="minSellingPrice"
											data-a-sign="¥ " data-m-dec="0">
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label for="offerPrice" class="required">Web Price</label> <input
											type="text" class="form-control autonumber required"
											id="webPrice" data-a-sign="¥ " data-m-dec="0">
									</div>
								</div>
								<div class="col-md-4">
									<div class="col-md-12">
										<label></label>
										<div class="form-control">
											<input type="checkbox" id="showStock" name="showStock"
												class="form-control" value=1 checked><label
												class="ml-5">&nbsp; Show Stock</label>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button id="btn-update-price" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Update
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>
