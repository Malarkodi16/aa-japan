<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Supplier Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Administration</span></li>
		<li class="active">Supplier Management</li>
	</ol>
</section>

<!--Master Supplier List-->
<section class="content">
	<div class="box box-solid">
		<form action="${contextPath }/a/supplier/create">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-3 pull-right mt-5">
							<div class="form-group">
								<button class="btn btn-primary pull-right" id="create-supplier">Create
									New Supplier</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Supplier list show/search inputs -->
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
					<table id="table-master-supplier-list"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th>Supplier company</th>
								<th>Supplier Code</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div id="supplier-list-details-view" class="hidden">
			<div class="container-fluid supplier-detail-view">
				<input type="hidden" name="supplierCode" value="" />
				<div class="box-body no-padding bg-darkgray">
					<table class="table">
						<thead>
							<tr>
								<th style="width: 100px">#</th>
								<th class="align-center auction-house">Auction House</th>
								<th class="align-center">Address</th>
								<th class="align-center">Phone</th>
								<th class="align-center">Email</th>
								<th class="align-center">Fax</th>
								<th class="align-center">Pos Nos</th>
								<th class="align-center" style="width: 150px;">Action</th>
							</tr>
						</thead>
						<tbody>
							<tr class="clone-row hide">
								<td class="s-no"><span></span><input type="hidden"
									name="itemId" value="" /></td>
								<td class="auctionHouse auction-house"></td>
								<td class="address"></td>
								<td class="phone"></td>
								<td class="email"></td>
								<td class="fax"></td>
								<td class="posNos"></td>
								<td class="action align-center"><button
										class="ml-5 btn btn-danger btn-xs"
										id="delete-supplierLocation" title="delete">
										<i class="fa fa-fw fa-close"></i>
									</button></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>