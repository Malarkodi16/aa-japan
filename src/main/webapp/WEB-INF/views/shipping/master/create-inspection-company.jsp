<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Inspection Company</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Inspection Company</span></li>
		<li class="active">Create Inspection Company</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-body">
			<form class="ins-company" id="ins-company">

				<div class="container-fluid" id="company">
					<fieldset>
						<input type="hidden" class="location" id="code" name="code"
							value="${code}">
						<legend>Inspection Company</legend>
						<div class="form-group">
							<div class="row">
								<div class="col-md-2">
									<label class="required">Company Name</label>
									<div class="element-wrapper">
										<input type="text" id="companyName" name="name"
											class="form-control">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="container-fluid" id="location">
					<fieldset>
						<legend>Location Details</legend>
						<div id="location-clone-conatiner">
							<div class="item">
								<div class="row form-group location-row">
									<input type="hidden" class="location" id="locationId"
										name="locationId" value=""><input type="hidden"
										class="location" id="deleteStatus" name="deleteStatus"
										value="">
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Location Name</label>
											<div class="element-wrapper">
												<input type="text" name="locationName"
													class="form-control validation-required location validate-duplicate-locationName">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Zip</label>
											<div class="element-wrapper">
												<input type="text" name="zip" class="form-control location"
													maxlength="6" size="6">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<label>Tel</label>
										<div class="form-group">
											<div class="element-wrapper">
												<input class="form-control location tel" name="tel" id="tel"
													type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Fax</label>
											<div class="element-wrapper">
												<input class="form-control location" name="fax" id="fax"
													type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Contact Person</label>
											<div class="element-wrapper">
												<input type="text" name="contactPerson"
													class="form-control location">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-3">
										<div class="form-group">
											<label>Address</label>
											<div class="element-wrapper">
												<textarea name="address" id="address"
													class="form-control location" placeholder="Enter.."></textarea>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3 pull-right">
										<div class="form-group">
											<label>&nbsp;</label>
											<div>
												<button type="button" class="btn btn-success btn-clone">
													<i class="fa fa-fw fa-plus"></i>Add More
												</button>
												<button class="btn btn-danger btn-delete">
													<i class="fa fa-fw fa-remove"></i>Delete
												</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div id="location-container" class="location-container hidden">
							<div class="item">
								<div class="row form-group location-row">
									<input type="hidden" class="location" id="locationId"
										name="locationId" value=""> <input type="hidden"
										class="location" id="deleteStatus" name="deleteStatus"
										value="">
									<div class="col-md-3">
										<div class="form-group">
											<label class="required">Location Name</label>
											<div class="element-wrapper">
												<input type="text" name="locationName"
													class="form-control validation-required location validate-duplicate-locationName">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Zip</label>
											<div class="element-wrapper">
												<input type="text" name="zip" class="form-control location"
													maxlength="6" size="6">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<label>Tel</label>
										<div class="form-group">
											<div class="element-wrapper">
												<input class="form-control location tel" name="tel" id="tel"
													type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Fax</label>
											<div class="element-wrapper">
												<input class="form-control location" name="fax" id="fax"
													type="text">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-2">
										<div class="form-group">
											<label>Contact Person</label>
											<div class="element-wrapper">
												<input type="text" name="contactPerson"
													class="form-control location">
											</div>
											<span class="help-block"></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-3">
										<div class="form-group">
											<label>Address</label>
											<div class="element-wrapper">
												<textarea name="address" id="address"
													class="form-control location" placeholder="Enter.."></textarea>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-3 pull-right">
										<div class="form-group">
											<label>&nbsp;</label>
											<div>
												<button type="button" class="btn btn-success btn-clone">
													<i class="fa fa-fw fa-plus"></i>Add More
												</button>
												<button class="btn btn-danger btn-delete">
													<i class="fa fa-fw fa-remove"></i>Delete
												</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="box-footer ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary save-inspection"
							id="save-inspection">Save</button>
						<button type="reset" class="btn btn-primary "
							onclick="location.reload();" id="rest-form">Reset</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>