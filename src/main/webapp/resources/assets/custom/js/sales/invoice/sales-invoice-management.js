// Customize Datatable
$('#table-filter-search').keyup(function() {
    table.search($(this).val()).draw();
});
$('#table-filter-length').change(function() {
    table.page.len($(this).val()).draw();
});

 $.getJSON(myContextPath + "/data/invoice-management/status-count", function(data) {      
        $('#porforma-count').html(data.data.porforma);
        $('#sales-count').html(data.data.salesorder);
    });


/* Date picke function change */

var invoice_min;
var invoice_max;
$('#table-filter-invoice-date').daterangepicker({
    autoUpdateInput: false
}).on("apply.daterangepicker", function(e, picker) {
    invoice_min = picker.startDate;
    invoice_max = picker.endDate;
    picker.element.val(invoice_min.format('DD-MM-YYYY') + ' - ' + invoice_max.format('DD-MM-YYYY'));
    invoice_min = invoice_min._d.getTime();
    invoice_max = invoice_max._d.getTime();
    $('<div>', {
        'class': 'input-group-addon clear-date'
    }).append($('<i>', {
        'class': 'fa fa-times'
    })).appendTo($(this).closest('.input-group'))
    table.draw();
});
$('#date-form-group').on('click', '.clear-date', function() {
    invoice_min = '';
    invoice_max = '';
    table.draw();
    $('#table-filter-invoice-date').val('');
    $(this).remove();

});

/* Customer Id Select2 Filter */

$('#custId').select2({
    allowClear: true,
    minimumInputLength: 2,
    ajax: {
        url: myContextPath + "/customer/search?flag=customer",
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
                        id: item.code,
                        text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                    });
                });
            }
            return {
                results: results
            }

        }

    }
});

//Filter with Customer
var customerId;
$('.customer').change(function() {
    customerId = $(this).val();
    table.draw();

});
$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    // date filter
    if (typeof invoice_min != 'undefined' && invoice_min.length != '') {
        if (aData[6].length == 0) {
            return false;
        }
        if (typeof aData._date == 'undefined') {
            aData._date = moment(aData[6], 'DD-MM-YYYY')._d.getTime();
        }
        if (invoice_min && !isNaN(invoice_min)) {
            if (aData._date < invoice_min) {
                return false;
            }
        }
        if (invoice_max && !isNaN(invoice_max)) {
            if (aData._date > invoice_max) {
                return false;
            }
        }
    }
    if (typeof customerId != 'undefined' && customerId.length != '') {
        if (aData[7].length == 0 || aData[7] != customerId) {
            return false;
        }
    }
    return true;
});

$("#excel_export_all").on("click", function() {
    $("#groupselect").attr("checked", false);
    $(".selectBox").attr("checked", false);
    table.rows('.selected').deselect();
    table.button("#dt_excel_export_all").trigger();

});

/* Sales order invoice list Data-table */

var tableEle = $('#table-sales-order-invoice');
var table = tableEle.DataTable({
   "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength" : 25,
    "ajax": myContextPath + "/sales/orderInvoice/list/datasource",
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
        className: 'select-checkbox',
        "data": "id",
        "visible": false,
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            if (type === 'display') {
                return '<input class="selectBox" type="checkbox" data-id="' + row.invoiceNo + '" value="' + data + '">'
            }
            return data;
        }
    }, {
        targets: 1,
        "className": "details-control",
        "data": "invoiceNo"
    }, {
        targets: 2,
        "className": "details-control",
        "data": "fCustomerName"
    }, {
        targets: 3,
        "className": "details-control",
        "data": "fConsigneeName"
    }, {
        targets: 4,
        "className": "details-control",
        "data": "fNotifyName",
    }, {
        targets: 5,
        "className": "details-control",
        "data": "paymentType"
    }, {
        targets: 6,
        "className": "details-control",
        "data": "createdDate",
        "visible": false
    }, {
        targets: 7,
        "data": "customerId",
        "visible": false
    }, {
        targets: 8,
        "data": "invoiceNo",
        "className": 'align-center',
        "render": function(data, type, row) {
            var html = ''
            html += '<a href="' + myContextPath + '/download/sales/invoice/' + data + '.pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
            return html;
        }
    }],

    /*excel export*/
    buttons: [{
        extend: 'excel',
        text: 'Export All',
        title: '',
        filename: function() {
            var d = new Date();
            return 'SalesInvoiceList_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
        },
        attr: {
            type: "button",
            id: 'dt_excel_export_all'
        },
        exportOptions: {
            columns: [1, 2, 3, 4, 5]
        }
    }]

});

/* table on click to append sales invoice */

table.on('click', 'td.details-control', function() {
    var tr = $(this).closest('tr');
    var row = table.row(tr);
    if (row.child.isShown()) {
        row.child.hide();
        tr.removeClass('shown');
    } else {
        table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }

        })
        var detailsElement = format(row.data());
        row.child(detailsElement).show();
        row.child().find('span.autonumber').autoNumeric('init')
        tr.addClass('shown');
    }
});

/* function to format invoice details */

function format(rowData) {
    var element = $('#clone-container>#sales-order-invoice-rearrange>.clone-element').clone();
    $(element).find('input[name="salesOrderinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.salesInvoiceDetails.length; i++) {
        var row = $(rowClone).clone();
        var status;
        var className;
        if (rowData.salesInvoiceDetails[i].status == 0) {
            status = "Not Received"
            className = "label-default"

        } else if (rowData.salesInvoiceDetails[i].status == 1) {
            status = "Payment Received"
            className = "label-success"
        } else if (rowData.salesInvoiceDetails[i].status == 2) {
            status = "Paymeny Received Partial"
            className = "label-warning"
        }

        var actionHtml = ''
        if (rowData.salesInvoiceDetails[i].status == 1) {
            actionHtml = '<a href="#" class="ml-5 btn btn-danger btn-xs stock status-update" data-flag="specific" data-orderId="' + rowData.code + '" data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-close"></i></a>'
        }

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.salesInvoiceDetails[i].stockNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.salesInvoiceDetails[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.salesInvoiceDetails[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.salesInvoiceDetails[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.salesInvoiceDetails[i].model, ''));
        $(row).find('td.fob>span').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.salesInvoiceDetails[i].fob, '')).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' '
        });
        $(row).find('td.shipping>span').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.salesInvoiceDetails[i].shipping, '')).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' '
        });
        $(row).find('td.freight>span').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.salesInvoiceDetails[i].freight, '')).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' '
        });
        $(row).find('td.insurance>span').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.salesInvoiceDetails[i].insurance, '')).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' '
        });
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).find('td.action').html(actionHtml);
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
