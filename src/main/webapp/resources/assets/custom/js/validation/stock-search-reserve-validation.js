$(function() {
    $.validator.addMethod("validate-threshold", function(value, element, parms) {
        let reserved=getAutonumericValue($(element));
        var fob = getAutonumericValue($(element).closest('tr').find('td>span.minimumSellingPrice')); 
        return reserved >= Number(fob) ? true : false;

    }, "Below minimum selling price!");
    $('#reserveForm').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
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
            'price': {
                'required': true,
                'validate-threshold': true
            },
            'custId': 'required',
            'currency': 'required',

        }
    });
});
