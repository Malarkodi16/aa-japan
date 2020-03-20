var rconfirm;
let confirmDialogShowing = false;
$(function() {
//     $(document).on('focusout', '.has-error', function() {
//         $(this).find('input,select,textarea').valid();
//     })
//     jQuery.validator.addClassRules('valid-schedule-date', {
//         'vaild_schedule_date': true
//     });
    jQuery.validator.addClassRules('duplicate-country-port', {
        'duplicate_country_port': true
    });
    $.validator.addMethod('duplicate_country_port', function(value, element, param) {
        var closestEle = $(element).closest('.clone-container-location-toclone');
        var allElement = $(element).closest('.clone-container-location-toclone').prevAll();

        var arr = allElement.toArray().map(e=>{
            return ifNotValid($(e).find('select.country').val(), '').toUpperCase() + '' + ifNotValid($(e).find('select.port').val(), '').toUpperCase();
        }
        );
        var currentVal = ifNotValid($(closestEle).find('select.country').val(), '').toUpperCase() + '' + ifNotValid($(closestEle).find('select.port').val(), '').toUpperCase();
        return (arr.length < 1 || $.inArray(currentVal, arr) == -1) ? true : false;

    }, "Duplicate port");
    //     $.validator.addMethod('vaild_schedule_date', function(value, element, param) {
    //         let dateElementArr = $('#shipment-schedule-wrapper input.schedule-date').toArray();
    //         let index = dateElementArr.indexOf(element);
    //         var prevVal = index > 0 ? $(dateElementArr[index - 1]).val() : undefined;
    //         var currentVal = $(element).closest('.clone-container-location-toclone').find('input.schedule-date').val();
    //         if (typeof prevVal == 'undefined') {
    //             return true;
    //         } else if (!isEmpty(prevVal) && !isEmpty(currentVal)) {
    //             //              return moment(currentVal).format('DD-MM-YYYY')>=moment(prevVal).format('DD-MM-YYYY')?true:false;
    //             return moment(currentVal, 'DD-MM-YYYY').isSameOrAfter(moment(prevVal, 'DD-MM-YYYY'));
    //         }
    //         return true;

    //     }, "Not valid");
    $.validator.addMethod("voyageNo-exist", function(value, element, parms) {
        var isExist = checkVoyageNo();
        //          var isExist =true;
        if (isExist) {
            var saveExistingVoyageNo = $('#saveExistingVoyageNo').val();
            if (saveExistingVoyageNo == 0) {
                $('input[name="voyageNo"]').val('');
                return false;
            } else if (saveExistingVoyageNo == 1) {
                return true;
            } else {
                if (!confirmDialogShowing) {
                    $.confirm({
                        title: 'Confirm!',
                        content: "Another voyage already exist with same number and vessel name, do you want to continue?",
                        buttons: {
                            Yes: function() {
                                $('#saveExistingVoyageNo').val(1)
                            },
                            No: {
                                text: 'No',
                                btnClass: 'btn-blue',
                                //                             keys: ['enter', 'shift'],
                                action: function() {
                                    $('#saveExistingVoyageNo').val(-1);
                                    $('#voyageNo').val('');

                                }
                            }
                        },
                        onOpenBefore: function() {
                            confirmDialogShowing = true;
                        },
                        onDestroy: function() {
                            confirmDialogShowing = false;
                        }
                    });
                }

            }

        } else {
            $('#saveExistingVoyageNo').val(-1)
        }

        return true;
    }, 'Voyage No exist!');

    $('#addshipment').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
//         onkeyup: false,
//         onclick: false,
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
            'shippingCompanyNo': 'required',
            'shipId': 'required',
            'destinationCountry': 'required',
            'destinationPort': 'required',
            'shippingCompanyNo': 'required',
            'deckHeight': {
                number: true
            },
            'schedule.date': 'required',
            'voyageNo': {
                'required': true,
                'voyageNo-exist': true //                 remote: {
                //                     url: myContextPath + "/shipping/schedule/check/isVoyagenoExists",
                //                     type: "get",
                //                     cache: false,
                //                     dataType: "json",
                //                     data: {
                //                         scheduleId: function() {
                //                             return $('#scheduleId').val()
                //                         },
                //                         shippingCompanyNo: function() {
                //                             return $('#shippingCompanyNo').val()
                //                         },
                //                         voyageNo: function() {
                //                             return $('input[name="voyageNo"]').val()
                //                         },
                //                         shipId: function() {
                //                             return $('select[name="shipId"]').val()
                //                         }
                //                     },
                //                     dataFilter: function(response) {
                //                         return response == "false" ? true : false;

                //                     }
                //                 }
            }

        }//         ,
        //         messages: {
        //             'voyageNo': {

        //                 remote: function() {
        //                     rconfirm = confirm("Another voyage already exist with same number and vessel name, do you want to continue?");
        //                     if (voyageNo) {
        //                         var saveExistingVoyageNo = $('#saveExistingVoyageNo').val();
        //                         if (!isEmpty(saveExistingVoyageNo) && !rconfirm) {
        //                             return false;
        //                         } else if (!isEmpty(saveExistingVoyageNo) && rconfirm) {
        //                             return true;
        //                         } else {
        //                             $('#saveExistingVoyageNo').val(-1)
        //                         }

        //                     }

        //                     //                     rconfirm = confirm("Another voyage already exist with same number and vessel name, do you want to continue?");
        //                     //                     if (!rconfirm) {
        //                     //                         $('input[name="voyageNo"]').val('');
        //                     //                         $('input[name="voyageNo"]').closest('span.help-block').find('.has-error').removeClass('has-error');
        //                     //                         return;
        //                     //                     }else{
        //                     //                         $('input[name="voyageNo"]').closest('span.help-block').find('.has-error').removeClass('has-error');
        //                     //                     }

        //                 }
        //             }
        //         }
    });

})
