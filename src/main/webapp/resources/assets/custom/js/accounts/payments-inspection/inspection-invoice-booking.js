$(function() {

    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        let inspectionCompanyJson = data;
        $('#table-filter-inspection').select2({
            allowClear: true,
            width: '100%',
            data: $.map(inspectionCompanyJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        })

    });
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        inspection_booking_list.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        inspection_booking_list.page.len($(this).val()).draw();
    });

    var tableEle = $('#table-inspection-booking');
    inspection_booking_list = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/inspection-booking-datasource",
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
            "className": "select-checkbox",
            "width": "10px",
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
            "data": "inspectionSentDate"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "purchaseDate"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "destinationCountry",
            "render": function(data, type, row) {
                return data + '/' + row.destinationPort
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "inspectionCompany",
        }, {
            targets: 6,
            "className": "details-control",
            "data": "inspectionDate",
        }, {
            targets: 7,
            "data": "doumentSentStatus",
            "className": "details-control",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0 || isEmpty(data)) {
                    status = "Not Sent"
                    className = "default"
                } else if (data == 1) {
                    status = "Copy"
                    className = "primary"
                } else if (data == 2) {
                    status = "Original"
                    className = "success"

                }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 8,
            "className": "details-control",
            "data": "documentSentDate",
        }, {
            targets: 9,
            "width": "100px",
            "data": "status",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "INITIATED"
                    className = "default"
                } else if (data == 2) {
                    status = "FAILED"
                    className = "danger"
                } else if (data == 3) {
                    status = "COMPLETED"
                    className = "success"
                } else if (data == 5) {
                    status = "PASSED"
                    className = "Primary"

                }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 10,
            "className": "details-control",
            "data": "certificateNo"
        }, {
            targets: 11,
            "className": "details-control",
            "data": "dateOfIssue"
        }, {
            targets: 12,
            'visible': false,
            "data": "inspectionCompanyId"

        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')
        }

    });
    inspection_booking_list.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = inspection_booking_list.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.invoiceRefNo, data.refNo);
        //invoiceRefNo
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })

    inspection_booking_list.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            transport_completed.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            inspection_booking_list.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            inspection_booking_list.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            inspection_booking_list.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function(e) {
        if (inspection_booking_list.rows({
            selected: true,
            page: 'current'
        }).count() !== inspection_booking_list.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);
        }

    }).on("deselect", function() {
        if (inspection_booking_list.rows({
            selected: true,
            page: 'current'
        }).count() !== inspection_booking_list.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    })
    var filterInspectionCompany;
    $('#table-filter-inspection').on("change", function() {
        filterInspectionCompany = $(this).find('option:selected').val();
        inspection_booking_list.draw();
    })

    //Date Range
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
        inspection_booking_list.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        inspection_booking_list.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    });

    //Date Range
    var inspection_sent_min;
    var inspection_sent_max;
    $('#table-filter-inspection-sent-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        inspection_sent_min = picker.startDate;
        inspection_sent_max = picker.endDate;
        picker.element.val(inspection_sent_min.format('DD-MM-YYYY') + ' - ' + inspection_sent_max.format('DD-MM-YYYY'));
        inspection_sent_min = inspection_sent_min._d.getTime();
        inspection_sent_max = inspection_sent_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        inspection_booking_list.draw();
    });
    $('#inspection-sent-date-form-group').on('click', '.clear-date', function() {
        inspection_sent_min = '';
        inspection_sent_max = '';
        inspection_booking_list.draw();
        $('#table-filter-inspection-sent-date').val('');
        $(this).remove();

    });

    //Date Range
    var inspection_min;
    var inspection_max;
    $('#table-filter-inspection-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        inspection_min = picker.startDate;
        inspection_max = picker.endDate;
        picker.element.val(inspection_min.format('DD-MM-YYYY') + ' - ' + inspection_max.format('DD-MM-YYYY'));
        inspection_min = inspection_min._d.getTime();
        inspection_max = inspection_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        inspection_booking_list.draw();
    });
    $('#inspection-date-form-group').on('click', '.clear-date', function() {
        inspection_min = '';
        inspection_max = '';
        inspection_booking_list.draw();
        $('#table-filter-inspection-date').val('');
        $(this).remove();

    });

    //Date Range
    var issue_min;
    var issue_max;
    $('#table-filter-issue-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        issue_min = picker.startDate;
        issue_max = picker.endDate;
        picker.element.val(issue_min.format('DD-MM-YYYY') + ' - ' + issue_max.format('DD-MM-YYYY'));
        issue_min = issue_min._d.getTime();
        issue_max = issue_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        inspection_booking_list.draw();
    });
    $('#issue-date-form-group').on('click', '.clear-date', function() {
        issue_min = '';
        issue_max = '';
        inspection_booking_list.draw();
        $('#table-filter-issue-date').val('');
        $(this).remove();

    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Transport filter
        if (typeof filterInspectionCompany != 'undefined' && filterInspectionCompany.length != '') {
            if (aData[12].length == 0 || aData[12] != filterInspectionCompany) {
                return false;
            }
        }
        //date filter inspection sent date
        if (typeof inspection_sent_min != 'undefined' && inspection_sent_min.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
            }
            if (inspection_sent_min && !isNaN(inspection_sent_min)) {
                if (aData._date < inspection_sent_min) {
                    return false;
                }
            }
            if (inspection_sent_max && !isNaN(inspection_sent_max)) {
                if (aData._date > inspection_sent_max) {
                    return false;
                }
            }

        }
        //date filter purchase date
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

        }

        //date filter inspection date
        if (typeof inspection_min != 'undefined' && inspection_min.length != '') {
            if (aData[6].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[6], 'DD-MM-YYYY')._d.getTime();
            }
            if (inspection_min && !isNaN(inspection_min)) {
                if (aData._date < inspection_min) {
                    return false;
                }
            }
            if (inspection_max && !isNaN(inspection_max)) {
                if (aData._date > inspection_max) {
                    return false;
                }
            }

        }

        //date filter issue date
        if (typeof issue_min != 'undefined' && issue_min.length != '') {
            if (aData[11].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[11], 'DD-MM-YYYY')._d.getTime();
            }
            if (issue_min && !isNaN(issue_min)) {
                if (aData._date < issue_min) {
                    return false;
                }
            }
            if (issue_max && !isNaN(issue_max)) {
                if (aData._date > issue_max) {
                    return false;
                }
            }

        }
        return true;
    })

    $('#modal-create-invoice div.table-container').slimScroll({
        start: 'bottom',
        height: ''
    });

    var modal_create_inspection_invoice = $('#modal-create-invoice');
    var modalAcceptTriggerBtnEle
    $(modal_create_inspection_invoice).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var commonTax = $.i18n.prop('common.tax');
        $('#form-inspection-invoice-create').find('input[name="taxPercent"]').val(Number(commonTax));
        var data = inspection_booking_list.rows({
            selected: true,
            page: 'all'
        }).data().toArray();
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }
        let tmp_id = ''
          , tmp_name = '';
        for (var i = 0; i < data.length; i++) {
            if (i == 0) {
                tmp_id = data[i].inspectionCompanyId;
                tmp_name = data[i].inspectionCompany;
                continue;
            }
            if (tmp_id != data[i].inspectionCompanyId) {
                alert($.i18n.prop('alert.select.single.inspection.company'));
                return event.preventDefault();
            }
        }
        $(this).find('input[name="inspectionCompanyId"]').val(tmp_id);
        $(this).find('input[name="inspectionCompany"]').val(tmp_name);
        $(this).find('div.inspectionCompany').html(tmp_name)
        var item_tr_ele = $(this).find('table>tbody>tr.clone.hide');
        var count = 0;
        var tax = $.i18n.prop('common.tax');
        for (var i = 0; i < data.length; i++) {
            var element = $(item_tr_ele).clone();
            var taxAmount = ifNotValid(data[i].amount, 0.0) * ifNotValid(data[i].tax, tax) / 100;
            var totalAmount = ifNotValid(data[i].amount, 0.0) + taxAmount;
            $(element).find('input[name="rowData"]').val(JSON.stringify(data[i]));
            $(element).find('input[name="amount"]').autoNumeric('init');
            $(element).find('td.sno>span.sno').html(count + 1);
            $(element).find('td.stockNo>span').html(data[i].stockNo);
            $(element).find('td.chassisNo').html(data[i].chassisNo);
            $(element).find('td.maker').html(data[i].maker);
            $(element).find('td.model').html(data[i].model);
            $(element).find('td.certificateNo').html(data[i].certificateNo);
            $(element).find('td.dateOfIssue').html(data[i].dateOfIssue);
            $(element).find('td.amount input[name="amount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].amount, 0.0));
            $(element).find('td input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].totalAmount, totalAmount));
            $(element).find('td input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].taxAmount, taxAmount));
            $(element).find('td input[name="hiddenTaxAmount"]').val(ifNotValid(data[i].taxAmount, taxAmount));
            $(element).find('td input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].tax, tax));
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

    }).on('click', '.btn-add-item', function() {

        var item_tr_ele = $('#modal-create-invoice').find('table>tbody>tr.clone-cancel-stock.hide');
        var element = $(item_tr_ele).clone()
        initStockSearchSelect2($(element).find('.stockNo'));
        $(element).find('td.amount input[name="amount"]').autoNumeric('init').autoNumeric('set', 0.0);
        $(element).find('td input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', 0.0);
        $(element).find('td input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', 0.0);
        $(element).find('td input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', 0.0);
        $(element).find('td input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', 0.0);
        $(element).find('input.autonumber').autoNumeric('init')
        element.removeClass('clone-cancel-stock hide').addClass('delete-on-close data');
        $(element).appendTo($('#modal-create-invoice').find('table>tbody'));
        updateFooter();
    }).on('click', '.btn-remove-item', function() {
        if (modal_create_inspection_invoice.find('tr.data').length > 1) {
            $(this).closest('tr').remove();
        }
        updateFooter();
    }).on('change', 'select[name="stockNo"]', function() {
        let tr = $(this).closest('tr');
        let stockData = $(this).select2('data');
        if (stockData.length > 0 && !isEmpty(stockData[0].data)) {
            $(tr).find('td.maker').html(stockData[0].data.maker);
            $(tr).find('td.model').html(stockData[0].data.model);
        }
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
        if (!$('#form-inspection-invoice-create').find('input').valid()) {
            return;
        }
        let inspectionCompanyId = modal_create_inspection_invoice.find('input[name="inspectionCompanyId"]').val();
        let inspectionCompany = modal_create_inspection_invoice.find('input[name="inspectionCompany"]').val();
        let refNo = modal_create_inspection_invoice.find('input[name="refNo"]').val();
        let dueDate = modal_create_inspection_invoice.find('input[name="dueDate"]').val();
        var params = 'inspectionCompanyId=' + inspectionCompanyId + '&dueDate=' + dueDate + '&refNo=' + refNo + '&inspectionCompany=' + inspectionCompany
        var flag = false;
        let dataArray = [];
        $(modal_create_inspection_invoice).find('table>tbody>tr.data').each(function() {
            let stockNo;
            if (!isEmpty($(this).find('input[name="rowData"]').val())) {
                var rowData = $(this).find('input[name="rowData"]').val();
                rowData = JSON.parse(rowData);
            } else {
                stockNo = $(this).find('select[name="stockNo"]').find('option:selected').val();
            }
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
            data["code"] = !isEmpty(rowData) ? rowData.code : "";
            data["stockNo"] = !isEmpty(rowData) ? rowData.stockNo : stockNo;
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
            url: myContextPath + "/accounts/payment/inspection/invoice/create?" + params,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    inspection_booking_list.ajax.reload();
                    $(modal_create_inspection_invoice).modal('toggle');

                }

            }
        });
    })
})

