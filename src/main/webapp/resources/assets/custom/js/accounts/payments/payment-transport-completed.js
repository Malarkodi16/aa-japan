var transportersJson;
var transport_completed;
var tranport_complete_date_filter = $('#transport-invoice-create #table-filter-date');
var tranport_complete_transporter_filter = $('#transport-invoice-create #table-filter-transporter');
$(function() {
    $.getJSON(myContextPath + "/data/accname.json", function(data) {
        //         var coacategorytypeJson = data;
        $('#add-new-category select#accountType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.subAccount + '(' + item.account + ')' + '(' + item.code + ')',
                };
            })
        }).val('').trigger("change");
    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#table-filter-transporter, #transporter').select2({
            allowClear: true,
            placeholder: 'Select Transporter',
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            filterTransporterName = $(this).find('option:selected').val();
            transport_completed.draw();
        })

    });

    $('#stock').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })

    //Date Range
    var start_date;
    var end_date;
    $('#table-filter-date').daterangepicker({
        autoUpdateInput: false,
    }).on("apply.daterangepicker", function(e, picker) {
        start_date = picker.startDate;
        end_date = picker.endDate;
        picker.element.val(start_date.format('DD-MM-YYYY') + ' - ' + end_date.format('DD-MM-YYYY'));
        start_date = start_date._d.getTime();
        end_date = end_date._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transport_completed.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        start_date = '';
        end_date = '';
        transport_completed.draw();
        $('#table-filter-date').val('');
        $(this).remove();

    });

    // Purchase Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-purchase-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date-purchase'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transport_completed.draw();
    });
    $('#purchase-date-form-group').on('click', '.clear-date-purchase', function() {
        purchased_min = '';
        purchased_max = '';
        transport_completed.draw();
        $('#table-filter-purchase-date').val('');
        $(this).remove();

    })

    // Arrival Date range picker
    var arrival_min;
    var arrival_max;
    $('#table-filter-arrival-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        arrival_min = picker.startDate;
        arrival_max = picker.endDate;
        picker.element.val(arrival_min.format('DD-MM-YYYY') + ' - ' + arrival_max.format('DD-MM-YYYY'));
        arrival_min = arrival_min._d.getTime();
        arrival_max = arrival_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date-arrival'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        transport_completed.draw();
    });
    $('#arrival-date-form-group').on('click', '.clear-date-arrival', function() {
        arrival_min = '';
        arrival_max = '';
        transport_completed.draw();
        $('#table-filter-arrival-date').val('');
        $(this).remove();

    })
    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $('#modal-create-invoice div.table-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    var tableEle = $('#table-transport-completed');
    transport_completed = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/transport-completed-datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            "searchable": false,
            //             "width":'10px',
            "className": "select-checkbox",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "type": "date",
            "data": "createdDate"
        }, {
            targets: 2,
            "className": "details-control",
            "type": "date",
            "data": "purchaseDate"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "maker",
        }, {
            targets: 5,
            "className": "details-control",
            "data": "model",
        }, {
            targets: 6,
            "className": "details-control",
            "data": "transporterName",
        }, {
            targets: 7,
            "className": "details-control",
            "data": "pickupLocation",
            "render": function(data, type, row) {
                return data.toUpperCase() != 'OTHERS' ? row.pickupLocationName : row.pickupLocationCustom;
            }
        }, {
            targets: 8,
            "className": "details-control",
            "data": "dropLocation",
            "render": function(data, type, row) {
                return data.toUpperCase() != 'OTHERS' ? row.dropLocationName : row.dropLocationCustom;
            }
        }, {
            targets: 9,
            "className": "details-control",
            "type": "date",
            "data": "arrivalDate",
        }, {
            targets: 10,
            "className": "details-control",
            "data": "amount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>'
            }
        }, {
            targets: 11,
            'visible': false,
            "data": "transporterId"

        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });

    var filterTransporterName = $('#table-filter-transporter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Transport filter
        if (typeof filterTransporterName != 'undefined' && filterTransporterName.length != '') {
            if (aData[11].length == 0 || aData[11] != filterTransporterName) {
                return false;
            }
        }

        if (typeof start_date != 'undefined' && start_date.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
            }
            if (start_date && !isNaN(start_date)) {
                if (aData._date < start_date) {
                    return false;
                }
            }
            if (end_date && !isNaN(end_date)) {
                if (aData._date > end_date) {
                    return false;
                }
            }
            return true;
        }

        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[2].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[2], 'DD-MM-YYYY')._d.getTime();
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
            return true;
        }

        if (typeof arrival_min != 'undefined' && arrival_min.length != '') {
            if (aData[9].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[9], 'DD-MM-YYYY')._d.getTime();
            }
            if (arrival_min && !isNaN(arrival_min)) {
                if (aData._date < arrival_min) {
                    return false;
                }
            }
            if (arrival_max && !isNaN(arrival_max)) {
                if (aData._date > arrival_max) {
                    return false;
                }
            }
            return true;
        }
        return true;

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;$('#transport-invoice-create #table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        transport_completed.search(query, true, false).draw();
    });
    $('#transport-invoice-create #table-filter-length').change(function() {
        transport_completed.page.len($(this).val()).draw();
    });
    transport_completed.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            transport_completed.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            transport_completed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            transport_completed.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            transport_completed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (transport_completed.rows({
            selected: true,
            page: 'current'
        }).count() !== transport_completed.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (transport_completed.rows({
            selected: true,
            page: 'current'
        }).count() !== transport_completed.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    })

    let modal_add_element = $('#modal-add-payment')
    modal_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        $.getJSON(myContextPath + "/data/transportPaymentCategory.json", function(data) {
            $('#modal-add-payment #category').select2({
                allowClear: true,
                width: "250px",
                data: $.map(data, function(item) {
                    return {
                        id: item.category + ' - ' + ifNotValid(item.coaCode, ""),
                        text: item.category + ' - ' + ifNotValid(item.coaCode, ""),
                        data: item
                    };
                })
            }).val('').trigger("change");
        })
        modal_add_element.find('input[name=taxAmount]').val(0);
        modal_add_element.find('input[name=totalAmount]').val(0);
        $(this).find('input.autonumber').autoNumeric('init');
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
    }).on('change', 'input[name="amount"]', function() {
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));

        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = amount * taxPer / 100;
        let isTax = modal_add_element.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {}
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on('keyup', '.taxPercent', function(e) {
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt;
        let isTax = modal_add_element.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (totalAmt / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            taxAmt = amount * taxPer / 100;
        }
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on('click', '.taxInclusive', function(e) {
        var taxAmt = Number(ifNotValid(modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        if ($(this).is(':checked')) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;
        }
        totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        modal_add_element.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
    }).on("keyup", "input.autonumber", function(setting) {
        //update subtotal
        var amount = Number(ifNotValid(modal_add_element.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = Number(ifNotValid(modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = amount + taxAmt;
        modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt);
    }).on('change', 'select[name="category"]', function() {
        var data = $(this).find(':selected').data('data');

        if (!isEmpty(data.data)) {
            if (data.data.stockView == 1) {
                $('div#stockView').removeClass('hidden');

            } else {
                $('div#stockView').addClass('hidden')
                $('select[name="stock"]').val('').trigger('change');
            }
        } else {
            $('div#stockView').addClass('hidden')
            $('select[name="stock"]').val('').trigger('change');
        }
    }).on('click', 'button#add-payment', function() {
        var amtFlag = false;
        if (!$('#modal-add-payment form#formAddPayment').valid()) {
            return;
        }
        let amountValue = Number(getAutonumericValue(modal_add_element.find('input[name="amount"]')));
        let totalAmountValue = Number(getAutonumericValue(modal_add_element.find('input[name="totalAmount"]')));
        let hiddenTax = Number(getAutonumericValue(modal_add_element.find('input[name="hiddenTaxAmount"]')));
        let orgTaxAmount = Number(getAutonumericValue(modal_add_element.find('input[name="taxAmount"]')));
        var taxDiff = hiddenTax - orgTaxAmount;
        var totalValue = 0;
        if (taxDiff == 1 || taxDiff == -1 || taxDiff == 0) {
            totalValue = amountValue + orgTaxAmount;
            modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', orgTaxAmount);
            modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
        } else {
            totalValue = amountValue + hiddenTax;
            modal_add_element.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', hiddenTax);
            modal_add_element.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
            amtFlag = true;
            modal_add_element.find('input[name="taxAmount"]').css("border", "1px solid #FA0F1B");
        }
        if (amtFlag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }

        var object = getFormData(modal_add_element.find('input,select,textarea'));
        let amount = getAutonumericValue(modal_add_element.find('input[name="amount"]'));
        let othersTaxValue = getAutonumericValue(modal_add_element.find('input[name="taxPercent"]'));
        let othersCostTaxAmount = getAutonumericValue(modal_add_element.find('input[name="taxAmount"]'));
        let othersTotal = getAutonumericValue(modal_add_element.find('input[name="totalAmount"]'));

        paymentData = {};
        paymentData.stockNo = object.stock;
        paymentData.transporter = object.transporter;
        paymentData.invoiceDate = object.invoiceDate
        paymentData.category = object.category
        paymentData.amount = amount
        paymentData.tax = othersTaxValue
        paymentData.taxAmount = othersCostTaxAmount
        paymentData.totalTaxIncluded = othersTotal

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(paymentData),
            url: myContextPath + "/transport/addpayment",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    transport_completed.ajax.reload();
                    $(modal_add_element).modal('toggle');
                }

            }
        });
    })

    // Add new category
    var addPaymentEle = $('#add-new-category')
    $(addPaymentEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

    }).on('hide.bs.modal', function(e) {

        $(this).find('input,textarea').val('');
        $(this).find('select').val('').trigger('change');
        //         account();
    }).on('click', '#btn-create-category', function() {
        if (!$('#add-new-category-form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        let isChecked = addPaymentEle.find('input#stockView').is(":checked");
        var data = {};
        data['category'] = $('#categoryDesc').val();
        data['coaCode'] = $('#accountType').val();
        if (isChecked) {
            data['stockView'] = 1;
        } else {
            data['stockView'] = 0;
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/create/transport/payment/category",
            contentType: "application/json",
            async: true,
            success: function(data) {
                $('#add-new-category').modal('toggle');
            }
        });
    });

    var modal_create_transport_invoice = $('#modal-create-invoice');
    var modalAcceptTriggerBtnEle
    $(modal_create_transport_invoice).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var commonTax = $.i18n.prop('common.tax');
        $('#form-transport-invoice-create').find('input[name="taxPercent"]').val(Number(commonTax));
        var data = transport_completed.rows({
            selected: true,
            page: 'all'
        }).data().toArray();
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }
        let tmp_transporter = ''
          , tmp_transporter_name = '';
        for (var i = 0; i < data.length; i++) {
            if (i == 0) {
                tmp_transporter = data[i].transporterId;
                tmp_transporter_name = data[i].transporterName;
                continue;
            }
            if (tmp_transporter != data[i].transporterId) {
                alert($.i18n.prop('alert.select.single.transporter'));
                return e.preventDefault();
            }
        }
        $(this).find('input[name="transporter"]').val(tmp_transporter);
        $(this).find('div.transporter-name').html(tmp_transporter_name)
        var item_tr_ele = $(this).find('table>tbody>tr.clone.hide');
        var count = 0;
        var tax = $.i18n.prop('common.tax');
        for (var i = 0; i < data.length; i++) {
            var element = $(item_tr_ele).clone();
            var taxAmount = data[i].amount * ifNotValid(data[i].tax, tax) / 100;
            var totalAmount = data[i].amount + taxAmount;
            $(element).find('input[name="rowData"]').val(JSON.stringify(data[i]));
            $(element).find('input[name="amount"]').autoNumeric('init');
            $(element).find('td.sno>span.sno').html(count + 1);
            $(element).find('td.stockNo>span').html(data[i].stockNo);
            $(element).find('td.chassisNo').html(data[i].chassisNo);
            $(element).find('td.maker').html(data[i].maker);
            $(element).find('td.model').html(data[i].model);
            $(element).find('td.pickupLocation').html(data[i].pickupLocation.toUpperCase() != 'OTHERS' ? data[i].pickupLocationName : data[i].pickupLocationCustom);
            $(element).find('td.dropLocation').html(data[i].dropLocation.toUpperCase() != 'OTHERS' ? data[i].dropLocationName : data[i].dropLocationCustom);
            $(element).find('td.amount input[name="amount"]').autoNumeric('init').autoNumeric('set', data[i].amount);
            $(element).find('td input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].totalAmount, totalAmount));
            $(element).find('td input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].taxAmount, taxAmount));
            $(element).find('td input[name="hiddenTaxAmount"]').val(ifNotValid(data[i].taxAmount, taxAmount));
            $(element).find('td input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].tax, tax));
            $(element).find('td.actualAmt').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].amount, 0));
            count++;
            $(element).removeClass('clone hide').addClass('delete-on-close data')
            $(element).appendTo($(this).find('table>tbody'));

        }
        updateFooter();
        $(this).find('input[name="amount"]').trigger('keyup');
        // Date picker
        $(this).find('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        }).on('change', function() {
            $(this).valid();

        })
        $(this).find('input.autonumber').autoNumeric('init');
    }).on('hidden.bs.modal', function() {
        $(this).find('table>tbody>tr.delete-on-close').remove();
        $(this).find('input,select').val('').trigger('change');

    }).on('click', '.btn-remove-item', function() {
        if (modal_create_transport_invoice.find('tr.data').length > 1) {
            $(this).closest('tr').remove();
        }
        updateFooter();
    }).on('change', 'input[name="amount"]', function() {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));

        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = amount * taxPer / 100;
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {}
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on("keyup", "input.autonumber", function(setting) {
        var tr = $(this).closest('tr');
        var total = 0;
        var totalTaxExcludedAmount = 0;
        var totalTaxAmount = 0;
        var totalTaxIncludedAmount = 0;
        //update subtotal
        var amount = Number(ifNotValid(tr.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = Number(ifNotValid(tr.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = amount + taxAmt;
        tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt);
        //update table footer`
        updateFooter();
    }).on('keyup', '.taxPercent', function(e) {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt;
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = (totalAmt / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            taxAmt = amount * taxPer / 100;
        }
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on('click', '.taxInclusive', function(e) {
        let tr = $(this).closest('tr');
        var taxAmt = Number(ifNotValid(tr.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var amount = Number(ifNotValid(tr.find('input[name="amount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        if ($(this).is(':checked')) {
            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
        } else {
            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;
        }
        totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amount"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on('click', '#btn-save-invoice', function() {
        if (!$('#form-transport-invoice-create').find('input').valid()) {
            return;
        }
        let transporter = modal_create_transport_invoice.find('input[name="transporter"]').val();
        let refNo = modal_create_transport_invoice.find('input[name="refNo"]').val();
        let dueDate = modal_create_transport_invoice.find('input[name="dueDate"]').val();
        let invoiceDate = modal_create_transport_invoice.find('input[name="invoiceDate"]').val();
        var params = 'transporter=' + transporter + '&dueDate=' + dueDate + '&refNo=' + refNo + '&invoiceDate=' + invoiceDate
        var flag = false;
        let dataArray = [];
        $(modal_create_transport_invoice).find('table>tbody>tr.data').each(function() {
            var rowData = $(this).find('input[name="rowData"]').val();
            rowData = JSON.parse(rowData);
            let amountValue = Number(getAutonumericValue($(this).find('input[name="amount"]')));
            let totalAmountValue = Number(getAutonumericValue($(this).find('input[name="totalAmount"]')));
            let hiddenTax = Number(getAutonumericValue($(this).find('input[name="hiddenTaxAmount"]')));
            let orgTaxAmount = Number(getAutonumericValue($(this).find('input[name="taxAmount"]')));
            var taxDiff = hiddenTax - orgTaxAmount;
            var totalValue = 0;
            if (taxDiff == 1 || taxDiff == -1 || taxDiff == 0) {
                totalValue = amountValue + orgTaxAmount;
                $(this).find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', orgTaxAmount);
                $(this).find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
            } else {
                totalValue = amountValue + hiddenTax;
                $(this).find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', hiddenTax);
                $(this).find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
                flag = true;
                $(this).find('input[name="taxAmount"]').css("border", "1px solid #FA0F1B");
            }
            var data = {};
            data["invoiceId"] = rowData.invoiceId;
            data["amount"] = $(this).find('input[name="amount"]').autoNumeric('init').autoNumeric('get');
            data["tax"] = $(this).find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get');
            data["taxAmount"] = $(this).find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get');
            data["totalTaxIncluded"] = $(this).find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get');
            dataArray.push(data);
        })

        if (flag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            updateFooter();
            return false;
        }

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(dataArray),
            url: myContextPath + "/accounts/payment/transport/invoice/create?" + params,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    transport_completed.ajax.reload();
                    $(modal_create_transport_invoice).modal('toggle');

                }

            }
        });
    })
})

function updateFooter() {
    var totalTaxExcludedAmount = 0;
    var totalTaxAmount = 0;
    var totalTaxIncludedAmount = 0;
    $('#form-transport-invoice-create').find('input[name="amount"]').each(function() {
        totalTaxExcludedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total').autoNumeric('init').autoNumeric('set', totalTaxExcludedAmount);
    $('#form-transport-invoice-create').find('input[name="taxAmount"]').each(function() {
        totalTaxAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total-tax').autoNumeric('init').autoNumeric('set', totalTaxAmount);
    $('#form-transport-invoice-create').find('input[name="totalAmount"]').each(function() {
        totalTaxIncludedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total-tax-included').autoNumeric('init').autoNumeric('set', totalTaxIncludedAmount);

}

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}
