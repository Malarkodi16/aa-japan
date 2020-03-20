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
	<div class="box-body">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">

						<label>Purchase Date</label>
						<div class="input-group date">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>
							<input type="text" class="form-control datepicker"
								id="datepicker">
						</div>
					</div>
				</div>


				<div class="form-group col-md-3">
					<label>Stock No</label> <input type="text" class="form-control"
						placeholder="Stock No">

				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label>Customer</label> <select class="form-control" id="customer">

							<option></option>
							<option>Customer 1</option>
							<option>Customer 2</option>
							<option>Customer 3</option>
						</select>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label>Sales Staff</label> <select class="form-control"
							id="customer">

							<option></option>
							<option>Sales Staff 1</option>
							<option>Sales Staff 2</option>
							<option>Sales Staff 3</option>
						</select>
					</div>
				</div>
				<div class="form-group col-md-3">
					<label>Chassis No</label> <input type="text" class="form-control"
						placeholder="Chassis No">

				</div>



				<div class="col-md-3">
					<div class="form-group">
						<label>Sold From Date:</label>

						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>

							<input type="text" class="form-control pull-right datepicker"
								id="datepicker" placeholder="Select Date ">
						</div>
						<!-- /.input group -->
					</div>
				</div>

				<div class="col-md-3">
					<div class="form-group">
						<label>Sold To Date:</label>

						<div class="input-group">
							<div class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</div>

							<input type="text" class="form-control pull-right datepicker"
								id="datepicker" placeholder="Select Date ">
						</div>
						<!-- /.input group -->
					</div>
				</div>
			</div>

		</div>
	</div>
	<div class="box-footer">
		<div class="pull-right">
			<button type="button" id="btn-apply-filter" class="btn btn-primary">Search</button>
			<button type="button" id="btn-reset-filter" class="btn btn-primary">Reset</button>
		</div>
	</div>
	<!-- /.box-body -->
</div>
