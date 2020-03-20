<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Receipts List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Receipts</span></li>
		<li class="active">Receipts List</li>
	</ol>
</section>
<!-- stock. -->

<section class="content">
	<div class="box box-solid">
		<div class="box-header">
			<div class="nav-tabs-custom">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#"
						style="background-color: #2a4d61; color: white;"><strong>Un-Approved
								Receipt List</strong></a></li>
					<li><a href="${contextPath}/daybook/daybook-list/approved"><strong>Approved
								Receipt List</strong></a></li>

				</ul>
			</div>
		</div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Transaction Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-purchased-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Bank</label> <select id="bankFilter" name="bankFilter"
								class="form-control select2-tag" style="width: 100%;"
								data-placeholder="Select Bank">
								<option value=""></option>
							</select>
						</div>
					</div>
					<!-- <div class="col-md-2">
						<div class="form-group">
							<label>Tax Code</label> <select id="coaNo" name="coaNo"
								class="form-control select2-tag" style="width: 100%;"
								data-placeholder="Select Tax Code">
								<option></option>
							</select>
						</div>
					</div> -->
					<div class="col-md-2">
						<div class="form-group">
							<label>Remit Type</label> <select id="remitTypeFilter"
								class="form-control select2 select2-tag" style="width: 100%;"
								data-placeholder="Select Remit Type">
								<option value=""></option>
								<c:forEach items="${mRemitType}" var="item">
									<option value="${item.remitSeq}">${item.remitType}</option>
								</c:forEach>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields">
						<div class="col-md-2">
							<div class="form-group">
								<label for="currency">Currency</label> <select
									id="currencyFilter" class="form-control select2-tag"
									style="width: 100%;" data-placeholder="Select Currency">
									<option></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Account Type</label> <select id="accountTypeFilter"
								name="accountTypeFilter" class="form-control select2-tag"
								style="width: 100%;" data-placeholder="Select Account Type">
								<option value="">Select Account Type</option>
								<option value="1">Clearing Account</option>
								<option value="0">Bank Account</option>
								<option value="">All</option>
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
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group"></div>
				<div class="table-responsive">
					<table id="table-auction-payment"
						class="table table-bordered table-striped"
						style="width: 150%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0">Remit Date</th>
								<th data-index="0">Receipt No</th>
								<th data-index="1">Remit Type</th>
								<th data-index="2">Tax Code</th>
								<th data-index="3">Remitter</th>
								<th data-index="4">Bank</th>
								<th data-index="5">Net Amount</th>
								<th data-index="6">Bank Charges</th>
								<th data-index="7">Gross Amount</th>
								<th data-index="8">BOE</th>
								<th data-index="9">LC No</th>
								<th data-index="10">Customer</th>
								<th data-index="11">Account Type</th>
								<th data-index="12">Remarks</th>
								<th data-index="13">Action</th>
								<th data-index="14">Id</th>
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
	<!-- Model -->
	<div class="modal fade" id="modal-edit-daybook">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Day Book Entry</h4>
				</div>
				<div class="modal-body">
					<form id="form-daybook-edit">
						<div class="container-fluid" id="cloneTO">
							<div class="row form-group">
								<input type="hidden" name="id" id="id"
									class="form-control data required id" />
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Date</label>
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-fw fa-calendar"></i>
											</div>

											<input type="text" name="remitDate" id="entryDate"
												class="form-control data required entryDate" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Bank</label><select id="bank"
											name="bank" class="form-control data select2"
											style="width: 100%;" data-placeholder="Select Bank">
											<option></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Currency</label> <input type="hidden"
											name="hiddenCurrency" id="hiddenCurrency"
											class="form-control hiddenCurrency" /> <select
											name="currency" id="currency"
											class="form-control data required select2-tag select2-select readonly currency"
											style="width: 100%;" data-placeholder="Select Currency">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 mt-25" id="clearingAccount">
									<div class="form-group">
										<input type="checkbox" value="1" name="clearingAccount"
											class="clearingAccount"> <label>Clearing
											Account</label> <span class="help-block"></span>
									</div>
								</div>
							</div>
							<div id="daybook-entry" class="row toCloneDiv">
								<div class="clone-container-location-toclone">
									<fieldset>
										<legend>Entry Details</legend>
										<div class="row">
											<div class="col-md-2">
												<div class="form-group">
													<label class="required">Transaction Type</label> <select
														id="remitType" name="remitType"
														class="form-control data select2-tag" style="width: 100%;"
														data-placeholder="Select Remit Type">
														<option></option>
													</select> <span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-2 hidden">
												<div class="form-group">
													<label>Tax Code</label> <select name="coaNo" id="coaNo"
														class="form-control select2-tag coaNo"
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
														<input type="hidden" name="hiddenRemitter"
															id="hiddenRemitter" class="form-control hiddenRemitter" />
														<input type="text" name="remitter" id="remitter"
															class="form-control data remitter required" />
													</div>
													<span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">
													<label>Amount</label>
													<div class="input-group">
														<input type="hidden" name="hiddenAmountWithoutBankCharge"
															id="hiddenAmountWithoutBankCharge"
															class="form-control hiddenAmountWithoutBankCharge autonumber"
															data-m-dec="0" data-v-min="0" /> <input type="text"
															name="amountWithOutBankCharge"
															id="amountWithOutBankCharge"
															class="form-control data required amountWithOutBankCharge autonumber"
															data-a-sign="¥ " data-m-dec="0" data-v-min="0" />
													</div>
													<span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">
													<label>Bank Charges</label>
													<div class="input-group">
														<input type="hidden" name="hiddenBankCharge"
															id="hiddenBankCharge"
															class="form-control hiddenBankCharge autonumber"
															data-m-dec="0" data-v-min="0" /> <input type="text"
															name="bankCharges"
															class="form-control data required bankcharge autonumber"
															data-a-sign="¥ " data-m-dec="0" data-v-min="0" />
													</div>
													<span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-2 ">
												<div class="form-group">
													<label class="required">Total</label> <input type="hidden"
														name="hiddenAmount" id="hiddenAmount"
														class="form-control hiddenAmount autonumber"
														data-m-dec="0" data-v-min="0" /> <input type="text"
														name="amount" id="amount"
														class="form-control data required amount autonumber"
														readonly="readonly" data-a-sign="¥ " data-m-dec="0"
														data-v-min="0" /> <span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-2">
												<div class="form-group">
													<label>&nbsp;</label>
													<div class="form-control">
														<input type="checkbox" value="1" name="customerBankCharge"
															class="customerBankCharge"><label class="ml-5">AAJ
															Accept chrgs.</label>
													</div>
												</div>
											</div>
										</div>
										<!-- 										style= "display:none;" -->
										<div id="lcRemit" class="row" style="display: none;">
											<div class="col-md-3">
												<div class="form-group">
													<label class="required">Bill Of Exchange</label> <select
														name="billOfExchange" id="billOfExchange" data-value=""
														class="form-control required data select2-tag billOfExchange"
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
														<input type="text" name="lcNo" id="lcNo"
															class="form-control data lcNo" />
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
														<input type="text" name="staff" id="staff"
															class="form-control data staff" />
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
														<input type="hidden" name="customerId" id="customerId"
															class="form-control data customerId" /> <input
															type="text" name="customer" id="customer"
															class="form-control data customer" />
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
														<input type="text" name="remarks" id="remarks"
															class="form-control data" />
													</div>
													<span class="help-block"></span>
												</div>
											</div>
										</div>
									</fieldset>

								</div>
							</div>
						</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-order">
							<i class="fa fa-fw fa-save"></i>Update Day Book
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
	<!-- /.modal -->
</section>
