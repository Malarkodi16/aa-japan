$(function() {

    $.validator.addMethod("chassisno-exist", function(value, element, parms) {
        var isExist = checkChassisNo(value);
        if (isExist) {
            var saveExistingChassisNo = $('#saveExistingChassisNo').val();
            if (!isEmpty(saveExistingChassisNo) && saveExistingChassisNo == 0) {
                return false;
            } else if (!isEmpty(saveExistingChassisNo) && saveExistingChassisNo == 1) {
                return true;
            } else {
                if (!confirm($.i18n.prop('common.alert.chassisNo.check.exist'))) {
                    $('#saveExistingChassisNo').val(0)
                    return false;
                } else {
                    $('#saveExistingChassisNo').val(1)
                    return true;
                }
            }

        } else {
            $('#saveExistingChassisNo').val(-1)
        }

        return true;
    }, 'Chassis no exist!');

    $('#receivedForm').validate({
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
            'documentConvertTo': 'required',

        }
    });
    $('#notReceivedForm').validate({
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

        }
    });
    $('#docType-update').validate({
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
            'docType': 'required',
            'receivedDate': 'required',
            'documentConvertTo': 'required'

        }
    });

    $('#stock-edit-detail').validate({
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
            'sFirstRegDate': 'required',
            'mileage': 'required',
            'chassisNo': {
                'required': true,
                'chassisno-exist': true
            }

        }
    });
})
