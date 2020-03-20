<%@ taglib prefix="form"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<sec:authorize access="canAccess(101)">
<section class="content-header">
	<h1>Inquiry list</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>My Sales</span></li>
		<li class="active">Inquiry</li>
	</ol>
</section>
</sec:authorize>
<section class="content">
<sec:authorize access="canAccessAny(101,102,103,104,105)">
	<jsp:include page="/WEB-INF/views/sales/dashboard.jsp" />
</sec:authorize>
<sec:authorize access="canAccess(101)">
	<div class="alert alert-success" id="alert-block"
		style="display: ${message==null||message==''?'none':'block'}">
		<strong>${message}</strong>
	</div>
	<div class="box box-solid">
		
		<div class="box-body">
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
						<div class="form-group">
							<label for="maker">Maker</label> <select
								class="form-control maker-dropdown" name="makers[]"
								id="makers" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-2">
						<div class="form-group">
							<label for="model">Model</label> <select class="form-control"
								name="models[]" id="models" data-placeholder="All">
								<option value=""></option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="form-group" id="date-form-group">
							<label>Inquiry Created Date</label>
							<div class="input-group">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right"
									id="table-filter-inquiry-date"
									placeholder="dd-mm-yyyy - dd-mm-yyyy" readonly="readonly">
							</div>
							<!-- /.input group -->
						</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-1 form-inline pull-left">
						<div class="pull-left">
							<select id="table-filter-length" class="form-control input-sm">
								<option value="10">10</option>
								<option value="25" selected="selected">25</option>
								<option value="100">100</option>
								<option value="1000">1000</option>
							</select>
						</div>
					</div>
					<div class="col-md-3">
						<div class="has-feedback">
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword"
								autocomplete="off"> <span
								class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-8">
					<div class="pull-right">
						<div class="form-group">
							<a href="${contextPath }/inquiry/create">
								<button class="btn btn-primary">Create New Inquiry</button>
							</a>
						</div>
					</div>
					</div>
				</div>
				<div class="table-responsive">
					<table id="table-inquirylist"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
								<th data-index="0" style="width: 10px"><input
									type="checkbox" id="select-all" /></th>
								<th data-index="1" class="align-center">Inquiry Date</th>
								<th data-index="2" class="align-center">Customer Name</th>
								<th data-index="3" class="align-center">Mobile</th>
								<th data-index="4" class="align-center">Skype Id</th>
								<th data-index="5" class="align-center">Company Name</th>
								<th data-index="6" class="align-center">Category</th>
								<th data-index="7" class="align-center">Sub Category</th>
								<th data-index="8" class="align-center">Maker</th>
								<th data-index="9" class="align-center">Model</th>
								<th data-index="10" class="align-center">Country</th>
								<th data-index="11" class="align-center">Port</th>
								<th data-index="12" class="align-center">Action</th>
								<th data-index="13" class="align-center">SubModel</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- /.form:form -->
	<!-- Model -->
	<div class="modal fade" id="myModalInquiryList">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">Edit Inquiry</h4>
				</div>
				<div class="modal-body">
					<form action="#" id="inquiry-edit-modal">
						<div class="container-fluid">
							<div class="row">
								<input type="hidden" name="inquiryId" value="" /> <input
									type="hidden" name="inquiryItemId" value="" />
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Maker</label> <select id="maker"
											class="form-control modal-add-inquiry select2-select  maker-dropdown "
											name="maker" data-placeholder="Select Maker">
										</select>
									<span class="help-block"></span>
									</div>
									
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="required">Model</label> <select id="model"
											class="form-control modal-add-inquiry select2-select "
											name="model" data-placeholder="Select Model"
											style="width: 100%">
											<option value=""></option>
										</select>
										<span class="help-block"></span>
									</div>
									
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Sub Model</label> <select id="subModel"
											class="form-control modal-add-inquiry select2-select "
											name="subModel" data-placeholder="Select Sub Model"
											style="width: 100%">
											<option value=""></option>
										</select>
										<span class="help-block"></span>
									</div>
									
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label>Category</label> <input type="text"
											name="categoryAndSubcategory" id="categoryAndSubcategory"
											class="form-control categoryAndSubcategory" value=""
											readonly="readonly" /> <input type="text" name="category"
											id="category" class="form-control category hidden" value=""
											readonly="readonly" /> <input type="text"
											name="subCategory" id="subCategory"
											class="form-control subCategory hidden" value=""
											readonly="readonly" />
									</div>
									<span class="help-block"></span>
								</div>
								</div>
								<div class="row">
								<div class="col-md-4">
									<div class="form-group">
										<label>Country</label> <select
											class="form-control modal-add-inquiry select2-select country country-dropdown"
											name="country" data-placeholder="Select Country">
										</select>
									</div>
									<span class="help-block"></span>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label>Port</label> <select
											class="form-control modal-add-inquiry select2-select port"
											name="port" data-placeholder="Select Port"
											style="width: 100%">
											<option value=""></option>
										</select>
									</div>
									<span class="help-block"></span>
								</div>
							</div>
						</div>
					</form>
					<div class="box-footer" style="margin-top: 10px;">
						<div class="pull-right">
							<button type="button" class="btn btn-primary" id="btn-edit">
								<i class="fa fa-fw fa-save"></i>Save
							</button>
							<button class="btn btn-primary" data-dismiss="modal">
								<i class="fa fa-fw fa-close"></i>Close
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="hide" id="clone-element-container">
		<div id="inquiry-item-details">
			<div class="box-body no-padding bg-darkgray inquiry-item-container">
				<input type="hidden" name="customerCode" value="" /> <input
					type="hidden" name="createdDate" value="" /><input type="hidden"
					name="mobile" value="" /> <input type="hidden"
					name="customerName" value="" /> <input type="hidden"
					name="skypeId" value="" /><input type="hidden"
					name="companyName" value="" />
				<div class="table-responsive order-item-container">
					<input type="hidden" name="id1" value="" /> <input type="hidden"
						name="inquiryId" value="" />
					<table class="table table-bordered" style="overflow-x: auto;">
						<thead>
							<tr>
								<th class="align-center bg-ghostwhite" style="width: 10px">#</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Category
								</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Sub
									Category</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Maker
								</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Model
								</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Country
								</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Port
								</th>
								<th style="width: 60px" class="align-center bg-ghostwhite">Actions
								</th>
							</tr>
						</thead>
						<tbody>
							<tr class="hide clone-row">
								<td class="s-no"><span></span> <input type="hidden"
									name="inquiryId" value="" /> <input type="hidden"
									name="inquiryVehiclesId" value="" /> <input type="hidden"
									name="category" value="" /> <input type="hidden"
									name="subCategory" value="" /> <input type="hidden"
									name="maker" value="" /> <input type="hidden" name="model"
									value="" /> <input type="hidden" name="country" value="" />
									<input type="hidden" name="port" value="" /></td>
								<td class="align-center category"></td>
								<td class="align-center subCategory"></td>
								<td class="align-center maker"></td>
								<td class="align-center model"></td>
								<td class="align-center country"></td>
								<td class="align-center port"></td>
								<td class="align-center">
									<button type="button" id="edit-btn"
										class="btn btn-primary btn-sm edit" style="width: 60px;"
										data-backdrop="static" data-keyboard="false"
										data-toggle="modal" data-target="#myModalInquiryList">Edit</button>
									<button type="button" id="delete-btn"
										class="btn btn-primary btn-sm delete"
										style="margin-left: 10px;">Delete</button>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>
</section>