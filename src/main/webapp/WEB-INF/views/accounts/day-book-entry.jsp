<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Receipts Booking</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Receipts</span></li>
		<li class="active">Receipts Booking</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<form method="POST" id="daybookForm"
				action="${contextPath}/accounts/daybookcreate"
				enctype="multipart/form-data">
				<div class="box-body" id="cloneTO">
					<div class="container-fluid">
						<!-- <div class="row">
							<div class="col-md-2 form-group">
								<div class="radio">
									<label> <input value="1" type="radio" name="settled">Payment
										Settled
									</label>
								</div>
							</div>
							<div class="col-md-2 form-group">
								<div class="radio">
									<label> <input value="2" type="radio" name="settled"
										checked="checked">Received
									</label>
								</div>
							</div>
						</div> -->
						<div class="row received">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-fw fa-calendar"></i>
										</div>
										<input type="text" name="remitDate" id="entryDate"
											class="form-control required entryDate"
											placeholder="dd-mm-yyyy" readonly />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Bank</label> <select name="bank"
										id="bank" class="form-control required select2-tag"
										style="width: 100%;" data-placeholder="Select Bank">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>

							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Currency</label> <select
										name="currency" id="currency"
										class="form-control currency required select2-tag select2-select readonly"
										style="width: 100%;" data-placeholder="Select Currency">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3 mt-25" id="clearingAccount">
								<div class="form-group">
									<input type="checkbox" value="1" name="clearingAccount">
									<label>Clearing Account</label> <span class="help-block"></span>
								</div>
							</div>
						</div>
						<fieldset class="daybook-entry">
							<legend>Entry Details</legend>
							<div id="daybook-entry">
								<div class="clone-container-location form-group">
									<div class="row">
										<div class="col-md-2">
											<div class="form-group">
												<label class="required">Transaction Type</label>
												<!-- <select
														id="remitType" name="remitType"
														class="form-control required remitType select2-tag" style="width: 100%;"
														data-placeholder="Select Type">
														<option></option>
													</select> -->
												<select name="items[0].remitType" id="remitType"
													class="form-control required remitType select2-tag"
													style="width: 100%;" data-placeholder="Select Type">
													<option value=""></option>
													<c:forEach items="${mRemitType}" var="item">
														<option value="${item.remitSeq}">${item.remitType}</option>
													</c:forEach>
												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2 hidden">
											<div class="form-group">
												<label>Tax Code</label> <select name="items[0].coaNo"
													id="coaNo" class="form-control select2-tag coaNo"
													data-placeholder="Select Tax Code">
													<option value=""></option>

												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label class="required">Remitter</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="items[0].remitter" id="remitter"
														class="form-control required remitter" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label class="required">Total Amount</label> <input
													type="text" name="items[0].amountWithOutBankCharge"
													id="amountWithOutBankCharge"
													class="form-control required amountWithOutBankCharge autonumber"
													data-a-sign="¥ " data-m-dec="0" /> <span
													class="help-block"></span>
											</div>

										</div>
										<div class="col-md-2 ">
											<div class="form-group">
												<label class="required">Gross Amount</label> <input
													type="text" name="items[0].amount" id="amount"
													class="form-control required amount autonumber"
													data-a-sign="¥ " data-m-dec="0" /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label class="required">Bank Charges</label> <input
													type="text" name="items[0].bankCharges"
													class="form-control required bankcharge autonumber"
													readonly="readonly" data-a-sign="¥ " data-m-dec="0"
													data-v-min="0" /> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label>&nbsp;</label>
												<div class="form-control">
													<!-- <input type="checkbox" id="customerBankChargeCheck"
														name="items[0].customerBankCharge"
														class="form-control minimal customerBankCharge" value="1"><label
														class="ml-5">AAJ Accept chrgs.</label> -->
													<input type="checkbox" value="1"
														name="items[0].customerBankCharge"
														class="customerBankCharge" autocomplete="off"><label
														class="ml-5">AAJ Accept chrgs.</label>
												</div>
											</div>
										</div>
									</div>
									<div id="lcRemit" class="row lcRemit" style="display: none;">
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Bill Of Exchange</label> <select
													name="items[0].billOfExchange" id="billOfExchange"
													class="form-control required select2-tag billOfExchange"
													style="width: 100%;" data-placeholder="Bill of Exchange">
												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label>Lc No</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-newspaper-o "></i>
													</div>
													<input type="text" name="items[0].lcNo" id="lcNo"
														class="form-control lcNo" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label>Staff</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user-circle-o "></i>
													</div>
													<input type="text" name="items[0].staff" id="staff"
														class="form-control staff" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label>Customer</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="items[0].customer" id="customer"
														class="form-control customer" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label>Remarks</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-comments"></i>
													</div>
													<input type="text" name="items[0].remarks" id="remarks"
														class="form-control" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<label class="required">Approve</label>
											 <div class="form-group"> 
												<input type="file" id="attachment" name="items[0].attachment"
													class="form-control required" data-directory="slip_upload"
													accept="image/x-png,image/gif,image/jpeg" />
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="row">
										<div class="col-md-12 btn-container">
											<button type="button"
												class="btn btn-success pull-right btn-clone">
												<i class="fa fa-fw fa-plus"></i>Add More
											</button>
											<button class="btn btn-danger pull-left btn-delete">
												<i class="fa fa-fw fa-remove"></i>Delete
											</button>
										</div>
									</div>
								</div>
							</div>
						</fieldset>
						<!-- table start -->
						<div class="settled" style="margin-top: 25px;">
							<div class="row form-group ">
								<div class="col-md-6">
									<div class="has-feedback">
										<input type="text" id="table-filter-search"
											class="form-control" placeholder="Search by keyword">
										<input type="hidden" id="invoiceType" value="${invoiceType}">
										<span class="glyphicon glyphicon-search form-control-feedback"></span>
									</div>
								</div>
								<div class="col-md-2 form-inline pull-right">
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
							<div class="row form-group">
								<div class="col-md-12">
									<div class="table-responsive">
										<table id="table-auction-payment"
											class="table table-bordered table-striped"
											style="width: 100%; overflow: scroll;">
											<thead>
												<tr>
													<th data-index="0"><input type="checkbox"
														id="select-all" /></th>
													<th data-index="1">Date</th>
													<th data-index="2">Due Date</th>
													<th data-index="3">Invoice No</th>
													<th data-index="4">Invoice Type</th>
													<th data-index="5">Bank</th>
													<th data-index="6">Remit To</th>
													<th data-index="7">Total Amount</th>
													<th data-index="8">Action</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer received">
					<div class="pull-right">
						<button type="submit" id="btn-save" class="btn btn-primary">
							<i class="fa fa-save mr-5"></i>Save
						</button>
						<button type="reset" class="btn btn-primary">
							<i class="fa fa-repeat mr-5"></i>Reset
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>