<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header ">
	<h1>Customer Transaction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Customer Management</span></li>
		<li class="active">Customer Transaction</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<input type="hidden" id="customerId" value="${custId}" />
			<div class="container-fluid">
				<div class="row">
					<div class="form-group">
						<div class="col-md-3" id="customer-block">
							<div class="form-group" id="search-customer">
								<label>Customer</label> <select class="form-control customer"
									id="custId" style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>Sales Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="date-range" placeholder="dd-mm-yyyy - dd-mm-yyyy">
								</div>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label>&nbsp;</label>
								<button type="submit" class="btn form-control btn-primary"
									id="search">
									<i class="fa fa-fw fa-search"></i>Search
								</button>
							</div>
						</div>
						<!-- <div class="col-md-2">
							<div class="form-group">
								<label>&nbsp;</label>
								<button class="btn form-control btn-primary" id="exportExcel">
									<i class="fa fa-fw fa-file-excel-o"></i>Export
								</button>
							</div>
						</div> -->
						<div class="col-md-2">
							<div class="form-group">
								<label>&nbsp;</label>
								<button class="btn form-control btn-primary" id="exportExcelNew">
									<i class="fa fa-fw fa-file-excel-o"></i>Export
								</button>
							</div>
						</div>

					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="table-responsive">
					<table class="table table-bordered table-striped"
						id="customer-month-transaction"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Month</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="customer-transaction-details-view" class="hidden">
					<div class="container-fluid detail-view">
						<div class="box-body no-padding bg-darkgray">
							<table class="table" style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th colspan="11" style="background-color: #c220205c;">Purchase
											Details</th>
										<th colspan="1" style="background-color: #c220205c;">Receipt
											Details</th>
										<th colspan="5" style="background-color: skyblue;">Shipping
											Details</th>
										<th colspan="4" style="background-color: lightgreen;">Action</th>
									</tr>
									<tr>
										<th style="width: 10px">#</th>
										<th>Purchase Date</th>
										<th>Stock No.</th>
										<th style="background-color: skyblue;">Lot No.</th>
										<th style="background-color: skyblue;">Auction</th>
										<th>Chassis No.</th>
										<th style="background-color: skyblue;">Purchase Price</th>
										<th style="background-color: skyblue;">Commission</th>
										<th style="background-color: skyblue;">Additional</th>
										<th>Reserve Price</th>
										<!-- <th>USD $Price</th>
										<th>Rate</th> -->
										<th>Lc Number</th>
										<th>Received Amount</th>
										<th>Vessel Name</th>
										<!-- <th>Container No.</th> -->
										<th>Est.Departure(ETD)</th>
										<th>Est.Arrival(ETA)</th>
										<th>DHL No.</th>
										<th>Status</th>
										<!-- 										<th>Date</th> -->
										<!-- 										<th>Reference</th> -->
										<th>DL Invoice</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<tr class="hide broughtForwardRow">
										<td colspan="7"></td>
										<td colspan="2">Brought Forward</td>
										<td class="broughtForward dt-right"><span
											class="broughtForward autonumber price" data-m-dec="0"></span></td>
									</tr>
									<tr class="clone-row hide" data-json="">
										<td class="s-no"><span class="s-no" data-value=""></span></td>
										<td class="date"></td>
										<td class="stockNo"></td>
										<td class="lotNo"></td>
										<td class="auction"></td>
										<td class="chassisNo"></td>
										<td class="dt-right purchasePrice"><span
											class="purchasePrice autonumber" data-m-dec="0"></span></td>
										<td class="dt-right commission"><span
											class="commission autonumber" data-m-dec="0"></span></td>
										<td class="dt-right additional"><span
											class="additional autonumber" data-m-dec="0"></span></td>
										<td class="dt-right price"><span class="price autonumber"
											data-m-dec="0"></span></td>
										<!-- <td class="usdPrice"></td>
										<td class="rate"></td> -->
										<td class="lcNo"></td>
										<td class="receivedAmount dt-right"><span
											class="receivedAmount autonumber" data-m-dec="0"></span></td>
										<td class="vessal"></td>
										<!-- <td class="containerNo"></td> -->
										<td class="etd"></td>
										<td class="eta"></td>
										<td class="dhlNo"></td>
										<td class="shippingStatus align-center"><span
											class="label"></span></td>
										<!-- 										<td class="recieptDate"></td> -->
										<!-- 										<td class="recieptReference"></td> -->
										<!-- <td class="recieptAmount"><span class="autonumber"></span></td> -->
										<td class="recieptDLInvoice"></td>
										<td class="action"><button
												class="btn btn-primary btn-xs create-shipping"
												data-target="#modal-create-shipping" data-toggle="modal"
												data-backdrop="static">
												<i class="fa fa-plus"></i>&nbsp;&nbsp; Arrange Shipping
											</button></td>
									</tr>
								</tbody>
								<tfoot>
									<tr class="clone-total">
										<th colspan="8"></th>
										<th>Total Sales</th>
										<th class="dt-right priceTotal"><span class="autonumber"
											data-m-dec="0"></span></th>
										<th colspan="8"></th>
									</tr>
									<tr class="balanceReceipt">
										<th colspan="8"></th>
										<th>Paid</th>
										<th class="dt-right balanceReceipt"><span
											class="autonumber" data-m-dec="0"></span></th>
										<th class="showTrans"><button type="button"
												class="btn btn-primary btn-xs"
												data-target="#modal-view-transactions" data-toggle="modal"
												data-backdrop="static">View Transactions</button></th>
										<th colspan="8"></th>
									</tr>
									<tr class="clone-total-balance">
										<th colspan="8"></th>
										<th>Balance</th>
										<th class="dt-right totalBalanceReceipt"><span
											class="autonumber" data-m-dec="0"></span></th>
										<th colspan="8"></th>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modal-create-shipping">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create Shipping Instruction</h4>
				</div>
				<div class="modal-body">
					<jsp:include
						page="/WEB-INF/views/sales/createshippinginstruction.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-save-shipping">
							<i class="fa fa-fw fa-save"></i>Save
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

	<div class="modal fade" id="modal-view-transactions">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Transaction Details</h4>
				</div>
				<div class="modal-body" id="transactionDetailsBody">
					<div class="container-fluid">
						<div class="table-responsive">
							<table id="table-detail-transaction"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Date</th>
										<th data-index="1">Amount</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

</section>