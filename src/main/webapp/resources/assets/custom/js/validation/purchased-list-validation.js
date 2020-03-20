$(function() {

    //class validation
    $.validator.addMethod("_required", $.validator.methods.required, "Required");
    jQuery.validator.addClassRules('validate-required', {
        _required: true
    });
    jQuery.validator.addClassRules('validate-number', {
        number: true,
    });
    jQuery.validator.addClassRules('validate-email', {
        email: true
    });

    $('#form-purchased').validate({
        highlight: function(element) {
            $(element).addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).removeClass("has-error");
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
            'invoiceNo': 'required',
        }
    });
        $('#add-new-category-form').validate({
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
            'categoryDesc': 'required',
            'accountType': 'required',

        }
    });
    $('#purchase-invoice-save').validate({
        highlight: function(element) {
            $(element).addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).removeClass("has-error");
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
            'invoiceNo': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/invoiceNo",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        invoiceNo: function() {
                            return $('#input-invoice-no').val()
                        }
                    },
                    dataFilter: function(response) {
                        if (response == "false") {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            },
            'dueDate': 'required',
        },
        messages: {
            'invoiceNo': {
                remote: function() {
                    return $.i18n.prop('alert.invoice.no.exists')
                }
            }
        }
    });
    $('#modal-add-payment form#formAddPayment').validate({
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
            'invoiceItemType': 'required',
            'invoiceDate': 'required',
            'purchasedSupplier': 'required',
            'purchasedAuctionHouse': 'required',
            'amount':'required',
            'cancelledStock':'required',
            'reauctionStock':'required',
            'transportStock':'required',
            'stockVisible':'required'
        },
        messages: {
            'invoiceNo': {
                remote: function() {
                    return $.i18n.prop('alert.invoice.no.exists')
                }
            }
        }
    });

})
