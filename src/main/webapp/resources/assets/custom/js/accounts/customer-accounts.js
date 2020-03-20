/* Customer Id Select2 Filter */

$('#custId').select2({
    allowClear: true,
    minimumInputLength: 2,
    ajax: {
        url: myContextPath + "/customer/admin/search",
        dataType: 'json',
        delay: 500,
        data: function(params) {
            var query = {
                search: params.term,
                type: 'public'
            }
            return query;

        },
        processResults: function(data) {
            var results = [];
            data = data.data;
            if (data != null && data.length > 0) {
                $.each(data, function(index, item) {
                    results.push({
                        id: item.code,
                        text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                    });
                });
            }
            return {
                results: results
            }

        }

    }
});

//get sales person list

$.getJSON(myContextPath + "/user/getRoleSales", function(data) {
    var salesJson = data;
    $('#select_sales_staff').select2({
        allowClear: true,
        width: '100%',
        data: $.map(salesJson, function(item) {
            var staff = item.username + (item.userId)
            return {
                id: item.username,
                text: item.user.fullname + ' ' + '( ' + item.userId + ' )'
            };
        })
    }).val('').trigger("change");
})

// date pickers

$('#table-filter-customer-date').daterangepicker({
    autoUpdateInput: false
}).on("apply.daterangepicker", function(e, picker) {
    createdDate_min = picker.startDate;
    createdDate_max = picker.endDate;
    picker.element.val(createdDate_min.format('DD-MM-YYYY') + ' - ' + createdDate_max.format('DD-MM-YYYY'));
    createdDate_min = createdDate_min._d.getTime();
    createdDate_max = createdDate_max._d.getTime();
    $('<div>', {
        'class': 'input-group-addon clear-date'
    }).append($('<i>', {
        'class': 'fa fa-times'
    })).appendTo($(this).closest('.input-group'))
    table.draw();
});
$('#date-form-group').on('click', '.clear-date', function() {
    createdDate_min = '';
    createdDate_max = '';
    table.draw();
    $('#table-filter-customer-date').val('');
    $(this).remove();

});

// Customer Accounts Datatable

var table = $('#table-customerAccounts-transactions').DataTable({
    "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength" : 25,
    "ajax": {
        url: myContextPath + "/accounts/customer-accounts/list/datasource",
        data: function(data) {
            data.custId = $('#searchCondition').find('select[name="custId"]').val();
            data.staff = $('#searchCondition').find('select[name="staff"]').val();
        },
    },
    columnDefs: [{
        "targets": '_all',
        "defaultContent": ""
    }, {
        targets: 0,
        className: 'details-control',
        "name": "Stock No",
        "data": "stockNo",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            if (type === 'display') {

                return '<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>'
            }
            return data;
        }
    }, {
        targets: 1,
        "className": "details-control",
        "name": "Invoice No",
        "data": "invoiceNo",

    }, {
        targets: 2,
        "className": "details-control",
        "name": "Date",
        "data": "date"
    }, {
        targets: 3,
        "className": "details-control",
        "name": "Chassis No",
        "data": "chassisNo",

    }, {
        targets: 4,
        "className": "dt-right details-control",
        "name": "Lc Amount",
        "visible":false,
        "data": "lcAmount",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
        }
    }, {
        targets: 5,
        "className": "dt-right details-control",
        "name": "Invoice Amount",
        "data": "invoiceAmount",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            return '<span class="autonumber" name="invoiceAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
        }
    }, {
        targets: 6,
        "className": "dt-right details-control",
        "name": "Amount Received",
        "data": "amountReceived",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            return '<span class="autonumber" name="amountReceived" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
        }
    }, {
        targets: 7,
        "className": "dt-right details-control",
        "name": "Balance",
        "data": "balance",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            return '<span class="autonumber" name="balance" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
        }
    }, {
        targets: 8,
        "className": "details-control",
        "name": "LC Number",
         "visible":false,
        "data": "lcNo",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            if (type === 'display') {
                return '<span class="autonumber" data-m-dec="0">' + data + '</span>';
            }
        }
    }, {
        targets: 9,
        "className": "details-control",
        "name": "Customer",
        "data": "customerName"
    }, {
        targets: 10,
        "className": "details-control",
        "name": "Sales Person",
        "data": "salesPersonName"
    }, {
        targets: 11,
        "className": "details-control",
        "data": "customerId",
        "visible": false
    }, {
        targets: 12,
        "className": "details-control",
        "data": "createdBy",
        "visible": false
    }],
    "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
    "drawCallback": function(settings, json) {
        $('#table-customerAccounts-transactions').find('span.autonumber').autoNumeric('init')
    },

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


//stock details modal update
var stockDetailsModal = $('#modal-stock-details');
var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
var stockCloneElement = $('#stock-details-html>.stock-details');
stockDetailsModalBodyDiv.slimScroll({
    start: 'bottom',
    height: ''
});
stockDetailsModal.on('show.bs.modal', function(e) {
    if (e.namespace != 'bs.modal') {
        return;
    }
    var targetElement = $(e.relatedTarget);
    var stockNo = targetElement.attr('data-stockNo');
    stockCloneElement.clone().appendTo(stockDetailsModalBody);
    //updateStockDetailsData
    updateStockDetailsData(stockDetailsModal, stockNo)
}).on('hidden.bs.modal', function() {
    stockDetailsModalBody.html('');
})

// search condition and draw
var custId, date, staff;
$('select[name="custId"]').on('change', function() {
    custId = $(this).val();
    table.draw();
});
$('select[name="staff"]').on('change', function() {
    staff = $(this).val();
    table.draw();
});
//DataTable Filter
$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    //date filter
    if (typeof createdDate_min != 'undefined' && createdDate_min.length != '') {
        if (aData[2].length == 0) {
            return false;
        }
        if (typeof aData._date == 'undefined') {
            aData._date = moment(aData[2], 'DD-MM-YYYY')._d.getTime();
        }
        if (createdDate_min && !isNaN(createdDate_min)) {
            if (aData._date < createdDate_min) {
                return false;
            }
        }
        if (createdDate_max && !isNaN(createdDate_max)) {
            if (aData._date > createdDate_max) {
                return false;
            }
        }

    }
    //custId filter
    if (typeof custId != 'undefined' && custId.length != '') {
        if (aData[11].length == 0 || aData[11] != custId) {
            return false;
        }
    }
    if (typeof staff != 'undefined' && staff.length != '') {
        if (aData[12].length == 0 || aData[12] != staff) {
            return false;
        }
    }
    return true;
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
    var invoiceAmount = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="invoiceAmount"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var amountReceived = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="amountReceived"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var balance = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="balance"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-customerAccounts-transactions>tfoot>tr.sum').find('span.invoiceAmountTotal').autoNumeric('init').autoNumeric('set', invoiceAmount);

    $('#table-customerAccounts-transactions>tfoot>tr.sum').find('span.amountReceivedTotal').autoNumeric('init').autoNumeric('set', amountReceived);

    $('#table-customerAccounts-transactions>tfoot>tr.sum').find('span.balanceAmountTotal').autoNumeric('init').autoNumeric('set', balance);

}