$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('#hsCode-form').validate({
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
            'cc': {
                'required': true,
                remote: {
                    url: myContextPath + "/master/hsCode/validHsCode",
                    type: "get",
                    cache: false,
                    async: false,
                    dataType: "json",
                    data: {
                        cc: function() {
                            return $('input[name="cc"]').val()
                        },
                        category: function() {
                            return $('#categoryDrop').val()
                        },
                         subCategory: function() {
                            return $('#subCategoryDrop').val()
                        },
                        hsCode: function() {
                            return $('input[name="hsCode"]').val()
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
            'category': {
                'required': true,
                remote: {
                    url: myContextPath + "/master/hsCode/validHsCode",
                    type: "get",
                    cache: false,
                    async: false,
                    dataType: "json",
                    data: {
                        cc: function() {
                            return $('input[name="cc"]').val()
                        },
                        category: function() {
                            return $('#categoryDrop').val()
                        },
                         subCategory: function() {
                            return $('#subCategoryDrop').val()
                        },
                        hsCode: function() {
                            return $('input[name="hsCode"]').val()
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
            'subCategory': {
                'required': true,
                remote: {
                    url: myContextPath + "/master/hsCode/validHsCode",
                    type: "get",
                    cache: false,
                    async: false,
                    dataType: "json",
                    data: {
                        cc: function() {
                            return $('input[name="cc"]').val()
                        },
                        category: function() {
                            return $('#categoryDrop').val()
                        },
                        subCategory: function() {
                            return $('#subCategoryDrop').val()
                        },
                        hsCode: function() {
                            return $('input[name="hsCode"]').val()
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
            'hsCode': {
                'required': true,
                remote: {
                    url: myContextPath + "/master/hsCode/validHsCode",
                    type: "get",
                    cache: false,
                    async: false,
                    dataType: "json",
                    data: {
                        cc: function() {
                            return $('input[name="cc"]').val()
                        },
                        category: function() {
                            return $('#categoryDrop').val()
                        },
                         subCategory: function() {
                            return $('#subCategoryDrop').val()
                        },
                        hsCode: function() {
                            return $('input[name="hsCode"]').val()
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
            'hsCode': {
                remote: "This Combination Is Already Exist"
            }
        }
    });
})
