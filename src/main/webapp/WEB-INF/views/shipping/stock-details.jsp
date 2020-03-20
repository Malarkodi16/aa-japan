<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<div class="container-fluid" id="stockDetailsContainer">
	<input type="hidden" id="stockNo" name="stockNo" value="${stockNo}" />
	<sec:authorize access="hasAnyRole('ROLE_SHIPPING','ROLE_ADMIN')">
		<div class="row form-group">
			<div class="col-md-12 ">
				<a class="btn btn-info" id="btn-export-certificate" href="#">Export
					Certificate</a> <a class="btn btn-info pull-right"
					id="btn-search-stock" style="width: 100px" href="#">Edit</a>
			</div>
		</div>
	</sec:authorize>

	<div class="row">
		<div class="col-md-9">
			<div class="row">
				<div class="col-md-5">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Basic Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>

										<th>Stock Type</th>
										<td><span id="stockType">-</span></td>

									</tr>
									<tr>

										<th>Bidding No</th>
										<td><span id="biddingNo">-</span></td>

									</tr>
									<tr>

										<th>Chassis No</th>
										<td><span id="chassisNo">-</span></td>

									</tr>
									<tr>
										<th>Maker &amp; Model</th>
										<td><span id="maker-model">-</span></td>

									</tr>
									<tr>
										<th>Supplier</th>
										<td><span id="supplierName"></span></td>

									</tr>
									<!-- <tr>
                  <th>Auction House</th>
                  <td><span id="auctionHouse"></span></td>
                  
                </tr> -->
									<tr>
										<th>Dest Country/Port</th>
										<td><span id="destinationCountry" class="word-break"></span>/<span
											id="destinationPort" class="word-break"></span></td>

									</tr>
									<tr>
										<th>First Reg. Year</th>
										<td><span id="sFirstRegDate" class="word-break"></span></td>

									</tr>
									<tr>
										<th>Grade</th>
										<td><span id="grade" class="word-break"></span></td>

									</tr>
									<tr>
										<th>Exterior Auction Grade</th>
										<td><span id="auctionGradeExt" class="word-break"></span></td>

									</tr>
									<tr>
										<th>Interior Auction Grade</th>
										<td><span id="auctionGrade" class="word-break"></span></td>

									</tr>
									<tr>
										<th>Shipment Type</th>
										<td><span id="shipmentType" class="word-break"></span></td>

									</tr>
									
									<tr>
										<th>Auction Remarks</th>
										<td><span id="auctionRemarks" class="word-break"></span></td>

									</tr>
									
									<tr>
										<th>Remarks</th>
										<td><span id="remarks" class="word-break"></span></td>

									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->


					</div>
					<!--          #089992 -->
				</div>
				<div class="col-md-3 reserved">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Reserved Details</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>

										<th>Reserved By</th>
										<td><span id="reservedByName" class="word-break">-</span></td>

									</tr>
									<tr>
										<th>Reserved Date</th>
										<td><span id="reservedDate" class="word-break">-</span></td>

									</tr>
									<tr>
										<th>Reserved Customer</th>
										<td><span id="reservedCustomerName" class="word-break">-</span></td>

									</tr>
									<tr>
										<th>Reserved Price</th>
										<td><span id="reservedPrice"
											class="word-break autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Base Price</th>
										<td><span id="basePrice" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Manufacture Year</th>
										<td><span id="manufactureYear"
											class="word-break manufactureYear"></span></td>

									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->

					</div>
				</div>

				<div class="col-md-4 purchase">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Purchase Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Purchased Date</th>
										<td><span id="purchasedDate" class="word-break">-</span></td>

									</tr>
									<tr>

										<th>Purchase Price</th>
										<td><span id="purchaseCost" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Commission Amount</th>
										<td><span id="commision" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Recycle Amount</th>
										<td><span id="recycleAmount"
											class="word-break autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Road Tax</th>
										<td><span id="otherCharges" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Others</th>
										<td><span id="other-charge" class="word-break">¥ 0</span></td>

									</tr>
									<tr>
										<th>Total</th>
										<td><span id="total" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Total Tax</th>
										<td><span id="totalTax" class="word-break autonumber"
											data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>
									<tr>
										<th>Total with Tax</th>
										<td><span id="totalTaxIncluded"
											class="word-break autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>

									</tr>

								</tbody>
							</table>
						</div>
						<!-- /.box-body -->

					</div>
					<!-- /.col -->


				</div>
			</div>
			<div class="row">
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">Transport Info</h3>
						<span class="pull-right"><label style="font-size: 15px;">Current
								Location : </label><span id="currentLocation" style="font-size: 15px;">N/A</span></span>
					</div>
					<!-- /.box-header -->
					<div class="box-body no-padding">
						<table class="table table-condensed stkview"
							id="transport-info-container">
							<thead>
								<tr>
									<th>#</th>
									<th>From Location</th>
									<th>To Location</th>
									<th>Transporter</th>
									<th>Charge</th>
									<th>Arrival Date</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class='sno'></td>
									<td class='from'></td>
									<td class='to'></td>
									<td class=' transporter'></td>
									<td class=' charge'></td>
									<td class=' etd'></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- /.box-body -->
				</div>
			</div>
			<div class="row">
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">Inspection Info</h3>

					</div>
					<!-- /.box-header -->
					<div class="box-body no-padding">
						<table class="table table-condensed stkview"
							id="inspection-info-container">
							<thead>
								<tr>
									<th style="width: 10px">#</th>
									<th>Ins. sent date</th>
									<th>Country</th>
									<th>Ins. Date</th>
									<th>Forwarder/Inspection Company</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class=' sno' class="sno"></td>
									<td class=' sendDate' class="from"></td>
									<td class=' country' class="to"></td>
									<td class=' inspectionDate' class="transporter"></td>
									<td class=' forwarder-inspection-company' class="charge"></td>
									<td class="etd"></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- /.box-body -->
				</div>
			</div>
		</div>
		<div></div>

		<div class="col-md-3">
			<div class="box">
				<div class="box-header with-border">
					<h3 class="box-title">Vehicle Info</h3>
				</div>
				<!-- /.box-header -->
				<div class="box-body">
					<table class="table table-bordered">
						<tbody>
							<tr>

								<th>Transmission</th>
								<td><span id="transmission" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Manual Types</th>
								<td><span id="manualTypes" class="word-break"> </span></td>

							</tr>
							<tr>
								<th>No. of Seat</th>
								<td><span id="noOfSeat" class="word-break"></span></td>

							</tr>
							<tr>
								<th>No. of Door</th>
								<td><span id="noOfDoors" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Fuel</th>
								<td><span id="fuel" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Driven</th>
								<td><span id="driven" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Mileage</th>
								<td><span id="mileage" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Color</th>
								<td><span id="color" class="word-break"></span></td>

							</tr>
							<tr>
								<th>CC</th>
								<td><span id="cc" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Recycle</th>
								<td><span id="recycle" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Number Plate</th>
								<td><span id="numberPlate" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Old NP</th>
								<td><span id="oldNumberPlate" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Equipment</th>
								<td><span id="equipment" class="word-break"></span></td>

							</tr>
							<tr>
								<th>Extra Accessories</th>
								<td><span id="extraAccessories" class="word-break">-</span></td>

							</tr>

						</tbody>
					</table>
				</div>
				<!-- /.box-body -->

			</div>
			<!-- /.col -->


		</div>
		<!-- /.col -->
		<!-- /.col -->
	</div>
