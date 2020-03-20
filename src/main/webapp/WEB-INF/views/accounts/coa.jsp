<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Chart Of Accounts</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active">Chart Of Accounts</li>
	</ol>
</section>


<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Reporting Category</label> <select id="reportingCategory"
								name="reportingCategory" class="form-control select2-tag"
								style="width: 100%;"
								data-placeholder="Select Reporting Category">
								<option value=""></option>
							</select>
						</div>
					</div>
				</div>
			</div>
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
					<div class="col-md-2 pull-right">
						<button class="btn btn-primary pull-right" type="button"
							id="excel_export_all">
							<i class="fa fa-file-excel-o" aria-hidden="true"> Export
								Excel</i>
						</button>
						<button type="button" class="btn  btn-primary" id="addNew"
							data-toggle="modal" data-target="#add-new-coa">New</button>
					</div>
				</div>

				<!-- table start -->

				<div class="row form-group">
					<div class="col-md-2">
						
					</div>
					
				</div>
				<div class="table-responsive">
					<table id="coa-table" class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>

								<th data-index="0">Tax Code</th>
								<th data-index="1">Account</th>
								<th data-index="2">Sub Account</th>
								<th data-index="3">Reporting Category</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model -->
	<div class="modal fade" id="add-new-coa">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Chart Of Account</h4>
					<h4 class="modal-title account-code pull-right hidden">
						Last Code : <span class="code"></span>
					</h4>
				</div>
				<div class="modal-body">
					<form id="create-coa-form">
						<div class="box-body">
							<div class="box-body" id="cloneTO">
								<div class="container-fluid">
									<div class="row">
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Reporting Category</label> <select
													name="reportingCategory" id="reportingCategoryType"
													class="form-control required select2-tag"
													data-placeholder="Reporting Category">
													<option></option>

												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Account Type</label> <select
													name="account" id="coaType"
													class="form-control required select2-tag"
													data-placeholder="Account Type">
													<option></option>
												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Account Name</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="subAccount" id="coaDesc"
														class="form-control required" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Code</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="code" id="coaNo"
														class="form-control required" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">General Ledger</label> <select
													name="generalLedger" id="generalLedger"
													class="form-control required select2-tag"
													data-placeholder="General Ledger">
													<option></option>
												</select> <span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Tkc Code</label>
												<div class="input-group">
													<input type="text" name="tkcCode" id="tkcCode"
														class="form-control required" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-3">
											<div class="form-group">
												<label class="required">Tkc Description</label>
												<div class="input-group">
													<textarea name="tkcDescription" id="tkcDescription"
														class="form-control required"></textarea>
												</div>
												<span class="help-block"></span>
											</div>
										</div>
									</div>
								</div>

								<!-- /.form:form -->
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-coa">
							<i class="fa fa-fw fa-save"></i>Create
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->


</section>
