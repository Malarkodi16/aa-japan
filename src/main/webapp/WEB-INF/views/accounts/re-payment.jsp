<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Re Pay Loan</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Loan Management</span></li>
		<li class="active">Re Payment</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="box box-solid">
		<!-- /.box-header -->
		<form method="POST" id="rePaymentForm"
			action="${contextPath}/accounts/create/re-payment">
			<div class="box-header"></div>
			<div class="box-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-6">
							<input name="savingAccount" id="savingAccount" type="hidden" />
							<input name="loanDtlId" id="loanDtlId" type="hidden" />
							<div class="form-group row">
								<label for="preClose" class="col-md-4 control-label"></label>
								<div class="col-md-8">
									<div class="form-control">
										<input type="checkbox" id="preClose" name="preClose"
											class="form-control" value=1><label class="ml-5">Pre
											Close loan</label>
									</div>
								</div>
							</div>
							<div class="form-group row">
								<label for="loanSearch" class="col-md-4 control-label">Loan
									Search</label>
								<div class="col-md-8">
									<select name="loanId" id="loanId"
										class="form-control loanSearch"
										data-placeholder="Search by Loan Id. or Reference No."><option
											value=""></option></select>
								</div>
							</div>
							<div class="form-group row">
								<label for="bank" class="col-md-4 control-label">Bank</label>
								<div class="col-md-8">
									<select name="bank" id="bank" class="form-control"
										style="width: 100%;" data-placeholder="Select Bank">
										<option></option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label for="amount" class="col-md-4 control-label">Amount</label>
								<div class="col-md-8">
									<input type="text" id="installmentAmount" name="installmentAmount"
										data-validation="number" class="form-control autonumber"
										data-a-sign="¥ " data-m-dec="0" readonly="readonly">
								</div>
							</div>
							<div class="form-group row">
								<label for="loanType" class="col-md-4 control-label">Loan
									Type</label>
								<div class="col-md-8">
									<select name="loanType" id="loanType"
										class="form-control select2" style="width: 100%;"
										data-placeholder="Select Loan Type">
										<option></option>
										<option value="1">Syndicate Loan</option>
										<option value="2">Toza Loan</option>
										<option value="3">Swap Loan/Corporate bond</option>
										<option value="4">Normal Loan</option>
									</select>
								</div>
							</div>
							<div class="form-group row">
								<label for="paymentDate" class="col-md-4 control-label">Payment
									Date</label>
								<div class="col-md-8">
									<input type="text" class="form-control datepicker"
										id="paymentDate" name="paymentDate" placeholder="DD-MM-YYYY">
								</div>
							</div>
							<div id="savings" class="hidden">
								<div class="form-group row">
									<label for="savingsBankAccount" class="col-md-4 control-label">Savings
										Bank Account</label>
									<div class="col-md-8">
										<input type="text" class="form-control"
											id="savingsBankAccount" name="savingsBankAccount"
											readonly="readonly">
									</div>
								</div>
								<div class="form-group row">
									<label for="savingsAccountAmount"
										class="col-md-4 control-label">Savings Account Amount</label>
									<div class="col-md-8">
										<input type="text" class="form-control autonumber"
											id="savingsAccountAmount" name="savingsAccountAmount"
											data-a-sign="¥ " data-m-dec="0" readonly="readonly">
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<div class="pull-right">
					<button type="submit" class="btn btn-primary">Pay</button>
					<button type="reset" class="btn btn-primary"
						onclick="location.reload();">Reset</button>
				</div>

			</div>
		</form>
	</div>
</section>
