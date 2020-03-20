<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form action="#" id="create-proforma-form">
	<div class="box-body">
		<div class="container-fluid" id="proforma-invoice-container">
			<fieldset id="invoice-details">
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<label class="required">Customer</label><select name="customerId"
								id="customerCode" class="form-control select2 invoiceData"
								style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
						<span class="help-block"></span>
					</div>
					<div class="col-sm-2">
						<div class="form-group ">
							<label class="required">Consignee</label>
							<div class="input-group">
								<div class="elemenr-wrapper" style="width: 80%;">
									<select name="consigneeId" id="cFirstName"
										class="form-control select2 invoiceData" style="width: 150px;"
										data-placeholder="Select Consignee">
										<option value=""></option>
									</select>
								</div>
								<div class="input-group-addon" title="Add Consignee"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#add-porfoma-consigner"
									style="width: 20%;">
									<i class="fa fa-plus"></i>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group ">
							<label>Notify Party</label>
							<div class="input-group">
								<div class="elemenr-wrapper" style="width: 80%;">
									<select name="notifypartyId" id="npFirstName"
										class="form-control select2 invoiceData" style="width: 150px;"
										data-placeholder="Select Notify Party">
										<option value=""></option>
									</select>
								</div>
								<div class="input-group-addon" title="Add Notify Party"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#add-porfoma-ntfy-party"
									style="width: 20%;">
									<i class="fa fa-plus"></i>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label>Payment Type</label> <select
								class="form-control  invoiceData select2-select "
								name="paymentType" id="paymentType" style="width: 100%;"
								data-placeholder="Select Payment Type">
								<option value=""></option>
								<option value="FOB">FOB</option>
								<option value="C&F">C&F</option>
								<option value="CIF">CIF</option>
							</select>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label>Currency Type</label> <select
								class="form-control select2-select invoiceData"
								name="currencyType" id="currencyType" style="width: 100%;"
								data-placeholder="Select Curency Type">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
				</div>
			</fieldset>
			<fieldset id="invoice-items">
				<div id="item-invoice-clone" class="hidden">
					<div class="item-invoice">
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<input type="hidden" name="stockNo" id="stockNo"
										class="form-control" value=""> <input type="hidden"
										name="maker" id="maker" class="form-control" value="">
									<input type="hidden" name="firstRegDate" id="firstRegDate"
										class="form-control" value=""> <input name="model"
										type="text" class="form-control" value="" readonly="readonly" />
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<input name="chassisNo" class="form-control"
										readonly="readonly" />
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input name="firstRegDate" id="firstRegDate" type="text"
										class="form-control" readonly="readonly" />
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input name="hsCode" id="hsCode" type="text"
										class="form-control" /> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="fob" name="fob"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="insurance" name="insurance"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="shipping" name="shipping"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="freight" name="freight"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" name="total" class="form-control autonumber"
										data-m-dec="0" data-a-sign="&yen; " style="width: 100px;" />
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<button type="button"
										class="form-control close btn-remove-item" aria-label="Close">
										<span aria-hidden="true"><i class="fa fa-fw fa-remove"></i></span>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="item-invoice-container" style="max-height: 250px;">
					<div class="item-invoice-header">
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label>Maker/Model</label>
									<!-- <input name="stockNo" type="hidden"
										class="form-control" value="" /> <input name="maker"
										type="hidden" class="form-control" value="" /> <input
										name="model" type="text" class="form-control"
										readonly="readonly" /> -->
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Chassis No.</label>
									<!-- <input name="chassisNo"
										class="form-control" readonly="readonly" /> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Year</label>
									<!-- <input name="firstRegDate"
										class="form-control" readonly="readonly" /> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>HS Code</label>
								</div>
							</div>
							<div class="col-md-1" id="gfob">
								<div class="form-group">
									<label>FOB</label>
									<!-- <input type="text" name="fob"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
									<span class="help-block"></span> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Insurance</label>
									<!-- <input type="text" name="insurance"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<!-- shipping label changed into inspection -->
									<label>Inspection</label>
									<!-- <input type="text" name="shipping"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Freight</label>
									<!-- <input type="text" name="freight"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Amount</label>
									<!-- <input type="text" name="total"
										class="form-control calculation autonumber" data-m-dec="0"
										data-a-sign="&yen; " style="width: 100px;" readonly="readonly" />
									<span class="help-block"></span> -->
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Remove</label>
									<!-- <button type="button"
										class="form-control close btn-remove-item" aria-label="Close">
										<span aria-hidden="true"><i class="fa fa-fw fa-remove"></i></span>
									</button> -->
								</div>
							</div>
						</div>
					</div>
					<div id="item-invoice-clone-container"></div>
				</div>
			</fieldset>
			<div id="totalInvoiceDiv">
				<div class="row">
					<div class="col-md-2">
						<div class="form-group"></div>
					</div>
					<div class="col-md-3">
						<div class="form-group"></div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<label>Total</label>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<input type="text" id="fobTotal" name="fobTotal"
								class="form-control invoiceData autonumber" data-m-dec="0"
								data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<input type="text" id="insuranceTotal" name="insuranceTotal"
								class="form-control invoiceData autonumber" data-m-dec="0"
								data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<input type="text" id="shippingTotal" name="shippingTotal"
								class="form-control invoiceData autonumber" data-m-dec="0"
								data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<input type="text" id="freightTotal" name="freightTotal"
								class="form-control invoiceData autonumber" data-m-dec="0"
								data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group">
							<input type="text" id="grand-total" name="grand-total"
								class="form-control invoiceData autonumber" data-m-dec="0"
								data-a-sign="&yen; " style="width: 100px;" readonly="readonly" />
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-1">
						<div class="form-group"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<!-- /.box-body -->
