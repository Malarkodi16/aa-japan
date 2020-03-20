var bankJson;
var unClearedBalanceCheckVal;
$(function() {
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    })
    $('input[type="checkbox"][name="unClearedBalance"]').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        unClearedBalanceCheckVal = ifNotValid($('#unClearedBalanceCheck').is(':checked'), '');
        table.ajax.reload()
    }).on('ifUnchecked', function(e) {
        unClearedBalanceCheckVal = ifNotValid($('#unClearedBalanceCheck').is(':checked'), '');
        table.ajax.reload()
    })
    $.getJSON(myContextPath + "/data/bank.json", function(data) {
        bankJson = data;
        $('#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    //     $('#bank').change(function() {
    //         let val = $(this).val();
    //         if (!isEmpty(val)) {
    //             $('.bank-balance').removeClass('hidden');
    //             $.getJSON(myContextPath + "/bank/currentBankBalance?bankId=" + val + '&currency=' + 1, function(data) {
    //                 $('.bank-balance').find('.amount').autoNumeric('init').autoNumeric('set', data.data)
    //             });

    //         } else {
    //             $('.bank-balance').addClass('hidden').find('span.amount').autoNumeric('init').autoNumeric('set', 0);
    //         }
    //     });
    // Date range picker
    var createdDate_min;
    var createdDate_max;
    $('#table-filter-trailBalance-date').daterangepicker({
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

    })

    $("#excel_export_all").on("click", function() {
        $("#groupselect").attr("checked", false);
        $(".selectBox").attr("checked", false);
        //table.rows('.selected').deselect();
        table.button("#dt_excel_export_all").trigger();

    });

    //Bank Accounts Datatable
    var table = $('#table-report-trailBalance').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/bankTransaction/list/datasource",
            data: function(data) {
                data.bank = $('#bank').find('option:selected').val();
                data.fromDate = createdDate_min;
                data.toDate = createdDate_max;

            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "createdDate",

        }, {
            targets: 1,
            "className": "dt-right details-control",
            "data": "closingBalance",
            "render": function(data, type, row) {
                return '<span class="autonumber" name="closingBalance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 2,
            "className": "dt-right details-control",
            "name": "Debit",
            "render": function(data, type, row) {
                var returnDebitAmount;
                if (row.transactionType == 0) {
                    returnDebitAmount = '<span class="autonumber" name="debit" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
                } else {
                    returnDebitAmount = "";
                }
                return returnDebitAmount;
            }
        }, {
            targets: 3,
            "className": "dt-right details-control",
            "name": "Credit",
            "render": function(data, type, row) {
                var returnCreditAmount;
                if (row.transactionType == 1) {
                    returnCreditAmount = '<span class="autonumber" name="credit" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
                } else {
                    returnCreditAmount = "";
                }
                return returnCreditAmount;
            }
        }, {
            targets: 4,
            "className": "dt-right details-control",
            "name": "Balance",
            "data": "balance",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="balance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "visible": false,
            "data": "clearingAccount",

        }, {
            targets: 6,

            "data": "description",

        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-report-trailBalance').find('span.autonumber').autoNumeric('init')
        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.clearingAccount == "1") {
                $('td', nRow).css('background-color', '#fb8d59');
            }

        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var arr = jQuery.grep(bankJson, function(a) {
                    return $('#bank').find('option:selected').val() == a.bankSeq;
                });
                if (isEmpty(createdDate_min)) {
                    return arr[0].bankName
                } else {
                    return arr[0].bankName + '_' + ifNotValid(createdDate_min, '') + '-' + ifNotValid(createdDate_max, '');
                }
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0, 1, 2, 3, 4, 6]
            }
        }]

    });
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
    // Search Button
    $('#btn-search').on('click', function() {
        table.ajax.reload();
        $('#unClearedBalanceCheck').closest('.form-group').removeClass('hidden');
    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        if (typeof unClearedBalanceCheckVal != 'undefined' && unClearedBalanceCheckVal == true) {
            if (ifNotValid(aData[5], '0') == '0') {
                return false;
            }
        }
        return true;
    });

});

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // Capital Amount(JPY) Total

    var closingBalance = table.column(1, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="closingBalance"]'))));
        return intVal(a) + amount;
    }, 0);

    var debit = table.column(2, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        if (isEmpty($(b).find('span[name="debit"]').html())) {
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="debit"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var credit = table.column(3, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        if (isEmpty($(b).find('span[name="credit"]').html())) {
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="credit"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var balance = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="balance"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.openingBalanceTotal').autoNumeric('init').autoNumeric('set', closingBalance);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.debitTotal').autoNumeric('init').autoNumeric('set', debit);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.creditTotal').autoNumeric('init').autoNumeric('set', credit);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.closingBalanceTotal').autoNumeric('init').autoNumeric('set', balance);

}
