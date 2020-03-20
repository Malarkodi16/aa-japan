<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Inquiry</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Create Inquiry</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<form:form method="POST" id="inquiryForm"
		action="${contextPath}/inquiry/save" modelAttribute="inquiryForm">
		<div class="box box-solid">
			<div class="box-header"></div>
			<div class="box-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label class="required">Customer</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-user-circle-o"></i>
									</div>
									<form:select path="customerCode" id="customerCode"
										class="form-control select2-select">
										<form:option value=""></form:option>
									</form:select>
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label class="required">Mobile</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-phone"></i>
									</div>
									<form:input path="mobile" type="text"
										class="form-control phone" placeholder="Mobile No" />
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label class="required">Customer Name</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-user"></i>
									</div>
									<form:input path="customerName" type="text"
										class="form-control" placeholder="Customer Name" />
								</div>
								<span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label for="skypeId">Skype Id</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-skype"></i>
									</div>
									<form:input path="skypeId" type="text" class="form-control"
										placeholder="Skype Id" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label class="required" for="companyName">Company Name</label>
								<div class="input-group">
									<div class="input-group-addon">
										<i class="fa fa-building"></i>
									</div>
									<form:input path="companyName" type="text" class="form-control"
										placeholder="Company Name" />
								</div>
								<span class="help-block"></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<fieldset id="inquiry-items-wrapper">
								<legend>Inquiry Items</legend>
								<div id="clone-container-inquiry-item">
									<div class="row clone-container-inquiry-item-toclone container-fluid">
										<div class="row">
											<div class="col-md-3">
												<label class="required" for="category">Maker</label>
												<form:select path="inquiryVehicles[0].maker"
													class="form-control required select2-select maker"
													style="width: 100%;" data-placeholder="Select Maker">
													<option value=""></option>
												</form:select>
												<span class="help-block"></span>
											</div>
											<div class="col-md-3">
												<label class="required">Model</label> <select
													name="inquiryVehicles[0].model" id="model"
													class="form-control required model select2-select"
													style="width: 100%;" data-placeholder="Select Model">
													<option value=""></option>
												</select> <span class="help-block"></span>
											</div>
											<!-- <div class="col-md-3">
											<label>Sub Model</label> <select
												name="inquiryVehicles[0].subModel" id="subModel"
												class="form-control subModel select2-select"
												style="width: 100%;" data-placeholder="Select Sub Model">
												<option value=""></option>
											</select> <span class="help-block"></span>
										</div> -->
											<div class="col-md-3">
												<div class="form-group">
													<label for="category">Category</label>
													<form:hidden path="inquiryVehicles[0].category"
														id="category" class="form-control  required category"
														style="width: 100%;" data-placeholder="Select Category"
														readonly="true" />
													<form:hidden path="inquiryVehicles[0].subCategory"
														id="subcategory" class="form-control required subCategory"
														style="width: 100%;"
														data-placeholder="Select Sub Category" readonly="true" />
													<input type="text" id="categoryAndSubcategory"
														class="form-control categoryAndSubcategory" value=""
														readonly="readonly" />
												</div>
											</div>

											<div class="col-md-3">
												<label for="category">Country</label>
												<form:select path="inquiryVehicles[0].country"
													name="inquiryVehicles[0].country"
													data-placeholder="Select Country"
													class="form-control select2-select country"
													style="width: 100%;">
													<form:option value=""></form:option>
												</form:select>
											</div>
										</div>
										<div class="row secnd-row">
											<div class="col-md-3">
												<label for="category">Port</label> <select
													name="inquiryVehicles[0].port" id="port"
													class="form-control port select2-select"
													data-placeholder="Select Port" style="width: 100%;">
													<option value=""></option>
												</select>
											</div>
											<div class="col-md-6"></div>
											<div class="col-md-1 clone-btn">
												<button type="button" id="inquiry-item"
													class="btn btn-success btn-sm clone"
													style="margin-top: 30%;">
													<i class="fa fa-fw fa-plus"></i>Add More
												</button>
											</div>
											<div class="col-md-1 clone-btn">
												<button type="button" id="inquiry-item"
													class="btn btn-danger btn-sm delete"
													style="margin-top: 30%;">
													<i class="fa fa-remove"></i>Delete
												</button>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<div class="btn-group pull-right">
					<button type="submit" id="save" name="action" value="save"
						class="btn btn-primary">Save</button>
					<button type="submit" id="saveAndContinue" name="action"
						value="continue" class="btn btn-primary">Save &amp;
						Continue</button>
					<button type="reset" class="btn btn-primary" value="Reset"
						onclick="location.reload();">Reset</button>
				</div>
				<!-- <button class="btn btn-primary pull-right">
					<i class="fa fa-fw fa-save"></i>Save
				</button> -->
			</div>
		</div>
	</form:form>
</section>
