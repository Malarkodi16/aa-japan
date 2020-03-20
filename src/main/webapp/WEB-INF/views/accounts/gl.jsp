<%@ taglib prefix="form"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<section class="content-header">
	<h1>Auction Payment</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Dashboard</span></li>
		<li class="active">Purchase Vehicles</li>
	</ol>
</section>


<section class="content">
	
	<div class="box box-solid">
		<div class="box-header"></div>
		<div class="box-body">
			<div class="container-fluid">
			<div class="row form-group">
					
					<div class="col-md-2">
						<div class="form-group">
							<label>Bank</label> <select id="type"
								name="type" class="form-control select2-tag" style="width: 100%;">
								<option value="">Select Type</option>
								<c:forEach  items="${mCoaType}" var="item">
										<option value="${item.coaType}">${item.coaType}</option>
										</c:forEach>
							</select>
						</div>
					</div>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-md-6">
						<div class="has-feedback">
							<input type="text" id="table-filter-search"
								class="form-control" placeholder="Search by keyword">
							<span class="glyphicon glyphicon-search form-control-feedback"></span>
						</div>
					</div>
					<div class="col-md-2 form-inline pull-right">
						<div class="pull-right">
							 <select id="table-filter-length"
								class="form-control input-sm">
									<option value="10">10</option>
									<option value="25" selected="selected">25</option>
									<option value="100">100</option>
									<option value="1000">1000</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<!-- table start -->
			<div class="container-fluid">
				<div class="row form-group">
					<div class="col-md-2">
					<button type="button" class="btn  btn-primary" id="addNew" data-toggle="modal" data-target="#add-new-coa">New</button>
				</div></div>
				<div class="table-responsive">
					<table id="coa-table"
						class="table table-bordered table-striped"
						style="width: 100%; overflow: scroll;">
						<thead>
							<tr>
							
								<th data-index="0">Tax Code</th>
								<th data-index="1">Description</th>
								
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
	<div class="modal fade" id="add-new-coa">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title">Chart Of Account</h4>
                                </div>
				<div class="modal-body">
					<div class="box">
		<div class="box-body">
				<div class="box-body" id="cloneTO">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-4">
											<div class="form-group">
												<label class="required">Tax No</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="coaNo"  id="coaNo"
														class="form-control required" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="required">Description</label>
												<div class="input-group">
													<div class="input-group-addon">
														<i class="fa fa-fw fa-user"></i>
													</div>
													<input type="text" name="coaDesc"  id="coaDesc"
														class="form-control required" />
												</div>
												<span class="help-block"></span>
											</div>
										</div>
							
							<div class="col-md-4">
								<div class="form-group">
									<label class="required">Type</label> <select
										name="coaType" id="coaType"
										class="form-control required select2-tag">
										<option value="">Select Type</option>
										<c:forEach  items="${mCoaType}" var="item">
										<option value="${item.coaType}">${item.coaType}</option>
										</c:forEach>
									</select> <span class="help-block"></span>
								</div>
							</div>
						</div>
						
						
					</div>
				</div>
				
			<!-- /.form:form -->
		</div>
	</div>
				</div>
				<div class="modal-footer">
					<div class="pull-right">
						<button class="btn btn-primary " id="btn-create-coa">
							<i class="fa fa-fw fa-save"></i>Create
						</button>
						<button class="btn btn-primary" id="btn-close"
							data-dismiss="modal">
							<i class="fa fa-fw fa-close"></i>Close
						</button>
					</div>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</section>
