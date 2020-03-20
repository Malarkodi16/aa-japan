<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form action="#" id="payment-detail-form">
	<div class="box-body">
		<div class="container-fluid" id="payment-container">
			<div class="row">
				<input type="hidden" name="selRec" id="selRec"
					class="form-control required " /> <input type="hidden" name="flag"
					id="flag" class="form-control required " /> <input type="hidden"
					name="invoiceType" /> <input type="hidden" name="invoiceNo" /> <input
					type="hidden" name="balanceAmount" data-m-dec="0" /> <input
					type="hidden" name="balanceAmountYen" /> <input type="hidden"
					name="balanceAmountUsd" /> <input type="hidden"
					name="currencyType" /> <input type="hidden" name="exchangeRate" />
					<input type="hidden" name="ids" />
					
				<div class="col-md-3">
					<label class="required">Bank</label> <select name="bank" id="bank"
						data-placeholder="Select Bank"
						class="form-control select2-select bank" style="width: 100%">
						<option></option>
					</select> <span class="help-block"></span>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="required">Payment Date</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-fw fa-calendar"></i>
							</div>
							<input type="text" name="approvedDate" id="approvedDate"
								class="form-control required entryDate datepicker"
								placeholder="dd-mm-yyyy" readonly="readonly" />
						</div>
						<span class="help-block"></span>
					</div>
				</div>

				<div class="col-md-3" id="amountDiv">
					<div class="form-group">
						<label class="required">Amount</label> <input type="text"
							name="amount" id="amount"
							class="form-control autonumeric required" data-a-sign="¥ "
							data-m-dec="0"> <span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label>Remarks</label>
						<textarea name="remarks" id="remarks" class="form-control"
							placeholder="Enter.."></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<!-- /.box-body -->
