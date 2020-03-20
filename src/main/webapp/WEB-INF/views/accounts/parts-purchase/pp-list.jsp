<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Parts Purchase List</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking & Approval</span></li>
		<li class="active">Parts Purchase</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="container-fluid">
			<form action="${contextPath }/parts/create/page">
				<div class="box-header with-border">
					<div class="container-fluid">
						<div class="row">
							<div class="form-group mt-25">
								<div class="col-md-3 pull-right">
									<div class="form-group">
										<button class="btn btn-primary pull-right" id="create">Create
											invoice</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>

		<div class="box-body">
			<!-- table start -->
			<div class="container-fluid">

				<div class="row">
					<div class="form-group">
						<div class="col-md-6 form-inline pull-right">
							<div class="pull-right">
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
						<div class="col-md-6 form-inline pull-left">
							<div class="has-feedback pull-left">
								<label></label><input type="text" id="table-filter-search"
									class="form-control" placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-parts-purchase-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Invoice No.</th>
								<th data-index="1">Date</th>
								<th data-index="2">Customer</th>
								<th data-index="3" class="align-center">Action</th>
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