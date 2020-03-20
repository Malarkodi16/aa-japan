var bankJson, boeJson;
$(function() {

    // Date range picker
    var remit_date_min;
    var remit_date_max;
    $('#table-filter-remit-date').daterangepicker({
        autoUpdateInput: false,
        showDropdowns: true,
        //minYear: 1901,
        //maxYear: parseInt(moment().format('YYYY')),
        "autoApply": true,
        "minDate": "01/01/1970",
        "maxDate": moment().format('MM/DD/YYYY')
    }).on("apply.daterangepicker", function(e, picker) {
        remit_date_min = picker.startDate.format('DD-MM-YYYY');
        remit_date_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(remit_date_min + ' - ' + remit_date_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        remit_date_min = '';
        remit_date_max = '';
        $('#table-filter-remit-date').val('');
        $(this).remove();

    })

    var table = $('#table-auction-payment').DataTable({
        "pageLength": 25,
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ajax": {
            url: myContextPath + "/daybook/approved/data-list",
            data: function(data) {
                data.fromDate = remit_date_min;
                data.toDate = remit_date_max;
            }
        },
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
                return '<span class="autonumber"  data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, '') + '</span>';

            }

        }, {
            targets: 8,
            "className": "dt-right",
            "data": "amount",
            "type": "num-fmt",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "  data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';

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
            "data": "remarks"
        }, {
            targets: 13,
            "data": "id",
            "visible": false,
            "render": function(data, type, row) {
                let html = '';
                return html;

            }
        }, {
            targets: 14,
            "data": "id",
            "visible": false
        }, {
            targets: 15,
            "data": "bank",
            "visible": false
        }, {
            targets: 16,
            "data": "remitTypeId",
            "visible": false
        }, {
            targets: 17,
            "data": "currency",
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

    $('#btn-search').on('click', function() {
        table.ajax.reload();
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
                table.ajax.reload();

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
    $('#table-auction-payment').on('click', '#edit', function() {
        var row = $(this).parents('tr')[0];
        var mydata = (table.row(row).data());
        var data = {};
        var date = mydata["remitDate"];
        var bank = mydata["bank"];
        var remitTypeVal = mydata["remitType"];
        $("#modal-edit-daybook").modal('show');
        $("#modal-edit-daybook").find("#id").val(mydata["id"]);
        $("#modal-edit-daybook").find("#entryDate").datepicker('setDate', date);
        $("#modal-edit-daybook").find("#bank").val(mydata["bank"]).trigger('change');
        $("#modal-edit-daybook").find("#remitType").val(mydata["remitType"]).trigger('change');
        $("#modal-edit-daybook").find("#currency").val(mydata["currency"]).trigger('change');
        $("#modal-edit-daybook").find("#remitter").val(mydata["remitter"]);
        $("#modal-edit-daybook").find(".amount").autoNumeric('init').autoNumeric('set', mydata["amount"]);
        $("#modal-edit-daybook").find(".bankcharge").autoNumeric('init').autoNumeric('set', mydata["bankCharges"]);
        $("#modal-edit-daybook").find("#lcNo").val(mydata["lcNo"]);
        $("#modal-edit-daybook").find("#staff").val(mydata["staff"]);
        $("#modal-edit-daybook").find("#customer").val(mydata["customer"]);
        $("#modal-edit-daybook").find("#remarks").val(mydata["remarks"]);
        if (mydata["remitType"] == 4) {
            $("#modal-edit-daybook").find(".billOfExchange").val(mydata["billOfExchange"]).trigger('change');
        }

    });
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/\s+/g, "").replace(/¥/g, "");
    }
    ;// Table Search Filter - Start
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });

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
                };
            })
        })

    });

    var filterBank = $('#bankFilter').find('option:selected').val();
    var filterCoa = $('#coaNo').find('option:selected').val();
    var filterRemType = $('#remitTypeFilter').find('option:selected').val();
    var filterCurrency = $('#currencyFilter').find('option:selected').val();
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
            if (aData[15].length == 0 || aData[15] != filterBank) {
                return false;
            }
        }
        if (typeof filterRemType != 'undefined' && filterRemType.length != '') {
            if (aData[16].length == 0 || aData[16] != filterRemType) {
                return false;
            }
        }
        if (typeof filterCurrency != 'undefined' && filterCurrency.length != '') {
            if (aData[17].length == 0 || aData[17] != filterCurrency) {
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

    //Edit Model $("#modal-edit-daybook").'change', '.categorysel',
    $("#modal-edit-daybook").on('change', '#remitType', function() {
        var selectedVal = $(this).find('option:selected').val();
        let container = $("#modal-edit-daybook");
        updateBillofExchange($("#modal-edit-daybook").find('select.billOfExchange'))
        if (selectedVal.length > 0) {
            $('#purchasedSupplier').prop('disabled', false);
            if (selectedVal == 4) {
                $('#lcRemit').css('display', '')
                $(container).find('select.currency').val(1).trigger('change').addClass('select2-select readonly');
                $(container).find('input.remitter,input.amount,input.lcNo,input.staff,input.customer').prop('readonly', true);
            } else {
                $('#lcRemit').css('display', 'none')
                $(container).find('select.billOfExchange').val('').trigger('change');
                $(container).find('select.currency').val('').trigger('change').removeClass('select2-select readonly');
                $(container).find('input.remitter,input.amount,input.lcNo,input.staff,input.customer').prop('readonly', false);
            }
        }
    });
    $("#modal-edit-daybook").on('change', 'select.billOfExchange', function() {
        let data = $(this).select2('data')[0];
        let container = $("#modal-edit-daybook");
        if (!isEmpty(data)) {
            data = data.data;
            container.find('input.remitter').val(data.customerName);
            container.find('input.lcNo').val(data.lcNo);
            container.find('input.customer').val(data.customerName);
            container.find('input.staff').val(data.salesPerson);
            setAutonumericValue(container.find('input.amount'), data.amount);
        } else {
            container.find('input.remitter').val('');
            container.find('input.lcNo').val('');
            container.find('input.customer').val('');
            container.find('input.staff').val('');
            setAutonumericValue(container.find('input.amount'), 0);
        }
    })
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
                })
            }
        });
    }

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
        var object;
        var data = {};
        autoNumericSetRawValue($("#modal-edit-daybook").find('input[name="amount"],input[name="bankCharges"]'))
        object = getFormData($('#modal-edit-daybook').find('.data'));
        data = object;

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
