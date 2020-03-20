<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ju" uri="/WEB-INF/tld/jsonutils.tld"%>

<section class="content-header">
	<h1>Create Loan</h1>
	<ol class="breadcrumb">
		<li><span><i class="fa fa-home"></i> Home</span></li>
		<li><span>Loan Management</span></li>
		<li class="active">Create Loan</li>
	</ol>
</section>
<!-- stock. -->
<section>
<div class="box box-solid">

                            <!-- /.box-header -->

                            <div class="box-body">
		    <div class="row">

                    <div class="form-group col-md-3">
                        <label>Loan Name</label>

                        <input type="text" class="form-control" placeholder="Loan Name">

                        <!-- /.input group -->
                    </div>
                    <div class="form-group col-md-3">
                        <label>Loan Amount</label>

                        <input type="text" class="form-control" placeholder="Loan Amount">

                    </div>
                    <div class="form-group col-md-3">
                       <label>Currency</label>
					    <select class="form-control select2" style="width: 100%;"  id="Currency">
                                     <option value=""></option>
                                    <option value="Currency1">Yen</option>
                                    <option value="Currency2">Rupees</option>
                                    <option value="Currency3">Dollar</option>
                                   
                                </select>

                        

                    </div>
					<div class="form-group col-md-3">
				
					
                        <label>Interest Rate</label>


                        <input type="text" class="form-control" placeholder="Loan Amount">
						
						
						 
						 
						

                    </div>

                </div>
                <div class="row">

                    <div class="form-group col-md-3">
                        <label>Loan Term</label>

                        <input type="text" class="form-control" placeholder="Loan Term">

                        <!-- /.input group -->
                    </div>
					
                    <div class="form-group col-md-3">
                        <label>Year</label>

                        <input type="text" class="form-control" placeholder="Year">

                        <!-- /.input group -->
                    </div>
					
                    <div class="form-group col-md-3">
                        <label>Month</label>

                        <input type="text" class="form-control" placeholder="Month">

                        <!-- /.input group -->
                    </div>
                   
				</div>
		   
		   
		    
		   </div>
		   <div class="box-footer">
			<div class="container-fluid pull-right">
				
				
				<button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#myModalupdate">Calculate</button>	
				<button type="submit" class="btn btn-primary">Create</button>
				
			</div>
			</div>
		   
		   </div>


</section>
