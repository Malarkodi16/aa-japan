<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Location</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Location</span></li>
		<li class="active">Create Location</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<form:form method="POST" action="${contextPath}/master/location/save"
			id="locationForm" modelAttribute="locationForm">
			<div class="box-body">
				<form:input name="id" id="locationId" type="hidden" path="id"
					data-value="${id}" />
				<form:input name="code" id="locationCode" type="hidden" path="code"
					data-value="${code}" />
				<fieldset>
					<legend>Location Info</legend>
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Location Type</label>
									<div class="element-wrapper">
										<div class="element-wrapper">
											<form:select path="type" id="type" name="type"
												class="form-control" style="width: 100%;" tabindex="1"
												data-value="${locationForm.type}"
												data-placeholder="Select Purchase Type">
												<form:option value=""></form:option>
												<form:option data-type="auction" value="auction">Auction</form:option>
												<form:option data-type="port" value="port">Port</form:option>
											</form:select>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<label class="required">Location Name</label>
								<div class="element-wrapper">
									<form:input path="displayName" class="form-control"
										tabindex="2" name="displayName" id="displayName"
										data-value="${locationForm.displayName}" />
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<label class="required">Address</label>
								<div class="element-wrapper">
									<form:input path="address" class="form-control" tabindex="3"
										id="address" name="address"
										data-value="${locationForm.address}" />
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<label>Atsukai</label>
								<div class="element-wrapper">
									<form:input path="atsukai" class="form-control" tabindex="4"
										name="atsukai" data-value="${locationForm.atsukai}" />
								</div>
								<span class="help-block"></span>
							</div>	
						</div>
						<div class="row">
						<div class="col-md-3">
								<label>Tantousha</label>
								<div class="element-wrapper">
									<form:input path="tantousha" class="form-control" tabindex="4"
										name="tantousha" data-value="${locationForm.tantousha}" />
								</div>
								<span class="help-block"></span>
							</div>
						<div class="col-md-3">
								<label>Phone</label>
								<div class="element-wrapper">
									<form:input path="phone" class="form-control" tabindex="4"
										name="phone" data-value="${locationForm.phone}" />
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<label>Fax</label>
								<div class="element-wrapper">
									<form:input path="fax" class="form-control" tabindex="5"
										name="fax" data-value="${locationForm.fax}" />
								</div>
								<span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Shipment Type</label>
									<div class="element-wrapper">
										<div class="element-wrapper">
											<form:select path="shipmentType" id="shipmentType"
												name="shipmentType" class="form-control"
												style="width: 100%;" tabindex="6"
												data-value="${locationForm.shipmentType}"
												data-placeholder="Select Shipment Type">
												<form:option value=""></form:option>
												<form:option data-type="1" value="1">RORO</form:option>
												<form:option data-type="2" value="2">CONTAINER</form:option>
											</form:select>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
						<div class="col-md-3">
								<div class="form-group">
									<label class="required">Shipment Origin Port</label>
									<div class="element-wrapper">
										<div class="element-wrapper">
											<form:select path="shipmentOriginPort"
												id="shipmentOriginPort" name="shipmentOriginPort"
												class="form-control" style="width: 100%;" tabindex="8"
												data-value="${locationForm.shipmentOriginPort}"
												data-placeholder="Select Shipment Origin Port">
												<form:option value=""></form:option>
											</form:select>
										</div>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						
						</div>
					</div>
				</fieldset>

			</div>
			<div class="box-footer">
				<div class="col-md-12">
					<div class="btn-group pull-right">
						<button type="submit" id="save" name="action" value="save"
							class="btn btn-primary">Save</button>
						<button type="submit" id="saveAndContinue" name="action"
							value="continue" class="btn btn-primary">Save &amp;
							Continue</button>
						<c:if test="${empty code}">
							<button type="reset" class="btn btn-primary" value="Reset"
								onclick="location.reload();">Reset</button>
						</c:if>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</section>