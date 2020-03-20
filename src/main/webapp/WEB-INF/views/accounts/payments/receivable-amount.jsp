<section class="content-header">
	<h1>Amount Receivable</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i>Home</span></li>
		<li class="active">Amount Receivable</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid ">
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
								class="form-control" placeholder="Search by keyword">
							<span class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					
				</div>
				<div class="table-responsive">
					<table id="table-payment-receivable-amount"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Customer Id</th>
								<th data-index="1">Customer Name</th>
								<th data-index="2">Credit limit</th>
								<th data-index="3">Total</th>
								<th data-index="4">Received</th>
								<th data-index="5">Receivable Amount</th>
								<th data-index="6">Balance</th>
								
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>