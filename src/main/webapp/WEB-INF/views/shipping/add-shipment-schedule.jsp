<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Shipment Schedule</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Shipping Management</span></li>
		<li class="active">Create Schedule</li>
	</ol>
</section>
<form:form method="POST" id="addshipment"
	action="${contextPath}/shipping/schedule/save"
	modelAttribute="shipmentForm">
	<!-- Content Header (Page header) -->
	<!-- Main content -->
	<section class="content">
		<div class="alert alert-success" id="alert-block"
			style="display: ${message==null||message==''?'none':'block'}">
			<strong>${message}</strong>
		</div>
		<div class="box box-solid">
			<!-- /.box-header -->
			<div class="box-body">
				<div class="row">
					<!-- /.col -->
					<!-- /.col -->
					<input type="hidden" id="scheduleId" name="scheduleId"
						value="${scheduleId}" />
					<div class="col-md-3">
						<div class="form-group">
							<label class="required">Select Shipping Company</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-university"></i>
								</div>
								<div class="element-wrapper">
									<form:select name="shippingCompanyNo" id="shippingCompanyNo"
										class="form-control select2 shippingCompanyNo"
										data-placeholder="Select ShippingCompany" path=""
										style="width: 100%;">
										<form:option value=""></form:option>
										<form:options items="${masterShippingCompany}"
											itemLabel="name" itemValue="shippingCompanyNo"></form:options>
									</form:select>
								</div>
								<div class="input-group-addon" title="Add Shipping Company"
									id="add-shipName" data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#modal-add-shipComp">
									<i class="fa fa-plus"></i>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label class="required">Select Ship</label>
							<!-- 							<div class="input-group-1"> -->

							<div class="element-wrapper input-group">
								<div class="input-group-addon">
									<i class="fa fa-ship"></i>
								</div>
								<div>
									<form:select class="form-control select2 ship" name="shipId"
										id="shipId" data-placeholder="Select Ship" path=""
										style="width: 100%;">
										<form:option value=""></form:option>
									</form:select>
								</div>
								<div class="input-group-addon" title="Add Ship Name"
									id="add-shipName" data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#modal-add-shipName">
									<i class="fa fa-plus"></i>
								</div>
							</div>

							<!-- 							</div> -->
							<span class="help-block"></span>
						</div>
					</div>
					<input type="hidden" id="saveExistingVoyageNo" value="-1" />
					<div class="col-md-2">
						<div class="form-group">
							<label class="required">Voyage No</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa   fa-arrows-v"></i>
								</div>
								<div class="element-wrapper">
									<input type="text" class="form-control" name="voyageNo" id="voyageNo" />
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group">
							<label class="required">Continent</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-globe"></i>
								</div>
								<div class="element-wrapper">
									<select name="continents" id="continents"
										data-placeholder="Select Continent"
										class="form-control required select2" id="continent"
										multiple="multiple">

									</select>
								</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
				<div class="row">

					<div class="col-md-2">
						<div class="form-group">
							<label>Deck Height</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa   fa-arrows-v"></i>
								</div>
								<div class="element-wrapper">
									<input type="text" class="form-control" name="deckHeight" />
								</div>
								<div class="input-group-addon">CM</div>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
				</div>
				<div id="shipment-schedule-wrapper">
					<fieldset>
						<legend>
							Loading Port<small class="ml-5 pull-right"
								style="font-size: 15px">* Enter schedules in order wise</small>
						</legend>
						<!-- /.box-header -->
						<div id="clone-container-location" class="loading-port">
							<div class="clone-container-location-toclone">
								<div class="row">
									<input type="hidden" name="schedule.portFlag" value="loading">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Port name</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa  fa-anchor"></i>
												</div>
												<div class="element-wrapper">
													<select
														class="form-control required select2 port duplicate-country-port"
														name="schedule.portName" data-placeholder="Select Port"
														style="width: 100%;">
														<option value=""></option>
													</select>
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">ETD</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</div>
												<div class="element-wrapper">
													<input name="schedule.date" type="text"
														class="form-control required pull-right schedule-date datepicker"
														placeholder="dd-mm-yyyy" readonly="readonly" />
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>S/O Cut Date</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</div>
												<div class="element-wrapper">
													<input name="schedule.soCutDate" type="text"
														class="form-control pull-right soCutDate datepicker"
														placeholder="dd-mm-yyyy" readonly="readonly" />
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Sub Vessel Name</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-get-pocket"></i>
												</div>
												<div class="element-wrapper">
													<input type="text" name="schedule.subVessel"
														class="form-control pull-right" />
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-2" style="margin-top: 23px;">
										<button type="button" class="btn btn-success btn-clone">
											<i class="fa fa-fw fa-plus"></i>Add
										</button>
										<button class="btn btn-danger btn-delete">
											<i class="fa fa-fw fa-remove"></i>Delete
										</button>
									</div>
									<%-- 									</c:forEach> --%>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend> Destination Port </legend>
						<!-- /.box-header -->
						<div id="clone-container-location" class="destination-port">
							<div class="clone-container-location-toclone">
								<div class="row">
									<input type="hidden" name="schedule.portFlag"
										value="destination">

									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Port name</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa  fa-anchor"></i>
												</div>
												<div class="element-wrapper">
													<select
														class="form-control required select2 port duplicate-country-port"
														name="schedule.portName" data-placeholder="Select Port"
														style="width: 100%;">
														<option value=""></option>
													</select>
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">ETA</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</div>
												<div class="element-wrapper">
													<input name="schedule.date" type="text"
														class="form-control required pull-right schedule-date datepicker valid-schedule-date"
														placeholder="dd-mm-yyyy" readonly="readonly" />
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Sub Vessel Name</label>
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-get-pocket"></i>
												</div>
												<div class="element-wrapper">
													<input type="text" name="schedule.subVessel"
														class="form-control pull-right" />
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-2" style="margin-top: 23px;">
										<button type="button" class="btn btn-success btn-clone">
											<i class="fa fa-fw fa-plus"></i>Add
										</button>
										<button class="btn btn-danger btn-delete">
											<i class="fa fa-fw fa-remove"></i>Delete
										</button>
									</div>
									<%-- 									</c:forEach> --%>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
			</div>
			<div class="box-footer">
				<div class="pull-right">
					<button type="submit" id="btn-save" class="btn btn-primary"
						name="action" value="save">
						<i class="fa fa-save mr-5"></i>Save
					</button>
					<button type="submit" id="btn-save" class="btn btn-primary"
						name="action" value="continue">
						<i class="fa fa-save mr-5"></i>Save & Continue
					</button>
					<button type="reset" class="btn btn-primary"
						onclick="location.reload();">
						<i class="fa fa-repeat mr-5"></i>Reset
					</button>
				</div>
			</div>
		</div>
	</section>
