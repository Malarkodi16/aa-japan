<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header ">
	<h1>Stock Search</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Stock Management</span></li>
		<li class="active">Stock Search</li>
	</ol>
</section>
<!-- stock. -->
<section class="content ">
	<jsp:include page="/WEB-INF/views/sales/stock-filters.jsp" />
	<div class="box box-solid">
		<div class="box-header">
			<div class="row form-group">

				<div class="col-md-2 pull-left">
					<div class=" ">
						<select class="form-control" id="filter-account">
							<option value="0">All</option>
							<option value="1">AAJ</option>
							<option value="2">SOMO</option>
						</select>
					</div>
				</div>
				<div class="col-md-5">
					<div class="form-inline pull-right">
						<div class="form-group">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="0" checked>&nbsp;&nbsp;Available
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="1">&nbsp;&nbsp;Reserved
								</label>
							</div>
						</div>
						<div class="form-group" style="margin-left: 25px">
							<div class="radio">
								<label> <input type="radio" class="minimal"
									name="statusFilter" value="2">&nbsp;&nbsp;Sold
								</label>
							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
		<div class="box-body">
			<!-- table start -->
			<form action="#" id="form-purchased">
				<div class="container-fluid ">
					<div class="row form-group">
						<div class="col-md-1 form-inline pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
						<div class="col-md-3">
							<div class="has-feedback">
								<input type="text" id="table-filter-search" class="form-control"
									placeholder="Search by keyword"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</div>
						<div class="col-md-8">
							<div class="btn-group pull-right">
								<button type="button" class="btn btn-default"
									data-target="#modal-reauction" data-toggle="modal"
									data-backdrop="static" id="btn-reauction" style="margin-right: 10px;">Re Auction</button>
								<button type="button" class="btn btn-default"
									data-target="#modal-cancel" data-toggle="modal"
									data-backdrop="static" id="btn-cancel">Cancel</button>
							</div>
						</div>
					</div>
					<div class="table-responsive overlay stock-table">
						<i class="fa fa-refresh fa-spin"></i>
						<table id="table-stock" class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th data-index="0" style="text-align: center; width: 20px;"><input
										type="checkbox" id="select-all" /></th>
									<th data-index="1">Stock No</th>
									<th data-index="2">Chassis No</th>
									<th data-index="3">Model Type</th>
									<th data-index="4">Maker</th>
									<th data-index="5">Model</th>
									<th data-index="6">Grade</th>
									<th data-index="7">Min. Selling Price</th>
									<th data-index="8">Year</th>
									<th data-index="9">Buying Price</th>
									<th data-index="10">Purchase Date</th>
									<th data-index="11">Options</th>

								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</form>
		</div>
		<!-- modal -->
		<div class="modal fade" id="modal-reauction">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3 class="modal-title">Re Auction</h3>
					</div>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label class="required">ReAuction Date</label>
								<div class="element-wrapper">
									<input type="text" id="reaucDate" name="reaucDate"
										class="form-control datepicker required"
										placeholder="DD-MM-YYYY" readonly="readonly"
										autocomplete="off">
								</div>
								<span class="help-block"></span>
							</div>
						</div>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<button id="save-reauction" class="btn btn-primary">
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
	<!-- /.modal -->
	<div id="clonable-items" class="hide">
		<div class="container" id="re-auction-clone-container">
			<div class="row item form-group">
				<input type="hidden" name="recycleAmount">
				<div class="col-md-2" style="display: none;">
					<div class="form-group">
						<label>reAuction Date</label><input
							class="form-control reauctionDate" name="reauctionDate"
							id="reauctionDate" type="text">
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Stock No</label><input class="form-control" name="stockNo"
							id="stockNo" type="text" readonly="readonly">
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label>Chassis No</label><input class="form-control"
							name="chassisNo" id="chassisNo" type="text" readonly="readonly">
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label>Auction Company</label><select id="auctionCompany"
							name="auctionCompany" data-placeholder="Select Auction Company">
							<option value=""></option>
						</select>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
						<label>Auction House</label> <select id="auctionHouse"
							name="auctionHouse" data-placeholder="Select Auction House">
							<option value=""></option>
						</select>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- Stock Modal -->
	<div class="modal fade" id="modal-stock-details">
		<!-- /.modal-dialog -->
		<div class="modal-dialog modal-lg"
			style="min-width: 100%; margin: 0; display: block !important;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Stock Details</h4>
				</div>
				<div class="modal-body " id="modal-stock-details-body"></div>
			</div>
			<!-- /.modal-content -->
		</div>
	</div>
	<div id="cloneable-items">
		<div id="stock-details-html" class="hide">
			<div class="stock-details">
				<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
			</div>
		</div>
	</div>
	<!-- The Modal Image preview-->
	<div id="myModalImagePreview" class="modalPreviewImage modal"
		style="z-index: 1000000015">
		<span class="myModalImagePreviewClose">&times;</span> <img
			class="modal-content-img" id="imgPreview">
	</div>

	<div class="modal fade" id="modal-cancel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Cancel Stock</h3>
				</div>
				<div class="modal-body">
					<div class="box-body">
						<form action="#" id="cancel-form">
							<div class="container-fluid">
								<div class="row">
									<div class="form-group">
										<label class="required">Vehicle Remarks</label>
										<div class="element-wrapper">
											<textarea name="vechicleRemarks" id="vechicleRemarks"
												class="form-control location required"
												placeholder="Enter Vehicle Remarks"></textarea>
										</div>

										<span class="help-block"></span>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button id="add-remarks" class="btn btn-primary">
						<em class="fa fa-fw fa-save"></em> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<em class="fa fa-fw fa-close"></em> Close
					</button>
				</div>
			</div>
		</div>
	</div>


</section>
