
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Cancelled Invoices</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Invoice Booking&Approval</span></li>
		<li class="active">Cancelled Invoices</li>
	</ol>
</section>

<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
			<div class="box-header with-border">
				<div class="container-fluid">
					<div class="pull-right">
						<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Invoice Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-invoice-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group" id="date-form-group">
							<label>Cancelled Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-cancelled-date" placeholder="dd-mm-yyyy"
									readonly="readonly">
							</div>
						</div>
					</div>
					</div>
				</div>
			</div>
		</form>
		<div class="box-body">
			<div class="container-fluid ">
				<!-- Cancelled Invoice List show/search inputs -->
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
				<table id="table-cancelledInvoice-list"
					class="table table-bordered table-striped"
					style="width: 100%; overflow: scroll;">
					<thead>
						<tr>
							
							<th data-index="0" name="invoiceDate" class="align-center">Invoice Date</th>
							<th data-index="1" name="cancelledDate" class="align-center">Invoice Cancelled Date</th>
							<th data-index="2" name="invoiceNo" class="align-center">Invoice No</th>
							<th data-index="3" name="company" class="align-center">Remit To</th>
							<th data-index="4" name="invoiceAmount" class="align-center">Invoice Amount</th>
							<th data-index="5" name="refNo" class="align-center">Reference No</th>
							<th data-index="6" name="remarks" class="align-center">Remarks</th>
							<th data-index="7" name="cancellationRemarks" class="align-center">Cancellation Remarks</th>
						</tr>
					</thead>
					<tbody>
					<tr class="clone-row hide">
								
								<td class="dt-right invoiceAmount"><span
									class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</section>