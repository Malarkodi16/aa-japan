<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>User Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">User Management</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="pull-right">
						<button class="btn btn-primary" id="new_user" type="button"
							data-backdrop="static" data-keyboard="false" data-toggle="modal"
							data-target="#add-User">Create User</button>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
				<div class="table-responsive">
					<table id="table-userlist"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Full Name</th>
								<th>Date of Birth</th>
								<th>Mobile No</th>
								<th>Username</th>
								<th>Department</th>
								<th>Role</th>
								<th>Report to</th>
								<th>Location</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>
<form action="#" id="user-form">
	<div class="modal fade" id="add-User">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">User Details</h4>
				</div>
				<div class="modal-body">
					<div class="box-body">
						<input type="hidden" name="userId" value="" />
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Full Name</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-user"></i>
												</div>
												<input name="fullname" id="fullname"
													placeholder="Enter Fullname Name" type="text"
													class="form-control required" />
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="	">Date of Birth</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-calendar"></i>
												</div>
												<input name="dob" id="dob" type="text"
													class="form-control  pull-right datepicker"
													placeholder="dd-mm-yyyy" />
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="">Mobile No</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-phone"></i>
												</div>
												<input name="mobileno" id="mobileno"
													placeholder="Enter Mobile No" type="text"
													class="form-control " />
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">User Name</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-address-card-o"></i>
												</div>
												<input name="username" id="username"
													placeholder="Enter User Name" type="text"
													class="form-control required skip-toUppercase" />
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Password</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa  fa-unlock-alt"></i>
												</div>
												<input name="password" id="password"
													placeholder="Enter Password" type="password"
													class="form-control required skip-toUppercase" />
												<div class="input-group-addon" id="passwordEye">
													<i class="fa fa-eye"></i> <i class="fa fa-eye-slash hidden"></i>
												</div>
											</div>
											<span class="help-block"></span>
										</div>
									</div>

								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Confirm Password</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa  fa-unlock-alt"></i>
												</div>
												<input name="confirmPassword" id="confirmPassword"
													placeholder="Re Enter Password" type="password"
													class="form-control required skip-toUppercase" />
											</div>
											<span class="help-block"></span>
										</div>
									</div>

								</div>
							</div>
							<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Department</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-building"></i>
												</div>
												<select class="form-control select2" id="department"
													name="department" data-placeholder="Select Department">
													<option value=""></option>
												</select>
											</div>
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="required">Role</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-users"></i>
												</div>
												<select class="form-control select2" id="role" name="role"
													data-placeholder="Select Role">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
								<div class="col-md-4 reporting-person-container">
									<div class="form-group">
										<label class="required">Reporting Person</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-users"></i>
												</div>
												<select class="form-control select2" id="reportTo"
													name="reportTo" data-placeholder="Select Reporting Person">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-4 sales-person-location">
									<div class="form-group">
										<label class="required">Location</label>
										<div class="element-wrapper">
											<div class="input-group">
												<div class="input-group-addon">
													<i class="fa fa-users"></i>
												</div>
												<select class="form-control select2" id="location"
													name="location"
													data-placeholder="Select Sales Person Location">
													<option value=""></option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- /.box-body -->
					<div class="box-footer ">
						<div class="pull-right">
							<button type="button" class="btn btn-primary " id="save_user">
								<i class="fa fa-fw fa-save"></i>Save
							</button>
							<button type="button" class="btn btn-primary " id="close_user"
								data-dismiss="modal">
								<i class="fa fa-fw fa-save"></i>Close
							</button>
						</div>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</form>
<!-- /.modal -->
