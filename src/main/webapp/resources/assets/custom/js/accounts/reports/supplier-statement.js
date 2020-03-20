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
$.getJSON(myContextPath + "/data/suppliers.json", function(data) {
    $('select#supplier-filter').select2({
        allowClear: true,
        width: '100%',
        data: $.map(data, function(item) {
            return {
                id: item.supplierCode,
                text: item.company
            };
        })
    })

});

// Date range picker
var createdDate_min;
var createdDate_max;
$('#table-filter-transaction-date').daterangepicker({
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

//Customer Accounts Datatable
var table = $('#table-report-supplierStatement').DataTable({
    "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength" : 25,
    "ordering": true,
    "ajax": {
        url: myContextPath + "/accounts/report/supplierStatement/datasource",
        data: function(data) {
            data.fromDate = createdDate_min;
            data.toDate = createdDate_max;
            data.supplier = $('#supplier-filter').val();
        },
        "dataSrc": function(json) {
            if (json.data.length > 0) {
                $('#opening-balance').html('');
                setAutonumericValue($('#opening-balance'), json.data[0].closingBalance)
            } else {
                $('#opening-balance').html('N/A')
            }
            return json.data;
        }
    },
    columnDefs: [{
        "targets": '_all',
        "defaultContent": ""
    }, {
        targets: 0,
        className: 'details-control',
        "data": "date"

    }, {
        targets: 1,
        "className": "details-control",
        "data": "invoiceNo",

    }, {
        targets: 2,
        "className": "details-control",
        "data": "description",

    }, {
        targets: 3,
        "className": "dt-right details-control",
        "data": "amount",
        "render": function(data, type, row) {
            var returnDebitAmount;
            if (row.transactionType == 0) {
                returnDebitAmount = '<span class="autonumber" name="debit" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            } else {
                returnDebitAmount = "";
            }
            return returnDebitAmount;
        }
    }, {
        targets: 4,
        "data": "amount",
        "className": "dt-right details-control",
        "render": function(data, type, row) {
            var returnCreditAmount;
            if (row.transactionType == 1) {
                returnCreditAmount = '<span class="autonumber" name="credit" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            } else {
                returnCreditAmount = "";
            }
            return returnCreditAmount;
        }
    }, {
        targets: 5,
        "className": "dt-right details-control",
        "data": "balance",
        "render": function(data, type, row) {
            return '<span class="autonumber" name="balance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
        }

    }],
    "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
    "drawCallback": function(settings, json) {
        $('#table-report-supplierStatement').find('span.autonumber').autoNumeric('init')
    },

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
    var debit = table.column(3, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        if(isEmpty($(b).find('span[name="debit"]').html())){
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="debit"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var credit = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
         if(isEmpty($(b).find('span[name="credit"]').html())){
            return intVal(a);
        }
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="credit"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var balance = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="balance"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-report-supplierStatement>tfoot>tr.sum').find('span.debitTotal').autoNumeric('init').autoNumeric('set', debit);

    $('#table-report-supplierStatement>tfoot>tr.sum').find('span.creditTotal').autoNumeric('init').autoNumeric('set', credit);

    $('#table-report-supplierStatement>tfoot>tr.sum').find('span.balanceTotal').autoNumeric('init').autoNumeric('set', balance);

}
