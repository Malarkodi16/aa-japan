<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="row offer-pg-cont">
	<div class="offer-pg'">
		<c:forEach items="${mbank}" var="item">
			<div class="col-md-2 acc portfolio-item">

				<div class="small-box bg-aqua">
					<div class="inner">
						<h4>${item.bankName}</h4>
						<h5 class="count-purchased">
							<span class="pull-left">¥ ${item.yenBalance}</span>
							<%-- <span  class="pull-right"> $ ${item.usdBalance}</span> --%>
						</h5>
						<br />
						<h5 class="count-purchased">
							<span class="pull-left">A$ ${item.clearingBalance}</span>
						</h5>
						<br />
					</div>
					<div class="icon">
						<i class="fa fa-bank"></i>
					</div>
				</div>

			</div>

		</c:forEach>
	</div>
</div>



