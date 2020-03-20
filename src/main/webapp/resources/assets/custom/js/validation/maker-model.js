$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    jQuery.validator.addClassRules('validate-duplicate-modelName', {
        'duplicate_modelName': true
    });
    jQuery.validator.addClassRules('validation-required', {
        'required': true
    });
    $.validator.addMethod('duplicate_modelName', function(value, element, param) {
        var closestEle = $(element).closest('.item');
        var allElement = closestEle.prevAll();

        var arr = allElement.toArray().map(e=>{
            return ifNotValid($(e).find('input[name="modelName"]').val(), '').toUpperCase();
        }
        );
        var currentVal = ifNotValid($(closestEle).find('input[name="modelName"]').val(), '').toUpperCase();
        return (arr.length < 1 || $.inArray(currentVal, arr) == -1) ? true : false;

    }, "Duplicate model name");
    $('#maker-model').validate({
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
                    url: myContextPath + "/master/isMakerExists?",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        id: function() {
                            return $('input[name="code"]').val()
                        },
                        maker: function() {
                            return $('#makerName').val()
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
                remote: "Maker Already Exist"
            }
        }
    });
    $('#modal-add-model-form').validate({
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
            'modelName': 'required',
            'category': 'required',
            'm3': 'required',
            'subcategory': 'required',
            'transportCategory': 'required',

        }
    });
    $('#modal-edit-model-form').validate({
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
            'category': 'required',
            'transportCategory': 'required',
            'subcategory': {
                'required': true,
                remote: {
                    url: myContextPath + "/master/model/check/DuplicateSubCategory",
                    type: "get",
                    cache: false,
                    dataType: "json",
                    data: {
                        makerName: function() {
                            return $('input[name="makerName"]').val()
                        },
                        modelId: function() {
                            return $('input[name="modelId"]').val()
                        },
                        modelName: function() {
                            return $('#modal-edit-model-form').find('input[name="modelName"]').val()
                        },
                        category: function() {
                            return $('input[name="category"]').val()
                        },
                        subcategory: function() {
                            return $('#modal-edit-model-form').find('select[name="subcategory"]').val()
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
            'subcategory': {
                remote: "Sub Category Already Exist"
            }
        }
    });
})
