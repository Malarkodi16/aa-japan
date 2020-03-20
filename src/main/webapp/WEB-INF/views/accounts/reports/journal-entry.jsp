<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Journal Entry</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Reports</span></li>
		<li class="active">Journal Entry</li>
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
			<form id="journalEntryForm">
				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label>Journal Date</label> <input
								type="text" id="transactionDate" name="transactionDate" readonly="true"
								class="form-control datepicker transactionDate"
								placeholder="Transaction Date"> <span class="help-block"></span>
						</div>
					</div>
				</div>
				<div class="container-fluid journalEntry">
					<div id="clone-container-journalEntry">
						<div class="clone-container-journalEntry-toclone">
							<div class="row credit">
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Code</label> <select name="code"
											id="code" class="form-control required select2-tag"
											style="width: 100%;" data-placeholder="Select Code">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Transaction Type</label><select
											id="type" name="type"
											class="form-control type select2-tag valid-type"
											data-placeholder="Select Type" style="width: 100%;">
											<option value=""></option>
											<option value="1">Credit</option>
											<option value="0">Debit</option>
										</select><span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<label class="required">Amount</label><input type="text"
										class="form-control autonumber amount required" name="amount"
										data-a-sign="¥ " data-v-min="0" data-m-dec="0"><span
										class="help-block"></span>
								</div>
								<div class="col-md-2">
									<label>Remarks</label><input type="text" class="form-control"
										name="description">
								</div>

								<div class="col-md-2 clone-btn" style="margin-top: 23px;">
									<button type="button" class="btn btn-success clone">
										<i class="fa fa-fw fa-plus"></i>
									</button>
									<button class="btn btn-danger delete">
										<i class="fa fa-fw fa-remove"></i>
									</button>
								</div>
							</div>

						</div>
					</div>
				</div>


				<div class="box-footer">
					<div class="pull-right">
						<button type="button" id="btn-save-entry" class="btn btn-primary">
							<i class="fa fa-save mr-5"></i>Save
						</button>
						<button type="reset" class="btn btn-primary">
							<i class="fa fa-repeat mr-5" onclick="location.reload();"></i>Reset
						</button>
					</div>
				</div>
			</form>
			<!-- /.form:form -->
		</div>
	</div>
</section>
