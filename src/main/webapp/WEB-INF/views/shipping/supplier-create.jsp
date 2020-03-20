<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Supplier Creation</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Supplier Create</li>
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
			<form method="POST" id="supplierForm"
				action="${contextPath}/a/supplier/create">
				<input type="hidden" id="supplierCode" name="supplierCode"
					value="${supplierCode}">
				<div class="box-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Supplier Type</label>
									<div class="element-wrapper">
										<select name="type" id="modelSupplierType"
											class="form-control required" style="width: 100%;">
											<option value="">Select Type</option>
											<option value="auction">Auction</option>
											<option value="supplier">Supplier</option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Company/Supplier</label>
									<div class="element-wrapper">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-fw fa-building"></i>
											</div>
											<input type="text" id="company" name="company"
												class="form-control required" />
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>

							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Maximum Due Days</label>
									<div class="element-wrapper">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-calendar"></i>
											</div>
											<input type="text" name="maxDueDays"
												class="form-control required autonumber" />
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Maximum Credit Amount</label>
									<div class="element-wrapper">
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-yen"></i>
											</div>
											<input type="text" name="maxCreditAmount"
												id="maxCreditAmount" data-validation="number"
												class="form-control required autonumber" data-m-dec="0"
												data-a-sign="¥ " />
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div id="clone-container-location">
							<div class="clone-container-location-toclone">
								<fieldset class="auction-info hidden">
									<legend>Auction Info</legend>
									<div id="auctionHouseContainer">
										<div class="row">
											<div class="col-md-2">
												<div class="form-group">
													<label class="required">Auction House</label>
													<div class="element-wrapper">
														<div class="input-group">
															<div class="input-group-addon">
																<i class="fa fa-fw fa-home"></i>
															</div>
															<input type="text"
																name="supplierLocations[0].auctionHouse" id="auctionHouse"
																class="form-control required duplicate-supplier-check auctionHouse" />
															<input type="hidden" name="supplierLocations[0].id" /> <input
																type="hidden" name="supplierLocations[0].deleteStatus" />
														</div>
													</div>
													<span class="help-block"></span>
												</div>
											</div>
											<div class="col-md-4">
												<div class="row">
													<div class="col-md-8">
														<div class="form-group">
															<label class="required">POS No</label><small class="ml-5">(Multiple
																POS number can add)</small>
															<div class="element-wrapper">
																<div class="input-group">
																	<div class="input-group-addon">
																		<i class="fa fa-fw fa-id-card"></i>
																	</div>
																	<select name="supplierLocations[0].posNos" id="posNo"
																		multiple="multiple" data-placeholder="Enter Pos Nos"
																		class="form-control select2-tag required posNo"
																		style="width: 100%;">

																	</select>
																</div>
																<!-- <input name="supplierLocations[0].posNos"
																	data-placeholder="Enter Pos Nos"
																	class="form-control select2-tag required"
																	style="width: 100%;" /> -->
															</div>
															<span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-4"></div>
												</div>
											</div>
										</div>
									</div>
								</fieldset>
								<fieldset>
									<legend>Contact Info</legend>
									<div class="row">
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Address</label>
												<div class="element-wrapper">
													<div class="input-group">
														<div class="input-group-addon">
															<i class="fa fa-fw fa-map-marker"></i>
														</div>
														<input type="text" name="supplierLocations[0].address"
															class="form-control required" />
													</div>
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label>Email</label>
												<div class="element-wrapper">
													<div class="input-group">
														<div class="input-group-addon">
															<i class="fa fa-fw fa-envelope"></i>
														</div>
														<input type="text" name="supplierLocations[0].email"
															class="form-control email" />
													</div>
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label>Phone</label>
												<div class="element-wrapper">
													<div class="input-group">
														<div class="input-group-addon">
															<i class="fa fa-fw fa-phone"></i>
														</div>
														<input type="text" name="supplierLocations[0].phone"
															class="form-control phone" />
													</div>
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-2">
											<div class="form-group">
												<label>Fax</label>
												<div class="element-wrapper">
													<div class="input-group">
														<div class="input-group-addon">
															<i class="fa fa-fw fa-fax"></i>
														</div>
														<input type="text" name="supplierLocations[0].fax"
															class="form-control fax" />
													</div>
												</div>
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</fieldset>
								<div class="row clone-btn auction-info">
									<div class="col-md-12">
										<button type="button" class="btn btn-success pull-right clone">
											<i class="fa fa-fw fa-plus"></i>Add More
										</button>
										<button class="btn btn-danger pull-left delete">
											<i class="fa fa-fw fa-remove"></i>Delete
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="box-footer">
					<div class="pull-right">
						<button type="submit" id="btn-save" class="btn btn-primary">
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
