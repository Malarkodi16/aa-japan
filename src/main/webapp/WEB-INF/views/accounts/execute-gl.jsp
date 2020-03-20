<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Execute GL</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Execute GL</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form action="">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<div class="input-group date">
									<div class="input-group-addon">
										<i class="fa fa-calendar"></i> <b>As of Now</b>
									</div>
									<input type="text" class="form-control datepicker"
										id="datepicker" placeholder="dd-mm-yyyy" readonly>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</form>
		<div class="box box-solid">
			<div class="box-body">
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
						<div class="col-md-8 form-inline pull-right">
							<div class="pull-right">
								<div class="form-group">
									<button type="button" class="btn btn-primary Rec-one"
										id="btn-glCountryRec">
										<i class=""></i>Country Reconciliation
									</button>
									<button type="button" class="btn btn-primary Rec-two"
										id="glReconTwo">
										<i class=""></i>Stock Reconciliation
									</button>
									<button type="button" class="btn btn-primary execute-gl"
										id="glExecute">
										<i class=""></i>Execute GL
									</button>
								</div>
							</div>
						</div>
					</div>

					<!-- table start -->

					<div class="table-responsive">
						<table id="table-gl-data"
							class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0" style="width: 10px"><input
										type="checkbox" id="select-all" /></th>
									<th data-index="1" class="align-center">Stock No</th>
									<th data-index="2" class="align-center">Purchase Key
										Credit</th>
									<th data-index="3" class="align-center">Purchase Key Debit</th>
									<th data-index="4" class="align-center">Commission Key
										Credit</th>
									<th data-index="5" class="align-center">Commission Key
										Debit</th>
									<th data-index="6" class="align-center">Road Tax Key
										Credit</th>
									<th data-index="7" class="align-center">Road Tax Key Debit</th>
									<th data-index="8" class="align-center">Recycle Key Credit</th>
									<th data-index="9" class="align-center">Recycle Key Debit</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>