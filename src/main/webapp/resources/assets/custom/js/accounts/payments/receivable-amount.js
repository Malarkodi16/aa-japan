$(function() {
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/$/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    //Amount Receivable Datatable
    var tableEle = $('#table-payment-receivable-amount');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": myContextPath + "/accounts/payment/receivableAmount/data-source",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        },{
            targets: 0,
            "className": "details-control",
            "data": "code",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "fullName",

        },{
            targets: 2,
            "className": "dt-right details-control",
            "data": "creditBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="creditBalance" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }

        },{
            targets: 3,
            "className": "dt-right details-control",
            "data": "total",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="creditBalance" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }

        },{
            targets: 4,
            "className": "dt-right details-control",
            "data": "amountReceived",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="creditBalance" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 5,
            "className": "dt-right details-control",
            "data": "receivableAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="receivableAmount" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }
        },{
            targets: 6,
            "className": "dt-right details-control",
            "data": "balance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
//                     var amount = row.creditBalance + data;
                return '<span class="autonumber" name="balance" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }
        }],
        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')
        }
    })

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
            var rowData = row.data();
            row.child(format(rowData)).show();
            row.child().find('span.autonumber').autoNumeric('init');
            tr.addClass('shown');
        }

    });

})

function format(rowData) {
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    var currencySymbol = isEmpty(rowData.currencyDetails) ? '' : ifNotValid(rowData.currencyDetails.symbol, '');
    var tbody = '';
    for (var i = 0; i < rowData.salesDetails.length; i++) {
        var td = '<td>' + (i + 1) + '</td>';
        td += '<td class="align-center">' + ifNotValid(rowData.salesDetails[i].chassisNo, '') + '</td>'
        td += '<td class="align-center">' + ifNotValid(rowData.salesDetails[i].maker, '') + '</td>';
        td += '<td class="align-center">' + ifNotValid(rowData.salesDetails[i].model, '') + '</td>';
        td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="0">' + ifNotValid(rowData.salesDetails[i].fob, '') + '</span></td>';
        td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="0">' + ifNotValid(rowData.salesDetails[i].insurance, '') + '</span></td>';
        td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="0">' + ifNotValid(rowData.salesDetails[i].shipping, '') + '</span></td>';
        td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="0">' + ifNotValid(rowData.salesDetails[i].freight, '') + '</span></td>';
        td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="0">' + ifNotValid(rowData.salesDetails[i].total, '') + '</span></td>';
        tbody += '<tr>' + td + '</tr>'
    }
    var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container">' + '    <table class="table table-bordered">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 10px">#</th><th style="width: 100px" class="align-center bg-ghostwhite">Chassis No</th><th style="width: 100px" class="align-center bg-ghostwhite">Maker</th><th style="width: 100px" class="align-center bg-ghostwhite">Model</th><th class="align-center bg-ghostwhite">FOB</th><th class="align-center bg-ghostwhite">Insurance</th><th class="align-center bg-ghostwhite">Shipping</th><th class="align-center bg-ghostwhite">Freight</th><th class="align-center bg-ghostwhite">Total</th></tr></thead><tbody>' + tbody + '</tbody></table></div></div>';

    return html;
}
