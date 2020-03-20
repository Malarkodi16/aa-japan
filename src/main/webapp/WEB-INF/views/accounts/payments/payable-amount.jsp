<section class="content-header">
	<h1>Accounts Payable</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li class="active">Amount Payable</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<!-- Radio Buttton start -->
			<div style="text-align: center;" class="form-group">
				<label> <input name="radioShowTable" type="radio"
					class="minimal" value="0" checked>&nbsp;&nbsp;AUCTION
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="1">&nbsp;&nbsp;TRANSPORT
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="3">&nbsp;&nbsp;GENERAL
					EXPENSES
				</label> <label class="ml-5"> <input name="radioShowTable"
					type="radio" class="minimal" value="4">&nbsp;&nbsp;STORAGE
					AND PHOTOS
				</label>
			</div>


			<!-- table start -->
			<div class="container-fluid">
				<!-- Filters -->
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
				<div class="table-responsive">
					<table id="table-amount-payable"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" class="align-center">Remit To</th>
								<th data-index="1" class="align-center">Grand Amount</th>
								<th data-index="2" class="align-center">Invoice No</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="1" style="text-align: right"></th>
							<th>Grand Amount Total</th>
						</tr>
						<tr class=sum>
							<th colspan="1" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal grandTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
						</tr>
					</tfoot>
						<!-- <tfoot>
							<tr class=totalFooter>
								<th style="text-align: right">Total</th>
								<th class="dt-right"><span class="autonumber pagetotal grandTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
							</tr>
						</tfoot> -->
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="clone-container">
		<div id="invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="3" class="align-center">Purchase Cost</th>
								<th data-index="3" class="align-center">Purchase Cost Tax</th>
								<th data-index="4" class="align-center">Commission</th>
								<th data-index="4" class="align-center">Commission Tax</th>
								<th data-index="5" class="align-center">Road Tax</th>
								<th data-index="6" class="align-center">Recycle</th>
								<th data-index="7" class="align-center">Others</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="dt-right purchaseCost"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right purchaseCostTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right commision"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right commisionTax"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right roadTax"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right recycle"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="dt-right otherCharges"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- Transport Invoice Table Format Details -->
	<div id="transport-clone-container">
		<div id="payment-transport-approve-details" class="hide">
			<div class="box-body no-padding clone-element">
				<div class="table-responsive transportorder-item-container">
					<input type="hidden" name="transportinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center">#</th>
								<th data-index="0" class="align-center">Stock No</th>
								<th data-index="1" class="align-center">Amount</th>
								<th data-index="2" class="align-center">Tax</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center stockNo"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
								<td class="dt-right tax"><span class="autonumber"
									data-a-sign="&yen; " data-m-dec="0"></span></td>
							</tr>
						</tbody>

					</table>
				</div>
			</div>
		</div>
	</div>
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
								<th data-index="1" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center type"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="storage-clone-container">
		<div id="storage-invoice-details" class="hide">
			<div class="box-body no-padding bg-darkgray clone-element">
				<div class="table-responsive order-item-container">
					<input type="hidden" name="paymentinvoiceNo" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center ">#</th>
								<th data-index="0" class="align-center">Chassis No</th>
								<th data-index="1" class="align-center">Category</th>
								<th data-index="2" class="align-center">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span></td>
								<td class="align-center chassisNo"></td>
								<td class="align-center category"></td>
								<td class="dt-right amount"><span class="autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>