</div>







<h3 class="page-header">
	Images <span class="pull-right"><label style="font-size: 15px;">Download
			Images : </label> &nbsp <span style="font-size: 15px;" class="downloadImages"><a
			type="button" class="btn btn-primary ml-5 btn-xs"
			title="Download Images" id="downloadImages"><i
				class="fa fa-download"></i></a></span></span>
</h3>

<!-- Attachment -->
<div class="row">
	<div class="col-md-12">
		<div class="carousel slide" id="stock-images-carousel">
			<div class="carousel-inner">
				<div class="item active" data-index="0">
					<div class="col-md-4">
						<a href="#"><img
							src="${contextPath}/resources/assets/images/no-image-icon.png"
							height="500" width="500" class="img-responsive"
							alt="stock images"></a>
					</div>
				</div>
				<div class="item" data-index="1">
					<div class="col-md-4">
						<a href="#"><img
							src="${contextPath}/resources/assets/images/no-image-icon.png"
							height="500" width="500" class="img-responsive"
							alt="stock images"></a>
					</div>
				</div>
				<div class="item" data-index="2">
					<div class="col-md-4">
						<a href="#"><img
							src="${contextPath}/resources/assets/images/no-image-icon.png"
							height="500" width="500" class="img-responsive"
							alt="stock images"></a>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#stock-images-carousel"
				data-slide="prev"><i class="glyphicon glyphicon-chevron-left"></i></a>
			<a class="right carousel-control" href="#stock-images-carousel"
				data-slide="next"><i class="glyphicon glyphicon-chevron-right"></i></a>
		</div>
	</div>
</div>

<div id="clonable-items">
	<div id="carousel-item" class="hidden">
		<div class="item" data-index="">
			<div class="col-md-4">
				<a href="#"><img src="" height="500" width="500"
					class="img-responsive" alt="stock images"></a>
			</div>
		</div>
	</div>
</div>
<!-- The Modal -->

