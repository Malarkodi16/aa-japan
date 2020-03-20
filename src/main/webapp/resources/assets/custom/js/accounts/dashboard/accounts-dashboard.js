$(function() {
    $.getJSON(myContextPath + "/accounts/dash-board/total-amount", function(data) {
        setAmountDashboardStatus(data.data)
    });

    //     var bankName;
    //     $.getJSON(myContextPath + '/master/dash-board/bank.json', function(data) {
    //         bankJson = data;
    //         $('#bank').select2({
    //             allowClear: true,
    //             width: '100%',
    //             data: $.map(bankJson, function(item) {
    //                 return {
    //                     id: item.bankName,
    //                     text: item.bankName,
    //                     data: item
    //                 };
    //             })
    //         }).on('change', function() {
    //             bankName = $(this).val();
    //             tableMBank.draw();
    //         })
    //     })

    var tableEle = $('#table-master-bank-list')
    var tableMBank = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ajax": myContextPath + "/master/list-bank-data",
        aaSorting: [[3, 'asc']],
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "orderable": false,
            "data": "symbol",
            "className": "details-control",

        }, {
            targets: 1,
            "orderable": false,
            "data": "currency",
            "className": "details-control",
        }, {
            targets: 2,
            "orderable": false,
            "className": "details-control",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<div>' + row.symbol + ' ' + '<span class="amount" id="yenBalance" data-m-dec="0">' + data + '</span></div>';

            }
        }, {
            targets: 3,
            "orderable": true,
            "visible": false,
            "data": "currencySeq",
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('span.amount').autoNumeric('init')
        }
    })

    var tableSOEle = $('#table-so')
    var tableSo = tableSOEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ordering" : false,
        "ajax": myContextPath + "/sales/all/salesorder",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "orderable": false,
            "data": "createdDate"
        }, {
            targets: 1,
            "orderable": false,
            "data": "fCustomerName"
        }, {
            targets: 2,
            "orderable": false,
            "data": "salesPerson"
        }, {
            targets: 3,
            "orderable": false,
            "data": "maker",
            "render": function(data, type, row) {
                var data;
                //data = data == null ? '' : data;
                data = row.maker + ' / ' + row.model
                return data;
            }
        }, {
            targets: 4,
            "data": "purchaseCostTotal",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {

                    return '<span class="amount"  data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 5,
            "data": "salesAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {

                    return '<div>' + row.currencySymbol + ' ' + '<span class="amount" data-m-dec="0">' + data + '</span></div>';
                }
            }
        }],

        "drawCallback": function(settings, json) {
            tableSOEle.find('span.amount').autoNumeric('init')

        }
    })
    //     $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

    //         if (typeof bankName != 'undefined' && bankName.length != '') {
    //             if (aData[0].length == 0 || aData[0] != bankName) {
    //                 return false;
    //             }
    //         }
    //         return true;
    //     })

    tableMBank.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = tableMBank.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            tableMBank.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = tableMBank.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            row.child().find('span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

})

function format(rowData) {
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }

    var tbody = '';
    for (var i = 0; i < rowData.bankDetails.length; i++) {
        var td = '<td>' + (i + 1) + '</td>';
        td += '<td class="align-center">' + ifNotValid(rowData.bankDetails[i].bankSeq, '') + '</td>'
        td += '<td class="align-center">' + ifNotValid(rowData.bankDetails[i].bankName, '') + '</td>'
        td += '<td class="align-center">' + ifNotValid(rowData.bankDetails[i].coaCode, '') + '</td>'
        td += '<td class="align-center"><span class="autonumber" data-a-sign="' + rowData.symbol + ' " data-m-dec="0">' + ifNotValid(rowData.bankDetails[i].yenBalance, '') + '</span></td>';
        if (rowData.bankDetails[i].currencyType != 1) {
            td += '<td class="align-center"><span class="autonumber" data-a-sign="' + rowData.symbol + '" data-m-dec="0">' + ifNotValid(rowData.bankDetails[i].clearingBalance, '') + '</span></td>';
        }
        tbody += '<tr>' + td + '</tr>'
    }
    var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container">' + '    <table class="table table-bordered">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 20px">#</th><th style="width: 100px" class="align-center bg-ghostwhite">Bank Seq.</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Name</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Coa Code</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Total Balance</th>' + '<th class="align-center bg-ghostwhite">Clearing Balance</th>' + '</tr></thead><tbody>' + tbody + '</tbody></table></div></div>';

    return html;
}

function setAmountDashboardStatus(data) {
    setAutonumericValue($('#total-auction').find('.autoNumeric'), data.auction)
    setAutonumericValue($('#total-freight-shipping').find('.autoNumeric'), data.freight)
    setAutonumericValue($('#total-transport').find('.autoNumeric'), data.transport)
    setAutonumericValue($('#total-general-expense').find('.autoNumeric'), data.generalExpense)
    setAutonumericValue($('#total-storage').find('.autoNumeric'), data.storage)

}
