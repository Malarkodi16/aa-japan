<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Insurance</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Claim</span></li>
		<li class="active">Insurance</li>
	</ol>
</section>
<section class="content">
	<jsp:include page="/WEB-INF/views/accounts/claim/claimstatus.jsp" />
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		<div class="box-body">
			<div class="container-fluid">
				<div class="row">
					<!-- table start -->
					<div style="text-align: center;">
						<label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="1" checked="checked">
							Applied
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal ml-5" value="2"> Received
						</label>
					</div>
				</div>
				<div class="row">

					<form method="post" enctype="multipart/form-data"
						action="${contextPath}/accounts/claim/insurance/apply/uploadExcelFile"
						id="form-received-upload" class="col-md-6">
						<label>&nbsp;</label>
						<div class="form-inline">
							<input type="file" name="file" class="form-control"
								accept=".xlsx" />
							<button type="submit" class="btn btn-primary form-control">
								<em class="fa fa-fw fa-upload"></em>Upload
							</button>
						</div>
						<div class="col-md-3">
							<label>&nbsp;</label>
						</div>
					</form>
				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-insurance"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Chassis No</th>
								<th data-index="1">Insurance Company</th>
								<th data-index="2">Insurance No</th>
								<th data-index="3">Owner Address</th>
								<th data-index="4">Owner Name</th>
								<th data-index="5">From Year</th>
								<th data-index="6">From Month</th>
								<th data-index="7">From Date</th>
								<th data-index="8">To Year</th>
								<th data-index="9">To Month</th>
								<th data-index="10">ToDate</th>
								<th data-index="11">Period</th>
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