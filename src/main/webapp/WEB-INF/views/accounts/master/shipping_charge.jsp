<section class="content-header">
	<h1>Create Shipping Charge</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Shipping Charge</li>
	</ol>
</section>
<section class="content">
	<div class="box box-solid">
		<form id="create-ship-charge">
			<div class="box-body">
				<div class="container-fluid">
					<div class="row form-group">
						<div class="col-md-2">
							<label>Origin Country</label> <select
								class="select2-select form-control country-dropdown"
								id="originCountry" name="originCountry"
								data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
						<div class="col-md-2">
							<label>Destination Country</label> <select
								class=" select2-select form-control country-dropdown"
								id="destCountry" name="destCountry"
								data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
						
						<div class="col-md-2">
							<label>M3 From</label> <input type="text"
								name="m3From" class="form-control" />
						</div>
						<div class="col-md-2">
							<label>M3 To</label> <input type="text" name="m3To"
								class="form-control" />
						</div>
						<div class="col-md-2">
								<div class="form-group">
									<label class="required">Amount</label><div class="element-wrapper"><input type="text"
										name="amount" id="amount" data-validation="number"
										class="form-control required autonumber" data-m-dec="0" data-a-sign="¥ " /></div> <span
										class="help-block"></span>
								</div>
							</div>
					</div>
					
				</div>
			</div>
			<div class="box-footer">
				<div class="pull-right">
					<button type="button" class="btn btn-primary" id="save-shipCharge">Save</button>
					<button type="reset" class="btn btn-default" id="cancel">Cancel</button>
				</div>
			</div>
		</form>
	</div>
</section>