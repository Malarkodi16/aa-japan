$(function() {
    $.validator.addMethod("valid-fields", function(value, element, parms) {
        let row = $(element).closest('.row');
        let estimatedDeparture = $(row).find('input[name="estimatedDeparture"]').val();
        let estimatedArrival = $(row).find('input[name="estimatedArrival"]').val();

        if (isEmpty(estimatedDeparture) && isEmpty(estimatedArrival)) {
            return false;
        } else {
            return true;
        }
    }, "Fill Required Fileds");
    $('#edit-shipping-instruction-form').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        errorPlacement: function(error, element) {
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
        success: function(element) {
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
        rules: {
            'consigneeId': 'required',
            'destCountry': 'required',
            'destPort': 'required',
            'yard': 'required',
            'paymentType': 'required',
            'estimatedArrival': {
                'required': false,
                'valid-fields': true
            },
            'estimatedDeparture': {
                'required': false,
                'valid-fields': true
            }

        }
    });

})
