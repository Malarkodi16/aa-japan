<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>CR RECEIVED</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Cr Received</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid ">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label>Purchase Type</label> <select id="purchaseType"
								name="supplierType" class="form-control" style="width: 100%;">
								<option value="">All</option>
								<option data-type="auction" value="auction">Auction</option>
								<option data-type="supplier" value="local">Local</option>
								<option data-type="supplier" value="overseas">Overseas</option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label>Supplier/Auction</label> <select id="purchasedSupplier"
								class="form-control select2" style="width: 100%;"
								disabled="true">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div id="auctionFields" style="display: none;">
						<div class="col-md-2">
							<div class="form-group">
								<label for="purchasedAuctionHouse">Auction House</label> <select
									id="purchasedAuctionHouse" class="form-control"
									style="width: 100%;">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchased Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" name="purchaseDate"
									id="table-filter-purchased-date" class="form-control"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly" />
							</div>
						</div>
					</div>
				</div>

				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-export-certificates-filter-length"
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
							<input type="text" id="table-export-certificates-filter-search"
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-2 form-inline pull-right">
						<button type="button" class="btn btn-block btn-primary"
							data-toggle="modal" data-backdrop="static" data-keyboard="false"
							data-target="#modal-handover-document">Handover Document</button>
					</div>

				</div>
				<div class="table-responsive">
					<table id="table-export-certificates"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0"><input type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Purchased Date</th>
								<th data-index="2" class="align-center">Shuppin No.</th>
								<th data-index="3" class="align-center">Stock No.</th>
								<th data-index="4" class="align-center">Chassis No.</th>
								<th data-index="5" class="align-center">Auction/Supplier</th>
								<th data-index="6" class="align-center">Auction House</th>
								<th data-index="7" class="align-center">First Reg.Date</th>
								<th data-index="8" class="align-center">Received Doc Type</th>
								<th data-index="9" class="align-center">Doc Received Date</th>
								<th data-index="10" class="align-center">Converted Date</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<!-- 	.modal Update Document Detail-->
		<div class="modal fade" id="modal-handover-document">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">Hand Over Document</h4>
					</div>
					<div class="modal-body">
						<form id="form-handover-document">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label>Handover To</label>
											<div class="element-wrapper">
												<select name="handoverTo" id="handoverTo"
													class="form-control" style="width: 100%;">
													<option value="">Select HandOver Type</option>
													<option value="ACCOUNTS">Accounts</option>
													<option value="SALES">Sales</option>
													<option value="RECYCLE_PURPOSE">Recycle Purpose</option>
												</select>
											</div>
											<span class="help-block"></span>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group" id="select-User">
											<label>Users</label>
											<div class="element-wrapper">
												<select name="handoverToUserId" id="handoverToUserId"
													class="form-control select2-select" style="width: 100%;"
													data-placeholder="HandOver To User">
													<option value=""></option>
												</select> <span class="help-block"></span>
											</div>

										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button id="update-cr-doc" class="btn btn-primary">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button id="btn-close" data-dismiss="modal"
							class="btn btn-primary">
							<i class="fa fa-fw fa-close"></i> Close
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>