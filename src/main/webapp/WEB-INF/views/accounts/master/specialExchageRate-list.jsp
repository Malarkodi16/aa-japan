<section class="content-header">
	<h1>Special Exchange Rate Management</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master Data</span></li>
		<li class="active">Special Exchange Rate</li>
	</ol>
</section>

<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<form method="post" id="special-exchange-form" action="${contextPath}/accounts/master/specialExchageRate/create">
			<div class="box-body">
				<div class="container-fluid">
					<div class="row form-group">
						<div class="col-md-2" style="text-align: center;">
							<label></label>
						</div>


						<div class="col-md-4" style="text-align: center;">
							<label>Sales Exchange Rate</label>
						</div>

						<div class="col-md-4" style="text-align: center;">
							<label>Special Exchange Rate</label>
						</div>


					</div>

					<div class="row form-group">
						<div class="col-md-2">
							<label>US Dollar</label>
						</div>


						<div class="col-md-4">
							<input type="text" class="form-control autonumeric us"
								data-a-sign="$ " data-v-min="0" data-m-dec="2"
								name="usSalesExchangeRate" id="us_currency_sales_exchange_rate"
								placeholder="$ Sales Rate" required>
								<span class="help-block"></span>
						</div>

						<div class="col-md-4">
							<input type="text" class="form-control autonumeric us"
								data-a-sign="$ " data-v-min="0" data-m-dec="2"
								name="usSpecialExchangeRate"
								id="us_currency_special_exchange_rate"
								placeholder="$ Special Rate" required>
								<span class="help-block"></span>
						</div>


					</div>

					<div class="row form-group">
						<div class="col-md-2">
							<label>Australian Dollar</label>
						</div>


						<div class="col-md-4">
							<input type="text" class="form-control autonumeric american"
								data-a-sign="A$ " data-v-min="0" data-m-dec="2"
								name="auSalesExchangeRate" id="au_currency_sales_exchange_rate"
								placeholder="A$ Sales Rate" required>
								<span class="help-block"></span>
						</div>

						<div class="col-md-4">
							<input type="text" class="form-control autonumeric american"
								data-a-sign="A$ " data-v-min="0" data-m-dec="2"
								name="auSpecialExchangeRate"
								id="au_currency_special_exchange_rate"
								placeholder="A$ Special Rate" required>
								<span class="help-block"></span>
						</div>


					</div>

					<div class="row form-group">
						<div class="col-md-2">
							<label>Pound Rate</label>
						</div>


						<div class="col-md-4">
							<input type="text" class="form-control autonumeric pound"
								data-a-sign="£ " data-v-min="0" data-m-dec="2"
								name="poundSalesExchangeRate"
								id="pound_currency_sales_exchange_rate_3"
								placeholder="£ Sales Rate" required>
								<span class="help-block"></span>
						</div>

						<div class="col-md-4">
							<input type="text" class="form-control autonumeric pound"
								data-a-sign="£ " data-v-min="0" data-m-dec="2"
								name="poundSpecialExchangeRate"
								id="pound_currency_special_exchange_rate_3"
								placeholder="£ Special Rate" required>
								<span class="help-block"></span>
						</div>


					</div>
				</div>
			</div>
			<div class="box-footer">
				<div class="pull-right">
					<button type="button" class="btn btn-primary" id="saveExchangeRate">Save</button>
					<button type="reset" class="btn btn-default" id="cancel">Cancel</button>
				</div>
			</div>
		</form>
	</div>
</section>