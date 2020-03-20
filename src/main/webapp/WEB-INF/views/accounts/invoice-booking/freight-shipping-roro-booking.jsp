<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Freight & Shipping</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li><span><em class=""></em>Booking</span></li>
		<li><span><em class=""></em>Freight & Shipping</span></li>
		<li class="active">RORO</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/invoice-booking/invoice-booking-navigation.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li class="active"><a
						href="${contextPath}/accounts/invoice/booking/shipping/roro"><strong>RORO</strong></a></li>
					<li><a href="${contextPath}/accounts/invoice/booking/shipping/container"><strong>CONTAINER</strong></a></li>
				</ul>
			</div>
		</div>
		<div class="box-body">

			<div class="container-fluid shipping-container"
				id="shipping-container">
				<input name="forwarderId" class="hidden">
				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label class="required">Forwarder</label> <select
								class="form-control shipping-container-data"
								id="container-filter-frwdr" data-placeholder="Select Forwarder"
								style="width: 100%" name="forwarder">
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label class="required">Vessel/Voyage</label> <select
								class="form-control shipping-container-data"
								id="container-filter-vessel" name="voyageNos"
								data-placeholder="Select Vessel/Voyage" style="width: 100%"
								multiple="multiple">
								<option></option>
							</select><span class="help-block"></span>
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
					<table id="table-shipping-container"
						class="table table-bordered table-striped" style="width: 100%;">
						<thead>
							<tr>
								<th>#</th>
								<th>Ship and shipping company</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="clone-elements" class="hide clone-elements">
		<div id="shipping-container-detail-view"
			class="shipping-container-detail-view">

			<div class="box-body">
				<div class="row">
					<div class="col-md-2">
						<label class="required">Origin Port</label> <select
							class="form-control" name="orginPortFilter"
							data-placeholder="Origin Port" style="width: 100%">
							<option></option>
						</select><span class="help-block"></span>
					</div>
					<div class="col-md-2">
						<label class="required">Destination Port</label> <select
							class="form-control" name="destPortFilter"
							data-placeholder="Destination Port" style="width: 100%">
							<option></option>
						</select><span class="help-block"></span>
					</div>
					<div class="col-md-2 pull-right mt-25">
						<button type="button" class="btn btn-primary pull-right"
							data-action="update" data-backdrop="static" data-keyboard="false"
							data-toggle="modal" data-target="#create-container-invoice">Create
							Invoice</button>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<table
							class="table frwdr-charge-table table-bordered table-striped">
							<thead>
								<tr>
									<th class="align-center">Freight</th>
									<th class="align-center">Shipping</th>
									<th class="align-center">Inspection</th>
									<th class="align-center">Radiation</th>
									<th class="align-center">Per M3 USD</th>
									<th class="align-center">Exchange Rate</th>
									<th class="align-center">&nbsp;</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="frwdr-charge freight align-center "><input
										type="text" class="form-control autonumeric"
										data-a-sign="&yen; " style="width: 80px" name="freightCharges" data-m-dec="0"/></td>
									<td class="frwdr-charge shipping align-center "><input
										type="text" class="form-control autonumeric"
										data-a-sign="&yen; " style="width: 80px" data-m-dec="0"
										name="shippingCharges" /></td>
									<td class="frwdr-charge inspection align-center"><input
										type="text" class="form-control autonumeric" data-m-dec="0"
										data-a-sign="&yen; " style="width: 80px"
										name="inspectionCharges" /></td>
									<td class="frwdr-charge radiation align-center "><input
										type="text" class="form-control autonumeric" data-m-dec="0"
										data-a-sign="&yen; " style="width: 80px"
										name="radiationCharges" /></td>
									<td class="frwdr-charge perM3Usd align-center "><input
										type="text" class="form-control autonumeric" data-m-dec="2"
										style="width: 80px" name="perM3Usd" /></td>
									<td class="frwdr-charge exchangeRate align-center "><input
										type="text" class="form-control autonumeric" data-m-dec="2"
										style="width: 80px" name="exchangeRate" /></td>
									<td class="frwdr-charge radiation align-center"><button
											type="button" class="btn btn-primary" id="btn-update-amount">Update</button></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div id="table-shipping-details-container" class="table-responsive">
					<table id="table-shipping-details"
						class="table table-bordered table-striped" style="width: 100%;table-layout:fixed">
						<thead>
							<tr>
								<th style="width: 1%"><input type="checkbox"
									id="select-all" /></th>
								<th style="width: 10%">Stock No</th>
								<th style="width: 10%">Chassis No</th>
								<th style="width: 12%">Cust. Name</th>
								<th style="width: 10%">M3</th>
								<th style="width: 10%">Length</th>
								<th style="width: 10%">Width</th>
								<th style="width: 10%">Height</th>
								<th style="width: 10%" class="m3usd">Per M3 USD</th>
								<th style="width: 10%" class="frtRte">Freight (¥)</th>
								<th style="width: 10%" class="frtRteDollar">Freight ($)</th>
								<th style="width: 10%" class="shpngChrge">Shipping</th>
								<th style="width: 10%" class="inspctnChrge">Inspection</th>
								<th style="width: 10%" class="rdtnChrge">Radiation</th>
								<th style="width: 10%" class="otherCharges">Other Charges</th>
								<th  class="blNo">BL No</th>
								<th>Orgin Port</th>
								<th>Destination Port</th>

							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="9" style="text-align: right">Total</th>
								<th><span class="autonumber pagetotal freightTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th><span class="autonumber pagetotal freightTotalUsd"
									data-a-sign="$ " data-m-dec="0">0</span></th>
								<th><span class="autonumber pagetotal shippingTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th><span class="autonumber pagetotal inspectionTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th><span class="autonumber pagetotal radiationTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th><span class="autonumber pagetotal otherChargesTotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th></th>
								<th></th>
								<th></th>

							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
		<div class="box box-solid clon-vessel-container">
			<div class="box-header with-border btn-expand">
				<h3 class="box-title" id="vessel-name"></h3>
				<div class="box-tools pull-right">
					<button type="button" class="btn btn-box-tool">
						<i class="fa fa-plus"></i>
					</button>
				</div>
				<!-- /.box-tools -->
			</div>
			<!-- /.box-header -->
			<div class="box-body hide">
				<div class="row freightPort">
					<div class="col-md-2">
						<div class="form-group">
							<label class="required">Origin Port</label> <select
								class="form-control orginPort" name="orginPort" id="orginPort"
								data-placeholder="Origin Port" style="width: 100%" multiple>
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label class="required">Destination Port</label> <select
								class="form-control destPort" name="destPort" id="destPort"
								data-placeholder="Destination Port" style="width: 100%" multiple>
								<option></option>
							</select><span class="help-block"></span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="freight-container">
						<div class="col-md-1">
							<div class="form-group">
								<label>Freight</label> <input type="text" name="freightRate"
									id="freightRate" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label>Shipping</label> <input type="text" name="shipping"
									id="shipping" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label>Inspection</label> <input type="text" name="inspection"
									id="inspection" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label>Radiation</label> <input type="text" name="radiation"
									id="radiation" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label> </label>
								<button type="button" class="btn btn-primary mt-5"
									id="chrgeUpdt">Update</button>
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label>Exchange</label> <input type="text" name="exchangeRate"
									id="exchangeRate" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<label> </label>
								<button type="button" class="btn btn-primary mt-5"
									id="btn-exchange-rate">Refresh</button>
							</div>
						</div>
					</div>
					<div class="col-md-2 pull-right">
						<div class="form-group">

							<button type="button" class="btn btn-primary mt-25 pull-right"
								data-action="update" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#freight-Update">Freight Update</button>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-roro-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;" data-id="0">
						<thead>
							<tr>
								<th style="width: 10px"><input type="checkbox"
									id="select-all" /></th>
								<th>Stock No</th>
								<th>Chassis No</th>
								<th>Consignee</th>
								<!-- <th style="width: 100px">ETD</th>
								<th style="width: 100px">ETA</th> -->
								<th>M3</th>
								<th>Length</th>
								<th>Width</th>
								<th>Height</th>
								<th class="m3usd">Per M3 USD</th>
								<th class="usd">Freight USD</th>
								<th class="jpy">Freight JPY</th>
								<!-- <th>Vessel</th>
								<th>VoyageNo</th> -->
								<th>Orgin Port</th>
								<th>Destination Port</th>
								<th class="frtRte">Freight</th>
								<th class="shpngChrge">Shipping</th>
								<th class="inspctnChrge">Inspection</th>
								<th class="rdtnChrge">Radiation</th>
								<th class="otherCharges">Other Charges</th>
								<th class="blNo">BL No</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<th colspan="12" style="text-align: right">Total</th>
								<th></th>
								<th class="freightTotal"><span class="autonumber pagetotal"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="shippingTotal"><span
									class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="inspectionTotal"><span
									class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="radiationTotal"><span
									class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="otherChargesTotal"><span
									class="autonumber pagetotal" data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
			<!-- /.box-body -->
		</div>
	</div>
	<div class="modal fade" id="freight-Update">
		<div class="modal-dialog">
			<div class="modal-content" id="frieghtModal">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Freight Data</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label>Invoice No</label> <input type="text" name="invoiceNo"
									id="" class="form-control freight-modal-data" />
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>Invoice Date</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<input type="text"
										class="form-control datepicker freight-modal-data" name="date"
										placeholder="dd-mm-yyyy">
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="required">Supplier</label> <select
									class="form-control freight-data select2 " name="code"
									id="forwarder" data-placeholder="Select Forwarder"
									style="width: 100%">
									<option></option>
								</select><span class="help-block"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="save-freight">Update</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<div class="modal fade" id="create-container-invoice">
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
										id="container-forwarder" data-placeholder="Select Forwarder"
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
						<button type="button" class="btn btn-primary" id="save-container">Update</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</form>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>