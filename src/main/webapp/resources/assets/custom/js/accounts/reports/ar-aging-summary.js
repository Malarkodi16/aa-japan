$(function() {
    var tableEle = $('#table-ar-aging-summary-report')
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": {
            url: myContextPath + "/accounts/report/ar-aging-summary-list"
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "customerName"
        }, {
            targets: 1,
            "data": "customerId"
        }, {
            targets: 2,
            "className": "dt-right",
            "data": "amountOutstanding",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="amountOutstanding" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 3,
            "className": "dt-right",
            "data": "current",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="current" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "className": "dt-right",
            "data": "aged30",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="aged30" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className": "dt-right",
            "data": "aged60",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="aged60" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className": "dt-right",
            "data": "aged90",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="aged90" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "dt-right",
            "data": "agedAbove90",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="amount" name="agedAbove90" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            tableEle.find('span.amount').autoNumeric('init')
        }
    })
     // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    })
})
function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
   
function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }

    // total Amount Outstanding
    var totalAmountOutstanding = table.column(2, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="amountOutstanding"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    
    //total Current Amount
    var totalCurrentAmount = table.column(3, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="current"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // total 1 To 30
    var total1To30 = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="aged30"]').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + amount;
    }, 0);

    // total 31 To 60
    var total31To60 = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="aged60"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // total 61 To 90
    var total61To90 = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="aged90"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // total Above 90
    var totalAbove90 = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="agedAbove90"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.totalAmountOutstanding').autoNumeric('init').autoNumeric('set', totalAmountOutstanding);

    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.totalCurrentAmount').autoNumeric('init').autoNumeric('set', totalCurrentAmount);

    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.total1To30').autoNumeric('init').autoNumeric('set', total1To30);

    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.total31To60').autoNumeric('init').autoNumeric('set', total31To60);

    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.total61To90').autoNumeric('init').autoNumeric('set', total61To90);

    $('#table-ar-aging-summary-report>tfoot>tr.totalFooter').find('span.totalAbove90').autoNumeric('init').autoNumeric('set', totalAbove90);

}
