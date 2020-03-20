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

    $('select[name="selectDateRange"]').select2({
        allowClear: true,
        width: "100%"
    }).val('').trigger("change");

    $('select[name="selectDateRange"]').on('change', function() {
        var selectedVal = $(this).val();
        if (selectedVal == "0") {
            $('#betweenDatesSelected').removeClass('hidden')
        } else {
            $('#betweenDatesSelected').addClass('hidden')
        }
    })

    // Date range picker
    var createdDate_min;
    var createdDate_max;
    $('#table-filter-profit-loss-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        createdDate_min = picker.startDate.format('DD-MM-YYYY');
        createdDate_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(createdDate_min + ' - ' + createdDate_max);
        $('#date-form-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))

    });
    $('#date-form-group').on('click', '.clear-date', function() {
        createdDate_min = '';
        createdDate_max = '';
        $('#table-filter-trailBalance-date').val('');
        $(this).remove();

    });

    $('#btn-export-excel').on('click', function() {

        let fromDate = createdDate_min;
        let toDate = createdDate_max;
        let flag = $('select[name="selectDateRange"]').find('option:selected').val();
        if (!isEmpty(flag)) {
            let parms = '';
            if (!isEmpty(fromDate)) {
                parms = 'fromDate=' + fromDate + '&';
            }
            if (!isEmpty(toDate)) {
                parms += 'toDate=' + toDate + '&';
            }
            parms += 'flag=' + flag;
            $.redirect(myContextPath + '/accounts/report/pl/report?' + parms, '', 'GET');
        } else {
            alert('Please Select Date Range.!')
        }
    })
    //     var exportoptions = {
    //         filename: function() {
    //             var d = new Date();
    //             var title;
    //             var dateRange = $('select[name="selectDateRange"]').find('option:selected').val();
    //             if (dateRange == "0") {
    //                 title = 'ProfitAndLoss' + '_' + createdDate_min + '-' + createdDate_max
    //             } else if (dateRange == "1") {
    //                 return 'Profit_Loss_Last_Year' + '_' + d.getFullYear();
    //             } else if (dateRange == "2") {
    //                 title = 'Profit_Loss_Current_Year' + '_' + d.getFullYear();
    //             } else if (dateRange == "3") {
    //                 title = 'Profit_Loss_6_months_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
    //             } else if (dateRange == "4") {
    //                 title = 'Profit_Loss_3_months_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
    //             }
    //             return title;
    //         },
    //         attr: {
    //             type: "button",
    //             id: 'dt_excel_export'
    //         },
    //         title: ''
    //     };

    //Profit And Loss Datatable
    var tableEle = $('#table-report-profitAndLoss');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/profitAndLoss/list/datasource",
            data: function(data) {
                data.fromDate = createdDate_min;
                data.toDate = createdDate_max;
                data.flag = $('select[name="selectDateRange"]').find('option:selected').val();
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
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')
        }//         ,
        //         buttons: [$.extend(true, {}, exportoptions, {
        //             extend: 'excelHtml5'
        //         })]
    })

    // Search Button
    $('#btn-search').on('click', function() {
        if (isEmpty($('#selectDateRange').val())) {
            alert('Please Select Date Range.!')
            return false
        }
        table.ajax.reload();
    })

    //     jQuery.fn.DataTable.Api.register('buttons.exportData()', function(options) {
    //         if (this.context.length) {
    //             var data = profitAndLossFormat(createdDate_min, createdDate_max, $('select[name="selectDateRange"]').find('option:selected').val());

    //             var columnDefs = getExcelExportColumnDefs();
    //             let header = columnDefs.map((col)=>col.name);
    //             return {
    //                 body: formatExcelData(data.data, columnDefs),
    //                 header: header
    //             };
    //         }
    //     });

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

    var tableFooterTax = $('#table-report-profitAndLoss>tfoot>tr.totalTax');
    tableFooterTax.find('input[name=tax].tax-percentange').on('keyup', function() {
        updateFooter(table);

    });

});

