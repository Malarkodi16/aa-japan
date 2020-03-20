<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock Organizer</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Stock Organizer</li>
	</ol>
</section>

<section class="content">
	<%-- <div class="box box-widget">
		<form method="POST" id="transporter-fee-Form"
			action="${contextPath}/report/save-list">
			<div class="overlay hidden">
				<i class="fa fa-refresh fa-spin"></i>
			</div>



		</form>
	</div> --%>

	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="required">Report</label>
							<div class="element-wrapper">
								<select name="customLists" id="customLists"
									data-placeholder="Select Report" class="form-control required"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="submit" id="btn-search"
								class="btn form-control btn-primary">
								<i class="fa fa-save mr-5"></i>View
							</button>
						</div>

					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>&nbsp;</label>
							<button type="button" class="btn form-control btn-primary"
								data-backdrop="static" data-keyboard="false" data-toggle="modal"
								data-target="#modal-create-report" id="btn-create-report">Create
								Report</button>
						</div>
					</div>

				</div>
			</div>
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-6 pull-left">
						<div class="has-feedback pull-left">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-6 form-inline">
						<div class="pull-right">
							<button class="btn btn-primary " type="button"
								id="excel_export_all">
								<i class="fa fa-file-excel-o" aria-hidden="true"> Export
									Excel</i>
							</button>
							<span> <select id="table-filter-length"
								class="form-control input-sm">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
							</select>
							</span>
						</div>
					</div>
				</div>
				<div class="table-responsive reportTable" id="showData">
					<table id="table-report" class="table table-bordered table-striped">
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modal-create-report">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Report</h4>
				</div>
				<div class="modal-body">
					<input type="hidden" name="code" value="" />
				<form id="reportCreateForm">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="required">Report Fields</label>
									<div class="element-wrapper">
										<select name="fields" id="customFields"
											data-placeholder="Select Fields"
											class="form-control" multiple="multiple"
											style="width: 100%;">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>

						</div>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Report Name</label>
									<div class="element-wrapper">
										<input type="text" name="name" id="customListName"
											class="form-control " />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Report Fields</label>
									<div class="element-wrapper">
										<select name="period" id="period"
											data-placeholder="Select Date"
											class="form-control"
											style="width: 100%;">
											<option value="thismonth">This Month</option>
											<option value="lastmonth">Last Month</option>
											<option value="last3months">Last 3 Months</option>
											<option value="period">Selected Date</option>
											</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3 custom-date">
								<div class="form-group">
									<label class="required">From</label>
									<div class="element-wrapper">
										<input type="text" name="from" id="from"
											class="form-control datepicker" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3 custom-date">
								<div class="form-group">
									<label class="required">To</label>
									<div class="element-wrapper">
										<input type="text" name="to" id="to"
											class="form-control datepicker" />
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</div>
				
				</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-report">
							<em class="fa fa-fw fa-save"></em>Save
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<em class="fa fa-fw fa-close"></em>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>