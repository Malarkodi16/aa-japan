
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>LC Invoice Create</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>LC Management</span></li>
		<li class="active">Create Invoice</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header">
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<select id="custId" class="form-control select2"
							style="width: 100%;"
							data-placeholder="Search by Customer ID, Name, Email">
							<option value=""></option>
						</select> <span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-2">
					<button type="button" class="btn btn-primary" type="button"
						id="btn-searchData" style="width: 80px;">Search</button>
				</div>
			</div>
		</div>
		<div class="box-body">
			<div class="container-fluid" id="invoice-list-container">
				<div class="row form-group">
					<div class="col-md-6 form-inline pull-left">
						<div class="has-feedback pull-left">
							<label></label> <input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-6 form-inline pull-right">
						<div class="pull-right">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-invoice-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Invoice No</th>
								<th data-index="2" class="align-center">Date</th>
								<th data-index="3" class="align-center">Customer</th>
								<th data-index="4" class="align-center">Consignee</th>
								<th data-index="5" class="align-center">Notify Party</th>
								<th data-index="6" class="align-center">Payment Type</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 	Modal -->
	<div class="modal fade" id="modal-lc-update">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">LC Details</h4>
				</div>
				<div class="modal-body">
					<form id="update-lc-form">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="lc-no" class="required">LC No</label> <input
										type="text" class="form-control" id="lc-no"
										placeholder="Enter LC No" name="lcNo"><span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<div class="form-group">
										<label for="bank" class="required">Bank</label> <select
											class="form-control" id="bank" name="bankId"
											data-placeholder="Select Bank">
											<option value=""></option>
										</select><span class="help-block"></span>
									</div>
								</div>
							</div>							
							<div class="col-md-4">
								<div class="form-group">
									<label for="validity" class="required">Validity</label> <input
										type="text" class="form-control datepicker" id="validity"
										placeholder="dd-MM-yyyy" name="validity"><span
										class="help-block"></span>
								</div>
							</div>
							</div>
							<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="amount" class="required">Amount</label> <input
										type="text" class="form-control autonumber" id="amount"
										placeholder="Enter Amount" data-a-sign="¥ " data-m-dec="0"
										data-v-min="0" name="amount"><span class="help-block"></span>
								</div>
							</div>
							
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class=" pull-right">
						<button type="button" class="btn btn-primary" id="btn-save-lc">Save
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
	<div class="hide" id="clone-element-container">
		<div id="cutomer-dropdown">
			<div class="align-center">
				<select name="itemCustId" class="form-control select2"
					style="width: 100%; text-align: left"
					data-placeholder="Search by Customer ID, Name, Email">
					<option value=""></option>
				</select>
			</div>
		</div>
		<div id="invoice-item-details">
			<div class="box-body no-padding bg-darkgray invoice-item-container">
				<input type="hidden" name="proformaInvoiceId" value="" /> <input
					type="hidden" name="cAddress" value="" /> <input type="hidden"
					name="npAddress" value="" /> <input type="hidden"
					name="customerName" value="" /><input type="hidden"
					name="consignee" value="" /> <input type="hidden"
					name="notifyParty" value="" />
				<div class="table-responsive order-item-container">
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Stock
									No</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Chassis
									No</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">HS
									Code</th>
								<th style="width: 100px" class="align-center bg-ghostwhite">Maker/Model</th>
								<th class="align-center bg-ghostwhite" style="width: 50px">Year</th>
								<th class="align-center bg-ghostwhite" style="width: 200px;">Customer</th>
								<th class="align-center bg-ghostwhite" style="width: 60px">Amount</th>
								<th class="align-center bg-ghostwhite" style="width: 60px">Proforma
									Amount</th>
								<th class="align-center bg-ghostwhite" style="width: 100px">Status</th>
								<th class="align-center bg-ghostwhite" style="width: 60px">LC
									No</th>
							</tr>
						</thead>
						<tbody>
							<tr class="hide clone-row">
								<td class="s-no"><input type="checkbox" name="stockNo[]"
									value=""><input type="hidden" name="maker" value="" /><input
									type="hidden" name="model" value="" /><input type="hidden"
									name="hsCode" value="" /><input type="hidden" name="chassisNo"
									value="" /></td>
								<td class="align-center stockNo"></td>
								<td class="align-center chassisNo"></td>
								<td class="hsCode"></td>
								<td class="align-center makerModel"></td>
								<td class="align-center year"></td>
								<td class="align-center customer"></td>
								<td class="align-center amount"><input type="text"
									class="form-control autonumber" name="amount" data-a-sign="¥ "
									data-m-dec="0" data-v-min="0" style="width: 100px;"
									placeholder="Amount" value=""> <span class="help-block autonumber"
									data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center pAmount"><input type="text"
									class="form-control autonumber" name="pAmount" data-a-sign="¥ " data-m-dec="0"><span
									 data-a-sign="¥ " data-m-dec="0"></span></td>
								<td class="align-center status"><span class="label"></span></td>
								<td class="lcNo"></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7" style="text-align: center;"><strong>Total</strong></td>
								<td style="text-align: center;" data-a-sign="¥ " data-m-dec="0"
									class="total autonumber">0</td>
								<td style="text-align: center;" data-a-sign="¥ " data-m-dec="0"
									class="ptotal autonumber">0</td>
								<td></td>
								<td></td>
							</tr>
						</tfoot>
					</table>
				</div>
				<div class="row box-footer">
					<div class="col-md-1 pull-right">
						<button type="button" class="btn btn-primary" style="width: 80px;"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#modal-lc-update">Update LC</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>
