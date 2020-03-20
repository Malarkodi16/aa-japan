<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<sec:authorize access="canAccess(130)">
<section class="content-header">
	<h1>Recycle Claim</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active">Recycle Claim</li>
	</ol>
</section>
</sec:authorize>

<section class="content">
<sec:authorize access="canAccessAny(130,131)">
	<jsp:include
		page="/WEB-INF/views/documents/recycle/recycle-dashboard.jsp" />
</sec:authorize>
<sec:authorize access="canAccess(130)">
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
						<label> <input name="radioReceivedFilter" type="radio"
							class="minimal" value="0" checked="checked"> Available
						</label> <label> <input name="radioReceivedFilter" type="radio"
							class="minimal" value="1"> Applied
						</label> <label class="ml-5"> <input name="radioReceivedFilter"
							type="radio" class="minimal" value="2"> Received
						</label>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Purchase Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-recycle-claim"
									placeholder="dd-mm-yyyy - dd-mm-yyyy">
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<form method="post" enctype="multipart/form-data"
						action="${contextPath}/documents/recycle/claim/apply/uploadExcelFile" id="form-received-upload">
						<div class="col-md-6 ">
							<label>&nbsp;</label>
							<div class="form-inline">
								<input type="file" name="file" class="form-control"
									accept=".xlsx" />
								<button type="submit" class="btn btn-primary form-control"><em class="fa fa-fw fa-upload"></em>Upload</button>
							</div>
						</div>
						<div class="col-md-3">
							<label>&nbsp;</label>
						</div>
					</form>
				
				</div>
				
				<div class="row form-group">
				<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-recycle-claim-filter-length"
								class="form-control ">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
				<div class="col-md-3">
						<div class="has-feedback">
						
							<input type="text" id="table-filter-search" class="form-control"
								placeholder="Search by keyword"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
				</div>
				<!-- table start -->
				<div class="table-responsive">
					<table id="table-claim-recycle"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0">Purchase Date</th>
								<th data-index="1">Chassis No</th>
								<th data-index="2">Recycle Amount</th>
								<th data-index="3">Charges</th>
								<th data-index="4">Interest</th>
								<th data-index="5">Received Amount</th>
								<th data-index="6">Submit Date</th>
								<th data-index="7">Received Date</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>
</section>