var tableInvoice;
var remitterJson;
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
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        //         var remit = {}
        //         remit.code = "others"
        //         remit.name = "Others"
        //         data.push(remit)
        $('select.supplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        remitterJson = data;
        $('#table-filter-remitter').select2({
            allowClear: true,
            placeholder: 'Select Remiter',
            width: '100%',
            data: $.map(remitterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            filterRemitterName = $(this).find('option:selected').val();
            console.log(isEmpty(filterRemitterName));
            tableInvoice.draw();
        })

    })
    $.getJSON(myContextPath + "/data/paymentCategory.json", function(data) {
        //         var cloneRowCategory = $('#payment-item-table').find('tr.clone-row').find('.category');
        //         var CategoryFirstRow = $('#payment-item-table').find('tr.first-row').find('.category');
        //         var StockFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
        $('#add-payment .category,#edit-payment .category ').select2({
            allowClear: true,
            width: "250px",
            data: $.map(data, function(item) {
                return {
                    id: item.categoryCode,
                    text: item.category + ' [' + ifNotValid(item.tkcCode, "") + '] - ' + ifNotValid(item.tkcDescription, ""),
                };
            })
        }).val('').trigger("change");
        $('#add-payment tr.clone-row.hide select.category').select2('destroy');
        $('#edit-payment tr.clone-row.hide select.category').select2('destroy');
    });
    $('#payment-item-table').on('change', '.category', function() {
        var categoryEle = $(this).closest('.payment-category-select').find('.category');
        var stockEle = $(this).closest('tr').find('.stockNo');
        var categorySelectVal = categoryEle.find('option:selected').text();
        if (categorySelectVal == "Repair") {
            stockEle.prop('disabled', false);
        } else {
            stockEle.prop('disabled', true);
        }
    })

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/invoiceTypes.json", function(data) {
        var invType = {};
        invType.typeId = "others";
        invType.type = "Others";
        data.push(invType);
        $('#invoiceType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.typeId,
                    text: item.type
                };
            })
        }).val('').trigger("change");
    });

    /* DatePicker */
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

    var due_date;
    $('#table-filter-due-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        due_date = $(this).val();
        $(this).closest('.input-group').find('.due-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon due-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        tableInvoice.draw();
    });
    $('.input-group').on('click', '.due-clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        tableInvoice.draw();

    })

    var invoice_date;
    $('#table-filter-invoice-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        invoice_date = $(this).val();
        $(this).closest('.input-group').find('.invoice-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon invoice-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        tableInvoice.draw();
    });
    $('.input-group').on('click', '.invoice-clear-date', function() {
        invoice_date = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();
        tableInvoice.draw();

    })

    $('#remit-select').on('change', '.select2.with-remit', function() {
        var closest_container = $(this).closest('.form-group');
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.remit-input-container').removeClass('hidden');
            $(closest_container).find('.select2').addClass('hidden');
        }
    }).on('click', 'a.show-dropdown', function() {
        var closest_container = $(this).closest('.form-group');
        $(closest_container).find('select.select2').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('.select2').removeClass('hidden');
        $(closest_container).find('div.remit-input-container').addClass('hidden');
    });

    //AutoNumeric

    $('#add-payment').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        inspectionModalEle = $(event.relatedTarget);
        var currentDate = new Date();
        var paymentDate = moment(currentDate, "DD-MM-YYYY").format('DD-MM-YYYY');
        $('input[name="date"]').datepicker('setDate', paymentDate);
        var commonTax = $.i18n.prop('common.tax');
        $('#payment-item-table').find('input[name="taxPercent"]').val(Number(commonTax));
        var subtotal = 0;
        setAutonumericValue($('#payment-item-table tfoot').find('span.autonumber'), subtotal);
        $('#add-payment .autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        $(this).find('table>tbody>tr.invoice-item').not(':first').remove();
        $(this).find('input[name="taxAmount"]').css('border', '');
        $(this).find('input,select').val('').trigger('change');
        $(this).find('form').trigger('reset');

    }).on('click', '.add-payment-row', function() {

        var addPayment = $('#payment-item-table').find('tr.clone-row').clone();
        addPayment.find('.autonumber');
        addPayment.removeClass('hide clone-row')
        //initStockSearchSelect2($(addPayment).find('.stockNo'));
        addPayment.find('.category').select2({
            allowClear: true,
            width: "250px"
        })
        addPayment.appendTo('.payment-row-clone-container');
        addPayment.addClass('invoice-item');
    }).on('click', '.delete-payment-row', function(e) {

        var deletePayment = $('#payment-item-table').find('tbody>tr');
        if (deletePayment.length > 2) {
            $(this).closest('tr').remove();
        } else {
            deletePayment.find('input').val('');
            deletePayment.find('select').val('').trigger('change');
        }
        updateFooter();
    }).on('keyup', '.amount', function(e) {
        var excRateEle = $(this).closest('tr').find('.exchangeRate');
        var srcAmt = getAutonumericValue($(this));
        var totAmt = getAutonumericValue($(this).closest('tr').find('.totalAmount'));
        var exc = totAmt / srcAmt;
        if (exc > 0 && exc != 'Infinity') {
            excRateEle.val(exc);
        }
    }).on("keyup", "input.autonumber", function(setting) {
        var tr = $(this).closest('tr');
        //update subtotal
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = Number(ifNotValid(tr.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = amount + taxAmt;
        tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt);
        //update table footer`
        updateFooter();
    }).on('change', '.taxPercent', function(e) {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt;
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {

            amount = totalAmt / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {
            taxAmt = amount * taxPer / 100;
        }
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on('change', '.amountInYen', function(e) {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = amount * taxPer / 100
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = amount / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {}
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on('click', '.taxInclusive', function(e) {
        let tr = $(this).closest('tr');
        var taxAmt;
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        if ($(this).is(':checked')) {
            amount = amount / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {
            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;
        }
        totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        updateFooter();
    }).on('click', '#save-other-payments', function() {
        let validationFields = $('#other-form').find('select[name="remitter"],input[name="refNo"],input[name="date"],input[name="dueDate"]' + ',input[name="dueDate"],select[name="category"],input[name="amountInYen"]')
        if (!validationFields.valid()) {
            return false;
        }

        var flag = false;
        var data = {};

        var tableArray = [];
        data = getFormData($('#add-payment').find('.other-data'));
        //         var otherPaymentData = $('.payment-row-clone-container');
        var table = $('#payment-item-table');
        table.find('table>tbody>tr.invoice-item').each(function() {
            let amountValue = Number(getAutonumericValue($(this).find('input[name="amountInYen"]')));
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

            var tableData = {};

            tableData.refNo = data.refNo;
            tableData.invoiceDate = data.date;
            tableData.dueDate = data.dueDate;
            tableData.remitter = data.remitter;
            tableData.remitterOthers = data.remitterOthers;
            tableData.category = $(this).find('select[name="category"]').val();
            tableData.categoryOthers = $(this).find('input[name="categoryOthers"]').val();
            tableData.description = $(this).find('input[name="description"]').val();
            tableData.amountInYen = getAutonumericValue($(this).find('input[name="amountInYen"]'));
            tableData.taxPercentage = getAutonumericValue($(this).find('input[name="taxPercent"]'));
            tableData.taxAmount = getAutonumericValue($(this).find('input[name="taxAmount"]'));
            tableData.taxIncludedAmount = getAutonumericValue($(this).find('input[name="totalAmount"]'));
            let isTax = $(this).find('input[name="taxInclusive"]').is(':checked');
            if (isTax) {
                tableData.taxInclusive = true;
            } else {
                tableData.taxInclusive = false;
            }
            tableData.sourceCurrency = $(this).find('input[name="sourceCurrency"]').val();
            tableData.amount = getAutonumericValue($(this).find('input[name="amount"]'));
            tableData.exchangeRate = getAutonumericValue($(this).find('input[name="exchangeRate"]'));
            tableArray.push(tableData);
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
            type: "post",
            data: JSON.stringify(tableArray),
            contentType: "application/json",
            url: myContextPath + "/accounts/payment/save-other-payments",
            success: function(status) {
                $('#add-payment').modal('toggle')
                //                 $('#add-payment').find('#stockNo').val('').trigger('change');
                //                 $('#add-payment').find('#category').val('').trigger('change');
                tableInvoice.ajax.reload();
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        })

    })

    function updateFooter() {
        var total = 0;
        var totalTax = 0;
        var totalTaxIncluded = 0;
        $('#payment-item-table').find('input[name="amountInYen"]').each(function() {
            let valueTotal = $(this).val();
            !isEmpty(valueTotal) ? total += Number($(this).autoNumeric('init').autoNumeric('get')) : total += 0.0;
        })
        setAutonumericValue($('#payment-item-table tfoot>tr>td.amountInYenTotal').find('span.autonumber.amountInYenTotal').autoNumeric('init'), total);
        $('#payment-item-table').find('input[name="taxAmount"]').each(function() {
            let valueTotalTax = $(this).val();
            !isEmpty(valueTotalTax) ? totalTax += Number($(this).autoNumeric('init').autoNumeric('get')) : totalTax += 0.0;
        })
        setAutonumericValue($('#payment-item-table tfoot>tr>td.taxTotal').find('span.autonumber.taxTotal').autoNumeric('init'), totalTax);
        $('#payment-item-table').find('input[name="totalAmount"]').each(function() {
            let valueTotalTaxInc = $(this).val();
            !isEmpty(valueTotalTaxInc) ? totalTaxIncluded += Number($(this).autoNumeric('init').autoNumeric('get')) : totalTaxIncluded += 0.0;

        })
        setAutonumericValue($('#payment-item-table tfoot>tr>td.footerTotal').find('span.autonumber.footerTotal').autoNumeric('init'), totalTaxIncluded);
    }

    //DataTable Payment
    var tableEle = $('#table-payment-others-invoice');
    tableInvoice = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/payment-others-datasource",
        "autoWidth": true,
        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    //return '<input class="selectBox selRow" name="selRow" id="selRow_' + data + '" type="checkbox" data-stockNo="' + row.stockNo + '" value="' + data + '">';
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "createdDate"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"
        }, //          {
        //             targets: 3,
        //             "className": "details-control",
        //             "data": "refNo",

        //         },
        {
            targets: 3,
            "className": "details-control",
            "data": "invoiceType",
            "visible": false,
        }, {
            targets: 4,
            "className": "details-control",
            "data": "remitter",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (data.toLowerCase() == 'others') {
                    return row.remitterOthers
                } else {
                    return row.remitterName;
                }
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "dueDate"
        }, {
            targets: 6,
            "className": "details-control dt-right",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "data": "invoiceUpload",
            "className": "details-control",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "INVOICE_NOT_UPLOADED"
                    className = "default"
                } else if (data == 1) {
                    status = "INVOICE_UPLOADED"
                    className = "success"
                }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 8,
            "width": "120px",
            "data": "invoiceNo",
            "render": function(data, type, row) {
                var html = '';
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#edit-payment" value="edit-general-expense"><i class="fa fa-fw fa-edit"></i></a>'

                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>';
                //                 html += '<button type="button" id="view" class="btn btn-default ml-5 btn-xs" data-target="#modal-invoice-preview" title="Upload" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-eye"></i></button>'
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                }
                html += '<a class="ml-5 btn btn-default btn-xs" name="delete_invoice"><i class="fa fa-fw fa-close"></i></a>';

                return html
            }
        }, {
            targets: 9,
            'visible': false,
            "data": "remitter"

        }],
        "drawCallback": function(settings, json) {
            $(tableEle).find('span.autonumber').autoNumeric('init')
        },

    });
    $('#payment-item-table').slimScroll({
        start: 'bottom',
        height: '',
        railVisible: true,
    });

    var filterRemitterName = $('#table-filter-remitter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Remiter filter
        if (typeof filterRemitterName != 'undefined' && filterRemitterName.length != '') {
            if (aData[9].length == 0 || aData[9] != filterRemitterName) {
                return false;
            }
        }
        //Due Date Filter
        if (typeof due_date != 'undefined' && due_date.length != '') {
            if (aData[5].length == 0 || aData[5] != due_date) {
                return false;
            }
        }

        //Invoice Date Filter
        if (typeof invoice_date != 'undefined' && invoice_date.length != '') {
            if (aData[1].length == 0 || aData[1] != invoice_date) {
                return false;
            }
        }
        return true;
    });

    tableInvoice.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = tableInvoice.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.id);
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })

    tableInvoice.on("click", 'a[name="delete_invoice"]', function() {
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        let tr = $(this).closest('tr')
        let row = tableInvoice.row(tr)
        let data = row.data();
        response = deleteGeneralExpenseInvoice(data.invoiceNo);
        if (response.status == "success") {
            tableInvoice.ajax.reload()
        }
    })
    //end of DataTable
    tableEle.on("click", 'td>input[type="checkbox"]', function() {
        $(tableEle).find("input[type=checkbox]:checked").not(this).prop("checked", false)
    })
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        tableInvoice.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        tableInvoice.page.len($(this).val()).draw();
    });

    tableInvoice.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = tableInvoice.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            tableInvoice.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = tableInvoice.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = othersFormat(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    //approve Payment addition start//

    tableInvoice.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableInvoice.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableInvoice.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableInvoice.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableInvoice.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (tableInvoice.rows({
            selected: true,
            page: 'current'
        }).count() !== tableInvoice.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (tableInvoice.rows({
            selected: true,
            page: 'current'
        }).count() !== tableInvoice.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    })
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    //approve payment
    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    // Approve Payment
    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');
    //validation
    payment_details_form.validate({

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
            'dueDate': 'required',

        }
    });
    $('#modal-approve-payment').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (tableInvoice.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = tableInvoice.rows({
            selected: true,
            page: 'current'
        }).data();
        var element;
        var i = 0;
        tableInvoice.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableInvoice.row(this).data();
            if (data.invoiceUpload == 0) {
                alert($.i18n.prop('alert.invoice.not.uploaded'))
                return e.preventDefault();
            } else if (data.attachementViewed == 0) {
                alert($.i18n.prop('alert.invoice.not.viewed'))
                return e.preventDefault();
            } else {
                var stockNo = ifNotValid(data.stockNo, '');
                var chassisNo = ifNotValid(data.chassisNo, '');
                var dueDate = ifNotValid(data.dueDate, '');
                $('#modal-approve-payment').find('input[name="dueDate"]').datepicker("setDate", dueDate);
                i++;
            }
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input,textarea').val('').trigger('change');
    }).on('click', '#approve', function() {
        if (!payment_details_form.valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let params = tableInvoice.rows({
            selected: true,
            page: 'current'
        }).data().toArray().map(row=>row.id);
        let data = {}

        data['dueDate'] = $('#modal-approve-payment').find('input[name="dueDate"]').val();
        let response = approvePayment(data, $.param({
            "id": params
        }));
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-approve-payment').modal('toggle');

            tableInvoice.ajax.reload();
            setPaymentBookingDashboardStatus(data);
        }

    })
    var approvePayment = function(data, params) {
        let result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/approve/payment/others?" + params,
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    let modal_image_preview = $("#modal-invoice-preview");
    modal_image_preview.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = tableInvoice.row($(targetElement).closest('tr')).data();
        let filename = data.invoiceAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            updateAttachmentViewedStatus(data.id);
            $('embed.image_preview').attr('src', myContextPath + '/get/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
            tableInvoice.ajax.reload();
        } else {
            $('embed.image_preview').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }
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
        var data = {};
        data['category'] = $('#categoryDesc').val();
        data['coaCode'] = $('#accountType').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/create/category",
            contentType: "application/json",
            async: true,
            success: function(data) {

                $('#add-new-category').modal('toggle');

                $.getJSON(myContextPath + "/data/paymentCategory.json", function(data) {
                    //                     var cloneRowCategory = $('#payment-item-table').find('tr.clone-row').find('.category');
                    //                     var CategoryFirstRow = $('#payment-item-table').find('tr.first-row').find('.category');
                    //                     var StockFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
                    $('#add-payment .category').select2({
                        allowClear: true,
                        width: "150px",
                        data: $.map(data, function(item) {
                            return {
                                id: item.categoryCode,
                                text: item.category
                            };
                        })
                    }).val('').trigger("change");

                });
            }
        });
    });

    var addSupplierEle = $('#add-gen-supplier')
    $(addSupplierEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

    }).on('hide.bs.modal', function(e) {
        $(this).find('input').val('');
        $(this).find('span').html('')
    }).on('click', '#btn-create-general-supplier', function() {
        let isValid = $('#add-new-general-supplier').find('input[name="suppliername"]').valid()
        if (!isValid) {
            return false;
        }
        var data = {};
        data['name'] = $('#suppliername').val();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/master/generalSupplier/create",
            contentType: "application/json",
            success: function(data) {
                if (data.status == "success") {
                    $(addSupplierEle).modal('toggle');

                    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
                        //                             var remit = {}
                        //                             remit.code = "others"
                        //                             remit.name = "Others"
                        //                             data.push(remit)
                        $('select.supplier').select2({
                            allowClear: true,
                            width: '100%',
                            data: $.map(data, function(item) {
                                return {
                                    id: item.code,
                                    text: item.name
                                };
                            })
                        }).val('').trigger("change");
                    })
                }
            }
        });

    })

});

