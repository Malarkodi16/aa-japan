<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Maker</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Maker-Model</span></li>
		<li class="active">Create Maker</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-body">
			<form class="maker/model" id="maker-model">

				<div class="container-fluid" id="maker">
					<fieldset>
						<input type="hidden" id="code" name="code" value="${code}">
						<legend>Maker</legend>
						<div class="form-group">
							<div class="row">
								<div class="col-md-2">
									<label class="required">Maker Name</label>
									<div class="element-wrapper">
										<input type="text" id="makerName" name="name"
											class="form-control">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="container-fluid" id="model">
					<fieldset>
						<legend>Model</legend>
						<div id="model-clone-conatiner">
							<div class="item">
								<div class="row form-group model-row">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Model Name</label>
											<div class="element-wrapper">
												<input type="text" name="modelName"
													class="form-control validation-required model validate-duplicate-modelName">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<!-- <div class="col-md-3 subModel">
										<div class="row">
											<div class="form-group">
												<label>Sub Model</label>
												<div class="element-wrapper">
													<select name="subModelName" multiple="multiple"
														data-placeholder="Enter Sub Model"
														class="form-control select2-tag " style="width: 100%;">
													</select>
												</div>
												<span class="help-block"></span>
											</div>
											<div class="col-md-4"></div>
										</div>
									</div> -->
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Category</label>
											<div class="element-wrapper">
												<select
													class="form-control consignee-data select2-select model validation-required category"
													name="category" data-placeholder="Select Category"
													style="width: 100%">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<!-- <div class="col-md-2">
										<div class="form-group">
											<label class="required">Sub Category</label>
											<div class="element-wrapper">
												<select
													class="form-control  select2-select model subcategory validation-required"
													name="subcategory" data-placeholder="Select Sub Category"
													style="width: 100%">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div> -->
									<div class="col-md-1">
										<div class="form-group">
											<label class="required">M3</label>
											<div class="element-wrapper">
												<input type="text" name="m3"
													class="form-control model autonumber validation-required"
													data-m-dec="0" type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Length</label>
											<div class="element-wrapper">
												<input type="text" name="length"
													class="form-control model autonumber " data-m-dec="0">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Width</label>
											<div class="element-wrapper">
												<input type="text" name="width"
													class="form-control model autonumber " data-m-dec="0"
													data-m-sep="">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Height</label>
											<div class="element-wrapper">
												<input type="text" name="height"
													class="form-control model autonumber" data-m-dec="0"
													data-m-sep="">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<div>
												<button type="button"
													class="btn btn-primary btn-sm add-maker" id="add-maker">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-maker-item"
													id="delete-maker">
													<i class="fa fa-trash"></i>
												</button>
											</div>
										</div>
									</div>
								</div>

								<!-- <div class="row">
									<div class="form-group"></div>
								</div> -->
							</div>
						</div>
						<div id="model-container" class="model-container hidden">
							<div class="item">
								<div class="row form-group model-row">
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Model Name</label>
											<div class="element-wrapper">
												<input type="text" name="modelName"
													class="form-control validation-required model validate-duplicate-modelName">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<!-- <div class="col-md-3 subModel">
										<div class="row">
											<div class="form-group">
												<label>Sub Model</label>
												<div class="element-wrapper">
													<select name="subModelName" multiple="multiple"
														data-placeholder="Enter Sub Model"
														class="form-control model select2-tag"
														style="width: 100%;">
													</select>
												</div>
												<span class="help-block"></span>
											</div>
											<div class="col-md-4"></div>
										</div>
									</div> -->
									<div class="col-md-2">
										<div class="form-group">
											<label class="required">Category</label>
											<div class="element-wrapper">
												<select
													class="form-control consignee-data select2-select validation-required model category"
													name="category" data-placeholder="Select Category"
													style="width: 100%">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<!-- <div class="col-md-2">
										<div class="form-group">
											<label class="required">Sub Category</label>
											<div class="element-wrapper">
												<select
													class="form-control  select2-select model validation-required subcategory"
													name="subcategory" data-placeholder="Select Sub Category"
													style="width: 100%">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div> -->
									<div class="col-md-1">
										<div class="form-group">
											<label class="required">M3</label>
											<div class="element-wrapper">
												<input type="text" name="m3"
													class="form-control model autonumber validation-required"
													data-m-dec="0">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Length</label>
											<div class="element-wrapper">
												<input type="text" name="length"
													class="form-control model autonumber" data-m-dec="0">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Width</label>
											<div class="element-wrapper">
												<input type="text" name="width"
													class="form-control model autonumber" data-m-dec="0">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-1">
										<div class="form-group">
											<label>Height</label>
											<div class="element-wrapper">
												<input type="text" name="height"
													class="form-control model autonumber" data-m-dec="0"
													data-m-sep="">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>&nbsp;</label>
											<div>
												<button type="button"
													class="btn btn-primary btn-sm add-maker clone"
													id="add-maker">
													<i class="fa fa-plus"></i>
												</button>
												<button type="button"
													class="btn btn-danger btn-sm delete-maker-item"
													id="delete-maker">
													<i class="fa fa-trash"></i>
												</button>
											</div>
										</div>
									</div>
								</div>

								<!-- <div class="row"></div> -->
							</div>
						</div>
					</fieldset>
				</div>
				<div class="box-footer ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary save-maker"
							id="save-maker">Save</button>
						<button type="reset" class="btn btn-primary "
							onclick="location.reload();" id="rest-form">Reset</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>