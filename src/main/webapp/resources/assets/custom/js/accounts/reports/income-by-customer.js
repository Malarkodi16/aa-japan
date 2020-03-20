var supplierJson, table;
$(function() {
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

    var custId, date;
    $('select[name="custId"]').on('change', function() {
        custId = $(this).val();
        table.draw();
    });

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })

    var tableEle = $('#table-income-by-customer');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/income-by-customer-list",

        },
        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "customerName"

        }, {
            targets: 1,
            "className": "details-control",
            "data": "totalStock",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return row.stockDetails.length;
                }
            }
        }, {
            targets: 2,
            "className": "dt-right details-control",
            "data": "totalPurchasePrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="totalPurchasePrice" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 3,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalSellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="totalSellingPrice" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "className": "dt-right details-control",
            "data": "totalMargin",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="totalMargin" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "visible": false,
            "data": "customerId"

        }, {
            targets: 6,
            "visible": false,
            "data": "salesDate"

        }],
         "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-income-by-customer').find('input.autonumber,span.autonumber').autoNumeric('init')

        }

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

    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    var custName = $('#custId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //date filter
        if (typeof createdDate_min != 'undefined' && createdDate_min.length != '') {
            if (aData[6].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[6], 'DD-MM-YYYY')._d.getTime();
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
        //customer filter
        if (typeof custId != 'undefined' && custId.length != '') {
            if (aData[5].length == 0 || aData[5] != custId) {
                return false;
            }
        }

        return true;
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });
});
//Income report Format
function format(rowData) {
    var element = $('#clone-container>#stock-details>.clone-element').clone();
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');

    for (var i = 0; i < rowData.stockDetails.length; i++) {
        var row = $(rowClone).clone();

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.stockDetails[i].chassisNo, ''));
        $(row).find('td.salesDate').html(ifNotValid(rowData.stockDetails[i].salesDate, ''));
        $(row).find('td.purchasePrice span.autonumber').html(ifNotValid(rowData.stockDetails[i].purchasePrice, ''));
        $(row).find('td.sellingPrice span.autonumber').html(ifNotValid(rowData.stockDetails[i].sellingPrice, ''));
        $(row).find('td.margin span.autonumber').html(ifNotValid(rowData.stockDetails[i].margin, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }

    return element;

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
    var totalPurchasePrice = table.column(2, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="totalPurchasePrice"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var totalSellingPrice = table.column(3, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="totalSellingPrice"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var totalMargin = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="totalMargin"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-income-by-customer>tfoot>tr.sum').find('span.totalPurchasePrice').autoNumeric('init').autoNumeric('set', totalPurchasePrice);

    $('#table-income-by-customer>tfoot>tr.sum').find('span.totalSellingPrice').autoNumeric('init').autoNumeric('set', totalSellingPrice);

    $('#table-income-by-customer>tfoot>tr.sum').find('span.totalMargin').autoNumeric('init').autoNumeric('set', totalMargin);

}
