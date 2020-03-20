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
})
$('.select2-select').select2({
    allowClear: true,
    width: '100%',
})
var mcoaJson;
var filterAccountName;
var filterSubAccountName;

$.getJSON(myContextPath + "/data/accnameFilter.json", function(data) {
    mcoaJson = data;
    $('.sub-account').select2({
        allowClear: true,
        width: '100%',
        placeholder: 'All',
        data: $.map(mcoaJson, function(item) {
            return {
                id: item.code,
                text: item.subAccount + '(' + item.account + ')' + '(' + item.code + ')',
                data: item
            };
        })
    }).on("change", function(event) {
        filterSubAccountName = $(this).find('option:selected').val();
    })

});

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

//Customer Accounts Datatable
var table = $('#table-report-trailBalance').DataTable({
    "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength": 25,
    "ordering": true,
    "ajax": {
        url: myContextPath + "/accounts/report/trailBalanceTransaction/list/datasource",
        data: function(data) {
            data.fromDate = createdDate_min;
            data.toDate = createdDate_max;
            data.subAccount = filterSubAccountName;
        }
    },
    columnDefs: [{
        "targets": '_all',
        "defaultContent": ""
    }, {
        targets: 0,
        className: 'details-control',

        "data": "transactionId",

    }, {
        targets: 1,
        "className": "details-control",
        "name": "refInvoiceNo",
        "data": "refInvoiceNo",

    }, {
        targets: 2,
        "className": "details-control",
        "name": "Date",
        "data": "createdDate",

    }, {
        targets: 3,
        "className": "details-control",
        "name": "Code",
        "data": "code",

    }, {
        targets: 4,
        "className": "details-control",

        "data": "subAccount",

    }, {
        targets: 5,
        "className": "details-control",
        "data": "description"
    }, {
        targets: 6,
        "className": "dt-right details-control",
        "name": "Debit",
        "render": function(data, type, row) {
            var returnDebitAmount;
            if (row.type == 0) {
                returnDebitAmount = '<span class="autonumber" name="debit" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
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
                returnCreditAmount = '<span class="autonumber" name="credit" data-a-sign="¥ " data-m-dec="0">' + row.amount + '</span>';
            } else {
                returnCreditAmount = "";
            }
            return returnCreditAmount;
        }
    }, {
        targets: 8,
        "className": "dt-right details-control",
        "name": "Balance",
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
            return 'GL_TransactionReport_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
        },
        attr: {
            type: "button",
            id: 'dt_excel_export_all'
        },
        exportOptions: {
            columns: [0, 1, 2, 3, 4, 5, 6, 7, 8]
        }
    }]

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
    var debit = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        if(isEmpty($(b).find('span[name="debit"]').html())){
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="debit"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var credit = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
         if(isEmpty($(b).find('span[name="credit"]').html())){
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="credit"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var balance = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="balance"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.debitTotal').autoNumeric('init').autoNumeric('set', debit);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.creditTotal').autoNumeric('init').autoNumeric('set', credit);

    $('#table-report-trailBalance>tfoot>tr.sum').find('span.balanceTotal').autoNumeric('init').autoNumeric('set', balance);

}
