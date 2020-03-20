$(function() {
    $.validator.addMethod("valid-fields", function(value, element, parms) {
        let row = $(element).closest('tr');
        let amount = getAutonumericValue($(row).find('input[name="amount"]'));
        let shippingCharges = getAutonumericValue($(row).find('input[name="shippingCharges"]'));
        let photoCharges = getAutonumericValue($(row).find('input[name="photoCharges"]'))
        let blAmendCombineCharges = getAutonumericValue($(row).find('input[name="blAmendCombineCharges"]'))
        let radiationCharges = getAutonumericValue($(row).find('input[name="radiationCharges"]'))
        let repairCharges = getAutonumericValue($(row).find('input[name="repairCharges"]'));
        let yardHandlingCharges = getAutonumericValue($(row).find('input[name="yardHandlingCharges"]'))
        let inspectionCharges = getAutonumericValue($(row).find('input[name="inspectionCharges"]'))
        let transportCharges = getAutonumericValue($(row).find('input[name="transportCharges"]'))
        let freightCharges = getAutonumericValue($(row).find('input[name="freightCharges"]'))
        if (isEmpty(amount) && isEmpty(shippingCharges) && isEmpty(photoCharges) && isEmpty(blAmendCombineCharges) && isEmpty(radiationCharges) && isEmpty(repairCharges) && isEmpty(yardHandlingCharges) && isEmpty(inspectionCharges) && isEmpty(transportCharges) && isEmpty(freightCharges)) {
            return false;
        } 
        if (amount > 0 || shippingCharges > 0 || photoCharges > 0 || blAmendCombineCharges > 0 || radiationCharges > 0 || repairCharges > 0 || yardHandlingCharges > 0 || inspectionCharges > 0 || transportCharges > 0 || freightCharges > 0) {
            return true;
        } else {
            return false;
        }

        return true;

    }, "Fill Required Fileds");

    $('#storage-photos-form').validate({
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
            'remitter': 'required',
            'currency': 'required',
            'refNo': 'required',
            'invoiceDate': 'required',
            'dueDate': 'required',
            'stockNo': 'required',
            'amount': {
                'required': false,
                'valid-fields': true
            },
        }
    });
});
