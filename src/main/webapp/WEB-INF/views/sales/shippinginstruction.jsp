<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<sec:authorize
				access="hasRole('ROLE_SALES_ADMIN') or hasRole('ROLE_SALES_MANAGER') or hasRole('ADMIN')"
				var="isAdminManager"></sec:authorize>
<section class="content-header">
	<h1>Shipping Instruction</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Shipping Instruction</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-header with-border">
			<h3 class="box-title">Shipping Instruction List</h3>
		</div>
		<div class="box-body">
			<div class="row form-group">
				<input type="hidden" id="shipping-arranged-id"
					value="${shippingInstructionId }">
				<div class="col-md-3">
					<div class="form-group" id="date-form-group">
						<label>From To Date</label>
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control pull-right"
								id="table-filter-shipping-date"
								placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
						</div>
						<!-- /.input group -->
					</div>
				</div>
				<c:choose>
					<c:when test="${isAdminManager}">
					<div class="row form-group">
					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback pull-left">
					<input type="checkbox" id="showMine" name="showMine"
									 ><label
									class="ml-5">Own Ship Instrc. Only</label>
							
						</div>
					</div>
					</div>
					</c:when>
					</c:choose>
			</div>
			<!-- </div>     -->
		</div>
		<div class="container-fluid ">
			
			<c:choose>
				<c:when test="${isAdminManager}">
					<input type="hidden" value="1" name="isAdminManager">
				</c:when>
				<c:otherwise>
					<input type="hidden" value="0" name="isAdminManager">
				</c:otherwise>
			</c:choose>
			<div class="table-responsive">
				<table id="table-shipping-instruction"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							<th data-index="0" style="width: 10px"><input
								type="checkbox" id="select-all" /></th>
							<th data-index="1" class="align-center">Request Date</th>
							<th data-index="2" class="align-center">Stock No.</th>
							<th data-index="3" class="align-center">Chassis No.</th>
							<th data-index="4" class="align-center">Customer</th>
							<th data-index="5" class="align-center">Consignee</th>
							<th data-index="6" class="align-center">Notify Party</th>
							<th data-index="7" class="align-center">Instruction By</th>
							<th data-index="8" class="align-center">Country</th>
							<th data-index="9" class="align-center">Destination Port</th>
							<th data-index="10" class="align-center">Action</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
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
	<!-- /./. start Model -->
	<div class="modal fade" id="modal-shipping-user-details">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Update user details</h3>
				</div>
				<div class="modal-body">
					<form id="user-details-form">
						<div class="container-fluid">
							<div class="row">
								<input type="hidden" name="stockNo">
								<div class="col-md-3">
									<label class="required">User</label>
									<div class="form-group">
										<input class="form-control required" name="shippingUser"
											id="shippingUser" type="text"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">User Id</label>
									<div class="form-group">
										<input class="form-control required" name="shippingId"
											id="shippingId" type="text"><span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Tel</label>
									<div class="form-group">
										<input class="form-control required shippingTel"
											name="shippingTel" id="shippingTel" type="text"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Hs Code</label>
									<div class="form-group">
										<input class="form-control required" name="hsCode" id="hsCode"
											type="text"><span class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="userDetails-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
	<!-- /./. end Model /./-->
</section>