<%@ taglib prefix="form"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<section class="content-header">
	<h1>Stock View</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>shipping</span></li>
		<li class="active">Stock View</li>
	</ol>
</section>
<section class="content">
	
<div class="box box-solid">
	<div class="box-body">

		<jsp:include page="/WEB-INF/views/shipping/stock-details.jsp" />
	</div>
	<!-- /.box-body -->
</div>
<!-- /.box -->
</section>