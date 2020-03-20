var subAccountBody = $('#subAccountBody');
var tableEle = subAccountBody.find('table')
var subAccountDetailTable, subAccountDateFilter;
var createdDate_min;
var createdDate_max;
$(function() {
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //Customer Accounts Datatable
    var table = $('#table-report-trailBalance').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/accountTransaction/list/datasource"
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        }, {
            targets: 0,
            "data": "code",

        }, {
            targets: 1,

            "data": "account",

        }, {
            targets: 2,

            "data": "subAccount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="code" data-target="#modal-subaccount-details" data-code="' + row.code + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 3,
            "className": "dt-right",
            "data": "balance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="balance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-report-trailBalance').find('span.autonumber').autoNumeric('init')
        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'AccountTransaction_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0, 1, 2, 3],
            },
            customize: function(xlsx) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                var downrows = 5;
                var clRow = $('row', sheet);
                //update Row
                clRow.each(function() {
                    var attr = $(this).attr('r');
                    var ind = parseInt(attr);
                    ind = ind + downrows;
                    $(this).attr("r", ind);
                });

                // Update  row > c
                $('row c ', sheet).each(function() {
                    var attr = $(this).attr('r');
                    var pre = attr.substring(0, 1);
                    var ind = parseInt(attr.substring(1, attr.length));
                    ind = ind + downrows;
                    $(this).attr("r", pre + ind);
                });

                function Addrow(index, data) {
                    msg = '<row r="' + index + '">'
                    for (i = 0; i < data.length; i++) {
                        var key = data[i].k;
                        var value = data[i].v;
                        msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
                        msg += '<is>';
                        msg += '<t>' + value + '</t>';
                        msg += '</is>';
                        msg += '</c>';
                    }
                    msg += '</row>';
                    return msg;
                }

                var d = new Date();

                //insert
                var r2 = Addrow(2, [{
                    k: 'A',
                    v: 'Company'
                }, {
                    k: 'B',
                    v: 'AA Japan'
                }]);
                0
                var r3 = Addrow(3, [{
                    k: 'A',
                    v: 'Title'
                }, {
                    k: 'B',
                    v: 'Account Transaction Report'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear()
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }],

    });

    $("#excel_export_all").on("click", function() {
        table.button("#dt_excel_export_all").trigger();

    });

    //Sub Account Detail Modal
    var subAccountDetailsEle = $('#modal-subaccount-details')
    var subAccountDetailsModalBody = subAccountDetailsEle.find('#subAccountBody');
    subAccountDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var code = targetElement.attr('data-code');
        setSubAccountDetailsData(code, null, null);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#subAccountBody').find('table').dataTable().fnDestroy();
    });

    //Sub Account Detail Date Filter
    // Date range picker

    $('#table-filter-subAccountDetail-date').daterangepicker({
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
        $('#table-filter-subAccountDetail-date').val('');
        $(this).remove();
    })

    // Search Button
    $('#btn-search').on('click', function() {
        subAccountDetailTable.ajax.reload();
    })

    $("#excel_export_sub_account").on("click", function() {
        subAccountDetailTable.button("#export_excel_report").trigger();

    });

    // Customize Datatable
    $('#table-filter-subAcc-search').keyup(function() {
        var query = regex_escape($(this).val());
        subAccountDetailTable.search(query, true, false).draw();
    });
    $('#table-filter-subAcc-length').change(function() {
        subAccountDetailTable.page.len($(this).val()).draw();
    });
})

function setSubAccountDetailsData(code) {

    subAccountDetailTable = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength": 25,
        "ordering": false,
        "ajax": {
            url: myContextPath + "/costofsales/details/data",
            data: function(data) {
                data.fromDate = createdDate_min;
                data.toDate = createdDate_max;
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
            "name": "Invoice No",
            "data": "refInvoiceNo"
        }, {
            targets: 4,
            "className": "details-control",
            "name": "Account Name",
            "data": "accountName",

        }, {
            targets: 5,
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
            targets: 6,
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
            targets: 7,
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
            targets: 8,
            "className": "dt-right details-control",
            "name": "Closing Balance",
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
        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'SubAccountDetails_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'export_excel_report'
            },
            exportOptions: {
                columns: [0, 1, 2, 3, 4, 5, 6, 7, 8],
            },
            customize: function(xlsx) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                var downrows = 5;
                var clRow = $('row', sheet);
                //update Row
                clRow.each(function() {
                    var attr = $(this).attr('r');
                    var ind = parseInt(attr);
                    ind = ind + downrows;
                    $(this).attr("r", ind);
                });

                // Update  row > c
                $('row c ', sheet).each(function() {
                    var attr = $(this).attr('r');
                    var pre = attr.substring(0, 1);
                    var ind = parseInt(attr.substring(1, attr.length));
                    ind = ind + downrows;
                    $(this).attr("r", pre + ind);
                });

                function Addrow(index, data) {
                    msg = '<row r="' + index + '">'
                    for (i = 0; i < data.length; i++) {
                        var key = data[i].k;
                        var value = data[i].v;
                        msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
                        msg += '<is>';
                        msg += '<t>' + value + '</t>';
                        msg += '</is>';
                        msg += '</c>';
                    }
                    msg += '</row>';
                    return msg;
                }

                var d = new Date();

                //insert
                var r2 = Addrow(2, [{
                    k: 'A',
                    v: 'Company'
                }, {
                    k: 'B',
                    v: 'AA Japan'
                }]);
                0
                var r3 = Addrow(3, [{
                    k: 'A',
                    v: 'Title'
                }, {
                    k: 'B',
                    v: 'Sub Account Details'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear()
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }]
    })

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
    var balance = table.column(3, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="balance"]'))));
        return intVal(a) + amount;
    }, 0);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.totalBalance').autoNumeric('init').autoNumeric('set', balance);

}
