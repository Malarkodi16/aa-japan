<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock Entry</h1>
	<ol class="breadcrumb">
		<li><span><em class="fa fa-home"></em> Home</span></li>
		<li><span>Stock Management</span></li>
		<li class="active">Stock Upload</li>
	</ol>
</section>
<!-- stock. -->
<section class="content">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<form method="post" enctype="multipart/form-data"
		action="${contextPath}/opening/stock/uploadStock"
		id="form-received-upload">
		<div class="col-md-6 ">
			<label>&nbsp;</label>
			<div class="form-inline">
				<input type="file" name="file" class="form-control" accept=".xlsx" />
				<button type="submit" class="btn btn-primary form-control">
					<em class="fa fa-fw fa-upload"></em>Upload
				</button>
			</div>
		</div>
		<div class="col-md-3">
			<label>&nbsp;</label>
		</div>
	</form>
</section>
