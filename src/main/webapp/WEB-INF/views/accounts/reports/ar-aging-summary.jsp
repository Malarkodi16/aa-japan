<section class="content-header">
	<h1>A/R Aging Summary</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li>Payable &amp; Receivable Reports</li>
		<li class="active">A/R Aging Summary</li>
	</ol>
</section>

<section class="content">
	<div class="box box-solid">
		<div class="box-header"></div>
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
								class="form-control" placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-ar-aging-summary-report"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Customer Name</th>
								<th data-index="1">Customer ID</th>
								<th data-index="2">Amount Outstanding</th>
								<th data-index="3">Current</th>
								<th data-index="4">Aged 1-30</th>
								<th data-index="5">Aged 31-60</th>
								<th data-index="6">Aged 61-90</th>
								<th data-index="7">Aged > 91</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr class=totalFooter>
								<th colspan="2" style="text-align: right">Total</th>
								<th class="dt-right"><span class="autonumber pagetotal totalAmountOutstanding"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotal totalCurrentAmount"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotal total1To30"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotal total31To60"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotal total61To90"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
								<th class="dt-right"><span class="autonumber pagetotal totalAbove90"
									data-a-sign="¥ " data-m-dec="0">0</span></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</section>