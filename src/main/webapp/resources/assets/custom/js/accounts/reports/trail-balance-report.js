var subAccountBody = $('#subAccountBody');
var tableEle = subAccountBody.find('table')
var subAccountDetailTable, subAccountDateFilter;
var createdDate_min;
var createdDate_max;
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
    //Sub Account Detail Date Filter

    $('#table-filter-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    });
    
    $("#excel_export_all").on("click", function() {
        $("#groupselect").attr("checked", false);
        $(".selectBox").attr("checked", false);
        //table.rows('.selected').deselect();
        table.button("#dt_excel_export_all").trigger();

    });
    //Customer Accounts Datatable
    var table = $('#table-report-trailBalance').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/trailBalance/list/datasource",
            data: function(data) {
                data.toDate = $('#table-filter-date').val();

            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        }, {
            targets: 0,
            "data": "accountNo",

        }, {
            targets: 1,
            "data": "description",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="code" data-target="#modal-subaccount-details" data-code="' + row.accountNo + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 2,
            "className": "dt-right details-control",
            "data": "obDebit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 3,
            "className": "dt-right details-control",
            "data": "obCredit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "className": "dt-right details-control",
            "data": "debit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "data": "credit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "data": "cbDebit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "dt-right details-control",
            "data": "cbCredit",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-report-trailBalance').find('span.autonumber').autoNumeric('init')
        },
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            var intVal = function(i) {
                return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
            };
            var isValid = function(val) {
                return typeof val === 'undefined' || val == null ? 0 : val;
            }
            //opening balance debit totals
            var obDebitTotal = table.column(2, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);
            //opening balance credit totals
            var obCreditTotal = table.column(3, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);
            //opening balance credit totals
            var monthDebitTotal = table.column(4, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);
            //opening balance credit totals
            var monthCredittTotal = table.column(5, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);
            //closing balance debit totals
            var cbDebitTotal = table.column(6, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);
            //closing balance credit totals
            var cbCreditTotal = table.column(7, {
                page: 'current'
            }).nodes().reduce(function(a, b) {
                var val = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));
                return intVal(a) + val;
            }, 0);

            $(table.column(2).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', obDebitTotal);
            $(table.column(3).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', obCreditTotal);
            $(table.column(4).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', monthDebitTotal);
            $(table.column(5).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', monthCredittTotal);
            $(table.column(6).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', cbDebitTotal);
            $(table.column(7).footer()).find('span.autonumber').autoNumeric('init').autoNumeric('set', cbCreditTotal);
        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'TrailBalanceReport_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0,1, 2, 3, 4, 5, 6, 7]
            }
        }]

    });

    // Search Button
    $('#btn-search').on('click', function() {
        table.ajax.reload();

    })
    //Sub Account Detail Modal
    var subAccountDetailsEle = $('#modal-subaccount-details')
    var subAccountDetailsModalBody = subAccountDetailsEle.find('#subAccountBody');
    subAccountDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var code = targetElement.attr('data-code');
        setSubAccountDetailsData(code, '', '');
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));

        $('#subAccountBody').find('table').dataTable().fnDestroy();
    });

    //Sub Account Detail Date Filter
    // Date range picker
        $('#table-filter-subAccountDetail-date').daterangepicker({
            autoUpdateInput: false,
            clearBtn: true
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
            $('#table-filter-subAccountDetail-date').val('');
            $(this).remove();
        })

//     $('#table-filter-subAccountDetail-date').daterangepicker({
//         autoApply: true,
//         autoUpdateInput: false,
//         format: 'dd-MM-yyyy - dd-MM-yyyy',
//         ranges: {
//             'Today': [moment(), moment()],
//             'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
//             'This Week': [moment().startOf('week'), moment()],
//             'Last 7 Days': [moment().subtract(6, 'days'), moment()],
//             'Last 30 Days': [moment().subtract(29, 'days'), moment()],
//             'This Month': [moment().startOf('month'), moment().endOf('month')],
//             'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
//         }
//     }, cb);
//     function cb(start, end) {
//         if (start._isValid && end._isValid) {
//             start = start.format('DD-MM-YYYY');
//             end = end.format('DD-MM-YYYY')
//             $('#table-filter-subAccountDetail-date').val(start + ' - ' + end);
//         } else {
//             $('#table-filter-subAccountDetail-date').val('');
//         }
//     }

    $('#btn-date-search').on('click', function() {
       
        subAccountDetailTable.ajax.reload();
    })
})

function setSubAccountDetailsData(code,start , end) {
  
    subAccountDetailTable = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": false,
        "ajax": {
            url: myContextPath + "/costofsales/details/data",
            data: function(data) {
                data.fromDate = createdDate_min
                data.toDate = createdDate_max
                data.code = code;
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            className: 'details-control',
            "name": "Code",
            "data": "code",

        }, {
            targets: 1,
            "className": "details-control",
            "name": "Date",
            "data": "createdDate",

        }, {
            targets: 2,
            "className": "details-control",
            "name": "Transaction Id",
            "data": "transactionId"
        }, {
            targets: 3,
            "className": "details-control",
            "name": "Account Name",
            "data": "accountName",

        }, {
            targets: 4,
            "className": "dt-right details-control",
            "name": "Opening Balance",
            "data": "closingBalance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "name": "Debit",
            "render": function(data, type, row) {
                var returnDebitAmount;
                if (row.type == 0) {
                    returnDebitAmount = '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
                } else {
                    returnDebitAmount = "";
                }
                return returnDebitAmount;
            }
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "name": "Credit",
            "render": function(data, type, row) {
                var returnCreditAmount;
                if (row.type == 1) {
                    returnCreditAmount = '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
                } else {
                    returnCreditAmount = "";
                }
                return returnCreditAmount;
            }
        }, {
            targets: 7,
            "className": "dt-right details-control",
            "name": "Balance",
            "data": "balance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-detail-subAccount').find('span.autonumber').autoNumeric('init')
        }

    })

}
