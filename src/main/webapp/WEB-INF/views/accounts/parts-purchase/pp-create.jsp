<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Parts Purchase Create</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span><em class=""></em> Invoice Booking &amp;
				Approval</span></li>
		<li><span><em class=""></em> Parts Purchase Customer</span></li>
		<li class="active"><a>create</a></li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<div class="box-body">
			<form id="parts-purchase-create-form">
				<div class="container-fluid" id="partsPurchase">
					<fieldset>
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Invoice No</label><input
										class="form-control required" name="invoiceNo" id="invoiceNo"
										type="text"><span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Invoice Date</label>
									<div class="input-group">
										<div class="input-group-addon">
											<i class="fa fa-calendar"></i>
										</div>
										<input type="text" class="form-control datepicker required"
											name="invoiceDate" placeholder="dd-mm-yyyy">
									</div>
									<span class="help-block"></span>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label class="required">Customer</label><select id="customerId"
										name="customerId" class="form-control required"
										data-placeholder="Select Customer" style="width: 100%;">
										<option value=""></option>
									</select><span class="help-block"></span>
								</div>
							</div>
						</div>
					</fieldset>
				</div>
				<div class="container-fluid">
					<div class="table-responsive" id="parts-item-table">
						<table class="table table-bordered table-striped"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th colspan="10" bgcolor="#3c8dbc" class="align-center">Parts
										Purchased</th>
								</tr>
								<tr>
									<th><label>S No</label></th>
									<th><label>Part No</label></th>
									<th><label>Part Description</label></th>
									<th><label>Unit Price</label></th>
									<th><label>Quantity</label></th>
									<th><label>Amount</label></th>
									<th><label>Tax Inc.</label></th>
									<th><label>Tax Amount</label></th>
									<th><label>Total</label></th>
									<th><label>Action</label></th>
								</tr>
							</thead>
							<tbody class="parts-row-clone-container">
								<tr class="clone-row hide">
									<td class="sno"><span></span></td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control required"
												name="partNo"><span class="help-block"></span>
										</div></td>
									<td><div class="element-wrapper">
											<textarea class="form-control" name="partDescription"></textarea>
										</div></td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control autonumber required amountCalc"
												name="unitPrice" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0"><span class="help-block"></span>
										</div></td>
									<td>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="quantity" data-v-min="0" data-m-dec="0">
										</div>
									</td>
									<td>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="amount" data-a-sign="¥ " data-v-min="0" data-m-dec="0" readonly="readonly">
										</div>
									</td>
									<td>
										<div class="element-wrapper">
											<input type="checkbox" class="taxInclusive"
												name="taxInclusive" />
										</div>
									</td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="taxAmount" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0" readonly="readonly">
										</div></td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="taxIncludedAmount" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0" readonly="readonly">
										</div></td>
									<td style="width: 150px"><button type="button"
											class="btn btn-primary btn-sm add-new-row" id="add-new-row">
											<i class="fa fa-plus"></i>
										</button>
										<button type="button"
											class="btn btn-danger btn-sm delete-item-row"
											id="delete-item-row">
											<i class="fa fa-close"></i>
										</button></td>
								</tr>
								<tr class="parts-item">
									<td class="sno"><span>1</span></td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control required"
												name="partNo"><span class="help-block"></span>
										</div></td>
									<td><div class="element-wrapper">
											<textarea class="form-control" name="partDescription"></textarea>
										</div></td>
									<td><div class="element-wrapper">
											<input type="text"
												class="form-control autonumber required amountCalc"
												name="unitPrice" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0"><span class="help-block"></span>
										</div></td>
									<td>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="quantity" data-v-min="0" data-m-dec="0">
										</div>
									</td>
									<td>
										<div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="amount" data-a-sign="¥ " data-v-min="0" data-m-dec="0" readonly="readonly">
										</div>
									</td>
									<td>
										<div class="element-wrapper">
											<input type="checkbox" class="taxInclusive"
												name="taxInclusive" />
										</div>
									</td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="taxAmount" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0" readonly="readonly">
										</div></td>
									<td><div class="element-wrapper">
											<input type="text" class="form-control autonumber amountCalc"
												name="taxIncludedAmount" data-a-sign="¥ " data-v-min="0"
												data-m-dec="0" readonly="readonly">
										</div></td>
									<td style="width: 150px"><button type="button"
											class="btn btn-primary btn-sm add-new-row" id="add-new-row">
											<i class="fa fa-plus"></i>
										</button>
										<button type="button"
											class="btn btn-danger btn-sm delete-item-row"
											id="delete-item-row">
											<i class="fa fa-close"></i>
										</button></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="box-footer ">
					<div class="pull-right">
						<button type="button" class="btn btn-primary"
							id="save-parts-purcahsed">
							<i class="fa fa-fw fa-save"></i> Save
						</button>
						<button type="reset" class="btn btn-primary "
							onclick="location.reload();" id="rest-form">Reset</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>