function format(data) {
    var element = $('#clone-element-container').find('.profit-loss-item-container').clone();
    var row = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < data.items.length; i++) {
        var rowEle = $(row).clone();
        $(rowEle).find('td.s-no>span').html(i + 1);
        $(rowEle).find('td.code').html(ifNotValid(data.items[i].code, ''));
        $(rowEle).find('td.subAccount').html(ifNotValid(data.items[i].subAccount, ''));
        $(rowEle).find('td.ptdAmount span.autonumber').html(ifNotValid(data.items[i].ptdAmount, ''));
        $(rowEle).find('td.ytdAmount span.autonumber').html(ifNotValid(data.items[i].ytdAmount, ''));
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

// function profitAndLossFormat(fromDate, toDate, flag) {
//     //var queryString = "?fromDate=" + fromDate + "&toDate=" + toDate + "&flag=" + flag;
//     data = {};
//     data.fromDate = fromDate;
//     data.toDate = toDate;
//     data.flag = flag;
//     var response;
//     $.ajax({
//         beforeSend: function() {
//             $('#spinner').show()
//         },
//         complete: function() {
//             $('#spinner').hide();
//         },
//         type: "get",
//         async: false,
//         data: data,
//         url: myContextPath + "/accounts/report/profitAndLoss/search-data",
//         contentType: "application/json",
//         success: function(data) {
//             response = data;
//         }
//     });
//     return response;
// }
// function getExcelExportColumnDefs() {
//     return [{
//         'data': 'code',
//         'name': 'Code'
//     }, {
//         'data': 'account',
//         'name': 'Account'
//     }, {
//         'data': 'subAccount',
//         'name': 'Sub Account'
//     }, {
//         'data': 'ptdAmount',
//         'name': 'PTD Amount'
//     }, {
//         'data': 'ytdAmount',
//         'name': 'YTD Amount'
//     }, {
//         'data': 'totalPtdAmount',
//         'name': 'Total PTD Amount'
//     }, {
//         'data': 'totalYtdAmount',
//         'name': 'Total YTD Amount'
//     }]
// }

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }

    var tableFooter = $('#table-report-profitAndLoss>tfoot>tr.total')
    var tableFooterTax = $('#table-report-profitAndLoss>tfoot>tr.totalTax')

    // purchase cost total
    var pagePTDAmountTotal = table.column(1, {
        page: 'current'
    }).nodes().reduce(function(a, b) {

        var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // purchase cost total tax included
    var pagePTDAmountTotalTaxIncluded = table.column(1, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var total = 0;
        if (pagePTDAmountTotal > 0) {
            var tax = tableFooterTax.find('input[name=tax].tax-percentange').val();
            var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
            var pagePTDAmountTotalTax = Number((amount * tax) / 100);
            total = Number(amount - pagePTDAmountTotalTax);

        } else {
            var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
            total = Number(amount);

        }
        return intVal(a) + total;
    }, 0);

    // purchase cost total
    var pageYTDAmountTotal = table.column(2, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // purchase cost tax total tax included
    var pageYTDAmountTotalTaxIncluded = table.column(2, {
        page: 'current'
    }).nodes().reduce(function(a, b) {

        var total = 0;
        if (pageYTDAmountTotal > 0) {
            var tax = tableFooterTax.find('input[name=tax].tax-percentange').val();
            var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
            var pagePTDAmountTotalTax = Number((amount * tax) / 100);
            total = Number(amount - pagePTDAmountTotalTax);
        }
        if (pageYTDAmountTotal < 0) {
            var amount = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
            total = Number(amount);
        }
        return intVal(a) + total;
    }, 0);

    //Total Without Tax
    tableFooter.find('.ptdTotal span.autonumber').autoNumeric('init').autoNumeric('set', pagePTDAmountTotal);

    tableFooter.find('.ytdTotal span.autonumber').autoNumeric('init').autoNumeric('set', pageYTDAmountTotal);

    //Total With Tax
    tableFooterTax.find('.ptdTotalTax span.autonumber').autoNumeric('init').autoNumeric('set', pagePTDAmountTotalTaxIncluded);

    tableFooterTax.find('.ytdTotalTax span.autonumber').autoNumeric('init').autoNumeric('set', pageYTDAmountTotalTaxIncluded);

    var pageOverallTotal = Number(pagePTDAmountTotal) + Number(pageYTDAmountTotal);

}
