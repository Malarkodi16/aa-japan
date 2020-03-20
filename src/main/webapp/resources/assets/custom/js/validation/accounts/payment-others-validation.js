$(function() {
    $('#other-form').validate({
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
            'date': 'required',
            'dueDate': 'required',
            'category': 'required',
            'refNo': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/in/generalExpense/invoiceNo",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        refNo: function() {
                            return $('input[name="refNo"]').val()
                        },
                        invoiceNo: function() {
                            return $('input[name="invoiceNo"]').val()
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
            }

        },
        messages: {
            'refNo': {
                remote: function() {
                    return $.i18n.prop('alert.invoice.no.exists')
                }
            }
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

    $('#add-new-general-supplier').validate({
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
            'name': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/in/generalSupplier",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        name: function() {
                            return $('#name').val()
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
            }
        },
        messages: {
            'name': {
                remote: function() {
                    return $.i18n.prop('alert.supplier.name.exists')
                }
            }
        }
    });
});
