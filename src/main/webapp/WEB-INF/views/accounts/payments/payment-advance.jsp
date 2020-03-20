<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Advance And PrePayments</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Payment</span></li>
		<li><span>Payment Booking</span></li>
		<li class="active"><a>Advance And PrePayments</a></li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<%-- <jsp:include page="/WEB-INF/views/accounts/auction-icons.jsp" /> --%>
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
								data-backdrop="static" data-target="#add-payment">
								<i class="fa fa-fw fa-plus"></i>Add Payment
							</button>
						</div>
					</div>

				</div>
				
					<div style="text-align: center;" class="form-group"
						id="radioShowTable_div">
						<label> <input name="radioShowTable" type="radio"
							class="minimal" value="0" checked>&nbsp;&nbsp;BOOKING
						</label> <label class="ml-5"> <input name="radioShowTable"
							type="radio" class="minimal" value="1">&nbsp;&nbsp;APPROVED
						</label> <label class="ml-5"> <input name="radioShowTable"
							type="radio" class="minimal" value="2">&nbsp;&nbsp;COMPLETED
						</label>
					</div>
				
				<div class="table-responsive">
					<!-- /. DataTable Start -->
					<table id="table-advance-prepayment"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<!-- <th data-index="0"><input type="checkbox" id="select-all" /></th> -->
								<th data-index="0" style="width: 10px">#</th>
								<th data-index="1" class="align-center">Date</th>
								<th data-index="2" class="align-center">Payment Type</th>
								<th data-index="3" class="align-center">Remit Type</th>
								<th data-index="4" class="align-center">Remit To</th>
								<th data-index="6" class="align-center">Amount</th>
								<th data-index="7" class="align-center">Action</th>
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
	<div class="modal fade" id="add-payment">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title">Advance And Pre Payment</h3>
				</div>
				<div class="modal-body">
					<form id="add-payment-detail-form">
						<div class="container-fluid">
							<div class="row">
								<div style="text-align: center;" class="form-group"
									id="advance-and-prepayment">
									<label> <input id="paymentType" name="paymentType"
										type="radio" class="minimal" value="0" checked>&nbsp;&nbsp;Advance
									</label> <label class="ml-5"> <input name="paymentType"
										type="radio" class="minimal" value="1">&nbsp;&nbsp;Pre
										Payment
									</label>
								</div>
							</div>
							<div class="row">
								<div class="col-md-3 ">
									<label class="required">Remitter Type</label>
									<div class="form-group">
										<select id="remitterType" name="remitterType"
											class="form-control required"
											data-placeholder="Select Remitter Type" style="width: 100%;">
											<option value="SUPPLIER">SUPPLIER</option>
											<option value="FORWARDER">FORWARDER</option>
											<option value="TRANSPORTER">TRANSPORTER</option>
										</select> <span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<label class="required">Remitter</label>
									<div class="form-group">
										<select name="remitter" id="remitter"
											class="form-control required"
											data-placeholder="Select Remitter"><option value=""></option></select><span
											class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3 ">
									<label class="required">Amount</label>
									<div class="form-group">
										<input class="form-control required amount" name="amount"
											id="amount" type="text" data-a-sign="¥ " data-v-min="0"
											data-m-dec="0"><span class="help-block"></span>
									</div>
								</div>

							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="charge-save" class="btn btn-primary">
						<i class="fa fa-fw fa-save"></i> Save
					</button>
					<button id="btn-close" data-dismiss="modal" class="btn btn-primary">
						<i class="fa fa-fw fa-close"></i> Close
					</button>
				</div>
			</div>
		</div>
	</div>


	<!-- /.modal -->
	<!-- Model -->
	<div class="modal fade" id="modal-approve-payment">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<!-- <button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button> -->
					<h4 class="modal-title bank-balance pull-right hidden">
						Current Balance : <span class="amount" data-a-sign="¥ "
							data-m-dec="0"></span>
					</h4>
					<h4 class="modal-title">Payment Details</h4>
				</div>
				<div class="modal-body">
					<form action="#" id="payment-detail-form">
						<div class="container-fluid" id="payment-container">
							<div class="row">
								<input type="hidden" name="selRec" id="selRec"
									class="form-control required " /> <input type="hidden"
									name="flag" id="flag" class="form-control required " />
								<div class="col-md-3">
									<label class="required">Bank</label> <select name="bank"
										id="bank" data-placeholder="Select Bank"
										class="form-control select2 bank" style="width: 100%">
										<option value=""></option>
										<c:forEach items="${mbank}" var="item">
											<option value="${item.bankSeq}">${item.bankName}</option>
										</c:forEach>
									</select> <span class="help-block"></span>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Payment Date</label>
										<div class="input-group">
											<div class="input-group-addon">
												<i class="fa fa-fw fa-calendar"></i>
											</div>
											<input type="text" name="approvedDate" id="approvedDate"
												class="form-control required entryDate datepicker"
												placeholder="dd-mm-yyyy" readonly="readonly" />
										</div>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Remarks</label>
										<textarea name="remarks" id="remarks" class="form-control"
											placeholder="Enter.."></textarea>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="approve">
							<i class="fa fa-fw fa-save"></i>Approve Payment
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>


</section>