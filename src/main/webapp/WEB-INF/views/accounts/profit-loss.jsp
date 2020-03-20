<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>Profit Or Loss</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Profit/loss</li>
	</ol>
</section>
<section class="content">

	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<!-- <div class="row">
					<div class="col-md-3">
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
					<div class="col-md-3">
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
				</div> -->

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
					<table id="table-profit-loss"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px">#</th>
								<th data-index="1" class="align-center">Stock No</th>
								<th data-index="2" class="align-center">Chassis No</th>
								<th data-index="3" class="align-center">Stock Type</th>
								<th data-index="4" class="align-center">Customer Name</th>
								<th data-index="5" class="align-center">Purchase Cost</th>
								<th data-index="6" class="align-center">Purchase Tax</th>
								<th data-index="7" class="align-center">Commission</th>
								<th data-index="8" class="align-center">Commission Tax</th>
								<th data-index="9" class="align-center">Road Tax</th>
								<th data-index="10" class="align-center">Recycle</th>
								<th data-index="11" class="align-center">Other Charges</th>
								<th data-index="12" class="align-center">Other Tax</th>
								<th data-index="13" class="align-center">Sold Price</th>
								<th data-index="14" class="align-center">Sold Date</th>
								<th data-index="15" class="align-center">Shuppin Commission</th>
								<th data-index="16" class="align-center">Shuppin Tax</th>
								<th data-index="17" class="align-center">Sold Commission</th>
								<th data-index="18" class="align-center">Sold Tax</th>
								<th data-index="19" class="align-center">Recycle Claimed</th>
								<th data-index="20" class="align-center">Others</th>
								<th data-index="21" class="align-center">Total</th>
								<th data-index="22" class="align-center">Sold Total</th>
								<th data-index="23" class="align-center">Profit/loss</th>
								<th data-index="24" class="align-center">Profit/loss Amt</th>
								<th data-index="25" class="align-center">Profit/loss per.</th>

								<!-- <th>Action</th> -->
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
						<tr>
							<th colspan="5" style="text-align: right"></th>
							<th>Purchase Cost Total</th>
							<th>Purchase Tax Total</th>
							<th>Commission Total</th>
							<th>Commission Tax Total</th>
							<th>Road Tax Total</th>
							<th>Recycle Total</th>
							<th>Other Charges Total</th>
							<th>Other Tax Total</th>
							<th>Sold Price Total</th>
							<th></th>
							<th>Shuppin Commission Total</th>
							<th>Shuppin Tax Total</th>
							<th>Sold Commission Total</th>
							<th>Sold Tax Total</th>
							<th>Recycle Claimed Total</th>
							<th>Others Total</th>
							<th>Total</th>
							<th>Sold Total</th>
							<th></th>
							<th>Profit/loss Amt Total</th>
							<th></th>
						</tr>
						<tr class=sum>
							<th colspan="5" style="text-align: right"></th>
							<th class="dt-right"><span
								class="autonumber pagetotal purchaseCostTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal purchaseTaxTotal" data-a-sign="¥ "
								data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal commissionTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal commisionTaxTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal roadTaxTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal recycleTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal otherChargesTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal otherTaxTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal soldPriceTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>

							<th></th>
							<th class="dt-right"><span
								class="autonumber pagetotal shuppinCommissionTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal shuppinTaxTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal soldCommissionTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal soldTaxTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th class="dt-right"><span
								class="autonumber pagetotal recycleClaimedTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
								class="autonumber pagetotal othersTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
								class="autonumber pagetotal total"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span
								class="autonumber pagetotal soldTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th></th>
							<th class="dt-right"><span
								class="autonumber pagetotal profitLossTotal"
								data-a-sign="¥ " data-m-dec="0">0</span></th>
							<th></th>
						</tr>
					</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>