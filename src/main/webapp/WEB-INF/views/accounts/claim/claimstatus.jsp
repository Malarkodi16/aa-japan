<!-- # Copyright (c) 2018 - AAJ
# @Author: Rajlakshman(Nexware)
# @Date: 
# @Last Modified by: Rajlakshman(Nexware)
# @Last Modified time: 2018-11-15
-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row" id="stock-status-wrapper">
	<div class="col-md-2">
		<a href="${contextPath}/accounts/claim/tax">
			<div class="small-box bg-aqua claim-nav-box blur" data-nav="0">
				<div class="inner">
					<h3 id="count-tax">0</h3>
					<p>Tax</p>
				</div>
				<div class="icon">
					<em class="fa fa-sticky-note"></em>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/claim/recycle">
			<div class="small-box bg-green claim-nav-box blur" data-nav="1">
				<div class="inner">
					<h3 id="count-recycle">0</h3>
					<p>Recycle</p>
				</div>
				<div class="icon">
					<em class="fa fa-support"></em>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<a href="${contextPath}/accounts/claim/cartax">
			<div class="small-box bg-red claim-nav-box blur" data-nav="2">
				<div class="inner">
					<h3 id="count-carTax">0</h3>
					<p>CarTax</p>
				</div>
				<div class="icon">
					<em class="fa  fa-road"></em>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/claim/insurance">
			<div class="small-box bg-orange claim-nav-box blur" data-nav="3">
				<div class="inner">
					<h3 id="count-insurance">0</h3>
					<p>Insurance</p>
				</div>
				<div class="icon">
					<em class="fa fa-suitcase"></em>
				</div>
			</div>
		</a>
	</div>
	<div class="col-md-2">
		<!-- small box -->
		<a href="${contextPath}/accounts/claim/radiation">
			<div class="small-box bg-blue claim-nav-box blur" data-nav="4">
				<div class="inner">
					<h3 id="count-radiation">0</h3>
					<p>Radiation</p>
				</div>
				<div class="icon">
					<em class="fa fa-search"></em>
				</div>
			</div>
		</a>
	</div>

</div>