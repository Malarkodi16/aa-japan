<!-- # Copyright (c) 2018 - AAJ
# @Author: saravanan(Nexware)
# @Date: 
# @Last Modified by: saravanan(Nexware)
# @Last Modified time: 2018-10-03
-->

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>View Loan</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Loan Management</span></li>
		<li class="active">View Loan</li>
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

				<div class="row">
					<div class="col-md-2 form-inline pull-left">
						<select id="table-filter-length" class="form-control input-sm">
							<option value="10">10</option>
							<option value="25" selected="selected">25</option>
							<option value="100">100</option>
							<option value="1000">1000</option>
						</select>
					</div>

				</div>

				<div class="table-responsive">
					<table id="table-viewloan"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">

						<thead>
							<tr>
								<th><input type="checkbox" id="select-all" /></th>
								<th>Sequence</th>
								<th>Date</th>
								<th>bank</th>
								<th>Reference</th>
								<th>Loan type</th>
								<th>Due Date</th>
								<th>From Date</th>
								<th>To Date</th>
								<th>Interest Rate</th>
								<th>Loan Term</th>
								<th>Description</th>
								<th>Loan Amount</th>
								<th>Loan Update</th>



							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>




		</div>

	</div>


	<!-- /.form:form -->
	<!-- Model -->



</section>