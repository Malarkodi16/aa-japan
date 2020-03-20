<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>Inventory</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Inventory</li>
	</ol>
</section>
<section class="content">

	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Select Payment Type</label> <select id="paymentTypeFilter"
								class="form-control select2" name="paymentType"
								data-placeholder="Select Payment Type">
								<option value=""></option>
								<option value="FOB">FOB</option>
								<option value="C&F">C&amp;F</option>
								<option value="CIF">CIF</option>
							</select><span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Payment Status</label> <select id="paymentStatusFilter"
								class="form-control select2" name="paymentStatus"
								data-placeholder="Select Type">
								<option value=""></option>
								<option value="NOT_RECEIVED">NOT RECEIVED</option>
								<option value="RECEIVED">RECEIVED</option>
								<option value="RECEIVED_PARTIAL">RECEIVED PARTIAL</option>
							</select><span class="help-block"></span>
						</div>
					</div>
				</div>

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

				<!-- table start -->
				<div class="table-responsive">
					<table id="table-inventory"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">Shipping Status</th>
								<th data-index="4" class="align-center" style="width: 50px">ETD</th>
								<th data-index="5" class="align-center" style="width: 50px">ETA</th>
								<th data-index="6" class="align-center">Selling Price</th>
								<th data-index="7" class="align-center">Payment Type</th>
								<th data-index="8" class="align-center">Payment Status</th>
								<th data-index="9" class="align-center">Inventory Status</th>
								<!-- <th>Action</th> -->
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