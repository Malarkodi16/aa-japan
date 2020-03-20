let confirmDialogShowing = false;
$(function() {
    $.validator.addMethod("regex", function(value, element, regexp) {
        var re = new RegExp(regexp);
        return this.optional(element) || re.test(value);
    }, "Invalid!");

    $.validator.addMethod("valid-date", function(value, element, parms) {
        if (!isEmpty(value)) {
            return moment(value, 'YYYY/MM').isValid();
        }
        return true;

    }, "Date is not valid!");

    $.validator.addMethod("not-exist", function(value, element, parms) {
        let stockNo = $('input[name="stock.stockNo"]').val();
        let purchaseDate = $('input[name="stock.purchaseInfo.date"]').val();
        let lotNo = $('input[name="stock.purchaseInfo.auctionInfo.lotNo"]').val()
        let supplier = $('select[name="stock.purchaseInfo.supplier"]').val()
        let auctionHouse = $('select[name="stock.purchaseInfo.auctionInfo.auctionHouse"]').val()
        if (isEmpty(purchaseDate) || isEmpty(lotNo) || isEmpty(supplier) || isEmpty(auctionHouse)) {
            return true;
        }

        let exists = false;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/check/existing/stock",
            type: "get",
            cache: false,
            dataType: "json",
            async: false,
            data: {
                stockNo: stockNo,
                purchaseDate: purchaseDate,
                lotNo: lotNo,
                supplier: supplier,
                auctionHouse: auctionHouse

            },
            success: function(data) {
                exists = data;
            }

        })

        return !exists;
    }, 'Already exists');
    jQuery.validator.addClassRules('validate-stock-exist', {
        required: true
    });
    $.validator.addMethod("chassisno-exist", function(value, element, parms) {
        var isExist = checkChassisNo($('#chassisNo').val());
        //          var isExist =true;
        if (isExist) {
            var saveExistingChassisNo = $('#saveExistingChassisNo').val();
            if (!isEmpty(saveExistingChassisNo) && saveExistingChassisNo == 0) {
                return false;
            } else if (!isEmpty(saveExistingChassisNo) && saveExistingChassisNo == 1) {
                return true;
            } else {
                if (!confirmDialogShowing) {
                    $.confirm({
                        title: 'Confirm!',
                        content: $.i18n.prop('common.alert.chassisNo.check.exist'),
                        buttons: {
                            Yes: function() {
                                $('#saveExistingChassisNo').val(1)
                            },
                            No: {
                                text: 'No',
                                btnClass: 'btn-blue',
                                //                             keys: ['enter', 'shift'],
                                action: function() {
                                    $('#saveExistingChassisNo').val(0)
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
            $('#saveExistingChassisNo').val(-1)
        }

        return true;
    }, 'Chassis no exist!');
    $.validator.addMethod("not-more-than-current-date", function(value, element, parms) {
        if (!isEmpty(value)) {
            return !moment(value, 'YYYY/MM').isSameOrAfter(new Date());
        }
        return true;

    }, "Date greater than today!");
    $('#stockSearchForm').validate({

        highlight: function(element) {

            $(element).parent().parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().parent().removeClass("has-error");
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
            stockNo: {
                required: true,
                remote: {
                    url: myContextPath + "/stock/isfound",
                    type: "POST",
                    cache: false,
                    dataType: "json",
                    data: {
                        stockNo: function() {
                            return $('#stockNo').val();
                        }
                    },
                    dataFilter: function(response) {
                        return response;
                    }
                }
            }

        },
        messages: {
            stockNo: {
                remote: "Stock number does not exist!"
            }
        }
    });
    $('#stockEntryForm').validate({
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
            'stock.purchaseInfo.auctionInfo.lotNo': {
                'required': true,
                'not-exist': true

            },
            'stock.purchaseInfo.type': 'required',
            'stock.purchaseInfo.date': {
                required: true,
                remote: {
                    url: myContextPath + "/stock/chassisNo/purchaseDate/isSame",
                    type: "POST",
                    cache: false,
                    dataType: "json",
                    data: {
                        stockNo: function() {
                            return $('input[name="stock.stockNo"]').val();
                        },
                        chassisNo: function() {
                            return $('#chassisNo').val();
                        },
                        purchaseDate: function() {
                            return $('input[name="stock.purchaseInfo.date"]').val();
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
            'stock.purchaseInfo.supplier': 'required',
            'stock.purchaseInfo.auctionInfo.auctionHouse': 'required',
            //             'stock.purchaseInfo.auctionInfo.lotNo': 'required',
            // 'stock.purchaseInfo.auctionInfo.posNo': 'required',
            'stock.sFirstRegDate': {
                'required': false,
                'regex': '^(\\d{4}/\\d{2})|(\\d{4}/__)$',
                'valid-date': true,
                'not-more-than-current-date': true
            },
            'stock.transmission': 'required',
            'stock.maker': 'required',
            'stock.model': 'required',
            'stock.fuel': 'required',
            'stock.driven': 'required',
            //             'stock.reservedInfo.salesPersonId': 'required',
            'stock.chassisNo': {
                'required': true,
                'chassisno-exist': true,
                remote: {
                    url: myContextPath + "/stock/chassisNo/purchaseDate/isSame",
                    type: "POST",
                    cache: false,
                    dataType: "json",
                    data: {
                        stockNo: function() {
                            return $('input[name="stock.stockNo"]').val();
                        },
                        chassisNo: function() {
                            return $('#chassisNo').val();
                        },
                        purchaseDate: function() {
                            return $('input[name="stock.purchaseInfo.date"]').val();
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
            'stock.noOfDoors': {
                number: true,
            },
            'stock.noOfSeat': {
                regex: '^[0-9/]+$',
            }
        },
        messages: {
            'stock.chassisNo': {
                remote: function() {
                    return $.i18n.prop('alert.stock.chassisno.purchasedate')
                }
            },
            'stock.purchaseInfo.date': {
                remote: function() {
                    return $.i18n.prop('alert.stock.chassisno.purchasedate')
                }
            }
        }
    });

})
