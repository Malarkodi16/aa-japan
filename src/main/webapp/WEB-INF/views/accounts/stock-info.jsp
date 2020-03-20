<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock Info</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Stock Info</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
	<input type="hidden" name="stockNo" value="${stockNo}"/>
		<div class="box-header with-border">
			<div class="container-fluid">
				<div class="row">
					<div class="form-group">
						<div class="col-md-3">
							<div class="form-group">
								<select class="form-control stockNo" id="stockNo"
									style="width: 100%;"
									data-placeholder="Search by Stock No. or Chassis No.">
									<option value=""></option>
								</select> <span class="help-block"></span>
							</div>
						</div>
						<div class="col-md-1">
							<button type="button" class="btn btn-primary pull-left"
								style="width: 100px" id="btn-search-stock">Search</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<h3 class="page-header">
			Images <span class="pull-right"><label
				style="font-size: 15px;">Download Images : </label> &nbsp <span
				style="font-size: 15px;" class="downloadImages"><a
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



		<div class="box-header with-border">
			<h4 class="modal-title"></h4>
		</div>
		<div class="container-fluid" id="stockInfoContainer">

			<div class="row">
				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Basic Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Stock No</th>
										<td><span id="stockNoValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Chassis No</th>
										<td><span id="chassisNo" class="word-break"> </span></td>
									</tr>

									<tr>
										<th>First Reg. Date</th>
										<td><span id="firstRegDate" class="word-break"></span></td>
									</tr>


									<tr>
										<th>Model Type</th>
										<td><span id="modelType" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Maker</th>
										<td><span id="maker" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Model</th>
										<td><span id="model" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Manufacture Year</th>
										<td><span id="manufactureYear" class="word-break"></span></td>
									</tr>

								</tbody>
							</table>
						</div>
					</div>
				</div>




				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Info And Extra Accessories</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Color</th>
										<td><span id="color" class="word-break"></span></td>
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
										<th>Driven</th>
										<td><span id="driven" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Equipment</th>
										<td><span id="equipment" class="word-break"></span></td>
									</tr>
									<tr>
										<th>Extra Accessories</th>
										<td><span id="extraAccessories" class="word-break"></span></td>
									</tr>

									<tr>
										<th>M3</th>
										<td><span id="m3" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>






				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Engine And Transmission</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>

									<tr>
										<th>CC</th>
										<td><span id="cc" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Fuel</th>
										<td><span id="fuel" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Mileage</th>
										<td><span id="mileage" class="word-break"></span></td>
									</tr>



									<tr>
										<th>Transmission</th>
										<td><span id="transmission" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Manual Types</th>
										<td><span id="manualTypes" class="word-break"> </span></td>
									</tr>

									<tr>
										<th>Number Plate</th>
										<td><span id="numberPlate" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Old NP</th>
										<td><span id="oldNumberPlate" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>





				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Extra Equipments</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Category</th>
										<td><span id="category" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Sub Category</th>
										<td><span id="subcategory" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Extra Equipments</th>
										<td><span id="extraEquipments" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Tank KiloLitre</th>
										<td><span id="tankKiloLitre" class="word-break">-</span></td>
									</tr>

									<tr>
										<th>Crane Type</th>
										<td><span id="craneType" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Crane Cut</th>
										<td><span id="craneCut" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Exel</th>
										<td><span id="exel" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Tyre Size</th>
										<td><span id="tyreSize" class="word-break"></span></td>
									</tr>

									<tr>

									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>


			</div>
		</div>


		<div class="container-fluid" id="stockInfoContainer">

			<div class="row">

				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Dimensions</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Length</th>
										<td><span id="length" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Width</th>
										<td><span id="width" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Height</th>
										<td><span id="height" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Weight</th>
										<td><span id="weight" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Grade</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Grade</th>
										<td><span id="grade" class="word-break">-</span></td>
									</tr>

									<tr>
										<th>Auction Grade Interior</th>
										<td><span id="auctionGrade" class="word-break">-</span></td>
									</tr>

									<tr>
										<th>Auction Grade Exterior</th>
										<td><span id="auctionGradeExt" class="word-break">-</span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Remarks</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Option Description</th>
										<td><span id="optionDescription" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Remarks</th>
										<td><span id="remarks" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Auction Remarks</th>
										<td><span id="auctionRemarks" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Vehicle Pricing</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Buying Price</th>
										<td><span id="buyingPrice" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Offer Price</th>
										<td><span id="offerPrice" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Min. Selling Price ( $ )</th>
										<td><span id="minSellingPriceInDollar" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Exchange Rate</th>
										<td><span id="exchangeRate" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="container-fluid" id="stockInfoContainer">

			<div class="row">

				<div class="col-md-4">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Auction Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Purchase Type</th>
										<td><span id="purchaseType" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Purchase Date</th>
										<td><span id="purchaseDate" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Shuppin No</th>
										<td><span id="lotNo" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Pos No</th>
										<td><span id="posNo" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Fob</th>
										<td><span id="fob" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Recycle</th>
										<td><span id="recycle" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Account</th>
										<td><span id="accountTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>IsPhoto Uploaded</th>
										<td><span id="isPhotoUploadedTextValue"
											class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="col-md-4">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Status Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Unit</th>
										<td><span id="unit" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Is Reserved</th>
										<td><span id="reserveTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Status</th>
										<td><span id="statusTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Lc Status</th>
										<td><span id="lcStatusTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Is Locked</th>
										<td><span id="isLockedTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Show For Sales</th>
										<td><span id="showForSalesTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>shippingInstStatusTextValue</th>
										<td><span id="shippingInstructionStatusTextValue"
											class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="col-md-4">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Stock Status</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Is Movable</th>
										<td><span id="isMovableTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Is Bidding</th>
										<td><span id="isBiddingTextValues" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Shipping Status</th>
										<td><span id="statusTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Inspection Status</th>
										<td><span id="inspectionStatusTextValue"
											class="word-break"></span></td>
									</tr>

									<tr>
										<th>Shipment Type</th>
										<td><span id="shipmentTypeTextValue" class="word-break"></span></td>
									</tr>

									<tr>
										<th>Transportation Status</th>
										<td><span id="transportationStatusTextValue"
											class="word-break"></span></td>
									</tr>

									<tr>
										<th>lockedBySalesPersonName</th>
										<td><span id="lockedBySalesPersonName" class="word-break"></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>


		<div class="box-body">
			<!-- table start -->
			<div class="container-fluid" id="stockInfoContainer">
				<div class="row basic-stock-details">



					<div class="col-md-12">
						<div class="row">
							<div class="col-md-4 purchase">
								<div class="box">
									<div class="box-header with-border">
										<h3 class="box-title">Purchase &amp; Invoice Info</h3>
									</div>
									<!-- /.box-header -->
									<div class="box-body">
										<table class="table table-bordered">
											<tbody>
												<tr>
													<th>Auction Company</th>
													<td><span id="auctionCompany">-</span></td>
												</tr>
												<tr>

													<th>Auction House</th>
													<td><span id="auctionHouse">-</span></td>
												</tr>
												<tr>

													<th>Invoice Status</th>
													<td><span id="invoiceStatus">-</span></td>
												</tr>
												<tr>
													<th>Invoice No</th>
													<td><span id="invoiceNo">-</span></td>
												</tr>
												<tr>
													<th>Ref No</th>
													<td><span id="refNo"></span></td>
												</tr>
												<tr>
													<th>Invoice Date</th>
													<td><span id="invoiceDate" class="word-break"></span>
												</tr>
												<tr>
													<th>Due Date</th>
													<td><span id="dueDate" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Uploaded Invoice</th>
													<td id="uploadedInvoice"><span
														class="word-break upload"></span></td>
												</tr>
											</tbody>
										</table>
									</div>
									<!-- /.box-body -->
								</div>
								<!--          #089992 -->
							</div>
							<div class="col-md-4 transport">
								<div class="box">
									<div class="box-header with-border">
										<h3 class="box-title">Transport Info</h3>
									</div>
									<!-- /.box-header -->
									<div class="box-body">
										<table class="table table-bordered">
											<tbody>
												<tr>
													<th>Transport Status</th>
													<td><span id="lastTransportStatus" class="word-break">-</span></td>
												</tr>
												<tr>
													<th>No Of Transport Arranged</th>
													<td><span id="noOfTransportArranged"
														class="word-break">-</span></td>
												</tr>
												<tr>
													<th>Invoice Status</th>
													<td><span id="lastTransportInvoiceStatus"
														class="word-break">-</span></td>
												</tr>
												<tr>
													<th>Invoice No</th>
													<td><span id="transportInvoiceRefNo"
														class="word-break"></span></td>
												</tr>
												<tr>
													<th>Ref No</th>
													<td><span id="transportRefNo" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Invoice Date</th>
													<td><span id="transportInvoiceDate" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Due Date</th>
													<td><span id="transportDueDate" class="word-break">-</span></td>
												</tr>
											</tbody>
										</table>
									</div>
									<!-- /.box-body -->

								</div>
							</div>

							<div class="col-md-4 inspection">
								<div class="box">
									<div class="box-header with-border">
										<h3 class="box-title">Inspection Info</h3>
									</div>
									<!-- /.box-header -->
									<div class="box-body">
										<table class="table table-bordered">
											<tbody>
												<tr>
													<th>Inspection Country</th>
													<td><span id="inspectionCountry" class="word-break">-</span></td>
												</tr>
												<tr>
													<th>Inspection Status</th>
													<td><span id="inspectionStatus" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Inspection Sent Date</th>
													<td><span id="inspectionSentDate" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Inspection Date</th>
													<td><span id="inspectionDate" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Inspection Doc Status</th>
													<td><span id="inspectionDocStatus" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Inspection Doc Sent Date</th>
													<td><span id="inspectionDocSentDate"
														class="word-break"></span></td>
												</tr>
												<tr>
													<th>Date Of Issue</th>
													<td><span id="dateOfIssue" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Certificate No</th>
													<td><span id="certificateNo" class="word-break"></span></td>
												</tr>
												<tr>
													<th>Inspection Company</th>
													<td><span id="inspectionCompany" class="word-break"></span></td>
												</tr>
											</tbody>
										</table>
									</div>
									<!-- /.box-body -->
								</div>
								<!-- /.col -->
							</div>
						</div>
					</div>


					<!-- /.col -->
					<!-- /.col -->
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
										<th>Transporter</th>
										<th>From Location</th>
										<th>To Location</th>
										<th>Transport Status</th>
										<th>Invoice Status</th>
										<th>Estimated Amount</th>
										<th>Actual Amount</th>
										<th>Invoice View</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class='sno'></td>
										<td class='transporter'></td>
										<td class='from'></td>
										<td class='to'></td>
										<td class='transportStatus'></td>
										<td class='invoiceStatus'></td>
										<td class='estimatedAmount'></td>
										<td class='actualAmount'></td>
										<td class='invoiceView'></td>
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
										<th>Forwarder/Inspection Company</th>
										<th>Dest Country</th>
										<th>Status</th>
										<th>Invoice Status</th>
										<th>Estimated Amount</th>
										<th>Actual Amount</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class='sno'></td>
										<td class='forwarder-inspection-company'></td>
										<td class='dest-country'></td>
										<td class='status'></td>
										<td class='inspectionInvoiceStatus'></td>
										<td class="estimatedAmount"></td>
										<td class="actualAmount"></td>
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
							<h3 class="box-title">Shipping Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body table-responsive">
							<table
								class="table table-condensed stkview table-bordered table-striped"
								id="shipping-info-container">
								<thead>
									<tr>
										<th style="width: 10px">#</th>
										<th>Forwarder</th>
										<th>Origin Port</th>
										<th>Destination Country</th>
										<th>Destination Port</th>
										<th>Destination Yard</th>
										<th>Type</th>
										<th>Vessel</th>
										<th>Shipping Status</th>
										<th>Invoice Status</th>
										<th>Container No</th>
										<th>Container Name</th>
										<th>Shipping BlNo</th>
										<th>SlaNo</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class='sno'></td>
										<td class='forwarder'></td>
										<td class='originPort'></td>
										<td class='destinationCountry'></td>
										<td class='destinationPort'></td>
										<td class="destinationYard"></td>
										<td class="type"></td>
										<td class="vessel"></td>
										<td class="shippingStatus"></td>
										<td class="invoiceStatus"></td>
										<td class="containerNo"></td>
										<td class="containerName"></td>
										<td class="shippingBlNo"></td>
										<td class="slaNo"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
				</div>

				<div class="row">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">Document Info</h3>
						</div>
						<div class="box-body">
							<table class="table table-bordered">
								<tbody>
									<tr>
										<th>Doc Received Status</th>
										<th>PlateNo Received Date</th>
										<th>Document Type</th>
										<th>Document Received Date</th>
										<th>Convert To</th>
										<th>Fob</th>
										<th>Converted Date</th>
										<th>HandOver To</th>
										<th>User Name</th>
										<th>Rikuji Status</th>
										<th>Rikuji Date</th>
										<th>RikujiRemarks</th>
									</tr>
									<tr>
										<td><span id="documentRecivedStatusTextValue"
											class="word-break">-</span></td>
										<td><span id="plateNoReceivedDate" class="word-break">-</span></td>
										<td><span id="documentTypeTextValue" class="word-break">-</span></td>
										<td><span id="documentReceivedDate" class="word-break">-</span></td>
										<td><span id="documentConvertToTextValue"
											class="word-break">-</span></td>
										<td><span id="documentFob" class="word-break">-</span></td>
										<td><span id="documentConvertedDate" class="word-break">-</span></td>
										<td><span id="handoverToTextValue" class="word-break">-</span></td>
										<td><span id="handoverToUserName" class="word-break">-</span></td>
										<td><span id="rikujiStatusValueText" class="word-break">-</span></td>
										<td><span id="rikujiUpdateToOneDate" class="word-break">-</span></td>
										<td><span id="rikujiRemarks" class="word-break">-</span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">Document Conversion Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body table-responsive">
							<table
								class="table table-condensed stkview table-bordered table-striped"
								id="document-conversion-info-container">
								<thead>
									<tr>
										<th style="width: 10px">#</th>
										<th>Convert To</th>
										<th>Export Certificate Status</th>
										<th>Shipping Company</th>
										<th>Inspection Company</th>
										<th>Original Sent/Not</th>
										<th>Email Sent/Not</th>
										<th>Original Received Status</th>
										<th>Handover To</th>
										<th>Sent Date</th>
										<th>Reauction Date</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class='sno'></td>
										<td class='docConvertToTextValue'></td>
										<td class='exportCertificateStatusTextValue'></td>
										<td class='shippingCompanyName'></td>
										<td class='inspectionCompanyName'></td>
										<td class="docOriginalSentTextValue"></td>
										<td class="docEmailSentTextValue"></td>
										<td class="docReceivedStatusTextValue"></td>
										<td class="handoverStatusTextValue"></td>
										<td class="docSendDate"></td>
										<td class="reauctionDate"></td>
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
							<h3 class="box-title">Sales Info</h3>
						</div>
						<!-- /.box-header -->
						<div class="box-body table-responsive">
							<table
								class="table table-condensed stkview table-bordered table-striped"
								id="sales-invoice-info-container">
								<thead>
									<tr>
										<th style="width: 10px">#</th>
										<th>Invoice No</th>
										<th>Customer Name</th>
										<th>Customer Type</th>
										<th>Consignee Name</th>
										<th>Notifyparty Name</th>
										<th>Currency Type</th>
										<th>Exchange Rate</th>
										<th>Total</th>
										<th>Status</th>
										<th>Sales Person</th>
										<th>Allocated</th>
										<th>Received</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class='sno'></td>
										<td class='invoiceNo'></td>
										<td class='customerName'></td>
										<td class='customerType'></td>
										<td class='consigneeName'></td>
										<td class="notifypartyName"></td>
										<td class="currencyTypeTextValue"></td>
										<td class="exchangeRate"></td>
										<td class="total"></td>
										<td class="statusTextValue"></td>
										<td class="salesPersonIdName"></td>
										<td class="amountAllocatted"></td>
										<td class="amountReceived"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
				</div>

			</div>
		</div>
	</div>
</section>