function updateFooter() {
    var totalTaxExcludedAmount = 0;
    var totalTaxAmount = 0;
    var totalTaxIncludedAmount = 0;
    $('#form-inspection-invoice-create').find('input[name="amount"]').each(function() {
        totalTaxExcludedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total').autoNumeric('init').autoNumeric('set', totalTaxExcludedAmount);
    $('#form-inspection-invoice-create').find('input[name="taxAmount"]').each(function() {
        totalTaxAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total-tax').autoNumeric('init').autoNumeric('set', totalTaxAmount);
    $('#form-inspection-invoice-create').find('input[name="totalAmount"]').each(function() {
        totalTaxIncludedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-create-invoice').find('div.summary-container span.total-tax-included').autoNumeric('init').autoNumeric('set', totalTaxIncludedAmount);

}

function initStockSearchSelect2(element) {
    element.select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "200px",
        ajax: {
            url: myContextPath + "/stock/cancelled/search/account-inspection",
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
                            text: item.stockNo + ' :: ' + item.chassisNo,
                            data: item
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });
}
function setPaymentBookingDashboardStatus(data) {
    $('#count-approval-auction').html(data.auctionApproval);
    $('#count-approval-transport').html(data.transportApproval);
    $('#count-approval-freight').html(data.freight);
    $('#count-approval-others').html(data.others);
    $('#count-approval-storage').html(data.storage);
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-storage').html(data.storage);
    $('#count-others').html(data.others);
    // $('#count-prepayment').html(data.paymentAdvance);

}
