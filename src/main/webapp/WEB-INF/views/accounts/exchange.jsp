<%@ taglib prefix="form"uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<section class="content-header">
	<h1>Exchange Rate</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Master</span></li>
		<li class="active">Exchange Rate</li>
	</ol>
</section>
<!-- stock. -->
<jsp:include page="/WEB-INF/views/accounts/daybook-icons.jsp" />
<section class="content">
	
	<div class="box box-solid">
		
		<div class="box-body">
			<div class="container-fluid">
			<div class="row form-group">
			
			
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Transaction Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control required pull-right datepicker"
									id="exchangeRateDate"
									placeholder="dd-mm-yyyy" />
							</div>
							<!-- /.input group -->
						</div>
					</div>
					<button type="submit" id="btn-search" class="btn btn-primary" style="margin-top: 24px;">
						<i class="fa fa-save mr-5"></i>Search 
					</button>
					
					
					
				</div>
				
					
					
					<div class="table-responsive">
			<table id="table-exchangeRate-list"
				class="table table-bordered table-striped"
				style="width: 100%; overflow: scroll;">
				<thead>
					<tr>
						<th><input type="checkbox" id="select-all" /></th>
						<th>Dollar Rate</th>
						<th>Aud Rate</th>
						<th>Pound Rate</th>
						
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
				
			</div>
			<!-- table start -->
			
		</div>
	</div>
	
	
	
		<!-- table start -->
		
	

	<!-- /.form:form -->
	<!-- Model -->
	
	<!-- /.modal -->
</section>
