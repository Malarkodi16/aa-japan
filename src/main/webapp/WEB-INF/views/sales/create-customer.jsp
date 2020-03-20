<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section class="content-header ">
	<h1>Create Customer</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Customer Management</span></li>
		<li class="active">Create Customer</li>
	</ol>
</section>
<section class="content">
	<div class="alert" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<!-- <div class="box-header"></div> -->
		<div class="box-body customer-data">
			<form method="POST" id="customerForm"
				data-modelAttribute="customerForm">
				<input type="hidden" id="custId" value="${custId}" /> <input
					type="hidden" name="id" class="consignee-data" value="" /> <input
					type="hidden" name="code" class="consignee-data" value="" />
				<div class="container-fluid">
					<fieldset>
						<legend>Customer Type</legend>
						<div class="form-group">
							<div class="row">
								<div class="col-md-4">
									<label> <input type="checkbox" class="minimal"
										id="lcCustomer" name="lcCustomer">&nbsp;&nbsp; LC
										Customer
									</label>
								</div>
								<div class="col-md-4">
									<label> <input name="flag" type="radio" id="flag2"
										class="minimal" value="0" checked>&nbsp;&nbsp;Customer
									</label> <label class="ml-5"> <input name="flag" id="flag"
										type="radio" class="minimal" value="1">&nbsp;&nbsp;Branch
									</label>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend>Basic Info</legend>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Full Name</label> <input
										name="firstName" id="firstName" type="text"
										class="form-control consignee-data required" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2 hidden">
								<div class="form-group">
									<label>Last Name</label> <input name="lastName" id="lastName"
										type="text" class="form-control consignee-data" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Nick Name</label> <input
										name="nickName" id="nickName" type="text"
										class="form-control consignee-data required" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Mobile Number</label> <input
										name="mobileNo" id="mobileNo" type="text"
										class="form-control consignee-data phone required" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Email</label> <input name="email"
										id="email" type="text" class="form-control consignee-data" />
									<span class="help-block required"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Company</label> <input
										name="companyName" id="companyName" type="text"
										class="form-control consignee-data" /> <span
										class="help-block required"></span>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<div class="form-group">
									<label>Skype Id</label> <input name="skypeId" id="skypeId"
										type="text" class="form-control consignee-data" /> <span
										class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label>Comments</label>
									<textarea name="comments" id="comments"
										class="form-control consignee-data" rows="2"></textarea>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<label>Credit Limit</label> <input
									class="form-control autonumber consignee-data creditBalance"
									name="creditBalance" id="creditBalance" type="text"
									data-a-sign="¥ " data-v-min="0" data-m-dec="0">
							</div>
							<div class="col-md-3">
								<label> &nbsp; &nbsp;</label>
								<div>
									<input type="checkbox" class="minimal form-control"
										id="checkCreditLimit" name="checkCreditLimit">&nbsp;&nbsp;
									<label>Check Credit Limit </label>
								</div>
							</div>

						</div>
					</fieldset>
					<fieldset>
						<legend>Address Info</legend>
						<div class="row form-group">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Address</label> <input name="address"
										id="address" type="text"
										class="form-control consignee-data required" /><span
										class="help-block required"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">City</label> <input name="city"
										id="city" type="text"
										class="form-control consignee-data required" /><span
										class="help-block required"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Country</label> <select
										class="form-control consignee-data select2 con_country"
										name="country" id="customerCountry"
										data-placeholder="Select Country" style="width: 100%">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-2">
								<div class="form-group">
									<label class="required">Port</label> <select
										class="form-control consignee-data select2 port" name="port"
										data-placeholder="Select Port" style="width: 100%;">
										<option value=""></option>
									</select> <span class="help-block"></span>
								</div>
							</div>
							<div id="yardFields" style="display: none;">
								<div class="col-md-2">
									<div class="form-group">
										<label>CFS Yard</label> <select
											class="form-control consignee-data select2 yard" name="yard"
											data-placeholder="Select Yard" style="width: 100%;">
											<option value=""></option>
										</select> <span class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
						<legend>Bank Details</legend>
						<div class="row">
							<div class="col-md-3">
								<label class="required">Currency Type</label> <select
									class="form-control consignee-data select2 currencyType"
									name="currencyType" data-placeholder="Select Currency Type"
									style="width: 100%;">
									<option value=""></option>
								</select><span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<label class="required">Default Payment</label> <select
									class="form-control consignee-data select2 paymentType"
									name="paymentType" data-placeholder="Select Payment Type"
									style="width: 100%;">
									<option value=""></option>
									<option value="C&F">C&F</option>
									<option value="CIF">CIF</option>
									<option value="FOB">FOB</option>
								</select><span class="help-block"></span>
							</div>
							<div class="col-md-3">
								<label>Bank Name</label> <select
									class="form-control consignee-data select2 bank" name="bank"
									data-placeholder="Select Bank" style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
							<div class="col-md-3">
								<label>Account No</label> <input type="text" name="accountNo"
									id="accountNo" class="form-control consignee-data" />
							</div>

						</div>
					</fieldset>
					<div class="row padding mt-25 mb-5t">
						<div class="form-group">
							<div class="col-md-3 pull-left">
								<button type="button" id="hide" data-action="add"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" data-target="#add-consignee"
									class="btn btn-primary">Add Consignee</button>
								<button type="button" id="addNotifyParty" data-action="add"
									data-backdrop="static" data-keyboard="false"
									data-toggle="modal" title="Add Notify Party"
									data-target="#add-notify-party" class="btn btn-primary">Add
									Notify party</button>
							</div>
						</div>
					</div>
					<div id="consignee-container">
						<div class="row"></div>
					</div>

					<div id="consignee-clone-container" class="box box-info hidden">
						<div class="box-body col-md-4 consignee-block active"
							data-name="consigneeBlock" data-update="0">
							<div class="media" id="view-consignee">
								<div class="media-body">
									<div class="clearfix">
										<input type="hidden" name="consigneeData" value="" />
										<p class="pull-right hidden consignee ">
											<span>consignee</span> <i
												class="fa fa-fw fa-edit edit-consignee" data-action="update"
												id="update-consignee" data-target="#add-consignee"
												data-toggle="modal"></i> <i
												class="fa fa-fw fa-trash delete-consignee"> </i>
										</p>
										<p class="pull-right hidden notifyParty">
											<span>Notify Party</span> <i class="fa fa-fw fa-edit"
												data-action="update" id="update-notify-party"
												data-target="#add-notify-party" data-toggle="modal"></i> <i
												class="fa fa-fw fa-trash delete-notify-party"> </i>
										</p>

										<h4 style="margin-top: 0">
											<span id="fName"></span> <span id="lName"></span>
										</h4>
										<i class="fa fa-map-marker mr-5"></i><span id="address"></span>
										<p style="margin-bottom: 1"></p>
										<hr>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer">
						<div class="pull-right">
							<button type="button" class="btn btn-primary save-customer"
								id="save_customer" data-value=0>
								<i class=""></i>Save
							</button>
							<button type="button" class="btn btn-primary save-customer"
								id="save_continue_customer" data-value=1>
								<i class=""></i>Save &amp; Continue
							</button>
							<button type="reset" class="btn btn-primary" value="Reset"
								onclick="location.reload();">Reset</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="modal fade" id="add-consignee">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<div class="row">
						<div class="col-md-4">
							<h3 class="modal-title">Consignee Info</h3>
						</div>
						<div class="col-md-4"></div>
						<div class="form-inline hidden" id="sameAsCustomerDiv">
							<div class="form-group">
								<label>&nbsp;</label>
								<div class="form-control">
									<input type="checkbox" id="sameAsCustomer"
										name="sameAsCustomer" class="form-control minimal" value="1"><label
										class="ml-5">Same as Customer</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<form id="consigneeForm">
						<div class="container-fluid">
							<input type="hidden" id="update-id" value="" />

							<!-- Consignee Info -->
							<div id="consigneeModal">
								<input type="hidden" id="id" name="id" value="" /> <input
									type="hidden" id="deleteFlag" name="deleteFlag" value="" />
								<fieldset>
									<div class="row">
										<div class="col-md-12">
											<div class="row">
												<div class="col-md-6">
													<div class="form-group">
														<label class="required">Full Name</label> <input
															type="text" name="cFirstName" id="cFirstName"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Last Name</label> <input
															type="text" name="cLastName" id="cLastName"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Email</label> <input type="text"
															name="cEmail" id="cEmail" class="form-control" /> <span
															class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Mobile No</label> <input
															type="text" name="cMobileNo" id="cMobileNo"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label>Address</label>
														<textarea name="cAddress" id="cAddress"
															class="form-control"></textarea>
														<span class="help-block"></span>
													</div>
												</div>
											</div>
											<div id="IfYes" class="hidden">
												<legend>Notify Party</legend>
												<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label>Full Name</label> <input type="text"
																name="npFirstName" id="npFirstName" class="form-control" />
															<span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Last Name</label> <input
																type="text" name="npLastName" id="npLastName"
																class="form-control" /> <span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Email</label> <input type="text"
																name="npEmail" id="npEmail" class="form-control" /> <span
																class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Mobile No</label> <input
																type="text" name="npMobileNo" id="npMobileNo"
																class="form-control" /> <span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-6">
														<div class="form-group">
															<label>Address</label>
															<textarea name="npAddress" id="npAddress"
																class="form-control"></textarea>
															<span class="help-block"></span>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</fieldset>
							</div>

							<!-- /.box-body -->
							<div class="box-footer ">
								<div class="pull-right">
									<button type="button" class="btn btn-primary save_consignee"
										id="add_consignee">Add</button>
									<button type="reset" class="btn btn-primary"
										id="close_consignee" data-dismiss="modal">Close</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<div class="modal fade" id="add-notify-party">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Notify Party Info</h3>
				</div> -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<div class="row">
						<div class="col-md-4">
							<h3 class="modal-title">Notify Party Info</h3>
						</div>
						<div class="col-md-4"></div>
						<div class="form-inline" id="sameAsConsigneeDiv">
							<div class="form-group">
								<label>&nbsp;</label>
								<div class="form-control">
									<input type="checkbox" id="sameAsConsignee"
										name="sameAsConsignee" class="form-control minimal" value="1"><label
										class="ml-5">Same as Consignee</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<form id="nofityPartyForm">
						<div class="container-fluid">
							<input type="hidden" id="update-id" value="" />

							<!-- Consignee Info -->
							<div id="notifyPartyModal">
								<input type="hidden" id="id" name="id" value="" /> <input
									type="hidden" id="deleteFlag" name="deleteFlag" value="" />
								<fieldset>
									<div class="row">
										<div class="col-md-12">
											<div class="row hidden">
												<div class="col-md-6">
													<div class="form-group">
														<label class="required">Full Name</label> <input
															type="text" name="cFirstName" id="cFirstName"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Last Name</label> <input
															type="text" name="cLastName" id="cLastName"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Email</label> <input type="text"
															name="cEmail" id="cEmail" class="form-control" /> <span
															class="help-block"></span>
													</div>
												</div>
												<div class="col-md-3 hidden">
													<div class="form-group">
														<label class="required">Mobile No</label> <input
															type="text" name="cMobileNo" id="cMobileNo"
															class="form-control" /> <span class="help-block"></span>
													</div>
												</div>
												<div class="col-md-6">
													<div class="form-group">
														<label>Address</label>
														<textarea name="cAddress" id="cAddress"
															class="form-control"></textarea>
														<span class="help-block"></span>
													</div>
												</div>
											</div>
											<div id="IfYes">
												<div class="row">
													<div class="col-md-6">
														<div class="form-group">
															<label class="required">Full Name</label> <input
																type="text" name="npFirstName" id="npFirstName"
																class="form-control" required="required" /> <span
																class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Last Name</label> <input
																type="text" name="npLastName" id="npLastName"
																class="form-control" /> <span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Email</label> <input type="text"
																name="npEmail" id="npEmail" class="form-control" /> <span
																class="help-block"></span>
														</div>
													</div>
													<div class="col-md-3 hidden">
														<div class="form-group">
															<label class="required">Mobile No</label> <input
																type="text" name="npMobileNo" id="npMobileNo"
																class="form-control" /> <span class="help-block"></span>
														</div>
													</div>
													<div class="col-md-6">
														<div class="form-group">
															<label>Address</label>
															<textarea name="npAddress" id="npAddress"
																class="form-control"></textarea>
															<span class="help-block"></span>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</fieldset>
							</div>

							<!-- /.box-body -->
							<div class="box-footer ">
								<div class="pull-right">
									<button type="button" class="btn btn-primary save_notify_party"
										id="add_notify_party">Add</button>
									<button type="reset" class="btn btn-primary"
										id="close_notify_party" data-dismiss="modal">Close</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>
