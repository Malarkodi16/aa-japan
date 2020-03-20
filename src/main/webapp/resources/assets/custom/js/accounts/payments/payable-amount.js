var selected;
$(function() {
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            table.ajax.reload()
        } else if ($(this).val() == 1) {
            table.ajax.reload()
        } else if ($(this).val() == 3) {
            table.ajax.reload()
        } else if ($(this).val() == 4) {
            table.ajax.reload()
        }
    })

   function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //table request from sales
    var tableEle = $('#table-amount-payable');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": {
            "url": myContextPath + "/accounts/payment/payable-amount/data-source",
            "data": function(data) {
                 selected = $('input[name="radioShowTable"]:checked').val();
                data["flag"] = selected;
                return data;
            }
        },
        ordering: true,

        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "remitter"
        }, {
            targets: 1,
            "className": "dt-right details-control",
            "data": "receivableAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="receivableAmount" data-a-sign="¥ " data-m-dec="0">' +( isNaN(data)?0:data) + '</span>';
            }
        }, {
            targets: 2,
            "className": "details-control",
            "data": "sequenceId",
            "visible": false
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },

        "drawCallback": function(settings, json) {
           $('#table-amount-payable').find('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });
//     table.on('click', 'td.details-control', function() {
//         var tr = $(this).closest('tr');
//         var row = table.row(tr);
//         if (row.child.isShown()) {
//             row.child.hide();
//             tr.removeClass('shown');
//         } else {
//             table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
//                 var row = table.row(rowIdx);
//                 if (row.child.isShown()) {
//                     row.child.hide();
//                     tr.removeClass('shown');
//                 }

//             })
//             var rowData = row.data();
//             var childHtml = '';
//             if (selected == 0) {
//                 childHtml = format(rowData)
//             } else if (selected == 1) {
//                 childHtml = transportFormat(rowData)
//             } else if (selected == 2) {
//                 childHtml = othersFormat(rowData)
//             } else if (selected == 3) {
//                 childHtml = othersFormat(rowData)
//             } else if (selected == 4) {
//                 childHtml = storageFormat(rowData)
//             }

//             row.child(childHtml).show();
//             row.child().find('span.autonumber').autoNumeric('init');
//             tr.addClass('shown');

//         }
//     });
})
//Purchase Format
function format(rowData) {
    var element = $('#clone-container>#invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let purchaseCostTotal = 0
      , purchaseCostTaxTotal = 0
      , commisionTotal = 0
      , commisionTaxTotal = 0
      , roadTaxTotal = 0
      , recycleTotal = 0
      , otherChargesTotal = 0;
    for (var i = 0; i < rowData.paymentItems.length; i++) {
        var row = $(rowClone).clone();

        purchaseCostTotal += Number(ifNotValid(rowData.paymentItems[i].purchaseCost, 0));
        purchaseCostTaxTotal += Number(ifNotValid(rowData.paymentItems[i].purchaseCostTaxAmount, 0));
        commisionTotal += Number(ifNotValid(rowData.paymentItems[i].commision, 0));
        commisionTaxTotal += Number(ifNotValid(rowData.paymentItems[i].commisionTaxAmount, 0));
        roadTaxTotal += Number(ifNotValid(rowData.paymentItems[i].roadTax, 0));
        recycleTotal += Number(ifNotValid(rowData.paymentItems[i].recycle, 0));
        otherChargesTotal += Number(ifNotValid(rowData.paymentItems[i].otherCharges, 0));

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.paymentItems[i].stockNo, ''));
        $(row).find('td.purchaseCost span.autonumber').html(ifNotValid(rowData.paymentItems[i].purchaseCost, ''));
        $(row).find('td.purchaseCostTax span.autonumber').html(ifNotValid(rowData.paymentItems[i].purchaseCostTaxAmount, ''));
        $(row).find('td.commision span.autonumber').html(ifNotValid(rowData.paymentItems[i].commision, ''));
        $(row).find('td.commisionTax span.autonumber').html(ifNotValid(rowData.paymentItems[i].commisionTaxAmount, ''));
        $(row).find('td.roadTax span.autonumber').html(ifNotValid(rowData.paymentItems[i].roadTax, ''));
        $(row).find('td.recycle span.autonumber').html(ifNotValid(rowData.paymentItems[i].recycle, ''));
        $(row).find('td.otherCharges span.autonumber').html(ifNotValid(rowData.paymentItems[i].otherCharges, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }

    return element;

}
//Transport Format
function transportFormat(rowData) {
    var element = $('#transport-clone-container>#payment-transport-approve-details>.clone-element').clone();
    $(element).find('input[name="transportinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let taxTotal = 0;
    let amountTotal = 0;
    for (var i = 0; i < rowData.paymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.paymentItems[i].stockNo, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.paymentItems[i].amount, ''));
        $(row).find('td.tax>span').html(ifNotValid(rowData.paymentItems[i].taxAmount, ''));
        taxTotal += Number(ifNotValid(rowData.paymentItems[i].taxAmount, 0));
        amountTotal += Number(ifNotValid(rowData.paymentItems[i].amount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}
//Others Format
function othersFormat(rowData) {
    var element = $('#others-clone-container>#others-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.paymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.type').html((ifNotValid(rowData.paymentItems[i].invoiceType, '').toLowerCase() != 'others' ? ifNotValid(rowData.paymentItems[i].category, '') : ifNotValid(rowData.paymentItems[i].categoryOthers, '')));
        $(row).find('td.remarks').html(ifNotValid(rowData.paymentItems[i].remarks, ''));
        amountTotal += Number(ifNotValid(rowData.paymentItems[i].amount, 0));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.paymentItems[i].amount, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}
//Purchase Format
function storageFormat(rowData) {
    var element = $('#storage-clone-container>#storage-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.paymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.paymentItems[i].chassisNo, ''));
        $(row).find('td.category').html(ifNotValid(rowData.paymentItems[i].category, ''));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.paymentItems[i].amount, ''));
        amountTotal += Number(ifNotValid(rowData.paymentItems[i].amount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }

    // total Amount
    var grandTotal = table.column(1, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="receivableAmount"]'))));
        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
  $('#table-amount-payable>tfoot>tr.sum').find('span.grandTotal').autoNumeric('init').autoNumeric('set', grandTotal);

}