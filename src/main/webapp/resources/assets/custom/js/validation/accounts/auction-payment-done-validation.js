$(function() {
	
	$('#payment-cancel-form').validate({
		highlight : function(element) {

			$(element).parent().parent().addClass("has-error");
		},
		unhighlight : function(element) {
			$(element).parent().parent().removeClass("has-error");
		},
		errorPlacement : function(error, element) {
			var isFound = false;
			var itr = 0;
			while (!isFound && itr < 5) {
				var e = $(element).parent();
				if (e.find('.help-block').length > 0) {
					isFound = true;
				}
				element = e;
				itr++;
			}
			if (isFound) {
				$(element).find('.help-block').text(error.text());
			}

		},
		success : function(element) {
			var isFound = false;
			var itr = 0;
			while (!isFound && itr < 5) {
				var e = $(element).parent();
				if (e.find('.help-block').length > 0) {
					isFound = true;
				}
				element = e;
				itr++;
			}
			if (isFound) {
				$(element).find('.help-block').hide();
			}

		},
		rules : {
			'cancelledRemarks' : 'required',
		}
	});
})
