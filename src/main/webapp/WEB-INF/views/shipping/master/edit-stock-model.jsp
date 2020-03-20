<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Edit Stock Model Type</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Edit Stock Model Type</li>
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
			<form id="stockModelTypeForm">
				<input type="hidden" id="code" name="code" value="${code}"
					class="form-control stock-details-to-save object">
				<div class="box-body">
					<div class="container-fluid">
						<fieldset>
							<legend>Model Type</legend>
							<div class="row">
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Model Type</label>
										<div class="element-wrapper">
											<div class="input-group">
												<input type="text" id="modelType" name="modelType"
													class="form-control required stock-details-to-save object" />
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Maker</label>
										<div class="element-wrapper">
											<select name="maker" id="maker"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Maker">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Model</label>
										<div class="element-wrapper">
											<select name="model" id="model"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Model">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">CC</label>
										<div class="element-wrapper">
											<div class="input-group">
												<input type="text" id="cc" name="cc"
													class="form-control required stock-details-to-save object" />
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<label class="required">Unit</label>
										<div class="element-wrapper">
											<select name="unit" id="unit"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Unit">
												<option value="cc">CC</option>
												<option value="kw">KW</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								
								<!-- div class="col-md-3">
									<div class="form-group">
										<label class="required">Category</label>
										<div class="element-wrapper">
											<select name="category" id="category"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Category">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div> -->
								<div class="col-md-3">
									<div class="form-group">
										<input type="hidden" id="category" name="category"
											class="form-control stock-details-to-save object"
											style="width: 100%;" /> <label class="required">Sub
											Category</label>
										<div class="element-wrapper">
											<select name="subcategory" id="subcategory"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select SubCategory">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Transmission</label>
										<div class="element-wrapper">
											<select name="transmission" id="transmission"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Transmission">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Manual Types</label>
										<div class="element-wrapper">
											<select name="manualTypes" id="manualTypes"
												class="form-control select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Manual Type"
												disabled="true">
												<option value=""></option>
												<option value="F4">F4</option>
												<option value="F5">F5</option>
												<option value="F6">F6</option>
												<option value="F7">F7</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">

								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Fuel</label>
										<div class="element-wrapper">
											<select name="fuel" id="fuel"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Fuel">
												<option value=""></option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Driven</label>
										<div class="element-wrapper">
											<select name="driven" id="driven"
												class="form-control required select2-select stock-details-to-save object"
												style="width: 100%;" data-placeholder="Select Driven">
												<option value="Left">Left</option>
												<option value="Right">Right</option>
											</select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>

							</div>
						</fieldset>
						<fieldset>
							<legend>Equipment</legend>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="A/C" /><span
												class="ml-5">A/C</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/S" /><span
												class="ml-5">P/S</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/W" /><span
												class="ml-5">P/W</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="S/R" /><span
												class="ml-5">S/R</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="A/W" /><span
												class="ml-5">A/W</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="ABS" /><span
												class="ml-5">ABS</span>
											</label>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="AIR BAG" /><span
												class="ml-5">Air Bag</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="4WD" /><span
												class="ml-5">4WD</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="P/M" /><span
												class="ml-5">P/M</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="TV" /><span
												class="ml-5">TV</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="PD" /><span
												class="ml-5">PD</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="NV" /><span
												class="ml-5">NV</span>
											</label>
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="R/S" /><span
												class="ml-5">R/S</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="F/LAMP" /><span
												class="ml-5">F/LAMP</span>
											</label>
										</div>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<div class="element-wrapper">
											<label> <input type="checkbox" name="equipment"
												class="stock-details-to-save array" value="CD" /><span
												class="ml-5">CD</span>
											</label>
										</div>
									</div>
								</div>
							</div>
						</fieldset>
					</div>

				</div>
				<div class="box-footer">
					<div class="pull-right">
						<button type="button" id="btn-save" class="btn btn-primary">
							<i class="fa fa-save mr-5"></i>Save
						</button>
						<button type="reset" class="btn btn-primary">
							<i class="fa fa-repeat mr-5"></i>Reset
						</button>
					</div>
				</div>
			</form>
			<!-- /.form:form -->
		</div>
	</div>
</section>
