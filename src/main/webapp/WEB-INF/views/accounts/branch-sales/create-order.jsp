<section class="content-header">
	<h1>Create Branch Sales Order</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Create Branch Sales Order</li>
	</ol>
</section>
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid ">
				<div class="row form-group">

					<div class="col-md-3 form-inline pull-left">
						<div class="has-feedback">
							<label>Customer</label> <select class="form-control customer"
								id="custselectId" style="width: 100%;"
								data-placeholder="Search by Customer ID, Name, Email">
								<option value=""></option>
							</select> <span class="help-block"></span>
						</div>
					</div>
					<div class="col-md-4 form-inline pull-right">
						<div class="pull-right">
							<button type="submit" class="btn btn-primary enable-on-select"
								id="btn-create-sales" data-backdrop="static"
								data-keyboard="false" data-toggle="modal"
								data-target="#modal-create-sales" disabled="disabled">Sales
								order</button>
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<select id="table-filter-length" class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-branch-salesOrder"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Stock No</th>
								<th>Chassis No</th>
								<th>Category</th>
								<th>Maker</th>
								<th>Model</th>
								<th>Dest Country</th>
								<th>Dest port</th>
								<th>Customer</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.modal Create Sales Invoice -->
	<div class="modal fade" id="modal-create-sales">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Generate Sales Invoice</h4>
				</div>
				<div class="modal-body">
					<jsp:include page="/WEB-INF/views/accounts/createsalesorder.jsp" />
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-generate-sales-invoice">
							<i class="fa fa-fw fa-save"></i>Create Order
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
</section>