<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Create Year Of Manufacture</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Create Year Of Manufacture</li>
	</ol>
</section>
<section class="content">
<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form method="post" id="manufacture-of-year-form" action="${contextPath}/documents/year-of-manufacture/create">
		<input type="hidden" id="code" name="code"
					value="${code}">
			<div class="box-body">
				<div class="container-fluid">
					<div class="row form-group">
						<div class="col-md-2">
							<label class="required">Maker</label> <select
								class="select2-select form-control maker" name="maker"
								id="maker" data-placeholder="Select Maker">
								<option value=""></option>
							</select>
							<span class="help-block"></span>
						</div>
						<div class="col-md-2">
							<label class="required">Model</label> <select
								class="select2-select form-control model" name="model"
								id="model" data-placeholder="Select Model">
								<option value=""></option>
							</select>
							<span class="help-block"></span>
						</div>
						<div class="col-md-2">
							<label class="required">Model No.</label> <input type="text" name="modelNo"
								class="form-control" />
								<span class="help-block"></span>
						</div>
						<div class="col-md-2">
							<label class="required">Frame</label> <input type="text" name="frame"
								class="form-control" />
								<span class="help-block"></span>
						</div>
						<div class="col-md-2">
							<label class="required">Serial No From</label> <input type="text"
								name="formatedSerialNoFrom" class="form-control" />
								<span class="help-block"></span>
						</div>
						<div class="col-md-2">
							<label class="required">Serial No To</label> <input type="text" name="formatedSerialNoTo"
								class="form-control" />
								<span class="help-block"></span>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-md-2">
							<label class="required">Manufacture Year</label> <input type="text"
								name="manufactureYear" class="form-control datepicker"
								placeholder=" yyyy" readonly />
								<span class="help-block"></span>
						</div>
					</div>
				</div>
			</div>
			<div class="box-footer">
				<div class="pull-right">
					<button type="button" class="btn btn-primary" id="save-manufacture">Save</button>
					<button type="reset" class="btn btn-default" id="cancel">Cancel</button>
				</div>
			</div>
		</form>
	</div>
</section>