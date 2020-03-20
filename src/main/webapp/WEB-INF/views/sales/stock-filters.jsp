<div class="box box-solid">
	<div class="box-header with-border" data-widget="collapse">
		<h3 class="box-title">Basic Search</h3>
		<div class="box-tools pull-right">
			<button type="button" class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header -->
	<div class="box-body" id="filter-container">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-2">
					<div class="form-group" id="date-form-group">
						<label for="purchaseDate">Purchase Date</label>
						<!-- 						<div class="col-md-9"> -->
						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control" id="purchaseDate"
								placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
						</div>
						<input type="hidden" class="form-control" name="purchaseDateFrom"
							id="purchaseDateFrom" /> <input type="hidden"
							class="form-control" name="purchaseDateTo" id="purchaseDateTo" />

					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="destinationCountry">Country</label> <select
							class="form-control" name="destinationCountry"
							id="destinationCountry" data-placeholder="All">
							<option value=""></option>
						</select>

					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="maker">Maker</label> <select class="form-control"
							name="makers[]" data-value="${maker}" id="makers"
							data-placeholder="All" multiple>
							<option value=""></option>
						</select>

					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<label for="model">Model</label> <select class="form-control"
							name="models[]" id="models" data-value="${model}"
							data-placeholder="All" multiple>
							<option value=""></option>
						</select>

					</div>
				</div>
				<%-- <div class="col-md-2">
					<div class="form-group">
						<label for="model">Sub Model</label> <select class="form-control"
							name="subModels[]" id="subModels" data-value="${subModel}"
							data-placeholder="All" multiple>
							<option value=""></option>
						</select>

					</div>
				</div> --%>
				<div class="col-md-2">
					<div class="form-group">
						<label for="modelType">Model Type</label> <select
							class="form-control" name="modelTypes[]" id="modelTypes"
							data-placeholder="All" multiple>
							<option value=""></option>
						</select>

					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2">
					<div class="form-group">
						<label for="yearMin">Year</label>

						<div class="form-inline">
							<input type="text" class="form-control year-picker autonumber"
								data-v-min="0" data-v-max="9999" data-a-sep="" name="yearMin"
								id="yearMin" placeholder="From" style="width: 90px"> <input
								type="text" class="form-control year-picker autonumber"
								name="yearMax" data-v-min="0" data-v-max="9999" data-a-sep=""
								id="yearMax" placeholder="To" style="width: 90px">
						</div>

					</div>
				</div>
			</div>
			<div class="row form-group"></div>
		</div>
		<div class="box box-widget">
			<div class="box-header with-border" data-widget="collapse">
				<h3 class="box-title">Advanced Search</h3>
				<div class="box-tools pull-right">
					<button type="button" class="btn btn-box-tool"
						data-widget="collapse">
						<i class="fa fa-minus"></i>
					</button>
				</div>
				<!-- /.box-tools -->
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<div class="row ">
					<div class="col-md-2">
						<div class="form-group">
							<label for="vehicle">Vehicle Category</label>
							<!-- 							<div class="col-md-9"> -->
							<select id="vehicleCategories" name="vehicleCategories[]"
								data-value="${category}" class="form-control select2"
								style="width: 100%;" data-placeholder="All" multiple="multiple">
								<option value=""></option>
							</select>
							<!-- 							</div> -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="color">Colors</label> <select name="colors[]"
								id="colors" class="form-control select2" style="width: 100%;"
								data-placeholder="All" multiple="multiple">
								<option value=""></option>
							</select>

						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="transmission">Transmission</label> <select
								class="form-control" name="transmissions[]" id="transmissions"
								data-placeholder="All" multiple>
								<option value=""></option>
							</select>

						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="milageMin">Mileage</label>
							<div class="form-inline">
								<input type="text" class="form-control autonumber"
									name="mileageMin" id="milageMin" placeholder="Min"
									style="width: 90px;" data-a-sep="," data-m-dec="0"> <input
									type="text" class="form-control autonumber" name="mileageMax"
									id="milageMax" placeholder="Max" style="width: 90px;"
									data-a-sep="," data-m-dec="0">
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="milageMin">CC</label>
							<div class="form-inline">
								<input type="text" class="form-control autonumber" name="ccMin"
									id="ccMin" placeholder="Min" data-a-sep="," data-m-dec="0"
									style="width: 90px;"> <input type="text"
									class="form-control autonumber" name="ccMax" id="ccMax"
									placeholder="Max" data-a-sep="," data-m-dec="0"
									style="width: 90px;">
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="auctionGrade">Grade</label> <select
								class="form-control" name="grades[]" id="grades"
								data-placeholder="All" multiple>
								<option value=""></option>
							</select>

						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-2">
						<div class="form-group">
							<label for="options">Options</label> <select
								class="form-control select2" name="options[]" id="options"
								style="width: 100%;" data-placeholder="All" multiple="multiple">
								<option value=""></option>
								<option value="A/C">A/C</option>
								<option value="P/S">P/S</option>
								<option value="P/W">P/W</option>
								<option value="S/R">S/R</option>
								<option value="A/W">A/W</option>
								<option value="ABS">ABS</option>
								<option value="AIR BAG">AIR BAG</option>
								<option value="4WD">4WD</option>
								<option value="P/M">P/M</option>
								<option value="TV">TV</option>
								<option value="PD">PD</option>
								<option value="NV">NV</option>
								<option value=R/S>R/S</option>
								<option value="F/LAMP">F/LAMP</option>
								<option value="CD">CD</option>
								<option value="LEATHER SEAT">LEATHER SEAT</option>

							</select>

						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="driven">Driven</label> <select class="form-control"
								name="driven">
								<option value="">All</option>
								<option value="Left">Left</option>
								<option value="Right">Right</option>
							</select>

						</div>
					</div>
					<!-- <div class="col-md-2">
						<div class="form-group">
							<label for="auctionGrade">Auction Grade</label>
							
							<select class="form-control" name="auctionGrades[]" id="auctionGrades"
								data-placeholder="All" multiple="multiple">
								
							</select>
							
						</div>
					</div> -->
					<div class="col-md-2">
						<div class="form-group">
							<label for="fob">FOB Price</label>
							<!-- 							<div class="col-md-4"> -->
							<div class="form-inline">
								<input type="text" class="form-control autonumber" name="fobMin"
									id="fobMin" placeholder="Min" style="width: 90px"
									data-a-sep="," data-m-dec="0">
								<!-- 							</div> -->
								<!-- 							<div class="col-md-4"> -->
								<input type="text" class="form-control autonumber" name="fobMax"
									id="fobMax" placeholder="Max" style="width: 90px"
									data-a-sep="," data-m-dec="0">
							</div>
							<!-- 							</div> -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="bidNo">Shuppin No.</label>
							<!-- 							<div class="col-md-9"> -->
							<select class="form-control" name="lotNos[]" id="lotNos"
								data-placeholder="All" multiple>
								<option value=""></option>
							</select>
							<!-- 							</div> -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="priceMin" class="priceMin">Purchased Price</label>
							<!-- 							<div class="col-md-4"> -->
							<div class="form-inline">
								<input type="text" class="form-control autonumber"
									name="priceMin" id="priceMin" placeholder="Min"
									style="width: 90px" data-a-sep="," data-m-dec="0">
								<!-- 							</div> -->
								<!-- 							<div class="col-md-4"> -->
								<input type="text" class="form-control autonumber"
									name="priceMax" id="priceMax" data-a-sep="," data-m-dec="0"
									placeholder="Max" style="width: 90px">
							</div>
							<!-- 							</div> -->
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="stockNo">Stock No.</label> <select
								class="form-control" name="stockNos[]" id="stockNos"
								data-placeholder="All" multiple>
								<option value=""></option>
							</select>

						</div>
					</div>
				</div>
				<div class="row"></div>
			</div>
			<!-- /.box-body -->
		</div>
	</div>
	<div class="box-footer">
		<small class="ml-5">*Type and press enter for select multiple
			values</small>
		<div class="pull-right">
			<button type="button" id="btn-apply-filter" class="btn btn-primary">Apply</button>
			<button type="button" id="btn-reset-filter" class="btn btn-primary"
				onclick="location.reload();">Reset</button>
		</div>
	</div>
	<!-- /.box-body -->
</div>
