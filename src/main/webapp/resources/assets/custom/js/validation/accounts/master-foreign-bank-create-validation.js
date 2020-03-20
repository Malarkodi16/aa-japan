$(function() {
	$('#bank-create-form, #modal-edit-bank-form').validate({
		highlight : function(element) {
			$(element).parent().addClass("has-error");
		},
		unhighlight : function(element) {
			$(element).parent().removeClass("has-error");
		},
		onfocusout : function(element) {
			$(element).valid();
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
			'bank' : 'required',
			'country' : 'required'
		}
	});
});