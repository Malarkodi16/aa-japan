<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- java script -->
<script type="text/javascript">
addInquiryItem.inquiryItem = $('#inquiry-size').val();
function addInquiryItem() {
	var index = addInquiryItem.inquiryItem++;
	var newItem = '<div class="row">' + '											<div class="col-md-2">'
			+ '												<div class="form-group">' + '													 <select'
			+ '														name="inquiries['
			+ index
			+ '].category"'
			+ '														class="form-control select2 required category" style="width: 100%;">'
			+ '														<option value=""></option>'
			+'<c:forEach items="${masterVechicleCategories}" var="item"><option value="${item.category}">${item.category}</option></c:forEach>'
			+ '													</select><span class="help-block"></span>'
			
			+ '												</div>'
			+ '											</div>'
			+ '											<div class="col-md-2">'
			+ '												<select'
			+ '													name="inquiries['
			+ index
			+ '].maker"'
			+ '													class="form-control select2 maker required" style="width: 100%;">'
			+ '													<option value=""></option>'
			+'<c:forEach items="${masterVechicleMakers}" var="item"><option value="${item.maker}">${item.maker}</option></c:forEach>'
			+ '												</select><span class="help-block"></span>'
			+ '											</div>'
			+ '											<div class="col-md-2">'
			+ '												 <input'
			+ '													name="inquiries['
			+ index
			+ '].model" type="text" placeholder="Enter Model"'
			+ '													class="form-control model required" /><span class="help-block"></span>'
			+ '											</div>'
			+ '											<div class="col-md-2">'
			+ '												<select'
			+ '													name="inquiries['
			+ index
			+ '].country"'
			+ '													class="form-control select2 country" style="width: 100%;">'
			+ '													<option value=""></option>'
			+'<c:forEach items="${masterCountries}" var="item"><option value="${item.country}">${item.country}</option></c:forEach>'
			+ '												</select>'
			+ '											</div>'
			+ '											<div class="col-md-2">'
			+ '												<select'
			+ '													name="inquiries['
			+ index
			+ '].port"'
			+ '													class="form-control select2 port" style="width: 100%;">'
			+ '													<option value=""></option>'
			+ '												</select>'
			+ '											</div>'
			+ '											<div class="col-md-1">'
			+ '											<button type="button" class="btn btn-danger item_remove"><i class="fa fa-fw fa-remove"></i>Remove</button>'
			+ '											</div>' + '										</div>';
	
		

	$(newItem).appendTo('#inquiry-item-container');
	$('.category').select2({
		allowClear : true,
		placeholder : 'Select Category'
	});
	$('.maker').select2({
		allowClear : true,
		placeholder : 'Select Maker'
	});
	$('.country').select2({
		allowClear : true,
		placeholder : 'Select Country'
	});
	$('.port').select2({
		allowClear : true,
		placeholder : 'Select Port'
	});
	

}
</script>