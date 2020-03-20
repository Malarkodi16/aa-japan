<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="canAccess(65)">
	<section class="content-header">
		<h1>Purchased Vehicles</h1>
		<ol class="breadcrumb">
			<li><span><em class="fa fa-home"></em> Home</span></li>
			<li><span>Dashboard</span></li>
			<li class="active">Purchased Vehicles</li>
		</ol>
	</section>
</sec:authorize>
<!-- stock. -->

<section class="content">
	<sec:authorize access="canAccessAny(65,66,67,68,69,70)">
		<jsp:include page="/WEB-INF/views/shipping/dashboard-status.jsp" />
	</sec:authorize>
	<sec:authorize access="canAccess(65)">
		<div class="alert alert-success" id="alert-block"
			style="display: none"></div>
		<div class="box box-solid">
			<div class="box-header"></div>
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
									disabled="disabled">
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
										id="purchasedInfoPos" class="form-control"
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
										<em class="fa fa-calendar"></em>
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
						<div class="col-md-1 form-inline">
							<div class="form-group">
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
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="pull-right">
							<button type="button" class="btn btn-primary"
								id="save-purchased-costs">Save</button>
							<button type="button" class="btn btn-primary ml-5"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-arrange-transport" id="arrange-transport">Arrange
								Transport</button>
						</div>
					</div>
				</div>
				<!-- table start -->
				<form action="#" id="form-purchased">
					<!-- <div class="container-fluid ">
					<div class="row">
						<div class="col-md-6">
							
						</div>
						
					</div> -->
					<div class="table-responsive">
						<table id="table-purchased"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1">Purchase</br>Date
									</th>
									<th data-index="2">Chassis No.</th>
									<th data-index="3">Model</th>
									<th data-index="4">Shuppin</br>No.
									</th>
									<th data-index="5">Pos No.</th>
									<th data-index="6">Auction/</br>Supplier
									</th>
									<th data-index="7">Auction</br>House
									</th>
									<th data-index="8">Type</th>
									<th data-index="9">Purchase Cost</th>
									<th data-index="10">Commission</th>
									<th data-index="11">Road Tax</th>
									<th data-index="12">Recycle</th>
									<th data-index="13">Others</th>
									<th data-index="14">Total</th>
									<th data-index="15">Purchase Type</th>
									<th data-index="16">Supplier</th>
									<th data-index="17">Auction House</th>
									<th data-index="18">Transportation</br>Status
									</th>
									<th data-index="19">Action</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="9" style="text-align: right"></th>
									<th>Purchase Cost Total</th>
									<th>Commission Total</th>
									<th>Road Tax Total</th>
									<th>Recycle Amt</br>Total
									</th>
									<th>Others Total</th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=sum>
									<th colspan="9" style="text-align: right"></th>
									<th class="dt-right"><span
										class="autonumber pagetotal purchaseTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal commisionTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal roadTaxTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal recycleTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal othersTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr class=taxtotal>
									<th colspan="9" style="text-align: right"></th>
									<th class="dt-right"><span
										class="autonumber pagetotal purchaseTaxTotal" data-a-sign="¥ "
										data-m-dec="0">0</span></th>
									<th class="dt-right"><span
										class="autonumber pagetotal commisionTaxTotal"
										data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
								<tr id="grandTotal">
									<th class="dt-right" colspan="12">Total:</th>
									<th class="dt-right" colspan="2"><span
										class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
							</tfoot>
						</table>
					</div>
			</div>
			</form>
		</div>
		</div>
		<!-- /.form:form -->
		<!-- 	/.modal -->

		<!-- 	/.modal -->
		<div class="modal fade" id="modal-arrange-transport">
			<div class="modal-dialog" style="width: 80%">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">
							Arrange Transport<span class="pull-right"><span>Credit
									Balance : </span><span class="autonumber" id="creditBalance"
								data-a-sign="¥ " data-m-dec="0"></span>&nbsp;&nbsp;&nbsp;</span>
						</h4>
					</div>
					<div class="modal-body">
						<jsp:include page="/WEB-INF/views/shipping/arrange-transport.jsp" />
					</div>
					<div class="modal-footer">
						<div class="pull-right">
							<button class="btn btn-primary " id="btn-create-transport-order">
								<em class="fa fa-fw fa-save"></em>Create Order
							</button>
							<button class="btn btn-primary" id="btn-close"
								data-dismiss="modal">
								<em class="fa fa-fw fa-close"></em>Close
							</button>
						</div>
					</div>
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
	</sec:authorize>
</section>
