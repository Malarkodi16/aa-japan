<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Bank List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active"><a>Bank List</a></li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-head">
			<form action="${contextPath }/master/create-bank">
				<div class="box-header with-border">
					<div class="container-fluid">
						<div class="row form-group">
							<div class="col-md-3 pull-right mt-5">
								<div class="form-group">
									<button class="btn btn-primary pull-right" id="create-maker">Create
										New Bank</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Bank List show/search inputs -->
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
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					
				</div>
				<!-- datatable -->
				<div class="table-responsive">
					<table id="table-master-bank-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th class="align-center">Bank Name</th>
								<th class="align-center">Account Code</th>
								<th class="align-center">Total Balance</th>
								<th class="align-center">Clearing Balance</th>
								<th class="align-center">Action</th>
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