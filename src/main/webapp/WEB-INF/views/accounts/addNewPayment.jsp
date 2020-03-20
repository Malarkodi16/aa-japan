<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Day Book Entry</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Accounts</span></li>
		<li class="active">Day Book Entry</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<form method="POST" id="paymentForm" action="${contextPath}/accounts/save/addpayment">
				<div class="box-body" id="cloneTO">
					<div class="container-fluid">
						<div class="row received">
							<div class="col-sm-2">
								<div class="form-group">
									<label class="required">Invoice Type</label> <select
										name="invoiceType" id="coaType"
										class="form-control required select2-tag"
										data-placeholder="Select Invoice Type">
										<option></option>
									</select>
								</div>
							</div>
							<div class="col-sm-2">
								<div class="form-group">
									<label class="required">Remit To</label> <select
										name="remitTo" id="supplrType"
										class="form-control required select2-tag"
										data-placeholder="Select Remit To">
										<option></option>
									</select>
								</div>
							</div>
							<div class="form-group col-md-2">
								<label class="required">Invoice No</label> <input type="text"
								name="invoiceNo" class="form-control" placeholder="Invoice No">
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required"> Date:</label>

									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control pull-right datepicker"
											name="invoiceDate" id="startDatePicker" placeholder="Select Date ">
									</div>
									<!-- /.input group -->
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Due Date:</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control pull-right datepicker"
											name="dueDate" id="endDatePicker" placeholder="Select Due Date ">
									</div>
								</div>
							</div>
						</div>
						<fieldset>
							<legend>Payment Item</legend>
							<div id="clone-container-addpayment">
								<div class="clone-container-paymentclone">
									<div class="row">
										<div class="col-md-2">
											<div class="form-group">
												<label class="required">Type</label> <select name="items[0].paymentType"
													id="coaDesc" class="form-control required select2-tag coaDesc"
													data-placeholder="Select Payment Type">
													<option></option>
												</select>
											</div>
										</div>
										<div class="form-group col-md-2">
											<label class="required">Description</label> <input
												type="text" name="items[0].description" class="form-control" placeholder="Write Description">
											<!-- /.input group -->
										</div>
										<div class="form-group col-md-2">
											<label class="required">Amount</label> <input type="text" name="items[0].purchaseCost"
												class="form-control" placeholder="Enter Amount">
											<!-- /.input group -->
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label>Stock No</label> <select name="items[0].stockNo" id="stockNo"
													class="form-control required select2-tag stockNo" data-placeholder="Select Stock No">
													<option value=""></option>
												</select>
											</div>
										</div>
										<div class="form-group col-md-2">
											<label>Remarks</label> <input type="text" name="items[0].remarks"
												class="form-control" placeholder="Write Remarks">
											<!-- /.input group -->
										</div>
										<div class="col-md-2" style="margin-top: 23px;">
											<button type="button" class="btn btn-success btn-clone">
												<i class="fa fa-fw fa-plus"></i>Add
											</button>
											<button class="btn btn-danger btn-delete">
												<i class="fa fa-fw fa-remove"></i>Delete
											</button>
										</div>
									</div>
								</div>
							</div>
						</fieldset>
							<div class="col-md-12">
								<button type="submit" class="btn btn-primary pull-right">
									Save Payment</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>