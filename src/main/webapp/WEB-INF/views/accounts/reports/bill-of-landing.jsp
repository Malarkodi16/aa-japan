<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Sri Lankan Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li>Reports</li>
		<li class="active">Sri Lankan Management</li>
	</ol>
</section>
<section class="content">
	<div class="container-fluid">
		<form action="${contextPath}/accounts/billoflanding/.pdf"
			method="POST">
			<div class="col-md-6 billofLanding">
				<div class="box box-solid">
					<div class="box-header"></div>
					<div class="box-body">
						<div class="col-md-12">
							<label>Shipper</label>
							<textarea rows="4" cols="3" class="form-control bof"
								name="shipper" readonly="readonly">ALAIN JAPAN (PVT) LTD
1-28-21 HAYABUCHI, TSUZUKI-KU, YOKOHAMA-SHI
KANAGAWA-KEN, JAPAN.
TEL: 045-594-05057, FAX:045-594-0508</textarea>
						</div>
						<div class="col-md-12">
							<label>Consignee(Complete Name and Address)</label>
							<textarea rows="2" class="form-control bof consignee"
								name="consignee"></textarea>
						</div>
						<div class="col-md-12">
							<label>Notify Party</label>
							<textarea rows="2" name="notifyParty"
								class="form-control bof notifyParty"></textarea>
						</div>
						<div class="col-md-6">
							<label>Pre-Carriage by</label> <input name="preCarriageBy"
								class="form-control bof" />
						</div>
						<div class="col-md-6">
							<label>Place of Receipt</label> <input type="text"
								name="placeOfReceipt" class="form-control bof" />
						</div>
						<div class="col-md-12">
							<label>Vessel/Voyage No.</label> <input type="text"
								name="vesselVoyage" class="form-control bof vesselVoyage" />
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6 billofLanding">
				<div class="box box-solid">
					<div class="box-header"></div>
					<div class="box-body">
						<div class="col-md-12">
							<label>Bill Of Landing No</label> <select name="billOfLandingNo"
								id="billOfLandingNo" class="form-control select2-select bof"
								style="width: 100%;" data-value=""
								data-placeholder="Select BlNo">
								<option value=""></option>
							</select>
							<!-- <input type="text" id="billOfLandingNo"
							 	name="billOfLandingNo" class="form-control bof" onchange="billOfLanding()" /> -->
						</div>
						<div class="col-md-12">
							<label>Booking No</label> <input type="text" name="bookingNo"
								class="form-control bof" />
						</div>
						<div class="col-md-12">
							<label>For Delivery of Goods Please Apply to</label>
							<textarea rows="9" cols="3" name="deliveryAddress"
								class="form-control bof"></textarea>
						</div>
						<div class="col-md-12">
							<label>Port of Loading</label> <input type="text"
								name="portOfLoading" class="form-control bof portOfLoading" />
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-12 billofLanding">
				<div class="box box-solid">
					<div class="box-body">
						<div class="col-md-4">
							<label>Port of Discharge</label> <input name="portOfDischarge"
								class="form-control bof portOfDischarge" />
						</div>
						<div class="col-md-4">
							<label>Port of Delivery</label> <input name="portOfDelivery"
								class="form-control bof portOfDelivery" />
						</div>
						<div class="col-md-4">
							<label>Final Destination</label> <input name="finalDestination"
								class="form-control bof finalDestination" />
						</div>
						<div class="col-md-12 table-responsive">
							<table class="table table-striped table-bordered">
								<thead>
									<tr>
										<th class="align-center" colspan="5"><h4>
												<b>Particulars Furnished by consignor / Shipper</b>
											</h4></th>
									</tr>
									<tr>
										<th>Container No. &amp; Seal No. Marks &amp; No.</th>
										<th>No &amp; Kinds of Containers or Packages</th>
										<th>Description of Goods</th>
										<th>Gross Weight(KGS)</th>
										<th>Measurement(CBM)</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><div class="col-md-16">
												<textarea rows="4" cols="3" class="form-control bof"
													name="containerNo" readonly="readonly">ALAIN JAPAN (PVT) LTD
1-28-21 HAYABUCHI, TSUZUKI-KU, YOKOHAMA-SHI
KANAGAWA-KEN, JAPAN.
TEL: 045-594-05057, 
FAX: 045-594-0508, 
Mobile: 0081-80-1094-0907</textarea>
											</div></td>

										<td><div class="col-md-16">
												<textarea rows="4" cols="3" class="form-control bof"
													name="noOfKinds"></textarea>
											</div></td>
										<td><div class="col-md-16">
												<textarea rows="4" cols="3" class="form-control bof"
													name="descriptionOfGoods"></textarea>
											</div></td>
										<td><div class="col-md-16">
												<input class="form-control bof" name="grossWeight" />
											</div></td>
										<td><div class="col-md-16">
												<input class="form-control bof" name="measurement" />
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="col-md-12">
							<div class="col-md-4">
								<label>Shipped On Board</label> <input type="text"
									class="form-control bof" name="shipOnBoard" />
							</div>
							<div class="col-md-4">
								<label>Shipped On Board Date</label> <input type="text"
									class="form-control bof datepicker" name="shipOnBoardDate" />
							</div>
							<div class="col-md-4">
								<label>Origin Port</label> <input type="text"
									class="form-control bof" name="originPort" />
							</div>
						</div>
						<div class="col-md-12">
							<div class="col-md-8">
								<label>Total Number of Containers or Packages(In Words)</label>
								<input type="text" class="form-control bof" name="noOfPackages" />
							</div>
							<div class="col-md-4">
								<label>Freight Payable at</label> <input type="text"
									class="form-control bof" name="freightPayableAt" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="col-md-6">
								<label>Freight &amp; Charges</label> <input type="text"
									class="form-control bof" name="freightCharges" />
							</div>
							<div class="col-md-3">
								<label>Prepaid</label> <input type="text"
									class="form-control bof" name="prepaid" />
							</div>
							<div class="col-md-3">
								<label>Collect</label> <input type="text"
									class="form-control bof" name="collect" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="col-md-6">
								<label>Place &amp; Date of Issue</label> <input type="text"
									class="form-control bof datepicker" name="placeAndDate" />
							</div>
							<div class="col-md-6">
								<label>No. of Original B/L</label> <input type="text"
									class="form-control bof" name="noOfOriginalBL" />
							</div>
						</div>
					</div>
					<div class="box-footer">
						<div class="pull-right">
							<button class="btn btn-primary" id="btn-save" type="submit">Save</button>
							<button class="btn btn-primary" type="button">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>