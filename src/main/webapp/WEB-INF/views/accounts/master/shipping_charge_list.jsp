<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Shipping Charge List</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Shipping Charge List</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="${contextPath }/accounts/create/master/shipping">
			<div class="box-header">
				<div class="form-group">
					<div class="col-md-3 pull-right">
						<button id="create-ship-charge" class="btn btn-primary pull-right">Create
							Shipping Charge</button>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="manufacture-table-filter-length"
								class="form-control ">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="manufacture-table-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>

				</div>
				<div class="table-responsive">
					<table id="manufacture-table"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th class="align-center">Origin Country</th>
								<th class="align-center">Dest Country</th>
								<th class="align-center">M3 From</th>
								<th class="align-center">M3 To</th>
								<th class="align-center">Amount</th>
							</tr>
						</thead>

					</table>
				</div>
			</div>
		</div>

	</div>
</section>