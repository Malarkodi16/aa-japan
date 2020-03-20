$(function() {
    $('#transporter-fee-Form').validate({
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
            'transporter': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/transporter",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        id: function() {
                            return $('#id').val()
                        },
                        transporter: function() {
                            return $('#transporter').val()
                        },
                        from: function() {
                            return $('#from').val()
                        },
                        to: function() {
                            return $('#to').val()
                        },
                        categories: function() {
                            return $('#categories').val()
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
            'from': 'required',
            'to': 'required',
            'maker': 'required',
            'model': 'required',
            'amount': 'required',
            'transportCategory': 'required'

        },
        messages: {
            'transporter': {
                remote: function() {
                    return $.i18n.prop('alert.transporter.fee')
                }
            }
        }
    });
})