// invoice upload
$(function() {
    // upload invoice image
    let invoice_upload_form = $('#modal-invoice-upload form#form-file-upload');
    //validation
    invoice_upload_form.validate({
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
            "invoiceFile": 'required'
        }
    });

    //file uplaod
    $('#modal-invoice-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = tableInvoice.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="invoiceId"]').val(data.id);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#upload', function() {
        if (!invoice_upload_form.valid()) {
            return false;
        }
        if (!isValidFileSelected($('#modal-invoice-upload input[name="invoiceFile"]'))) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var data = new FormData();
        var invoiceFileEle = $('#modal-invoice-upload input[name="invoiceFile"]');
        if (invoiceFileEle.prop('files').length > 0) {
            let file = invoiceFileEle.prop('files')[0];
            data.append("invoiceFile", file);
        } else {
            return false;
        }
        let id = $('#modal-invoice-upload input[name="invoiceId"]').val();
        let response = uploadFile(data, id)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-invoice-upload').modal('toggle');
            tableInvoice.ajax.reload();

        }
    })
    var uploadFile = function(data, id) {
        let result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            processData: false,
            data: data,
            url: myContextPath + "/accounts/approve/payment/other/invoice/upload?id=" + id,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})
function initStockSearchSelect2(element) {
    element.select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "200px",
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
}
//Others Format
function othersFormat(rowData) {
    var element = $('#others-clone-container>#others-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    let taxTotal = 0;
    let totalTaxIncluded = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.category').html((ifNotValid(rowData.approvePaymentItems[i].invoiceType, '').toLowerCase() != 'others' ? ifNotValid(rowData.approvePaymentItems[i].category + ' [' + ifNotValid(rowData.approvePaymentItems[i].tkcCode, "") + '] - ' + ifNotValid(rowData.approvePaymentItems[i].tkcDescription, ""), '') : ifNotValid(rowData.approvePaymentItems[i].categoryOthers, '')));
        $(row).find('td.description').html(ifNotValid(rowData.approvePaymentItems[i].description, ''));
        $(row).find('td.amountInYen span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amountInYen, ''));
        $(row).find('td.taxAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.hiddenTaxAmount input.hiddenTaxAmount').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.totalAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, ''));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.currency').html(ifNotValid(rowData.approvePaymentItems[i].sourceCurrency, ''));
        $(row).find('td.exchangeRate span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].exchangeRate, ''));
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].amountInYen, 0));
        taxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].taxAmount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, 0));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.taxTotal>span').html(taxTotal);
    $(element).find('table>tfoot td.taxIncluded>span').html(totalTaxIncluded);
    return element;

}

function updateAttachmentViewedStatus(invoiceNo) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/update/attachmentView/others?id=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}

function deleteGeneralExpenseInvoice(invoiceNo) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/delete/general-expense/booking?invoiceNo=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
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

// function account() {

// }
function updateAttachmentViewedStatus(invoiceNo) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/update/attachmentView/others?id=" + invoiceNo,
        contentType: false,
        async: true,
        success: function(data) {
            console.log("viewed")
        }
    });
}
