<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Transporter Fee Creation</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Transporter Fee</span></li>
		<li class="active">Create</li>
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
			<form method="POST" id="transporter-fee-Form"
				action="${contextPath}/transport/transporter/fee/save">
				<input type="hidden" id="id" name="id" value="${id}">
				<div class="box-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Transporter</label>
									<div class="element-wrapper">
										<select name="transporter" id="transporter"
											data-placeholder="Select Transporter"
											class="form-control required" style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">From</label>
									<div class="element-wrapper">
										<select name="from" id="from" data-placeholder="Select From"
											class="form-control required" style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">To</label>
									<div class="element-wrapper">
										<select name="to" id="to" data-placeholder="Select To"
											class="form-control required" style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<!-- <div class="col-md-3">
								<div class="form-group">
									<label class="required">Categories</label><div class="element-wrapper"> <select name="categories" id="categories"
										data-placeholder="Categories" class="form-control required" multiple="multiple"
										style="width: 100%;">
										<option value=""></option>
									</select></div><span class="help-block"></span>
								</div>
							</div> -->
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Transport Category</label>
									<div class="element-wrapper">
										<select name="transportCategory" id="transportCategory"
											data-placeholder="Transport Category"
											class="form-control required" multiple="multiple"
											style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Amount</label>
									<div class="element-wrapper">
										<input type="text" name="amount" id="amount"
											data-validation="number"
											class="form-control required autonumber" data-m-dec="0"
											data-a-sign="¥ " />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="pull-right">
					<button type="submit" id="btn-save" class="btn btn-primary">
						<i class="fa fa-save mr-5"></i>Save
					</button>
					<button type="reset" class="btn btn-primary reset">
						<i class="fa fa-repeat mr-5"></i>Reset
					</button>
				</div>
			</form>
		</div>
	</div>
</section>