</form:form>

<div class="modal fade" id="modal-add-shipName">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title">Add ShipName</h3>
			</div>
			<div class="modal-body">
				<form action="#" id="formAddShipName">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="required">Ship Name</label>
									<div class="element-wrapper">
										<input type="text" id="name" name="name" class="form-control"
											placeholder="Ship Name" required="required">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="add-ship" class="btn btn-primary">
					<em class="fa fa-fw fa-save"></em> Add
				</button>
				<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
					<em class="fa fa-fw fa-close"></em> Close
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="modal-add-shipComp">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title">Add Shipping Company</h3>
			</div>
			<div class="modal-body">
				<form action="#" id="formAddShipCompany">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="required">Shipping Company</label>
									<div class="element-wrapper">
										<input type="text" id="shippingName" name="name"
											class="form-control" placeholder="Ship Name"
											required="required">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label>Address</label>
									<div class="element-wrapper">
										<input type="text" id="shipCompAddr" name="shipCompAddr"
											class="form-control" placeholder="Address">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label>Email</label>
									<div class="element-wrapper">
										<input type="text" id="shipCompMail" name="shipCompMail"
											class="form-control" placeholder="Email">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label>Mobile Number</label> <input name="mobileNo"
										id="mobileNo" type="text"
										class="form-control consignee-data phone" /> <span
										class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="add-shipComp" class="btn btn-primary">
					<em class="fa fa-fw fa-save"></em> Add
				</button>
				<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
					<em class="fa fa-fw fa-close"></em> Close
				</button>
			</div>
		</div>
	</div>
</div>