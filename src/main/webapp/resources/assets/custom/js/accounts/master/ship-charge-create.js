var countriesJson
$(function() {
	
	  $('.autonumber').autoNumeric('init');
	$.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
        var elements = $('#originCountry');
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('JAPAN').trigger('change');
        
     
        var elements = $('#destCountry');
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger('change');

       
    })
    $('#create-ship-charge').on('submit', function() {
        autoNumericSetRawValue($('input.autonumber'))
    })

    //Form Save
    $("#save-shipCharge").on('click', function() {
    	 autoNumericSetRawValue($('input.autonumber'));
        var data = getFormData($('#create-ship-charge').find('input,select'));
//        $('input[name="amount"]').autoNumeric('set', amount);
        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/ship/charge/save",
            contentType: "application/json",
            success: function(status) {
                 $('#manufacture-of-year-form').find('input,select').val('').trigger('change')
            }
        });
    })

    //Form reset
    $("#cancel").on('click', function() {
        $('#manufacture-of-year-form').find('input,select').val('').trigger('change')
    })
})
