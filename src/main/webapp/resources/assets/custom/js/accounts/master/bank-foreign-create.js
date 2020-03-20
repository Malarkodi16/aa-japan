var currencyJson
$(function() {

	$.getJSON(myContextPath + "/data/countries.json", function(data) {
		countryJson = data;
		$('select[name="country"]').select2({
			allowClear : true,
			width : '100%',
			data : $.map(countryJson, function(item) {
				return {
					id : item.country,
					text : item.country
				};
			})
		}).val('').trigger("change");
	});

	$('#btn-save').on(
			'click',
			function() {
				if (!$("#bank-create-form").find('input,select,textarea').valid()) {
					return;
				}
				var bankData = getFormData($('#bank-create-form').find(
						'input,select,textarea'));

				$.ajax({
					beforeSend : function() {
						$('#spinner').show()
					},
					complete : function() {
						$('#spinner').hide();
					},
					type : "post",
					data : JSON.stringify(bankData),
					url : myContextPath + "/master/save-foreign-bank",
					contentType : "application/json",
					success : function(status) {

					}
				});
			})

})
