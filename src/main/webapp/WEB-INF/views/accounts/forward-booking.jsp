<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Forward Booking</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active"><a>Forward Booking</a></li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">

					<div class="col-md-1 form-inline">
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
					<div class="col-md-4 pull-right">
						<div class="btn-group pull-right">
							<button type="button" class="btn btn-primary" data-toggle="modal"
								data-backdrop="static" data-target="#add-booking">
								<i class="fa fa-fw fa-plus"></i>Add Booking
							</button>
						</div>
					</div>

				</div>
				<div class="table-responsive">
					<!-- /. DataTable Start -->
					<table id="table-forward-booking"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px">Booking Date</th>
								<th data-index="1" class="align-center">Bank</th>
								<th data-index="2" class="align-center">Closing Date</th>
								<th data-index="3" class="align-center">Currency</th>
								<th data-index="4" class="align-center">Amount</th>
								<th data-index="6" class="align-center">Current ER</th>
								<th data-index="7" class="align-center">Booking ER</th>
								<!-- <th data-index="8" class="align-center">Action</th> -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /. DataTable End -->
		</div>
	</div>
	<!-- Model -->
	<div class="modal fade" id="add-booking">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title bank-balance pull-right hidden">
						Current Balance : <span class="amount" data-a-sign="¥ "
							data-m-dec="0"></span>
					</h4>
					<h3 class="modal-title">Forward Booking</h3>
				</div>
				<div class="modal-body">
					<form id="forward-booking-form">
						<div class="container-fluid item">
							<div class="row">
								<div class="col-md-3 ">
									<div class="form-group">
										<label class="required">Booking Date</label>
										<div class="element-wrapper">
											<input type="text" id="bookingDate" name="bookingDate"
												class="form-control datepicker" placeholder="Booking Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<label class="required">Bank</label>
									<div class="form-group">
										<select name="bank" id="bank"
											class="form-control required select2-select bank"
											data-placeholder="Select Bank"><option value=""></option></select><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<div class="form-group">
										<label class="required">Closing Date</label>
										<div class="element-wrapper">
											<input type="text" id="closingDate" name="closingDate"
												class="form-control datepicker" placeholder="Closing Date">
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Currency</label>
									<div class="form-group">
										<select name="currency" id="currency"
											class="form-control required select2-select currency"
											data-placeholder="Select Curency"><option value=""></option></select><span
											class="help-block"></span>
									</div>
								</div>
							</div>
							<div class="row">
								
								<div class="col-md-3">
									<label class="required">Amount</label>
									<div class="form-group">
										<input class="form-control required autonumeric" name="amount"
											id="amount" type="text" data-v-min="0" data-m-dec="0"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<label class="required">Current ExchangeRate</label>
									<div class="form-group">
										<input class="form-control required autonumeric"
											name="currentExchangeRate" id="currentExchangeRate"
											type="text" data-v-min="0" readonly="readonly"><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<label class="required">Booking ExchangeRate</label>
									<div class="form-group">
										<input class="form-control required autonumeric"
											name="bookingExchangeRate" id="bookingExchangeRate"
											type="text" data-v-min="0" data-m-dec="0"><span
											class="help-block"></span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="forward-booking-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>
</section>