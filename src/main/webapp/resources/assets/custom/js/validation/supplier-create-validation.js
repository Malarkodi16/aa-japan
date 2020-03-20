$(function() {
    jQuery.validator.addClassRules('duplicate-supplier-check', {
        'duplicate_supplier_check': true
    });
    $.validator.addMethod('duplicate_supplier_check', function(value, element, param) {
        var closestEle = $(element).closest('.clone-container-location-toclone');
        var allElement = $(element).closest('.clone-container-location-toclone').prevAll();

        var arr = allElement.toArray().map(e=>{
            return ifNotValid($(e).find('input.auctionHouse').val(), '').toUpperCase();
        }
        );
        var currentVal = ifNotValid($(closestEle).find('input.auctionHouse').val(), '').toUpperCase();
        return (arr.length < 1 || $.inArray(currentVal, arr) == -1) ? true : false;

    }, "Duplicate Auction House");

    $('#supplierForm').validate({
        highlight: function(element) {

            $(element).parent().parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().parent().removeClass("has-error");
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
            'company': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/company",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        company: function() {
                            return $('#company').val()
                        },
                        supplierCode: function() {
                            return $('#supplierCode').val()
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
            'company': {
                remote: function() {
                    return $.i18n.prop('alert.supplier.company.exists')
                }
            }
        }
    });

})
