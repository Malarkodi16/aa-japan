$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    jQuery.validator.addClassRules('validate-duplicate-locationName', {
        'duplicate_locationName': true
    });
    jQuery.validator.addClassRules('validation-required', {
        'required': true
    });
    $.validator.addMethod('duplicate_locationName', function(value, element, param) {
        var closestEle = $(element).closest('.item');
        var allElement = closestEle.prevAll();

        var arr = allElement.toArray().map(e=>{
            return ifNotValid($(e).find('input[name="locationName"]').val(), '').toUpperCase();
        }
        );
        var currentVal = ifNotValid($(closestEle).find('input[name="locationName"]').val(), '').toUpperCase();
        return (arr.length < 1 || $.inArray(currentVal, arr) == -1) ? true : false;

    }, "Duplicate location name");
    $('#ins-company').validate({
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
                    url: myContextPath + "/master/inspection/isCompanyExists?",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        id: function() {
                            return $('input[name="code"]').val()
                        },
                        company: function() {
                            return $('#companyName').val()
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
                remote: "Company Already Exist"
            }
        }
    });
})
