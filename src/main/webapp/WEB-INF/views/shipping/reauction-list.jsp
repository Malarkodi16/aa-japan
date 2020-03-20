<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="canAccess(75)">
	<section class="content-header">
		<h1>Re Auction</h1>
		<ol class="breadcrumb">
			<li><span><i class="fa fa-home"></i> Home</span></li>
			<li class="#">ReAuction/Cancelled</li>
			<li class="active">ReAuction</li>
		</ol>
	</section>
</sec:authorize>

<section class="content">
	<sec:authorize access="canAccessAny(75,76)">
		<jsp:include
			page="/WEB-INF/views/shipping/re-auction-cancel-dashboard.jsp" />
	</sec:authorize>
	<sec:authorize access="canAccess(75)">
		<div class="box box-solid">
			<div class="box-header"></div>
			<div class="box-body">
				<div class="container-fluid ">
					<!-- Re-Auction list show/search inputs -->
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
									class="form-control" placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						
					</div>
					<div class="table-responsive">
						<table id="table-re-auction-list"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0">Stock No.</th>
									<th data-index="1">Chassis No.</th>
									<th data-index="2">Re-Auction Date</th>
									<th data-index="3">Recycle Amount</th>
									<th data-index="4">Auction Company</th>
									<th data-index="5">Auction House</th>
									<th data-index="6">Status</th>
									<th data-index="7">Action</th>
									<th data-index="8">Id</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
				<!-- Re Auction Modal Start -->
				<div class="modal fade" id="re-auctionModal">
					<div class="modal-dialog modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h3 class="modal-title">Sold Details</h3>
							</div>
							<div class="modal-body">
								<form action="#">
									<div class="row form-group" id="priceDetails">
										<!-- Customer Wise Filter -->
										<input class="hide" name="id" id="id" type="text">
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Sold Price</label> <input
													type="text" name="soldPrice" id="amount"
													data-validation="number"
													class="form-control required autonumber calcTax"
													data-m-dec="0" data-a-sign="¥ " /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Shuppin Commission</label> <input
													type="text" name="shuppinCommission" id="shuppinCommission"
													data-validation="number"
													class="form-control required autonumber calcTax"
													data-m-dec="0" data-a-sign="¥ " /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Sold Commission</label> <input
													type="text" name="soldCommission" id="soldCommission"
													data-validation="number"
													class="form-control required autonumber calcTax"
													data-m-dec="0" data-a-sign="¥ " /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Recycle</label><input type="text"
													name="recycleAmount" id="recycleAmount"
													data-validation="number"
													class="form-control required autonumber calcTax"
													data-m-dec="0" data-a-sign="¥ " /> <span
													class="help-block"></span>
											</div>
										</div>

										<!-- <div class="col-md-3">
										<div class="form-group">
											<label class="required">Commission</label><input type="text"
												name="commission" id="amount" data-validation="number"
												class="form-control required autonumber" data-m-dec="0"
												data-a-sign="¥ " /> <span class="help-block"></span>
										</div>
										
									</div> -->

									</div>
									<div class="row form-group">
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Tax</label><input type="text"
													name="tax" id="tax" data-validation="number"
													class="form-control required autonumber" data-v-min="0"
													data-a-sign="¥ " readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Shuppin Tax</label><input
													type="text" name="shuppinTax" id="shuppinTax"
													data-validation="number"
													class="form-control required autonumber" data-v-min="0"
													data-a-sign="¥ " readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Sold Tax</label><input type="text"
													name="soldCommTax" id="soldCommTax"
													data-validation="number"
													class="form-control required autonumber" data-v-min="0"
													data-a-sign="¥ " readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Invoice Date</label><input
													type="text" name="invoiceDate" id="invoiceDate"
													class="form-control required datepicker"
													readonly="readonly" /> <span class="help-block"></span>
											</div>
										</div>
									</div>
									<div class="row form-group">
										<div class="col-md-3">
											<div class="form-group">
												<label>All Total</label><input type="text" id="allTotal"
													data-validation="number" class="form-control autonumber"
													data-v-min="0" data-a-sign="¥ " readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" id="save-reAuction-details"
									class="btn btn-primary">Update</button>
								<button type="button" data-dismiss="modal"
									class="btn btn-primary">Close</button>
							</div>
						</div>
					</div>
				</div>
				<!-- Re Auction Modal end -->
			</div>
		</div>

		<div class="modal fade" id="modal-edit-reauction">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Edit ReAuction</h4>
					</div>
					<div class="modal-body">
						<form id="form-reauction-edit">
							<div class="container-fluid">
								<div class="row form-group">
									<input type="hidden" name="id" id="id" class="form-control id" />
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Reauction Date</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-fw fa-calendar"></i>
												</div>

												<input type="text" id="reauctionDate" name="reauctionDate"
													class="form-control datepicker required"
													placeholder="DD-MM-YYYY" readonly="readonly"
													autocomplete="off">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row form-group">
									<div class="col-md-3">
										<div class="form-group">
											<label>Stock No</label> <input type="text" name="stockNo"
												id="stockNo" class="form-control stockNo"
												readonly="readonly" /> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Chassis No</label> <input type="text" name="chassisNo"
												id="chassisNo" class="form-control chassisNo"
												readonly="readonly" /> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Auction Company</label> <select
												id="auctionCompany" name="auctionCompany"
												class="form-control required" style="width: 100%;"
												data-placeholder="Select Auction Company">
												<option></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Auction House</label> <select
												id="auctionHouse" name="auctionHouse"
												class="form-control required" style="width: 100%;"
												data-placeholder="Select Auction House">
												<option></option>
											</select> <span class="help-block"></span>
										</div>
									</div>
								</div>

							</div>
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-edit-reauction">
								<i class="fa fa-fw fa-save"></i>Save
							</button>
							<button class="btn btn-primary" id="btn-close"
								data-dismiss="modal">
								<i class="fa fa-fw fa-close"></i>Close
							</button>
						</div>
					</div>
					</form>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<!-- Stock Modal -->
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


		<div class="modal fade" id="cancel-re-auction">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title">Move to Inventory</h3>
					</div>
					<div class="modal-body">
						<form action="#">
							<div class="row form-group">
								<input class="hide" name="id" id="id" type="text">
								<!-- Customer Wise Filter -->
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Nagare Charge</label> <input
											type="text" name="nagareCharge" id="nagareCharge"
											data-validation="number"
											class="form-control required autonumber calcTax"
											data-m-dec="0" data-a-sign="¥ " /> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Auction Shipping Charge</label><input
											type="text" name="auctionShippingCharge" id="tax"
											data-validation="number"
											class="form-control required autonumber" data-v-min="0" data-m-dec="0"
											data-a-sign="¥ " /> <span class="help-block"></span>
									</div>
								</div>

								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Invoice Date</label><input type="text"
											name="invoiceDate" id="invoiceDate"
											class="form-control required datepicker" readonly="readonly" />
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" id="update-inventory-details"
							class="btn btn-primary">Update</button>
						<button type="button" data-dismiss="modal" class="btn btn-primary">Close</button>
					</div>
				</div>
			</div>
		</div>
	</sec:authorize>
</section>
