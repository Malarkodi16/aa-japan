<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>General Expenses Invoice</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Completed</span></li>
		<li class="active">General Expenses</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/payment-freeze-icons.jsp" />
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">

					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="auctionCompany"
								class="form-control select2" style="width: 100%;">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Due Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-due-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="paymentDate-form-group">
							<label>Payment Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-payment-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>

				</div>
				<div class="row form-group">
					<div class="col-md-1 form-inline">
						<div class="form-group">
							<select id="table-filter-length" class="form-control">
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
								placeholder="Search by keyword" autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="row form-group">
						<div class="col-md-6">
							<div class="box-tools pull-left">
								<form action="${contextPath }/accounts/add-payment">
									<button type="submit" style="display: none;"
										class="btn btn-primary pull-right" id="addPayment">Add
										Payment</button>
								</form>
								<!-- <button type="button" class="btn btn-primary pull-right" data-target="#myModalCreatereceive" data-toggle="modal" style="margin-right: 8px">Create</button> -->

							</div>
						</div>

					</div>
					<div class="table-responsive">
						<table id="table-auction-payment"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>

									<th data-index="0"><input type="checkbox" id="select-all" /></th>
									<th data-index="1">Date</th>
									<th data-index="2">Invoice No</th>
									<th data-index="3">Supplier Name</th>
									<th data-index="4">Due Date</th>
									<th data-index="5">Total Amount</th>
									<th data-index="6">Action</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!-- /.form:form -->
		<!-- 		<div> -->

		<div id="others-clone-container">
			<div id="others-invoice-details" class="hide">
				<div class="box-body no-padding bg-darkgray clone-element">
					<div class="table-responsive order-item-container">
						<input type="hidden" name="paymentinvoiceNo" value="" />
						<table class="table table-bordered" style="overflow-x: auto;">
							<thead>
								<tr>
									<th class="align-center ">#</th>
									<th data-index="0" class="align-center">Category</th>
									<th data-index="1" class="align-center">Description</th>
									<th data-index="2" class="align-center">Amount(¥)</th>
									<th data-index="3" class="align-center">TaxAmount</th>
									<th data-index="4" class="align-center">TotalAmount</th>
									<th data-index="5" class="align-center">Source Currency</th>
									<th data-index="6" class="align-center">Amount</th>
									<th data-index="7" class="align-center">Exchange rate</th>
								</tr>
							</thead>
							<tbody>
								<tr class="clone-row hide">
									<td class="s-no"><span></span></td>
									<td class="align-center category"></td>
									<td class="align-center description"></td>
									<td class="dt-right amountInYen"><span class="autonumber"
										data-a-sign="¥ " data-m-dec="0"></span></td>
									<td class="dt-right taxAmount"><span class="autonumber"
										data-m-dec="0"></span></td>
									<td class="dt-right totalAmount"><span class="autonumber"
										data-a-sign="¥ " data-m-dec="0"></span></td>
									<td class="align-center currency"></td>
									<td class="dt-right amount"><span class="autonumber"
										data-a-sign="¥ " data-m-dec="0"></span></td>
									<td class="dt-right exchangeRate"><span class="autonumber"
										data-m-dec="0"></span></td>
								</tr>
							<tfoot>
								<tr>
									<td colspan="3"><strong class="pull-right">Total</strong></td>
									<td class="dt-right amount"><span class="autonumber"
										data-a-sign="&yen; " data-m-dec="0"></span></td>
									<td class="dt-right taxTotal"><span class="autonumber"
										data-m-dec="0"></span></td>
									<td class="dt-right taxIncluded"><span class="autonumber"
										data-a-sign="&yen; " data-m-dec="0"></span></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tfoot>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

		<!-- 		</div> -->

		<!-- Model -->
		<div class="modal fade" id="modal-invoice-preview">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Invoice</h4>
					</div>
					<div class="modal-body">
						<embed
							src="${contextPath}/resources/assets/images/no-image-icon.png"
							width="100%" style="height: 500px" class="image_preview">
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
</section>
