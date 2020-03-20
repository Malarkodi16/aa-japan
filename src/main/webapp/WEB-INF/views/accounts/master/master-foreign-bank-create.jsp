<section class="content-header">
	<h1>Create Bank</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active"><a>Create Bank</a></li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<form id="bank-create-form">
			<div class="box-body">
				<div class="container-fluid">
					<div class="row form-group">
						<div class="col-md-3">
							<label class="required">Bank Name</label> <input name="bank"
								type="text" class="form-control required bankName" id="bank" />
							<span class="help-block"></span>
						</div>
						<div class="col-md-3">
							<label class="required">Country</label> <select name="country"
								class="form-control select2-select"
								data-placeholder="Select Country" id="country">
								<option value=""></option>

							</select> <span class="help-block"></span>
						</div>


					</div>
					<div class="row form-group">
						<div class="col-md-8">
							<label >Beneficiary Certify</label>
							<textarea name="beneficiaryCertify" cols="150px" rows="5px"
								data-placeholder="Beneficiary Certify" id="beneficiaryCertify"></textarea>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-md-8">
							<label >Emission Remarks</label>
							<textarea name="licenseDoc" cols="150px" rows="5px"
								data-placeholder="License Doc" id="licenseDoc"></textarea>
							<span class="help-block"></span>
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
	</div>
</section>