$(function() {
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

    $('#table-filter-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    });

    $('#btn-export-excel').on('click', function() {
        let date = $('#table-filter-date').val();
        if (!isEmpty(date)) {
            $.redirect(myContextPath + '/accounts/report/bs/report?date=' + date, '', 'GET');
        } else {
            alert('Please select date.!')
        }
    })
    var exportoptions = {
        filename: function() {
            var d = new Date();
            return 'BalanceStatement_' + $('#table-filter-date').val();
        },
        attr: {
            type: "button",
            id: 'dt_excel_export'
        },
        title: ''
    };
    //Balance Statement Datatable
    var tableEle = $('#table-report-balanceStatement');
    var table = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength" : 25,
        "order": [[3, "asc"]],
        "ajax": {
            url: myContextPath + "/accounts/report/balanceStatement/list/datasource",
            data: function(data) {
                data.toDate = $('#table-filter-date').val();
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        }, {
            targets: 0,
            "className": "details-control",
            "data": "account",

        }, {
            targets: 1,
            visible: false,
            "className": "dt-right details-control",
            "data": "ptdAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 2,
            "className": "dt-right details-control",
            "data": "ytdAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="ytdAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 3,
            "visible": false,
            "className": "dt-right details-control",
            "data": "ptdAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            if (!isEmpty(data)) {
                $('#table-report-balanceStatement>tfoot>tr.total>.ptdTotal ').find('span.autonumber').autoNumeric('init').autoNumeric('set', data[0].plPtdAmount);
                $('#table-report-balanceStatement>tfoot>tr.total>.ytdTotal ').find('span.autonumber').autoNumeric('init').autoNumeric('set', data[0].plYtdAmount);
            }
        },
         "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-report-balanceStatement').find('span.autonumber').autoNumeric('init');
        },
        buttons: [$.extend(true, {}, exportoptions, {
            extend: 'excelHtml5'
        })]
    })

    // Search Button
    $('#btn-search').on('click', function() {
        table.ajax.reload();

    });
    jQuery.fn.DataTable.Api.register('buttons.exportData()', function(options) {
        if (this.context.length) {
            var data = balanceStatementFormat();
            plData = {
                account: "Profit and Loss",
                code: null,
                plYtdAmount: null,
                ptdAmount: null,
                subAccount: null,
                totalPtdAmount: null,
                totalYtdAmount: null,
                ytdAmount: data.data[0].plYtdAmount
            };
            data.data.push(plData)
            var columnDefs = getExcelExportColumnDefs();
            let header = columnDefs.map((col)=>col.name);
            return {
                body: formatExcelData(data.data, columnDefs),
                header: header
            };
        }
    });

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
            var rowData = format(row.data());
            row.child(rowData).show();
            rowData.find('span.autonumber').autoNumeric('init');
            tr.addClass('shown');
        }

    });
});

function format(data) {
    var element = $('#clone-element-container').find('.sub-account-item-container').clone();
    var row = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < data.items.length; i++) {
        var rowEle = $(row).clone();
        $(rowEle).find('td.s-no>span').html(i + 1);
        $(rowEle).find('td.code').html(ifNotValid(data.items[i].code, ''));
        $(rowEle).find('td.subAccount').html(ifNotValid(data.items[i].subAccount, ''));
        // $(rowEle).find('td.ptdAmount').html(ifNotValid(data.items[i].ptdAmount, ''));
        $(rowEle).find('td.ytdAmount>span').html(ifNotValid(data.items[i].ytdAmount, ''));
        $(rowEle).removeClass('hide');
        $(element).find('table>tbody').append(rowEle);
    }
    return element;
}
function formatExcelData(data, header) {

    let results = data.map((item)=>{
        var result = [];
        for (let i = 0; i < header.length; i++) {
            var value = ifNotValid(item[header[i].data], '')
            result.push(value)
        }
        return result
    }
    )
    return results
}

function balanceStatementFormat() {

    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        async: false,
        url: myContextPath + "/accounts/report/balanceStatement/search-data?toDate=" + $('#table-filter-date').val(),
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function getExcelExportColumnDefs() {
    return [{
        'data': 'code',
        'name': 'Code'
    }, {
        'data': 'account',
        'name': 'Account'
    }, {
        'data': 'subAccount',
        'name': 'Sub Account'
    }, {
        'data': 'ytdAmount',
        'name': 'YTD Amount'
    }]
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
    var ytdAmount = table.column(11, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="ytdAmount"]'))));
        return intVal(a) + amount;
    }, 0);
   
    $('#table-report-balanceStatement>tfoot>tr.sum').find('span.soldAmountTotal').autoNumeric('init').autoNumeric('set', ytdAmount);

}