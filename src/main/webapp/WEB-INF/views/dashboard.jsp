<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- Small boxes (Stat box) -->
<div class="row">
	<div class="col-lg-3 col-xs-6">
		<!-- small box -->
		<div class="small-box bg-aqua">
			<div class="inner">
				<h3>Accounts</h3>
			</div>
			<div class="icon">
				<i class="ion ion-briefcase"></i>
			</div>
			<a href="${contextPath }/accounts/dash-board/view"
				class="small-box-footer">More info <i
				class="fa fa-arrow-circle-right"></i></a>
		</div>
	</div>
	<!-- ./col -->
	<div class="col-lg-3 col-xs-6">
		<!-- small box -->
		<div class="small-box bg-green">
			<div class="inner">
				<h3>Shipping</h3>
			</div>
			<div class="icon">
				<i class="ion ion-android-boat"></i>
			</div>
			<a href="${contextPath }/stock/purchased" class="small-box-footer">More
				info <i class="fa fa-arrow-circle-right"></i>
			</a>
		</div>
	</div>
	<!-- ./col -->
	<div class="col-lg-3 col-xs-6">
		<!-- small box -->
		<div class="small-box bg-yellow">
			<div class="inner">
				<h3>Sales</h3>
			</div>
			<div class="icon">
				<i class="ion ion-bag"></i>
			</div>
			<a href="${contextPath }/inquiry/listview" class="small-box-footer">More
				info <i class="fa fa-arrow-circle-right"></i>
			</a>
		</div>
	</div>
	<!-- ./col -->
	<div class="col-lg-3 col-xs-6">
		<!-- small box -->
		<div class="small-box bg-red">
			<div class="inner">
				<h3>Documents</h3>
			</div>
			<div class="icon">
				<i class="ion ion-document"></i>
			</div>
			<a href="${contextPath }/documents/tracking/not-received" class="small-box-footer">More
				info <i class="fa fa-arrow-circle-right"></i>
			</a>
		</div>
	</div>

</div>
<!-- /.row -->