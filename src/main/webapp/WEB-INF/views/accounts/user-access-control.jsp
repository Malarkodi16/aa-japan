
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header ">
	<h1>User Access Control</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li class="active"><span>User Access Control</span></li>
	</ol>
</section>
<!-- stock. -->
<section class="content ">
	<div class="alert alert-success" id="alert-block" style="display: none"></div>
	<div class="container-fluide">
		<section class="content ">
			<div class="row">
				<div class="col-md-3">
					<select name="user" id="user" class="form-control"
						data-placeholder="Select User"><option
							value=""></option></select> <span class="help-block"></span>
				</div>
				<div class="col-md-3">
					<button type="button" class="btn btn-primary" id="update">Update</button>
				</div>
			</div>
			<div class="box box-widget">
				<div class="box-body no-padding">
					<div id="tree"></div>
				</div>
			</div>
		</section>

	</div>
</section>
