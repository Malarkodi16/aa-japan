<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock Entry</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span>Stock Management</span></li>
		<li class="active">Stock Entry</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>

	<!-- Model -->
	<div class="modal fade" id="modal-stock-history">
		<div class="modal-dialog" style="width: 60%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Edit History</h4>
				</div>
				<div class="modal-body" id="stockHistoryBody">
					<div class="container-fluid">
						<div class="row form-group">
							<div class="col-md-1 form-inline">
								<div class="form-group">
									<label></label> <select id="table-filter-editHistory-length"
										class="form-control">
										<option value="10">10</option>
										<option value="25" selected="selected">25</option>
										<option value="100">100</option>
										<option value="1000">1000</option>
									</select>
								</div>
							</div>
							<div class="col-md-4 ml-10">
								<div class="has-feedback">
									<label></label> <input type="text"
										id="table-filter-editHistory-search" class="form-control"
										placeholder="Search by keyword" autocomplete="off"> <span
										class="glyphicon glyphicon-search form-control-feedback"></span>
								</div>
							</div>
						</div>
						<div class="table-responsive">
							<table id="table-edit-history"
								class="table table-bordered table-striped"
								style="width: 100%; overflow: scroll;">
								<thead>
									<tr>
										<th data-index="0">Column Name</th>
										<th data-index="1">Original Value</th>
										<th data-index="2">New Value</th>
										<th data-index="4">Edited Date</th>
										<th data-index="5">Edited By</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<form:form method="POST" id="stockEntryForm"
		action="${contextPath}/stock/stock-entry" modelAttribute="stockForm"
		enctype="multipart/form-data">
		<div class="box box-solid">
			<div class="box-body">
				<div class="container-fluid">
					<div class="row mt-5">
						<c:if test="${editFlag != 1}">
							<div class="col-md-3">
								<select name="stockNo" id="stockNo" class="form-control stockNo"
									data-placeholder="Search by Stock No. or Chassis No."><option
										value=""></option></select> <span class="help-block"></span>
							</div>
							<div class="col-md-1">
								<button type="button" class="btn btn-primary pull-left"
									style="width: 100px" id="btn-search-stock">Edit</button>
							</div>
						</c:if>

						<div class="col-md-2"></div>
						<c:if test="${editFlag == 1}">
							<div class="col-md-3"></div>
						</c:if>
						<div class="col-md-5 form-inline">
							<div class="radio mr-5">
								<div class="element-wrapper">
									<label> <form:radiobutton class="minimal"
											path="stock.account" value="1" checked="checked" />&nbsp;&nbsp;AAJ
									</label>
								</div>
							</div>
							<div class="radio ml-5">
								<div class="element-wrapper">
									<label> <form:radiobutton class="minimal"
											path="stock.account" value="2" />&nbsp;&nbsp;SOMO
									</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-md-1">
							<button type="button" class="btn btn-primary pull-left"
								id="btn-stock-history" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-stock-history">Stock Edit History</button>
						</div>
					</div>
				</div>
				<!-- form start -->
				<div class="container-fluid">
					<input type="hidden" id="attaachment_directory"
						name="attaachment_directory" value=""> <input
						type="hidden" id="saveExistingChassisNo" value="-1" />
					<form:hidden path="stock.id" id="id" />
					<form:hidden path="stock.stockNo" id="_stockNo" value="" />
					<form:hidden path="stock.status"
						data-value="${stockForm.stock.status}" id="status" />
					<div class="row">
						<div class="col-md-4">
							<div class="form-group" style="margin-top: 15px">
								<div class="row">
									<div class="col-md-6">
										<div class="radio">
											<div class="element-wrapper">
												<label> <form:radiobutton class="minimal"
														name="stock.isBidding" id="bid" path="stock.isBidding"
														value="1" data-value="${stockForm.stock.isBidding}" />&nbsp;&nbsp;Bidding
												</label>
											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="radio">
											<div class="element-wrapper">
												<label> <form:radiobutton class="minimal"
														name="stock.isBidding" id="stk" path="stock.isBidding"
														value="0" data-value="${stockForm.stock.isBidding}" />&nbsp;&nbsp;Stock
												</label>
											</div>
										</div>
									</div>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div id="ifBidding" class="show">
							<div class="col-md-4">
								<div class="form-group">
									<label>Sales Staff</label>
									<div class="element-wrapper">
										<form:select class="form-control select2"
											data-placeholder="Sales Staff" id="select_sales_staff"
											path="stock.reservedInfo.salesPersonId"
											data-value="${stockForm.stock.reservedInfo.salesPersonId}"
											style="width: 100%;">
											<option value=""></option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label>Customer</label>
									<div class="element-wrapper">
										<form:select class="form-control" id="custId"
											path="stock.reservedInfo.customerId" style="width: 100%;"
											data-placeholder="Search by Customer ID, Name, Email"
											data-value="${stockForm.stock.reservedInfo.customerId}">
											<option value=""></option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
					<fieldset>
						<legend>Purchase Info</legend>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Purchase Type</label>
									<div class="element-wrapper">
										<c:if
											test="${empty stockForm.stock.status||stockForm.stock.status==0}">
											<form:select path="stock.purchaseInfo.type" id="purchaseType"
												name="supplierType" class="form-control "
												style="width: 100%;" tabindex="0"
												data-value="${stockForm.stock.purchaseInfo.type}">
												<form:option value="">Select Purchase Type</form:option>
												<form:option data-type="auction" value="auction">Auction</form:option>
												<form:option data-type="supplier" value="local">Local</form:option>
												<form:option data-type="supplier" value="overseas">Overseas</form:option>
											</form:select>
										</c:if>
										<c:if
											test="${not empty stockForm.stock.status&&stockForm.stock.status!=0}">
											<form:select path="stock.purchaseInfo.type" id="purchaseType"
												name="supplierType"
												class="form-control t${stockForm.stock.status }"
												style="width: 100%;" tabindex="0"
												data-value="${stockForm.stock.purchaseInfo.type}"
												disabled="true">
												<form:option value="">Select Purchase Type</form:option>
												<form:option data-type="auction" value="auction">Auction</form:option>
												<form:option data-type="supplier" value="local">Local</form:option>
												<form:option data-type="supplier" value="overseas">Overseas</form:option>
											</form:select>
										</c:if>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Purchase Date</label>
									<div class="element-wrapper">
										<div class="input-group">
											<div class="input-group-addon">
												<em class="fa fa-calendar"></em>
											</div>
											<c:if
												test="${empty stockForm.stock.status||stockForm.stock.status==0}">
												<form:input path="stock.purchaseInfo.date" type="text"
													tabindex="0"
													class="form-control pull-right datepicker default-today"
													placeholder="dd-mm-yyyy"
													data-value="${stock.purchaseInfo.date}" />
											</c:if>
											<c:if
												test="${not empty stockForm.stock.status&&stockForm.stock.status!=0}">
												<form:input path="stock.purchaseInfo.date" type="text"
													tabindex="0"
													class="form-control pull-right datepicker default-today"
													placeholder="dd-mm-yyyy"
													data-value="${stock.purchaseInfo.date}" disabled="true" />
											</c:if>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Supplier/Auction</label>
									<div class="element-wrapper">
										<form:select path="stock.purchaseInfo.supplier" tabindex="0"
											id="purchasedSupplier"
											class="form-control select2  ${(empty stockForm.stock.status||stockForm.stock.status==0)?'':'select2-select readonly'}"
											style="width: 100%;" disabled="true"
											data-placeholder="Select Supplier"
											data-value="${stockForm.stock.purchaseInfo.supplier}">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div id="auctionFields" style="display: none;">
								<div class="col-md-2">
									<div class="form-group">
										<label class="required" for="purchasedAuctionHouse">Auction
											House</label>
										<div class="element-wrapper">
											<form:select
												path="stock.purchaseInfo.auctionInfo.auctionHouse"
												id="purchasedAuctionHouse"
												class="form-control ${(empty stockForm.stock.status||stockForm.stock.status==0)?'':'select2-select readonly'}"
												style="width: 100%;" data-placeholder="Select Auction House"
												tabindex="0"
												data-value="${stockForm.stock.purchaseInfo.auctionInfo.auctionHouse}">
												<option value=""></option>
											</form:select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label for="purchaseInfo.auctionInfo.posNo">POS No.</label>
										<div class="element-wrapper">
											<form:select path="stock.purchaseInfo.auctionInfo.posNo"
												id="purchaseInfoPos"
												class="form-control ${(empty stockForm.stock.status||stockForm.stock.status==0)?'':'select2-select readonly'}"
												style="width: 100%;" tabindex="0"
												data-value="${stockForm.stock.purchaseInfo.auctionInfo.posNo}">
												<form:option value=""></form:option>
											</form:select>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<label class="required" for="purchaseInfo.auctionInfo.lotNo">Shuppin
											No.</label>
										<div class="element-wrapper">
											<c:if
												test="${empty stockForm.stock.status||stockForm.stock.status==0}">
												<input name="stock.purchaseInfo.auctionInfo.lotNo"
													tabindex="0" id="stock.purchaseInfo.auctionInfo.lotNo"
													type="text" class="form-control autonumber"
													style="width: 100%;"
													data-a-value="${stockForm.stock.purchaseInfo.auctionInfo.lotNo}"
													value="${stockForm.stock.purchaseInfo.auctionInfo.lotNo}"
													data-a-sep="" data-m-dec="0" />
											</c:if>
											<c:if
												test="${not empty stockForm.stock.status&&stockForm.stock.status!=0}">
												<input name="stock.purchaseInfo.auctionInfo.lotNo"
													tabindex="0" id="stock.purchaseInfo.auctionInfo.lotNo"
													type="text" class="form-control autonumber"
													style="width: 100%;"
													data-a-value="${stockForm.stock.purchaseInfo.auctionInfo.lotNo}"
													value="${stockForm.stock.purchaseInfo.auctionInfo.lotNo}"
													data-a-sep="" data-m-dec="0" disabled="disabled" />
											</c:if>

										</div>
										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset id="transportInfo">
						<legend>Transport Info</legend>
						<div class="row">
							<div class="col-md-5 form-inline">
								<div class="radio mr-5">
									<div class="element-wrapper">
										<label> <form:radiobutton class="minimal"
												path="stock.isMovable" value="0" checked="checked"
												tabindex="0" />&nbsp;&nbsp;Movable
										</label>
									</div>
								</div>
								<div class="radio ml-5">
									<div class="element-wrapper">
										<label> <form:radiobutton class="minimal"
												path="stock.isMovable" value="1" tabindex="0" />&nbsp;&nbsp;Immovable
										</label>
									</div>
								</div>
							</div>
						</div>
						<div class="row mt-5">
							<div class="col-md-3">
								<div class="form-group">
									<label>Pickup Location</label>
									<div class="element-wrapper">
										<select name="stock.transportInfo.pickupLocation"
											id="transportPickupLoc"
											class="form-control select2 with-others locations chargeSelector"
											<%-- ${fn:length(stockForm.stock.id)>0?'disabled':''} --%>
											data-value="${stockForm.stock.transportInfo.pickupLocation}"
											data-placeholder="Select Pickup Location" tabindex="0">
											<option value=""></option>
										</select>
									</div>
									<div class="others-input-container hidden">
										<div class="element-wrapper">
											<form:textarea class="form-control others-input" rows="2"
												tabindex="0" placeholder="Enter.."
												path="stock.transportInfo.pickupLocationCustom"></form:textarea>
										</div>
										<a class="show-hand-cursor show-dropdown">show dropdown</a>
									</div>
								</div>
							</div>
							<!-- 
							<div class="col-md-3">
								<div class="form-group">
									<label>Drop Location</label>
									<div class="element-wrapper">
										<select name="stock.transportInfo.dropLocation"
											class="form-control select2 with-others locations chargeSelector"
											id="transportDropupLoc"
											data-placeholder="Select Drop Location"
											${fn:length(stockForm.stock.id)>0?'disabled':''}
											data-value="${stockForm.stock.transportInfo.dropLocation}"
											tabindex="0">
											<option value=""></option>
										</select>
									</div>
									<div class="others-input-container hidden">
										<div class="element-wrapper">
											<form:textarea class="form-control others-input" rows="2"
												placeholder="Enter.."
												path="stock.transportInfo.dropLocationCustom" tabindex="0"></form:textarea>
										</div>
										<a class="show-hand-cursor show-dropdown">show dropdown</a>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Transporter</label>
									<div class="element-wrapper">
										<select name="stock.transportInfo.transporter"
											id="transporter" class="form-control"
											data-placeholder="Select Transporter"
											${fn:length(stockForm.stock.id)>0?'disabled':''}
											data-value="${stockForm.stock.transportInfo.transporter}"
											tabindex="0">
											<option value=""></option>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Charge</label>
									<div class="element-wrapper">
										<form:input path="stock.transportInfo.charge" id="charge"
											type="text" class="form-control autonumber" data-a-sign="¥ "
											data-m-dec="0" tabindex="0" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						-->
						</div>
					</fieldset>
					<fieldset>
						<legend>Basic Info</legend>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required" for="chassisNo">Chassis No.</label>
									<div class="element-wrapper">
										<form:input path="stock.chassisNo" id="chassisNo" tabindex="0"
											type="text" class="form-control" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label for="firstRegDate">First Reg. Year</label>
									<div class="element-wrapper">
										<form:input path="stock.sFirstRegDate" id="firstRegDate"
											tabindex="0" type="text"
											class="form-control year-month-picker" placeholder="YYYY/MM" />
									</div>
									<span class="help-block"></span>
									<!-- /.input group -->
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label for="">Model Type</label>
									<div class="element-wrapper">
										<form:input path="stock.modelType" id="modelType" type="text"
											class="form-control" tabindex="0" />
									</div>
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required" for="maker">Maker</label>
									<div class="element-wrapper">
										<form:select path="stock.maker" id="maker"
											class="form-control select2 chargeSelector"
											style="width: 100%;" data-placeholder="Select Maker"
											data-value="${stockForm.stock.maker}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required" for="model">Model</label>
									<div class="element-wrapper">
										<form:select path="stock.model" id="model"
											class="form-control select2 chargeSelector"
											style="width: 100%;" data-placeholder="Select Model"
											data-value="${stockForm.stock.modelId}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<%-- <div class="col-md-2">
								<div class="form-group">
									<label>Category</label>
									
									<form:hidden path="stock.subcategory" id="subcategory"
										class="form-control" style="width: 100%;" readonly="true" />
									<!-- <div class="element-wrapper">
										<input type="text" id="categoryAndSubcategory"
											class="form-control" value="" readonly="readonly"
											tabindex="0" />
									</div> -->
									<span class="help-block"></span>
								</div>
							</div> --%>


							<div class="col-md-2">
								<div class="form-group">
									<label for="subModel">Type</label>
									<div class="element-wrapper">
										<form:input type="hidden" path="stock.category" id="category"
											class="form-control" style="width: 100%;"
											data-value="${stockForm.stock.category}" readonly="true" />
										<form:select path="stock.subcategory" id="subcategory"
											class="form-control select2" style="width: 100%;"
											data-placeholder="Select Sub Category"
											data-value="${stockForm.stock.subcategory}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-8">
								<div class="row">

									<div class="col-md-2">
										<div class="form-group">
											<label class="required" for="transmission">Transmission</label>
											<div class="element-wrapper">
												<form:select path="stock.transmission" id="transmission"
													class="form-control" style="width: 100%;"
													data-placeholder="Select Transmission"
													data-value="${stockForm.stock.transmission}" tabindex="0">
													<option value=""></option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>

									<div class="col-md-2">
										<div class="form-group">
											<label>Manual types</label>
											<div class="element-wrapper">
												<form:select path="stock.manualTypes" id="manualTypes"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Manual Types" tabindex="0">
													<form:option value=""></form:option>
													<form:option value="F4">F4</form:option>
													<form:option value="F5">F5</form:option>
													<form:option value="F6">F6</form:option>
													<form:option value="F7">F7</form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<div class="row">
												<div class="col-md-6">
													<label>No of Door</label>
													<div class="element-wrapper">
														<form:input path="stock.noOfDoors" id="noOfDoors"
															type="text" class="form-control autonumber"
															style="width: 100%;" value="" data-v-min="0"
															data-v-max="10" data-a-sep="" tabindex="0" />
													</div>
													<span class="help-block"></span>
												</div>
												<div class="col-md-6">
													<label>No of Seat</label>
													<div class="element-wrapper">
														<form:input path="stock.noOfSeat" id="noOfSeat"
															type="text" class="form-control" style="width: 100%;"
															data-v-min="0" data-v-max="1000" value="" data-a-sep=""
															tabindex="0" />
													</div>
													<span class="help-block"></span>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Grade</label>
											<div class="element-wrapper">
												<form:input path="stock.grade" type="text"
													class="form-control" tabindex="0" />
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-3">
										<div class="form-group">
											<label class="required" for="fuel">Fuel</label>
											<div class="element-wrapper">
												<form:select path="stock.fuel" id="fuel"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Fuel Type"
													data-value="${stockForm.stock.fuel}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label class="required" for="driven">Driven</label>
											<div class="element-wrapper">
												<form:select path="stock.driven" id="driven"
													class="form-control select2" style="width: 100%;"
													tabindex="0">
													<form:option value=""></form:option>
													<form:option value="Left">Left</form:option>
													<form:option value="Right">Right</form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Mileage</label>
											<div class="element-wrapper">
												<form:input path="stock.mileage" type="text"
													class="form-control autonumber" data-a-sep=","
													data-m-dec="0" tabindex="0" />
											</div>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Color</label>
											<div class="element-wrapper">
												<form:select path="stock.color" id="colors"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Colors"
													data-value="${stockForm.stock.color}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-2">
										<div class="form-group">
											<label for="cc">CC</label>
											<div class="element-wrapper">
												<input name="stock.cc" id="cc" type="text"
													class="form-control autonumber" data-a-sep=","
													data-m-dec="0" value="${stockForm.stock.cc}" tabindex="0" />
											</div>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Unit</label>
											<div class="element-wrapper">
												<form:select path="stock.unit" id="unit"
													class="form-control select2" style="width: 100%;"
													tabindex="0">
													<form:option value=""></form:option>
													<form:option value="cc">CC</form:option>
													<form:option value="kw">KW</form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Recycle</label>
											<div class="element-wrapper">
												<form:select path="stock.recycle"
													data-value="${stockForm.stock.recycle}" id="recycle"
													class="form-control select2" style="width: 100%;"
													tabindex="0">
													<form:option value=""></form:option>
													<form:option value="yes" label="Yes" selected="selected"></form:option>
													<form:option value="no" label="No"></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Number Plate</label>
											<div class="element-wrapper">
												<form:select path="stock.numberPlate"
													data-value="${stockForm.stock.numberPlate}"
													id="numberPlate" class="form-control select2"
													style="width: 100%;" tabindex="0">
													<form:option value=""></form:option>
													<form:option value="yes" label="Yes"></form:option>
													<form:option value="no" label="No" selected="selected"></form:option>
												</form:select>
											</div>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Old NP</label>
											<div class="element-wrapper">
												<form:input path="stock.oldNumberPlate" id="oldNp"
													type="text" class="form-control" tabindex="0" />
											</div>
										</div>
									</div>

								</div>
								<div class="row">
									<div class="col-md-3">
										<div class="form-group">
											<label>Origin</label>
											<div class="element-wrapper">
												<form:select id="orgin" path="stock.orgin"
													data-value="${stockForm.stock.orgin}"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Orgin" tabindex="0">
													<option value=""></option>
													<option value="JAPAN">JAPAN</option>
													<option value="FOREIGN COUNTRY">FOREIGN COUNTRY</option>
												</form:select>
											</div>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Shipment Type</label>
											<div class="element-wrapper">
												<form:select id="shipmentType" path="stock.shipmentType"
													class="form-control select2" style="width: 100%;"
													data-value="${stockForm.stock.shipmentType}"
													data-placeholder="Select Shipment Type" tabindex="0">
													<form:option value="">Select Shipment Type</form:option>
													<form:option value="1">RORO</form:option>
													<form:option value="2">CONTAINER</form:option>
												</form:select>
											</div>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label>Forwarder</label>
											<div class="element-wrapper">
												<form:select path="stock.forwarder" id="forwarder"
													class="form-control select2 chargeSelector"
													style="width: 100%;" data-placeholder="Select forwarder"
													data-value="${stockForm.stock.forwarder}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-4">
										<div class="form-group">
											<label>Additional Option Description</label>
											<div class="element-wrapper">
												<form:textarea path="stock.optionDescription" type="text"
													class="form-control" tabindex="0"></form:textarea>
											</div>
										</div>
									</div>
								</div>
								<div class="row hidden" id="extraEquipmentsDiv">
									<div class="col-md-4">
										<div class="form-group">
											<label>Extra Equipments</label>
											<div class="element-wrapper">
												<form:select path="stock.extraEquipments"
													id="extraEquipments"
													data-placeholder="Select Extra Equipments"
													data-value="${stockForm.stock.extraEquipments}"
													style="width: 100%;" class="form-control"
													multiple="multiple" tabindex="0">
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="tyreSizeDiv">
										<div class="form-group">
											<label>Tyre Size</label>
											<div class="element-wrapper">
												<form:select path="stock.tyreSize" id="tyreSize"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Tyre Size"
													data-value="${stockForm.stock.tyreSize}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="craneTypeDiv">
										<div class="form-group">
											<label>Crane Type</label>
											<div class="element-wrapper">
												<form:select path="stock.craneType" id="craneType"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Crane Type"
													data-value="${stockForm.stock.craneType}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="craneCutDiv">
										<div class="form-group">
											<label>Crane Cut</label>
											<div class="element-wrapper">
												<form:select path="stock.craneCut" id="craneCut"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Crane Cut"
													data-value="${stockForm.stock.craneCut}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2 hidden" id="exelDiv">
										<div class="form-group">
											<label>Exel</label>
											<div class="element-wrapper">
												<form:select path="stock.exel" id="exel"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select exel"
													data-value="${stockForm.stock.exel}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group hidden" id="tankKiloLitreDiv">
											<label>Tank KL</label>
											<div class="element-wrapper">
												<form:select path="stock.tankKiloLitre" id="tankKiloLitre"
													class="form-control select2" style="width: 100%;"
													data-placeholder="Select Tank KL"
													data-value="${stockForm.stock.tankKiloLitre}" tabindex="0">
													<form:option value=""></form:option>
												</form:select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<fieldset>
									<legend>Equipment</legend>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="A/C" tabindex="0" /><span
														class="ml-5">A/C</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group element-wrapper">
												<label> <form:checkbox path="stock.equipment"
														class="minimal" value="P/S" tabindex="0" /><span
													class="ml-5">P/S</span>
												</label>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="P/W" tabindex="0" /><span
														class="ml-5">P/W</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="S/R" tabindex="0" /><span
														class="ml-5">S/R</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="A/W" tabindex="0" /><span
														class="ml-5">A/W</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="ABS" tabindex="0" /><span
														class="ml-5">ABS</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="AIR BAG" tabindex="0" /><span
														class="ml-5">AIR BAG</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="4WD" tabindex="0" /><span
														class="ml-5">4WD</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="P/M" tabindex="0" /><span
														class="ml-5">P/M</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="TV" tabindex="0" /><span
														class="ml-5">TV</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="CD" tabindex="0" /><span
														class="ml-5">CD</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="NV" tabindex="0" /><span
														class="ml-5">NV</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="R/S" tabindex="0" /><span
														class="ml-5">R/S</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="F/LAMP" tabindex="0" /><span
														class="ml-5">F/LAMP</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="POWER DOOR" tabindex="0" /><span
														class="ml-5">POWER DOOR</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="SINGLE POWER DOOR" tabindex="0" /><span
														class="ml-5">SINGLE POWER DOOR</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="LEATHER SEAT" tabindex="0" /><span
														class="ml-5">LEATHER SEAT</span>
													</label>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<div class="element-wrapper">
													<label> <form:checkbox path="stock.equipment"
															class="minimal" value="HALF LEATHER SEAT" tabindex="0" /><span
														class="ml-5">HALF LEATHER SEAT</span>
													</label>
												</div>
											</div>
										</div>
									</div>
								</fieldset>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend>Extra Accessories</legend>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group element-wrapper">
									<label><form:checkbox path="stock.extraAccessories"
											class="minimal" value="SPARE KEY" tabindex="0" /><span
										class="ml-5">SPARE KEY</span> </label>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group element-wrapper">
									<label> <form:checkbox path="stock.extraAccessories"
											class="minimal" value="ELECTRIC CODE" tabindex="0" /><span
										class="ml-5">CHARGING CABLE</span>
									</label>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group element-wrapper">
									<label> <form:checkbox path="stock.extraAccessories"
											class="minimal" value="SD CARD" tabindex="0" /><span
										class="ml-5">SD CARD</span>
									</label>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group element-wrapper">
									<label> <form:checkbox path="stock.extraAccessories"
											class="minimal" value="SHIFT KNOB" tabindex="0" /><span
										class="ml-5">SHIFT KNOB</span>
									</label>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group element-wrapper">
									<label> <form:checkbox path="stock.extraAccessories"
											class="minimal" value="MANUALS" tabindex="0" /><span
										class="ml-5">MANUALS</span>
									</label>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend>Remarks</legend>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label>Auction Remark</label>
									<div class="element-wrapper">
										<form:textarea path="stock.auctionRemarks"
											class="form-control" tabindex="0"></form:textarea>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label>Remark</label>
									<div class="element-wrapper">
										<form:textarea path="stock.remarks" class="form-control"
											tabindex="0"></form:textarea>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label>Exterior Auction Grade</label>
									<div class="element-wrapper">
										<form:select path="stock.auctionGradeExt" id="auctionGradeExt"
											class="form-control select2" style="width: 100%;"
											data-placeholder="Select Grade"
											data-value="${stockForm.stock.auctionGradeExt}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Interior Auction Grade</label>
									<div class="element-wrapper">
										<form:select path="stock.auctionGrade" id="auctionGrade"
											class="form-control select2 " style="width: 100%;"
											data-placeholder="Select Grade"
											data-value="${stockForm.stock.auctionGrade}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Destination Country</label>
									<div class="element-wrapper">
										<form:select id="destinationCountry"
											path="stock.destinationCountry" class="form-control select2"
											style="width: 100%;"
											data-value="${stockForm.stock.destinationCountry}"
											data-placeholder="Select Destination Country" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Destination Port</label>
									<div class="element-wrapper">
										<form:select id="destinationPort"
											data-placeholder="Select Destination Port"
											path="stock.destinationPort" class="form-control select2"
											style="width: 100%;"
											data-value="${stockForm.stock.destinationPort}" tabindex="0">
											<form:option value=""></form:option>
										</form:select>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset id="purchaseInfoContainer">
						<legend>Purchase Info</legend>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label>Purchase Price</label>
									<div class="element-wrapper">
										<form:input id="purchaseCost" type="text"
											path="purchaseInvoice.purchaseCost"
											class="form-control purchase-info-calc autonumber" value=""
											data-a-sign="¥ " data-m-dec="0" tabindex="0"
											placeholder="¥ 0" />
									</div>
									<form:input path="purchaseInvoice.purchaseCostTax"
										id="purchaseCostTax" type="text"
										class="form-control hide mt-5 purchase-info-calc autonumber"
										data-v-max="100" style="width:100px" data-v-min="0"
										data-a-sign="%" data-m-dec="0" data-p-sign="s" tabindex="-1"></form:input>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Commission Amount</label>
									<div class="element-wrapper">
										<form:input id="commisionAmount"
											path="purchaseInvoice.commision" data-validation="number"
											class="form-control purchase-info-calc autonumber"
											data-a-sign="¥ " data-m-dec="0" tabindex="0" />
									</div>
									<form:input path="purchaseInvoice.commisionTax" type="text"
										data-validation="number" id="commisionTax"
										class="form-control hide purchase-info-calc mt-5 autonumber"
										data-v-max="100" data-v-min="0" data-a-sign="%" data-m-dec="0"
										style="width:100px" data-p-sign="s" tabindex="-1"></form:input>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Recycle Amount</label>
									<div class="element-wrapper">
										<form:input id="recycleAmount" path="purchaseInvoice.recycle"
											data-validation="number"
											class="form-control purchase-info-calc autonumber" value=""
											data-a-sign="¥ " data-m-dec="0" tabindex="0"
											placeholder="¥ 0" />
									</div>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Other Charges</label>
									<div class="element-wrapper">
										<form:input id="otherCharges"
											path="purchaseInvoice.otherCharges"
											class="form-control purchase-info-calc autonumber"
											data-a-sign="¥ " data-m-dec="0" value="" tabindex="0" />
									</div>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Road Tax</label>
									<div class="element-wrapper">
										<form:input id="roadTax" path="purchaseInvoice.roadTax"
											data-a-sign="¥ " data-m-dec="0" value=""
											class="form-control purchase-info-calc autonumber"
											tabindex="0" />
									</div>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Total</label>
									<div class="element-wrapper">
										<input id="total" class="form-control autonumber"
											data-a-sign="¥ " data-m-dec="0"
											value="${stockForm.purchaseInvoice.total }"
											disabled="disabled" tabindex="0" />
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label>Total Tax </label>
									<div class="element-wrapper">
										<input type="text" id="totalTaxAutonumber"
											class="form-control autonumber" data-a-sign="¥ "
											data-m-dec="0" disabled="disabled"
											value="${stockForm.purchaseInvoice.totalTax }" tabindex="0" />
									</div>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label>Total(Tax Included)</label>
									<div class="element-wrapper">
										<input type="text" id="totalTaxIncludedAutonumber"
											class="form-control autonumber" data-a-sign="¥ "
											data-m-dec="0" disabled="disabled"
											value="${stockForm.purchaseInvoice.totalTaxIncluded }"
											tabindex="0" />
									</div>
								</div>
							</div>
						</div>
					</fieldset>

					<div id="attachment-block">
						<input type="hidden" id="attachment-size"
							value="${fn:length(stockForm.stock.attachments)>0?fn:length(stockForm.stock.attachments):0}" />
						<fieldset>
							<legend>Photos</legend>
							<p id="attachments_form">
								<label>Files</label> <span class="attachments_form"> <span
									class="attachments_fields"> <c:forEach
											items="${stockForm.stock.attachments}" var="attachment"
											varStatus="i">
											<c:if test="${attachment.subDirectory =='images'}">
												<span id="attachments_${i.index}" class="row"><span
													class="col-md-12"><i
														class="fa fa-fw fa-file-photo-o"
														style="margin-right: 10px;"></i><span
														style="margin-right: 10px;">${attachment.filename }</span>
														<span class="file_upload_status"
														style="margin-right: 10px;"><a
															href="${contextPath}/downloadFile/${attachment.diskFilename }?path=${attachment.diskDirectory }/${attachment.subDirectory }&from=upload"><i
																class="fa fa-download"
																style="margin-right: 10px; color: green;"></i></a></span><span
														class="file-remove" data-upload-directory="upload"
														data-filename="${attachment.diskFilename }"
														data-directory="${attachment.diskDirectory }"
														data-subdirectory="${attachment.subDirectory }"
														data-directory="upload" data-id="${attachment.id}"><i
															class="fa fa-fw fa-trash-o" style="margin-right: 10px;"></i></span></span></span>
											</c:if>
										</c:forEach>
								</span> <span class="add_attachment" style=""> <input
										type="file" accept="image/x-png,image/gif,image/jpeg"
										class="file_selector filedrop" multiple="multiple"
										onchange="addInputFiles(this);" data-subdirectory="images"
										data-description="false"
										data-description-placeholder="Optional description"
										tabindex="0" />
								</span>
								</span>
							</p>
						</fieldset>
						<fieldset>
							<legend>Document</legend>
							<p id="attachments_form_2">
								<label>Files</label> <span class="attachments_form"> <span
									class="attachments_fields"> <c:forEach
											items="${stockForm.stock.attachments}" var="attachment"
											varStatus="i">
											<c:if test="${attachment.subDirectory =='document'}">
												<span id="attachments_${i.index}" class="row"><span
													class="col-md-12"><i
														class="fa fa-fw fa-file-photo-o"
														style="margin-right: 10px;"></i><span
														style="margin-right: 10px;">${attachment.filename }</span>
														<span style="margin-right: 10px;">[${attachment.description }]</span>
														<span class="file_upload_status"
														style="margin-right: 10px;"><a
															href="${contextPath}/downloadFile/${attachment.diskFilename }?path=${attachment.diskDirectory }/${attachment.subDirectory }&from=upload"><i
																class="fa fa-download"
																style="margin-right: 10px; color: green;"></i></a></span><span
														class="file-remove" data-upload-directory="upload"
														data-filename="${attachment.diskFilename }"
														data-directory="${attachment.diskDirectory }"
														data-subdirectory="${attachment.subDirectory }"
														data-directory="upload" data-id="${attachment.id}"><i
															class="fa fa-fw fa-trash-o" style="margin-right: 10px;"></i></span></span></span>
											</c:if>
										</c:forEach>
								</span> <span class="add_attachment" style=""> <input
										type="file" class="file_selector filedrop" multiple="multiple"
										onchange="addInputFiles(this);" data-subdirectory="document"
										data-description="true"
										data-description-placeholder="Description" tabindex="0" />
								</span>
								</span>
							</p>
						</fieldset>
					</div>
					<div class="box-footer">
						<!-- <div class="row sticky "> -->
							<div class="col-md-12">
								<input type="hidden" name="returnPath" value="${returnPath}" />
								<c:if test="${editFlag != 1}">
									<div class="btn-group pull-right">
										<button type="submit" id="save" name="action" value="save"
											class="btn btn-primary" tabindex="0">Save</button>
										<button type="submit" id="saveAndContinue" name="action"
											value="continue" class="btn btn-primary" tabindex="0">Save
											&amp; Continue</button>
										<button type="reset" class="btn btn-primary" value="Reset"
											onclick="location.reload();" tabindex="0">Reset</button>
									</div>
								</c:if>
								<c:if test="${editFlag == 1}">
									<div class="pull-right">
										<button type="submit" id="save" name="action" value="save"
											class="btn btn-primary" style="width: 100px" tabindex="0">Save</button>
										<a href="${returnPath}" type="button" class="btn btn-primary"
											style="width: 100px">Cancel</a>
									</div>
								</c:if>
							</div>
						<!-- </div> -->
					</div>
				</div>
			</div>
		</div>
	</form:form>
	<!-- /.form:form -->
</section>
