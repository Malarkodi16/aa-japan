<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form action="#" id="transport-arrangement-form">
	<div class="container-fluid" id="transport-order-container">
		<fieldset id="transport-schedule-details">
			<legend>Schedule Details</legend>
			<input type="hidden" name="purchaseDate" value="" /> <input
				type="hidden" name="supplierCode" value="" />
			<div class="row">
				<div class="col-md-6" id="selectedDate">
					<div class="col-md-6">
						<div class="form-group">
							<label class="ml-5"> <input name="selecteddate"
								id="auto-date" type="radio" class="minimal schedule-data"
								value="0"> Pickup Before Payment
							</label>
						</div>
					</div>

					<div class="col-md-6">
						<div class="form-group has-error">
							<label class="ml-5" for="inputError"> <input
								name="selecteddate" id="specified-date" type="radio"
								class="minimal schedule-data" value="1"> Pickup After
								Payment
							</label>
						</div>
					</div>
				</div>
				<div class="col-md-6" id="selectedtype">
					<label> <input name="selectedtype" type="radio" id="asap"
						class="minimal selected-type" value="0" checked> ASAP
					</label> <label class="ml-5"> <input name="selectedtype"
						id="urgent" type="radio" class="minimal selected-type" value="1">
						URGENT
					</label>
				</div>
			</div>
			<div class="row" id="schedule-date">

				<div class="col-md-3">
					<div class="form-group">
						<label class="required">Pickup Date &amp; Time</label>
						<div class="row">
							<div class="col-md-8 nopadding_right">
								<div class="input-group date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<div class="element-wrapper">
										<input type="text"
											class="form-control datepicker schedule-data auto-date valid-required-transport-fields"
											id="pickupDate" name="pickupDate" placeholder="dd-mm-yyyy"
											readonly="readonly">
									</div>
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-4 nopadding_left">
								<div class="element-wrapper">
									<select class="form-control schedule-data auto-date"
										name="pickupTime">
										<option></option>
										<option value="AM">AM</option>
										<option value="PM">PM</option>
									</select>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="required">Payment Date &amp; Time</label>
						<div class="row">
							<div class="col-md-8 nopadding_right">
								<div class="input-group date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</div>
									<div class="element-wrapper">
										<input type="text"
											class="form-control datepicker schedule-data auto-date valid-required-transport-fields"
											id="deliveryDate" name="deliveryDate"
											placeholder="dd-mm-yyyy" readonly="readonly">
									</div>
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-4 nopadding_left">
								<div class="element-wrapper">
									<select class="form-control schedule-data auto-date"
										name="deliveryTime">
										<option></option>
										<option value="AM">AM</option>
										<option value="PM">PM</option>
									</select>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
		</fieldset>
		<fieldset id="transport-items">
			<legend>Transport Details</legend>
			<div id="item-vechicle-clone" class="hidden">
				<div class="item-vehicle">
					<input type="hidden" name="data" value="" /><input type="hidden"
						name="model" value="" /> <input type="hidden" name="maker"
						value="" /> <input type="hidden" name="lotNo" value="" /> <input
						type="hidden" name="posNo" value="" /> <input type="hidden"
						name="numberPlate" value="" /> <input type="hidden"
						name="destinationCountry" value="" /><input type="hidden"
						name="hiddencharge" value="" /><input type="hidden" name="id"
						value="" /> <input type="hidden" name="transportCategory"
						value="" />
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Shuppin No.</label>
								<div class="element-wrapper">
									<input type="text" name="lotNo" class="form-control"
										readonly="readonly" />
								</div>

							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Chassis No.</label><input name="stockNo" type="hidden"
									class="form-control" value="" />
								<div class="element-wrapper">
									<input name="chassisNo" type="text" class="form-control"
										value="" readonly="readonly" />
								</div>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label>Model</label>
								<div class="element-wrapper">
									<input type="text" name="model" class="form-control"
										readonly="readonly" />
								</div>

							</div>
						</div>
						<div class="col-md-2 hidePosNo">
							<div class="form-group">
								<label class="required">Pos No.</label>
								<div class="element-wrapper">
									<select name="posNo" data-placeholder="Select Pos.No"
										class="form-control valid-required-transport-fields posNo select2-select"><option
											value=""></option></select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Destination Country</label>
								<div class="element-wrapper">
									<select name="destinationCountry"
										data-placeholder="Select Destination Country"
										class="form-control destinationCountry valid-required-transport-fields"><option
											value=""></option></select><span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Destination Port</label>
								<div class="element-wrapper">
									<select name="destinationPort"
										data-placeholder="Select Destination Port"
										class="form-control select2-select destinationPort valid-required-transport-fields"><option
											value=""></option></select><span class="help-block"></span>
								</div>
							</div>
						</div>

					</div>
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Pickup Location</label>
								<div class="element-wrapper">
									<select name="pickupLocation"
										class="form-control select2-select  with-others pickupLocation valid-required-transport-fields"
										data-placeholder="Select Pickup Location"><option
											value=""></option></select> <span class="help-block"></span>
								</div>
								<div class="others-input-container hidden">
									<div class="element-wrapper">
										<textarea class="form-control others-input" rows="2"
											placeholder="Enter.." name="pickupLocationCustom"></textarea>
									</div>
									<a class="show-hand-cursor show-dropdown">show dropdown</a>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Drop Location</label>
								<div class="element-wrapper">
									<select name="dropLocation"
										class="form-control select2-select with-others drop-location valid-required-transport-fields"
										data-placeholder="Select Drop Location"><option
											value=""></option></select> <span class="help-block"></span>
								</div>
								<div class="others-input-container hidden">
									<div class="element-wrapper">
										<textarea class="form-control others-input" rows="2"
											placeholder="Enter.." name="dropLocationCustom"></textarea>
									</div>
									<a class="show-hand-cursor show-dropdown">show dropdown</a>
								</div>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Transporter</label>
								<div class="element-wrapper">
									<select
										class="form-control select2-select transporter valid-required-transport-fields"
										name="transporter" data-placeholder="Select Transport">
										<option value=""></option>
									</select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Forwarder</label>
								<div class="element-wrapper">
									<select class="form-control select2-select" name="forwarder"
										data-placeholder="Select Forwarder">
										<option value=""></option>
									</select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Category</label>
								<div class="element-wrapper">
									<input type="text" name="category" class="form-control"
										readonly="readonly" />
								</div>
								<input type="hidden" name="subcategory" class="form-control"
									readonly="readonly" />
							</div>
						</div>
						<div class="col-md-2">

							<div class="form-group">
								<label class="required">Charge</label>
								<div class="element-wrapper">
									<input name="charge"
										class="form-control charge valid-required-transport-fields"
										data-a-sign="¥ " data-m-dec="0" />
								</div>
								<span class="help-block"></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Remark</label>
								<div class="element-wrapper">
									<input name="remarks" class="form-control"
										placeholder="Enter.." />
								</div>
							</div>
						</div>
						<div class="col-md-2 hidden" id="arrivalDate">
							<div class="form-group">
								<label>Arrival Date</label>
								<div class="element-wrapper">
									<input type="text" id="etd" name="etd"
										class="form-control valid-required-transport-fields datepicker"
										placeholder="Arrival Date">
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<!-- <div class="col-md-2">
							<div class="form-group">
								<label></label>
								<div class="form-control">
									<input type="checkbox" id="inspectionFlagYes"
										name="inspectionFlag" class="inspectionFlag" value="1"><label
										class="ml-5">Inspection Flag</label>
								</div>
							</div>
						</div> -->
					</div>

					<hr>
				</div>
			</div>
			<div id="item-vehicle-container" style="max-height: 380px;">
				<div class="item-vehicle">
					<input type="hidden" name="data" value="" /> <input type="hidden"
						name="model" value="" /> <input type="hidden" name="maker"
						value="" /> <input type="hidden" name="lotNo" value="" /> <input
						type="hidden" name="posNo" value="" /> <input type="hidden"
						name="numberPlate" value="" /> <input type="hidden"
						name="destinationCountry" value="" /><input type="hidden"
						name="hiddencharge" value="" /><input type="hidden" name="id"
						value="" /> <input type="hidden" name="transportCategory"
						value="" />
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Shuppin No.</label>
								<div class="element-wrapper">
									<input type="text" name="lotNo" class="form-control"
										readonly="readonly" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Chassis No.</label> <input name="stockNo" type="hidden"
									class="form-control" value="" />
								<div class="element-wrapper">
									<input name="chassisNo" type="text" class="form-control"
										value="" readonly="readonly">
								</div>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label>Model</label>
								<div class="element-wrapper">
									<input type="text" name="model" class="form-control"
										readonly="readonly" />
								</div>
							</div>
						</div>
						<div class="col-md-2 hidePosNo">
							<div class="form-group">
								<label class="required">Pos No.</label>
								<div class="element-wrapper">
									<select name="posNo" data-placeholder="Select Pos.No"
										class="form-control valid-required-transport-fields posNo select2-select"><option
											value=""></option></select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Destination Country</label>
								<div class="element-wrapper">
									<select name="destinationCountry"
										data-placeholder="Select Destination Country"
										class="form-control with-others destinationCountry valid-required-transport-fields"><option
											value=""></option></select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Destination Port</label>
								<div class="element-wrapper">
									<select name="destinationPort"
										data-placeholder="Select Destination Port"
										class="form-control select2-select with-others destinationPort valid-required-transport-fields"><option
											value=""></option></select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>

					</div>
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Pickup Location</label>
								<div class="element-wrapper">
									<select name="pickupLocation"
										data-placeholder="Select Pickup Location"
										class="form-control select2-select  with-others pickupLocation valid-required-transport-fields"><option
											value=""></option></select>
								</div>
								<div class="others-input-container hidden">
									<div class="element-wrapper">
										<textarea class="form-control others-input width-100" rows="2"
											placeholder="Enter.." name="pickupLocationCustom"></textarea>
									</div>
									<a class="show-hand-cursor show-dropdown">show dropdown</a>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Drop Location</label>
								<div class="element-wrapper">
									<select name="dropLocation"
										data-placeholder="Select Drop Location"
										class="form-control select2-select with-others dropLocation valid-required-transport-fields"><option
											value=""></option></select>
								</div>
								<div class="others-input-container hidden">
									<div class="element-wrapper">
										<textarea class="form-control others-input width-100" rows="2"
											placeholder="Enter.." name="dropLocationCustom"></textarea>
										<a class="show-hand-cursor show-dropdown">show dropdown</a>
									</div>
								</div>
								<span class="help-block"></span>
							</div>
						</div>

						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Transporter</label>
								<div class="element-wrapper">
									<select
										class="form-control select2-select transporter with-others valid-required-transport-fields"
										name="transporter" id="transporter"
										data-placeholder="Select Transport">
										<option value=""></option>
									</select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Forwarder</label>
								<div class="element-wrapper">
									<select class="form-control select2-select" name="forwarder"
										data-placeholder="Select Forwarder">
										<option value=""></option>
									</select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Category</label><input type="text" name="category"
									class="form-control" readonly="readonly" />
								<div class="element-wrapper">
									<input type="hidden" name="subcategory" class="form-control"
										readonly="readonly" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label class="required">Charge</label>
								<div class="element-wrapper">
									<input name="charge"
										class="form-control charge valid-required-transport-fields"
										data-a-sign="¥ " data-m-dec="0" />
								</div>
								<span class="help-block"></span>
							</div>
						</div>

					</div>
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Remarks</label>
								<div class="element-wrapper">
									<input name="remarks" class="form-control"
										placeholder="Enter.." />
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-2 hidden" id="arrivalDate">
							<div class="form-group">
								<label>Arrival Date</label>
								<div class="element-wrapper">
									<input type="text" id="etd" name="etd"
										class="form-control valid-required-transport-fields datepicker"
										placeholder="Arrival Date">
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<!-- <div class="col-md-3">
							<div class="form-group">
								<label>Arrival Date</label>
								<div class="element-wrapper">
									<input class="form-control input-date datepicker required"
										placeholder="DD-MM-YYYY" name="etd" />
								</div>
								<span class="help-block"></span>
							</div>
						</div> -->
						<!-- <div class="col-md-2">
							<div class="form-group">
								<label></label>
								<div class="form-control">
									<input type="checkbox" id="inspectionFlagYes"
										name="inspectionFlag" class="inspectionFlag" value="1"><label
										class="ml-5">Inspection Flag</label>
								</div>
							</div>
						</div> -->
					</div>
					<hr>
				</div>
				<div id="item-vehicle-clone-container"></div>
			</div>
			<div class="comment mt-5" id="transport-comment">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label>Comment</label>
							<div class="element-wrapper">
								<textarea name="comment" rows="2" class="form-control comment"
									placeholder="Enter.."></textarea>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
		</fieldset>

	</div>
</form>
<!-- /.box-body -->
