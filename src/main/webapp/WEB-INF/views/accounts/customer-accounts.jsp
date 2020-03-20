<section class="content-header">
	<h1>Customer Accounts</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Customer Accounts</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form>
			<div class="box-header with-border">
				<div class="container-fluid ">
					<div class="row form-group" id="searchCondition">
						<div class="col-md-3">
							<div class="form-group">
								<label>Customer</label> <select
									class="form-control select2 customer" id="custId" name="custId"
									style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group" id="date-form-group">
								<label>Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text" class="form-control pull-right"
										id="table-filter-customer-date"
										placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly>
								</div>
								<!-- /.input group -->
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label>Staff</label><select id="select_sales_staff" name="staff"
									class="form-control select2 staff"
									data-placeholder="Select staff" style="width: 100%;">
									<option value="">Select staff</option>
								</select>
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
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword" autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">

				<div class="table-responsive">
					<table id="table-customerAccounts-transactions"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Stock No</th>
								<th data-index="1">Invoice No</th>
								<th data-index="2">Date</th>
								<th data-index="3">Chassis No</th>
								<th data-index="4">LC Amount</th>
								<th data-index="5">Invoice Amount</th>
								<th data-index="6">Amount Received</th>
								<th data-index="7">Balance</th>
								<th data-index="8">LC Number</th>
								<th data-index="9">Customer</th>
								<th data-index="10">Sales Person</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="5" style="text-align: right"></th>
							<th>Invoice Amount(JPY) Total</th>
							<th>Amount Received(JPY)Total</th>
							<th>Balance Amount(JPY)Total</th>
							<th></th>
							<th></th>
						</tr>
						<tr class=sum>
							<th colspan="5" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal invoiceAmountTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal amountReceivedTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal balanceAmountTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th></th>
							<th></th>
						</tr>
					</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
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
</section>