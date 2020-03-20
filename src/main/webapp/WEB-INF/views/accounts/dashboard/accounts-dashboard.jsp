<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="canAccess(1)">
<section class="content-header">
	<h1>Dashboard</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Dashboard</li>
	</ol>
</section>

<section class="content">
	<jsp:include
		page="/WEB-INF/views/accounts/dashboard/accounts-dashboard-icon.jsp" />
	<div class="row form-group">
		<!-- Table -->
		<div class="col-md-6">
			<div class="box box-solid">
				<div class="box-header">
					<h4 class="box-title">Recent Sales Order</h4>
				</div>
				<div class="box-body">
					<div class="table-responsive">
						<table id="table-so" class="table table-bordered table-hover"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th class="align-center">Date</th>
									<th class="align-center">Customer</th>
									<th class="align-center">Sales Person</th>
									<th class="align-center">Maker/Model</th>
									<th class="align-center">Purchase Price</th>
									<th class="align-center">Sold Amount</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="box box-solid">
				<div class="box-header">
					<h4 class="box-title">Bank List</h4>
					<!-- <div class="col-md-6 pull-right">
						<label>Bank</label> 
						<select name="bank"
							class="form-control select2-select required bank"
							data-placeholder="Select Bank" id="bank">
							<option value=""></option>

						</select>
					</div> -->
				</div>
				<div class="box-body">
					<div class="row"></div>
					<div class="table-responsive">
						<table id="table-master-bank-list"
							class="table table-bordered table-hover"
							style="width: 100%; overflow: scroll;">
							<thead>
								<tr>
									<th class="align-center">Symbol</th>
									<th class="align-center">Currency</th>
									<th class="align-center">Total Amoount</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

</section>
</sec:authorize>