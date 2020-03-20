<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Loan</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Loan Management</span></li>
		<li class="active">Create Loan</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<!-- /.box-header -->
		<%-- <form id="loancreateForm"
	method="POST" id="loancreateForm"
			action="${contextPath}/accounts/createLoan"
			> --%>
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid loancreation">

				<div class="createloa">
					<form id="loancreateForm">
					<input type="hidden"  id="loanId" name="loanId"
							value="${loanId}">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group row">
									<label for="date" class="col-md-4 control-label required">Date</label>
									<div class="col-md-8">
										<input type="text" class="form-control required datepicker"
											id="date" name="date" placeholder="DD/MM/YYYY"> <span
											class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="bank" class="col-md-4 control-label required">Bank</label>
									<div class="col-md-8">
										<select name="bank" id="bank" class="form-control required"
											style="width: 100%;" data-placeholder="Select Bank">
											<option></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="reference" class="col-md-4 control-label">Reference</label>
									<div class="col-md-8">
										<input type="text" class="form-control" id="reference"
											name="reference">
									</div>
								</div>
								<div class="form-group row">
									<label for="loanType" class="col-md-4 control-label required">Loan
										Type</label>
									<div class="col-md-8">
										<select name="loanType" id="loanType"
											class="form-control required select2" style="width: 100%;"
											data-placeholder="Select Loan Type">
											<option></option>
											<option value="1">Syndicate Loan</option>
											<option value="2">Toza Loan</option>
											<option value="3">Swap Loan/Corporate bond</option>
											<option value="4">Normal Loan</option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="dueDate" class="col-md-4 control-label required">Due
										Date</label>
									<div class="col-md-8">
										<input type="text" class="form-control required datepicker"
											id="dueDate" name="dueDate" placeholder="DD/MM/YYYY">
										<span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="leaveDay" class="col-md-4 control-label">Leave
										Day</label>
									<div class="col-md-8">
										<select name="leaveDay" id="leaveDay"
											class="form-control select2" style="width: 100%;">
											<option value="Pay Before" selected="selected">Pay
												Before</option>
											<option value="Pay After">Pay After</option>
										</select>
									</div>
								</div>
								<div class="form-group row">
									<label for="savingAccount" class="col-md-4 control-label">Savings
										Account</label>
									<div class="col-md-8">
										<input type="checkbox" name="savingAccount" id="savingAccount"
											value="0" />
									</div>
								</div>
								<div class="form-group row">
									<label for="savingsAccountAmount"
										class="col-md-4 control-label">Savings Account Amount</label>
									<div class="col-md-8">
										<input type="text" class="form-control autonumber"
											id="savingsAccountAmount" name="savingsAccountAmount"
											disabled="disabled" data-m-dec="0" data-a-sign="¥ ">
									</div>

								</div>
								<div class="form-group row">
									<label for="savingsBankAccount" class="col-md-4 control-label">Savings
										Bank Account</label>
									<div class="col-md-8">
										<select name="savingsBankAccount" id="savingsBankAccount"
											class="form-control" style="width: 100%;"
											data-placeholder="Select Bank" disabled="disabled">
											<option></option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group row">
									<label for="principalPaymentFrequency"
										class="col-md-4 control-label">Principal Payment
										Frequency</label>
									<div class="col-md-8">
										<select name="principalPaymentFrequency"
											id="principalPaymentFrequency" class="form-control select2"
											style="width: 100%;">
											<option value="1">Monthly</option>
											<option value="3">Quaterly</option>
											<option value="6">Half Yearly</option>
											<option value="12">Yearly</option>
											<option value="10">Contract Basis</option>
										</select>
									</div>
								</div>
								<div class="form-group row">
									<label for="interestPaymentFrequency"
										class="col-md-4 control-label">Interest Payment
										Frequency</label>
									<div class="col-md-8">
										<select name="interestPaymentFrequency"
											id="interestPaymentFrequency" class="form-control select2"
											style="width: 100%;">
											<option value="1">Monthly</option>
											<option value="3">Quaterly</option>
											<option value="6">Half Yearly</option>
											<option value="12">Yearly</option>
										</select>
									</div>
								</div>
								<div class="form-group row">
									<label for="loanAmount" class="col-md-4 control-label required">Loan
										Amount</label>
									<div class="col-md-8">
										<input type="text"
											class="form-control required autonumber calc-loan"
											id="loanAmount" name="loanAmount" data-m-dec="0"
											data-a-sign="¥ "> <span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="firstPaymentDate"
										class="col-md-4 control-label required">First Payment
										Date</label>
									<div class="col-md-8">
										<input type="text" class="form-control required datepicker"
											id="firstPaymentDate" name="firstPaymentDate"
											placeholder="DD/MM/YYYY"> <span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="rateOfInterest"
										class="col-md-4 control-label required">Rate of
										Interest</label>
									<div class="col-md-8">
										<input type="text"
											class="form-control required autonumber calc-loan"
											id="rateOfInterest" name="rateOfInterest" data-a-sign=" %"
											data-p-sign="s" data-v-max="100" data-m-dec="4"> <span
											class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="loanTerm" class="col-md-4 control-label required">Loan
										Terms (Months)</label>
									<div class="col-md-8">
										<input type="text"
											class="form-control required autonumber calc-loan"
											id="loanTerm" name="loanTerm" data-a-sep="" data-m-dec="0">
										<span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="monthlyEmi" class="col-md-4 control-label required">Monthly
										Principal EMI</label>
									<div class="col-md-8">
										<input type="text" class="form-control required autonumber"
											id="monthlyEmi" name="monthlyEmi" data-m-dec="0"
											data-a-sign="¥ "> <span class="help-block"></span>
									</div>
								</div>
								<div class="form-group row">
									<label for="description" class="col-md-4 control-label">Loan
										Purpose/Description</label>
									<div class="col-md-8">
										<input type="text" class="form-control " id="description"
											name="description"> <span class="help-block"></span>
									</div>
								</div>
								<div class="row">
									<!-- <div class="form-group col-md-3">
									<label for="loanAmount">Loan Amount</label> <input
										type="text" class="form-control autonumber calc-loan"
										id="loanAmount" name="loanAmount" data-m-dec="0"
										data-a-sign="¥ ">
								</div> -->
									<!-- <div class="form-group col-md-3">
									<label for="totalInterest">Total Interest</label> <input
										type="text" class="form-control autonumber"
										id="totalInterest" name="totalInterest" data-m-dec="0"
										data-a-sign="¥ " readonly="readonly">
								</div>
								<div class="form-group col-md-3">
									<label for="installmentAmount">Installment Amount</label> <input
										type="text" class="form-control autonumber"
										id="installmentAmount" name="installmentAmount"
										data-m-dec="0" data-a-sign="¥ " readonly="readonly">
								</div>
								<div class="form-group col-md-3">
									<label for="totalPayable">Total Payable</label> <input
										type="text" class="form-control autonumber"
										id="totalPayable" name="totalPayable" readonly="readonly"
										data-m-dec="0" data-a-sign="¥ " readonly="readonly">
								</div> -->
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="table-responsive confirmloan hidden">
					<table id="table-confirmloan"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">

						<thead>
							<tr>
								<th>#</th>
								<th>Due Date</th>
								<th>Principal Amount</th>
								<th>Interest Amount</th>
								<th>Total Amount</th>
								<th>Opening Balance</th>
								<th>Closing Balance</th>
								<th>Status</th>



							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>

		</div>
		<div class="box-footer">
			<div class="pull-right">
				<button class="btn btn-primary hidden" id="previous">Previous</button>
				<button class="btn btn-primary" id="next">Next</button>
				<button class="btn btn-primary hidden" id="create">Create</button>
				<!-- 					<button type="button" class="btn btn-primary" id="calculate">Calculate</button> -->
				<button type="reset" class="btn btn-primary"
					onclick="location.reload();">Reset</button>
			</div>
		</div>
		<%-- </form> --%>
	</div>
</section>

