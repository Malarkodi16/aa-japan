$(function() {
    // Datatable filter
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
  
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $.getJSON(myContextPath + "/data/bank.json", function(data) {
        $('#bank').select2({
            allowClear: "true",
            width: "100%",
            data: $.map(data, function(bank) {
                return {
                    id: bank.bankSeq,
                    text: bank.bankName
                };
            })
        }).val('').trigger('change');
    })
    var tableEle = $('#table-viewloan');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": myContextPath + "/accounts/viewloan/details/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "searchable": false,
            className: 'details-control',
            "name": "bankName",
            "data": "bankName",
        }, {
            targets: 1,
            "searchable": true,
            "className": "details-control",
            "visible": false,
            "name": "loanId",
            "data": "loanId",

        }, {
            targets: 2,
            "className": "details-control",
            "searchable": true,
            "name": "reference",
            "data": "reference"
        }, {
            targets: 3,
            "className": "details-control",
            "searchable": true,
            "name": "date",
            "data": "date"
        }, {
            targets: 4,
            "className": "details-control",
            "searchable": true,
            "name": "loanType",
            "data": "loanType",
            "render": function(data, type, row, meta) {
                if (data == 1) {
                    return 'Syndicate Loan'
                } else if (data == 2) {
                    return 'Toza Loan'
                } else if (data == 3) {
                    return 'Swap Loan/Corporate bond'
                } else if (data == 4) {
                    return 'Normal Loan'
                }
            }
        }, {
            targets: 5,
            "className": "details-control",
            "searchable": true,
            "name": "loanTerm",
            "data": "loanTerm",

        }, {
            targets: 6,
            "className": "details-control",
            "searchable": true,
            "name": "rateOfInterest",
            "data": "rateOfInterest",

        }, {
            targets: 7,
            "className": "details-control",
            "searchable": true,
            "name": "firstPaymentDate",
            "data": "firstPaymentDate",

        }, {
            targets: 8,
            "className": "dt-right details-control",
            "searchable": true,
            "name": "loanAmount",
            "data": "loanAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="loanAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 9,
            "className": "dt-right details-control",
            "searchable": true,
            "name": "totalPayable",
            "data": "totalPayable",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="totalPayable" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 10,
            "className": "dt-right details-control",
            "searchable": true,
            "name": "closingBalance",
            "data": "closingBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="closingBalance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 11,
            "className": "details-control",
            "render": function(data, type, row) {
                var html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/accounts/loan/edit/' + row.loanId + '" class="ml-5 btn btn-info btn-xs"><i class="fa fa-fw fa-edit"></i></a>'

                }
                return html;
            }

        }, {
            targets: 12,
            "visible": false,
            "className": "details-control",
            "data": "bank",

        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },

        "fnDrawCallback": function(oSettings) {
            $('#table-viewloan').find('span.autonumber').autoNumeric('init')
        }

    });

    /* table on click to append loandetails */
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
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;
    // Search Button
    var bankFilter;
    $('#btn-searchData').on('click', function() {
        bankFilter = $('select[name="bank"]').val();
        table.ajax.reload();
    })

    // DataTable Filter
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // date filter
        if (typeof bankFilter != 'undefined' && bankFilter.length != '') {
            if (aData[12].length == 0 || aData[12] != bankFilter) {
                return false;
            }
        }
        return true;
    });

})

/* function to format loan details */

function format(rowData) {
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }

    var tbody = '';
    for (var i = 0; i < rowData.loanDetails.length; i++) {
        var className = rowData.loanDetails[i].status == 1 ? "success" : "warning"
        var td = '<td>' + (i + 1) + '</td>';
        td += '<td class="align-center">' + ifNotValid(rowData.loanDetails[i].dueDate, '') + '</td>'
        td += '<td class="dt-right "><span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(rowData.loanDetails[i].principalAmt, '') + '</span></td>';
        td += '<td class="dt-right "><span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(rowData.loanDetails[i].interestAmount, '') + '</span></td>';
        td += '<td class="dt-right "><span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(rowData.loanDetails[i].amount, '') + '</span></td>';
        td += '<td class="dt-right "><span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(rowData.loanDetails[i].openingBalance, '') + '</span></td>';
        td += '<td class="dt-right "><span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(rowData.loanDetails[i].closingBalance, '') + '</span></td>';
        td += '<td class="align-center"><span class="label label-' + className + '">' + (rowData.loanDetails[i].status == 1 ? "Paid" : "Not Paid") + '</span></td>'
        tbody += '<tr>' + td + '</tr>'
    }
    var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container">' + '    <table class="table table-bordered">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 10px">#</th><th style="width: 100px" class="align-center bg-ghostwhite">Due Date</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Principal Amount</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Interest Amount</th>' + '<th style="width: 100px" class="align-center bg-ghostwhite">Total Amount</th>' + '<th class="align-center bg-ghostwhite">Opening Balance</th>' + '<th class="align-center bg-ghostwhite">Closing Balance</th>' + '<th class="align-center bg-ghostwhite">Status</th></tr></thead><tbody>' + tbody + '</tbody></table></div></div>';

    return html;
}

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // Capital Amount(JPY) Total
    var pageCapitalAmountTotal = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="loanAmount"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var pagePayableTotal = table.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="totalPayable"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var pageClosingBalanceTotal = table.column(10, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="closingBalance"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-viewloan>tfoot>tr.sum').find('span.capitalAmountTotal').autoNumeric('init').autoNumeric('set', pageCapitalAmountTotal);

    $('#table-viewloan>tfoot>tr.sum').find('span.payableTotal').autoNumeric('init').autoNumeric('set', pagePayableTotal);

    $('#table-viewloan>tfoot>tr.sum').find('span.closingBalanceTotal').autoNumeric('init').autoNumeric('set', pageClosingBalanceTotal);

}
