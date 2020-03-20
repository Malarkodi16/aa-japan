$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('#stockModelTypeForm').validate({
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
            'maker': 'required',
            'model': 'required',
            'cc': 'required',
            'unit': 'required',
            'subcategory': 'required',
            'transmission': 'required',
            'fuel': 'required',
            'driven': 'required',
            'modelType': {
                'required': true,
                remote: {
                    url: myContextPath + "/check/existing/in/isModelTypeExists?",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        code: function() {
                            return $('input[name="code"]').val()
                        },
                        modelType: function() {
                            return $('#modelType').val()
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
            'modelType': {
                remote: "Model Type Already Exist"
            }
        }
    });
    

})
