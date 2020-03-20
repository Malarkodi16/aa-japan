<form action="#" id="edit-shipping-instruction-form">
	<div class="box-body">
		<div class="container-fluid" id="shipping-invoice-container">
			<fieldset id="shipping-items">
				<div id="item-shipping-container" style="max-height: 600px;">
					<div class="item-shipping">
						<div class="row form-group">
							<input type="hidden" name="stockNo" id="stockNo"
								class="form-control shippingData" value=""> <input
								type="hidden" name="customerId" id="customerId"
								class="form-control shippingData" value=""> <input
								type="hidden" name="shippingInstructionId" id="shippingInstructionId"
								class="form-control shippingData" value="">
							<div class="col-md-2">
								<div class="form-group">
									<label>Chassis No.</label> <input name="chassisNo"
										class="form-control shippingData" readonly="readonly" />
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Customer</label> <input name="customernameId"
										class="form-control shippingData" readonly="readonly" />
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Consignee</label> <select
										name="consigneeId" id="cFirstshippingName"
										class="form-control select2-select shippingData"
										style="width: 100%;" data-placeholder="Select Consignee">
										<option value=""></option>

									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Notify Party</label> <select name="notifypartyId"
										id="npFirstshippingName"
										class="form-control select2-select shippingData"
										style="width: 100%;" data-placeholder="Select Notify Party">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<label class="required">Destination Country</label> <select
									name="destCountry" id="destcountry"
									class="form-control select2-select shippingData country"
									style="width: 100%;"
									data-placeholder="Select Destination Country">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Port</label> <select name="destPort"
										id="destport"
										class="form-control select2-select shippingData port"
										style="width: 100%;"
										data-placeholder="Select Destination Port">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-md-2 hidden" id="yardFields">
								<div class="form-group">
									<label class="required">CFS Yard</label> <select name="yard" id="yard"
										class="form-control select2-select shippingData yard"
										style="width: 100%;" data-placeholder="Select Yard">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Payment Type</label>
									<div class="element-wrapper">
										<select id="paymentType" name="paymentType"
											class="form-control select2-select shippingData"
											data-placeholder="Payment Type" style="width: 100%;">
											<option value="">Select Payment Type</option>
											<option data-type="PREPAID" value="PREPAID">PREPAID</option>
											<option data-type="COLLECT" value="COLLECT">COLLECT</option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>&nbsp;</label>
									<div class="form-control">
										<input type="checkbox" value="1" name="inspectionFlag"
											class="shippingData" autocomplete="off"><label
											class="ml-5">Arrange Inspection</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row form-group estimatedData">
							<div class="col-md-6" id="estimatedData">
								<div class="col-md-3">
									<div class="form-group">
										<label class="ml-5"> <input name="estimatedType"
											id="immediate" type="radio" class="minimal estimated-data"
											value="0" checked="checked"> Immediate
										</label>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="ml-5"> <input name="estimatedType"
											id="next-available" type="radio"
											class="minimal estimated-data" value="1"> Next
											Available
										</label>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="ml-5"> <input name="estimatedType"
											id="preferredMonth" type="radio"
											class="minimal estimated-data" value="2"> Preferred
											Month
										</label>
									</div>
								</div>
								<div class="col-md-6 estimatedDate" id="estimatedDate">
									<div class="form-group">
										<div class="col-md-6">
											<div class="form-group">
												<label class="required">Estimated Departure</label> <input
													name="estimatedDeparture"
													class="form-control shippingData datePicker"
													placeholder="mm/yyyy" readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="required">Estimated Arrival</label> <input
													name="estimatedArrival"
													class="form-control shippingData datePicker"
													placeholder="mm/yyyy" readonly="readonly" /> <span
													class="help-block"></span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</fieldset>
		</div>
	</div>
</form>
<!-- /.box-body -->
