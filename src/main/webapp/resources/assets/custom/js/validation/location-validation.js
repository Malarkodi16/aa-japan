$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $('#locationForm').validate({
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
            'type': 'required',
            'displayName': {
                'required': true,
               
                remote: {
                    url: myContextPath + "/master/location/check/existing",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        id: function() {
                            return $('input[name="id"]').val()
                        },
                        displayName: function() {
                            return $('#displayName').val()
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
            'address': 'required',
            'shipmentType': 'required',
            'shipmentOriginCountry': 'required',
            'shipmentOriginPort': 'required',
        },
        messages: {
            'displayName': {
                remote: "Already Exist"
            }
        }
    });
})
