var bankJson, mRemitTypeJson;
$(function() {
    $(".settled").hide();
    $('.select2-tag').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
        tags: true
    })

    //     $('input[type="checkbox"].minimal').iCheck({
    //         checkboxClass: 'icheckbox_minimal-blue',
    //         radioClass: 'iradio_minimal-blue'
    //     })

    /*numbers*/
    // $('.autonumber').autoNumeric('init');

    $.getJSON(myContextPath + '/data/currency.json', function(data) {
        currencyJson = data;
        $('#currency').select2({
            allowClear: true,
            width: '100%',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.symbol + "-" + item.currency,
                    data: item
                };
            })
        }).val('').trigger('change')
    })

    //     $.getJSON(myContextPath + "/data/mRemitType.json", function(data) {
    //         mRemitTypeJson = data;
    //         $('#remitType').select2({
    //             allowClear: true,
    //             width: '100%',
    //             placeholder: 'All',
    //             data: $.map(mRemitTypeJson, function(item) {
    //                 return {
    //                     id: item.remitSeq,
    //                     text: item.remitType,
    //                 };
    //             })
    //         }).val('').trigger('change')

    //     });

    $.getJSON(myContextPath + '/data/bank.json', function(data) {
        bankJson = data;
        $('#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        }).val('').trigger('change')
        $('.coaNo').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.coaCode,
                    text: item.coaCode + ' - ' + item.bankName,
                    data: item
                };
            })
        })
    })
    $('#bank').on('change', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data.data)) {
            $('.coaNo').val(data.data.coaCode).trigger('change');
            $('.currency').val(data.data.currencyType).trigger('change');
            if (data.data.currencyType != 1) {
                $('div#clearingAccount').show();

            } else {
                $('div#clearingAccount').hide();
                $('input[name="clearingAccount"]').prop('checked', false);
            }
        } else if (isEmpty(data.data)) {
            $('.coaNo').val('').trigger('change');
            $('.currency').val('').trigger('change');
            $('div#clearingAccount').hide();
            $('input[name="clearingAccount"]').prop('checked', false);
        }
    })

    $('#currency').on('change', function() {
        var data = $(this).find(':selected').data('data');
        let val = $(this).val();
        if (!isEmpty(data.data)) {
            $('.amountWithOutBankCharge, .bankcharge, .amount').autoNumeric('init').autoNumeric('update', {
                //aSep: '',
                mDec: val != 2 ? 0 : 2,
                aSign: data.data.symbol + ' '
            });
        }
    })
    $("input[name='settled']").click(function() {
        var test = $(this).val();

        if (test == 1) {
            $(".received").hide();
            $('.daybook-entry').hide();
            $(".settled").show();
        } else {
            $(".received").show();
            $('.daybook-entry').show();
            $(".settled").hide();
        }
    });
    $('#tranType').select();

    $('.entryDate').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })

    //$('.amount, .bankcharge, .amountWithOutBankCharge').autoNumeric('init')
    //on select billOfExchange
    $(document).on('change', 'select.billOfExchange', function() {
        let data = $(this).select2('data')[0];
        let container = $(this).closest('.clone-container-location');
        if (!isEmpty(data)) {
            data = data.data;
            container.find('input.remitter').val(data.customerName);
            container.find('input.lcNo').val(data.lcNo);
            container.find('input.customer').val(data.customerName);
            container.find('input.staff').val(data.salesPerson);
            setAutonumericValue(container.find('input.amount'), data.amount);
            setAutonumericValue(container.find('input.amountWithOutBankCharge'), data.amount);
            setAutonumericValue(container.find('input.bankcharge'), 0);
        } else {
            container.find('input.remitter').val('');
            container.find('input.lcNo').val('');
            container.find('input.customer').val('');
            container.find('input.staff').val('');
            setAutonumericValue(container.find('input.amount'), 0);
            setAutonumericValue(container.find('input.amountWithOutBankCharge'), 0);
        }
    })
    //Remit Type On Change
    $('#daybook-entry').on('change', '.remitType', function() {
        var selectedVal = $(this).find('option:selected').val();
        let container = $(this).closest('.clone-container-location');
        if (selectedVal == 4) {
            $(container).find('.lcRemit').css('display', '')
            //             $('.currency').val(1).trigger('change');
            $(container).find('input.remitter,input.lcNo,input.staff,input.customer').prop('readonly', true);
            updateBillofExchange($(container).find('select.billOfExchange'))
        } else {
            $(container).find('.lcRemit').css('display', 'none')
            $(container).find('select.billOfExchange').val('').trigger('change');
            //             $('.currency').val('').trigger('change');
            $(container).find('input.remitter,input.lcNo,input.staff,input.customer').prop('readonly', false);

        }
    });
    function updateBillofExchange(element) {
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/daybook/get/billofexchange/details",
            success: function(data) {
                $(element).empty();
                $(element).select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data, function(item) {
                        return {
                            id: item.billOfExchangeNo,
                            text: item.billOfExchangeNo,
                            data: item
                        };
                    })
                }).val('').trigger('change');
            }
        });
    }

    $('#billOfExchange').select2({
        allowClear: true,
        width: '100%'
    })
    //     .on('change', function() {
    //         var boe = $(this).select2('data');
    //         if (!isEmpty(boe)) {
    //             boe = boe[0]["data"];
    //             $('#lcNo').val(boe.lcNo);
    //             $('#customer').val(boe.customer);
    //             $('#staff').val(boe.createdBy);
    //         } else {
    //             $('#lcNo').val('');
    //             $('#customer').val('');
    //             $('#staff').val('');

    //         }

    //         //         $.each(boeJson, function(i, item) {
    //         //             if (item.billOfExchange == boe) {
    //         //                 lcno = item.lcNo;
    //         //                 customer = item.customer;
    //         //                 staff = item.staff;
    //         //                 return false;
    //         //             } else {
    //         //                 $('#lcNo').val('');
    //         //                 $('#customer').val('');
    //         //                 $('#staff').val('');
    //         //                 return;
    //         //             }

    //         //         });
    //         //         if (boe != 'null') {
    //         //             $('#lcNo').val(lcno);
    //         //             $('#customer').val(customer);
    //         //             $('#staff').val(staff);
    //         //             return;
    //         //         }

    //     });
    $("#daybook-entry").on("click", ".delete", function(e) {
        $(this).closest('.row').remove();
    })

    $('#daybookForm').on('submit', function(e) {

        if ($(this).valid()) {

            var autoNumericElements = $('.autonumber');
            autoNumericSetRawValue(autoNumericElements)
        }
    })

    //Add Row
    $('#daybook-entry').cloneya({
        minimum: 1,
        maximum: 999,
        cloneThis: '.clone-container-location',
        valueClone: false,
        dataClone: false,
        deepClone: false,
        cloneButton: '.row>.btn-container>.btn-clone',
        deleteButton: '.row>.btn-container>.btn-delete',
        clonePosition: 'after',
        serializeID: true,
        serializeIndex: true,
        preserveChildCount: true
    }).on('before_clone.cloneya', function(event, toclone) {
        $(toclone).find('.select2-tag').each(function() {
            if ($(this).data('select2')) {
                $(this).select2('destroy');
            }
        });
        $(toclone).find('.amount, .bankcharge, .amountWithOutBankCharge').autoNumeric('init');
    }).on('after_append.cloneya', function(event, toclone, newclone) {
        $(toclone).find('.select2-tag').select2({
            allowClear: true
        });
        let taxcode = $(toclone).find('select.coaNo').val();
        let currency = $(toclone).find('select.currency').val();
        $(newclone).find('select.coaNo').val(taxcode);
        $(newclone).find('select.currency').val(currency);
        //         $(newclone).find('#lcRemit').css("display", "none");
        $(newclone).find('.select2-tag').select2({
            allowClear: true
        });
        $(newclone).find('.amount, .bankcharge, .amountWithOutBankCharge').autoNumeric('init');

    });
    $(document).on('keyup', 'input.amountWithOutBankCharge,input.amount', function() {
        calculateAmount($(this));
    })
    $(document).on('change', 'input.customerBankCharge', function() {
        calculateAmount($(this));
    })
    function calculateAmount(element) {
        let wrapper = $(element).closest('.clone-container-location');
        let amountWithOutBankCharge = getAutonumericValue(wrapper.find('input.amountWithOutBankCharge'));
        let totalAmount = getAutonumericValue(wrapper.find('input.amount'));
        let bankcharge = wrapper.find('input.bankcharge');
        let totalAmountEle = wrapper.find('input.amount');
        let isChecked = wrapper.find('input.customerBankCharge').is(":checked");
        if (isChecked) {
            //setAutonumericValue(bankcharge, 0);
            setAutonumericValue(totalAmountEle, Number(amountWithOutBankCharge));
        } else {
            setAutonumericValue(bankcharge, Number(amountWithOutBankCharge) - Number(totalAmount));
        }
    }
    $('#daybookForm').submit('#btn-save', function(e) {
        var autoNumericElement = $(this).find('input.amount,input.bankcharge,input.amountWithOutBankCharge')

        for (var i = 0; i < autoNumericElement.length; i++) {
            autoNumericSetRawValue(autoNumericElement[i]);
        }

    })
    var table = $('#table-auction-payment').DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ajax": {
            "url": myContextPath + "/accounts/payment-daybook",
            "data": {
                "status": $("#invoiceType").val()
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            visible: false,
            className: 'select-checkbox',
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "createdDate"
        }, {
            targets: 2,
            "data": "dueDate",
        }, {
            targets: 3,
            "data": "invoiceNo"
        }, {
            targets: 4,
            "data": "type"
        }, {
            targets: 5,
            "data": "bank"
        }, {
            targets: 6,
            "data": "invoiceName"
        }, {

            targets: 7,
            "data": "totalAmount"

        }, {
            targets: 8,
            "data": "paymentStatus",
            visible: false,
            "render": function(data, type, row) {
                row.paymentStatus = row.paymentStatus == null ? '' : row.paymentStatus;
                if (row.paymentStatus.length > 0) {
                    if (row.paymentStatus == 'initiated') {
                        return '<a><i class="fa fa-fw fa-pause" id="edit" ></i></a>';
                    } else {
                        return '<a><i class="fa fa-fw fa-check" id="edit" ></i></a>';
                    }
                    return row.paymentStatus;
                }
            },

        }],
        "initComplete": function(settings, json) {
            $('#table-auction-payment').find('.select2').select2({
                allowClear: true,
                width: '100%'
            });
        },

    });
    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    });

    function datediff(first, second) {
        // Take the difference between the dates and divide by milliseconds per day.
        // Round to nearest whole number to deal with DST.
        return Math.round((second - first) / (1000 * 60 * 60 * 24));
    }

});
