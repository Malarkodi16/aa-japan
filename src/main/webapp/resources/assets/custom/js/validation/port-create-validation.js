$(function() {

	$('#port-form').validate({
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
			'continent' : 'required',
			'country' : 'required',
			'portName' : {
				required : true,
				remote : {
					url : myContextPath + "/master/port/validate/portName",
					type : "get",
					 async: false,
					cache : false,
					dataType : "json",
					data : {
						portName : function() {
							return $('input[name="portName"]').val();
						}
					},
					dataFilter : function(response) {
						return response == "true" ? false : true
					}
				}
			}
		},
		messages : {
			'portName' : {
				remote : function() {
					return $.i18n.prop('alert.master.port.name.duplicate')
				}
			}
		}
	});
})
