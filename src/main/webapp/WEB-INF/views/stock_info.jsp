<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock Entry</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span>Stock Management</span></li>
		<li class="active">Stock Entry</li>
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
				<div class="container-fluid">
					<div class="row mt-5">
						
							<div class="col-md-3">
								<select name="stockNo" id="stockNo" class="form-control stockNo"
									data-placeholder="Search by Stock No. or Chassis No."><option
										value=""></option></select> <span class="help-block"></span>
							</div>
							<div class="col-md-1">
								<button type="button" class="btn btn-primary pull-left"
									style="width: 100px" id="btn-search-stock">Edit</button>
							</div>
						
						
					</div>
				</div>
							</div>
		</div>
	<!-- /.form:form -->
</section>
