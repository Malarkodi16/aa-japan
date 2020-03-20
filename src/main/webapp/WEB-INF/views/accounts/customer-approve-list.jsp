<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Customer List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Customer Approve List</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-header with-border">
			<div class="container-fluid">
				<div class="row">
					<div class="form-group">
						<div class="col-md-3">
							<div class="form-group">
								<label>Customer</label> <select class="form-control customer"
									id="custId" style="width: 100%;"
									data-placeholder="Search by Customer ID, Name, Email">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="box-body">
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="form-group">
						<div class="col-md-1 form-inline pull-left">
							<div class="pull-left">
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
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-customerlist"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Customer No.</th>
								<th data-index="1">Full Name</th>
								<th data-index="2">Last Name</th>
								<th data-index="3">Nick Name</th>
								<th data-index="4">Email</th>
								<th data-index="5">SkypeID</th>
								<th data-index="6">Mobile No.</th>
								<th data-index="7">Company Name</th>
								<th data-index="8">Credit Limit</th>
								<th data-index="9">Country</th>
								<th data-index="10">Port</th>
								<th data-index="11" class="align-center">Action</th>
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