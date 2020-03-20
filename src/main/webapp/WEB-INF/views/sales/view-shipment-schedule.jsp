
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Shipment Schedule</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Shipment Schedule</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="row form-group" id="searchCondition">
				<!-- <div class="col-md-2">
					<div class="form-group">
						<label>Continent</label> <select
							class="form-control data-to-save select2" id="continent"
							name="continent" data-placeholder="All">
							<option value=""></option>
						</select>
					</div>
				</div> -->

				<div class="col-md-2">
					<div class="form-group">
						<label>Origin Port</label> <select
							class="form-control data-to-save select2" name="orginPort"
							data-placeholder="All">
							<option value=""></option>
						</select>
					</div>
				</div>

				<div class="col-md-2">
					<div class="form-group">
						<label>Dest. Port</label> <select class="form-control select2"
							name="destPortSelect" data-placeholder="All">
							<option value=""></option>
						</select> <input type="hidden" class="data-to-save" name="destPort"
							value="" />
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<button type="button" class="btn btn-primary"
							style="margin-top: 24px" id="btn-search">Search</button>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="table-responsive">
				<div class="row form-group">
					<div class="form-group">
						<div class="col-md-1 form-inline pull-left">
							<div class="form-group">
								<select id="table-filter-length" class="form-control">
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
									placeholder="Search by keyword" autocomplete="off"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>

					</div>
				</div>
				<table id="table-shipment-list"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0" style="width: 10px"><input
								type="checkbox" id="select-all" /></th>
							<th data-index="1" class="align-center">Shipping Company</th>
							<th data-index="2" class="align-center">Vessel Name</th>
							<th data-index="3" class="align-center">Voyage</th>
							<th data-index="4" class="align-center">Deck Height</th>
							<!-- 							<th data-index="5" class="align-center">Start Country</th> -->
							<th data-index="5" class="align-center">Start Port</th>
							<th data-index="6" class="align-center">Start Date</th>
							<!-- 							<th data-index="8" class="align-center">Destination -->
							<!-- 								Country</th> -->
							<th data-index="7" class="align-center">Destination Port</th>
							<th data-index="8" class="align-center">End Date</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>

</section>
