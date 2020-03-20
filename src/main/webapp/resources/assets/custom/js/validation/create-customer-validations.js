$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $('input[name="mobileNo"]').keypress(function(e) {
        if (e.charCode < 39 || e.charCode > 57) {
            return false;
        }
    });

    $('#customerForm').validate({
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
            'firstName': 'required',
            'lastName': 'required',
            'mobileNo': {
                'required': true,

            },
            'email': {
                'required': true,
                'email': true,
                remote: {
                    url: myContextPath + "/check/existing/customer",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        id: function() {
                            return $('input[name="id"]').val()
                        },
                        email: function() {
                            return $('#email').val()
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
            'nickName': 'required',
            'address': 'required',
            'city': 'required',
            'country': 'required',
            'port': 'required',
            'currencyType': 'required',
            'paymentType': 'required'
        },
        messages: {
            'email': {
                remote: "Already Exist"
            }
        }
    });

    $('#consigneeForm').validate({
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
            'cFirstName': 'required',
            'cEmail': 'required',
            'cMobileNo': 'required',
            // 'npFirstName': 'required',
            'npEmail': 'required',
            'npMobileNo': 'required',
        }
    });
})
