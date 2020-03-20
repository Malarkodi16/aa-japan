<form action="#" id="create-sales-form">
	<div class="box-body">
		<div class="container-fluid" id="sales-invoice-container">
			<fieldset id="sales-details">
				<div class="row">
					<div class="col-sm-3">
						<div class="form-group">
							<input type="hidden" name="customerId" id="customerSalesId"
								class="form-control salesData" value=""> <label
								class="required">Customer</label>
							<div class="form-group">
								<input name="customernameId" id="customernameSalesId"
									type="text" class="form-control salesData" readonly="readonly" />
							</div>

						</div>
						<span class="help-block"></span>
					</div>
					<div class="col-sm-2">
						<div class="form-group ">
							<label>Consignee</label>
							<div class="input-group">
								<div class="elemenr-wrapper" style="width: 80%;">
									<select name="consigneeId" id="cFirstsalesName"
										class="form-control select2 salesData" style="width: 150px;"
										data-placeholder="Select Consignee">
										<option value=""></option>
									</select>
								</div>
								<div class="input-group-addon" title="Add Consignee"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#add-sales-consigner"
									style="width: 20%;">
									<i class="fa fa-plus"></i>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group ">
							<label class="required">Notify Party</label>
							<div class="input-group">
								<div class="elemenr-wrapper" style="width: 80%;">
									<select name="notifypartyId" id="npFirstsalesName"
										class="form-control select2 salesData" style="width: 150px;"
										data-placeholder="Select Notify Party">
										<option value=""></option>
									</select>
								</div>
								<div class="input-group-addon" title="Add Notify Party"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#add-sales-ntfy-party"
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
								class="form-control select2-select salesData" name="paymentType"
								id="paymentSalesType" style="width: 100%;"
								data-placeholder="Select Payment Type">
								<option value=""></option>
								<option value="FOB">FOB</option>
								<option value="C&F">C &amp; F</option>
								<option value="CIF">CIF</option>
							</select>
						</div>
					</div>
					<div class="col-sm-2">
						<div class="form-group">
							<label>Currency Type</label> <select
								class="form-control select2-select salesData"
								name="currencyType" id="currencySalesType" style="width: 100%;"
								data-placeholder="Select Curency Type">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
				</div>
			</fieldset>
			<fieldset id="sales-items">
				<div id="item-sales-clone" class="hidden">
					<div class="item-sales">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<input type="hidden" name="stockNo" id="stockNo"
										class="form-control" value=""> <input type="hidden"
										name="maker" id="maker" class="form-control" value="">
									<input name="model" type="text" class="form-control" value=""
										readonly="readonly" />

								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<input name="chassisNo" class="form-control"
										readonly="readonly" />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<input name="firstRegDate" id="firstRegDate" type="text"
										class="form-control" readonly="readonly" />
								</div>
							</div>
							<!-- <div class="col-md-1">
								<div class="form-group">
									<input type="text" id="fob" name="fob"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="insurance" name="insurance"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="shipping" name="shipping"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<input type="text" id="freight" name="freight"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<input type="text" name="total" id="total"
										class="form-control calculation autonumber"
										readonly="readonly" data-a-sign="&yen; " style="width: 150px;" />
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="item-sales-container" style="max-height: 250px;">
					<div class="item-sales">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label>Maker/Model</label> <input name="stockNo" type="hidden"
										class="form-control" value="" /> <input name="maker"
										type="hidden" class="form-control" value="" /> <input
										name="model" type="text" class="form-control"
										readonly="readonly" />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Chasis No.</label> <input name="chassisNo"
										class="form-control" readonly="readonly" />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Year</label> <input name="firstRegDate"
										class="form-control" readonly="readonly" />
								</div>
							</div>
							<!-- <div class="col-md-1" id="gfob">
								<div class="form-group">
									<label>FOB</label> <input type="text" name="fob"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" readonly="readonly" />
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Insurance</label> <input type="text" name="insurance"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Shipping</label> <input type="text" name="shipping"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-1">
								<div class="form-group">
									<label>Freight</label> <input type="text" name="freight"
										class="form-control calculation autonumber"
										data-a-sign="&yen; " style="width: 88px;" /> <span
										class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<label>Total</label> <input type="text" name="total"
										class="form-control calculation autonumber"
										readonly="readonly" data-a-sign="&yen; " style="width: 150px;" />
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
					<div id="item-sales-clone-container"></div>
				</div>
				<div id=totalSalesDiv>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group"></div>
						</div>
						<div class="col-md-3">
							<div class="form-group"></div>
						</div>

						<div class="col-md-3">
							<div class="form-group">
								<label>TOTAL</label>
							</div>
						</div>
						<!-- <div class="col-md-1">
							<div class="form-group">
								<input type="text" id="fobsalesTotal" name="fobTotal"
									class="form-control salesData autonumber" readonly="readonly"
									data-a-sign="&yen; " style="width: 88px;" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<input type="text" id="insurancesalesTotal"
									name="insuranceTotal" class="form-control salesData autonumber"
									readonly="readonly" data-a-sign="&yen; " style="width: 88px;" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<input type="text" id="shippingsalesTotal" name="shippingTotal"
									class="form-control salesData autonumber" readonly="readonly"
									data-a-sign="&yen; " style="width: 88px;" />
							</div>
						</div>
						<div class="col-md-1">
							<div class="form-group">
								<input type="text" id="freightsalesTotal" name="freightTotal"
									class="form-control salesData autonumber" readonly="readonly"
									data-a-sign="&yen; " style="width: 88px;" />
							</div>
						</div> -->
						<div class="col-md-2">
							<div class="form-group">
								<input type="text" id="grand-total-sales"
									name="grand-total-sales"
									class="form-control salesData autonumber" readonly="readonly"
									data-a-sign="&yen; " style="width: 150px;" />
							</div>
						</div>
					</div>
				</div>
			</fieldset>
		</div>
	</div>
</form>
<!-- /.box-body -->
