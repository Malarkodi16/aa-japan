<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Freight-Shipping</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Booking</span></li>
		<li><span><em class=""></em>Freight & Shipping</span></li>
		<li class="active"><a>Container</a></li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li><a href="${contextPath}/accounts/invoice/booking/shipping/roro"><strong>RORO</strong></a></li>
					<li class="active"><a
						href="${contextPath}/accounts/invoice/booking/shipping/container"><strong>CONTAINER</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">

			<div class="container-fluid shipping-container"
				id="shipping-container">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Forwarder</label> <select
								class="form-control shipping-container-data"
								id="container-filter-frwdr" data-placeholder="Select Forwarder"
								style="width: 100%" name="forwarder">
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Vessel/Voyage</label> <select
								class="form-control shipping-container-data"
								id="container-filter-vessel" name="voyageNos"
								data-placeholder="Select Vessel/Voyage" style="width: 100%"
								multiple="multiple">

							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Orgin Country</label> <select
								class="form-control shipping-container-data"
								id="container-filter-orginCountry"
								data-placeholder="Orgin Country" style="width: 100%"
								multiple="multiple">

							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Orgin Port</label> <select
								class="form-control shipping-container-data"
								id="container-filter-orginPort" data-placeholder="Orgin Port"
								style="width: 100%" multiple="multiple">

							</select><span class="help-block"></span>
						</div>
					</div>

				</div>
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group">
							<label>Destination Country</label> <select
								class="form-control shipping-container-data"
								id="container-filter-destinationCountry"
								data-placeholder="Destination Country" style="width: 100%"
								multiple="multiple">

							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label>Destination Port</label> <select
								class="form-control shipping-container-data"
								id="container-filter-destinationPort"
								data-placeholder="Destination Port" style="width: 100%"
								multiple="multiple">
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>BL No</label><select
								class="form-control shipping-container-data"
								id="container-filter-blNo" data-placeholder="Enter BL No"
								style="width: 100%" multiple="multiple">
								<option></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="button" class="btn btn-primary form-control"
								id="container-filter-search">Search</button>
						</div>
					</div>
				</div>

				<div class="table-responsive">
					<!-- TForwarder invoice show/search inputs -->
					<div class="row form-group">
						<div class="form-group">
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
									<input type="text" id="table-filter-search"
										class="form-control" placeholder="Search by keyword"
										autocomplete="off"> <span
										class="glyphicon glyphicon-search form-control-feedback"></span>
								</div>
							</div>
							<button type="button" class="btn col-md-2 btn-primary pull-right"
								id="addInvoice" data-backdrop="static" data-keyboard="false"
								data-toggle="modal" data-target="#modal-invoice">
								<i class="fa fa-file-text-o mr-5"></i>Invoice
							</button>
							<button type="button"
								class="btn col-md-2 btn-primary mr-5 ml-5 pull-right"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-create-invoice">
								<i class="fa fa-save mr-5"></i>Create Invoice
							</button>
							<!-- 							<button type="button" -->
							<!-- 								class="btn col-md-1 mr-5 ml-5 btn-primary pull-right" -->
							<!-- 								id="createInvoice"> -->
							<!-- 								<i class="fa fa-save mr-5"></i>Save -->
							<!-- 							</button> -->

						</div>
					</div>
					<table id="tableShippingContainer"
						class="table table-bordered table-striped">
						<thead>
							<tr>

								<th>#</th>
								<th>Chassis No</th>
								<th>Maker</th>
								<th>Model</th>
								<th>Dest. Country</th>
								<th>Sales Person</th>
								<th>M3</th>
								<th>Container</th>
								<th>Amount</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="6"></th>
								<th></th>
								<th><span class="autonumber" data-a-sign="¥ "
									data-m-dec="0"></span></th>

							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-invoice">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<!-- <button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button> -->
					<div class="row">
						<div class="col-md-2 pull-left">
							<h3 class="modal-title col-md-2 ">Invoice</h3>
						</div>
						<div class="col-md-4 pull-right">
							<div class="col-md-6">
								<div class="form-group">
									<label>USD</label> <input
										class="form-control input-sm autonumber"
										name="currencyRateUSD" type="text" data-a-sign="$ "
										data-m-dec="2">
								</div>

							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label>ZAR</label> <input
										class="form-control input-sm autonumber"
										name="currencyRateZAR" type="text" data-a-sign="R "
										data-m-dec="0">
								</div>
							</div>

						</div>
					</div>

				</div>
				<div class="modal-body">
					<div class="container-fluid table-responsive no-padding">
						<table class="table table-hover">
							<thead>
								<tr>
									<th>Qty</th>
									<th>Description</th>
									<th>USD</th>
									<th>Zar</th>
									<th>Unit Price</th>
									<th>Amount</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<tr class="hidden">
									<td width="50px"><span class="qty"></span></td>
									<td><span class="description"></span></td>
									<td><span class="usd autonumber" data-a-sign="$ "
										data-m-dec="0"></span></td>
									<td><span class="zar autonumber" data-a-sign="R "
										data-m-dec="0"></span></td>
									<td><span class="unitprice autonumber" data-a-sign="¥ "
										data-m-dec="0"></span></td>
									<td><span class="amount autonumber" data-a-sign="¥ "
										data-m-dec="0"></span></td>
									<td><button type="button" name="btn-delete"
											class="btn btn-warning ml-5 btn-xs" title="Remove"
											style="width: 70px;">
											<i class="fa fa-times"></i>Remove
										</button></td>

								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="2"></td>
									<td><span class="totalUsd autonumber" data-a-sign="$ "
										data-m-dec="0" id="invoiceTotalUsd"></span></td>
									<td colspan="2"></td>
									<td><span class="total autonumber" data-a-sign="¥ "
										data-m-dec="0" id="invoiceTotal"></span></td>
								</tr>
								<tr>
									<td><input class="form-control autonumber" type="text"
										data-m-dec="0" data-a-sep="" name="quantity"
										style="width: 50px" /></td>
									<td><select class="form-control select2-select readonly"
										name="description" style="width: 200px"
										data-placeholder="Description">
											<!-- 											<option value=""></option> -->
											<option data-currency="usd" value="10% Tax">10% Tax</option>
											<option data-currency="usd" value="Amendment Charges">Amendment
												Charges</option>
											<option data-currency="usd" value="Oceasn Freight">Oceasn
												Freight</option>
											<option data-currency="usd" value="Vanning Charges">Vanning
												Charges</option>
											<option data-currency="usd" value="BL Fees">BL Fees</option>
											<option data-currency="zar" value="On Carriage">On
												Carriage</option>
											<option data-currency="usd" value="Container Fee">Container
												Fee</option>
											<option data-currency="usd" value="Demurriage Fee">Demurriage
												Fee</option>
											<option data-currency="usd" value="Document Fees">Document
												Fees</option>
											<option data-currency="usd" value="Handling Charges">Handling
												Charges</option>
											<option data-currency="usd" value="Security Charges">Security
												Charges</option>
											<option data-currency="usd" value="Shipping Charges 2 Units">Shipping
												Charges 2 Units</option>
											<option data-currency="usd" value="Shipping Charges 3 Units">Shipping
												Charges 3 Units</option>
											<option data-currency="usd" value="Shipping Charges 4 Units">Shipping
												Charges 4 Units</option>
											<option data-currency="usd" value="Shipping Charges 5 Units">Shipping
												Charges 5 Units</option>
											<option data-currency="usd" value="Shipping Charges 6 Units">Shipping
												Charges 6 Units</option>
											<option data-currency="usd" value="Surrender Fee">Surrender
												Fee</option>
											<option data-currency="usd" value="Terminal Handling Charges">Terminal
												Handling Charges</option>
											<option data-currency="usd" value="X- Ray Inspection Charges">X-
												Ray Inspection Charges</option>
									</select></td>
									<td><input class="form-control usd autonumber" type="text"
										data-a-sign="$ " data-m-dec="0" style="width: 100px"
										readonly="readonly" name="usdAmount" /></td>
									<td><input class="form-control zar autonumber" type="text"
										data-m-dec="0" style="width: 100px" data-a-sign="R "
										readonly="readonly" name="zarAmount" /></td>
									<td><input class="form-control unitprice autonumber"
										type="text" data-a-sign="¥ " data-m-dec="0"
										style="width: 100px" name="unitPrice" /></td>
									<td><input class="form-control amount autonumber"
										type="text" data-a-sign="¥ " data-m-dec="0"
										style="width: 100px" readonly="readonly" name="amount" /></td>
									<td><button type="button" name="btn-add"
											class="btn btn-success ml-5 btn-xs" title="Add"
											style="width: 70px;">
											<i class="fa fa-plus"></i>Add
										</button></td>
								</tr>
							</tfoot>
						</table>

					</div>
				</div>
				<div class="modal-footer">
					<button id="btn-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modal-create-invoice">
		<div class="modal-dialog modal-lg">
			<div class="modal-content" id="containerModal">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Create Invoice</h4>
				</div>
				<form id="freight-shipping-form">
					<div class="modal-body">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label>Invoice Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text"
											class="form-control datepicker freight-modal-data"
											name="date" placeholder="dd-mm-yyyy" readonly>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Forwarder</label> <select
										class="form-control freight-data select2 " name="code"
										id="forwarder" data-placeholder="Select Forwarder"
										style="width: 100%">
										<option></option>
									</select><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Due Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" placeholder="dd-mm-yyyy" name="dueDate"
											id="dueDate"
											class="form-control datepicker freight-modal-data"
											readonly="readonly" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							id="btnCreateInvoice">Update</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

</section>

