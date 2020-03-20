var bankJson, boeJson;
$(function() {
    var table = $('#table-auction-payment').DataTable({
        "pageLength": 25,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ajax": myContextPath + "/accounts/daybook-data",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            targets: 0,
            "width": "100px",
            "data": "remitDate",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return data;
            }
        }, {
            targets: 1,
            "data": "daybookId",
        }, {
            targets: 2,
            "data": "remitType",
        }, {
            targets: 3,
            "visible": false,
            "data": "coaNo"
        }, {
            targets: 4,
            "data": "remitter"
        }, {
            targets: 5,
            "data": "bankName"
        }, {
            targets: 6,
            "className": "dt-right",
            "data": "amountWithOutBankCharge",
            "type": "num-fmt",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + ifNotValid(data, '') + '</span>';

            }
        }, {
            targets: 7,
            "className": "dt-right",
            "data": "bankCharges",
            "render": function(data, type, row) {
                return '<span class="autonumber"  data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + ifNotValid(data, '') + '</span>';

            }

        }, {
            targets: 8,
            "className": "dt-right",
            "data": "amount",
            "type": "num-fmt",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';

            }
        }, {
            targets: 9,
            "data": "billOfExchange"
        }, {
            targets: 10,
            "data": "lcNo"
        }, {
            targets: 11,
            "data": "customer"
        }, {
            targets: 12,
            "data": "clearingAccount",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "Bank Account"
                    className = "default"
                } else if (data == 1) {
                    status = "Clearing Account"
                    className = "success"
                }
                return '<span class="label label-' + className + '">' + status + '</span>';
            }
        }, {
            targets: 13,
            "data": "remarks"
        }, {
            targets: 14,
            "width": "50px",
            "data": "id",
            "render": function(data, type, row) {
                let html = '';
                html += '<a href="#" title="Edit Daybook Entry" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-daybook"><i class="fa fa-fw fa-edit" title="Edit" ></i></a>';
                html += '<a href="#"><i class="fa fa-fw fa-close" title="Delete" id="del" ></i></a>';
                html += '<a href="' + myContextPath + '/get/' + row.attachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&daybookId=' + row.daybookId + '" target="_blank" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                if (row.attachementViewed == 1) {
                    html += '<a href="#" name="approve" title="Approve" data-id="' + data + '"><i class="fa fa-fw fa-check"></i></a>';
                }

                return html;

            }
        }, {
            targets: 15,
            "data": "id",
            "visible": false
        }, {
            targets: 16,
            "data": "bank",
            "visible": false
        }, {
            targets: 17,
            "data": "remitTypeId",
            "visible": false
        }, {
            targets: 18,
            "data": "currency",
            "visible": false
        }, {
            targets: 19,
            "data": "customerId",
            "visible": false
        }, {
            targets: 20,
            "data": "clearingAccount",
            "visible": false
        }],
        "drawCallback": function(settings, json) {
            $('#table-auction-payment').find('span.autonumber').autoNumeric('init')
        },
        "initComplete": function(settings, json) {
            $('#table-auction-payment').find('.select2').select2({
                allowClear: true,
                width: '100%'
            });
        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.remitType == "LC Remit") {
                $('td', nRow).css('background-color', '#fff7ad');
            } else if (aData.remitType == "Branch Remit") {
                $('td', nRow).css('background-color', '#ffd4d4');
            }

        }

    });
    table.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.daybookId);
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })
    // Delete Row
    $('#table-auction-payment tbody').on('click', 'a[name="approve"]', function() {
        if (!confirm($.i18n.prop('confirm.daybook.transaction.approve'))) {
            return false;
        }
        let id = $(this).attr('data-id');

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            url: myContextPath + "/daybook/approve/entry?id=" + id,
            contentType: "application/json",
            success: function(data) {
                if (data.status == "success") {
                    table.ajax.reload();
                } else {
                    alert(data.message);
                }


            }
        });

    });
    // Delete Row
    $('#table-auction-payment tbody').on('click', 'tr td #del', function() {
        var row = $(this).parents('tr')[0];
        var mydata = (table.row(row).data());
        var data = {};
        data["id"] = mydata["id"];
        var con = confirm($.i18n.prop('common.confirm.delete'))
        if (con) {
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "post",
                data: JSON.stringify(data),
                url: myContextPath + "/accounts/delete/daybook",
                contentType: "application/json",
                async: false,
                success: function(data) {
                    table.ajax.reload();
                    var alertEle = $('#alert-block');
                    $(alertEle).css('display', '').html('<strong>Update Success!</strong> Shipment List Deleted Successfully');
                    $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                        $(alertEle).slideUp(500);
                    });
                }
            });
        } else {}
    });

    $("#entryDate").datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    $('.select2-tag').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
        tags: true
    })
    //Edit Daybook entry

    var modelEle = $('#modal-edit-daybook');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table.row(tr).data();
        var remitTypeVal = rowData["remitType"];
        $(modelEle).find("#id").val(rowData["id"]);
        $(modelEle).find("#entryDate").datepicker('setDate', rowData["remitDate"]);
        $(modelEle).find("#bank").val(rowData["bank"]).trigger('change');
        $(modelEle).find("#remitType").val(rowData["remitTypeId"]).trigger('change');
        if (rowData["remitTypeId"] == 4) {
            $(modelEle).find("#billOfExchange").val(rowData["billOfExchange"]).trigger('change');
        }
        $(modelEle).find("#currency").val(rowData["currency"]).trigger('change');
        $(modelEle).find("#remitter").val(rowData["remitter"]);
        $(modelEle).find("#hiddenRemitter").val(rowData["remitter"]);
        $(modelEle).find("#hiddenCurrency").val(rowData["currency"]);
        let currency = rowData["currency"];
        $(modelEle).find("#hiddenAmount").autoNumeric('init').autoNumeric('set', rowData["amount"]);
        $(modelEle).find("#hiddenAmountWithoutBankCharge").autoNumeric('init').autoNumeric('set', rowData["amountWithOutBankCharge"]);
        $(modelEle).find("#hiddenBankCharge").autoNumeric('init').autoNumeric('set', rowData["bankCharges"]);
        $(modelEle).find(".amountWithOutBankCharge").autoNumeric('init').autoNumeric('set', rowData["amountWithOutBankCharge"]).autoNumeric('update', {
            mDec: currency != 2 ? 0 : 2,
            aSign: rowData.currencySymbol + ' '
        });
        $(modelEle).find(".bankcharge").autoNumeric('init').autoNumeric('set', rowData["bankCharges"]).autoNumeric('update', {
            mDec: currency != 2 ? 0 : 2,
            aSign: rowData.currencySymbol + ' '
        });
        $(modelEle).find(".amount").autoNumeric('init').autoNumeric('set', rowData["amount"]).autoNumeric('update', {
            mDec: currency != 2 ? 0 : 2,
            aSign: rowData.currencySymbol + ' '
        });

        $(modelEle).find(".customerBankCharge").attr("checked", rowData.isCustomerBankCharge == 1 ? true : false);
        $(modelEle).find(".clearingAccount").attr("checked", rowData.clearingAccount == 1 ? true : false);
        $(modelEle).find("#remarks").val(rowData["remarks"]);
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input').val('').trigger('change');
        $(this).find('span.help-block').html('');
        $(this).find('.has-error').removeClass('has-error');
    }).on("keyup", "input.amountWithOutBankCharge,input.bankcharge", function(setting) {
        var amountWithOutBankCharge = getAutonumericValue($(modelEle).find(".amountWithOutBankCharge"))
        console.log(amountWithOutBankCharge)
        var bankCharges = getAutonumericValue($(modelEle).find(".bankcharge"))
        var total = $(modelEle).find(".amount")
        var hiddenTotal = $(modelEle).find(".hiddenAmount")
        var hiddenAmountWithoutBankCharge = $(modelEle).find(".hiddenAmountWithoutBankCharge")
        var hiddenBankCharge = $(modelEle).find(".hiddenBankCharge")
        var isChecked = $(modelEle).find(".customerBankCharge").is(':checked')

        $(modelEle).find("#hiddenAmountWithOutBankCharge").autoNumeric('init').autoNumeric('set', amountWithOutBankCharge)
        if (isChecked) {
            setAutonumericValue(hiddenTotal, Number(amountWithOutBankCharge))
            setAutonumericValue(total, Number(amountWithOutBankCharge))
        } else {
            setAutonumericValue(hiddenTotal, Number(amountWithOutBankCharge) - Number(bankCharges))
            setAutonumericValue(total, Number(amountWithOutBankCharge) - Number(bankCharges))
        }
        setAutonumericValue(hiddenAmountWithoutBankCharge, Number(amountWithOutBankCharge))
        setAutonumericValue(hiddenBankCharge, Number(bankCharges))

    }).on('change', '#bank', function() {
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
    }).on('change', '#remitType', function() {
        var selectedVal = $(this).find('option:selected').val();
        let container = $("#modal-edit-daybook");

        if (!isEmpty(selectedVal) && selectedVal.length > 0) {
            $('#purchasedSupplier').prop('disabled', false);
            if (selectedVal == 4) {
                updateBillofExchange($("#modal-edit-daybook").find('select.billOfExchange'))
                $('#lcRemit').css('display', '')
                $(container).find('select.currency').val(1).trigger('change');
                $(container).find('input.remitter,input.lcNo,input.staff,input.customer').prop('readonly', true);
            } else {
                $('#lcRemit').css('display', 'none')
                var hiddenRemitter = $(container).find('input.hiddenRemitter').val();
                var hiddenCurrency = $(container).find('input.hiddenCurrency').val();
                var hiddenAmount = $(container).find('input.hiddenAmount').autoNumeric('init').autoNumeric('get');
                var hiddenAmountWithoutBankCharge = $(container).find('input.hiddenAmountWithoutBankCharge').autoNumeric('init').autoNumeric('get');
                var hiddenBankCharge = $(container).find('input.hiddenBankCharge').autoNumeric('init').autoNumeric('get');
                $(container).find('select.billOfExchange').val('').trigger('change');
                $(container).find('select.currency').val(hiddenCurrency).trigger('change');
                $(container).find('input.remitter').val(hiddenRemitter);
                $(container).find('input.amount').autoNumeric('init').autoNumeric('set', hiddenAmount);
                $(container).find('input.amountWithOutBankCharge').autoNumeric('init').autoNumeric('set', hiddenAmountWithoutBankCharge);
                $(container).find('input.bankcharge').autoNumeric('init').autoNumeric('set', hiddenBankCharge);
                $(container).find('input.remitter,input.lcNo,input.staff,input.customer').prop('readonly', false);
            }
        }
    }).on('change', '#billOfExchange', function() {
        let data = $(this).select2('data')[0];
        let container = $("#modal-edit-daybook");
        if (!isEmpty(data)) {
            data = data.data;
            container.find('input.remitter').val(data.customerName);
            container.find('input.lcNo').val(data.lcNo);
            container.find('input.customer').val(data.customerName);
            container.find('input.customerId').val(data.customerId);
            container.find('input.staff').val(data.salesPerson);
            setAutonumericValue(container.find('input.amount'), data.amount);
            setAutonumericValue(container.find('input.amountWithOutBankCharge'), data.amount);
            setAutonumericValue(container.find('input.bankcharge'), 0);
        } else {
            container.find('input.remitter').val('');
            container.find('input.lcNo').val('');
            container.find('input.customer').val('');
            container.find('input.customerId').val('');
            container.find('input.staff').val('');
            //             setAutonumericValue(container.find('input.amount'), 0);
        }
    })
    $('#currency').on('change', function() {
        var data = $(this).find(':selected').data('data');
        let val = $(this).val();
        if (!isEmpty(data.data)) {
            $('.amountWithOutBankCharge, .bankcharge, .amount').autoNumeric('init').autoNumeric('update', {
                //aSep: '',
                //aDec: '0',
                mDec: val != 2 ? 0 : 2,
                aSign: data.data.symbol + ' '
            });
        }
    })
    $('.customerBankCharge').on('change', function() {
        var amountWithOutBankCharge = getAutonumericValue($(modelEle).find(".amountWithOutBankCharge"))
        var bankCharges = getAutonumericValue($(modelEle).find(".bankcharge"))
        var total = $(modelEle).find(".amount")
        var hiddenTotal = $(modelEle).find(".hiddenAmount")
        var isChecked = $(modelEle).find(".customerBankCharge").is(':checked')
        if (isChecked) {
            setAutonumericValue(hiddenTotal, Number(amountWithOutBankCharge))
            setAutonumericValue(total, Number(amountWithOutBankCharge))
        } else {
            setAutonumericValue(hiddenTotal, Number(amountWithOutBankCharge) - Number(bankCharges))
            setAutonumericValue(total, Number(amountWithOutBankCharge) - Number(bankCharges))
        }
    })
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Table Search Filter - Start
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });

    var purchased_min;
    var purchased_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })
    $.getJSON(myContextPath + '/data/bank.json', function(data) {
        bankJson = data;
        $('#bankFilter,#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        })
    })
    $.getJSON(myContextPath + "/data/accnameFilter.json", function(data) {
        mcoaJson = data;
        $('#coaNo').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mcoaJson, function(item) {
                return {
                    id: item.code,
                    text: item.code,
                };
            })
        })

    });
    $.getJSON(myContextPath + "/data/mRemitType.json", function(data) {
        mRemitTypeJson = data;
        $('#remitType').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mRemitTypeJson, function(item) {
                return {
                    id: item.remitSeq,
                    text: item.remitType,
                };
            })
        })

    });
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        mCurrencyJSON = data;
        $('#currency,#currencyFilter').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mCurrencyJSON, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.symbol + '(' + item.currency + ')',
                    data: item
                };
            })
        }).val('').trigger('change')

    });

    $('#accountTypeFilter').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
        width: '100%'
    }).on("change", function(event) {
        filterAccountType = $(this).find('option:selected').val();
        table.draw();
    })

    var filterBank = $('#bankFilter').find('option:selected').val();
    var filterCoa = $('#coaNo').find('option:selected').val();
    var filterRemType = $('#remitTypeFilter').find('option:selected').val();
    var filterCurrency = $('#currencyFilter').find('option:selected').val();
    var filterAccountType = $('#accountTypeFilter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[0].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[0], 'DD-MM-YYYY')._d.getTime();
            }
            if (purchased_min && !isNaN(purchased_min)) {
                if (aData._date < purchased_min) {
                    return false;
                }
            }
            if (purchased_max && !isNaN(purchased_max)) {
                if (aData._date > purchased_max) {
                    return false;
                }
            }

        }
        //purchase type filter
        if (typeof filterBank != 'undefined' && filterBank.length != '') {
            if (aData[16].length == 0 || aData[16] != filterBank) {
                return false;
            }
        }
        if (typeof filterRemType != 'undefined' && filterRemType.length != '') {
            if (aData[17].length == 0 || aData[17] != filterRemType) {
                return false;
            }
        }
        if (typeof filterCurrency != 'undefined' && filterCurrency.length != '') {
            if (aData[18].length == 0 || aData[18] != filterCurrency) {
                return false;
            }
        }
        if (typeof filterAccountType != 'undefined' && filterAccountType.length != '') {
            if (aData[20].length == 0 || aData[20] != filterAccountType) {
                return false;
            }
        }
        if (typeof filterCoa != 'undefined' && filterCoa.length != '') {
            if (aData[3].length == 0 || aData[3] != filterCoa) {
                return false;
            }
        }
        //Supplier filter

        return true;
    });
    $('#bankFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterBank = $('#bankFilter').val();
        table.draw();
    });
    $('#remitTypeFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterRemType = $('#remitTypeFilter').val();
        table.draw();
    });
    $('#currencyFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterCurrency = $('#currencyFilter').val();
        table.draw();
    });
    $('#coaNo').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterCoa = $('#coaNo').val();
        table.draw();
    });

    //Table Search Filter - end
    $('#billOfExchange').select2({
        allowClear: true,
        width: '100%'
    })

    //slider for status

    var len = $('.acc').length;
    var widthOneItem12 = parseInt(len) * parseInt($(".col-md-2").first().css("width"));
    $(".arrow-left").click(function() {
        $(".offer-pg-cont").animate({
            scrollLeft: "-=" + widthOneItem
        });
    });
    $(".arrow-right").click(function() {
        $(".offer-pg-cont").animate({
            scrollLeft: "+=" + widthOneItem
        });
    });

    //Edit Model $("#modal-edit-daybook").'change', '.categorysel',
    $("#modal-edit-daybook").on('click', '#btn-create-order', function() {
        if (!$('#modal-edit-daybook form#form-daybook-edit').valid()) {
            return;
        }
        var object;
        var data = {};
        let currencyType = $('#modal-edit-daybook').find('#bank option:selected').data('data')
        let remitType = $('#modal-edit-daybook').find('#remitType option:selected').val()
        if (remitType == 4) {
            if (currencyType.data.currencyType != 1) {
                $("#modal-edit-daybook").find('#bank').closest('div').find('.help-block').text('Please Select Valid Bank');
                alert("Please Change Bank With Yen")
                return false

            }
        }

        autoNumericSetRawValue($("#modal-edit-daybook").find('input[name="amount"],input[name="bankCharges"],input[name="amountWithOutBankCharge"]'))
        object = getFormData($('#modal-edit-daybook').find('.data'));
        data = object;
        var customerBankCharge;
        var clearingAccount;
        if ($("#modal-edit-daybook").find('.customerBankCharge').is(':checked')) {
            customerBankCharge = 1;
        } else {
            customerBankCharge = 0;
        }
        data['customerBankCharge'] = customerBankCharge;
        if ($("#modal-edit-daybook").find('.clearingAccount').is(':checked')) {
            clearingAccount = 1;
        } else {
            clearingAccount = 0;
        }
        data['clearingAccount'] = clearingAccount;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/update/daybook",
            contentType: "application/json",
            async: false,

            success: function(data) {
                table.ajax.reload();
                $('#modal-edit-daybook').modal('toggle');
            }
        });
    });
});

function updateBillofExchange(element) {

    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        async: false,
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

function updateAttachmentViewedStatus(daybookId) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/daybook/update/attachmentView/slip?daybookId=" + daybookId,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}
