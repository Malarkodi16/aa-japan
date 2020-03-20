$(function() {

    $('#table-filter-search').keyup(function() {
        var query = ($(this).val());
        table_cancelledInvoices_list.search(query).draw();
    });
    $('#table-filter-length').change(function() {
        table_cancelledInvoices_list.page.len($(this).val()).draw();
    });

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
        table_cancelledInvoices_list.draw();
    });
    $('.input-group').on('click', '.invoice-clear-date', function() {
        invoice_date = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();
        table_cancelledInvoices_list.draw();

    })

    var cancelled_date;
    $('#table-filter-cancelled-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        cancelled_date = $(this).val();
        $(this).closest('.input-group').find('.cancelled-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon cancelled-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_cancelledInvoices_list.draw();
    });
    $('.input-group').on('click', '.cancelled-clear-date', function() {
        cancelled_date = '';
        $('#table-filter-cancelled-date').val('');
        $(this).remove();
        table_cancelledInvoices_list.draw();

    })

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })

    var table_cancelledInvoices_list_Ele = $('#table-cancelledInvoice-list');
    var table_cancelledInvoices_list = table_cancelledInvoices_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/cancelledInvoices/list-data",
        "ordering": false,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "invoiceDate",
            "data": "invoiceDate",
        }, {
            targets: 1,
            "name": "cancelledDate",
            "data": "cancelledDate",

        }, {
            targets: 2,
            "name": "invoiceNo",
            "data": "invoiceNo",
        }, {
            targets: 3,
            "name": "company",
            "data": "company",
            "render": function(data, type, row) {
                data = data == null ? row.transporter : data + "/" + row.auctionHouseName;

                return data;
            }

        }, {
            targets: 4,
            "className": "dt-right",
            "type": "num-fmt",
            "name": "invoiceAmount",
            "data": "invoiceAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;

                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "name": "refNo",
            "data": "refNo",
        }, {
            targets: 6,
            "name": "remarks",
            "data": "remarks",

        }, {
            targets: 7,
            "name": "cancellationRemarks",
            "data": "cancellationRemarks",

        }],
        "drawCallback": function(settings, json) {
            table_cancelledInvoices_list_Ele.find('input.autonumber,span.autonumber').autoNumeric('init')

        },
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Invoice Date Filter
        if (typeof invoice_date != 'undefined' && invoice_date.length != '') {
            if (aData[0].length == 0 || aData[0] != invoice_date) {
                return false;
            }
        }

        //Cancelled Date Filter
        if (typeof cancelled_date != 'undefined' && cancelled_date.length != '') {
            if (aData[1].length == 0 || aData[1] != cancelled_date) {
                return false;
            }
        }

        return true;
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

})
