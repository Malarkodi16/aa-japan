<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<form action="#" id="customer-form">
	<div class="box-body">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="required">First Name</label> <input name="firstName"
							id="firstname" type="text" class="form-control required" /> <span
							class="help-block"></span>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="required">Last Name</label> <input name="lastName"
							id="lastname" type="text" class="form-control required" /> <span
							class="help-block"></span>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="required">Nick Name</label> <input name="nickName"
							id="nickName" type="text" class="form-control required" /> <span
							class="help-block"></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="required">Email</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-fw fa-envelope"></i>
							</div>
							<input name="email" id="email" type="text"
								class="form-control email" />
						</div>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label>Skype Id</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-skype"></i>
							</div>
							<input name="skypeId" id="skypeId" type="text"
								class="form-control" />
						</div>
						<span class="help-block"></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="required">Mobile No</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-fw fa-phone"></i>
							</div>
							<input name="mobileNo" id="mobileNo" type="text"
								class="form-control phone required" />
						</div>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label>Company Name</label> <input name="companyName"
							id="companyName" type="text" class="form-control required" /> <span
							class="help-block"></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label>Country</label> <select name="country"
							class="form-control select2" style="width: 100%;"
							data-placeholder="Select Country">
						</select> <span class="help-block"></span>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label>Comments</label>
						<textarea name="comments" class="form-control" rows="3"></textarea>
						<span class="help-block"></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
<!-- /.box-body -->
<div class="box-footer">
	<div class="pull-right">
		<button class="btn btn-primary " id="save_customer">
			<i class="fa fa-fw fa-save"></i>Save
		</button>
		<button class="btn btn-primary" data-dismiss="modal">
			<i class="fa fa-fw fa-close"></i>Close
		</button>
	</div>
</div>
